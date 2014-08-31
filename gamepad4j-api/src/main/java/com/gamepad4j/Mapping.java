/*
 * @Copyright: Marcel Schoen, Switzerland, 2014, All Rights Reserved.
 */

package com.gamepad4j;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Handles mappings for numerical button and axis index values to meaningful constants.
 * The mappings are defined in properties read from files, classpath resources etc.
 *
 * @author Marcel Schoen
 * @version $Revision: $
 */
public class Mapping {

	/** Stores the buttonID to code mapping, for each device type. */
	private static Map<Long, Map<Integer, ButtonID>> buttonIdMap = new HashMap<Long, Map<Integer, ButtonID>>();

	/** Stores the code to buttonID mapping, for each device type. */
	private static Map<Long, Map<ButtonID, Integer>> buttonCodeMap = new HashMap<Long, Map<ButtonID, Integer>>();

	/** Stores the default label for each buttong of each device type. */
	private static Map<Long, Map<ButtonID, String>> defaultLabelMap = new HashMap<Long, Map<ButtonID, String>>();

	/** Stores button ID aliases. */
	private static Map<Long, Map<ButtonID, ButtonID>> aliasMap = new HashMap<Long, Map<ButtonID, ButtonID>>();
	
	/** Stores the label key for each buttong of each device type. */
	private static Map<Long, Map<ButtonID, String>> labelKeyMap = new HashMap<Long, Map<ButtonID, String>>();

	/** Stores the default button text labels. */
	private static Properties defaultLabels = new Properties();
	
	/**
	 * Initializes the mappings from the resource properties.
	 */
	public static void initializeFromResources() {
		try {
			// Read the default text labels
			InputStream in = Mapping.class.getResourceAsStream("/mappings/default-labels.properties");
			defaultLabels.load(in);
			in.close();
			
			// Read the mappings for the various pads
			in = Mapping.class.getResourceAsStream("/mappings/mapping-files.properties");
			Properties fileListProps = new Properties();
			fileListProps.load(in);
			for(Object fileListName : fileListProps.values()) {
				String propertyFileName = (String)fileListName;
				System.out.println("> processing mapping file: " + propertyFileName);
				InputStream propIn = Mapping.class.getResourceAsStream(propertyFileName);
				Properties mappingProps = new Properties();
				mappingProps.load(propIn);
				addMappings(mappingProps);
			}
			in.close();
		} catch(Exception ex) {
			ex.printStackTrace();
			throw new IllegalStateException("Failed to process mappings from resources: " + ex);
		}
	}
	
	/**
	 * Adds mappings from properties. Each property consists of a key defining
	 * what type of element (button, stick) with which ID is mapped to which
	 * numerical value(s). Example:
	 * 
	 * button.FACE_UP=13
	 * button.HOME=1,7,8
	 * 
	 * axis.TRIGGER1_LEFT=4
	 * axis.TRIGGER1_RIGHT=5
	 * 
	 * axis.LEFT_ANALOG.X=0
	 * axis.LEFT_ANALOG.Y=1
	 * axis.RIGHT_ANALOG.X=2
	 * axis.RIGHT_ANALOG.Y=3
	 * 
	 * buttonlabel.FACE_UP=Y
	 * buttonlabel.FACE_DOWN=O
	 * 
	 * buttonlabelkey.FACE_UP=ouya.controller.facebutton.up
	 * buttonlabelkey.FACE_DOWN=ouya.controller.facebutton.down
	 * 
	 * @param properties
	 */
	public static void addMappings(Properties properties) {
		
		int vendorID = -1, productID = -1;
		try {
			vendorID = Integer.parseInt(properties.getProperty("vendor.id", ""), 16);
			productID = Integer.parseInt(properties.getProperty("product.id", ""), 16);
		} catch(NumberFormatException ex) {
			// ignore
			ex.printStackTrace();
		}
		if(vendorID == -1) {
			throw new IllegalArgumentException("Invalid/missing vendor ID propery ('vendor.id') in mapping.");
		}
		if(productID == -1) {
			throw new IllegalArgumentException("Invalid/missing product ID propery ('product.id') in mapping.");
		}
		long deviceTypeIdentifier = (vendorID << 16) + productID;
		if(buttonIdMap.get(deviceTypeIdentifier) == null) {
			aliasMap.put(deviceTypeIdentifier, new HashMap<ButtonID, ButtonID>());
			buttonIdMap.put(deviceTypeIdentifier, new HashMap<Integer, ButtonID>());
			buttonCodeMap.put(deviceTypeIdentifier, new HashMap<ButtonID, Integer>());
			defaultLabelMap.put(deviceTypeIdentifier, new HashMap<ButtonID, String>());
			labelKeyMap.put(deviceTypeIdentifier, new HashMap<ButtonID, String>());
		}

		// 1st run / define mappings
		Enumeration<Object> keys = properties.keys();
		while(keys.hasMoreElements()) {
			String key = (String)keys.nextElement();
			String value = properties.getProperty(key);
			if(key.startsWith("button.")) {
				addButtonMapping(deviceTypeIdentifier, key, value);
			} else if(key.startsWith("axis.")) {
				addAxisMapping(deviceTypeIdentifier, key, value);
			} else if(key.startsWith("buttonlabel.")) {
				defaultLabelMap.get(deviceTypeIdentifier).put(getButtonIDfromPropertyKey(key), value);
			} else if(key.startsWith("buttonlabelkey.")) {
				labelKeyMap.get(deviceTypeIdentifier).put(getButtonIDfromPropertyKey(key), value);
			}
		}
		// 2nd run / set aliases
		keys = properties.keys();
		while(keys.hasMoreElements()) {
			String key = (String)keys.nextElement();
			String value = properties.getProperty(key);
			if(key.startsWith("button.")) {
				addButtonAliasMapping(deviceTypeIdentifier, key, value);
			}
		}
	}

	/**
	 * @param key
	 */
	public static ButtonID getButtonIDfromPropertyKey(String key) {
		String buttonIDvalue = key.substring(key.indexOf(".") + 1);
		ButtonID id = ButtonID.getButtonIDfromString(buttonIDvalue);
		if(id == null) {
			throw new IllegalArgumentException("Invalid button ID in property key '" + key + "'");
		}
		return id;
	}

	/**
	 * Processes / creates the mappings for digital buttons.
	 *  
	 * @param deviceIdentifier The controller type identifier.
	 * @param key The mapping property key.
	 * @param value The mapping property value.
	 */
	private static void addButtonMapping(long deviceIdentifier, String key, String value) {
		ButtonID buttonID = getButtonIDfromPropertyKey(key);
		if(buttonID != null) {
			ButtonID aliasID = ButtonID.getButtonIDfromString(value);
			if(aliasID == null) {
				try {
					int code = Integer.parseInt(value);
					buttonIdMap.get(deviceIdentifier).put(code, buttonID);
					buttonCodeMap.get(deviceIdentifier).put(buttonID, code);
				} catch(NumberFormatException ex) {
					throw new IllegalArgumentException("Invalid numerical button code '" + value 
							+ "' in property '" + key + "' in mapping.");
				}
			}
		} else {
			throw new IllegalArgumentException("Invalid/unknown button ID in property '" + key + "' in mapping.");
		}
	}

	/**
	 * Processes / creates the aliaes for already defined mappings.
	 *  
	 * @param deviceIdentifier The controller type identifier.
	 * @param key The mapping property key.
	 * @param value The mapping property value.
	 */
	private static void addButtonAliasMapping(long deviceIdentifier, String key, String value) {
		ButtonID buttonID = getButtonIDfromPropertyKey(key);
		if(buttonID != null) {
			// 2nd run: Set aliases for predefined mappings
			ButtonID aliasID = ButtonID.getButtonIDfromString(value);
			if(aliasID != null) {
				try {
					aliasMap.get(deviceIdentifier).put(buttonID, aliasID);
				} catch(Exception ex) {
					throw new IllegalArgumentException("Invalid/unknown button alias '" + value + "' in mapping.");
				}
			}
		} else {
			throw new IllegalArgumentException("Invalid/unknown button ID in property '" + key + "' in mapping.");
		}
	}
	
	/**
	 * Processes / creates the mappings for analog axes.
	 *  
	 * @param deviceIdentifier The controller type identifier.
	 * @param key The mapping property key.
	 * @param value The mapping property value.
	 */
	private static void addAxisMapping(long deviceIdentifier, String key, String value) {
		// TODO
	}
	
	/**
	 * Returns the default text for the label for the given button.
	 * 
	 * @param controller The controller to which the button belongs.
	 * @param buttonID The ID of the button.
	 * @return The default text, or null, if none was defined.
	 */
	public static String getButtonLabel(IController controller, ButtonID buttonID) {
		return defaultLabelMap.get(controller.getDeviceTypeIdentifier()).get(buttonID);
	}
	
	/**
	 * Returns the resource key for the label for the given button.
	 * 
	 * @param controller The controller to which the button belongs.
	 * @param buttonID The ID of the button.
	 * @return The resource key, or null, if none was defined.
	 */
	public static String getButtonLabelKey(IController controller, ButtonID buttonID) {
		return labelKeyMap.get(controller.getDeviceTypeIdentifier()).get(buttonID);
	}
	
	/**
	 * Returns the alias butotn ID for the given button.
	 * 
	 * @param controller The controller to which the button belongs.
	 * @param buttonID The ID of the button.
	 * @return The alias button ID, or null, if none was defined.
	 */
	public static ButtonID getAliasID(IController controller, ButtonID buttonID) {
		return aliasMap.get(controller.getDeviceTypeIdentifier()).get(buttonID);
	}
	
	/**
	 * Returns the ID for a certain button, if one was defined.
	 * 
	 * @param controller The controller to which the button belongs.
	 * @param button The numerical code value of the button.
	 * @return The mapped ID, or "ButtonID.UNKNOWN".
	 */
	public static ButtonID getMappedID(IController controller, int button) {
		Map<Integer, ButtonID> IDmap = buttonIdMap.get(controller.getDeviceTypeIdentifier());
		if(IDmap != null) {
			ButtonID id = IDmap.get(button);
			if(id != null) {
				return id;
			}
		}
		return ButtonID.UNKNOWN;
	}
	
	/**
	 * Returns the default text label for the given button.
	 * 
	 * @param buttonID The ID of the button.
	 * @return The default label, or null.
	 */
	public static String getDefaultLabel(ButtonID buttonID) {
		return defaultLabels.getProperty("buttonlabel." + buttonID.name());
	}
}
