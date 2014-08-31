/*
 * @Copyright: Marcel Schoen, Switzerland, 2014, All Rights Reserved.
 */

package com.gamepad4j.test;

import com.gamepad4j.ButtonID;
import com.gamepad4j.Controllers;
import com.gamepad4j.IButton;
import com.gamepad4j.IController;

/**
 * Simulates game loop.
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
			IController[] controllers = Controllers.getControllers();
			if(controllers != null && controllers.length > 0) {
				IButton acceptButton = controllers[0].getButton(ButtonID.ACCEPT);
				if(acceptButton != null && acceptButton.isPressedOnce()) {
					System.out.println("*** ACCEPT ***");
				}
				IButton cancelButton = controllers[0].getButton(ButtonID.CANCEL);
				if(cancelButton != null && cancelButton.isPressedOnce()) {
					System.out.println("*** CANCEL / DENY ***");
				}
			}
			try {
				Thread.sleep(150);
			} catch (InterruptedException e) {
				// ignore
			}
		}
	}

}
