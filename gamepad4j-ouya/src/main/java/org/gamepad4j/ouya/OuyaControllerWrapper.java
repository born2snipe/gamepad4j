/*
 * @Copyright: Marcel Schoen, Switzerland, 2013, All Rights Reserved.
 */

package org.gamepad4j.ouya;

import org.gamepad4j.AxisID;
import org.gamepad4j.ButtonID;
import org.gamepad4j.DpadDirection;
import org.gamepad4j.IAxis;
import org.gamepad4j.IStick;
import org.gamepad4j.ITrigger;
import org.gamepad4j.StickID;
import org.gamepad4j.base.AbstractBaseController;
import org.gamepad4j.base.BaseAxis;
import org.gamepad4j.base.BaseTrigger;

import tv.ouya.console.api.OuyaController;

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
	private OuyaControllerStick[] sticks = new OuyaControllerStick[2];
	
	/** Reference to triggers. */
	private ITrigger[] triggers = new ITrigger[2];
	
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
		this.sticks[LEFT_STICK] = new OuyaControllerStick(StickID.LEFT, wrapped);
		this.sticks[RIGHT_STICK] = new OuyaControllerStick(StickID.RIGHT, wrapped);
		
		addButton(new OuyaControllerButton(this, ButtonID.ACCEPT, "O", "ouya.controller.facebutton.accept", this.wrappedController));
		addButton(new OuyaControllerButton(this, ButtonID.BACK, "A", "ouya.controller.facebutton.back", this.wrappedController));
		addButton(new OuyaControllerButton(this, ButtonID.CANCEL, "A", "ouya.controller.facebutton.cancel", this.wrappedController));
		addButton(new OuyaControllerButton(this, ButtonID.FACE_DOWN, "O", "ouya.controller.facebutton.down", this.wrappedController));
		addButton(new OuyaControllerButton(this, ButtonID.FACE_RIGHT, "A", "ouya.controller.facebutton.right", this.wrappedController));
		addButton(new OuyaControllerButton(this, ButtonID.FACE_LEFT, "U", "ouya.controller.facebutton.left", this.wrappedController));
		addButton(new OuyaControllerButton(this, ButtonID.FACE_UP, "Y", "ouya.controller.facebutton.up", this.wrappedController));
		addButton(new OuyaControllerButton(this, ButtonID.HOME, "OUYA", "ouya.controller.facebutton.home", this.wrappedController));
		addButton(new OuyaControllerButton(this, ButtonID.MENU, "OUYA", "ouya.controller.facebutton.menu", this.wrappedController));
		addButton(new OuyaControllerButton(this, ButtonID.PAUSE, "OUYA", "ouya.controller.facebutton.pause", this.wrappedController));
		addButton(new OuyaControllerButton(this, ButtonID.START, "O", "ouya.controller.facebutton.start", this.wrappedController));
		
		IAxis leftTriggerAxis = new BaseAxis(AxisID.TRIGGER, OuyaController.AXIS_L2);
		this.triggers[0] = new BaseTrigger(this, OuyaController.AXIS_L2, leftTriggerAxis, "Left Trigger", "ouya.controller.trigger.left");
		
		IAxis rightTriggerAxis = new BaseAxis(AxisID.TRIGGER, OuyaController.AXIS_R2);
		this.triggers[1] = new BaseTrigger(this, OuyaController.AXIS_R2, rightTriggerAxis, "Right Trigger", "ouya.controller.trigger.right");
	}
	
	/**
	 * Updates all the values in all axes and buttons.
	 * This is required for listeners, not for polling access.
	 */
	public void updateValues() {
		this.sticks[LEFT_STICK].updateValues();
		this.sticks[RIGHT_STICK].updateValues();
		
		// TODO - Update triggers and buttons
		
	}
	
	/* (non-Javadoc)
	 * @see org.gamepad4j.util.IController#getID()
	 */
	@Override
	public int getDeviceID() {
		return this.wrappedController.getDeviceId();
	}

	/* (non-Javadoc)
	 * @see org.gamepad4j.util.IController#getSticks()
	 */
	@Override
	public IStick[] getSticks() {
		return this.sticks;
	}

	/* (non-Javadoc)
	 * @see org.gamepad4j.util.IController#getStick(org.gamepad4j.util.StickID)
	 */
	@Override
	public IStick getStick(StickID stick) throws IllegalArgumentException {
		if(stick == StickID.LEFT) {
			return this.sticks[LEFT_STICK];
		} else if(stick == StickID.RIGHT) {
			return this.sticks[RIGHT_STICK];
		}
		throw new IllegalArgumentException("Stick '" + stick.name() + "' not supported by OUYA controller.");
	}

	/* (non-Javadoc)
	 * @see org.gamepad4j.util.IController#getDpadDirection()
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
