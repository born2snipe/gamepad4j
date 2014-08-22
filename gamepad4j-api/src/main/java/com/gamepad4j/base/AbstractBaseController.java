/*
 * @Copyright: Marcel Schoen, Switzerland, 2013, All Rights Reserved.
 */

package com.gamepad4j.base;

import java.util.HashMap;
import java.util.Map;

import com.gamepad4j.ButtonID;
import com.gamepad4j.DpadDirection;
import com.gamepad4j.IButton;
import com.gamepad4j.IController;

/**
 * Base class for controller wrappers.
 *
 * @author Marcel Schoen
 * @version $Revision: $
 */
public abstract class AbstractBaseController implements IController {

	/** Stores the ID of this controller. */
	protected String ID = null;

	/** Stores the last d-pad direction. */
	protected DpadDirection lastDirection = null;

	/** Lookup map for buttons based on their type. */
	private Map<ButtonID, IButton> buttonMap = new HashMap<ButtonID, IButton>();
	
	/** Lookup map for buttons based on their numeric code. */
	private Map<Integer, IButton> buttonCodeMap = new HashMap<Integer, IButton>();
	
	/** Stores the available buttons. */
	private IButton[] buttons = null;
	
	/**
	 * Creates a controller wrapper.
	 * 
	 * @param id The ID of the controller.
	 */
	protected AbstractBaseController(String id) {
		this.ID = id;
		if(this.ID == null) {
			this.ID = "unknown";
		}
	}
	
	/**
	 * Adds a button to the map of buttons.
	 * 
	 * @param button The button to add.
	 */
	protected void addButton(IButton button) {
		this.buttonMap.put(button.getID(), button);
		this.buttonCodeMap.put(button.getCode(), button);
	}
	
	/* (non-Javadoc)
	 * @see com.gamepad4j.util.IController#getID()
	 */
	@Override
	public String getID() {
		return this.ID;
	}

	/* (non-Javadoc)
	 * @see com.gamepad4j.util.IController#getDpadDirectionOnce()
	 */
	@Override
	public DpadDirection getDpadDirectionOnce() {
		DpadDirection current = getDpadDirection();
		if(current != lastDirection) {
			lastDirection = current;
			return current;
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.gamepad4j.util.IController#isButtonPressedOnce(com.gamepad4j.util.ButtonID)
	 */
	@Override
	public boolean isButtonPressedOnce(ButtonID buttonID) {
		return false;
//		return getButton(buttonID).isPressedOnce();
	}

	/* (non-Javadoc)
	 * @see com.gamepad4j.util.IController#getButtons()
	 */
	@Override
	public IButton[] getButtons() {
		if(this.buttons == null) {
			// Some best-effort completions
			// If either BACK or CANCEL was not set, use the same value for both
			// (they are usually the same button)
			if(this.buttonMap.get(ButtonID.BACK) == null && this.buttonMap.get(ButtonID.CANCEL) != null) {
				this.buttonMap.put(ButtonID.BACK, this.buttonMap.get(ButtonID.CANCEL));
			}
			if(this.buttonMap.get(ButtonID.BACK) != null && this.buttonMap.get(ButtonID.CANCEL) == null) {
				this.buttonMap.put(ButtonID.CANCEL, this.buttonMap.get(ButtonID.BACK));
			}
			// The same goes for PAUSE and MENU
			if(this.buttonMap.get(ButtonID.PAUSE) == null && this.buttonMap.get(ButtonID.MENU) != null) {
				this.buttonMap.put(ButtonID.PAUSE, this.buttonMap.get(ButtonID.MENU));
			}
			if(this.buttonMap.get(ButtonID.PAUSE) != null && this.buttonMap.get(ButtonID.MENU) == null) {
				this.buttonMap.put(ButtonID.MENU, this.buttonMap.get(ButtonID.PAUSE));
			}
			this.buttons = new IButton[this.buttonMap.size()];
			int ct = 0;
			for(IButton button : this.buttonMap.values()) {
				this.buttons[ct] = button;
				ct ++;
			}
		}
		return this.buttons;
	}

	/* (non-Javadoc)
	 * @see com.gamepad4j.util.IController#getButton(int)
	 */
	@Override
	public IButton getButton(int buttonCode) {
		return this.buttonCodeMap.get(buttonCode);
	}

	/* (non-Javadoc)
	 * @see com.gamepad4j.util.IController#getButton(com.gamepad4j.util.ButtonID)
	 */
	@Override
	public IButton getButton(ButtonID buttonID) {
		return this.buttonMap.get(buttonID);
	}

	/* (non-Javadoc)
	 * @see com.gamepad4j.util.IController#isButtonPressed(com.gamepad4j.util.ButtonID)
	 */
	@Override
	public boolean isButtonPressed(ButtonID buttonID) {
		return this.buttonMap.get(buttonID).isPressed();
	}

	/* (non-Javadoc)
	 * @see com.gamepad4j.util.IController#getAnalogButtonPressure(com.gamepad4j.util.ButtonID)
	 */
	@Override
	public float getAnalogButtonPressure(ButtonID buttonID)
			throws IllegalArgumentException {
		return this.buttonMap.get(buttonID).analogValue();
	}

}
