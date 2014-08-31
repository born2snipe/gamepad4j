/*
 * @Copyright: Marcel Schoen, Switzerland, 2013, All Rights Reserved.
 */

package com.jplay.gamepad.ouya;

import tv.ouya.console.api.OuyaController;

import com.gamepad4j.ButtonID;
import com.gamepad4j.base.BaseButton;

/**
 * Wrapper for OUYA controller button.
 *
 * @author Marcel Schoen
 * @version $Revision: $
 */
public class OuyaControllerButton extends BaseButton {

	/** Controller to which this button belongs. */
	private OuyaController controller = null;

	/** The numeric code of the OUYA controller button. */
	private int code = -1;
	
	/**
	 * Creates a new OUYA controller button wrapper.
	 * 
	 * @param id The deviceID of the button.
	 * @param isAnalog True if it is an analog button.
	 * @param label The english default text label for this button.
	 * @param labelKey The message resource key for the text label (may be null,
	 *                 in which case the given label will be used). If a valid
	 *                 key is provided, it can be used later to show localized
	 *                 button labels to the player.
	 * @param controller The OUYA controller to which this button belongs.
	 */
	public OuyaControllerButton(ButtonID id, String label, String labelKey, OuyaController controller) {
		super(id, false, label, labelKey);
		this.controller = controller;
		if(id == ButtonID.ACCEPT) {
			this.code = OuyaController.BUTTON_O;
		} else if(id == ButtonID.BACK) {
			this.code = OuyaController.BUTTON_A;
		} else if(id == ButtonID.CANCEL) {
			this.code = OuyaController.BUTTON_A;
		} else if(id == ButtonID.FACE_DOWN) {
			this.code = OuyaController.BUTTON_O;
		} else if(id == ButtonID.FACE_LEFT) {
			this.code = OuyaController.BUTTON_U;
		} else if(id == ButtonID.FACE_RIGHT) {
			this.code = OuyaController.BUTTON_A;
		} else if(id == ButtonID.FACE_UP) {
			this.code = OuyaController.BUTTON_Y;
		} else if(id == ButtonID.TRIGGER_LEFT_UP) {
			this.code = OuyaController.BUTTON_L1;
		} else if(id == ButtonID.TRIGGER_LEFT_DOWN) {
			this.isAnalog = true;
			this.code = OuyaController.AXIS_L2;
		} else if(id == ButtonID.TRIGGER_RIGHT_UP) {
			this.code = OuyaController.BUTTON_R1;
		} else if(id == ButtonID.TRIGGER_RIGHT_DOWN) {
			this.isAnalog = true;
			this.code = OuyaController.AXIS_R2;
		} else if(id == ButtonID.TRIGGER_LEFT_UP) {
			this.code = OuyaController.BUTTON_L1;
		} else if(id == ButtonID.TRIGGER_LEFT_UP) {
			this.code = OuyaController.BUTTON_L1;
		} else if(id == ButtonID.MENU) {
			this.code = OuyaController.BUTTON_MENU;
		} else if(id == ButtonID.HOME) {
			this.code = OuyaController.BUTTON_MENU;
		}
	}
	
	/**
	 * Creates a new OUYA controller button wrapper.
	 * 
	 * @param code The numeric code of the button.
	 * @param isAnalog True if it is an analog button.
	 * @param label The english default text label for this button.
	 * @param labelKey The message resource key for the text label (may be null,
	 *                 in which case the given label will be used). If a valid
	 *                 key is provided, it can be used later to show localized
	 *                 button labels to the player.
	 * @param controller The OUYA controller to which this button belongs.
	 */
	public OuyaControllerButton(int code, boolean isAnalog, String label, String labelKey, OuyaController controller) {
		super(code, isAnalog, label, labelKey);
		this.controller = controller;
	}

	/* (non-Javadoc)
	 * @see com.gamepad4j.util.IButton#isPressed()
	 */
	@Override
	public boolean isPressed() {
		return this.controller.getButton(this.code);
	}

	/* (non-Javadoc)
	 * @see com.gamepad4j.util.IButton#analogValue()
	 */
	@Override
	public float analogValue() {
		return this.controller.getAxisValue(this.code);
	}
}
