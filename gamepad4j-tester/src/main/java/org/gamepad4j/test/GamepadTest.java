/*
 * @Copyright: Marcel Schoen, Switzerland, 2013, All Rights Reserved.
 */

package org.gamepad4j.test;


/**
 * Starts test program.
 *
 * @author Marcel Schoen
 * @version $Revision: $
 */
public class GamepadTest {
	
	public static void main(String[] args) {
		GamepadTest gamepadTest = new GamepadTest();
		gamepadTest.runTest();
	}

	private void runTest() {
		new GamepadTestwindow().setVisible(true);
		Thread checkThread = new Thread(new GamepadCheck());
		checkThread.setDaemon(true);
		checkThread.start();
	}
}
