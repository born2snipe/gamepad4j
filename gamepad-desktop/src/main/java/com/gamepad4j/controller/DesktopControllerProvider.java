/*
 * @Copyright: Marcel Schoen, Switzerland, 2014, All Rights Reserved.
 */

package com.gamepad4j.controller;

import java.util.HashMap;
import java.util.Map;

import tv.ouya.console.api.OuyaController;

import com.gamepad4j.ButtonID;
import com.gamepad4j.ControllerListenerAdapter;
import com.gamepad4j.IButton;
import com.gamepad4j.IController;
import com.gamepad4j.IControllerListener;
import com.gamepad4j.IControllerProvider;
import com.gamepad4j.StickID;
import com.jplay.gamepad.ouya.OuyaControllerWrapper;

/**
 * Controller provider for desktop systems (Linux, MacOS X, Windows).
 *
 * @author Marcel Schoen
 * @version $Revision: $
 */
public class DesktopControllerProvider implements IControllerProvider, IControllerListener {
	private static final int MAX_CONTROLLERS = 8;
	
	/** Stores controller listeners. */
	private ControllerListenerAdapter listeners = new ControllerListenerAdapter();

	private GamepadJniWrapper jniWrapper = null;

	/** Map of all connected controllers (deviceID / controller). */
	private Map<Integer, IController> connected = new HashMap<Integer, IController>();
	
	/** Stores the array of controllers. */
	private IController[] controllerArray = null;
	
	/** Stores the number of connected controllers. */
	private int numberOfControllers = -1;
	
	/* (non-Javadoc)
	 * @see com.gamepad4j.IControllerProvider#initialize()
	 */
	@Override
	public void initialize() {
		jniWrapper = new GamepadJniWrapper(this);
		jniWrapper.initialize();
		System.out.flush();
	}

	/* (non-Javadoc)
	 * @see com.gamepad4j.IControllerProvider#release()
	 */
	@Override
	public void release() {
		jniWrapper.natRelease();
	}

	/* (non-Javadoc)
	 * @see com.gamepad4j.IControllerProvider#checkControllers()
	 */
	@Override
	public void checkControllers() {
		boolean update = false;
		jniWrapper.natDetectPads();
		this.numberOfControllers = jniWrapper.natGetNumberOfPads();
		for(int ct = 0; ct < this.numberOfControllers; ct++) {
			int deviceId = jniWrapper.natGetIdOfPad(ct);
			if(deviceId != -1) {
				if(connected.get(deviceId) == null) {
					// Newly connected controller found
					update = true;
					System.out.println("Newly connected controller found.");
				}
			}
		}
		if(update) {
			updateControllers();
		}
	}

	/**
	 * Updates the array of controllers. Invoke only when
	 * an update is really necessary (because this method creates new
	 * wrapper objects and changes the map of controllers).
	 */
	private void updateControllers() {
		this.connected.clear();
		this.numberOfControllers = jniWrapper.natGetNumberOfPads();
		for(int ct = 0; ct < this.numberOfControllers; ct++) {
			int deviceId = jniWrapper.natGetIdOfPad(ct);
			if(deviceId != -1) {
				DesktopController desktopController = new DesktopController(ct);
				jniWrapper.updateControllerStatus(desktopController);
				this.connected.put(desktopController.getDeviceID(), desktopController);
				for(IControllerListener listener : this.listeners.getListeners()) {
					listener.connected(desktopController);
				}
			}
		}
		// Re-adjust stored values to number of actually found valid controllers
		this.numberOfControllers = this.connected.size();
		this.controllerArray = new IController[this.numberOfControllers];
		int ct = 0;
		for(IController controller : this.connected.values()) {
			this.controllerArray[ct++] = controller;
		}
	}

	/* (non-Javadoc)
	 * @see com.gamepad4j.IControllerProvider#getControllers()
	 */
	@Override
	public IController[] getControllers() {
		return this.controllerArray;
	}

	/* (non-Javadoc)
	 * @see com.gamepad4j.IControllerProvider#supportsCallbacks()
	 */
	@Override
	public boolean supportsCallbacks() {
		return false;
	}
	
	/* (non-Javadoc)
	 * @see com.gamepad4j.util.IControllerProvider#addListener(com.gamepad4j.util.IControllerListener)
	 */
	@Override
	public void addListener(IControllerListener listener) {
		this.listeners.addListener(listener);
	}

	/* (non-Javadoc)
	 * @see com.gamepad4j.util.IControllerProvider#removeListener(com.gamepad4j.util.IControllerListener)
	 */
	@Override
	public void removeListener(IControllerListener listener) {
		this.listeners.removeListener(listener);
	}

	/* (non-Javadoc)
	 * @see com.gamepad4j.IControllerListener#connected(com.gamepad4j.IController)
	 */
	@Override
	public void connected(IController controller) {
		this.connected.put(controller.getDeviceID(), controller);
	}

	/* (non-Javadoc)
	 * @see com.gamepad4j.IControllerListener#disConnected(com.gamepad4j.IController)
	 */
	@Override
	public void disConnected(IController controller) {
		this.connected.remove(controller.getDeviceID());
	}

	/* (non-Javadoc)
	 * @see com.gamepad4j.IControllerListener#buttonDown(com.gamepad4j.IController, com.gamepad4j.IButton, com.gamepad4j.ButtonID)
	 */
	@Override
	public void buttonDown(IController controller, IButton button,
			ButtonID buttonID) {
		for(IControllerListener listener : this.listeners.getListeners()) {
			listener.buttonDown(controller, button, buttonID);
		}
	}

	/* (non-Javadoc)
	 * @see com.gamepad4j.IControllerListener#buttonUp(com.gamepad4j.IController, com.gamepad4j.IButton, com.gamepad4j.ButtonID)
	 */
	@Override
	public void buttonUp(IController controller, IButton button,
			ButtonID buttonID) {
		for(IControllerListener listener : this.listeners.getListeners()) {
			listener.buttonUp(controller, button, buttonID);
		}
	}

	/* (non-Javadoc)
	 * @see com.gamepad4j.IControllerListener#moveStick(com.gamepad4j.IController, com.gamepad4j.StickID)
	 */
	@Override
	public void moveStick(IController controller, StickID stick) {
		for(IControllerListener listener : this.listeners.getListeners()) {
			listener.moveStick(controller, stick);
		}
	}
}
