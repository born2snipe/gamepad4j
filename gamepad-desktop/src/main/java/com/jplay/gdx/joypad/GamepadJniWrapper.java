/*
 * @Copyright: Marcel Schoen, Switzerland, 2013, All Rights Reserved.
 */

package com.jplay.gdx.joypad;

/**
 * JNI wrapper for Gamepad library by Alex Diener.
 *
 * @author Marcel Schoen
 * @version $Revision: $
 */
public class GamepadJniWrapper {

	   static {
		      System.loadLibrary("gamepad-jni-wrapper"); // hello.dll (Windows) or libhello.so (Unixes)
	   }
	   
	   private native void natInit();
	 
	   private native void natRelease();

	   private native int natGetNumberOfPads();

	   private native void natDetectPads();

//	   private native IController natGetControllerAt(int index);
	   public static void main(String[] args) {
		   try {
			   System.out.println("----------- BEGIN -------------");
			   GamepadJniWrapper wrapper = new GamepadJniWrapper();
			   wrapper.natInit();
			   int numberOfPads = wrapper.natGetNumberOfPads();
			   System.out.println("Number of pads: " + numberOfPads);
			   wrapper.natRelease();
			   System.out.println("----------- END -------------");
		   } catch(Exception ex) {
			   ex.printStackTrace();
			   System.exit(-1);
		   }
	   }
}
