/**
 * A simple program of a grocery store, which assists
 * the purchases, calculate total price and display bill.
 **/

public class GroceryStore {
  
  // this method manages the entire shopping process
  public void start() {
    final String UPI = "xx";   // a constant for student UPI
    
    
    Stock stock = new Stock();
    stock.loadItems("stock.txt");
    Cart cart = new Cart();
    
    System.out.println("==============================================================");
    System.out.println("------This is a simple grocery store program by " + UPI + ".------");
    boolean displayTopMenu = true;
    boolean displaySubMenu = true;
    int choice = 0;
    
    
    while(displayTopMenu){
      topMenu();
      choice = getChoice(1,3);
      if(choice == 1){
        listOfItemsHeading();
        stock.displayItems();
      }
      
      if(choice == 2){
        while(displaySubMenu){
          subMenu();
          choice = getChoice(1,4);
          
          if(choice == 1){
            System.out.println("Enter item code to buy :");
            int itemWanted = getChoice(1001, 1018);
            String wanted = Integer.toString(itemWanted);
            SalesItem found = stock.findItem(wanted);
            cart.addItem(found);
            
          }
          
          if(choice == 2){
            System.out.println("Enter item code to delete: ");
            int itemToDelete = getChoice(1001, 1018);
            String toDelete = Integer.toString(itemToDelete);
            cart.deleteItem(toDelete);
            
          }
          if(choice == 3){
            cart.checkOut();
            displaySubMenu = false;
            
            
            
          }
          
          if(choice == 4){
            cart.deleteAll();
            displaySubMenu = false;
          }
        }
        
        
      }
      
      if(choice == 3){
        displayTopMenu = false;
        finishMenu();
      }
      
      
      
      
    }
    
    
    
    stock.saveItems("stock2.txt");
    
    
  }
// this method displays the top-level menu
  private void topMenu() {
    System.out.println("--------------------------------------------------------------");
    System.out.println("1. Show the list of items on sale.");
    System.out.println("2. Start to shop online.");
    System.out.println("3. Exit the system.");
    System.out.println("--------------------------------------------------------------");
    // to be implemented.
  }
  
// this method displays the second-level menu
  private void subMenu() {
    System.out.println("--------------------------------------------------------------");
    System.out.println("1. Add an item to the shopping cart.");
    System.out.println("2. Delete an item from the shopping cart.");
    System.out.println("3. Check out the shopping cart.");
    System.out.println("4. Exit without buying.");
    System.out.println("--------------------------------------------------------------");
    
  }
  //makes the start() look cleaner
  private void listOfItemsHeading(){
    System.out.println("-----------------List of the items on sale--------------------");
    System.out.println("--------------------------------------------------------------"); 
    
  }  
  
  
  //makes the start() look cleaner
  private void finishMenu(){
    System.out.println("--------------------------------------------------------------");
    System.out.println("---------------Thank you for shopping with us!----------------");
    System.out.println("==============================================================");  
    
  }
// this method gets the user's iunput choice
  private int getChoice(int lower, int upper) {
    int choice = 0;
    int i = 0;
    String checker = "";
    while(i == 0){
      String inputUserChoice = Keyboard.readInput();  
      checker = inputUserChoice;
      choice = Integer.parseInt(inputUserChoice);
      //checking the users input incase its invalid
      if(choice >= lower && choice <= upper && inputUserChoice.length() != 0){
        return choice;  
      }
      else{
        System.out.println("Invalid option, Please try again."); 
      }
    }
    return choice;
  }
}



