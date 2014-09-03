/*
 * @Copyright: Marcel Schoen, Switzerland, 2013, All Rights Reserved.
 */

package org.gamepad4j.ouya;

import org.gamepad4j.ButtonID;
import org.gamepad4j.DpadDirection;
import org.gamepad4j.IStick;
import org.gamepad4j.StickID;
import org.gamepad4j.base.AbstractBaseController;

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
		
		addButton(new OuyaControllerButton(this, ButtonID.ACCEPT, "O", "jplay.ouya.button.O", this.wrappedController));
		addButton(new OuyaControllerButton(this, ButtonID.BACK, "A", "jplay.ouya.button.A", this.wrappedController));
		addButton(new OuyaControllerButton(this, ButtonID.CANCEL, "A", "jplay.ouya.button.A", this.wrappedController));
		addButton(new OuyaControllerButton(this, ButtonID.FACE_DOWN, "O", "jplay.ouya.button.O", this.wrappedController));
		addButton(new OuyaControllerButton(this, ButtonID.FACE_RIGHT, "A", "jplay.ouya.button.A", this.wrappedController));
		addButton(new OuyaControllerButton(this, ButtonID.FACE_LEFT, "U", "jplay.ouya.button.U", this.wrappedController));
		addButton(new OuyaControllerButton(this, ButtonID.FACE_UP, "Y", "jplay.ouya.button.Y", this.wrappedController));
		addButton(new OuyaControllerButton(this, ButtonID.HOME, "OUYA", "jplay.ouya.button.OUYA", this.wrappedController));
		addButton(new OuyaControllerButton(this, ButtonID.MENU, "OUYA", "jplay.ouya.button.OUYA", this.wrappedController));
		addButton(new OuyaControllerButton(this, ButtonID.PAUSE, "OUYA", "jplay.ouya.button.OUYA", this.wrappedController));
		addButton(new OuyaControllerButton(this, ButtonID.START, "O", "jplay.ouya.button.O", this.wrappedController));
/*		
		addButton(new OuyaControllerButton(this, ButtonID.TRIGGER_LEFT_UP, "Left upper trigger", "jplay.ouya.button.leftUpperTrigger", this.wrappedController));
		addButton(new OuyaControllerButton(this, ButtonID.TRIGGER_LEFT_DOWN, "Left lower trigger", "jplay.ouya.button.leftLowerTrigger", this.wrappedController));
		addButton(new OuyaControllerButton(this, ButtonID.TRIGGER_RIGHT_UP, "Right upper trigger", "jplay.ouya.button.rightUpperTrigger", this.wrappedController));
		addButton(new OuyaControllerButton(this, ButtonID.TRIGGER_RIGHT_DOWN, "Right lower trigger", "jplay.ouya.button.rightLowerTrigger", this.wrappedController));
		*/
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
		if(stick == StickID.LEFT_ANALOG) {
			return this.sticks[LEFT_STICK];
		} else if(stick == StickID.RIGHT_ANALOG) {
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
