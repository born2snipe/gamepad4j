/*
 * @Copyright: Marcel Schoen, Switzerland, 2014, All Rights Reserved.
 */

package com.gamepad4j.test;

import com.gamepad4j.Controllers;

/**
 * ...
 *
 * @author Marcel Schoen
 * @version $Revision: $
 */
public class GamepadCheck implements Runnable {

	public static boolean running = true;
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		while(running) {
			Controllers.checkControllers();
			try {
				Thread.sleep(150);
			} catch (InterruptedException e) {
				// ignore
			}
		}
	}

}
