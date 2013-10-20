/*
 * Purpose: stores the constants for
 * the A1 game.
 * 
 * Author: xx
 * Date: S2 2013
*/

import java.awt.*;

public class A1Constants {	
//-------------------------------------------------------------------
//-------- Width and height of the JFrame ---------------------------
//-------------------------------------------------------------------
	public static final int JFRAME_AREA_WIDTH = 600;
	public static final int JFRAME_AREA_HEIGHT = 500;
	
//-------------------------------------------------------------------
//-------- Position for the game score and feedback -----------------
//-------------------------------------------------------------------	
	public static final Point SCORE_POSITION = new Point(10, 26);
		
//-------------------------------------------------------------------
//-------- Left position for the card display -----------------------
//-------------------------------------------------------------------
	public static final int CARD_DISPLAY_LEFT = 60;
	
//-------------------------------------------------------------------
//-------- Number of cards in the pack and number -------------------
//-------- of cards in each suit ------------------------------------
//-------------------------------------------------------------------		
	public static final int TOTAL_NUMBER_OF_CARDS = 52;//52
	public static final int CARDS_IN_EACH_SUIT = 13; //13
	
//-------------------------------------------------------------------
//-------- Number of cards across the JPanel ------------------------ 
//-------- and the number of cards down the JPanel ------------------
//-------------------------------------------------------------------	
	public static final int NUMBER_ROWS = 4;//4
	public static final int NUMBER_COLS = 13;//13	

//-------------------------------------------------------------------
//-------- A number to represent each suit in the pack --------------
//-------------------------------------------------------------------
	public static final int CLUBS = 0;
	public static final int DIAMONDS = CLUBS + 1;
	public static final int HEARTS = DIAMONDS + 1;
	public static final int SPADES = HEARTS + 1;

//-------------------------------------------------------------------
//-------- Colour of the background ----------------------------------
//-------------------------------------------------------------------
	public static final Color BACKGROUND_COLOUR = new Color(245, 227, 247);
}

