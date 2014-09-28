/*
 * @Copyright: Marcel Schoen, Switzerland, 2013, All Rights Reserved.
 */

package org.gamepad4j.ouya;

import org.gamepad4j.AxisID;
import org.gamepad4j.IStick;
import org.gamepad4j.base.BaseAxis;

import tv.ouya.console.api.OuyaController;

/**
 * Wrapper for an axis of an OUYA gamepad analog stick.
 *
 * @author Marcel Schoen
 * @version $Revision: $
 */
public class OuyaAxis extends BaseAxis {
	
	/** Reference to the OUYA controller. */
	private OuyaController controller = null;
	
	/** Stores the OUYA axis deviceID. */
	private int ouyaAxisID = -1;
	
	/**
	 * Creates a wrapper for an analog stick axis.
	 * 
	 * @param id The deviceID of the axis.
	 * @param stick The stick to which the axis belongs.
	 * @param controller The wrapped controller.
	 */
	public OuyaAxis(AxisID ID, int ouyaAxisID, IStick stick, OuyaController controller) {
		super(ID, ouyaAxisID);
		this.ouyaAxisID = ouyaAxisID;
		this.controller = controller;
	}

	/**
	 * Updates the value of this axis.
	 */
	public void updateValue() {
		float value = this.controller.getAxisValue(this.ouyaAxisID);
        value = Math.min(value, 1.0f);
        super.setValue(value);
	}
}
