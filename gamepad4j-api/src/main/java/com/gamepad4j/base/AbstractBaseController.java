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
	
	/** Lookup map for buttons based on their numeric code. */
	private Map<Integer, IButton> buttonCodeMap = new HashMap<Integer, IButton>();
	
	/** Stores the available buttons. */
	private IButton[] buttons = null;
	
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
	 * Adds a button to the map of buttons.
	 * 
	 * @param button The button to add.
	 */
	protected void addButton(IButton button) {
		this.buttonMap.put(button.getID(), button);
		this.buttonCodeMap.put(button.getIndex(), button);
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
		this.productID = productID;
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
		return false;
//		return getButton(buttonID).isPressedOnce();
	}

	/* (non-Javadoc)
	 * @see com.gamepad4j.util.IController#getButtons()
	 */
	@Override
	public IButton[] getButtons() {
		if(this.buttons == null) {
			// Some best-effort completions
			// If either BACK or CANCEL was not set, use the same value for both
			// (they are usually the same button)
			if(this.buttonMap.get(ButtonID.BACK) == null && this.buttonMap.get(ButtonID.CANCEL) != null) {
				this.buttonMap.put(ButtonID.BACK, this.buttonMap.get(ButtonID.CANCEL));
			}
			if(this.buttonMap.get(ButtonID.BACK) != null && this.buttonMap.get(ButtonID.CANCEL) == null) {
				this.buttonMap.put(ButtonID.CANCEL, this.buttonMap.get(ButtonID.BACK));
			}
			// The same goes for PAUSE and MENU
			if(this.buttonMap.get(ButtonID.PAUSE) == null && this.buttonMap.get(ButtonID.MENU) != null) {
				this.buttonMap.put(ButtonID.PAUSE, this.buttonMap.get(ButtonID.MENU));
			}
			if(this.buttonMap.get(ButtonID.PAUSE) != null && this.buttonMap.get(ButtonID.MENU) == null) {
				this.buttonMap.put(ButtonID.MENU, this.buttonMap.get(ButtonID.PAUSE));
			}
			this.buttons = new IButton[this.buttonMap.size()];
			int ct = 0;
			for(IButton button : this.buttonMap.values()) {
				this.buttons[ct] = button;
				ct ++;
			}
		}
		return this.buttons;
	}

	/* (non-Javadoc)
	 * @see com.gamepad4j.util.IController#getButton(int)
	 */
	@Override
	public IButton getButton(int buttonCode) {
		return this.buttonCodeMap.get(buttonCode);
	}

	/* (non-Javadoc)
	 * @see com.gamepad4j.util.IController#getButton(com.gamepad4j.util.ButtonID)
	 */
	@Override
	public IButton getButton(ButtonID buttonID) {
		return this.buttonMap.get(buttonID);
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
