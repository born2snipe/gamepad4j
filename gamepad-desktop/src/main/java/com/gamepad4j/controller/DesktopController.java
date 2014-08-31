/*
 * @Copyright: Marcel Schoen, Switzerland, 2014, All Rights Reserved.
 */

package com.gamepad4j.controller;

import java.util.HashMap;
import java.util.Map;

import com.gamepad4j.AxisID;
import com.gamepad4j.ButtonID;
import com.gamepad4j.DpadDirection;
import com.gamepad4j.IButton;
import com.gamepad4j.IStick;
import com.gamepad4j.StickID;
import com.gamepad4j.base.AbstractBaseController;
import com.gamepad4j.base.BaseButton;

/**
 * Holder for status information about a desktop controller.
 *
 * @author Marcel Schoen
 * @version $Revision: $
 */
public class DesktopController extends AbstractBaseController {

	/** Stores the controller index value. */
	private int index = -1;
	
	/** Flag for tracking check status. */
	private boolean checked = false;
	
	IStick[] sticks = new IStick[0];
	Map<StickID, IStick> stickMap = new HashMap<StickID, IStick>();

	/**
	 * Creates a desktop controller holder for a certain index.
	 * 
	 * @param index The index number of the controller.
	 */
	public DesktopController(int index) {
		// For now, create instance with "wrong" device ID 0.
		// Real value will be updated later through setter method.
		super(0);
		this.index = index;
	}
	
	/**
	 * @return the checked
	 */
	public boolean isChecked() {
		return checked;
	}

	/**
	 * @param checked the checked to set
	 */
	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	/**
	 * Returns the index value of this desktop controller holder.
	 * 
	 * @return The index value.
	 */
	public int getIndex() {
		return this.index;
	}
	
	/**
	 * Sets the index for this controller holder.
	 * 
	 * @param index The index value.
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	/* (non-Javadoc)
	 * @see com.gamepad4j.IController#getSticks()
	 */
	@Override
	public IStick[] getSticks() {
		return this.sticks;
	}

	/* (non-Javadoc)
	 * @see com.gamepad4j.IController#getStick(com.gamepad4j.StickID)
	 */
	@Override
	public IStick getStick(StickID stick) throws IllegalArgumentException {
		return stickMap.get(stick);
	}

	/* (non-Javadoc)
	 * @see com.gamepad4j.IController#getAxis(com.gamepad4j.StickID, com.gamepad4j.AxisID)
	 */
	@Override
	public float getAxis(StickID stick, AxisID axis) {
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.gamepad4j.IController#getDpadDirection()
	 */
	@Override
	public DpadDirection getDpadDirection() {
		// TODO Auto-generated method stub
		return null;
	}
}
