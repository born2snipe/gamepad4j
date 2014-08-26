/*
 * @Copyright: Marcel Schoen, Switzerland, 2013, All Rights Reserved.
 */

package com.gamepad4j.base;

import com.gamepad4j.AxisID;
import com.gamepad4j.IAxis;
import com.gamepad4j.IStick;
import com.gamepad4j.StickID;
import com.gamepad4j.StickPosition;

/**
 * Abstract base class for stick wrappers.
 *
 * @author Marcel Schoen
 * @version $Revision: $
 */
public abstract class AbstractBaseStick implements IStick {

	/** Stores the deviceID of this stick. */
	protected StickID ID = StickID.UNKNOWN;

	/** The stick position data holder. */
	protected StickPosition position = new StickPosition();
	
	/** Holds direct reference to the X-axis. */
	protected IAxis xAxis = null;
	
	/** Holds direct reference to the Y-axis. */
	protected IAxis yAxis = null;
	
	/**
	 * Creates a stick wrapper.
	 * 
	 * @param deviceID The deviceID of this stick.
	 */
	protected AbstractBaseStick(StickID ID) {
		this.ID = ID;
	}
	
	/* (non-Javadoc)
	 * @see com.gamepad4j.util.IStick#getID()
	 */
	@Override
	public StickID getID() {
		return this.ID;
	}

	/**
	 * Convenience method which provides pre-processed status
	 * information about the stick, such as the degree in which
	 * it is currently held, and the distance to the center.
	 * 
	 * @return The stick position data holder.
	 */
	public StickPosition getPosition() {
		if(this.xAxis == null) {
			this.xAxis = getAxis(AxisID.X);
			this.yAxis = getAxis(AxisID.Y);
		}
		this.position.update(this.xAxis.getValue(), this.yAxis.getValue());
		return this.position;
	}

	/* (non-Javadoc)
	 * @see com.gamepad4j.util.IStick#getAxes()
	 */
	@Override
	public abstract IAxis[] getAxes();

	/* (non-Javadoc)
	 * @see com.gamepad4j.util.IStick#getAxis(com.gamepad4j.util.AxisID)
	 */
	@Override
	public abstract IAxis getAxis(AxisID axis) throws IllegalArgumentException;

}
