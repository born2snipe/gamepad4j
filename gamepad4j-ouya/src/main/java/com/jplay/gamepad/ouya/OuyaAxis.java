/*
 * @Copyright: Marcel Schoen, Switzerland, 2013, All Rights Reserved.
 */

package com.jplay.gamepad.ouya;

import tv.ouya.console.api.OuyaController;

import com.gamepad4j.AxisID;
import com.gamepad4j.IAxis;
import com.gamepad4j.IStick;
import com.gamepad4j.StickID;

/**
 * Wrapper for an axis of an OUYA gamepad analog stick.
 *
 * @author Marcel Schoen
 * @version $Revision: $
 */
public class OuyaAxis implements IAxis {

	/** Stores the deviceID of the axis. */
	private AxisID id = null;

	/** Reference to the stick which this axis belongs to. */
	private IStick stick = null;
	
	/** Reference to the OUYA controller. */
	private OuyaController controller = null;
	
	/** Stores the OUYA axis deviceID. */
	private int ouyaAxisId = -1;
	
	/**
	 * Creates a wrapper for an analog stick axis.
	 * 
	 * @param id The deviceID of the axis.
	 * @param stick The stick to which the axis belongs.
	 * @param controller The wrapped controller.
	 */
	public OuyaAxis(AxisID id, IStick stick, OuyaController controller) {
		this.id = id;
		this.stick = stick;
		this.controller = controller;
		if(this.stick.getID() == StickID.LEFT_ANALOG) {
			if(id == AxisID.X) {
				this.ouyaAxisId = OuyaController.AXIS_LS_X;
			} else {
				this.ouyaAxisId = OuyaController.AXIS_LS_Y;
			}
		} else if(this.stick.getID() == StickID.RIGHT_ANALOG) {
			if(id == AxisID.X) {
				this.ouyaAxisId = OuyaController.AXIS_RS_X;
			} else {
				this.ouyaAxisId = OuyaController.AXIS_RS_Y;
			}
		} else {
			throw new IllegalArgumentException("Stick not yet supported: " + stick.getID().name());
		}
	}
	
	/* (non-Javadoc)
	 * @see com.gamepad4j.util.IAxis#getID()
	 */
	@Override
	public AxisID getID() {
		return this.id;
	}

	/* (non-Javadoc)
	 * @see com.gamepad4j.util.IAxis#getValue()
	 */
	@Override
	public float getValue() {
		float value = this.controller.getAxisValue(this.ouyaAxisId);
        value = Math.min(value, 1.0f);
//		System.out.println("Axis " + this.id.name() + ": " + value);
		return value;
	}

}
