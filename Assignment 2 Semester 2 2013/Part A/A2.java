/*
 * Purpose: application for the A2 game
 * 
 * Author: xx
 * Date: S2 2013
*/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class A2 {
	private static final int JFRAME_AREA_WIDTH = A2Constants.JFRAME_AREA_WIDTH;
	private static final int JFRAME_AREA_HEIGHT = A2Constants.JFRAME_AREA_HEIGHT;;

	public static void main(String[] args) {
//---------------------
// Stage 2 (4 Marks)
//---------------------
		JFrame gui = new A2JFrame("A2 Part A by xx", 3, 3, JFRAME_AREA_WIDTH, JFRAME_AREA_HEIGHT);
	}
}
class A2JFrame extends JFrame {
	public A2JFrame(String title, int x, int y, int width, int height) {
		// Set the title, top left location, and close operation for the frame
		setTitle(title);
		setLocation(x, y);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Create an instance of the JPanel class, and set this to define the
		// content of the window
		JPanel frameContent = new A2JPanel();
		Container visibleArea = getContentPane();
		visibleArea.add(frameContent);

		// Set the size of the content pane of the window, resize and validate the
		// window to suit, obtain keyboard focus, and then make the window visible
		frameContent.setPreferredSize(new Dimension(width, height));
		pack();
		frameContent.requestFocusInWindow();
		setVisible(true);
	}
}
