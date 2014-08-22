/*
 * @Copyright: Marcel Schoen, Switzerland, 2013, All Rights Reserved.
 */

package com.gamepad4j;

/**
 * Interface for handling one axis of joy- or gamepad-sticks, touchpads,
 * accelerometers etc.
 *
 * @author Marcel Schoen
 * @version $Revision: $
 */
public interface IAxis {

	/**
	 * Returns the ID of this axis.
	 * 
	 * @return The axis ID (type).
	 */
	AxisID getID();
	
	/**
	 * Returns the value of this axis (usually a value
	 * between -1.0 and 1.0).
	 * 
	 * @return The value of this axis.
	 */
	float getValue();
}
