//Name:		Ben actis
//Date:		04/15/2013
//Notes:	code from textbook chap 7
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

//=======================================HuffmanTreeAdvanced Class =====================================
//----------------------------------------------------------------------------------
//ClassName:		HuffmanTreeAdvanced
//Notes:			from the koffman and wolfgang textbook code chap 7
public class HuffmanTreeAdvanced extends HuffmanTree implements Serializable
{
	//--------------------------------Attributes
    private Map<Character, String> codeMap;		// The map between Characters and their coding


    //----------------------------------------------------------------------------------------------------
    //MethodName:			buildFreqTalble
    //Parameter:			bufferedReader - for reading in chars
    //Notes:				from textbook code
    //						1: creates new hashmap to hold frequency character mappings
    //						2: read through the file while there is still more data
    //						3: increment frequences
    //						4: move hashmap of frequencies into a huffdata array
    //						5: return huffdata array
    public static HuffData[] buildFreqTable(BufferedReader ins) 
    {
    	// Map of frequencies.
        Map<Character, Integer> frequencies =  new HashMap<Character, Integer>(); 
        
        try {
        	// For storing the next character as an int
            int nextChar;  
            
            // Test for more data
            while ((nextChar = ins.read()) != -1)
            {  
                Character next = new Character((char) nextChar);
                
                // Get the current count and increment it.
                Integer count = frequencies.get(next);
                
                // First occurrence.
                if (count == null) 
                {
                    count = 1;   
                }
                //if not first occurrence
                else
                {
                    count++;
                }

                // Store updated count.
                frequencies.put(next, count);
            }
            ins.close();
        } 
        catch (IOException ex) 
        {
            ex.printStackTrace();
        }
        
        // Copy Map entries to a HuffData[] array.
        HuffData[] freqTable = new HuffData[frequencies.size()];
        
        // Start at beginning of array.
        int i = 0;     
        
        // Get each map entry and store it in the array 
        // as a weight-symbol pair.
        for (Map.Entry<Character, Integer> entry : frequencies.entrySet())
        {
            freqTable[i] = new HuffData(entry.getValue().doubleValue(),entry.getKey());
            i++;
        }
        // Return the array.
        return freqTable;    
    }
    //----------------------------------------------------------------------------------------------------

    //----------------------------------------------------------------------------------------------------
    //MethodName:		buildCodeTable
    //Notes:			from txtbook
    //					starter method to build the code table
    //						changed from bitString to newString
    public void buildCodeTable()
    {
    	String newString = null;
    	
        // Initialize the code map
        codeMap = new HashMap<Character, String>();
        
        // Call recursive method with empty bit string for code so far.
        buildCodeTable(huffTree, newString);
    }

    /**
     * Recursive method to perform breadth-first traversal
     * of the Huffman tree and build the code table.
     * @param tree The current tree root
     * @param code The code string so far
     */
    private void buildCodeTable(BinaryTree<HuffData> tree, String code) {
        // Get data at local root.
        HuffData datum = tree.getData();
        if (datum.getSymbol() != null) {  // Test for leaf node.
            // Found a symbol, insert its code in the map.
            codeMap.put(datum.getSymbol(), code);
        } else {
            // Append 0 to code so far and traverse left. 
            String leftCode = code + "0";
            buildCodeTable(tree.getLeftSubtree(), leftCode);
            // Append 1 to code so far and traverse right.
            String rightCode = code + "1";
            buildCodeTable(tree.getRightSubtree(), rightCode);
        }
    }

    /**
     * Encodes a data file by writing it in compressed bit string form.
     * @param ins The input stream
     * @param outs The output stream
     */
    public String encode(BufferedReader ins, String outs)
    {
    	System.out.println("DEBUG: started encode method");
    	
    	ins.
    	
        String result = null;  // The complete bit string.
        try 
        {
            int nextChar;
            String nextChunk = null;
            while ((nextChar = ins.read()) != -1) 
            {  
            	System.out.println("DEBUG: STARTING TO ENCODE");
            	
            	// More data?
                Character next = (char) nextChar;

                // Get bit string corresponding to symbol nextChar.
                nextChunk = codeMap.get(next);
                result = result + nextChunk;   // Append to result string.
            }
           // result.trimCapacity();

            // Write result to output file and close files.
           // outs.writeObject(result);
            outs = result;
           // ins.close();
            //outs.close();
            
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            System.exit(1);
        }
        
        return outs;
    }

// Insert solution to programming project 1, chapter -1 here
}
/*</listing>*/
