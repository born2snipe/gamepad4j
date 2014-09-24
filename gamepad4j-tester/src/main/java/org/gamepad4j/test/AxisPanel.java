/*
 * @Copyright: Marcel Schoen, Switzerland, 2014, All Rights Reserved.
 */

package org.gamepad4j.test;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import org.gamepad4j.IAxis;

/**
 * ...
 *
 * @author Marcel Schoen
 * @version $Revision: $
 */
public class AxisPanel extends JPanel {

	private IAxis axis = null;
	private JPanel colorPanel = new JPanel();
	
	public AxisPanel(IAxis axis) {
		this.axis = axis;
	    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	    setBorder(BorderFactory.createLoweredBevelBorder());
	    setSize(new Dimension(50,30));
	    
	    JPanel noPanel = new JPanel();
	    noPanel.setSize(new Dimension(50, 18));
	    add(noPanel);
	    
	    colorPanel = new JPanel();
	    noPanel.setSize(new Dimension(50, 12));
	    add(colorPanel);
	}
	
	public void update() {
		
	}
}
