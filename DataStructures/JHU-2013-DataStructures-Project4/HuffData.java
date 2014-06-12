import java.io.Serializable;

//============================ HuffData Class  ==========================================
	//--------------------------------------------------------------------------------------
    // Nested Classes
    /** A datum in the Huffman tree. */
	//ClassName:	huffData
	//Notes			consists of a weight and char symbol
	//				this class is from textbook code
    public class HuffData implements Serializable
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
    }
    	//==============================End HuffData Class=========================================