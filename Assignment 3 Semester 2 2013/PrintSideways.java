import java.util.Arrays;
import java.util.Scanner;

public class PrintSideways extends StringItem {
	
	public PrintSideways(String str) {
		super(str);
		
		
		StringItem root = new StringItem(str.substring(0,1)); //creating the elements for our BST
		BinarySearchTree<StringItem,String> completeTree = new BinarySearchTree<StringItem,String>(root); //creating our BST, setting the root to be the first letter
																	// of the preorder traversal
		for(int i = 1; i < str.length(); i++){
			StringItem subtree = new StringItem(str.substring(i,i+1));
			completeTree.insert(subtree); //Adding Char Elements into the BST
			
        }
		
		System.out.println("Printing Sideways by xx");
		print(completeTree.getRoot(), 0); //prints our tree sideways
		inOrder(str);
		removal(completeTree); //Takes input from user and removes specified elements
		
		
	}

	public void print(TreeNode<?> t ,int indentNum){
		
        if (t != null){ //making sure our node isn't equal to null
            String indentString = ""; // our indent
            for (int i = 0; i < indentNum; i++)
                indentString += "    "; //increases the size of the indent per level
            indentNum++; 
            print(t.getRight(), indentNum); // we have to print the rightmost first, as its sideways
            System.out.println(indentString + t.getItem()); // prints our element, with the item and indention
            print(t.getLeft(), indentNum);
        }

	}
	
	public void removal(BinarySearchTree<StringItem, String> b){
			Scanner sc = new Scanner(System.in); 
			System.out.println("Enter removal sequence (CASE SENSITIVE) : "); 
			String remove = sc.nextLine(); //gets our removal values
			sc.close(); //Closing resources, like a boss
	try{	
			for(int i = 0; i < remove.length(); i++){
				if(b.retrieve(remove.substring(i,i+1)) != null){
					b.delete(remove.substring(i,i+1));
				}
				
				 							//removes the element specified from the BST, uses method defined in the BST class
			}								//In BinarySearchTree.java, the deleteItem method is flawed,
											//remove the "throws error", comment it out.
			
			System.out.println("Printing Sideways by xx");
			print(b.getRoot(), 0); //prints our new tree
			System.out.print("Inorder Traversal : " ); 
			inorderTraversal(b.getRoot());
			
	}catch(Exception e){}
		
		
	}
	
	public void inOrder(String preorder){ //sorts our preorder into inorder, so we don't have to
		String[] inorder = new String[preorder.length()];//do it recursively.
		String combined = "";
		for(int i = 0; i < preorder.length(); i++){
			inorder[i] = preorder.substring(i,i+1);
		}
		Arrays.sort(inorder);
		for(int j = 0; j < inorder.length; j++){
			combined += inorder[j];
		}
		System.out.println("Inorder Traversal : "+combined);
	}
	
	public void inorderTraversal(TreeNode<?> t){
		if(t != null){
			inorderTraversal(t.getLeft());//L
			System.out.print(t.getItem());//Root
			inorderTraversal(t.getRight());//R
		}
	
		
	}
	public static void main(String[] args){
		PrintSideways s = new PrintSideways(args[0]);
			
	}
	

}	
