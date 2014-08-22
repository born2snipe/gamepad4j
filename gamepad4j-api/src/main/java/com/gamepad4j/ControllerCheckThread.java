/*
 * @Copyright: Marcel Schoen, Switzerland, 2013, All Rights Reserved.
 */

package com.gamepad4j;

/**
 * This class can optionally be used to check for controller updates 
 * (especially connects / disconnects) on a regular basis, which may
 * be needed if the underlying backend doesn't support that.
 *
 * @author Marcel Schoen
 * @version $Revision: $
 */
public class ControllerCheckThread implements Runnable {

	/** Running flag. */
	private static boolean running = true;

	/**
	 * Stops this thread.
	 */
	public static void stop() {
		running = false;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		while(running) {
			Controllers.checkControllers();
			try {
				Thread.sleep(1000);
			} catch(Exception ex) {
				// ignore
			}
		}
	}
}
