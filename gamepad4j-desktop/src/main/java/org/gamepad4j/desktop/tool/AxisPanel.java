/*
 * @Copyright: Marcel Schoen, Switzerland, 2014, All Rights Reserved.
 */

package org.gamepad4j.desktop.tool;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.gamepad4j.IAxis;

/**
 * ...
 *
 * @author Marcel Schoen
 * @version $Revision: $
 */
public class AxisPanel extends JPanel {

	private ColorBoxPanel colorPanel = null;
	
	public AxisPanel(IAxis axis) {
	    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	    setBorder(BorderFactory.createLoweredBevelBorder());
	    
	    JPanel noPanel = new JPanel();
	    noPanel.setPreferredSize(new Dimension(50, 20));
	    JLabel number = new JLabel(String.valueOf(axis.getNumber()));
	    noPanel.add(number);
	    add(noPanel);
	    
	    colorPanel = new ColorBoxPanel(axis);
	    add(colorPanel);
	}
}
