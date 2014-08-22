/*
 * @Copyright: Marcel Schoen, Switzerland, 2013, All Rights Reserved.
 */

package com.gamepad4j;

/**
 * Interface for handling a game controller. 
 *
 * @author Marcel Schoen
 * @version $Revision: $
 */
public interface IController {

	/**
	 * Returns the ID of the joypad controller.
	 * 
	 * @return The controller ID string.
	 */
	String getID();

	/**
	 * Returns all the sticks of this controller. This also includes
	 * touchpads, accelerometers etc. Basically every kind of analog
	 * multi-axis input.
	 * 
	 * @return The sticks on this controller.
	 */
	IStick[] getSticks();
	
	/**
	 * Returns the given stick.
	 * 
	 * @param stick The stick to return.
	 * @return The requested stick.
	 * @throws IllegalArgumentException If there is no such stick.
	 */
	IStick getStick(StickID stick) throws IllegalArgumentException;
	
	/**
	 * Returns the value of the given axis of the given stick (which
	 * may also be a touchpad, accelerometer etc.).
	 * 
	 * @param stick The ID of the stick to check.
	 * @param axis The axis to read.
	 * @return The value of the axis (between -1.0 and 1.0).
	 */
	float getAxis(StickID stick, AxisID axis);
	
	/**
	 * Returns the current direction on the D-pad.
	 * 
	 * @return The current direction.
	 */
	DpadDirection getDpadDirection();
	
	/**
	 * Returns the current direction on the D-pad, but only for one single invocation. 
	 * After that, the method will return "DpadDirection.NONE", even if the d-pad is still 
	 * pressed, until it is released or changes direction. This can be used when the user 
	 * should press the d-pad repeatedly instead of just keeping it pressed.
	 * 
	 * @return The current direction.
	 */
	DpadDirection getDpadDirectionOnce();
	
	/**
	 * Returns a list of all buttons of this controller.
	 * 
	 * @return The buttons on this controller.
	 */
	IButton[] getButtons();

	/**
	 * Returns a reference to a specific button on this controller. This method
	 * should be used for "non-standard" buttons for which none of the predefined
	 * button IDs work.
	 * 
	 * @param buttonCode The numeric code of this button.
	 * @return The reference of that button (null if it does not exist).
	 */
	IButton getButton(int buttonCode);
	
	/**
	 * Returns a reference to a specific button on this controller.
	 * 
	 * @param buttonID The ID of the given button.
	 * @return The reference of that button (null if it does not exist).
	 */
	IButton getButton(ButtonID buttonID);
	
	/**
	 * Convenience method for checking if a given button (digital or analog)
	 * is currently pressed.
	 * 
	 * @param buttonID The ID of the button.
	 * @return True if it's pressed.
	 */
	boolean isButtonPressed(ButtonID buttonID);
	
	/**
	 * Convenience method for checking if a given button (digital or analog)
	 * is currently pressed, but only for one single invocation. After that,
	 * the method will return 'false', even if the button is still pressed,
	 * until it is released once. This can be used when the user should press
	 * the button repeatedly instead of just keeping it pressed.
	 * 
	 * @param buttonID The ID of the button.
	 * @return True if it's pressed.
	 */
	boolean isButtonPressedOnce(ButtonID buttonID);
	
	/**
	 * Returns the current pressure on the given analog button.
	 * 
	 * @param buttonID 
	 * @return The pressure as a value between 0.0 (not pressed) and 1.0 (fully pressed).
	 * @throws IllegalArgumentException If the given button is not an analog button.
	 */
	float getAnalogButtonPressure(ButtonID buttonID) throws IllegalArgumentException;
}
