/*
 * @Copyright: Marcel Schoen, Switzerland, 2013, All Rights Reserved.
 */

package org.gamepad4j.ouya;

import org.gamepad4j.AxisID;
import org.gamepad4j.StickID;
import org.gamepad4j.base.BaseStick;

import tv.ouya.console.api.OuyaController;

/**
 * Stick implementation for OUYA controllers.
 *
 * @author Marcel Schoen
 * @version $Revision: $
 */
public class OuyaControllerStick extends BaseStick {

	/**
	 * Creates an OUYA analog stick wrapper.
	 * 
	 * @param id The deviceID of the stick.
	 * @param controller The wrapped controller.
	 */
	public OuyaControllerStick(StickID id, OuyaController controller) {
		super(id);
		if(id == StickID.LEFT) {
			setAxis(new OuyaAxis(AxisID.X, OuyaController.AXIS_LS_X, this, controller));
			setAxis(new OuyaAxis(AxisID.Y, OuyaController.AXIS_LS_Y, this, controller));
		} else {
			setAxis(new OuyaAxis(AxisID.X, OuyaController.AXIS_RS_X, this, controller));
			setAxis(new OuyaAxis(AxisID.Y, OuyaController.AXIS_RS_Y, this, controller));
		}
	}
	
	/**
	 * Updates the axis values of this stick.
	 */
	public void updateValues() {
		((OuyaAxis)getAxis(AxisID.X)).updateValue();
		((OuyaAxis)getAxis(AxisID.Y)).updateValue();
	}
}
