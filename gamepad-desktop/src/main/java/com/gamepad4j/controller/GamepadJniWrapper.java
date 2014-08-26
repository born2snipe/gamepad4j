/*
 * @Copyright: Marcel Schoen, Switzerland, 2013, All Rights Reserved.
 */

package com.gamepad4j.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.gamepad4j.Controllers;
import com.gamepad4j.IController;
import com.gamepad4j.IControllerListener;
import com.gamepad4j.util.PlatformUtil;

/**
 * JNI wrapper for Gamepad library by Alex Diener.
 *
 * @author Marcel Schoen
 * @version $Revision: $
 */
public class GamepadJniWrapper {

	/** 
	 * Reference to the controller event listener,
	 * which is usually the DesktopControllerProvider instance. 
	 */
//	private IControllerListener listener;
	
	/**
	 * Prepares the native library for usage. This method
	 * must be invoked before anything else is used.
	 */
//	public GamepadJniWrapper(IControllerListener listener) {
	public GamepadJniWrapper() {
//		this.listener = listener;
		// Since there is no really portable way of detecting if a Java
		// program is currently running on a 32 or 64 bit system, we try
		// to load the 64 bit library first, and if that fails, fall back
		// to the 32 bit library.
		File libraryFile = unpackLibrary(true);
		if (!loadLibrary(libraryFile)) {
			libraryFile = unpackLibrary(false);
			if (!loadLibrary(libraryFile)) {
				System.err.println("*** FAILED TO LOAD EITHER 32 OR 64 BIT LIBRARY ***");
			}
		}
	}

	/**
	 * Callback method invoked from the native wrapper when a gamepad
	 * is disconnected.
	 * 
	 * @param deviceID The device ID of the disconnected gamepad.
	 */
	public void callbackDisconnectDevice(int deviceID) {
		System.out.println("*** Device disconnected / ID: " + deviceID + " ***");
	}

	/**
	 * Callback method invoked from the native wrapper when a gamepad
	 * is connected.
	 * 
	 * @param deviceID The device ID of the connected gamepad.
	 */
	public void callbackConnectDevice(int deviceID) {
		System.out.println("*** Device connected / ID: " + deviceID + " ***");
		/*
		DesktopController newController = new DesktopController(0);
		updateControllerStatus(newController);
		System.out.println("-> CONTROLLER: deviceID=" + newController.getDeviceID() + " / DESC=" + newController.getDescription());
		System.out.println("-> CONTROLLER: vendorID=" + newController.getVendorID() + " / productID=" + newController.getProductID());
		*/
	}
	
	/**
	 * Unpacks the JNI library to a temporary directory and sets the
	 * "java.library.path" to that directory.
	 * 
	 * @param use64bit
	 *            True, if the 64 bit version of the library should be used.
	 */
	private static File unpackLibrary(boolean use64bit) {
		try {
			File temp = File.createTempFile("nativelib", "");
			temp.delete();
			temp.mkdir();
			System.out.println("Using temporary directory: " + temp.getAbsolutePath());
			System.out.println("exists: " + temp.exists());
			System.out.println("Is directory: " + temp.isDirectory());
			if (temp.exists() && temp.isDirectory()) {
				String subPath = "/32bit/";
				if (use64bit) {
					subPath = "/64bit/";
				}
				String library = PlatformUtil.getPlatform().getLibraryName();
				String path = "/native/" + PlatformUtil.getPlatform().name()
						+ subPath;
				String resourceName = path + library;
				File libraryFile = new File(temp, library);
				System.out.println("Extracting library resource: " + resourceName);
				System.out.println("Extracting to local file: " + libraryFile.getCanonicalPath());
				InputStream in = GamepadJniWrapper.class.getResourceAsStream(resourceName);
				if(in != null) {
					FileOutputStream out = new FileOutputStream(libraryFile);
					byte[] buffer = new byte[64000];
					int num = -1;
					while ((num = in.read(buffer)) > 0) {
						out.write(buffer, 0, num);
					}
					out.flush();
					out.close();
					in.close();
					System.setProperty("java.library.path", temp.getCanonicalPath());
					System.out.println("Library successfully extracted.");
				} else {
					throw new IOException("Resource not found in classpath: " + resourceName);
				}
				return libraryFile;
			}
		} catch (IOException e) {
			System.err.println("*** FAILED TO EXTRACT LIBRARY ***");
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Tries to load the currently unpacked library.
	 * 
	 * @return True if the library could be loaded without errors.
	 */
	private static boolean loadLibrary(File libraryFile) {
		try {
			if(libraryFile == null) {
				throw new IllegalArgumentException("There must be a native library.");
			}
			System.out.println("Trying to load library: " + libraryFile.getCanonicalPath());
			System.load(libraryFile.getCanonicalPath());
			System.out.println("Gamepad4j native library successfully loaded.");
		} catch (Throwable t) {
			t.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * Updates the status information on the given controller holder object.
	 * 
	 * @param controller
	 * @return
	 */
	public void updateControllerStatus(DesktopController controller) {
		String description = natGetControllerDescription(controller.getIndex());
		controller.setDescription(description);
		int[] idArray = new int[3];
		natGetControllerIDs(controller.getIndex(), idArray);
		controller.setDeviceID(idArray[0]);
		controller.setVendorID(idArray[1]);
		controller.setProductID(idArray[2]);
	}
	

	
	public native String natGetControllerDescription(int index);
	public native void natGetControllerIDs(int index, int[] idArray);
	
	/**
	 * Initializes the JNI wrapper.
	 */
	public void initialize() {
		System.out.println("---- initialize JNI wrapper... ----");
		natInit();
		System.out.println("---- detect pads... ----");
		natDetectPads();
		System.out.println("---- done ----");
	}
	
	/**
	 * Initializes the native gamepad library.
	 */
	public native void natInit();

	/**
	 * Releases all resources held by the native gamepad library.
	 */
	public native void natRelease();
	
	/**
	 * Returns the number of gamepads currently connected.
	 * 
	 * @return The number of pads.
	 */
	public native int natGetNumberOfPads();

	/**
	 * Returns the ID of the pad with the given index.
	 * 
	 * @param index The index of the pad.
	 * @return The ID (if such a pad exists).
	 */
	public native int natGetIdOfPad(int index);
	
	/**
	 * Forces detection of connected gamepads.
	 */
	public native void natDetectPads();

	// private native IController natGetControllerAt(int index);
	/*
	public static void main(String[] args) {
		try {
			System.out.println("----------- BEGIN -------------");
			GamepadJniWrapper wrapper = new GamepadJniWrapper();
			wrapper.natInit();

			int numberOfPads = wrapper.natGetNumberOfPads();
			long start = System.currentTimeMillis();
			while (System.currentTimeMillis() - start < 20000) {
				int newNumberOfPads = wrapper.natGetNumberOfPads();
				if(newNumberOfPads != numberOfPads) {
					numberOfPads = newNumberOfPads;
					System.out.println("Number of pads: " + numberOfPads);
				}
			}

			wrapper.natRelease();
			System.out.println("----------- END -------------");
		} catch (Exception ex) {
			ex.printStackTrace();
			System.exit(-1);
		}
	}
	*/
}
