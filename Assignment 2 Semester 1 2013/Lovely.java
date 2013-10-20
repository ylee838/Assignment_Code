/*
 * COMMENT
 */

import java.awt.*;

public class Lovely {    
    public static final Rectangle[] STRIP_AREAS = A2Constants.STRIP_AREAS;    
    public static final int STRIP_AREAS_LHS = A2Constants.STRIP_AREAS_LHS;  
    public static final int STRIP_AREAS_RHS = A2Constants.STRIP_AREAS_RHS;  
    
    public static final int LOVELY_SIZE = A2Constants.LOVELY_SIZE; 
    public static final Color LOVELY_COLOUR = new Color(184, 200, 255);
    
    public static final Rectangle GAME_SCREEN_AREA = A2Constants.GAME_SCREEN_AREA;
    
    public static final Font VERY_SMALL_FONT = A2Constants.VERY_SMALL_FONT;
    public static final int VERY_SMALL_FONT_SIZE = A2Constants.VERY_SMALL_FONT_SIZE;
    
    private Rectangle area;
    private int moveX;
    private int value;
    private boolean isVisible;
    
    public Lovely(){
    	area = new Rectangle();
    	area.width = LOVELY_SIZE;
    	area.height = LOVELY_SIZE;
    	isVisible = true;
    	value = (int) (Math.random() * 5 + 1);
    	
    	int speed = (int) (Math.random() * 3 + 3);
    	double negative = Math.random();
    	int whichStrip = (int) (Math.random() * 4);

    	moveX = speed;
    	
    	if(whichStrip == 0){
    		area.x = STRIP_AREAS_LHS;
    		area.y = STRIP_AREAS[0].y;
    		
    	}
    	if(whichStrip == 1){
    		area.x = STRIP_AREAS_RHS;
    		area.y = STRIP_AREAS[1].y;
    		
    	}
    	
    	if(whichStrip == 2){
    		area.x = STRIP_AREAS_LHS;
    		area.y = STRIP_AREAS[2].y;
    		
    	}
    	if(whichStrip == 3){
    		area.x = STRIP_AREAS_RHS;
    		area.y = STRIP_AREAS[3].y;
    		
    	}

    	if(value == 1){	
    		if(negative < 0.3){
    			value = -1;
    		}else{
    			value = 1;
    		}
    	}
    	if(value == 2){
    		if(negative < 0.3){
    			value = -2;
    		}else{
    			value = 2;
    		}
    		
    	}
    	if(value == 3){
    		if(negative < 0.3){
    			value = - 3;
    		}else{
    			value = 3;
    		}
    	}
    	if(value == 4){
    		if(negative < 0.3){
    			value = -4;
    		}else{
    			value = 4;
    		}
    	}
    	if(value == 5){
    		if(negative < 0.3){
    			value = -5;
    		}else{
    			value = 5;
    		}
    	}
    }
    
    public boolean getIsVisible(){
      return isVisible;
    }
    
    public void setIsVisible(boolean visible){
      isVisible = visible;
    }
    
    public Rectangle getArea(){
      return area;
    }
    
    public int getValue(){
      return value;
    }
    
    public Point getCentrePt(){
      int midX = area.x + area.width/2;
      int midY = area.y + area.height/2;
      return new Point(midX,midY);      
    }
    
	
	public boolean isInsideStripArea(){
		if(area.y == STRIP_AREAS[0].y || area.y == STRIP_AREAS[2].y){
			return (area.x < STRIP_AREAS_RHS);
		}
		if(area.y == STRIP_AREAS[1].y || area.y == STRIP_AREAS[3].y){
			return (area.x > STRIP_AREAS_LHS);
		}
		else{	
		return false;
		
		}
	}
	public void move(){
		if(isInsideStripArea()){
			if(area.y == STRIP_AREAS[1].y || area.y == STRIP_AREAS[3].y){
				area.x -= moveX;
				if( ! isInsideStripArea()){
					setIsVisible(false);
				}
					
				
			}else{
				area.x = area.x + moveX;
				if(! isInsideStripArea()){
					setIsVisible(false);
				}
			}
			
		}
	}
	public void draw(Graphics g){
		if (isVisible){
			int color = (int) (Math.random() * 7 + 1);
			g.setColor(A2Constants.COLOURS[color]);
			g.fillOval(area.x, area.y, area.width, area.height);
			g.setColor(Color.BLACK);
			g.drawOval(area.x, area.y, area.width, area.height);
			displayValue(g);
		}
	}    
    public void displayValue(Graphics g){
    	g.drawString("" + getValue(), getCentrePt().x - 4, getCentrePt().y + 3); 
    }
    
    
}
    
    
    
    
    
    