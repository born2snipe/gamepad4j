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
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

import com.gamepad4j.ButtonID;
import com.gamepad4j.Controllers;
import com.gamepad4j.IButton;
import com.gamepad4j.IController;
import com.gamepad4j.IControllerListener;
import com.gamepad4j.StickID;

/**
 * Shows the test program window.
 *
 * @author Marcel Schoen
 * @version $Revision: $
 */
public class GamepadTestwindow extends JFrame implements IControllerListener {

	/** Stores ImageIcon instances for various pads. */
	public static Map<Long, ImageIcon> padImageMap = new HashMap<Long, ImageIcon>();

	private static int numberOfPads = 0;
	
	public GamepadTestwindow() {
		setTitle("Gamepad4J Test Program");
	    setSize(400,500);                            // Fenstergröße einstellen  
	    addWindowListener(new TestWindowListener()); // EventListener für das Fenster hinzufügen
	                                                 // (notwendig, damit das Fenster geschlossen werden kann)
	    getContentPane().setLayout(new FlowLayout());
		try {
			InputStream in = GamepadTestwindow.class.getResourceAsStream("/image-filenames.properties");
			Properties filenameProps = new Properties();
			filenameProps.load(in);
			
			// Store controller-specific images
			Enumeration keys = filenameProps.keys();
			while(keys.hasMoreElements()) {
				String key = (String)keys.nextElement();
				String imageFilename = filenameProps.getProperty(key);
				String id = key.substring(key.indexOf("-") + 3, key.indexOf("_"));
				int vendorID = Integer.parseInt(id, 16);
				id = key.substring(key.indexOf("_") + 3);
				int productID = Integer.parseInt(id, 16);
				long deviceTypeIdentifier = (vendorID << 16) + productID;
				System.out.println(">> load image: " + imageFilename);
			    InputStream input = GamepadTestwindow.class.getResourceAsStream(imageFilename);
			    ImageInputStream imageIn = ImageIO.createImageInputStream(input);
			    BufferedImage image = ImageIO.read(imageIn);
			    ImageIcon padImage = new ImageIcon(image);
			    padImageMap.put(deviceTypeIdentifier, padImage);
			}

			// Store default image
		    InputStream input = GamepadTestwindow.class.getResourceAsStream("/controller_xbox_360.png");
		    ImageInputStream imageIn = ImageIO.createImageInputStream(input);
		    BufferedImage image = ImageIO.read(imageIn);
		    ImageIcon padImage = new ImageIcon(image);
		    padImageMap.put(0L, padImage);
		    
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		// Initial gamepad detection
		Controllers.initialize();
		Controllers.instance().addListener(this);
		
		// Build window content
//		updateWindow();
	}
	
	private void updateWindow() {
		getContentPane().removeAll();
//		Controllers.checkControllers();
		IController[] controllers = Controllers.getControllers();
		numberOfPads = controllers.length;
		for(int i = 0; i < numberOfPads; i++) {
			getContentPane().add(new ControllerPanel(controllers[i]));
		}
		pack();
	}
	
	
	/* (non-Javadoc)
	 * @see com.gamepad4j.IControllerListener#connected(com.gamepad4j.IController)
	 */
	@Override
	public void connected(IController controller) {
		updateWindow();
	}

	/* (non-Javadoc)
	 * @see com.gamepad4j.IControllerListener#disConnected(com.gamepad4j.IController)
	 */
	@Override
	public void disConnected(IController controller) {
		updateWindow();
	}

	/* (non-Javadoc)
	 * @see com.gamepad4j.IControllerListener#buttonDown(com.gamepad4j.IController, com.gamepad4j.IButton, com.gamepad4j.ButtonID)
	 */
	@Override
	public void buttonDown(IController controller, IButton button,
			ButtonID buttonID) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.gamepad4j.IControllerListener#buttonUp(com.gamepad4j.IController, com.gamepad4j.IButton, com.gamepad4j.ButtonID)
	 */
	@Override
	public void buttonUp(IController controller, IButton button,
			ButtonID buttonID) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.gamepad4j.IControllerListener#moveStick(com.gamepad4j.IController, com.gamepad4j.StickID)
	 */
	@Override
	public void moveStick(IController controller, StickID stick) {
		// TODO Auto-generated method stub
		
	}


	class TestWindowListener extends WindowAdapter {
	    public void windowClosing(WindowEvent e) {
			Controllers.shutdown();
			e.getWindow().dispose();                   // Fenster "killen"
	    }           
	}
}
