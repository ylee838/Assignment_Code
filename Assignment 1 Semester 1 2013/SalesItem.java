/**
  *	COMPSCI 101 SalesItem class - storing info for an item on sale
  * For Assignment 1
*/

import java.text.DecimalFormat;

public class SalesItem {
	private String code;		// the item code
	private String description; // the item description
	private double price;		// the item unit price
	private int quantity;		// the number of item available
			
	// the constructor of the SalesItem class
	public SalesItem (String code, String description, double price, int quantity){
		this.code = code;
		this.description = description;
		this.price = price;
		this.quantity = quantity;
	}
	
	// accessor and mutator methods
	public String getItemCode(){
		return code;
	}
	
	public void setItemCode(String code){
		this.code = code;
	}
	
	public String getDescription(){
		return description;
	}
	
	public void setDescription(String description){
		this.description = description;
	}
	
	public double getPrice(){
		return price;
	}
	
	public void setPrice(double price){
		this.price = price;
	}
	
	public int getQuantity(){
		return quantity;
	}
	
	public void setQuantity(int quantity){
		this.quantity = quantity;
	}
	
	// the string representation of a SalesItem object
	public String toString(){
		String item = "";
		item = code + "," + description + "," + new DecimalFormat("0.00").format(price) + "," + quantity;
		return item;
	}
}