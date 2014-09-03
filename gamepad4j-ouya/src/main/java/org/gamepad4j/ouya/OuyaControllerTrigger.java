/*
 * @Copyright: Marcel Schoen, Switzerland, 2014, All Rights Reserved.
 */

package org.gamepad4j.ouya;

import org.gamepad4j.IController;
import org.gamepad4j.TriggerID;
import org.gamepad4j.base.BaseTrigger;

import tv.ouya.console.api.OuyaController;

/**
 * Wrapper for OUYA controller trigger.
 *
 * @author Marcel Schoen
 * @version $Revision: $
 */
public class OuyaControllerTrigger extends BaseTrigger {

	/** Controller to which this button belongs. */
	private OuyaController controller = null;
	
	/**
	 * Creates a new OUYA controller trigger wrapper.
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
	public OuyaControllerTrigger(IController controller, TriggerID id, String label, String labelKey, OuyaController wrappedController) {
		super(controller, 0, null, label, labelKey);
		this.controller = wrappedController;
		if(id ==  TriggerID.TRIGGER_LEFT_DOWN) {
			this.code = OuyaController.AXIS_L2;
		} else if(id == TriggerID.TRIGGER_RIGHT_DOWN) {
			this.code = OuyaController.AXIS_R2;
		}
	}

	/* (non-Javadoc)
	 * @see org.gamepad4j.base.BaseTrigger#analogValue()
	 */
	@Override
	public float analogValue() {
		return super.analogValue();
	}

}
