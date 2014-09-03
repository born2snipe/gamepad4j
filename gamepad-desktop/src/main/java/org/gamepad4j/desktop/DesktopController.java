/*
 * @Copyright: Marcel Schoen, Switzerland, 2014, All Rights Reserved.
 */

package org.gamepad4j.desktop;

import org.gamepad4j.base.AbstractBaseController;

/**
 * Holder for status information about a desktop controller.
 *
 * @author Marcel Schoen
 * @version $Revision: $
 */
public class DesktopController extends AbstractBaseController {

	/** Stores the controller code value. */
	private int index = -1;
	
	/** Flag for tracking check status. */
	private boolean checked = false;

	/**
	 * Creates a desktop controller holder for a certain code.
	 * 
	 * @param code The code number of the controller.
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
	 * Returns the code value of this desktop controller holder.
	 * 
	 * @return The code value.
	 */
	public int getIndex() {
		return this.index;
	}
	
	/**
	 * Sets the code for this controller holder.
	 * 
	 * @param code The code value.
	 */
	public void setIndex(int index) {
		this.index = index;
	}
}
