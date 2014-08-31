/*
 * @Copyright: Marcel Schoen, Switzerland, 2013, All Rights Reserved.
 */

package com.gamepad4j.base;

import java.util.HashMap;
import java.util.Map;

import com.gamepad4j.ButtonID;
import com.gamepad4j.DpadDirection;
import com.gamepad4j.IButton;
import com.gamepad4j.IController;
import com.gamepad4j.Mapping;

/**
 * Base class for controller wrappers.
 *
 * @author Marcel Schoen
 * @version $Revision: $
 */
public abstract class AbstractBaseController implements IController {

	/** Stores the deviceID of this controller. */
	protected int deviceID = -1;

	/** Stores the vendorID of this controller. */
	protected int vendorID = -1;

	/** Stores the productID of this controller. */
	protected int productID = -1;

	/** Stores the description of this controller. */
	protected String description = "";

	/** Stores the last d-pad direction. */
	protected DpadDirection lastDirection = null;

	/** Lookup map for buttons based on their type. */
	private Map<ButtonID, IButton> buttonMap = new HashMap<ButtonID, IButton>();

	/** Lookup map for button aliases. */
	private Map<ButtonID, IButton> buttonAliasMap = new HashMap<ButtonID, IButton>();
	
	/** Stores the buttons of this controller. */
	private BaseButton[] buttons = null;
	
	/**
	 * Creates a controller wrapper.
	 * 
	 * @param id The deviceID of the controller.
	 */
	protected AbstractBaseController(int deviceID) {
		this(deviceID, "");
	}
	
	/**
	 * Creates a controller wrapper.
	 * 
	 * @param id The deviceID of the controller.
	 */
	protected AbstractBaseController(int deviceID, String description) {
		this.deviceID = deviceID;
		if(this.deviceID < 0) {
			throw new IllegalArgumentException("Device ID must be positive integer value.");
		}
		if(description != null) {
			this.description = description;
		}
	}
	
	/**
	 * Initializes the array of buttons for this controller.
	 * 
	 * @param number The number of buttons.
	 */
	public void createButtons(int number) {
		System.out.println("Create " + number + " buttons for pad...");
		this.buttons = new BaseButton[number];
		for(int i = 0; i < this.buttons.length; i ++) {
			this.buttons[i] = new BaseButton(this, i, false, "", "");
		}
		
		for(int i = 0; i < this.buttons.length; i ++) {
			ButtonID mappedID = Mapping.getMappedID(this, this.buttons[i].getIndex());
			if(mappedID != ButtonID.UNKNOWN) {
				this.buttons[i].setID(mappedID);
				this.buttonMap.put(mappedID, this.buttons[i]);
				String label = Mapping.getButtonLabel(this, mappedID);
				if(label == null) {
					label = Mapping.getDefaultLabel(mappedID);
				}
				if(label != null) {
					this.buttons[i].setDefaultLabel(label);
				}
				String labelKey = Mapping.getButtonLabelKey(this, mappedID);
				if(labelKey != null) {
					this.buttons[i].setLabelKey(labelKey);
				}
			} else {
				System.out.println("No mapping found for button: " + this.buttons[i].getIndex());
			}
		}
		// Now create alias mappings
		for(ButtonID id : ButtonID.values()) {
			ButtonID aliasID = Mapping.getAliasID(this, id);
			if(aliasID != null && aliasID != ButtonID.UNKNOWN) {
				IButton button = this.buttonMap.get(aliasID);
				this.buttonAliasMap.put(id, button);
			}
		}
	}
	
	/**
	 * Adds a button to the map of buttons.
	 * 
	 * @param button The button to add.
	 */
	protected void addButton(IButton button) {
		this.buttonMap.put(button.getID(), button);
	}

	/* (non-Javadoc)
	 * @see com.gamepad4j.IController#getButton(int)
	 */
	@Override
	public IButton getButton(int buttonCode) {
		return this.buttons[buttonCode];
	}

	/* (non-Javadoc)
	 * @see com.gamepad4j.util.IController#getDescription()
	 */
	@Override
	public String getDescription() {
		return this.description;
	}
	
	/**
	 * Sets the description for this controller.
	 * 
	 * @param description The description text.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/* (non-Javadoc)
	 * @see com.gamepad4j.util.IController#getID()
	 */
	@Override
	public int getDeviceID() {
		return this.deviceID;
	}

	/**
	 * @param deviceID the deviceID to set
	 */
	public void setDeviceID(int deviceID) {
		this.deviceID = deviceID;
	}

	/*
	 * (non-Javadoc)
	 * @see com.gamepad4j.IController#getVendorID()
	 */
	@Override
	public int getVendorID() {
		return vendorID;
	}

	/**
	 * @param vendorID the vendorID to set
	 */
	public void setVendorID(int vendorID) {
		System.out.println("Set controller vendor ID: " + Integer.toHexString(vendorID));
		this.vendorID = vendorID;
	}

	/*
	 * (non-Javadoc)
	 * @see com.gamepad4j.IController#getProductID()
	 */
	@Override
	public int getProductID() {
		return productID;
	}

	/**
	 * @param productID the productID to set
	 */
	public void setProductID(int productID) {
		System.out.println("Set controller product ID: " + Integer.toHexString(productID));
		this.productID = productID;
	}
	
	/* (non-Javadoc)
	 * @see com.gamepad4j.IController#getDeviceIdentifier()
	 */
	@Override
	public long getDeviceTypeIdentifier() {
		if(this.vendorID == -1) {
			throw new IllegalArgumentException("Vendor ID not set for controller " + this.deviceID + " / " + this.description);
		}
		if(this.productID == -1) {
			throw new IllegalArgumentException("Product ID not set for controller " + this.deviceID + " / " + this.description);
		}
		return (vendorID << 16) + productID;
	}

	/* (non-Javadoc)
	 * @see com.gamepad4j.util.IController#getDpadDirectionOnce()
	 */
	@Override
	public DpadDirection getDpadDirectionOnce() {
		DpadDirection current = getDpadDirection();
		if(current != lastDirection) {
			lastDirection = current;
			return current;
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.gamepad4j.util.IController#isButtonPressedOnce(com.gamepad4j.util.ButtonID)
	 */
	@Override
	public boolean isButtonPressedOnce(ButtonID buttonID) {
		return getButton(buttonID).isPressedOnce();
	}

	/* (non-Javadoc)
	 * @see com.gamepad4j.util.IController#getButtons()
	 */
	@Override
	public IButton[] getButtons() {
		return this.buttons;
	}

	/* (non-Javadoc)
	 * @see com.gamepad4j.util.IController#getButton(com.gamepad4j.util.ButtonID)
	 */
	@Override
	public IButton getButton(ButtonID buttonID) {
		IButton button = this.buttonMap.get(buttonID);
		if(button == null) {
			button = this.buttonAliasMap.get(buttonID);
		}
		return button;
	}

	/* (non-Javadoc)
	 * @see com.gamepad4j.util.IController#isButtonPressed(com.gamepad4j.util.ButtonID)
	 */
	@Override
	public boolean isButtonPressed(ButtonID buttonID) {
		return this.buttonMap.get(buttonID).isPressed();
	}

	/* (non-Javadoc)
	 * @see com.gamepad4j.util.IController#getAnalogButtonPressure(com.gamepad4j.util.ButtonID)
	 */
	@Override
	public float getAnalogButtonPressure(ButtonID buttonID)
			throws IllegalArgumentException {
		return this.buttonMap.get(buttonID).analogValue();
	}

}
