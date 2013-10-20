/*
  * Class which defines
  * a single Cell object.
  *
  *
  * author xx
  * version S2, 2013
*/

import java.awt.*; 
import javax.swing.*;


public class Cell { 	
	private static final int CELL_SIZE = A2Constants.CELL_SIZE;
	
	public static final Color TRIM_COLOUR = A2Constants.TRIM_COLOUR;	
	private static final Color HIDDEN_COLOUR = A2Constants.HIDDEN_COLOUR;
	private static final Color REVEALED_COLOUR = A2Constants.REVEALED_COLOUR;
	private static final Color ZERO_BOMB_NEIGHBOURS_COLOUR = A2Constants.ZERO_BOMB_NEIGHBOURS_COLOUR;	
	public static final Color BOMB_COLOUR = A2Constants.BOMB_COLOUR;	
	public static final Color FLAGGED_AS_A_BOMB_COLOUR = A2Constants.FLAGGED_AS_A_BOMB_COLOUR;		
		
	private static final int NEIGHBOUR_NUMBER_FONT_SIZE = CELL_SIZE/3 * 2;
	private static final Font NEIGHBOUR_NUMBER_FONT = new Font("GENEVA", Font.BOLD, NEIGHBOUR_NUMBER_FONT_SIZE);
	
	private Rectangle cellArea;
	private int neighbouringBombNum;
	
	private boolean hasABomb;
	private boolean flaggedAsABomb;
	private boolean isRevealed;
	private boolean hasBeenVisited;	//used for clearing the bomb-free areas
		
	public Cell(int x, int y) {  
		isRevealed = false;
		cellArea = new Rectangle(x, y, CELL_SIZE, CELL_SIZE);
	} 	
//-------------------------------------------------------------------
//-------- Accessor and mutator methods -----------------------------
//-------------------------------------------------------------------
	public void setBombsInNeighbourhood(int neighbours) {  
		neighbouringBombNum = neighbours;
	}
	
	public void setHasBeenVisited(boolean visited) {  
		hasBeenVisited = visited;
	}
	public boolean getHasBeenVisited() {  
		return hasBeenVisited;
	}
	
	public void setNeighbouringBombNum(int neighbourBombs) {  
		neighbouringBombNum = neighbourBombs;
	}
	public int getNeighbouringBombNum() {  
		return neighbouringBombNum;
	}

	public void setHasABomb(boolean bomb) {  
		hasABomb = bomb;
	}
	public boolean getHasABomb() {  
		return hasABomb;
	}
	
	public void setFlaggedAsABomb(boolean flagged) {  
		flaggedAsABomb = flagged;
	}
	public boolean getFlaggedAsABomb() {  
		return flaggedAsABomb;
	}
	
	public void setIsRevealed(boolean revealed) {  
		isRevealed = revealed;
	}

	public boolean getIsRevealed() {  
		return isRevealed;
	}
//-------------------------------------------------------------------
//-------- Returns true if the parameter Point object, --------------
//-------- pressPt, is inside the Cell area. ------------------------
//-------------------------------------------------------------------
	public boolean containsMousePress(Point pressPt) {
		return cellArea.contains(pressPt);				
	}
//-------------------------------------------------------------------
//-------- Get String describing the cell status --------------------
//-------------------------------------------------------------------
	public String getCellDebugInformation() { 
		String cellInfo = " " + cellArea.x + " " + cellArea.y + " ";
		if (flaggedAsABomb) {
			cellInfo = cellInfo + " flaggedAsABomb";		
		} else if (isRevealed) {
			cellInfo = cellInfo + " Revealed";
			if (hasABomb) {
				cellInfo = cellInfo + " Bomb";
			}
		} else {
				cellInfo = cellInfo + " Hidden";			
		}

		return cellInfo + ", Neighbours " + neighbouringBombNum;
	}
//-------------------------------------------------------------------
//-------- Draw the Cell  -------------------------------------------
//-------------------------------------------------------------------
	public void drawCell(Graphics g, boolean cheatPeek) {  
		if (cheatPeek) {
			drawCheatPeekCell(g);
			return;
		}
		int offset = 5;
		if (flaggedAsABomb) {
			g.setColor(FLAGGED_AS_A_BOMB_COLOUR);
			g.fillRect(cellArea.x, cellArea.y, cellArea.width, cellArea.height);
			if (isRevealed) {
				g.setColor(Color.WHITE);
				offset = 3;
				g.fillOval(cellArea.x + offset, cellArea.y + offset, cellArea.width - offset * 2, cellArea.height - offset * 2);					
			}
		} else if (hasABomb && isRevealed) {
			g.setColor(BOMB_COLOUR);
			g.fillRect(cellArea.x, cellArea.y, cellArea.width, cellArea.height);
			
			g.setColor(Color.BLACK);
			offset = 3;
			g.fillOval(cellArea.x + offset, cellArea.y + offset, cellArea.width - offset * 2, cellArea.height - offset * 2);
			
			g.setColor(Color.WHITE);
			offset = 6;
			g.drawLine(cellArea.x + cellArea.width / 2, cellArea.y + offset, cellArea.x + cellArea.width / 2, cellArea.y + cellArea.height - offset);
			g.drawLine(cellArea.x + offset, cellArea.y + cellArea.height / 2, cellArea.x + cellArea.width - offset, cellArea.y + cellArea.height / 2);
		} else if (isRevealed) {
			if (neighbouringBombNum == 0) {
				g.setColor(ZERO_BOMB_NEIGHBOURS_COLOUR);
				g.fillRect(cellArea.x, cellArea.y, cellArea.width, cellArea.height);
			} else {
				g.setColor(REVEALED_COLOUR);
				g.fillRect(cellArea.x, cellArea.y, cellArea.width, cellArea.height);
				drawNeighbourNumber(g);	
			}
		} else {
			g.setColor(HIDDEN_COLOUR);
			g.fillRect(cellArea.x, cellArea.y, cellArea.width, cellArea.height);		
		}
		
		g.setColor(TRIM_COLOUR);
		g.drawRect(cellArea.x, cellArea.y, cellArea.width, cellArea.height);
	}
	private void drawCheatPeekCell(Graphics g) {  
		int offset = 5;
		if (flaggedAsABomb) {
			g.setColor(FLAGGED_AS_A_BOMB_COLOUR);
			g.fillRect(cellArea.x, cellArea.y, cellArea.width, cellArea.height);
			if (hasABomb) {
				g.setColor(Color.WHITE);
				offset = 3;
				g.fillOval(cellArea.x + offset, cellArea.y + offset, cellArea.width - offset * 2, cellArea.height - offset * 2);				
			} 					
		} else if (hasABomb) {
			g.setColor(BOMB_COLOUR);
			g.fillRect(cellArea.x, cellArea.y, cellArea.width, cellArea.height);
			
			g.setColor(Color.BLACK);
			offset = 3;
			g.fillOval(cellArea.x + offset, cellArea.y + offset, cellArea.width - offset * 2, cellArea.height - offset * 2);
			
			g.setColor(Color.WHITE);
			offset = 6;
			g.drawLine(cellArea.x + cellArea.width / 2, cellArea.y + offset, cellArea.x + cellArea.width / 2, cellArea.y + cellArea.height - offset);
			g.drawLine(cellArea.x + offset, cellArea.y + cellArea.height / 2, cellArea.x + cellArea.width - offset, cellArea.y + cellArea.height / 2);
		} else {
			if (neighbouringBombNum == 0) {
				g.setColor(ZERO_BOMB_NEIGHBOURS_COLOUR);
				g.fillRect(cellArea.x, cellArea.y, cellArea.width, cellArea.height);
			} else {
				g.setColor(REVEALED_COLOUR);
				g.fillRect(cellArea.x, cellArea.y, cellArea.width, cellArea.height);
				drawNeighbourNumber(g);	
			}
		}
		
		g.setColor(TRIM_COLOUR);
		g.drawRect(cellArea.x, cellArea.y, cellArea.width, cellArea.height);		
		
	}	
	private void drawNeighbourNumber(Graphics g) {
		if (neighbouringBombNum > 0) {
			g.setColor(TRIM_COLOUR);
			g.setFont(NEIGHBOUR_NUMBER_FONT);
			g.drawString("" + neighbouringBombNum, cellArea.x + cellArea.width / 3, cellArea.y + cellArea.height * 2 / 3);		
		}
	}  
//-------------------------------------------------------------------
//-------- Get String describing the card suit and value ------------
//-------------------------------------------------------------------
	public String toString() { 
		return cellArea.x + " " + cellArea.y + " " + neighbouringBombNum + " " + hasABomb + " " + flaggedAsABomb + " " + isRevealed + " " + hasBeenVisited;		
	}	
} 