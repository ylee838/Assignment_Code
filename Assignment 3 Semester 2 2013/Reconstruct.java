public class Reconstruct {


	public static void main(String[] args){
		Reconstruct s = new Reconstruct(args[0], args[1]);
		
	}
	
	public Reconstruct(String input1, String input2){
		BinaryTree<String> b = new BinaryTree<String>(buildATree(input1,input2));
		System.out.println("Printing tree reconstructed from inorder and post order By xx");
		print(b.getRoot(), 0);
		System.out.print("Preorder Traversal : ");
		preorderTraversal(b.getRoot());
	}

	public TreeNode<String> buildATree(String in, String post) {
			int lengthPst = post.length();
			int lengthIn = in.length();
			char[] inorder = new char[lengthIn];
			char[] postorder = new char[lengthPst];
			
		if(lengthPst != lengthIn){
			System.out.println("Input Mismatch, the 2 traversals are not the same!");
			return null;
		}else{
			for(int i = 0; i<lengthPst; i++){
				inorder[i] = in.charAt(i);
				postorder[i] = post.charAt(i);
			}
			
			return build(inorder, 0, inorder.length-1, postorder, 0, postorder.length-1);
		}
	}


	private TreeNode<String> build(char[] inorder, int startIN, int endIN, char[] postorder,
			int startPOST, int endPOST) {
		
		if(startIN > endIN || startPOST > endPOST){
			return null;
		}
		
		TreeNode<String> parent = new TreeNode(postorder[endPOST]);
	
		int location = find(inorder, startIN, endIN, postorder[endPOST]);
		int remaining = endIN - location;
		
		TreeNode<String> left= build(inorder, startIN, location-1, postorder, startPOST, endPOST-remaining-1);		
		TreeNode<String> right= build(inorder, location+1, endIN, postorder, endPOST-remaining, endPOST-1);

		parent.setLeft(left);
		parent.setRight(right);
		
		return parent;
	}

	private int find(char[] in, int start, int end, char target) { 
		
		for(int i=start; i<=end; i++){
			if(in[i]==target){
				return i; 
			}
		}
		return 0; 
		
	}

	public void print(TreeNode<?> t ,int indentNum){
		
        if (t != null){ //making sure our node isn't equal to null
            String indentString = ""; // our indent
            for (int i = 0; i < indentNum; i++)
                indentString += "    "; //increases the size of the indent per level
            indentNum++; //increment indentNumber
            print(t.getRight(), indentNum); // we have to print the rightmost first, as its sideways
            System.out.println(indentString + t.getItem()); // prints our element, with the item and indention
            print(t.getLeft(), indentNum);// gets our leftside and puts them to print
        }

	}
	
	
	public void preorderTraversal(TreeNode b){
		if(b != null){
			System.out.print(b.getItem());
			preorderTraversal(b.getLeft());
			preorderTraversal(b.getRight());
		}
	}
}
