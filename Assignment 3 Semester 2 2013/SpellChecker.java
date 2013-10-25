import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


public class SpellChecker {
	public static void main(String[] args){
		try {
		
				SpellChecker s = new SpellChecker(args[0], args[1]);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	BinarySearchTree<StringItem, String> b = new BinarySearchTree<StringItem, String>();
	int correct = 0;
	ArrayList<String> correctWords = new ArrayList<String>();
	ArrayList<String> incorrectWords = new ArrayList<String>();
	
	public SpellChecker(String locationOfDictionary, String inputToCheck) throws IOException{
		
		BufferedReader br = new BufferedReader(new FileReader(locationOfDictionary));
		File fileToCheck = new File(inputToCheck);
		
		ArrayList<String> parseTheFile = new ArrayList<String>();
		ArrayList<String> parseTheInput = new ArrayList<String>();
		
		int i = 0;
		String word = "";
		
		try{
			Scanner sc = new Scanner(fileToCheck);
			while(sc.hasNextLine()){
				String line = sc.nextLine();
				
				Scanner lineScanner = new Scanner(line);
				lineScanner.useDelimiter(" ");
				while(lineScanner.hasNext()){
					String part = lineScanner.next();
					parseTheInput.add(part);
					
				}
				lineScanner.close();
				
			}
			sc.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		

		while((word  = br.readLine()) != null){
			parseTheFile.add(word);
			
		}
	
		br.close();
	
		
		StringItem[] words = new StringItem[parseTheFile.size()];
		String[] input = new String[parseTheInput.size()];
		
		for(int j = 0; j < parseTheFile.size(); j++){
			words[j] = new StringItem(parseTheFile.get(j));
			
		}
		
		for(int k = 0; k < parseTheInput.size(); k++){
			input[k] = parseTheInput.get(k);
			input[k] = input[k].toLowerCase();
			String strip = input[k];
			strip = strip.replaceAll("[;()?!\"\\s]|[.](?!\\d)|[,](?!\\d)","");
			input[k] = strip;
			i++;
		}
		System.out.println("A simple spell checker by xx");
		System.out.println("Loading dictionary ... done");
		insertNodes(words, 0, words.length-1);
		System.out.println("The Height of the Binary Search Tree is "+findHeight(b.getRoot()));
		System.out.println("Spell check ... done");
		checkTheInput(input, i);
		
	}
	
	
	
	private void checkTheInput(String[] input, int numberOfWords) {
		for(int i = 0; i < input.length; i++){
			
			if(input[i].matches(".*\\d.*")){
				correct++;
			}else{
				if(b.retrieve(input[i]) != null){
					correct++;
					correctWords.add(input[i]);
				}else{
					incorrectWords.add(input[i]);
				}
			}
		
		}
		
		System.out.println("Possibly misspelled words in the file : ");
		for(int j = 0; j < incorrectWords.size(); j++){
			System.out.println(incorrectWords.get(j));
		}
		System.out.println("Total words checked : "+input.length);
		System.out.println("Total number of possibly misspelled words : "+incorrectWords.size());	
		
	}



	public TreeNode<StringItem> insertNodes(StringItem[] text, int left, int right){
		
		if(right < left){
			
			return null;
		}

		int mid = (left + right)/2;
		TreeNode<StringItem> n = new TreeNode<StringItem>(text[mid]);
		b.insert(n.getItem());
		
		if(right == left){
			return new TreeNode<StringItem>(text[left]);
		}		
		
		insertNodes(text, left, mid-1);
		insertNodes(text, mid+1, right);
	
		return n;
		
	}
		

	
	public int findHeight(TreeNode<?> tNode){
	
	    if(tNode == null)
	        return -1;

	    int heightLEFT = findHeight(tNode.getLeft());
	    int heightRIGHT = findHeight(tNode.getRight());

	    if(heightLEFT > heightRIGHT)
	        return heightLEFT + 1;
	    else
	        return heightRIGHT +1;
	}
	
}
