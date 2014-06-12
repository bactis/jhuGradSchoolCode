import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;



//import HuffmanTree.HuffData;

//Author: ben actis
//email:  ben.actis@gmail.com
//date:   04/09/2013
//Notes:  this does the following
//		  1: get file from user
//		  2: read in char by char and create map
//        5: create priority queue/huffman tree
public class Main 
{

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		//variables
		//BufferedReader fileForReading = null;
		//BufferedReader fileFor
		String fileName = null;

		//get file that will generate the map from the user
		fileName = getFileName();
		
		//create 2 buffered readers, because I was getting a closed session error
		BufferedReader br1 = null;
		try {
			br1 = new BufferedReader(new FileReader(fileName));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader br2 = null;
		try {
			br2 = new BufferedReader(new FileReader(fileName));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		System.out.println("DEBUG: FILE NAME VALID");
		//BufferedReader fileForReading2 = fileForReading;
		
		//read in the chars from teh file and create a huffData array
		System.out.println("DEBUG: Beginning Freq Table Generation");
		HuffmanTree.HuffData[] temp = HuffmanTreeAdvanced.buildFreqTable(br1);
		System.out.println("DEBUG: Finshed building the huffData arrary");
		
		//build the tree
		System.out.println("DEBUG: Starting to build huffman tree");
		HuffmanTreeAdvanced testTree = new HuffmanTreeAdvanced();
		testTree.buildTree(temp);
		System.out.println("DEBUG: Finished build huffman tree");

		
		//encode a string
		System.out.println("DEBUG: Start the encoding process");
		String outs = null;
		//print out the coded string of 1's and 0s
		outs = testTree.encode(br2, outs);
		System.out.println("DEBUG: Finsh the encoding process");
		
		
		
		
		/*
		//temp holds the the chracters (keys) and their frequencies (values)
		System.out.println("DEBUG: Beginning Freq Table Generation");
		HuffData[] temp = buildFreqTable(fileForReading);
		System.out.println("DEBUG: Finished Freq Table Generation");
		
		System.out.println("DEBUG: ATTEMPING PRINT ");
		System.out.print(temp);
		*/
		
		//count# of times each letter shows up
		//recomendation
        // 0   would be a   count 
        // 1   would be b   count
	}
	//Notes: MIKE SAYS USE A MAP SEE CHAPSTER 7
	//		then iternate through map and make custom object
	//		dump into priority que see chap6.6
	/*
	public static void determineFrequency(Scanner scanner)
	{
		char current = 'a';
		while (scanner.hasNext())
		{
			
		}
	}
	*/
	
	//--------------------------------------------------------------------------------------------------------------------
	
	//--------------------------------------------------------------------------------------------------------------------
	//Method Name: buildFreqTable
	//Parameters:  scanner object for reading input of chars 
	//				note was changed from buffered reader to scanner from txtbook example
	//Returns:		a map of characters and frequencies
	//Notes:		Uses textbook code from chap 7 pg 404
	//				How this method works....
	//				1: Creates a new Map/HashMap called frequenceis that consists of Character and Integer objects
	//				2: continue until no more characters to read in
	//				3: increment the count of the charactesr that are read in
	//				4: add this info to the frequencies map
	//				5: return the huffdata hashMap like object
	//				
	//--------------------------------------------------------------------------------------------------------------------
	/*
	public static HuffData[] buildFreqTable(BufferedReader ins)
	{
		//create a map to hold the character frequencies
		Map<Character, Integer> frequencies = new HashMap<Character, Integer>();
		
		//used to store next character as an int
		int nextChar = 0;
		
		System.out.println("DEBUG: POINT 1");
		
		//while there is still input
		//while (scanner.hasNext() )
        try {
			while ((nextChar = ins.read()) != -1)  // Test for more data
			{
				System.out.println("DEBUG: POINT 2");

			    Character next = new Character((char) nextChar);
				Integer count = frequencies.get((char) nextChar);
				
				//first occurrence
				if (count == null)
				{
					System.out.println("DEBUG: POINT 3a");

					count = 1; 
				}
				
				//not first, increment
				else
				{
					System.out.println("DEBUG: POINT 3b");

					count++; 
				}
				
				//store the updated count
				frequencies.put((char) nextChar, count);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// end loop that continues until no more data to read in
		
		System.out.println("DEBUG: POINT 4");

		
		//copy map entries to a HuffData[ arrary
		HuffData[] freqTable = new HuffData [frequencies.size()];
		
		//start at the beginning of this super cool huffdata arrary
		int i = 0;
		
		//get each map entry and store in the arrary as a weight-symbol pair
		for (Map.Entry<Character, Integer> entry : frequencies.entrySet())
		{
			System.out.println("DEBUG: POINT 5");

			
			//create a new huffData object at postion i of freqTable
			freqTable[i] = new HuffData(entry.getValue().doubleValue(), entry.getKey());
			
			//increment i
			i++;
		}
		System.out.println("DEBUG: POINT 6");

		
		//return the arrary
		return freqTable;
		
	}
	*/
	//--------------------------------------------------------------------------------------------------------------------
	
	//--------------------------------------------------------------------------------------------------------------------
	//Method Name: getFile
	//Parameters:  None
	//Returns:     Scanner object with a file or null
	//Notes:       Simply gets a file from user
	public static String getFileName()
	{
		//variables
		Scanner i = new Scanner(System.in); //for getting file name/path
		//Scanner scanner = null; //for reading in lines/chars from file
		BufferedReader br = null;
		String fileForInput = null;
		
		//read from a file
		System.out.print("Enter the file for input: ");
		fileForInput = i.nextLine();
		
		
		/*
		try 
		{
			br = new BufferedReader(new FileReader(fileForInput));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/

		
		//File textFile = new File(fileForInput);
		
		
		return fileForInput;
	}//end getFile method
	//--------------------------------------------------------------------------------------------------------------------

}
