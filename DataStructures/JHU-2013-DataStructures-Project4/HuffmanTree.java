//Name:		ben actis
//date:		04/15/2013
//Notes:	HuffmanTree code from textbook chap 6
//			CompareHuffmanTree Class
//			HuffManTree Class
//				binaryTree attribute
//				Methods
//					buildTree
//					decode
//					printCode (2 params)
//					printCode (1 param)
//			HuffData Class
//				weight and symbol attributes
//				constructor
//				getter for symbol

import java.io.PrintStream;
import java.io.Serializable;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.List;
import java.util.ArrayList;

//=================================HuffManTree Class==========================================
//ClassName:	HuffmanTree
//Notes			from textbook code
public class HuffmanTree implements Serializable {

	//============================ HuffData Class  ==========================================
	//--------------------------------------------------------------------------------------
    // Nested Classes
    /** A datum in the Huffman tree. */
	//ClassName:	huffData
	//Notes			consists of a weight and char symbol
	//				this class is from textbook code
    public static class HuffData implements Serializable
    {
        // ---------------------------Data Fields
    	private double weight;		//The weight or probability assigned to this HuffData.
        private Character symbol;	//the alphabet symbol if this is a leaf.

        //---------------------------------------------------------------------
        //Constructor:	HuffData
        //Parameters:	weight - the weight aka frequency of that char
        //				symbol - (a-z)
        public HuffData(double weight, Character symbol)
        {
            this.weight = weight;
            this.symbol = symbol;
        }
        //----------------------------------------------------------------------

        //------------------------------
        //Method:	GetSymbol
        //Return:	symbol
        //Notes:	getter for symbol
        public Character getSymbol() 
        {
        	return symbol;
        }
        //------------------------------
    	//==============================End HuffData Class=========================================

    }
    //--------------------------- Data Fields for HuffManTree ----------------------------
    protected BinaryTree<HuffData> huffTree;		// A reference to the completed Huffman tree.

    //============================CompareHuffmanTree Class ===========================================
    //-----------------------------------------------------------------------------------------------
    //Name:			compareHuffmanTrees
    //Notes:		A Comparator for Huffman trees; nested class. */
    private static class CompareHuffmanTrees implements Comparator<BinaryTree<HuffData>>
    {

        //-----------------------------------------------------------------------------------------------
    	//MethodName:		Overload Compare
    	//Parameters:		treeLeft- the left-hand object
    	//					treeRight- the right hand objet
    	//Returns:			-1 if left less than right
    	//					0 if left equals right
    	//					1 if left greater than right
    	//Notes:			again from textbook code
        @Override
        public int compare(BinaryTree<HuffData> treeLeft, BinaryTree<HuffData> treeRight)
        {
            double wLeft = treeLeft.getData().weight;
            double wRight = treeRight.getData().weight;
            return Double.compare(wLeft, wRight);
        }
        //-----------------------------------------------------------------------------------------------
    }
    //-----------------------------------------------------------------------------------------------
    //============================End CompareHuffmanTree Class ==============================================================
    //-------------------------------------------------------------------------------------------------------------------------
    //MethodName:		buildTree
    //Parameter:		symbols - an arrary of HuffData objects
    //Notes				from book code again
    //					1: creates a priority queue object
    //					2: load the queue with the leaves
    //					3: build the tree
    //					hufftree - contains refrence to the huffmantree
    public void buildTree(HuffData[] symbols) 
    {
        Queue<BinaryTree<HuffData>> theQueue = new PriorityQueue<BinaryTree<HuffData>>(symbols.length, new CompareHuffmanTrees());
        
        // Load the queue with the leaves.
        for (HuffData nextSymbol : symbols)
        {
            BinaryTree<HuffData> aBinaryTree = new BinaryTree<HuffData>(nextSymbol, null, null);
            theQueue.offer(aBinaryTree);
        }

        // Build the tree.
        while (theQueue.size() > 1)
        {
            BinaryTree<HuffData> left = theQueue.poll();
            BinaryTree<HuffData> right = theQueue.poll();
            double wl = left.getData().weight;
            double wr = right.getData().weight;
            HuffData sum = new HuffData(wl + wr, null);
            BinaryTree<HuffData> newTree = new BinaryTree<HuffData>(sum, left, right);
            theQueue.offer(newTree);
        }

        // The queue should now contain only one item.
        huffTree = theQueue.poll();
    }
    //---------------------------------------------------------------------------------------------------------------------------
    
    //----------------------------------------------------------------------------
    //MethodName:		printCode
    //Parameters		out -  a printStream to write the output to
    //					code - the code up to this node
    //					tree - the current node in the tree
    //Notes				from book code
    //					outputs the resulting code
    private void printCode(PrintStream out, String code, BinaryTree<HuffData> tree)
    {
        HuffData theData = tree.getData();
        if (theData.symbol != null) {
            if (theData.symbol.equals(' ')) {
                out.println("space: " + code);
            } else {
                out.println(theData.symbol + ": " + code);
            }
        } else {
            printCode(out, code + "0", tree.getLeftSubtree());
            printCode(out, code + "1", tree.getRightSubtree());
        }
    }
    //---------------------------------------------------------------------------

    //---------------------------------------------------------------------------
    //MethodName:		printCode
    //Parameter:		out - a printStream to write the output to
    //Notes:			from textbook code
    public void printCode(PrintStream out) {
        printCode(out, "", huffTree);
    }
    //---------------------------------------------------------------------------

    //---------------------------------------------------------------------------
    //methodName:		decode
    //Notes:			from txtbook code
    //					Method to decode a message that is input as a string of
    //					digit characters '0' and '1'.
    //Parameters:		codedMessage - the input message as a String of zeros and ones
    //Return			the decoded message as a string
    public String decode(String codedMessage)
    {
        StringBuilder result = new StringBuilder();
        BinaryTree<HuffData> currentTree = huffTree;
        
        for (int i = 0; i < codedMessage.length(); i++) 
        {
            if (codedMessage.charAt(i) == '1') 
            {
                currentTree = currentTree.getRightSubtree();
            }
            else 
            {
                currentTree = currentTree.getLeftSubtree();
            }
            if (currentTree.isLeaf())
            {
                HuffData theData = currentTree.getData();
                result.append(theData.symbol);
                currentTree = huffTree;
            }
        }
        return result.toString();
    }
    //---------------------------------------------------------------------------

// Insert solution to programming exercise 1, section 6, chapter 6 here
}
//=================================End HuffManTree Class==========================================
