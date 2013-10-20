/*
COMMENT
*/

import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class A2JPanel extends JPanel implements MouseListener, KeyListener {
	private static final Color BACKGROUND_COLOUR = A2Constants.BACKGROUND_COLOUR;
	public static final int JFRAME_AREA_WIDTH = A2Constants.JFRAME_AREA_WIDTH;
	public static final int JFRAME_AREA_HEIGHT = A2Constants.JFRAME_AREA_HEIGHT;
	
	private static final int CELL_SIZE = A2Constants.CELL_SIZE;	
	
	private static final int NUMBER_OF_ROWS = A2Constants.NUMBER_OF_ROWS;
	private static final int NUMBER_OF_COLS = A2Constants.NUMBER_OF_COLS;
	public static final int NUMBER_OF_BOMBS = A2Constants.NUMBER_OF_BOMBS;
	
	public static final int GAP_TOP = A2Constants.GAP_TOP;
	public static final int GAP_LEFT = A2Constants.GAP_LEFT;
	public static final int LEGEND_TOP = A2Constants.LEGEND_TOP;
	
	public static final Color[] LEGEND_COLOURS = A2Constants.LEGEND_COLOURS;
	public static final int BOMB_INDEX = A2Constants.BOMB_INDEX;
	public static final int MODE_INDEX = A2Constants.MODE_INDEX;
	private static final int LEGEND_FONT_SIZE = A2Constants.LEGEND_FONT_SIZE;
	private static final Font LEGEND_FONT = A2Constants.LEGEND_FONT;			
	public static final Rectangle[] LEGEND_RECTS = A2Constants.LEGEND_RECTS;	
	public static final String[] LEGEND_STRINGS = A2Constants.LEGEND_STRINGS;
	
	public static final String[] GAME_DIRECTIONS_STRINGS = A2Constants.GAME_DIRECTIONS_STRINGS;
	public static final Point[] GAME_DIRECTIONS_POSITIONS = A2Constants.GAME_DIRECTIONS_POSITIONS;
	
	private static final Color HIDDEN_COLOUR = A2Constants.HIDDEN_COLOUR;
	private static final Color REVEALED_COLOUR = A2Constants.REVEALED_COLOUR;
	private static final Color ZERO_BOMB_NEIGHBOURS_COLOUR = A2Constants.ZERO_BOMB_NEIGHBOURS_COLOUR;	
	public static final Color BOMB_COLOUR = A2Constants.BOMB_COLOUR;	
	public static final Color TRIM_COLOUR = A2Constants.TRIM_COLOUR;
	public static final Color IN_BOMB_MODE_COLOUR = A2Constants.IN_BOMB_MODE_COLOUR;
	public static final Color NOT_IN_BOMB_MODE_COLOUR = A2Constants.NOT_IN_BOMB_MODE_COLOUR;

	public static final Color SCORE_COLOUR = A2Constants.SCORE_COLOUR;	
	public static final Color JUST_CLEARED_COLOUR = A2Constants.JUST_CLEARED_COLOUR;
	public static final int SCORE_FONT_SIZE = A2Constants.SCORE_FONT_SIZE;
	public static final Font SCORE_FONT = A2Constants.SCORE_FONT;
	public static final int LARGE_SCORE_FONT_SIZE = A2Constants.LARGE_SCORE_FONT_SIZE;
	public static final Font LARGE_SCORE_FONT = A2Constants.LARGE_SCORE_FONT;	
	public static final Point LEFT_SCORE_POSITION = A2Constants.LEFT_SCORE_POSITION;
 
	private Cell[][] cells;
	
	private int cellsJustCleared;
	
	private boolean gameHasEnded;
	private boolean userHasWon;	
	
			//allows the user to mark bombs
	private boolean inBombDismantlingMode;
	
			//allows the user to check the cell set up
	private boolean inCheatMode;	
	
	private int numberOfLives;	
	
//---------------------------------------------------------------------	
//---------------------------------------------------------------------	
// Constructor and reset() method, creates grid of cells, -------------
// calls the methods which set up the bombs ---------------------------	
// and bomb neighbour numbers - ---------------------------------------	
//---------------------------------------------------------------------	
	public A2JPanel() {
		setBackground(BACKGROUND_COLOUR);
		
		addKeyListener(this);
		addMouseListener(this);
		
		reset();
	}
	
	private void reset() {
		createEmptyGridOfCells();
		plantRandomBombs();
		setUpCellBombNeighbourNumbers();
		
		numberOfLives = NUMBER_OF_BOMBS / 4;
		cellsJustCleared = 0;

		inCheatMode = false;
		inBombDismantlingMode = false;
		gameHasEnded = false;
		userHasWon = false;
	}
	
	private void createEmptyGridOfCells() {
		cells = new Cell[NUMBER_OF_ROWS][NUMBER_OF_COLS];
		int x = GAP_LEFT;
		int y = GAP_TOP;
		int numberAcross = 0;
		
		for (int i = 0; i < cells.length; i++) {
			for (int j = 0; j < cells[i].length; j++) {
				cells[i][j] = new Cell(x, y);
				x = x + CELL_SIZE;
				numberAcross++;
				
				if (numberAcross >= NUMBER_OF_COLS) {
					x = GAP_LEFT;
					y = y + CELL_SIZE;
					numberAcross = 0;
				}
			}
		}
	}
//---------------------------------------------------------------------
// Stage 1
//-------- Draw all the Cells  ----------------------------------------
//---------------------------------------------------------------------	
	private void drawCells(Graphics g) {
		for(int row = 0; row < cells.length; row++){
			for(int col = 0; col < cells[row].length; col++){
				if(cells[row][col] != null){
					if(inCheatMode){
						cells[row][col].drawCell(g, true);
					}else{
						cells[row][col].drawCell(g, false);
					}		
				}
			}
		}
	}
	
//---------------------------------------------------------------------	
// Stage 2
// Create the correct number of bombs (constant NUMBER_OF_BOMBS)
//---------------------------------------------------------------------		
	private void plantRandomBombs() {
		int currentlyPlantedBombs = 0;
		
		for(int i = 0; i < NUMBER_OF_BOMBS; i++){
			int selectedRow = (int) (Math.random()*NUMBER_OF_ROWS);
			int selectedCol = (int) (Math.random()*NUMBER_OF_COLS);
			
			if(currentlyPlantedBombs < NUMBER_OF_BOMBS){
				if(isBombAllowedToBePlaced(selectedRow, selectedCol) && ! cells[selectedRow][selectedCol].getHasBeenVisited()){
					cells[selectedRow][selectedCol].setHasABomb(true);
					cells[selectedRow][selectedCol].setHasBeenVisited(true);
					currentlyPlantedBombs++;
				}
			}
		}
	}
	
	private boolean isBombAllowedToBePlaced(int row, int col) {
		if(cells[row][col].getHasABomb()){
			return false;
		}else{
			return true;
		}
	}
//---------------------------------------------------------------------	
// Stage 3
// Set up the correct number of bomb neighbours for each cell
//---------------------------------------------------------------------		
	private void setUpCellBombNeighbourNumbers() {
			for(int row = 0; row < NUMBER_OF_ROWS; row++){
				for(int col = 0; col < cells[row].length; col++){
					if(cells[row][col] != null){
						if((getBombOrNoBomb(row,col)) != 1){
							if(row+1 <= NUMBER_OF_ROWS){
								if(getBombOrNoBomb(row+1, col) == 1){
									cells[row][col].setNeighbouringBombNum((cells[row][col].getNeighbouringBombNum()+1));
								}
								
							}
							if(col+1 <= NUMBER_OF_COLS){
								if(getBombOrNoBomb(row, col+1) == 1){
									cells[row][col].setNeighbouringBombNum((cells[row][col].getNeighbouringBombNum()+1));
								}
								
							}
							if(row-1 <= NUMBER_OF_ROWS){
								if(getBombOrNoBomb(row-1, col) == 1){
									cells[row][col].setNeighbouringBombNum((cells[row][col].getNeighbouringBombNum()+1));
								}
								
							}
							if(col-1 <= NUMBER_OF_COLS){
								if(getBombOrNoBomb(row, col-1) == 1){
									cells[row][col].setNeighbouringBombNum((cells[row][col].getNeighbouringBombNum()+1));
								}
								
							}
							if(row-1 <= NUMBER_OF_ROWS && col+1 <= NUMBER_OF_COLS){
								if(getBombOrNoBomb(row-1, col+1) == 1){
									cells[row][col].setNeighbouringBombNum((cells[row][col].getNeighbouringBombNum()+1));
								}
								
							}
							if(row-1 <= NUMBER_OF_ROWS && col-1 <= NUMBER_OF_COLS){
								if(getBombOrNoBomb(row-1, col-1) == 1){
									cells[row][col].setNeighbouringBombNum((cells[row][col].getNeighbouringBombNum()+1));
								}
								
							}
							if(row+1 <= NUMBER_OF_ROWS && col+1 <= NUMBER_OF_COLS){
								if(getBombOrNoBomb(row+1, col+1) == 1){
									cells[row][col].setNeighbouringBombNum((cells[row][col].getNeighbouringBombNum()+1));
								}
								
							}
							if(row+1 <= NUMBER_OF_ROWS && col-1 <= NUMBER_OF_COLS){
								if(getBombOrNoBomb(row+1, col-1) == 1){
									cells[row][col].setNeighbouringBombNum((cells[row][col].getNeighbouringBombNum()+1));
								}
								
							}	
						}
					}
				}	
			}
	}
	
	private int getBombOrNoBomb(int row, int col) {
		try{
			if(row <= NUMBER_OF_ROWS && col <= NUMBER_OF_COLS){
				if(cells[row][col].getHasABomb()){
					return 1;
				}
			}
		}
		catch(ArrayIndexOutOfBoundsException e){
			return 0;
		}
		return 0;
	}

//---------------------------------------------------------------------	
// Stage 4
// Reset all the cells to not visited 
//---------------------------------------------------------------------	
	private void resetHasBeenVisitedInAllCells() {	
		for(int i = 0; i < cells.length; i++){
			for(int j = 0; j < cells[i].length; j++){
				cells[i][j].setHasBeenVisited(false);
			}
		}
	}
//---------------------------------------------------------------------	
// Stage 5 
// Clear all the unnumbered cells connected to the parameter
// row, col cell -recursive methods
//---------------------------------------------------------------------
	private void clearBombFreeArea(int row, int col) {	
		try{	
			if(cells[row][col].getHasABomb()){
				return;
			}

			if(cells[row][col].getHasBeenVisited()){
				return;
			}
			if(cells[row][col].getNeighbouringBombNum() > 0){
				cells[row][col].setIsRevealed(true);
				cells[row][col].setHasBeenVisited(true);
				return;
			}
			
			if(!(cells[row][col].getHasBeenVisited())){		
				if(cells[row][col].getNeighbouringBombNum() == 0){
					cells[row][col].setIsRevealed(true);
					cells[row][col].setHasBeenVisited(true);
					clearBombFreeArea(row+1, col);
					clearBombFreeArea(row+1, col+1);
					clearBombFreeArea(row, col+1);
					clearBombFreeArea(row+1, col-1);
					clearBombFreeArea(row-1, col);
					clearBombFreeArea(row, col-1);
					clearBombFreeArea(row-1, col-1);
					clearBombFreeArea(row-1, col+1);
				}

			}
		}catch(ArrayIndexOutOfBoundsException e){
			return;
		}
			
	}
	
//---------------------------------------------------------------------	
// Stage 6 
// Clear all the unnumbered cells connected to the parameter
// row, col cell -recursive methods
//---------------------------------------------------------------------
	private int getNumberCleared(int row, int col) {	
		int number = 0;
		try{
			if(!(cells[row][col].getHasBeenVisited())){		
				if(cells[row][col].getNeighbouringBombNum() == 0){
					number++;
					cells[row][col].setHasBeenVisited(true);
					number += getNumberCleared(row+1, col);
					number += getNumberCleared(row+1, col+1);
					number += getNumberCleared(row, col+1);
					number += getNumberCleared(row+1, col-1);
					number += getNumberCleared(row-1, col);
					number += getNumberCleared(row, col-1);
					number += getNumberCleared(row-1, col-1);
					number += getNumberCleared(row-1, col+1);
					return number;
				}
			}
			return 0;
		}catch(ArrayIndexOutOfBoundsException e){
			
		}
		return 0;
	}
		
		

		

//---------------------------------------------------------------------
//-------- Write To File ----------------------------------------------
//---------------------------------------------------------------------	
// Stage 7
// Write the current game information to the file.
// The name of the file is given by the String parameter.
// http://thebest404pageever.com/swf/snap_your_shit.swf
// http://thebest404pageever.com/swf/squarey.swf	
// NOTE: the loadFromFile() method is at the end of this code.
//---------------------------------------------------------------------	
//---------------------------------------------------------------------	
	private void saveToFile(String fileName) {
		
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
			bw.write(""+NUMBER_OF_ROWS);
			bw.newLine();
			bw.write(""+NUMBER_OF_COLS);
			for(int i = 0; i < NUMBER_OF_ROWS; i++){
				for(int j = 0; j < cells[i].length; j++){
					bw.newLine();
					bw.write(cells[i][j].toString());
				}
			}
			bw.newLine();
			bw.write(""+gameHasEnded);
			bw.newLine();
			bw.write(""+userHasWon);
			bw.newLine();
			bw.write(""+inCheatMode);
			bw.newLine();
			bw.write(""+numberOfLives);
			bw.newLine();
			bw.close();
		
		} catch (IOException e) {
		
			
		}
	}


//---------------------------------------------------------------------	
// Handle MouseEvents
//---------------------------------------------------------------------	
	public void mousePressed(MouseEvent e) {	
		cellsJustCleared = 0;
		if (gameHasEnded || inCheatMode) {
			return;
		}
		
		Point mousePressCell = getCellPositionOfMousePress(e.getPoint());
		int r, c;
		boolean setFlagTo;
		
		if (mousePressCell != null) {
			r = mousePressCell.x;
			c = mousePressCell.y;
					//cell has already been revealed
			if (cells[r][c].getIsRevealed()) {
				return; 
			}
					//If in bomb dismantling mode
			if (inBombDismantlingMode) {
				setFlagTo = true;
			
					//If cell is already flagged, remove the flag
				if (cells[r][c].getFlaggedAsABomb()) {
					setFlagTo = false;
				}
				
				cells[r][c].setFlaggedAsABomb(setFlagTo);
				
				repaint();
				return;		
			}
					//ignore the cell if it is flagged as a bomb		
			if (cells[r][c].getFlaggedAsABomb()) {
				return;
			}
					//if there is a bomb, remove a life
			if (cells[r][c].getHasABomb()) {
				cells[r][c].setIsRevealed(true);
				numberOfLives--;

				doEndOfGameChecks();
				repaint();
				return;
			}
						
			if (cells[r][c].getNeighbouringBombNum() == 0) {
				resetHasBeenVisitedInAllCells(); //stage 5
				cellsJustCleared = getNumberCleared(r, c);   //stage 5
				
				resetHasBeenVisitedInAllCells(); //stage 4
				clearBombFreeArea(r, c);  //stage 4				
			}
			
			cells[r][c].setIsRevealed(true);
			
			doEndOfGameChecks();
		}

		repaint();
	}
	
	private int getNumberCleared() {
		int cleared = 0;
		for (int i = 0; i < cells.length; i++) {
			for (int j = 0; j < cells[i].length; j++) {
				if (!cells[i][j].getHasABomb() && cells[i][j].getIsRevealed()) {
					cleared++;
				}
			}		
		}
		
		return cleared;
	}
//---------------------------------------------------------------------	
// Get cell row,col in which mouse was pressed
//---------------------------------------------------------------------	
	private Point getCellPositionOfMousePress(Point mousePressPt) {	
		for (int i = 0; i < cells.length; i++) {
			for (int j = 0; j < cells[i].length; j++) {
				if (cells[i][j].containsMousePress(mousePressPt)) {
					return new Point(i, j);
				}
			}		
		}
		
		return null;	
	}

	public void mouseClicked(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
//---------------------------------------------------------------------	
// Handle KeyEvents
//---------------------------------------------------------------------	
	public void keyPressed(KeyEvent e) {
		char userC = e.getKeyChar();
		if (userC == 's' || userC == 'S') {
			saveToFile("SavedGame.txt");
			repaint();
			return;
		} else if (userC == 'l' || userC == 'L') {
			loadFromFile("SavedGame.txt");
			repaint();
			return;
		}
			
		if (gameHasEnded && userC != 'n' && userC != 'N') {
				//If game end the user must press new game
				
			return;
		} else if (!gameHasEnded && inCheatMode && userC != 'c' && userC != 'C') {
				//If in cheat mode the user must press c to move back to game mode
			return;
		}
		
		if (userC == 'n' || userC == 'N') {
				//Start a new game
			reset();
		} else if (userC == 'e' || userC == 'E') {
				//User chooses to end the game and display cells	
			doEndOfGameChecks();
			gameHasEnded = true;
		} else if (userC == 'm' || userC == 'M') {
				//User toggles between bomb dismantling mode 
				//and non-bomb dismantling mode
			inBombDismantlingMode = !inBombDismantlingMode;
		} else if (userC == 'c' || userC == 'C') {
				//User toggles between cheat mode and non-cheat mode
			
			inCheatMode = !inCheatMode;
		} else if (userC == 's' || userC == 'S') {
			saveToFile("SavedGame.txt");
		} else if (userC == 'l' || userC == 'L') {
			loadFromFile("SavedGame.txt");
		}

		repaint();
	}
	
	private void doEndOfGameChecks() {	
		if (getGameHasEnded()) {
			gameHasEnded = true;
			userHasWon = false;
			if (userHasWon()) {
				userHasWon = true;
			}
		}
	}	
	
	private boolean userHasWon() {
		return 	numberOfLives > -1 && (getNumberCleared() == NUMBER_OF_ROWS * NUMBER_OF_COLS - NUMBER_OF_BOMBS);
	}
	
	private boolean getGameHasEnded() {
		if (gameHasEnded) {
			return true;
		}
		if (numberOfLives < 0) {
			return true;
		}	
		if (getNumberCleared() == NUMBER_OF_ROWS * NUMBER_OF_COLS - NUMBER_OF_BOMBS) {
			return true;
		}
		
		return false;
	}

	
	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}
//---------------------------------------------------------------------
//-------- Draw all the Cells and the legend --------------------------
//---------------------------------------------------------------------	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		drawCells(g);		

		if (gameHasEnded) {
			displayFinalGameResults(g);
		} else {
			displayGameDirections(g);
			displayGameInformation(g);
			displayLegend(g);
		}
	}
//---------------------------------------------------------------------
//-------- Display legend, current score, final results ---------------
//---------------------------------------------------------------------		
	private void displayFinalGameResults(Graphics g) {
		g.setFont(LARGE_SCORE_FONT);
		g.setColor(JUST_CLEARED_COLOUR);
		int xPos = LEFT_SCORE_POSITION.x;
		int yPos = LEGEND_TOP - 45;
		
		if (userHasWon) {
			g.drawString("CONGRATULATIONS! ", xPos, yPos);
		} else {
			g.drawString("BAD LUCK ", xPos, yPos);
		}
		g.setFont(SCORE_FONT);
		yPos = yPos + SCORE_FONT_SIZE * 2;
		
		g.drawString("Cleared: " + getNumberCleared() +" / " + (NUMBER_OF_ROWS * NUMBER_OF_COLS - NUMBER_OF_BOMBS), xPos, yPos);		
		yPos = yPos + SCORE_FONT_SIZE + 10;		
		
		if (numberOfLives > 0) {
			g.drawString("Lives left: " + numberOfLives, xPos, yPos);
		} else if (numberOfLives == 0) {
			g.drawString("No lives left:", xPos, yPos);
		} else {
			g.drawString("Too many lives lost", xPos, yPos);
		}
		
		int flagged = getNumberCorrectlyFlagged();
		if (flagged > 0) {
			yPos = yPos + SCORE_FONT_SIZE + 10;
			g.drawString("Correctly flagged: ", xPos, yPos);	
			yPos = yPos + SCORE_FONT_SIZE + 10;
			g.drawString("               " + getNumberCorrectlyFlagged() + " / " + NUMBER_OF_BOMBS, xPos, yPos);
		}
		
		yPos = yPos + SCORE_FONT_SIZE + 20;
		g.drawString("New game -", xPos, yPos);	
		yPos = yPos + SCORE_FONT_SIZE + 10;
		g.drawString("   press 'n'", xPos, yPos);	
	}	
	
	private void displayGameInformation(Graphics g) {
		g.setFont(SCORE_FONT);
		g.setColor(SCORE_COLOUR);
		int xPos = LEFT_SCORE_POSITION.x;
		int yPos = LEFT_SCORE_POSITION.y;
		
		g.drawString("Number of cleared cells: " + getNumberCleared() +" / " + (NUMBER_OF_ROWS * NUMBER_OF_COLS - NUMBER_OF_BOMBS), xPos, yPos);		
		
		yPos = yPos + SCORE_FONT_SIZE + 6;
		g.drawString("Bombs: " + NUMBER_OF_BOMBS, xPos, yPos);
		
		yPos = yPos + SCORE_FONT_SIZE + 6;
		if (numberOfLives > 0) {
			g.drawString("Lives left: " + numberOfLives, xPos, yPos);
		} else if (numberOfLives == 0) {
			g.drawString("No lives left:", xPos, yPos);
		}		
		
		if (cellsJustCleared > 0) {
			g.setFont(LARGE_SCORE_FONT);
			g.setColor(JUST_CLEARED_COLOUR);		
			yPos = yPos + LARGE_SCORE_FONT_SIZE + 3;
			g.drawString(" " + cellsJustCleared, xPos, yPos);		
		}
	}
	
	private void displayLegend(Graphics g) {
		g.setFont(LEGEND_FONT);
		int offset;
		Rectangle r;
		int left = 10;
		int x = 10;
		int y = 0;
		for (int i = 0; i < LEGEND_RECTS.length; i++) {
			g.setColor(LEGEND_COLOURS[i]);
			r = LEGEND_RECTS[i];
			y = LEGEND_RECTS[i].y + LEGEND_RECTS[i].height / 3 + LEGEND_FONT_SIZE;
			if (i == MODE_INDEX) {
				g.setColor(TRIM_COLOUR);	
				g.fillRect(r.x - 2, r.y - 2, r.width + 4, r.height + 4);
				g.drawString(LEGEND_STRINGS[i], x - CELL_SIZE, y);
				g.setColor(LEGEND_COLOURS[i]);
				if (inBombDismantlingMode) {
					g.setColor(IN_BOMB_MODE_COLOUR);
				}			
				g.fillOval(r.x, r.y, r.width, r.height);										
			} else {
				g.fillRect(r.x, r.y, r.width, r.height);
				if (i == BOMB_INDEX) {
					g.setColor(Color.BLACK);
					offset = 3;
					g.fillOval(r.x + offset, r.y + offset, r.width - offset * 2, r.height - offset * 2);
					g.setColor(Color.WHITE);
					offset = 6;
					g.drawLine(r.x + r.width / 2, r.y + offset, r.x + r.width / 2, r.y + r.height - offset);
					g.drawLine(r.x + offset, r.y + r.height / 2, r.x + r.width - offset, r.y + r.height / 2);				
				}
				g.setColor(TRIM_COLOUR);							
				g.drawString(LEGEND_STRINGS[i], x, y);			
				g.drawRect(r.x, r.y, r.width, r.height);
			}
		}
	}
	
	private void displayGameDirections(Graphics g) {
		g.setFont(LEGEND_FONT);
		g.setColor(TRIM_COLOUR);
		for (int i = 0; i < GAME_DIRECTIONS_STRINGS.length; i++) {
			g.drawString(GAME_DIRECTIONS_STRINGS[i], GAME_DIRECTIONS_POSITIONS[i].x, GAME_DIRECTIONS_POSITIONS[i].y);			
		}		
	}
		
	private int getNumberCorrectlyFlagged() {
		int flagged = 0;
		for (int i = 0; i < cells.length; i++) {
			for (int j = 0; j < cells[i].length; j++) {
				if (cells[i][j].getHasABomb() && cells[i][j].getFlaggedAsABomb()) {
					flagged++;
				}
			}		
		}
		
		return flagged;
	}
//---------------------------------------------------------------------
//-------- Load From File ---------------------------------------------
//---------------------------------------------------------------------	
// Loads a game which has been previously saved
// using the saveToFile() method
//---------------------------------------------------------------------	
//---------------------------------------------------------------------	
	public void loadFromFile(String fileName) {
		Scanner scan = null;
		String cellInfo;
		int rows, cols;
		try {
			scan = new Scanner(new File(fileName));
			rows = scan.nextInt();
			cols = scan.nextInt();
			cells = new Cell[rows][cols];
			scan.nextLine();

			
			for (int i = 0; i < cells.length; i++) {
				for (int j = 0; j < cells[i].length; j++) {
					cellInfo = scan.nextLine();
					cells[i][j] = createACell(cellInfo);
				}
			}

			gameHasEnded = scan.nextBoolean();
			userHasWon = scan.nextBoolean();
			inCheatMode = scan.nextBoolean();
			numberOfLives = scan.nextInt();
			scan.close();
		} catch(Exception e) {
			System.out.println("Error loading game from " + fileName);
		}

	}

	private Cell createACell(String info) {
		Cell cell;
		int suit, value, x, y;
		boolean selected, faceUp;

		Scanner scanInfo = new Scanner(info);

		x = scanInfo.nextInt();
		y = scanInfo.nextInt();
		cell = new Cell(x, y);
		cell.setNeighbouringBombNum(scanInfo.nextInt());
		cell.setHasABomb(scanInfo.nextBoolean());
		cell.setFlaggedAsABomb(scanInfo.nextBoolean());
		cell.setIsRevealed(scanInfo.nextBoolean());
		cell.setHasBeenVisited(scanInfo.nextBoolean());
		return cell;
	}
}
