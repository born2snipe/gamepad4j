/*
 * @Copyright: Marcel Schoen, Switzerland, 2013, All Rights Reserved.
 */

package org.gamepad4j.ouya;

import org.gamepad4j.AxisID;
import org.gamepad4j.IAxis;
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

	/** Constants for axes array. */
	private final static int X_AXIS = 0;
	private final static int Y_AXIS = 1;
	
	/** Stores references to the axes. */
	private IAxis[] axes = new IAxis[2];

	/**
	 * Creates an OUYA analog stick wrapper.
	 * 
	 * @param id The deviceID of the stick.
	 * @param controller The wrapped controller.
	 */
	public OuyaControllerStick(StickID id, OuyaController controller) {
		super(id);
		axes[X_AXIS] = new OuyaAxis(AxisID.X, this, controller);
		axes[Y_AXIS] = new OuyaAxis(AxisID.Y, this, controller);
	}

	/* (non-Javadoc)
	 * @see org.gamepad4j.util.IStick#getAxes()
	 */
	@Override
	public IAxis[] getAxes() {
		return this.axes;
	}

	/* (non-Javadoc)
	 * @see org.gamepad4j.util.IStick#getAxis(org.gamepad4j.util.AxisID)
	 */
	@Override
	public IAxis getAxis(AxisID axis) throws IllegalArgumentException {
		if(axis == AxisID.X) {
			return this.axes[X_AXIS];
		}
		if(axis == AxisID.Y) {
			return this.axes[Y_AXIS];
		}
		throw new IllegalArgumentException("Axis '" + axis.name() + "' not supported by OUYA controller.");
	}

	
/*
    static private float stickMag(float axisX, float axisY) {
        float stickMag = (float) Math.sqrt(axisX * axisX + axisY * axisY);
        return stickMag;
    }

    static public boolean isStickNotCentered(float axisX, float axisY) {
        final float c_minStickDistance = 0.2f;
        float stickMag = stickMag(axisX, axisY);
        return (stickMag >= c_minStickDistance);
    }
    */
}