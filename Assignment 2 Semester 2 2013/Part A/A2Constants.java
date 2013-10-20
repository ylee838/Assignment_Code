/*
 * Purpose: stores the constants for
 * the A2 game.
 * 
 * Author: xx
 * Date: S2 2013
*/

import java.awt.*;

public class A2Constants {	
//-------------------------------------------------------------------
//-------- Size of each cell ----------------------------------------
//-------------------------------------------------------------------
	public static final int CELL_SIZE = 30;

//-------------------------------------------------------------------
//-------- Top left position of the Cell block ----------------------
//-------------------------------------------------------------------
	public static final int GAP_TOP = 100;
	public static final int GAP_LEFT = 200;
			
//-------------------------------------------------------------------
//-------- Number of rows, cols in the Cell grid --------------------
//-------------------------------------------------------------------		
	public static final int NUMBER_OF_ROWS = 10;
	public static final int NUMBER_OF_COLS = 8;
//-------------------------------------------------------------------
//-------- Number of bombs in the Cell grid -------------------------
//-------------------------------------------------------------------
	public static final int NUMBER_OF_BOMBS = (int)(NUMBER_OF_ROWS * NUMBER_OF_COLS * 0.15625);	
		
//-------------------------------------------------------------------
//-------- Width and height of the JFrame ---------------------------
//-------------------------------------------------------------------
	public static final int JFRAME_AREA_WIDTH = NUMBER_OF_COLS * CELL_SIZE + GAP_LEFT + 10;
	public static final int JFRAME_AREA_HEIGHT = NUMBER_OF_ROWS * CELL_SIZE + GAP_TOP + 10;
	
//-------------------------------------------------------------------
//-------- Various colours used in the game -------------------------
//-------------------------------------------------------------------
 	public static final Color BACKGROUND_COLOUR = new Color(255,240,245);	//lavender blush
	public static final Color IN_BOMB_MODE_COLOUR = Color.RED;
	public static final Color NOT_IN_BOMB_MODE_COLOUR = Color.GREEN;
	public static final Color TRIM_COLOUR = new Color(105,105,105);	//dim grey

//-------------------------------------------------------------------
//-------- Colours used for the cells (and the legend) --------------
//-------------------------------------------------------------------		
	public static final Color HIDDEN_COLOUR = new Color(100,149,237);	//corn flowerblue
	public static final Color REVEALED_COLOUR = new Color(218,112,214);	//orchid
	public static final Color ZERO_BOMB_NEIGHBOURS_COLOUR = new Color(240,248,255);	//alice blue	
	public static final Color BOMB_COLOUR = new Color(148,0,211);	//dark violet
	public static final Color FLAGGED_AS_A_BOMB_COLOUR = new Color(255,20,147);	//deep pink
	public static final Color SCORE_COLOUR = new Color(100,149,237);	//corn flowerblue
	public static final Color JUST_CLEARED_COLOUR = new Color(233, 0, 211);

//-------------------------------------------------------------------
//-------- Constants used for the displaying the legend -------------
//-------------------------------------------------------------------	
	public static final int LEGEND_FONT_SIZE = CELL_SIZE/3;
	public static final Font LEGEND_FONT = new Font("COURIER", Font.BOLD, LEGEND_FONT_SIZE);
	
//-------------------------------------------------------------------
//-------- Constants used for the displaying the game score ---------
//-------------------------------------------------------------------	
	public static final int SCORE_FONT_SIZE = CELL_SIZE *2/3;
	public static final int LARGE_SCORE_FONT_SIZE = CELL_SIZE + 10;
	public static final Font SCORE_FONT = new Font("Times", Font.BOLD, SCORE_FONT_SIZE);
	public static final Font LARGE_SCORE_FONT = new Font("Times", Font.BOLD, LARGE_SCORE_FONT_SIZE);
	public static final Point LEFT_SCORE_POSITION = new Point(10, LEGEND_FONT_SIZE + 10);
		
//-------------------------------------------------------------------
//-------- Constants used for displaying the legend -----------------
//-------------------------------------------------------------------		
	public static final int LEGEND_TOP = GAP_TOP;
	public static final int LEGEND_LEFT = 130;	
	
	public static final int MODE_INDEX = 0;	
	public static final int BOMB_INDEX = 4;	
	
	public static final String[] LEGEND_STRINGS = {	"               Mode ",
													"       Hidden cells ",
													"     Revealed cells ",
													" No bomb neighbours ",
													"               Bomb ",
													"    Flagged as bomb "
												};
	public static final Rectangle[] LEGEND_RECTS = {
													new Rectangle(LEGEND_LEFT - CELL_SIZE, LEGEND_TOP, CELL_SIZE * 2, CELL_SIZE * 2),
													new Rectangle(LEGEND_LEFT, LEGEND_TOP + CELL_SIZE * 2 + 40, CELL_SIZE, CELL_SIZE),
													new Rectangle(LEGEND_LEFT, LEGEND_TOP + CELL_SIZE * 3 + 60, CELL_SIZE, CELL_SIZE),
													new Rectangle(LEGEND_LEFT, LEGEND_TOP + CELL_SIZE * 4 + 70, CELL_SIZE, CELL_SIZE),
													new Rectangle(LEGEND_LEFT, LEGEND_TOP + CELL_SIZE * 5 + 80, CELL_SIZE, CELL_SIZE),
													new Rectangle(LEGEND_LEFT, LEGEND_TOP + CELL_SIZE * 6 + 90, CELL_SIZE, CELL_SIZE)													
												};
												
	public static final Color[] LEGEND_COLOURS = {NOT_IN_BOMB_MODE_COLOUR, HIDDEN_COLOUR, REVEALED_COLOUR, ZERO_BOMB_NEIGHBOURS_COLOUR, BOMB_COLOUR, FLAGGED_AS_A_BOMB_COLOUR};

	public static final String[] GAME_DIRECTIONS_STRINGS = { "m = change mode",
													         "e = end game",
															 "n = new game",
													         "c = cheat peek",
												};
	public static final Point[] GAME_DIRECTIONS_POSITIONS = { new Point(JFRAME_AREA_WIDTH - 12 * LEGEND_FONT_SIZE, LEFT_SCORE_POSITION.y),
															  new Point(JFRAME_AREA_WIDTH - 12 * LEGEND_FONT_SIZE, LEFT_SCORE_POSITION.y + LEGEND_FONT_SIZE + 3),
															  new Point(JFRAME_AREA_WIDTH - 12 * LEGEND_FONT_SIZE, LEFT_SCORE_POSITION.y + LEGEND_FONT_SIZE * 2 + 6),
															  new Point(JFRAME_AREA_WIDTH - 12 * LEGEND_FONT_SIZE, LEFT_SCORE_POSITION.y + LEGEND_FONT_SIZE * 3 + 9)
												};
}

/*
//Other colours which have not been used	
new Color(224,255,255);	//light cyan
new Color(100,149,237);	//corn flowerblue
new Color(147,112,219);  //medium purple
new Color(218,112,214);	//orchid
new Color(144,238,144);	//light green	
new Color(255,240,245);	//lavender blush
new Color(245,255,250);	//mint cream	
new Color(255,250,250);	//snow
new Color(255,20,147);	//deep pink
new Color(148,0,211);	//dark violet
new Color(105,105,105);	//dim grey
new Color(240,248,255);	//alice blue
1
new Color(150, 235, 164;
new Color(194, 208, 196;
new Color(233, 0, 211);
new Color(255,240,245);
new Color(245,255,250);
new Color(0, 250, 154);
*/