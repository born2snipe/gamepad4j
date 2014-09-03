/*
 * @Copyright: Marcel Schoen, Switzerland, 2014, All Rights Reserved.
 */

package org.gamepad4j.base;

import org.gamepad4j.AxisID;
import org.gamepad4j.IAxis;

/**
 * Holder for values of one axis.
 *
 * @author Marcel Schoen
 * @version $Revision: $
 */
public class BaseAxis implements IAxis {

	/** Stores the ID of this axis. */
	private AxisID ID = null;
	
	/** Stores the float value of this axis. */
	private float value = 0f;
	
	/**
	 * Creates a new base axis instance.
	 * 
	 * @param ID The ID of the axis (important only for multi-axes components like sticks).
	 * @param type The axis type (stick, trigger...).
	 */
	public BaseAxis(AxisID ID) {
		this.ID = ID;
	}
	
	/* (non-Javadoc)
	 * @see org.gamepad4j.IAxis#getID()
	 */
	@Override
	public AxisID getID() {
		return this.ID;
	}

	/* (non-Javadoc)
	 * @see org.gamepad4j.IAxis#getValue()
	 */
	@Override
	public float getValue() {
		return this.value;
	}

	/**
	 * Sets the float value of this axis.
	 * 
	 * @param value The new float value.
	 */
	public void setValue(float value) {
		this.value = value;
	}
}
