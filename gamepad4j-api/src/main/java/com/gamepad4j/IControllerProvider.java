/*
 * @Copyright: Marcel Schoen, Switzerland, 2013, All Rights Reserved.
 */

package com.gamepad4j;


/**
 * Interface for platform-specific provider for instances
 * of IController.
 *
 * @author Marcel Schoen
 * @version $Revision: $
 */
public interface IControllerProvider {

	/**
	 * Invoked once; may perform any kind of initialization.
	 */
	void initialize();
	
	/**
	 * Invoked once when the game terminates. Can be used
	 * to free native stuff in JNI wrappers etc.
	 */
	void release();
	
	/**
	 * Checks the state of all controllers. This method is 
	 * invoked by the update-thread which may be used if the
	 * underlying implementation does not support the callback
	 * listeners.
	 */
	void checkControllers();
	
	/**
	 * In case the implementation does not support callbacks,
	 * this method must be able to provide an array with all
	 * currently connected controllers.
	 * 
	 * @return The currently connected controllers.
	 */
	//IController[] getControllers();
	
	/**
	 * Must inform the caller if the implementation supports
	 * callbacks for events such as controller connects / disconnects etc.
	 * 
	 * @return True if the implementation supports callbacks.
	 */
	boolean supportsCallbacks();
	
	/**
	 * Registers a listener for controller events.
	 * 
	 * @param listener The controller listener.
	 */
	public void addListener(IControllerListener listener);
	
	/**
	 * Removes a listener for controller events.
	 * 
	 * @param listener The controller listener to remove.
	 */
	public void removeListener(IControllerListener listener);
}
