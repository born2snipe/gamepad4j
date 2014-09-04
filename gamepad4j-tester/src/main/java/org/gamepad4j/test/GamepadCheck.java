/*
 * @Copyright: Marcel Schoen, Switzerland, 2014, All Rights Reserved.
 */

package org.gamepad4j.test;

import org.gamepad4j.AxisID;
import org.gamepad4j.ButtonID;
import org.gamepad4j.Controllers;
import org.gamepad4j.DpadDirection;
import org.gamepad4j.IButton;
import org.gamepad4j.IController;
import org.gamepad4j.IStick;
import org.gamepad4j.ITrigger;
import org.gamepad4j.StickID;
import org.gamepad4j.TriggerID;

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
				
				ITrigger triggerLeft = controllers[0].getTrigger(TriggerID.LEFT_DOWN);
				if(triggerLeft == null) {
					System.err.println("no left trigger button found");
				} else {
					float trigger = triggerLeft.analogValue();
					if(trigger < -0.2 || trigger > 0.2) {
						System.out.println("> left trigger button: " + trigger);
					}
				}

				DpadDirection dpad = controllers[0].getDpadDirection();
				if(dpad != DpadDirection.NONE) {
					System.out.println("D-Pad: " + dpad);
				}
				
				IStick leftStick = controllers[0].getStick(StickID.LEFT);
				if(leftStick == null) {
					System.err.println("no left stick found");
				} else {
					/*
					DpadDirection stickDpad = leftStick.getPosition().getDirection();
					if(stickDpad !=	DpadDirection.NONE) {
						System.out.println(">> left stick as d-pad: " + stickDpad);
					}
					*/
					
					float xAxis = leftStick.getAxis(AxisID.X).getValue();
					
					// TODO: Y-AXIS NOT WORKING / DEGREE NOT CORRECT
					
					float yAxis = leftStick.getAxis(AxisID.Y).getValue();
					float degree = leftStick.getPosition().getDegree();
					float distance = leftStick.getPosition().getDistanceToCenter();
					System.out.println("> Left stick: X=" + xAxis + ",Y=" + yAxis + ",rotation=" + degree + " / distance: " + distance);
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
