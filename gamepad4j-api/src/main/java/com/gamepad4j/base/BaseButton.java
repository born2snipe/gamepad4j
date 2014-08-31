/*
 * @Copyright: Marcel Schoen, Switzerland, 2013, All Rights Reserved.
 */

package com.gamepad4j.base;

import com.gamepad4j.ButtonID;
import com.gamepad4j.IButton;
import com.gamepad4j.IController;

/**
 * Abstract base class for button wrappers.
 *
 * @author Marcel Schoen
 * @version $Revision: $
 */
public class BaseButton implements IButton {

	private float analogValue = -1;
	private boolean isPressed = false;

	/** Stores the deviceID of this button. */
	protected ButtonID ID = ButtonID.UNKNOWN;
	
	/** Stores the label of this button. */
	protected String label = null;

	/** Stores the label resource bundle key of this button. */
	protected String labelKey = null;

	/** Stores the last pressed state. */
	protected boolean lastPressed = false;

	/** Flag which indicates if this button is analog. */
	protected boolean isAnalog = false;

	/** The numeric code of the controller button. */
	protected int index = -1;
	
	/** Stores the controller to which this button belongs. */
	protected IController controller = null;
	
	/**
	 * Creates a button wrapper.
	 * 
	 * @param index The numeric index of the button.
	 * @param isAnalog Set to true if this is an analog button.
	 * @param label The text label (may be null).
	 * @param labelKey The text label key (may be null).
	 */
	public BaseButton(IController controller, int index, boolean isAnalog, String label, String labelKey) {
		this.controller = controller;
		this.index = index;
		this.isAnalog = isAnalog;
		this.label = label;
		this.labelKey = labelKey;
	}

	
	/* (non-Javadoc)
	 * @see com.gamepad4j.util.IButton#getID()
	 */
	@Override
	public ButtonID getID() {
		return this.ID;
	}

	/**
	 * Sets the ID of this button as defined by the mapping.
	 * 
	 * @param ID The ID for this button.
	 */
	public void setID(ButtonID ID) {
		this.ID = ID;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.gamepad4j.IButton#getIndex()
	 */
	@Override
	public int getIndex() {
		return this.index;
	}
	
	/* (non-Javadoc)
	 * @see com.gamepad4j.base.BaseButton#analogValue()
	 */
	@Override
	public float analogValue() {
		return analogValue;
	}

	/**
	 * Sets the analog value of this button.
	 * 
	 * @param analogValue The analog value.
	 */
	public void setAnalogValue(float analogValue) {
		this.analogValue = analogValue;
	}

	/* (non-Javadoc)
	 * @see com.gamepad4j.base.BaseButton#isPressed()
	 */
	@Override
	public boolean isPressed() {
		return isPressed;
	}

	/**
	 * Sets the pressed state of this button.
	 * 
	 * @param isPressed True if the button is pressed.
	 */
	public void setPressed(boolean isPressed) {
		if(isPressed != this.isPressed) {
			System.out.println("Button press change: " + isPressed + "/ index: " + this.index + " / ID: "
					+ this.ID + " / label: " + this.label + " / key: " + this.labelKey);
		}
		this.isPressed = isPressed;
		// TODO: Implement listener for buttons
	}
	
	/* (non-Javadoc)
	 * @see com.gamepad4j.util.IButton#isAnalogue()
	 */
	@Override
	public boolean isAnalog() {
		return this.isAnalog;
	}

	/* (non-Javadoc)
	 * @see com.gamepad4j.util.IButton#isPressedOnce()
	 */
	@Override
	public boolean isPressedOnce() {
		boolean pressed = isPressed();
		if(pressed != lastPressed) {
			lastPressed = pressed;
			return pressed;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see com.gamepad4j.util.IButton#getLabelKey()
	 */
	@Override
	public String getLabelKey() {
		return this.labelKey;
	}

	/**
	 * @param label the label to set
	 */
	public void setDefaultLabel(String label) {
		this.label = label;
	}


	/**
	 * @param labelKey the labelKey to set
	 */
	public void setLabelKey(String labelKey) {
		this.labelKey = labelKey;
	}


	/* (non-Javadoc)
	 * @see com.gamepad4j.util.IButton#getDefaultLabel()
	 */
	@Override
	public String getDefaultLabel() {
		return this.label;
	}

}
