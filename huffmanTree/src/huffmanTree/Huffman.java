package huffmanTree;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.io.File;
import java.io.PrintWriter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.io.FileNotFoundException;
/**
 * 
 * @author muzhouchen
 * this class includes huffmanNode structure, comparator and main function;
 */
//this class define 4 elements in huffmanNode, int data shows the integer in node which represent the frequency etc.
//the left, right just like we did in BST. the char c represents the letters from message. 
class HuffmanNode {
	int data;
	public char c;
	public HuffmanNode left;
	public HuffmanNode right;
}
//this is the comparator which return the value of difference between two data in nodes;
class MyComparator implements Comparator<HuffmanNode> { 
    public int compare(HuffmanNode x, HuffmanNode y) 
    { 
        return x.data - y.data; 
    } 
} 
//the huffman class contains main functions and format we required step by step: build a huffman tree based on
// the input message from input file; print original message, frequency table, code table, encode message and write the
//output file;
public class Huffman{
	//to find the frequency of the letters in a string, we use the hashmap;
	public static Map<Character, String> encodeMap = new HashMap<>();
	//the printCode method in order to print out the encode message, use string as the variable so that we can add 0 + 0 = 00
	public static void printCode(HuffmanNode root, String s) {	
		//the base case when left and right equals null, print the character and the encode;
		if(root.left ==null && root.right == null) {
			encodeMap.put(root.c, s);
			System.out.println(root.c + "      " + s);
			return;
		}
		//add 0 when left and add 1 when right;
		printCode(root.left, s + "0");
		printCode(root.right, s + "1");
	}
	//the main function;
	public static void main(String[]args) {
		//field
		Map<Character, Integer> map = new HashMap<>(); 
		char[] charArray = new char[1];
		File inputFile = new File("input.txt");
		String s = "";
		//input the file and store the message into string;
		try{
			s = new Scanner(inputFile).useDelimiter("\\A").next();	
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		//format: print out the original message
		System.out.println("original message");
		System.out.println(s);
		System.out.println();
		//put all characters in string into a hashmap;
		charArray = s.toCharArray();
		for(int i = 0; i < charArray.length; i++){
			if(map.containsKey(charArray[i])) {
				map.put(charArray[i], map.get(charArray[i])+1);
			}
			else {
				map.put(charArray[i], 1);
			}
		}
		//format print the frequency table by using the hashmap;key is the character and get(key) is the encode number;
		System.out.println("frequency table");
		for(Character key : map.keySet()){
			System.out.println(key+  "      "+ map.get(key) );
		}
		System.out.println();
		int n = map.size();
        PriorityQueue<HuffmanNode> q = new PriorityQueue<HuffmanNode>(n, new MyComparator()); 
        //in this for loop, we put all character from hashmap to the huffmanTree;
		for(Character key : map.keySet()){
			HuffmanNode hn = new HuffmanNode(); 
			hn.c = key;
			hn.data = map.get(key);
			hn.left = null;
			hn.right = null;
			q.add(hn);
		}
		//use priorityqueue to set the tree; add each 2 smallest numbers together by using .peek and .poll;
        HuffmanNode root = null; 
        while (q.size() > 1) { 
            HuffmanNode x = q.peek(); 
            q.poll(); 
            HuffmanNode y = q.peek(); 
            q.poll(); 
            HuffmanNode f = new HuffmanNode(); 
            f.data = x.data + y.data; 
            f.c = '-'; 
            f.left = x; 
            f.right = y; 
            root = f; 
            q.add(f); 
        } 
        //format to print out the code table;
		System.out.println("code table");
        printCode(root, ""); 
        //formate to print the encode message from charArray;
        System.out.println();
        System.out.println("encode message");
        String encodeMessage = "";
        for(int i = 0; i < charArray.length;i ++) {
        	System.out.print(encodeMap.get(charArray[i]));
        	encodeMessage = encodeMessage + encodeMap.get(charArray[i]);
        }
        //to print the decode message to the output file. 
        File outFile = new File("output.txt");
        try {
            PrintWriter output = new PrintWriter(outFile);
            output.println(decode(encodeMessage, root));
            output.close();
        }catch(FileNotFoundException e) {
			System.out.println(e.getMessage());
        }
	}
	/**
	 * 
	 * @param message
	 * @param root
	 * 
	 * 
	 */
	//the method to decode;
	public static String decode(String message, HuffmanNode root) {
		//put the encode message to charArray;
		char[] charArray = message.toCharArray();
		//initialize result as string;
		String result = "";
		//use a temp to run the tree start at root;
		HuffmanNode temp = root;
		//use index to get the numbers from encode message; message[index]
		int index = 0;
		while(index < charArray.length) {
			//when has left or right, go left / right when charArray has 0 / 1;
			if(temp.left != null && temp.right != null) {
				if(charArray[index] == '0') {
					temp = temp.left;
					index += 1;
				}
				else {
					temp = temp.right;
					index += 1;
				}
							
			}
			//if the temp is leaf, return the character in this temp and return the temp to root;
			if(temp.left == null && temp.right == null) {
				result = result + temp.c;
				temp = root;
			}
		}
		return result;
	}
	
}