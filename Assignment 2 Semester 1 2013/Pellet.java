/*
 * COMMENT
 */

import java.awt.*;

public class Pellet {
    public static final int PELLET_SIZE = A2Constants.PELLET_SIZE;
    public static final Color[] COLOURS = A2Constants.COLOURS;
    public static final Rectangle GAME_SCREEN_AREA = A2Constants.GAME_SCREEN_AREA;

    private Rectangle area;
    private int yMove;
    
    
    
    
    public Pellet(Point centre, int yMove) {
    	area = new Rectangle();
    	area.x = centre.x;
    	area.y = centre.y;
    	area.width = PELLET_SIZE;
    	area.height = PELLET_SIZE;
    	this.yMove = yMove;
    	
    }
    
    public Rectangle getArea() {
    	return area;
    }
    
    public boolean isInsideGameArea() {
    	if(area.intersects(GAME_SCREEN_AREA)){
    		return true;
    	}
    	
    	return false;
    }
 
    public void move() {
    	area.y += yMove;
    }
    
    public void draw(Graphics g) {
    	g.setColor(Color.RED);
    	g.fillRect(area.x, area.y, PELLET_SIZE, PELLET_SIZE);
    	
    }



}