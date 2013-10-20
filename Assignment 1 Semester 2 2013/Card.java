/*
 COMMENT
 */

import java.awt.*; 
import javax.swing.*;

public class Card {  
  private int suit;
  private int value;
  private Rectangle cardArea;
  private boolean isFaceUp;
  private boolean isSelected;
  
  public Card(int value, int suit) {  
    cardArea = new Rectangle(0,0,0,0);
    isFaceUp = false;
    isSelected = false;
    this.suit = suit;
    this.value = value;
  }  

//-------------------------------------------------------------------
//-------- Accessor and mutator methods -----------------------------
//-------------------------------------------------------------------
  public int getValue() {
		return value;
	} 
  
  public void setIsFaceUp(boolean faceUp) {  
    isFaceUp = faceUp;
  }
  public boolean getIsFaceUp() { 
    return isFaceUp;
    
  }
  public boolean getIsSelected() {  
    return isSelected;
    
  }
  public void setIsSelected(boolean selected) {  
    isSelected = selected;
  } 
  public void setCardArea(int x, int y, int w, int h) {  
    cardArea.x = x;
    cardArea.y = y;
    cardArea.width = w;
    cardArea.height = h;
    
  }
//-------------------------------------------------------------------
//-------- Short methods for getting the centre point  --------------
//-------- of the Card object, changing the position of -------------
//-------- the Card object and for comparing two Card objects: ------
//-------- (comparing suit and for comparing value). ----------------
//------------------------------------------------------------------- 
  public Rectangle getCardArea() {  
    return cardArea;
  }
  
  public void translate(int x, int y) {  
    cardArea.x += x;
    cardArea.y += y;
  }
  
  public int getSuit(){
	  return suit;
  }
  
  public boolean isSameSuit(Card other) {  
    if(suit == other.suit){
      return true;
    }
    else{
      return false;
    }
  }
  public boolean hasSmallerValue(Card other) {  
	  if(value == 0){
		  return false;
	  }
	  if(value <= other.value){
		  return true;
	  }else{
		  return false;
	  }
   
  }
  public Point getCardCentrePt() {  
    int x = cardArea.x + cardArea.width / 2;
    int y = cardArea.y + cardArea.height / 2;
    
    return new Point(x,y);
  }
//-------------------------------------------------------------------
//-------- Returns true if the parameter Point object, --------------
//-------- pressPt, is inside the Card area. ------------------------
//-------------------------------------------------------------------
  public boolean isInsideCardArea(Point pressPt) {
    if(cardArea.contains(pressPt)){
      return true;
    }else{
      return false;    
    }
  }
//-------------------------------------------------------------------
//-------- Get String containing the card state ---------------------
//-------------------------------------------------------------------
  public String getCardStatusInformation() { 
    
    return (value + " " + suit + " " + cardArea.x + " " + cardArea.y + " " + isFaceUp + " " + isSelected);
  }
//-------------------------------------------------------------------
//-------- Draw the Card object. ------------------------------------
//-------------------------------------------------------------------
  public void drawCard(Graphics g, JComponent theJPanelInstance) {  
    Image cardPic;
    int fileIndex;
  
    if (isFaceUp) {
      fileIndex = suit * A1Constants.CARDS_IN_EACH_SUIT + value;
      cardPic = CardImageLoadUp.getSingleCardImage(fileIndex);
    } else {
      cardPic = CardImageLoadUp.getFaceDownCardImage();
    }
    if (isSelected) {
      g.setColor(Color.WHITE);
    } else {
      g.setColor(Color.BLUE);
    }
    
    g.fillRect(cardArea.x - 2, cardArea.y - 2, cardArea.width + 4, cardArea.height + 4);
    
    g.drawImage(cardPic, cardArea.x, cardArea.y, theJPanelInstance);
  }  
//-------------------------------------------------------------------
//-------- Returns a String describing the card suit and value. -----
//-------- Useful for debugging purposes ----------------------------
//-------------------------------------------------------------------
  public String toString() { 
    final String[] SUITS = {"CLUBS", "DIAMONDS", "HEARTS", "SPADES"};
    if (value == 0) {
      return "A" + " " + SUITS[suit];
    } else if (value == 12) {
      return "K" + " " + SUITS[suit];
    } else if (value == 11) {
      return "Q" + " " + SUITS[suit];
    } else if (value == 10) {
      return "J" + " " + SUITS[suit];
    }
    
    return (value + 1)  + " " + SUITS[suit];
  }


} 

