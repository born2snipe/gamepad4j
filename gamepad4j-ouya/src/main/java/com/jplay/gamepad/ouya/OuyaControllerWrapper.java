/*
 * @Copyright: Marcel Schoen, Switzerland, 2013, All Rights Reserved.
 */

package com.jplay.gamepad.ouya;

import tv.ouya.console.api.OuyaController;

import com.gamepad4j.AxisID;
import com.gamepad4j.ButtonID;
import com.gamepad4j.DpadDirection;
import com.gamepad4j.IStick;
import com.gamepad4j.StickID;
import com.gamepad4j.base.AbstractBaseController;

/**
 * Wrapper for the OUYA controller.
 *
 * @author Marcel Schoen
 * @version $Revision: $
 */
public class OuyaControllerWrapper extends AbstractBaseController {

	/** Reference to the wrapped controller. */
	private OuyaController wrappedController = null;
	
	/** Reference to analog sticks. */
	private IStick[] sticks = new IStick[2];
	
	/** Constants for accessing sticks array. */
	private final static int LEFT_STICK = 0;
	private final static int RIGHT_STICK = 1;

	/** Array with possible value combinations for d-pad. */
	private final static DpadDirection[] directions = new DpadDirection[] {
		DpadDirection.NONE, // 0
		DpadDirection.UP, // 1
		DpadDirection.RIGHT, // 2
		DpadDirection.UP_RIGHT, // 3 (1 + 2)
		DpadDirection.DOWN, // 4
		null,
		DpadDirection.DOWN_RIGHT, // 6 (4 + 2)
		null,
		DpadDirection.LEFT, // 8
		DpadDirection.UP_LEFT, // 9 (8 + 1)
		null,
		null,
		DpadDirection.DOWN_LEFT // 12 (8 + 4)
	};
	
	/**
	 * Creates a controller wrapper.
	 * 
	 * @param wrapped The wrapped controller.
	 */
	public OuyaControllerWrapper(OuyaController wrapped) {
		super(wrapped.getDeviceId());
		this.wrappedController = wrapped;
		this.sticks[LEFT_STICK] = new OuyaControllerStick(StickID.LEFT_ANALOG, wrapped);
		this.sticks[RIGHT_STICK] = new OuyaControllerStick(StickID.RIGHT_ANALOG, wrapped);
		
		addButton(new OuyaControllerButton(ButtonID.ACCEPT, "O", "jplay.ouya.button.O", this.wrappedController));
		addButton(new OuyaControllerButton(ButtonID.BACK, "A", "jplay.ouya.button.A", this.wrappedController));
		addButton(new OuyaControllerButton(ButtonID.CANCEL, "A", "jplay.ouya.button.A", this.wrappedController));
		addButton(new OuyaControllerButton(ButtonID.FACE_DOWN, "O", "jplay.ouya.button.O", this.wrappedController));
		addButton(new OuyaControllerButton(ButtonID.FACE_RIGHT, "A", "jplay.ouya.button.A", this.wrappedController));
		addButton(new OuyaControllerButton(ButtonID.FACE_LEFT, "U", "jplay.ouya.button.U", this.wrappedController));
		addButton(new OuyaControllerButton(ButtonID.FACE_UP, "Y", "jplay.ouya.button.Y", this.wrappedController));
		addButton(new OuyaControllerButton(ButtonID.HOME, "OUYA", "jplay.ouya.button.OUYA", this.wrappedController));
		addButton(new OuyaControllerButton(ButtonID.MENU, "OUYA", "jplay.ouya.button.OUYA", this.wrappedController));
		addButton(new OuyaControllerButton(ButtonID.PAUSE, "OUYA", "jplay.ouya.button.OUYA", this.wrappedController));
		addButton(new OuyaControllerButton(ButtonID.START, "O", "jplay.ouya.button.O", this.wrappedController));
		addButton(new OuyaControllerButton(ButtonID.TRIGGER1_LEFT, "Left upper trigger", "jplay.ouya.button.leftUpperTrigger", this.wrappedController));
		addButton(new OuyaControllerButton(ButtonID.TRIGGER2_LEFT, "Left lower trigger", "jplay.ouya.button.leftLowerTrigger", this.wrappedController));
		addButton(new OuyaControllerButton(ButtonID.TRIGGER1_RIGHT, "Right upper trigger", "jplay.ouya.button.rightUpperTrigger", this.wrappedController));
		addButton(new OuyaControllerButton(ButtonID.TRIGGER2_RIGHT, "Right lower trigger", "jplay.ouya.button.rightLowerTrigger", this.wrappedController));
	}
	
	/* (non-Javadoc)
	 * @see com.gamepad4j.util.IController#getID()
	 */
	@Override
	public int getDeviceID() {
		return this.wrappedController.getDeviceId();
	}

	/* (non-Javadoc)
	 * @see com.gamepad4j.util.IController#getSticks()
	 */
	@Override
	public IStick[] getSticks() {
		return this.sticks;
	}

	/* (non-Javadoc)
	 * @see com.gamepad4j.util.IController#getStick(com.gamepad4j.util.StickID)
	 */
	@Override
	public IStick getStick(StickID stick) throws IllegalArgumentException {
		if(stick == StickID.LEFT_ANALOG) {
			return this.sticks[LEFT_STICK];
		} else if(stick == StickID.RIGHT_ANALOG) {
			return this.sticks[RIGHT_STICK];
		}
		throw new IllegalArgumentException("Stick '" + stick.name() + "' not supported by OUYA controller.");
	}

	/* (non-Javadoc)
	 * @see com.gamepad4j.util.IController#getAxis(com.gamepad4j.util.StickID, com.gamepad4j.util.AxisID)
	 */
	@Override
	public float getAxis(StickID stick, AxisID axis) {
		if(stick == StickID.LEFT_ANALOG) {
			if(axis == AxisID.X) {
				return this.wrappedController.getAxisValue(OuyaController.AXIS_LS_X);
			} else if(axis == AxisID.Y) {
				return this.wrappedController.getAxisValue(OuyaController.AXIS_LS_Y);
			}
		} else if(stick == StickID.RIGHT_ANALOG) {
			if(axis == AxisID.X) {
				return this.wrappedController.getAxisValue(OuyaController.AXIS_RS_X);
			} else if(axis == AxisID.Y) {
				return this.wrappedController.getAxisValue(OuyaController.AXIS_RS_Y);
			}
		}
		throw new IllegalArgumentException("Stick '" + stick.name() + "' with axis '" 
				+ axis.name() + "' not supported by OUYA controller.");
	}

	/* (non-Javadoc)
	 * @see com.gamepad4j.util.IController#getDpadDirection()
	 */
	@Override
	public DpadDirection getDpadDirection() {
		int direction = 0;
		if(this.wrappedController.getButton(OuyaController.BUTTON_DPAD_UP)) {
			direction += 1;
		}
		if(this.wrappedController.getButton(OuyaController.BUTTON_DPAD_RIGHT)) {
			direction += 2;
		}
		if(this.wrappedController.getButton(OuyaController.BUTTON_DPAD_DOWN)) {
			direction += 4;
		}
		if(this.wrappedController.getButton(OuyaController.BUTTON_DPAD_LEFT)) {
			direction += 8;
		}
		return directions[direction];
	}
}
