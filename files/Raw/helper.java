import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.JOptionPane;
import java.awt.Robot;
import java.awt.AWTException;
import java.awt.event.InputEvent;
import java.awt.MouseInfo;
import java.awt.Color;
import java.awt.datatransfer.*;
import java.awt.Toolkit;

public class helper extends JFrame {
	private JLabel lblCoords = new JLabel("Mouse Coordiantes: ", SwingConstants.CENTER); //SwingConstants.CENTER will center the label in column
	private JLabel lblColorRGB = new JLabel("Pixel Color (R,G,B): ", SwingConstants.CENTER);
	private JLabel lblColorINT = new JLabel("Pixel Color (INT): ", SwingConstants.CENTER);
	private JLabel lblEmpty = new JLabel("");
	
	private JTextField txtCoords = new JTextField();
	private JTextField txtColorRGB = new JTextField();
	private JTextField txtColorINT = new JTextField();
	
	private JButton btnHelp = new JButton("Help");
	private JButton btnExit = new JButton("Exit");
	private JButton btnCoordsCopy = new JButton("Copy to Clipboard");
	private JButton btnColorRGBCopy = new JButton("Copy to Clipboard");
	private JButton btnColorINTCopy = new JButton("Copy to Clipboard");
	
	//button handlers
	private btnHelpHandler bhHandler;
	private btnExitHandler beHandler;
	private btnCoordsCopyHandler bccHandler;
	private btnColorRGBCopyHandler bcrHandler;
	private btnColorINTCopyHandler bciHandler;
	
	//key action handler
	private keyPressedHandler kpHandler;
	
	
	
	private static boolean PAUSED = false;
	public void msgBox( String Text, String Title ) {
		JOptionPane.showMessageDialog(null,Text,Title,JOptionPane.PLAIN_MESSAGE); //Show message box
	}
	public void pause() {
		if (PAUSED == true) {
			//get the coordinates in the text field
			String rawCoords = txtCoords.getText();
			rawCoords = rawCoords.replace("(","");
			rawCoords = rawCoords.replace(")","");
			String[] Coords = rawCoords.split("[,]+");
			int[] cursorPos = {Integer.parseInt(Coords[0]), Integer.parseInt(Coords[1])};
			
			//move mouse to position
			try {
				Robot r = new Robot();
				r.mouseMove(cursorPos[0], cursorPos[1]);
			} catch(AWTException e) {
				throw new RuntimeException(e);
			}
		}
		PAUSED = !PAUSED;
	}
	
	private class btnHelpHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			msgBox("Press F8 to pause the helper\nVisit \"Step 4\" of the Getting Started section on http://javAuto.org/ for additional help.", "Help");
		}
	}
	
	private class btnExitHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}
	
	private class btnCoordsCopyHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String rawCoords = txtCoords.getText();
			rawCoords = rawCoords.replace("(","");
			rawCoords = rawCoords.replace(")","");
			String[] Coords = rawCoords.split("[,]+");
			String myString = Coords[0] + "," + Coords[1];
			StringSelection stringSelection = new StringSelection (myString);
			Clipboard clpbrd = Toolkit.getDefaultToolkit ().getSystemClipboard ();
			clpbrd.setContents (stringSelection, null);
		}
	}
	
	private class btnColorRGBCopyHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String myString = txtColorRGB.getText();
			StringSelection stringSelection = new StringSelection (myString);
			Clipboard clpbrd = Toolkit.getDefaultToolkit ().getSystemClipboard ();
			clpbrd.setContents (stringSelection, null);
		}
	}
	
	private class btnColorINTCopyHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String myString = txtColorINT.getText();
			StringSelection stringSelection = new StringSelection (myString);
			Clipboard clpbrd = Toolkit.getDefaultToolkit ().getSystemClipboard ();
			clpbrd.setContents (stringSelection, null);
		}
	}
	
	private class keyPressedHandler implements KeyListener {	
		public void keyTyped(KeyEvent e) {
			//do nothing
		}
		// /** Handle the key pressed event from the text field. */
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
				case 119: ; pause(); break; //pause if F8 is typed
				default: break;
			}
		}
		public void keyReleased(KeyEvent e) {
			//do nothing
		}
	}
	
	public void updateVals() {
			//function to update the Text Field vals
			
			//get mouse coordinates
			int X = MouseInfo.getPointerInfo().getLocation().x; //get the X coordinate of the mouse
			int Y = MouseInfo.getPointerInfo().getLocation().y; //get the Y coordinate of the mouse
			String coords = "(" + X + "," + Y + ")";
			
			//get the Color of a pixel at X, Y
			try {
				Robot r = new Robot();
				Color pixel = r.getPixelColor( X, Y );
				
				//get the RGB value
				String RGB = pixel.getRed() + "," + pixel.getGreen() + "," + pixel.getBlue();
				
				//get the int value
				int RGBint = pixel.getRGB();
				String RGB_str = String.valueOf(RGBint);
				
				//update the values
				txtCoords.setText(coords);
				txtColorRGB.setText(RGB);
				txtColorINT.setText(RGB_str);
				
			} catch(AWTException e) {
				throw new RuntimeException(e);
			}
		}
	
	public helper() {
		//specify handlers for help button
		bhHandler = new btnHelpHandler();
		btnHelp.addActionListener(bhHandler);
		
		//specify handler for exit button
		beHandler = new btnExitHandler();
		btnExit.addActionListener(beHandler);
		
		//copy buttons
		bccHandler = new btnCoordsCopyHandler();
		btnCoordsCopy.addActionListener(bccHandler);
		bcrHandler = new btnColorRGBCopyHandler();
		btnColorRGBCopy.addActionListener(bcrHandler);
		bciHandler = new btnColorINTCopyHandler();
		btnColorINTCopy.addActionListener(bciHandler);
		
		//specify handler for key listener
		kpHandler = new keyPressedHandler();
		txtCoords.addKeyListener(kpHandler);
		
		Container pane = getContentPane(); //get HWND for GUI frame
		pane.setLayout(new GridLayout(4, 3)); //set the layout type to a 3 x 4 grid
		
		//center the text fields
		txtCoords.setHorizontalAlignment(JLabel.CENTER);
		txtColorRGB.setHorizontalAlignment(JLabel.CENTER);
		txtColorINT.setHorizontalAlignment(JLabel.CENTER);
		
		//make the text fields uneditable
		txtColorRGB.setEditable(false);
		txtColorINT.setEditable(false);
		
		//add things to the pane in order of display (left to right top to bottom)
		pane.add(lblCoords);
		pane.add(txtCoords);
		pane.add(btnCoordsCopy);
		
		pane.add(lblColorRGB);
		pane.add(txtColorRGB);
		pane.add(btnColorRGBCopy);
		
		pane.add(lblColorINT);
		pane.add(txtColorINT);
		pane.add(btnColorINTCopy);
				
		pane.add(btnHelp);
		pane.add(lblEmpty); //margin between help and exit
		pane.add(btnExit);
		
		
		//finish up
		setTitle("javAuto Helper"); //set title
		setSize(493,175); //set size
		setVisible(true); //make visible
		setDefaultCloseOperation(EXIT_ON_CLOSE); //define what the X does
		setAlwaysOnTop(true); //always on top
	}
	
	public static void main(String[] args) {
		try {
			Robot roboto = new Robot(); //create robot to facilitate delays & other actions 
			helper javautoHelper = new helper(); //create GUI
			while (1==1) {
				roboto.delay(20);
				if (PAUSED == false) {
					javautoHelper.updateVals(); //update values
				}
			}			
		} catch(AWTException e) {
			throw new RuntimeException(e);
		}
	}
}