/*
 * COMMENT
 */

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;


public class A2JPanel extends JPanel implements KeyListener, ActionListener, MouseListener{
  private static final long serialVersionUID = 1L;
  public static final Rectangle PINKO_MOVE_LINE = A2Constants.PINKO_MOVE_LINE;  
  public static final Color GAME_SCREEN_COLOUR = A2Constants.GAME_SCREEN_COLOUR;  
  
  public static final int MAX_LOVELIES = A2Constants.MAX_LOVELIES;
  public static final Rectangle GAME_SCREEN_AREA = A2Constants.GAME_SCREEN_AREA;
  
  public static final Font TINY_FONT = A2Constants.TINY_FONT;
  public static final Font LARGE_FONT = A2Constants.LARGE_FONT;
  public static final Font HUGE_FONT = A2Constants.HUGE_FONT;
  
  public static final int LARGE_FONT_SIZE = A2Constants.LARGE_FONT_SIZE;
  public static final int HUGE_FONT_SIZE = A2Constants.HUGE_FONT_SIZE;
  
  public static final Point SCORE_POSITION = A2Constants.SCORE_POSITION;
  public static final Point INFORMATION_POSITION1 = A2Constants.INFORMATION_POSITION1;
  public static final Point INFORMATION_POSITION2 = A2Constants.INFORMATION_POSITION2;
  public static final Point INFORMATION_POSITION3 = A2Constants.INFORMATION_POSITION3;
  public static final Point WINNER_LOSER_INFO_POSITION = A2Constants.WINNER_LOSER_INFO_POSITION;  
  
  public static final int NUMBER_OF_PELLETS_ALLOWED = A2Constants.NUMBER_OF_PELLETS_ALLOWED;
  
  public static final int UP = A2Constants.UP;
  public static final int DOWN = A2Constants.DOWN;
  public static final int LEFT = A2Constants.LEFT;
  public static final int RIGHT = A2Constants.RIGHT;
  
  
  Lovely lovelies[];
  Pinko r;
  Timer t;
  int numberOfLovelies;
  int counterForLovelies;
  boolean gameHasEnded;
  boolean startScreen;
  boolean coloursFeature;
  
  public A2JPanel(){
    r = new Pinko();
    t = new Timer(45, this);
	coloursFeature = false;


	lovelies = new Lovely[MAX_LOVELIES];
   
	gameHasEnded = false;
	startScreen = true;
	numberOfLovelies = 0;
	counterForLovelies = 0;
	
    addKeyListener(this);
    addMouseListener(this);
    
    
  }

  

public void actionPerformed(ActionEvent e){
	  if(Math.random() < 0.05 && numberOfLovelies < MAX_LOVELIES){
		 lovelies[numberOfLovelies] = new Lovely();
		  numberOfLovelies++;
	  } 
	  for(int i = 0; i < numberOfLovelies; i++){
		  if(lovelies[i] != null){
			  lovelies[i].move();
		  }  
	  }
	  for(int i = 0; i < numberOfLovelies; i++){
		  r.makeAdjustmentsIfHit(lovelies[i]);
			  
		  }
		  
	  if(numberOfLovelies == MAX_LOVELIES){
	        for(int i =0; i < MAX_LOVELIES; i++){
	          if(!lovelies[i].isInsideStripArea()){
	        	  counterForLovelies++;
	          }
	        }
	      }
		if(counterForLovelies == MAX_LOVELIES || r.getPelletsFired()>=NUMBER_OF_PELLETS_ALLOWED){
	      	  gameHasEnded = true;
	      	  t.stop();
	      	  
	        }
	  r.move();
	  repaint();
	  }


  
	

public void paintComponent(Graphics g){ 
    if(startScreen){
    	g.setColor(Color.GRAY); 
    	g.fillRect(GAME_SCREEN_AREA.x, GAME_SCREEN_AREA.y, GAME_SCREEN_AREA.width, GAME_SCREEN_AREA.height);
    	g.setFont(LARGE_FONT);
    	g.setColor(Color.WHITE);
    	g.drawString("xx All stages complete", 47, 40);
  	    g.setFont(A2Constants.MEDIUM_FONT);
  	    g.drawString("Press Once to get to game screen", 47, 60);
  	    g.drawString("Press Again to get to start game", 47, 80);
  	    g.drawString("r to reset!", 47, 100);
    	g.setFont(HUGE_FONT);
  	    g.drawString("PINKO COLLECTS!", A2Constants.MIDDLE_OF_SCREEN.x - 250 , A2Constants.MIDDLE_OF_SCREEN.y);
  	    specialFeature(g);
  	    g.setColor(Color.WHITE);
  	    g.drawString("EPILEPSY WARNING", A2Constants.MIDDLE_OF_SCREEN.x - 250, A2Constants.MIDDLE_OF_SCREEN.y + 150);
  	    
    }
    else {
    	
    	super.paintComponent(g);
        drawGameScreen(g);
        drawPinkoArea(g);
        drawLovilies(g);
        displayPinkoFunds(g);
        if(counterForLovelies == MAX_LOVELIES || r.getPelletsFired()>=NUMBER_OF_PELLETS_ALLOWED){
        	endMessageDisplay(g);
        }
    }


  }   
  
  private void drawPinkoArea(Graphics g) {  
	if(coloursFeature){
		int color = (int) (Math.random() * 7 + 1);
		g.setColor(A2Constants.COLOURS[color]);
	    g.fillRect(PINKO_MOVE_LINE.x, PINKO_MOVE_LINE.y, PINKO_MOVE_LINE.width, PINKO_MOVE_LINE.height);
	    r.draw(g);
	}
	  
	else{
		g.setColor(Color.BLACK);
	    g.fillRect(PINKO_MOVE_LINE.x, PINKO_MOVE_LINE.y, PINKO_MOVE_LINE.width, PINKO_MOVE_LINE.height);
	    r.draw(g);
	}

    
  }
  
  private void drawGameScreen(Graphics g) {
	  if(coloursFeature){
		int color = (int) (Math.random() * 7 + 1);
		g.setColor(A2Constants.COLOURS[color]);
		g.fillRect(GAME_SCREEN_AREA.x, GAME_SCREEN_AREA.y, GAME_SCREEN_AREA.width, GAME_SCREEN_AREA.height);
	  }
	  else{
		  g.setColor(A2Constants.GAME_SCREEN_COLOUR);
		  g.fillRect(GAME_SCREEN_AREA.x, GAME_SCREEN_AREA.y, GAME_SCREEN_AREA.width, GAME_SCREEN_AREA.height);
	  }
	 
    
  }
  
  private void drawLovilies(Graphics g){
    	for(int i = 0; i < numberOfLovelies; i++){
    		if(lovelies[i] != null)	
    			lovelies[i].draw(g);
    		}
    
    	}

  private void displayPinkoFunds(Graphics g) { 
	  g.setFont(LARGE_FONT);
	  g.drawString("$"+ r.getCurrentFunds(), SCORE_POSITION.x + 35 , SCORE_POSITION.y); 
	  g.drawString("(" + (NUMBER_OF_PELLETS_ALLOWED - r.getPelletsFired()) + ")", SCORE_POSITION.x - 20, SCORE_POSITION.y);
	  g.drawString("[" + (lovelies.length - numberOfLovelies) + "]", SCORE_POSITION.x + 100, SCORE_POSITION.y);
  }
  
  private void endMessageDisplay(Graphics g) {

	  g.setFont(LARGE_FONT);
	  g.setColor(Color.WHITE);
	  g.drawString("GAME OVER", A2Constants.MIDDLE_OF_SCREEN.x - 90, A2Constants.MIDDLE_OF_SCREEN.y);
  }

  
  private void specialFeature(Graphics g){
	 for(int i = 0; i < A2Constants.COLOURS.length; i++ ) {
	  int color = (int) (Math.random() * 7 + 1);
	  g.setColor(A2Constants.COLOURS[color]);
	  g.setFont(LARGE_FONT);
	  g.drawString("Special Feature: COLOURS!", A2Constants.MIDDLE_OF_SCREEN.x - 250,A2Constants.MIDDLE_OF_SCREEN.y +50);
	  g.drawString("Press 'C' and then click your mouse!",  A2Constants.MIDDLE_OF_SCREEN.x - 250,A2Constants.MIDDLE_OF_SCREEN.y + 100);
	  repaint();
	 }
  }
  
  public void keyReleased(KeyEvent e) {}
  public void keyTyped(KeyEvent e) {}
  public void mouseClicked(MouseEvent e){}
  public void mouseEntered(MouseEvent e){}
  public void mouseExited(MouseEvent e){}
  public void mouseReleased(MouseEvent e){}
  
  public void keyPressed(KeyEvent e){
	    if(e.getKeyCode() == KeyEvent.VK_RIGHT){
	    	r.setCurrentDirection(RIGHT);
	    }
	    if(e.getKeyCode() == KeyEvent.VK_LEFT){
	    	r.setCurrentDirection(LEFT);
	    }
	    if(e.getKeyCode() == KeyEvent.VK_UP){
	    	r.firePellet(true);
	    }
	    if(e.getKeyCode() == KeyEvent.VK_DOWN){
	    	r.firePellet(false);
	    }
	    if(e.getKeyChar() == 'c'){
	    	coloursFeature = true;
	    }
	    if(e.getKeyChar() == 'r'){
	        r = new Pinko();
	        t = new Timer(45, this);
	    	coloursFeature = false;
	    	lovelies = new Lovely[MAX_LOVELIES];
	    	gameHasEnded = false;
	    	startScreen = true;
	    	numberOfLovelies = 0;
	    	counterForLovelies = 0;
	    	repaint();
	    	
	    }
	  }
  
  public void mousePressed(MouseEvent e) {
	  if(!startScreen){
	    	if(!gameHasEnded){
	        if(t.isRunning()){
	          t.stop();
	          
	        }
	        else if(!t.isRunning()){
	          t.start();
	          
	        }
	      }
      }else{
    	  startScreen = false;
    	  repaint();
      }
    }
  }
  
 



