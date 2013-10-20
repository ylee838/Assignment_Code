/*
 COMMENT
 */

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class A1JPanel extends JPanel implements ActionListener, MouseListener, KeyListener {
  public static final int JFRAME_AREA_WIDTH = A1Constants.JFRAME_AREA_WIDTH;
  public static final int JFRAME_AREA_HEIGHT = A1Constants.JFRAME_AREA_HEIGHT;
  
  private static final int CARDS_IN_EACH_SUIT = A1Constants.CARDS_IN_EACH_SUIT;
  private static final int TOTAL_NUMBER_OF_CARDS = A1Constants.TOTAL_NUMBER_OF_CARDS;
  
  private static final int NUMBER_ROWS = A1Constants.NUMBER_ROWS;
  private static final int NUMBER_COLS = A1Constants.NUMBER_COLS;
  
  private static final Color BACKGROUND_COLOUR = A1Constants.BACKGROUND_COLOUR;
  
  public static final Point SCORE_POSITION = A1Constants.SCORE_POSITION;
  private static final int CARD_DISPLAY_LEFT = A1Constants.CARD_DISPLAY_LEFT;
  
  //arraylist of cards to be dealt
  private ArrayList<Card> cardStack; 
  //stores the 2D array of cards on the table
  private Card[][] cardsOnTable; 
  //stores the number of cards currently 
  //in each row of the 2D array
  private int[] howManyInEachRow; 
  
  //The card which was most recently
  //removed from the table cards
  private Card lastRemovedCard;
  
  private Card currentlySelectedCard;
  private boolean selectedCardIsMoving; 
  private int cardMoveX, cardMoveY;
  
  //card which is face down
  //and represents the card stack
  private Card faceDownCard;
  
  private int cardWidth;
  private int cardHeight;
  
  private boolean cardStackIsEmpty;
  private boolean gameHasEnded; 
  
  private Timer t;
  //--------------------------------------------------------------------- 
  //--------------------------------------------------------------------- 
  // Stage 10  (6 Marks) variables used for storing the game situation
  //--------------------------------------------------------------------- 
  //--------------------------------------------------------------------- 
  private int cardsRemoved;
  private int numberOfCardsOnTable;
  
  public A1JPanel() {
    
    setBackground(BACKGROUND_COLOUR);
    loadAllCardImagesAndSetUpCardDimensions();
    
    t = new Timer(30, this);
    
    addKeyListener(this);
    addMouseListener(this);
    
    reset();
  }
  
  private void reset() {
    howManyInEachRow = new int[NUMBER_ROWS];
    for (int i = 0; i < howManyInEachRow.length; i++) {
      howManyInEachRow[i] = 0;
    }
    
    
    cardStack = createTheFullPack();
    cardStackIsEmpty = false;
    
    cardsOnTable = new Card[NUMBER_ROWS][NUMBER_COLS];
    addNextColOfCards(cardStack, cardsOnTable, howManyInEachRow);
    
    
    faceDownCard = createFaceDownCard();
    
    lastRemovedCard = null;
    currentlySelectedCard = null;
    selectedCardIsMoving = false;
    gameHasEnded = false;
    cardsRemoved = 0;
    numberOfCardsOnTable = 4;
    t.stop();
    
  }
  
  private Card createFaceDownCard() {
    final Point FACE_DOWN_CARD_POSITION = new Point(JFRAME_AREA_WIDTH - cardWidth - 10, cardHeight + 20);
    Card faceDownCard = new Card(0, 0);
    faceDownCard.setCardArea(FACE_DOWN_CARD_POSITION.x, FACE_DOWN_CARD_POSITION.y, cardWidth, cardHeight);
    faceDownCard.setIsFaceUp(false);
    return faceDownCard;
  } 
  //-------------------------------------------------------------------
  //-------- This method adds a col of random cards from the  ---------
  //-------- card stack to the cards on the table. --------------------
  //-------- The new cards are added to the right of each column ------
  //-------- The howManyInEachRow array needs to be updated. ----------
  //------------------------------------------------------------------- 
  //------------------------------------------------------------------- 
  /*
   Stage 3 (8 Marks)
   */
  private void addNextColOfCards(ArrayList<Card> cardStack, Card[][] cardsOnTable, int[] howManyInEachRow) {
    
    
    for(int row = 0; row < NUMBER_ROWS; row++){
      int cardStackSize = cardStack.size();
      int randomCard = (int) (Math.random()*cardStackSize - 1);
      if(cardStackSize == 0){
        if(! thereAreMoreAvailableMoves(cardStack, cardsOnTable, howManyInEachRow)){
          gameHasEnded = true;
        }
        
      }else{
        Card selectedCard = cardStack.get(randomCard);
        cardStack.remove(randomCard);
        selectedCard.setIsFaceUp(true); 
        cardsOnTable[row][howManyInEachRow[row]] = selectedCard;
        setupIndividualCardPositionAndSize(selectedCard, row , howManyInEachRow[row]);
        howManyInEachRow[row]++;
        numberOfCardsOnTable++;

      }    
    }
  }

  private void setupIndividualCardPositionAndSize(Card card, int row, int col) {
    final int CARD_DISPLAY_TOP = 65;
    final int DISPLAY_GAP = 6;
    
    int y = CARD_DISPLAY_TOP + (cardHeight + DISPLAY_GAP - 1) * row; 
    int x = CARD_DISPLAY_LEFT + (cardWidth / 3) * col;
    
    card.setCardArea(x, y, cardWidth, cardHeight);
    //System.out.println(x +" "+ y+ " ROW "+ row+" COL "+col);
    
  }
  //--------------------------------------------------------------------- 
  // Handle ActionEvents
  // Moves the card which is currently selected towards
  // the REMOVED_CARDS_POSITION in the JPanel. 
  // Once the card intersects the REMOVED_CARDS_POSITION
  // the card is placed exactly in position and the timer stops.
  //--------------------------------------------------------------------- 
  public void actionPerformed(ActionEvent e) {
    final Point REMOVED_CARDS_POSITION = new Point(JFRAME_AREA_WIDTH - cardWidth - 10, JFRAME_AREA_HEIGHT / 2);
    Rectangle removedCardsArea = new Rectangle(REMOVED_CARDS_POSITION.x, REMOVED_CARDS_POSITION.y, cardWidth, cardHeight);
    Rectangle cardArea;
    
    if (selectedCardIsMoving) {
      currentlySelectedCard.translate(cardMoveX, cardMoveY);
      cardArea = currentlySelectedCard.getCardArea();
      
      if (cardArea.intersects(removedCardsArea)) {
        selectedCardIsMoving = false;
        lastRemovedCard = currentlySelectedCard;
        lastRemovedCard.setCardArea(REMOVED_CARDS_POSITION.x, REMOVED_CARDS_POSITION.y, cardArea.width, cardArea.height);
        currentlySelectedCard = null;
        t.stop();
      }
    }
    
    repaint();
  }
  //--------------------------------------------------------------------- 
  // Handle MouseEvents
  //--------------------------------------------------------------------- 
  public void mousePressed(MouseEvent e) { 
    Card clickedOnCard;
    int emptyRowNumber;
    boolean cardHasBeenRemoved;
    boolean thereIsAUsableEmptyCol;
    Point rowColOfSelectedCard;
    Point pressPt = e.getPoint();
    
    if (selectedCardIsMoving || gameHasEnded) {
      return;
    }
    //Carry out appropriate action if user has
    //pressed inside the face down card representing
    //the pack cards.
    if (!cardStackIsEmpty && faceDownCard.isInsideCardArea(pressPt)) {
      addNextColOfCards(cardStack, cardsOnTable, howManyInEachRow);   
      
      if (currentlySelectedCard != null) {
        currentlySelectedCard.setIsSelected(false);
        currentlySelectedCard = null;
      }
      
      if (cardStack.size() == 0) {
        cardStackIsEmpty = true;
      }
    } else {
      //Carry out appropriate action if user has
      //pressed inside one of the right-most cards
      //in any one of the rows of table cards.
      rowColOfSelectedCard = getRowColOfSelectedCard(cardsOnTable, pressPt, howManyInEachRow);
      
      if (rowColOfSelectedCard != null) {
        clickedOnCard = cardsOnTable[rowColOfSelectedCard.x][rowColOfSelectedCard.y];
        
        if (currentlySelectedCard == null) {
          //previously selected card has been processed
          //and the user has selected another card.
          currentlySelectedCard = clickedOnCard;
          currentlySelectedCard.setIsSelected(true);
          //Check if the card needs to be removed
          cardHasBeenRemoved = setUpCardRemovalIfAppropriate(currentlySelectedCard, howManyInEachRow, rowColOfSelectedCard.x);
          
          //If the card wasn't removed,
          //check if the card should be moved to an 
          //empty row.  If yes, move the card.
          if (cardHasBeenRemoved == false) {
            emptyRowNumber = getEmptyRowNumber(howManyInEachRow);
            if (emptyRowNumber > -1) {
              moveSelectedCardToEmptyRow(cardsOnTable, rowColOfSelectedCard, howManyInEachRow, emptyRowNumber);
            }
            //Once moved, set the card to
            //not selected and set the
            //currentlySelectedCard to null
            currentlySelectedCard.setIsSelected(false);
            currentlySelectedCard = null;
          } 
        } 
      }
    }
    
    if (cardStackIsEmpty && getEmptyRowNumber(howManyInEachRow) == -1) {
      if (!thereAreMoreAvailableMoves(cardStack, cardsOnTable, howManyInEachRow)) {
        gameHasEnded = true;
      }
    }
    
    repaint();
  }
  //--------------------------------------------------------------------- 
  //--------------------------------------------------------------------- 
  /*
   Stage 6 (8 Marks)
   */
  private Point getRowColOfSelectedCard(Card[][] cards, Point pressPt, int[] howManyInEachRow) {
    for(int row = 0; row < cards.length; row++){
      for(int col = 0; col < howManyInEachRow[row]; col++){
        if(cards[row][col] != null){
          if(cards[row][col].isInsideCardArea(pressPt)){
            return (new Point(row,col));
          }
        }
      }
    }
    return null;
  }
  //--------------------------------------------------------------------- 
  //---------------------------------------------------------------------
  /*
   Stage 7 (8 Marks)
   */
  private boolean cardCanBeRemoved(Card cardToCheck, Card[][] cardsOnTable, int[] howManyInEachRow) {
    
    
    Card position = null;
    
    for(int i = 0; i < NUMBER_ROWS; i++){
      if(howManyInEachRow[i] > 1){
        position = cardsOnTable[i][howManyInEachRow[i] -1];
        
      }else{
        position = cardsOnTable[i][0];
        
      }
      if((cardToCheck != position && cardToCheck != null)){
        if(cardToCheck == cardsOnTable[i][0] && howManyInEachRow[i] > 1 ){
          return false;
        }
        if(cardToCheck.hasSmallerValue(position) && cardToCheck.isSameSuit(position)){
          cardsRemoved++;
          numberOfCardsOnTable--;
          return true;             
        }
        
      }

    }  
    return false;
  }

  //--------------------------------------------------------------------- 
  //---------------------------------------------------------------------
  /*
   Stage 8 (6 Marks)
   */
  private int getEmptyRowNumber(int[] howManyInEachRow) {
    for(int i = 0; i < NUMBER_ROWS; i++){
      if(howManyInEachRow[i] == 0){
        return i;
      }
      
    }
    return -1;
  }
  //--------------------------------------------------------------------- 
  //---------------------------------------------------------------------
  /*
   Stage 9 (8 Marks)
   */
  private void moveSelectedCardToEmptyRow(Card[][] cardsOnTable, Point rowColOfCardToMove, int[] howManyInEachRow, int whichRow) {
    whichRow = getEmptyRowNumber(howManyInEachRow);
    Card position = null;
    
    for(int i = 0; i < NUMBER_ROWS; i++){
      if(howManyInEachRow[i] > 1){
        position = cardsOnTable[i][howManyInEachRow[i] -1];
        
        
      }else{
        position = cardsOnTable[i][0];
        
      }
      if(position == cardsOnTable[rowColOfCardToMove.x][rowColOfCardToMove.y]){
        cardsOnTable[whichRow][0] = position;
        setupIndividualCardPositionAndSize(position, whichRow, 0);
        howManyInEachRow[whichRow]++;
        howManyInEachRow[rowColOfCardToMove.x]--;
        
      }
    }
    
  }
  
  private boolean setUpCardRemovalIfAppropriate(Card cardToCheck, int[] howManyInEachRow, int whichColumn) {
    if (cardToCheck == null || selectedCardIsMoving || !cardToCheck.getIsSelected()) {
      return false;
    }
    
    final Point REMOVED_CARDS_CENTRE_POSITION = new Point(JFRAME_AREA_WIDTH - cardWidth - 10 + cardWidth / 2, JFRAME_AREA_HEIGHT / 2 + cardHeight / 2);
    
    if (! cardCanBeRemoved(cardToCheck, cardsOnTable, howManyInEachRow)) {
      return false;
    }
    
    Point cardCentre = cardToCheck.getCardCentrePt();  
    setUpCardMoveAmts(REMOVED_CARDS_CENTRE_POSITION, cardCentre);
    
    selectedCardIsMoving = true;
    howManyInEachRow[whichColumn]--;
    t.start();
    
    return true;
  }
  
  private void setUpCardMoveAmts(Point destinationCentre, Point cardCentre) {
    // Work out the shortest distance from the centre of the 
    // card to the destination.  Divide moves into 15 pixel
    // moves along the shortest route.
    int xDistance = destinationCentre.x - cardCentre.x;
    int yDistance = destinationCentre.y - cardCentre.y;
    int xYDistance = (int) (Math.pow(xDistance * xDistance + yDistance * yDistance, 0.5));
    
    double howMany15PixelMoves = xYDistance / 15.0;
    
    cardMoveX = (int) (xDistance / howMany15PixelMoves);
    cardMoveY = (int) (yDistance / howMany15PixelMoves);
  } 
  //--------------------------------------------------------------------- 
  //--------------------------------------------------------------------- 
  /*
   Stage 11 (8 Marks)
   */
  private boolean thereAreMoreAvailableMoves(ArrayList<Card> cardStack, Card[][] cardsOnTable, int[] howManyInEachRow) {  
    
    if(cardStack.size() == 0){
      gameHasEnded = true;
      
    }
    for(int i = 0; i < NUMBER_ROWS;i++){
      if(howManyInEachRow[i] == 0){
        return true;
      }
      else if(howManyInEachRow[i] > 1){
        Card cardToCheck = cardsOnTable[i][howManyInEachRow[i]-1];
        if(cardCanBeRemoved(cardToCheck, cardsOnTable, howManyInEachRow)){
          return true;
        }else{
          return false;
        }
        
      }
    }
    return false;
  }
  
  public void mouseClicked(MouseEvent e) {}
  public void mouseReleased(MouseEvent e) {}
  public void mouseEntered(MouseEvent e) {}
  public void mouseExited(MouseEvent e) {}
  //--------------------------------------------------------------------- 
  // Handle KeyEvents
  // Stage 5  (4 Marks), 
  //and 
  // part of Stage 12 (10 Marks)
  //--------------------------------------------------------------------- 
  /*
   Stages 5 and 12
   */
  public void keyPressed(KeyEvent e) {
    String fileName = "SavedGame.txt";
    if(e.getKeyCode() == KeyEvent.VK_N){ 
      //System.out.println("Reset Has been pressed.");
      reset();
      repaint();
    }
    if(e.getKeyCode() == KeyEvent.VK_S){
      //System.out.println("Save key has been pressed.");
      saveToFile(fileName);
    }
    if(e.getKeyCode() == KeyEvent.VK_L){
      try {
        loadFromFile(fileName);
        //System.out.println("LOAD KEY PRESSED");
        repaint();
      } catch (FileNotFoundException e1) {
        System.err.println("FILE NOT FOUND");
        e1.printStackTrace();
      }
    }
  }
  
  public void keyReleased(KeyEvent e) {}
  public void keyTyped(KeyEvent e) {}
  //-------------------------------------------------------------------
  //-------- Draw all the CARD objects --------------------------------
  //--------------------------------------------------------------------- 
  //--------------------------------------------------------------------- 
  //--------------------------------------------------------------------- 
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    drawTableCards(g); 
    drawRestOfJPanelDisplay(g);  
 
  } 
  /*
   Stage 4 (8 Marks)
   */ 
  private void drawTableCards(Graphics g) {
    
    for(int row = 0; row < NUMBER_ROWS; row++){
      for(int col = 0; col < howManyInEachRow[row];col++){
        Card toDraw = cardsOnTable[row][col];
        if(toDraw != null){
          toDraw.drawCard(g, this);
        }
        
      }
    }
  }
  
  
  
  private void drawRestOfJPanelDisplay(Graphics g) {  
    if (lastRemovedCard != null) {
      lastRemovedCard.drawCard(g, this);
    }
    if (selectedCardIsMoving) {
      currentlySelectedCard.drawCard(g, this);
    }
    
    drawGameInformation(g); 
    drawFaceDownCard(g);
  }
  
  private void drawFaceDownCard(Graphics g) {
    faceDownCard.drawCard(g, this);
    Rectangle cardArea = faceDownCard.getCardArea();
    
    int numberLeftInPack = cardStack.size();
    
    g.setFont(new Font("Times", Font.BOLD, 48));
    if (numberLeftInPack < 10) {
      g.drawString("" + numberLeftInPack, cardArea.x + cardArea.width / 3, cardArea.y + cardArea.height * 2 / 3);
    } else {
      g.drawString("" + numberLeftInPack, cardArea.x + cardArea.width / 6, cardArea.y + cardArea.height * 2 / 3);
    }
  }
  
  private void drawGameInformation(Graphics g) {
    g.setFont(new Font("Times", Font.BOLD, 26));
    g.setColor(Color.BLUE);
    String gameEndMessage = "";
    String statusMessage = "Removed: " + cardsRemoved;
    statusMessage = statusMessage + ", On table: " + numberOfCardsOnTable;
    
    if (gameHasEnded) {
      gameEndMessage = "GAME OVER! "; 
      if (numberOfCardsOnTable < 5) {
        gameEndMessage = gameEndMessage + "STUPENDOUS, You won! "; 
      } else if (numberOfCardsOnTable < 11) {
        gameEndMessage = gameEndMessage + "Excellent Result! "; 
      } else {
        gameEndMessage = gameEndMessage + "Bad luck! (n - new game)"; 
      }
      
    }
    g.drawString(statusMessage, SCORE_POSITION.x, SCORE_POSITION.y);
    g.drawString(gameEndMessage, SCORE_POSITION.x, SCORE_POSITION.y + 27);
    
  }
  //---------------------------------------------------------------------
  //-------- Write To File ----------------------------------------------
  //--------------------------------------------------------------------- 
  // Write the current game information to the file.
  // The name of the file is given by the String parameter.
  //--------------------------------------------------------------------- 
  //--------------------------------------------------------------------- 
  private void saveToFile(String fileName) {
    PrintWriter pW = null;
    Card card; 
    //Moved the code around for a better save file.
    
    try {
      pW = new PrintWriter(fileName);
      
      pW.println(selectedCardIsMoving);
      pW.println(cardStackIsEmpty);
      pW.println(gameHasEnded);
      
      pW.println(cardMoveX);
      pW.println(cardMoveY);
      pW.println(cardsRemoved);
      pW.println(numberOfCardsOnTable);
      pW.println(cardWidth);
      pW.println(cardHeight);
      pW.println(cardStack.size());
      
      pW.println(faceDownCard.getCardStatusInformation());
      
      for (int row = 0; row < cardsOnTable.length; row++) {
        pW.println(howManyInEachRow[row]);
      }
      //pW.println(cardsOnTable.length); not needed, useless info
      //pW.println(cardsOnTable[0].length); not needed, useless info
      for (int row = 0; row < cardsOnTable.length; row++) {
        for (int down = 0; down < howManyInEachRow[row]; down++) {
          pW.println(cardsOnTable[row][down].getCardStatusInformation());
        }
      }  
      for (int i = 0; i < cardStack.size(); i++) {
        card = cardStack.get(i);
        pW.println(card.getCardStatusInformation());
      }
      
      
      if (currentlySelectedCard == null) {
        pW.println("null");
      } else {
        pW.println(currentlySelectedCard.getCardStatusInformation());
      }   
      if (lastRemovedCard == null) {
        pW.println("null");
      } else {
        pW.println(lastRemovedCard.getCardStatusInformation());
      }
      
      //pW.println(howManyInEachRow.length); useless info
      
      
      pW.close();
    } catch(IOException e) {
      System.out.println("Error saving game to " + fileName);
    }
  }
  //---------------------------------------------------------------------
  //-------- Load From File ---------------------------------------------
  //--------------------------------------------------------------------- 
  // The createACard helper method has been defined and
  // is useful when creating a Card object from one line of 
  // information read from the file.
  //--------------------------------------------------------------------- 
  //--------------------------------------------------------------------- 
  
  public void loadFromFile(String fileName) throws FileNotFoundException {
    Scanner scan = null;
    Card card; 
    String cardInfo;
    int value, suitIndex, xPos, yPos, cardStackSize, width, height;
    
    boolean isSelected;
    boolean isFaceUp;
    ArrayList<Card> loadedDeck = new ArrayList<Card>();
    
    scan = new Scanner(new FileReader(fileName));
    
    try{ 
    //booleans- 
      //selectedCardIsMoving
      selectedCardIsMoving = scan.nextBoolean();
      //cardStackIsEmpty
      cardStackIsEmpty = scan.nextBoolean();
      //gameHasEnded
      gameHasEnded = scan.nextBoolean();
      
    //ints -
      //cardMoveX
      xPos = scan.nextInt();
      //cardMoveY
      yPos = scan.nextInt();
      //cardsRemoved
      cardsRemoved = scan.nextInt();
      //numberOfCardsOntable
      numberOfCardsOnTable = scan.nextInt();
      //cardWidth
      width = scan.nextInt();
      //cardHeight
      height = scan.nextInt();
      //Card STack Size
      cardStackSize = scan.nextInt();
    //CARDS
      //FaceDownCard
      value = scan.nextInt();
      suitIndex = scan.nextInt();
      xPos = scan.nextInt();
      yPos = scan.nextInt();
      isFaceUp = scan.nextBoolean();
      isSelected = scan.nextBoolean();
      cardInfo = value +" "+suitIndex+" "+xPos+" "+yPos+" "+isFaceUp+" "+isSelected;
      card = createACard(cardInfo, width, height);
      System.out.println("FACEDOWN CARD IS - "+card.toString());
    //HowManyInEachRow
      howManyInEachRow[0] = scan.nextInt();
      howManyInEachRow[1] = scan.nextInt();
      howManyInEachRow[2] = scan.nextInt();
      howManyInEachRow[3] = scan.nextInt();
      
    //CardsInPlay
      for(int row = 0; row < cardsOnTable.length; row++){
        for(int col = 0; col < howManyInEachRow[row]; col++){
          value = scan.nextInt();
          suitIndex = scan.nextInt();
          xPos = scan.nextInt();   
          yPos = scan.nextInt();    
          isFaceUp = scan.nextBoolean();    
          isSelected = scan.nextBoolean();    
          cardInfo = value +" "+suitIndex+" "+xPos+" "+yPos+" "+isFaceUp+" "+isSelected;
          card = createACard(cardInfo, width, height);
          System.out.println("Card ONTABLE - "+card.toString());
          
          if(howManyInEachRow[row] > 1){
            cardsOnTable[row][howManyInEachRow[row]-1] = card;
          }else{
            cardsOnTable[row][0] = card;
          }
          
        }
      }
    //creating the loaded stack from save file
      for(int i = 0; i < cardStackSize; i++){
        value = scan.nextInt();
        suitIndex = scan.nextInt();
        xPos = scan.nextInt();   
        yPos = scan.nextInt();    
        isFaceUp = scan.nextBoolean();    
        isSelected = scan.nextBoolean();    
        cardInfo = value +" "+suitIndex+" "+xPos+" "+yPos+" "+isFaceUp+" "+isSelected;
        card = createACard(cardInfo, width, height);
        loadedDeck.add(card);
        cardStack = loadedDeck;
      }
      
      if(scan.next().equals("null")){
        currentlySelectedCard = null;
      }else{
        value = scan.nextInt();
        suitIndex = scan.nextInt();
        xPos = scan.nextInt();   
        yPos = scan.nextInt();    
        isFaceUp = scan.nextBoolean();    
        isSelected = scan.nextBoolean();    
        cardInfo = value +" "+suitIndex+" "+xPos+" "+yPos+" "+isFaceUp+" "+isSelected;
        card = createACard(cardInfo, width, height);
        currentlySelectedCard = card;
      }
      
      if(scan.next().equals("null")){ //check them nulls hater
        lastRemovedCard = null;
      }else{
        value = scan.nextInt();
        suitIndex = scan.nextInt();
        xPos = scan.nextInt();
        yPos = scan.nextInt();
        isFaceUp = scan.nextBoolean();
        isSelected = scan.nextBoolean();    
        cardInfo = value +" "+suitIndex+" "+xPos+" "+yPos+" "+isFaceUp+" "+isSelected;
        card = createACard(cardInfo, width, height);
        lastRemovedCard = card;
      }
      scan.close();
      
    }catch(Exception e){
      
    }
  } 
  
  private Card createACard(String info, int width, int height) {
    Card card;
    int suit, value, x, y;
    boolean selected, faceUp;
    
    Scanner scanInfo = new Scanner(info);
    
    value = scanInfo.nextInt();
    suit = scanInfo.nextInt();
    x = scanInfo.nextInt();
    y = scanInfo.nextInt();
    
    
    faceUp = scanInfo.nextBoolean();
    selected = scanInfo.nextBoolean();
    
    card = new Card(value, suit);
    
    card.setCardArea(x, y, width, height);
    card.setIsFaceUp(faceUp);
    card.setIsSelected(selected);  
    scanInfo.close();
    return card;
  }
  //-------------------------------------------------------------------
  //------ Create an ArrayList of the full pack of CARD objects -------
  //-------------------------------------------------------------------
  private ArrayList<Card> createTheFullPack() {
    ArrayList<Card> theCards = new ArrayList <Card> (TOTAL_NUMBER_OF_CARDS);
    int suitNum = A1Constants.CLUBS;
    int cardValue = 0;
    
    for (int i = 0; i < TOTAL_NUMBER_OF_CARDS; i++) {
      theCards.add(new Card(cardValue, suitNum));
      
      if(cardValue >= CARDS_IN_EACH_SUIT - 1) {
        suitNum++;
      }
      
      cardValue = (cardValue + 1) % (CARDS_IN_EACH_SUIT);
    }
    
    return theCards;
  }
  
  private void printTableCards(Card[][] cards) {
    for (int row = 0; row < cardsOnTable.length; row++) {
      for (int col = 0; col < cardsOnTable[row].length; col++) {
        if (cardsOnTable[row][col] != null) {
          System.out.printf("%6s ",cardsOnTable[row][col].toString());
        }
      }
      System.out.println();
    }
  }
  //-------------------------------------------------------------------
  //-------- Load all the CARD images ---------------------------------
  //-- Set up the width and height instance variables  ----------------
  //-------------------------------------------------------------------
  private void loadAllCardImagesAndSetUpCardDimensions() {
    CardImageLoadUp.loadAndSetUpAllCardImages(this);
    
    Dimension d = CardImageLoadUp.getDimensionOfSingleCard();
    cardWidth = d.width;
    cardHeight = d.height; 
  }
}