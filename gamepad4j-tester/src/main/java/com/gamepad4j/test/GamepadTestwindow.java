/*
 * @Copyright: Marcel Schoen, Switzerland, 2014, All Rights Reserved.
 */

package com.gamepad4j.test;

import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

import com.gamepad4j.Controllers;
import com.gamepad4j.IController;

/**
 * Shows the test program window.
 *
 * @author Marcel Schoen
 * @version $Revision: $
 */
public class GamepadTestwindow extends JFrame {

	public static ImageIcon padImage = null;

	private static int numberOfPads = 0;
	
	public GamepadTestwindow() {
		setTitle("Gamepad4J Test Program");
	    setSize(400,500);                            // Fenstergröße einstellen  
	    addWindowListener(new TestWindowListener()); // EventListener für das Fenster hinzufügen
	                                                 // (notwendig, damit das Fenster geschlossen werden kann)
	    
	    getContentPane().setLayout(new FlowLayout());
	    InputStream input = GamepadTestwindow.class.getResourceAsStream("/controller_xbox_360.png");
	    ImageInputStream in;
		try {
			in = ImageIO.createImageInputStream(input);
		    BufferedImage image = ImageIO.read(in);
		    padImage = new ImageIcon(image); 
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Initial gamepad detection
		Controllers.initialize();
//		Controllers.checkControllers();
		
		// Build window content
		updateWindow();
	}
	
	private void updateWindow() {
		getContentPane().removeAll();
		IController[] controllers = Controllers.getControllers();
		numberOfPads = controllers.length;
		for(int i = 0; i < numberOfPads; i++) {
			getContentPane().add(new ControllerPanel(controllers[i]));
		}
		pack();
	}
	
	class TestWindowListener extends WindowAdapter {
	    public void windowClosing(WindowEvent e) {
			Controllers.shutdown();
			e.getWindow().dispose();                   // Fenster "killen"
	    }           
	}
}
