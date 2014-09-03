/*
 * @Copyright: Marcel Schoen, Switzerland, 2013, All Rights Reserved.
 */

package org.gamepad4j.ouya;

import org.gamepad4j.ButtonID;
import org.gamepad4j.IController;
import org.gamepad4j.base.BaseButton;

import tv.ouya.console.api.OuyaController;

/**
 * Wrapper for OUYA controller button.
 *
 * @author Marcel Schoen
 * @version $Revision: $
 */
public class OuyaControllerButton extends BaseButton {

	/** Controller to which this button belongs. */
	private OuyaController controller = null;
	
	/**
	 * Creates a new OUYA controller button wrapper.
	 * 
	 * @param id The IController to which this button belongs.
	 * @param id The deviceID of the button.
	 * @param isAnalog True if it is an analog button.
	 * @param label The english default text label for this button.
	 * @param labelKey The message resource key for the text label (may be null,
	 *                 in which case the given label will be used). If a valid
	 *                 key is provided, it can be used later to show localized
	 *                 button labels to the player.
	 * @param wrappedController The OUYA controller to which this button belongs.
	 */
	public OuyaControllerButton(IController controller, ButtonID id, String label, String labelKey, OuyaController wrappedController) {
		super(controller, 0, label, labelKey);
		this.controller = wrappedController;
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
		} else if(id == ButtonID.SHOULDER_LEFT_UP) {
			this.code = OuyaController.BUTTON_L1;
		} else if(id == ButtonID.SHOULDER_RIGHT_UP) {
			this.code = OuyaController.BUTTON_R1;
		} else if(id == ButtonID.MENU) {
			this.code = OuyaController.BUTTON_MENU;
		} else if(id == ButtonID.HOME) {
			this.code = OuyaController.BUTTON_MENU;
		}
	}

	/* (non-Javadoc)
	 * @see org.gamepad4j.util.IButton#isPressed()
	 */
	@Override
	public boolean isPressed() {
		return this.controller.getButton(this.code);
	}
}
