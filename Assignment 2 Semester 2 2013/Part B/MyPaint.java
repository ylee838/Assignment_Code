import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.*;
import java.awt.image.*;

import javax.swing.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.awt.Point;
import java.lang.Math;

/**
 * Simple Paint Program
 * 
 * (c) Burkhard Wuensche 2013
 * version: 02/09/2013
 */
public class MyPaint extends JPanel {
  
  static String imageFileName = "Teddy.png";
  private BufferedImage bi;
  int w, h;
  static StackReferenceBased imageStack;
  
  // static boolean mousePressed;
  static int radius; 
  static int mode;      // current mode 
  static int colour;
  static int threshold;
  static String modes[] = {
    "Mode 0: Colour picker",
    "Mode 1: Draw",
    "Mode 2: Flood fill",
    "Mode 3: Undo"
  };
  static String menuItems[] = {
    "(0) Print Menu",
    "(1) Colour picker - click on window to output pixel colour",
    "(2) Change drawing colour",
    "(3) Change radius (for drawing)",
    "(4) Change threshold (for flood fill algorithm)",
    "(5) Draw (click at pixel or drag mouse with left button down)",
    "(6) Flood filling - click inside the area you want to fill",
    "(7) Undo (reverse previous draw or flodd filling operation)"
  };
  
  
  boolean pixelValid(int x, int y){
    return (x>=0 && x<w && y>=0 && y < h);
  }
  
  static int getRed(int rgbColour){ return (rgbColour>>16)&255;}
  static int getGreen(int rgbColour){ return (rgbColour>>8)&255;}
  static int getBlue(int rgbColour){ return (rgbColour)&255;}
  static int getRGBColour(int red, int green, int blue){ return ((255<<24)|(red<<16)|(green<<8)|blue);}
  
  // draw circle into the buffered image
  void drawCircle(int x, int y){
    int i,j;
    for(i=x-radius; i<=x+radius; i++){
      for(j=y-radius; j<=y+radius; j++){
        if(pixelValid(i,j) && ((i-x)*(i-x)+(j-y)*(j-y)<radius*radius)){
          bi.setRGB(i,j,colour);
        }
      }
    }
  }
  
  // returns copy of the current buffered image
  BufferedImage deepCloneBufferedImage(){
    // perform a "deepClone" for BufferedImage object
    ColorModel cm = bi.getColorModel();
    boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
    WritableRaster raster = bi.copyData(null);
    return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
  }
  
  
  // Fills the region of buffered image containing the clicked point (x,y)
  // The region is filled using the current drawing colour and pixels are collected subject to the current threshold
  // ALGORITHM:
  // Define an empty queue
  // Define seedColour=colour of pixel (x,y)
  // Add pixel to queue
  // While queue not empty
  //    Remove pixel from queue
  //    if (pixel not yet visited){
  //        Mark pixel as visited
  //        if (colour difference between current pixel and seed pixel is smaller or equal to the threshold){
  //           Add all neighbours to the queue (i.e. (x-1,y), (x+1,y), (x,y-1), (x,y+1)), 
  //              which have not been visited yet and which are valid pixels (i.e. inside image)
  //           Change pixel colour to current drawing colour
  //        }
  //    }   
  //
  // NOTE 1: For two colours (r1,g1,b1) and (r2,g2,b2) the colour difference is D=(max(abs(r1-r2), abs(g1-g2), abs(b1-b2))
  // NOTE 2: The filled region MUST be connected, i.e. you can iterate through all pixels of the image and change all within the current threshold
  void floodFill(int x, int y){
    ///////////////////////////////////////
    //  INSERT MISSING CODE FOR TASK 1  ///
    ///////////////////////////////////////
    QueueReferenceBased pixelQueue = new QueueReferenceBased();
    int seedColour = bi.getRGB(x, y);
    boolean[][] visited = new boolean[w][h];
    pixelQueue.enqueue(new Point(x,y));
    
    try{ 
    while(! pixelQueue.isEmpty()){
      Point pixel = (Point)pixelQueue.dequeue();
      
      if(visited[x][y] == false){
    	  visited[x][y] = true;
      }

      x = pixel.x;
      y = pixel.y;
      
      int[] rgbS = {getRed(seedColour), getGreen(seedColour), getBlue(seedColour)};
      int[] rgbC ={getRed(bi.getRGB(x, y)), getGreen(bi.getRGB(x, y)), getBlue(bi.getRGB(x, y))};

      if(Math.max( Math.abs(rgbC[0]-rgbS[0]), Math.max(Math.abs(rgbC[1]-rgbS[1]), Math.abs(rgbC[2]-rgbS[2]))) <= threshold) {
        if(visited[x-1][y] == false && pixelValid(x-1,y)) {
        	pixelQueue.enqueue(new Point(x-1,y));
        	visited[x-1][y] = true;
        } 
        if (visited[x+1][y] == false && pixelValid(x+1,y)) {
        	pixelQueue.enqueue(new Point(x+1,y));
        	visited[x+1][y] = true;
        } 
        if (visited[x][y-1] == false && pixelValid(x,y-1)) {
        	pixelQueue.enqueue(new Point(x,y-1));
        	visited[x][y-1] = true;
        } 
        if (visited[x][y+1] == false && pixelValid(x,y+1)) {
        	pixelQueue.enqueue(new Point(x,y+1));
        	visited[x][y+1] = true;
        }
        
        bi.setRGB(x, y, colour);
      }
      
    }
    }catch(ArrayIndexOutOfBoundsException e){
	   
   }
  }
  
  
  void processMouseClick(int x, int y){
    switch (mode) {
      case 0 : /* Mode 0 - Colour picker */
        // output colour of selected pixel
        int rgbColour=bi.getRGB(x,y);
        System.out.print("\nThe pixel colour is: (" + getRed(rgbColour) + ", " + getGreen(rgbColour) + ", " + getBlue(rgbColour) + ")"); 
        break;
      case 1 : /* Mode 1 - Draw */
        // draw circle at selected pixel
        drawCircle(x, y);
        repaint();
        break;
      case 2 : /* Mode 2 - Flood fill */
        // fill region starting at selected pixel
        floodFill(x, y);
        repaint();
        break;      
      default :
    }
  }
  
  void processMousePressed(){
///////////////////////////////////////
//  INSERT MISSING CODE FOR TASK 2  ///
///////////////////////////////////////
    if(mode == 1 || mode == 2){
    	imageStack.push(deepCloneBufferedImage());
    	//System.out.println(imageStack.peek());
    	//System.out.println("Pushing Image to stack");
    }
    if(imageStack.isEmpty()){
    	//System.out.println("Empty stack");
    	return;
    }
    if(mode == 3){
    	BufferedImage biN = (BufferedImage)imageStack.pop(); //Cast the popped object
    	bi = biN; //change current for previous
    	//System.out.println("Popping Stack");
    	
    }
  
  }
  
  void processMouseMove(int x, int y){
    switch (mode) {
      case 1 : /* Mode 1 - Draw */
        // draw circle at selected pixel
        drawCircle(x, y);
        repaint();
        break;
      default :
    }
  }
  
  /**  Perform operation for selected menu item  **/ 
  void processMenuSelection(char key){
    Scanner input;
    switch (key) {
      case '0' : /* (0) Print Menu */
        break;
      case '1' : /* (1) Colour picker */
        mode = 0;
        break;
      case '2' : /* (2) Change drawing colour */
        System.out.print("\n\nInput an RGB colour (e.g. \"255 0 0\"), and press return: ");
        input = new Scanner( System.in );
        int red=input.nextInt(); if (red<0) red=0; if (red>255) red=255;
        int green=input.nextInt(); if (green<0) green=0; if (green>255) green=255;
        int blue=input.nextInt(); if (blue<0) blue=0; if (blue>255) blue=255;
        colour=getRGBColour(red,green,blue);
        break;
      case '3' : /* (3) Change radius */
        System.out.print("\n\nInput an integer value between 1 and 50: ");
        input = new Scanner( System.in );
        int rad=input.nextInt(); if (rad<1) rad=1; if (rad>50) rad=55;
        radius = rad;
        break;
      case '4' : /* (4) Change threshold */
        System.out.print("\n\nInput an integer value between 0 and 255: ");
        input = new Scanner( System.in );
        int t=input.nextInt(); if (t<0) t=0; if (t>255) t=255;
        threshold = t;
        break;
      case '5' : /* (5) Draw */
        mode = 1;
        break;
      case '6' : /* (6) Flood fill */
    	 mode = 2;
        break;
///////////////////////////////////////
//  INSERT MISSING CODE FOR TASK 2  ///
///////////////////////////////////////
      case '7' : /* (7) Undo */
    	 mode = 3;
    	 processMousePressed();
    	 repaint();
    	 break;
      default :
    }
    printMenu();             
  }
  
  
  /**  Constructor  **/ 
  public MyPaint() {
    // load image from file imageFileName
    try {
      File file = new File(imageFileName);
      bi = ImageIO.read(file);
      w = bi.getWidth(null);
      h = bi.getHeight(null);
      if (bi.getType() != BufferedImage.TYPE_INT_RGB) {
        BufferedImage bi2 =
          new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics big = bi2.getGraphics();
        big.drawImage(bi, 0, 0, null);
        bi = bi2;
      }
    } catch (IOException e) {
      System.out.println("Image could not be read");
      System.exit(1);
    }
    // initialise graphics window
    setBackground(Color.WHITE);
    setPreferredSize(new java.awt.Dimension(w, h));
    // initialise event handling 
    // (mouse input for drawing and keyboard input for menu selection)
    addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        int x=e.getX();
        int y=e.getY();
        if (pixelValid(x, y)){ 
          processMouseClick(x, y);
        }
      }
      public void mousePressed(MouseEvent e) {
        processMousePressed();
      }
    });
    addMouseMotionListener(new MouseMotionAdapter() {
      public void mouseDragged(MouseEvent e) {
        int x=e.getX();
        int y=e.getY();
        if (pixelValid(x, y)){ 
          processMouseMove(x, y);
        }
      }
    });
    this.setFocusable(true);   // Allow this panel to get focus - necessary for detecting keyboard events
    addKeyListener(new KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        processMenuSelection(e.getKeyChar());
      }
    });
    
  }
  
  /**  Draw BufferedImage  **/
  public void paint(Graphics g) {
    g.drawImage(bi, 0, 0, null);
  }
  
  /**  Output menu onto console  **/
  public static void printMenu(){
    System.out.println("\n");
    for(int i=0; i< menuItems.length; i++){
      System.out.println(menuItems[i]);
    }
    System.out.println("\n" + modes[mode]);    
    System.out.println("The current colour is: (" + getRed(colour) + ", " + getGreen(colour) + ", " + getBlue(colour) +")");    
    System.out.println("The current radius is: " + radius);    
    System.out.println("The current threshold is: " + threshold);    
    System.out.println("\n");    
  }
  
  public static void main(String[] args) {
    JFrame frame = new JFrame("CS105 Ass 2 - Submitted by xx");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    MyPaint imagePanel = new MyPaint();
    // initialise state variables of paint program
    mode=0;
    colour=getRGBColour(255, 0, 0);
    threshold=20;
    radius = 5;
    // initialise image stack
    imageStack = new StackReferenceBased();
    // print menu
    printMenu();
    frame.add(imagePanel);
    frame.pack();
    frame.setVisible(true);
  }
  
}
