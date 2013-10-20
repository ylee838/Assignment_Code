/*
 * COMMENT
 */

import java.awt.*;

public class Pinko {
  public static final Rectangle PINKO_START_AREA = A2Constants.PINKO_START_AREA;
  
  public static final Rectangle PINKO_MOVE_LINE = A2Constants.PINKO_MOVE_LINE;  
  public static final int PINKO_MOVE_AREA_LHS = A2Constants.PINKO_MOVE_AREA_LHS;
  public static final int PINKO_MOVE_AREA_RHS = A2Constants.PINKO_MOVE_AREA_RHS; 
  public static final Point PINK0_CENTRE_POINT = A2Constants.MIDDLE_OF_SCREEN;
  public static final int PINKO_MOVE_AMT = A2Constants.PINKO_MOVE_AMT; 
  
  public static final int PINKO_SPOT_SIZE = A2Constants.PINKO_SPOT_SIZE; 
  
  public static final int UP = A2Constants.UP;
  public static final int DOWN = A2Constants.DOWN;
  public static final int LEFT = A2Constants.LEFT;
  public static final int RIGHT = A2Constants.RIGHT;
  
  public static final int MAX_PELLETS = A2Constants.MAX_PELLETS;  
  public static final int NUMBER_OF_PELLETS_ALLOWED = A2Constants.NUMBER_OF_PELLETS_ALLOWED;
  
  private Pellet[] pellets;
  
  private Rectangle area;
  private int speed;
  private int currentDirection;
  private int currentFunds;
  private int pelletsFired;
  private int numberOfPellets;
  
  public Pinko() { 
    pellets = new Pellet[MAX_PELLETS];
	area = PINKO_START_AREA;
    speed = PINKO_MOVE_AMT;
    currentDirection = RIGHT;
    currentFunds = 10;
    pelletsFired = 0;
    numberOfPellets = 0;
  }
  public int getPelletsFired(){
	  return pelletsFired;
  }
  
  public Rectangle getArea() {
    return area;
  }
  
  public int getCurrentFunds() { 
    return currentFunds;
  }
  
  public void setCurrentDirection(int direction) { 
    currentDirection = direction; 
  }
  
  public Point getCentrePt() {
    int midOfX = (area.x) + (area.width/2);
    int midOfY = (area.y) + (area.height/2);
    
    return new Point(midOfX, midOfY);
    
  }
  
  public void move() {
    if(currentDirection == RIGHT && (area.x + speed) > PINKO_MOVE_AREA_RHS){
      currentDirection = LEFT;
    }
    if(currentDirection == LEFT && (area.x - speed) < PINKO_MOVE_AREA_LHS){
      currentDirection = RIGHT;
    }
    if(currentDirection == RIGHT){
      area.x += speed;
    }
    if(currentDirection == LEFT){
      area.x -= speed;
    }
    for(int i=0; i<numberOfPellets;i++){
        pellets[i].move();
    }
  }
  
  public void firePellet(boolean fireUpwards){
	 if(pelletsFired >= MAX_PELLETS){
	 } else if(fireUpwards){
        Point up = new Point(area.x+area.width/2,area.y);
        pellets[numberOfPellets] = new Pellet(up,25*-1);
        numberOfPellets++;
        pelletsFired++;
        currentFunds--;
      }else {
        Point down = new Point(area.x+area.width/2,area.y+area.height);
        pellets[numberOfPellets] = new Pellet(down,25);
        numberOfPellets++;
        pelletsFired++;
        currentFunds--;
      } 
    
  }
   
  public boolean makeAdjustmentsIfHit(Lovely gorgeous){
      for(int i =0; i < numberOfPellets; i++){
        if(pellets[i].getArea().intersects(gorgeous.getArea())){
          gorgeous.setIsVisible(false);
          currentFunds = currentFunds + gorgeous.getValue();
          removePellet(i);
          numberOfPellets--;
          return true;          
        }        
    }
      return false;

    }
  
  private void removePellet(int index) {
	Pellet[] tempPellet = new Pellet[MAX_PELLETS];
	for(int i = 0; i < index; i++){
		tempPellet[i] = pellets[i];
	}
	for(int i = index + 1; i < numberOfPellets; i++){
		tempPellet[i-1] = pellets[i];
	}
	pellets = tempPellet;
}
public void draw(Graphics g) {
    g.setColor(Color.RED);
	g.fillRect(area.x, area.y, area.width, area.height);
	g.setColor(Color.BLACK);
	g.drawRect(area.x, area.y, area.width, area.height);
    getTopSpot(g);
    getBottomSpot(g);
    for(int i=0; i<numberOfPellets;i++){
        pellets[i].draw(g);
    }
  }  
  
  
  private void getTopSpot(Graphics g) {
	  int color = (int) (Math.random() * 7 + 1);
	  g.setColor(A2Constants.COLOURS[color]);
	  g.fillRect((getCentrePt().x - 2), (area.y - PINKO_SPOT_SIZE), PINKO_SPOT_SIZE, PINKO_SPOT_SIZE);   
	  g.setColor(Color.BLACK);
	  g.drawRect((getCentrePt().x - 2), (area.y - PINKO_SPOT_SIZE), PINKO_SPOT_SIZE, PINKO_SPOT_SIZE);
  }
  private void getBottomSpot(Graphics g) {  
	  int color = (int) (Math.random() * 7 + 1);
	  g.setColor(A2Constants.COLOURS[color]);
	  g.fillRect((getCentrePt().x - 2), (area.y + area.height), PINKO_SPOT_SIZE, PINKO_SPOT_SIZE);
	  g.setColor(Color.BLACK);
	  g.drawRect((getCentrePt().x - 2), (area.y + area.height), PINKO_SPOT_SIZE, PINKO_SPOT_SIZE);
    
  }
  
}


