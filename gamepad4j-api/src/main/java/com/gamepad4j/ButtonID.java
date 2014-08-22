/*
 * @Copyright: Marcel Schoen, Switzerland, 2013, All Rights Reserved.
 */

package com.gamepad4j;

/**
 * Enumeration of IDs of gamepad buttons.
 *
 * @author Marcel Schoen
 * @version $Revision: $
 */
public enum ButtonID {

	FACE_UP("Upper face-button"),
	FACE_DOWN("Lower face-button"),
	FACE_LEFT("Left face-button"),
	FACE_RIGHT("Right face-button"),
	
	TRIGGER1_LEFT("Left upper trigger"),
	TRIGGER2_LEFT("Left lower trigger"),
	TRIGGER1_RIGHT("Right upper trigger"),
	TRIGGER2_RIGHT("Right lower trigger"),

	LEFT_ANALOG_STICK("Left analog stick"),
	RIGHT_ANALOG_STICK("Right analog stick"),
	
	ACCEPT("Accept"),
	CANCEL("Cancel"),
	MENU("Menu"),
	HOME("Home"),
	BACK("Back"),
	PAUSE("Pause"),
	START("Start"),
	
	D_PAD_UP("D-Pad Up"),
	D_PAD_DOWN("D-Pad Down"),
	D_PAD_LEFT("D-Pad Left"),
	D_PAD_RIGHT("D-Pad Right"),
	
	UNKNOWN("Unknown")
	;
	
	/** Stores this buttons default text label. */
	private String defaultLabel = "";
	
	/**
	 * Creates a button ID holder.
	 * 
	 * @param label The default text label for this button.
	 */
	ButtonID(String label) {
		this.defaultLabel = label;
	}
	
	/**
	 * Returns the default text label for this button.
	 * 
	 * @return The default text label.
	 */
	public String getLabel() {
		return this.defaultLabel;
	}
}
