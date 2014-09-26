/*
 * @Copyright: Marcel Schoen, Switzerland, 2014, All Rights Reserved.
 */

package org.gamepad4j.test;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * Little panel which draws a green or red bar.
 *
 * @author Marcel Schoen
 * @version $Revision: $
 */
public class ColorBoxPanel extends JPanel {
	
	public int percent = 0;
	public boolean positive = true;

	@Override
	public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int width = getSize().width;
        int drawWidth = (width * this.percent) / 100;

        g.setColor(Color.BLACK);  
        g.fillRect(0, 0, getSize().width, getSize().height); 

        if(this.positive) {
            g.setColor(Color.GREEN);  
        } else {
            g.setColor(Color.RED);  
        }
        g.fillRect(0, 0, drawWidth, getSize().height); 
    }
}
