/*
 * @Copyright: Marcel Schoen, Switzerland, 2014, All Rights Reserved.
 */

package org.gamepad4j;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.gamepad4j.util.Log;

/**
 * Handles mappings for numerical button and axis code values to meaningful constants.
 * The mappings are defined in properties read from files, classpath resources etc.
 *
 * @author Marcel Schoen
 * @version $Revision: $
 */
public class Mapping {

	/** Stores the buttonID to code mapping, for each device type. */
	private static Map<Long, Map<Integer, ButtonID>> digitalButtonIdMap = new HashMap<Long, Map<Integer, ButtonID>>();

	/** Stores the code to buttonID mapping, for each device type. */
	private static Map<Long, Map<ButtonID, Integer>> digitalButtonCodeMap = new HashMap<Long, Map<ButtonID, Integer>>();

	/** Stores the TriggerID to code mapping, for each device type. */
	private static Map<Long, Map<Integer, TriggerID>> triggerIdMap = new HashMap<Long, Map<Integer, TriggerID>>();

	/** Stores the code to TriggerID mapping, for each device type. */
	private static Map<Long, Map<TriggerID, Integer>> triggerCodeMap = new HashMap<Long, Map<TriggerID, Integer>>();

	/** Stores the default label for each button of each device type. */
	private static Map<Long, Map<ButtonID, String>> defaultButtonLabelMap = new HashMap<Long, Map<ButtonID, String>>();

	/** Stores the default label for each trigger of each device type. */
	private static Map<Long, Map<TriggerID, String>> defaultTriggerLabelMap = new HashMap<Long, Map<TriggerID, String>>();

	/** Stores button ID aliases. */
	private static Map<Long, Map<ButtonID, ButtonID>> aliasMap = new HashMap<Long, Map<ButtonID, ButtonID>>();
	
	/** Stores the label key for each button of each device type. */
	private static Map<Long, Map<ButtonID, String>> buttonLabelKeyMap = new HashMap<Long, Map<ButtonID, String>>();
	
	/** Stores the label key for each trigger of each device type. */
	private static Map<Long, Map<TriggerID, String>> triggerLabelKeyMap = new HashMap<Long, Map<TriggerID, String>>();

	/** Stores the default button text labels. */
	private static Properties defaultLabels = new Properties();

	private static Map<Long, Map<Integer, StickID>> stickAxisIdMap = new HashMap<Long, Map<Integer, StickID>>();

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
				Log.log("> processing mapping file: " + propertyFileName);
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
		
		addButtonMappings(properties, deviceTypeIdentifier);
		addTriggerMappings(properties, deviceTypeIdentifier);
		addStickMappings(properties, deviceTypeIdentifier);
	}
	
	/**
	 * Adds mappings for all triggers.
	 * 
	 * @param properties The configuration properties.
	 * @param deviceTypeIdentifier The controller device identifier.
	 */
	public static void addTriggerMappings(Properties properties, long deviceTypeIdentifier) {
		if(triggerIdMap.get(deviceTypeIdentifier) == null) {
			triggerIdMap.put(deviceTypeIdentifier, new HashMap<Integer, TriggerID>());
			triggerCodeMap.put(deviceTypeIdentifier, new HashMap<TriggerID, Integer>());
		}
		
		Enumeration<Object> keys = properties.keys();
		while(keys.hasMoreElements()) {
			String key = (String)keys.nextElement();
			String value = properties.getProperty(key);
			if(key.startsWith("axis.")) {
				String idPart = key.substring(key.indexOf(".") + 1);
				if(idPart.startsWith("TRIGGER")) {
					// add mapping for analog trigger button
					addTriggerMapping(deviceTypeIdentifier, key, value);
				}
			}
		}
	}
	
	/**
	 * Adds mappings for all sticks.
	 * 
	 * @param properties The configuration properties.
	 * @param deviceTypeIdentifier The controller device identifier.
	 */
	public static void addStickMappings(Properties properties, long deviceTypeIdentifier) {
		if(stickAxisIdMap.get(deviceTypeIdentifier) == null) {
			stickAxisIdMap.put(deviceTypeIdentifier, new HashMap<Integer, StickID>());
		}
		
		Enumeration<Object> keys = properties.keys();
		while(keys.hasMoreElements()) {
			String key = (String)keys.nextElement();
			String value = properties.getProperty(key);
			if(key.startsWith("axis.")) {
				String idPart = key.substring(key.indexOf(".") + 1);
				if(!idPart.startsWith("TRIGGER")) {
					// add mapping for analog stick
					addAnalogStickAxisMapping(deviceTypeIdentifier, key, value);
				}
			}
		}
	}

	
	/**
	 * Adds mappings for all digital buttons.
	 * 
	 * @param properties The configuration properties.
	 * @param deviceTypeIdentifier The controller device identifier.
	 */
	public static void addButtonMappings(Properties properties, long deviceTypeIdentifier) {
		if(digitalButtonIdMap.get(deviceTypeIdentifier) == null) {
			aliasMap.put(deviceTypeIdentifier, new HashMap<ButtonID, ButtonID>());
			digitalButtonIdMap.put(deviceTypeIdentifier, new HashMap<Integer, ButtonID>());
			digitalButtonCodeMap.put(deviceTypeIdentifier, new HashMap<ButtonID, Integer>());
			defaultButtonLabelMap.put(deviceTypeIdentifier, new HashMap<ButtonID, String>());
			buttonLabelKeyMap.put(deviceTypeIdentifier, new HashMap<ButtonID, String>());
			triggerLabelKeyMap.put(deviceTypeIdentifier, new HashMap<TriggerID, String>());
			defaultTriggerLabelMap.put(deviceTypeIdentifier, new HashMap<TriggerID, String>());
		}

		// 1st run / define mappings
		Enumeration<Object> keys = properties.keys();
		while(keys.hasMoreElements()) {
			String key = (String)keys.nextElement();
			String value = properties.getProperty(key);
			if(key.startsWith("button.")) {
				addButtonMapping(deviceTypeIdentifier, key, value);
			} else if(key.startsWith("buttonlabel.")) {
				defaultButtonLabelMap.get(deviceTypeIdentifier).put(getButtonIDfromPropertyKey(key), value);
			} else if(key.startsWith("triggerlabel.")) {
				defaultTriggerLabelMap.get(deviceTypeIdentifier).put(getTriggerIDfromPropertyKey(key), value);
			} else if(key.startsWith("buttonlabelkey.")) {
				buttonLabelKeyMap.get(deviceTypeIdentifier).put(getButtonIDfromPropertyKey(key), value);
			} else if(key.startsWith("triggerlabelkey.")) {
				triggerLabelKeyMap.get(deviceTypeIdentifier).put(getTriggerIDfromPropertyKey(key), value);
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
	 * @param key
	 */
	public static TriggerID getTriggerIDfromPropertyKey(String key) {
		String triggerIDvalue = key.substring(key.indexOf(".") + 1);
		TriggerID id = TriggerID.getTriggerIDfromString(triggerIDvalue);
		if(id == null) {
			throw new IllegalArgumentException("Invalid trigger ID in property key '" + key + "'");
		}
		return id;
	}

	/**
	 * @param key
	 */
	public static StickID getStickIDfromPropertyKey(String key) {
		String stickIDvalue = key.substring(key.indexOf(".") + 1, key.lastIndexOf("."));
		StickID id = StickID.getStickIDfromString(stickIDvalue);
		if(id == null) {
			throw new IllegalArgumentException("Invalid stick ID '" + stickIDvalue + "' in property key '" + key + "'");
		}
		return id;
	}

	/**
	 * Processes / creates the mappings for analog sticks (or d-pads).
	 *  
	 * @param deviceIdentifier The controller type identifier.
	 * @param key The mapping property key.
	 * @param value The mapping property value.
	 */
	private static void addAnalogStickAxisMapping(long deviceIdentifier, String key, String value) {
		StickID stickID = getStickIDfromPropertyKey(key);
		if(stickID != null) {
			StickID aliasID = StickID.getStickIDfromString(value);
			if(aliasID == null) {
				try {
					int code = Integer.parseInt(value);
					stickAxisIdMap.get(deviceIdentifier).put(code, stickID);
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
	 * Processes / creates the mappings for trigger buttons.
	 *  
	 * @param deviceIdentifier The controller type identifier.
	 * @param key The mapping property key.
	 * @param value The mapping property value.
	 */
	private static void addTriggerMapping(long deviceIdentifier, String key, String value) {
		TriggerID triggerID = getTriggerIDfromPropertyKey(key);
		if(triggerID != null) {
			TriggerID aliasID = TriggerID.getTriggerIDfromString(value);
			if(aliasID == null) {
				try {
					int code = Integer.parseInt(value);
					triggerIdMap.get(deviceIdentifier).put(code, triggerID);
					triggerCodeMap.get(deviceIdentifier).put(triggerID, code);
				} catch(NumberFormatException ex) {
					throw new IllegalArgumentException("Invalid numerical trigger button code '" + value 
							+ "' in property '" + key + "' in mapping.");
				}
			}
		} else {
			throw new IllegalArgumentException("Invalid/unknown trigger button ID in property '" + key + "' in mapping.");
		}
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
					digitalButtonIdMap.get(deviceIdentifier).put(code, buttonID);
					digitalButtonCodeMap.get(deviceIdentifier).put(buttonID, code);
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
	 * Returns the default text for the label for the given button.
	 * 
	 * @param controller The controller to which the button belongs.
	 * @param buttonID The ID of the button.
	 * @return The default text, or null, if none was defined.
	 */
	public static String getButtonLabel(IController controller, ButtonID buttonID) {
		return defaultButtonLabelMap.get(controller.getDeviceTypeIdentifier()).get(buttonID);
	}
	
	/**
	 * Returns the resource key for the label for the given button.
	 * 
	 * @param controller The controller to which the button belongs.
	 * @param buttonID The ID of the button.
	 * @return The resource key, or null, if none was defined.
	 */
	public static String getButtonLabelKey(IController controller, ButtonID buttonID) {
		return buttonLabelKeyMap.get(controller.getDeviceTypeIdentifier()).get(buttonID);
	}
	
	/**
	 * Returns the alias butotn ID for the given button.
	 * 
	 * @param controller The controller to which the button belongs.
	 * @param buttonID The ID of the button.
	 * @return The alias button ID, or null, if none was defined.
	 */
	public static ButtonID getAliasID(IController controller, ButtonID buttonID) {
		Map<ButtonID, ButtonID> aliasIDmap = aliasMap.get(controller.getDeviceTypeIdentifier());
		if(aliasIDmap != null) {
			return aliasIDmap.get(buttonID);
		}
		return null;
	}
	
	/**
	 * Returns the ID for a certain digital button, if one was defined.
	 * 
	 * @param controller The controller to which the button belongs.
	 * @param button The numerical code value of the button.
	 * @return The mapped ID, or "ButtonID.UNKNOWN".
	 */
	public static ButtonID getMappedButtonID(IController controller, int button) {
		Map<Integer, ButtonID> IDmap = digitalButtonIdMap.get(controller.getDeviceTypeIdentifier());
		if(IDmap != null) {
			ButtonID id = IDmap.get(button);
			if(id != null) {
				return id;
			}
		}
		return ButtonID.UNKNOWN;
	}
	
	/**
	 * Returns the default text for the label for the given trigger.
	 * 
	 * @param controller The controller to which the trigger belongs.
	 * @param triggerID The ID of the trigger.
	 * @return The default text, or null, if none was defined.
	 */
	public static String getTriggerLabel(IController controller, TriggerID triggerID) {
		return defaultTriggerLabelMap.get(controller.getDeviceTypeIdentifier()).get(triggerID);
	}
	
	/**
	 * Returns the resource key for the label for the given trigger.
	 * 
	 * @param controller The controller to which the trigger belongs.
	 * @param triggerID The ID of the trigger.
	 * @return The resource key, or null, if none was defined.
	 */
	public static String getTriggerLabelKey(IController controller, TriggerID triggerID) {
		return triggerLabelKeyMap.get(controller.getDeviceTypeIdentifier()).get(triggerID);
	}
	
	/**
	 * Returns the ID for a certain trigger, if one was defined.
	 * 
	 * @param controller The controller to which the trigger button belongs.
	 * @param triggerCode The numerical code value of the trigger button.
	 * @return The mapped ID, or "TriggerID.UNKNOWN".
	 */
	public static TriggerID getMappedTriggerID(IController controller, int triggerCode) {
		Map<Integer, TriggerID> IDmap = triggerIdMap.get(controller.getDeviceTypeIdentifier());
		if(IDmap != null) {
			TriggerID id = IDmap.get(triggerCode);
			if(id != null) {
				return id;
			}
		}
		return TriggerID.UNKNOWN;
	}
	
	/**
	 * Returns the ID for a certain stick, if one was defined.
	 * 
	 * @param controller The controller to which the stick belongs.
	 * @param stick The numerical code value of the stick.
	 * @return The mapped ID, or "StickID.UNKNOWN".
	 */
	public static StickID getMappedStickID(IController controller, int stick) {
		Map<Integer, StickID> IDmap = stickAxisIdMap.get(controller.getDeviceTypeIdentifier());
		if(IDmap != null) {
			StickID id = IDmap.get(stick);
			if(id != null) {
				return id;
			}
		}
		return StickID.UNKNOWN;
	}
	
	/**
	 * Returns the default text label for the given trigger.
	 * 
	 * @param triggerID The ID of the trigger.
	 * @return The default label, or null.
	 */
	public static String getDefaultTriggerLabel(TriggerID triggerID) {
		return defaultLabels.getProperty("triggerlabel." + triggerID.name());
	}
	
	/**
	 * Returns the default text label for the given button.
	 * 
	 * @param buttonID The ID of the button.
	 * @return The default label, or null.
	 */
	public static String getDefaultButtonLabel(ButtonID buttonID) {
		return defaultLabels.getProperty("buttonlabel." + buttonID.name());
	}
}
