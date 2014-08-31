/*
 * @Copyright: Marcel Schoen, Switzerland, 2014, All Rights Reserved.
 */

package com.gamepad4j.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.gamepad4j.ControllerListenerAdapter;
import com.gamepad4j.IControllerListener;
import com.gamepad4j.IControllerProvider;
import com.gamepad4j.util.Log;

/**
 * Controller provider for desktop systems (Linux, MacOS X, Windows).
 *
 * @author Marcel Schoen
 * @version $Revision: $
 */
public class DesktopControllerProvider implements IControllerProvider {
	
	/** Stores controller listeners. */
	private ControllerListenerAdapter listeners = new ControllerListenerAdapter();

	public static GamepadJniWrapper jniWrapper = null;

	/** Map of all connected controllers (deviceID / controller). */
	private static Map<Integer, DesktopController> connected = new HashMap<Integer, DesktopController>();
	
	/** Stores the controllers instance pool. */
	private static DesktopController[] controllerPool = new DesktopController[16];
	
	/** Stores the number of connected controllers. */
	private static int numberOfControllers = -1;
	
	/* (non-Javadoc)
	 * @see com.gamepad4j.IControllerProvider#initialize()
	 */
	@Override
	public void initialize() {
		jniWrapper = new GamepadJniWrapper();
		jniWrapper.initialize();
		for(int i = 0; i < controllerPool.length; i++) {
			controllerPool[i] = new DesktopController(-1);
		}
		System.out.flush();
	}

	/* (non-Javadoc)
	 * @see com.gamepad4j.IControllerProvider#release()
	 */
	@Override
	public void release() {
		jniWrapper.natRelease();
	}

	/**
	 * Returns a controller holder instance from the pool and
	 * sets its index to the given value.
	 * 
	 * @param index The index to set for the controller instance.
	 * @return The controller holder (or null if there was none free).
	 */
	private synchronized static DesktopController getInstanceFromPool(int index) {
		for(int i = 0; i < controllerPool.length; i++) {
			if(controllerPool[i] != null) {
				DesktopController reference = controllerPool[i]; 
				reference.setIndex(index);
//				connected.put(index, reference);
				controllerPool[i] = null;
				return reference;
			}
		}
		return null;
	}

	/**
	 * Returns the given controller instance to the pool.
	 * 
	 * @param controller The controller instance to return (must not be null).
	 */
	private synchronized static void returnInstanceToPool(DesktopController controller) {
		for(int i = 0; i < controllerPool.length; i++) {
			if(controllerPool[i] == null) {
				controllerPool[i] = controller;
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see com.gamepad4j.IControllerProvider#checkControllers()
	 */
	@Override
	public synchronized void checkControllers() {
		jniWrapper.natDetectPads();
		for(DesktopController controller : this.connected.values()) {
			controller.setChecked(false);
		}
		
		// 1st check which controllers are (still) connected
		int newNumberOfControllers = jniWrapper.natGetNumberOfPads();
		if(newNumberOfControllers != this.numberOfControllers) {
			this.numberOfControllers = newNumberOfControllers;
			Log.log("Number of controllers: " + this.numberOfControllers);
		}
//		Log.log("Check for newly connected controllers...");
		for(int ct = 0; ct < this.numberOfControllers; ct++) {
			int connectedId = jniWrapper.natGetDeviceID(ct);
			if(connectedId != -1) {
				DesktopController controller = this.connected.get(connectedId);
				if(controller != null) {
					controller.setChecked(true);
				} else {
					DesktopController newController = getInstanceFromPool(ct);
					if(newController == null) {
						throw new IllegalStateException("** DesktopController instance pool exceeded! **");
					}
					newController.setChecked(true);
					jniWrapper.updateControllerInfo(newController);
					this.connected.put(newController.getDeviceID(), newController);
					Log.log("***********************************************************************");
					Log.log("Newly connected controller found: " + newController.getDeviceID()
							+ " (" + Integer.toHexString(newController.getVendorID()) + "/"
							+ Integer.toHexString(newController.getProductID()) 
							+ ") / " + newController.getDescription());
					Log.log("***********************************************************************");
					listeners.getListeners().get(0).connected(newController);
				}
			}
		}

		// 2nd remove the controllers not found in the first loop
//		Log.log("Check for disconnected controllers...");
		Iterator<Entry<Integer, DesktopController>> iter = this.connected.entrySet().iterator();
		while(iter.hasNext()) {
			Entry<Integer, DesktopController> entry = iter.next();
			DesktopController controller = entry.getValue();
			if(!controller.isChecked()) {
				Log.log("Controller disconnected: " + controller.getDeviceID() + " / " + controller.getDescription());
				listeners.getListeners().get(0).disConnected(controller);
				returnInstanceToPool(controller);
				// Must be removed from map with iterator, otherwise
				// ConcurrentModificationException will occur
				iter.remove();
			}
		}
		
		// 3rd update the state of all remaining controllers
//		Log.log("Update controllers...");
		for(DesktopController controller : this.connected.values()) {
			jniWrapper.updateControllerStatus(controller);
		}
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
}
