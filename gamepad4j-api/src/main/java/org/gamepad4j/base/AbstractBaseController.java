/*
 * @Copyright: Marcel Schoen, Switzerland, 2013, All Rights Reserved.
 */

package org.gamepad4j.base;

import java.util.HashMap;
import java.util.Map;

import org.gamepad4j.AxisID;
import org.gamepad4j.ButtonID;
import org.gamepad4j.DpadDirection;
import org.gamepad4j.IAxis;
import org.gamepad4j.IButton;
import org.gamepad4j.IController;
import org.gamepad4j.IStick;
import org.gamepad4j.ITrigger;
import org.gamepad4j.Mapping;
import org.gamepad4j.StickID;
import org.gamepad4j.TriggerID;
import org.gamepad4j.util.Log;

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

	// ----------------------- buttons ---------------------------
	
	/** Lookup map for buttons based on their type. */
	private Map<ButtonID, IButton> buttonMap = new HashMap<ButtonID, IButton>();

	/** Lookup map for button aliases. */
	private Map<ButtonID, IButton> buttonAliasMap = new HashMap<ButtonID, IButton>();
	
	/** Stores the buttons of this controller. */
	private BaseButton[] buttons = null;

	// ----------------------- triggers ---------------------------
	
	/** Lookup map for buttons based on their type. */
	private Map<TriggerID, ITrigger> triggerMap = new HashMap<TriggerID, ITrigger>();
	
	/** Stores the buttons of this controller. */
	private BaseTrigger[] triggers = null;

	// ----------------------- sticks ---------------------------
	
	/** Lookup map for sticks based on their type. */
	private Map<StickID, IStick> stickMap = new HashMap<StickID, IStick>();
	
	/** Stores the sticks of this controller. */
	private BaseStick[] sticks = null;

	// ----------------------- axes (for triggers AND sticks) ---------------------------
	
	/** Stores the axes of this controller. */
	private BaseAxis[] axes = null;
	
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
	 * Initializes the array of sticks and axes.
	 * 
	 * @param number The number of axes.
	 */
	public void createTriggersAndSticks(int numberOfAxes) {
		Log.log("Process " + numberOfAxes + " analog axes...");
		this.axes = new BaseAxis[numberOfAxes];

		createTriggers();
		createSticks();
		
		// Verify proper axis initialization
		for(int axisNo = 0; axisNo < axes.length; axisNo++) {
			if(this.axes[axisNo] == null) {
				throw new IllegalStateException("Axis no. " + axisNo + " was not initialized!");
			}
		}
	}

	/**
	 * Creates all the trigger holders.
	 */
	private void createTriggers() {
		int triggerCt = 0;
		Log.log("Process " + axes.length + " analog axes for triggers...");

		// First, count how many axes were defined as triggers
		for(int axisNo = 0; axisNo < axes.length; axisNo++) {
			TriggerID mappedID = Mapping.getMappedTriggerID(this, axisNo);
			if(mappedID != TriggerID.UNKNOWN) {
				triggerCt ++;
			}
		}
		Log.log("Create " + triggerCt + " triggers.");
		this.triggers = new BaseTrigger[triggerCt];
		
		// Create triggers
		int triggerNo = 0;
		for(int axisNo = 0; axisNo < axes.length; axisNo++) {
			TriggerID mappedID = Mapping.getMappedTriggerID(this, axisNo);
			if(mappedID != TriggerID.UNKNOWN) {
				// It's a trigger button, not a stick axis
				Log.log("Map axis no. " + axisNo + " to trigger " + mappedID);
				this.axes[axisNo] = new BaseAxis(AxisID.TRIGGER);
				
				this.triggers[triggerNo] = new BaseTrigger(this, triggerNo, this.axes[axisNo], "", "");
				this.triggers[triggerNo].setID(mappedID);
				
				this.triggerMap.put(mappedID, this.triggers[triggerNo]);
				String label = Mapping.getTriggerLabel(this, mappedID);
				if(label == null) {
					label = Mapping.getDefaultTriggerLabel(mappedID);
				}
				if(label != null) {
					this.triggers[triggerNo].setDefaultLabel(label);
				}
				String labelKey = Mapping.getTriggerLabelKey(this, mappedID);
				if(labelKey != null) {
					this.triggers[triggerNo].setLabelKey(labelKey);
				}
			}
		}
	}
	
	/**
	 * Creates all the stick holders.
	 */
	private void createSticks() {
		Log.log("Process " + this.axes.length + " analog axes for sticks...");
		
		// The analog axes are shared between sticks and triggers
		int axesForSticks = this.axes.length - this.triggers.length;
		
		int numberOfSticks = axesForSticks / 2;
		Log.log("Create " + numberOfSticks + " sticks for pad...");

		// Create all stick objects (each one contains two axes)
		this.sticks = new BaseStick[numberOfSticks];
		for(int i = 0; i < numberOfSticks; i++) {
			this.sticks[i] = new BaseStick(StickID.UNKNOWN);
		}

		// Create all axes in the array
		boolean isX = true;
		int stickCounter = 0;
		for(int axisNo = 0; axisNo < axes.length; axisNo++) {
			TriggerID mappedID = Mapping.getMappedTriggerID(this, axisNo);
			if(mappedID == TriggerID.UNKNOWN) {
				StickID mappedStickID = Mapping.getMappedStickID(this, axisNo);
				((BaseStick)this.sticks[stickCounter]).ID = mappedStickID;
				this.stickMap.put(mappedStickID, this.sticks[stickCounter]);
				if(isX) {
					this.axes[axisNo] = (BaseAxis)this.sticks[stickCounter].getAxis(AxisID.X);
					Log.log("Map axis no. " + axisNo + " to stick " + mappedID + " as X-axis");
				} else {
					Log.log("Map axis no. " + axisNo + " to stick " + mappedID + " as Y-axis");
					this.axes[axisNo] = (BaseAxis)this.sticks[stickCounter].getAxis(AxisID.Y);
				}
				isX = !isX;
				if(isX) {
					// Increase to new stick after every 2 axes
					stickCounter ++;
				}
			}
		}
	}
	
	/**
	 * Initializes the array of buttons for this controller.
	 * 
	 * @param number The number of buttons.
	 */
	public void createDigitalButtons(int number) {
		Log.log("Create " + number + " buttons for pad...");

		// TODO: Use pooling for button instances
		
		this.buttons = new BaseButton[number];
		for(int i = 0; i < this.buttons.length; i ++) {
			this.buttons[i] = new BaseButton(this, i, "", "");
		}
		
		for(int i = 0; i < this.buttons.length; i ++) {
			ButtonID mappedID = Mapping.getMappedButtonID(this, this.buttons[i].getCode());
			if(mappedID != ButtonID.UNKNOWN) {
				this.buttons[i].setID(mappedID);
				this.buttonMap.put(mappedID, this.buttons[i]);
				String label = Mapping.getButtonLabel(this, mappedID);
				if(label == null) {
					label = Mapping.getDefaultButtonLabel(mappedID);
				}
				if(label != null) {
					this.buttons[i].setDefaultLabel(label);
				}
				String labelKey = Mapping.getButtonLabelKey(this, mappedID);
				if(labelKey != null) {
					this.buttons[i].setLabelKey(labelKey);
				}
			} else {
				Log.log("No mapping found for button: " + this.buttons[i].getCode());
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
	 * @see org.gamepad4j.IController#getButton(int)
	 */
	@Override
	public IButton getButton(int buttonCode) {
		return this.buttons[buttonCode];
	}

	/* (non-Javadoc)
	 * @see org.gamepad4j.util.IController#getDescription()
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
	 * @see org.gamepad4j.util.IController#getID()
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
	 * @see org.gamepad4j.IController#getVendorID()
	 */
	@Override
	public int getVendorID() {
		return vendorID;
	}

	/**
	 * @param vendorID the vendorID to set
	 */
	public void setVendorID(int vendorID) {
		Log.log("Set controller vendor ID: " + Integer.toHexString(vendorID));
		this.vendorID = vendorID;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gamepad4j.IController#getProductID()
	 */
	@Override
	public int getProductID() {
		return productID;
	}

	/**
	 * @param productID the productID to set
	 */
	public void setProductID(int productID) {
		Log.log("Set controller product ID: " + Integer.toHexString(productID));
		this.productID = productID;
	}
	
	/* (non-Javadoc)
	 * @see org.gamepad4j.IController#getDeviceIdentifier()
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
	 * @see org.gamepad4j.util.IController#getDpadDirectionOnce()
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
	 * @see org.gamepad4j.util.IController#isButtonPressedOnce(org.gamepad4j.util.ButtonID)
	 */
	@Override
	public boolean isButtonPressedOnce(ButtonID buttonID) {
		return getButton(buttonID).isPressedOnce();
	}

	/* (non-Javadoc)
	 * @see org.gamepad4j.util.IController#getButtons()
	 */
	@Override
	public IButton[] getButtons() {
		return this.buttons;
	}

	
	/* (non-Javadoc)
	 * @see org.gamepad4j.IController#getDpadDirection()
	 */
	@Override
	public DpadDirection getDpadDirection() {
		// TODO Auto-generated method stub
		return null;
	}
	
	

	/* (non-Javadoc)
	 * @see org.gamepad4j.IController#getTriggers()
	 */
	@Override
	public ITrigger[] getTriggers() {
		return this.triggers;
	}

	/* (non-Javadoc)
	 * @see org.gamepad4j.IController#getTrigger(org.gamepad4j.TriggerID)
	 */
	@Override
	public ITrigger getTrigger(TriggerID triggerID) {
		return this.triggerMap.get(triggerID);
	}

	/* (non-Javadoc)
	 * @see org.gamepad4j.util.IController#getButton(org.gamepad4j.util.ButtonID)
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
	 * @see org.gamepad4j.util.IController#isButtonPressed(org.gamepad4j.util.ButtonID)
	 */
	@Override
	public boolean isButtonPressed(ButtonID buttonID) {
		return this.buttonMap.get(buttonID).isPressed();
	}

	/* (non-Javadoc)
	 * @see org.gamepad4j.util.IController#getAnalogButtonPressure(org.gamepad4j.util.ButtonID)
	 */
	@Override
	public float getTriggerPressure(TriggerID buttonID)
			throws IllegalArgumentException {
		return this.triggerMap.get(buttonID).analogValue();
	}

	/* (non-Javadoc)
	 * @see org.gamepad4j.IController#getSticks()
	 */
	@Override
	public IStick[] getSticks() {
		return this.sticks;
	}

	/* (non-Javadoc)
	 * @see org.gamepad4j.IController#getStick(org.gamepad4j.StickID)
	 */
	@Override
	public IStick getStick(StickID stick) throws IllegalArgumentException {
		return stickMap.get(stick);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gamepad4j.IController#getAxes()
	 */
	@Override
	public IAxis[] getAxes() {
		return this.axes;
	}
}
