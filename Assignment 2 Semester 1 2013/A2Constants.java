/*
 * Purpose: defines many constants 
 * which are useful in the game.
 * 
 * Author: xx
 * Date: S1 2013
 */

import java.awt.*;

public class A2Constants { 
//-------------------------------------------------------
// width, height of the game area
//------------------------------------------------------- 
    public static final int JFRAME_AREA_WIDTH = 600;
    public static final int JFRAME_AREA_HEIGHT = 360;
    public static final Rectangle GAME_SCREEN_AREA = new Rectangle(0, 0, JFRAME_AREA_WIDTH, JFRAME_AREA_HEIGHT);
    public static final Point MIDDLE_OF_SCREEN = new Point(JFRAME_AREA_WIDTH / 2, JFRAME_AREA_HEIGHT / 2);
    
//-------------------------------------------------------
// Constants to do with the strip along which the 
// user controlled object moves
//-------------------------------------------------------
    public static final int PINKO_SIZE = 20; 
    public static final int PINKO_MOVE_AMT = PINKO_SIZE / 4; 
    
    public static final Rectangle PINKO_MOVE_LINE = new Rectangle(MIDDLE_OF_SCREEN.x - 250, MIDDLE_OF_SCREEN.y - 1, 500, 3);
    public static final int PINKO_SPOT_SIZE = 5; 
    
    public static final int PINKO_MOVE_AREA_LHS = PINKO_MOVE_LINE.x;
    public static final int PINKO_MOVE_AREA_RHS = PINKO_MOVE_LINE.x + PINKO_MOVE_LINE.width;
    
    public static final Rectangle PINKO_START_AREA = new Rectangle(PINKO_MOVE_LINE.x + PINKO_MOVE_LINE. width / 2 - PINKO_SIZE / 2, MIDDLE_OF_SCREEN.y - PINKO_SIZE / 2, PINKO_SIZE, PINKO_SIZE);
    
//-------------------------------------------------------
// Constants to do with the paths followed by the 
// 4 rows of Lovely objects
//-------------------------------------------------------
    public static final int LOVELY_SIZE = PINKO_SIZE; 
    
    public static final Rectangle[] STRIP_AREAS = { new Rectangle(-LOVELY_SIZE, MIDDLE_OF_SCREEN.y - LOVELY_SIZE * 7, GAME_SCREEN_AREA.width + LOVELY_SIZE * 2, LOVELY_SIZE),
        new Rectangle(-LOVELY_SIZE, MIDDLE_OF_SCREEN.y - LOVELY_SIZE * 6, GAME_SCREEN_AREA.width + LOVELY_SIZE * 2, LOVELY_SIZE),
        new Rectangle(-LOVELY_SIZE, MIDDLE_OF_SCREEN.y + LOVELY_SIZE * 4, GAME_SCREEN_AREA.width + LOVELY_SIZE * 2, LOVELY_SIZE),
        new Rectangle(-LOVELY_SIZE, MIDDLE_OF_SCREEN.y + LOVELY_SIZE * 5, GAME_SCREEN_AREA.width + LOVELY_SIZE * 2, LOVELY_SIZE)
    };
    
    public static final int STRIP_AREAS_LHS = STRIP_AREAS[0].x;
    public static final int STRIP_AREAS_RHS = STRIP_AREAS[0].x + STRIP_AREAS[0].width;
    
//-------------------------------------------------------
// Pellet size, maximum number of Pellet/Lovely objects 
// in the array/s 
//-------------------------------------------------------
    public static final int PELLET_SIZE = 3;  
    public static final int MAX_PELLETS = 100;  
    public static final int MAX_LOVELIES = 200; 
    
    public static final int NUMBER_OF_PELLETS_ALLOWED = 50;
//-------------------------------------------------------
// Constants to define four directions
//-------------------------------------------------------  
    public static final int UP = 0;
    public static final int DOWN = 1;
    public static final int LEFT = 2;
    public static final int RIGHT = 3;
    
//-------------------------------------------------------
// Colours which could be used for the game
//------------------------------------------------------- 
    public static final Color[] COLOURS = {new Color(224,32,32), new Color(145,32,224), new Color(32,32,224), new Color(41,223,225), new Color(27,234,56), new Color(176,228,53), new Color(255,252,29), new Color(255,138,0)};
    public static final Color NICE_GRAY_COLOUR = new Color(157, 161, 158);
    public static final Color GAME_SCREEN_COLOUR = Color.GREEN; 
    
//-------------------------------------------------------
// Some Font sizes and Fonts
//-------------------------------------------------------
    public static final int TINY_FONT_SIZE = 12;
    public static final int VERY_SMALL_FONT_SIZE = 14;
    public static final int SMALL_FONT_SIZE = 16;
    public static final int MEDIUM_FONT_SIZE = 18;
    public static final int LARGE_FONT_SIZE = 30;
    public static final int HUGE_FONT_SIZE = 55;
    
    public static final Font TINY_FONT = new Font("ARIAL BLACK", Font.BOLD, TINY_FONT_SIZE);
    public static final Font VERY_SMALL_FONT = new Font("ARIAL BLACK", Font.BOLD, VERY_SMALL_FONT_SIZE);
    public static final Font SMALL_FONT = new Font("ARIAL BLACK", Font.CENTER_BASELINE, SMALL_FONT_SIZE);
    public static final Font MEDIUM_FONT = new Font("ARIAL BLACK", Font.CENTER_BASELINE, MEDIUM_FONT_SIZE);;
    public static final Font LARGE_FONT = new Font("SansSerif", Font.BOLD, LARGE_FONT_SIZE); 
    public static final Font HUGE_FONT = new Font("SansSerif", Font.BOLD, HUGE_FONT_SIZE);
    
    
//-------------------------------------------------------
// Possible x, y position for the game information
//-------------------------------------------------------
    public static final Point SCORE_POSITION = new Point(20, 30);
    public static final Point INFORMATION_POSITION1 = new Point(400, 20);
    public static final Point INFORMATION_POSITION2 = new Point(400, 40);
    public static final Point INFORMATION_POSITION3 = new Point(400, 60);
    public static final Point WINNER_LOSER_INFO_POSITION = new Point(60, STRIP_AREAS[2].y + STRIP_AREAS[2].height -50);
    
}
