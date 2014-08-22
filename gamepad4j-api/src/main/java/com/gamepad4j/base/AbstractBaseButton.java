/*
 * @Copyright: Marcel Schoen, Switzerland, 2013, All Rights Reserved.
 */

package com.gamepad4j.base;

import com.gamepad4j.ButtonID;
import com.gamepad4j.IButton;

/**
 * Abstract base class for button wrappers.
 *
 * @author Marcel Schoen
 * @version $Revision: $
 */
public abstract class AbstractBaseButton implements IButton {

	/** Stores the ID of this button. */
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
	protected int code = -1;
	
	/**
	 * Creates a button wrapper.
	 * 
	 * @param ID The ID of this button.
	 * @param isAnalog Set to true if this is an analog button.
	 * @param label The text label (may be null).
	 * @param labelKey The text label key (may be null).
	 */
	protected AbstractBaseButton(ButtonID ID, boolean isAnalog, String label, String labelKey) {
		this.ID = ID;
		this.isAnalog = isAnalog;
		this.label = label;
		this.labelKey = labelKey;
	}
	
	/**
	 * Creates a button wrapper.
	 * 
	 * @param code The numeric code of the button.
	 * @param isAnalog Set to true if this is an analog button.
	 * @param label The text label (may be null).
	 * @param labelKey The text label key (may be null).
	 */
	protected AbstractBaseButton(int code, boolean isAnalog, String label, String labelKey) {
		this.code = code;
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

	/* (non-Javadoc)
	 * @see com.gamepad4j.util.AbstractBaseButton#getCode()
	 */
	@Override
	public int getCode() {
		return this.code;
	}

	/* (non-Javadoc)
	 * @see com.gamepad4j.util.IButton#analogValue()
	 */
	@Override
	public abstract float analogValue();

	/* (non-Javadoc)
	 * @see com.gamepad4j.util.IButton#isPressed()
	 */
	@Override
	public abstract boolean isPressed();

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

	/* (non-Javadoc)
	 * @see com.gamepad4j.util.IButton#getDefaultLabel()
	 */
	@Override
	public String getDefaultLabel() {
		return this.label;
	}

}
