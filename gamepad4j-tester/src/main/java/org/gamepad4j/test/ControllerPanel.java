/*
 * @Copyright: Marcel Schoen, Switzerland, 2014, All Rights Reserved.
 */

package org.gamepad4j.test;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.gamepad4j.IController;

/**
 * Panel which shows the status of one gamepad.
 *
 * @author Marcel Schoen
 * @version $Revision: $
 */
public class ControllerPanel extends JPanel {

	public ControllerPanel(IController controller) {
	    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	    setBorder(BorderFactory.createLoweredBevelBorder());
//	    setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
	    
	    ImageIcon padImage = GamepadTestwindow.padImageMap.get(controller.getDeviceTypeIdentifier());
	    if(padImage == null) {
		    padImage = GamepadTestwindow.padImageMap.get(0L);
	    }
	    JLabel label = new JLabel(padImage, JLabel.CENTER);
	    add(label);
	}

}
