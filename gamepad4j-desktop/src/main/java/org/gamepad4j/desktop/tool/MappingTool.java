/*
 * @Copyright: Marcel Schoen, Switzerland, 2013, All Rights Reserved.
 */

package org.gamepad4j.desktop.tool;


/**
 * Starts test program.
 *
 * @author Marcel Schoen
 * @version $Revision: $
 */
public class MappingTool {
	
	public static void main(String[] args) {
		MappingTool gamepadTest = new MappingTool();
		gamepadTest.runTest();
	}

	private void runTest() {
		new MappingToolWindow().setVisible(true);
		Thread checkThread = new Thread(new GamepadCheck());
		checkThread.setDaemon(true);
		checkThread.start();
	}
}
