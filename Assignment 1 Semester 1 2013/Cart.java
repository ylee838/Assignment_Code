/**
 * COMPSCI 101 Cart class - storing info for the shopping cart
 * For Assignment 1
 */
//xx

import java.text.DecimalFormat;

public class Cart {
  private static final int MAX_SIZE = 500;   // a constant for maximum array size
  private SalesItem[] purchases;      // the array stores the items being purchased
  private int size;         // the total number of items in the array
  
  // the constructor of the Cart class
  public Cart (){
    purchases = new SalesItem[MAX_SIZE];
    size = 0;
  }
  
  // this method adds an item into the shopping cart
  public void addItem(SalesItem item){
    
    if(item.getQuantity() <= 0){
     System.out.println("Sorry, item " + item.getItemCode() + " is out of stock. Please select a different item."); 
    }
    else{
    size++;
    System.out.println(item.getDescription() + " is added to shopping cart");
    for(int i = 0; i < size; i++){ 
      purchases[i] = item;
     
    
    }
    }
  }    
  
  // this method removes an item from the shopping cart
  public SalesItem deleteItem(String itemCode){
    
    SalesItem[] purchasesNew;
    purchasesNew = new SalesItem[MAX_SIZE];
    
    int destX = 0;
    int destY = 0;
    
    for(int i = 0; i < size; i++){
      String item = (purchases[i].getItemCode());
      if(!(item.equals(itemCode))){
        purchasesNew[destY] = purchases[destX];      
        destY++;
        destX++;
      }
      if(item.equals(itemCode)){
       destX++; 
        System.out.println(purchases[i].getDescription() + " is removed from the shopping cart");
      }
    }
    
    purchases = purchasesNew;
 
    return null;
  }  
    
    // this method removes all the items in the shopping cart
    public void deleteAll(){
      System.out.println("All items are cleared from the shopping cart.");
      for(int i = 0; i < size; i++){
        purchases[i].setQuantity(purchases[i].getQuantity() + 1);
        purchases[i] = null; 
      }
      
    }
    
    // this method displays the items bought and calucates the final bill
    public void checkOut(){
     double sum = 0;
      System.out.println("Checking out items ...");
      DecimalFormat checkAmount = new DecimalFormat("#.##");
      for(int i = 0; i < size; i++){
       sum += purchases[i].getPrice();
        
        
      }
      System.out.println("Amount due: $" + checkAmount.format(sum));
    }
  }
  
