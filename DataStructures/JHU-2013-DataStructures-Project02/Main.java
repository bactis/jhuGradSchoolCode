import java.io.IOException;
import java.util.Scanner;

/**
 * 
 */

/**
 * @author Ben Actis
 * @version 1.0
 * date: 2/21/2012
 *
 */
public class Main 
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		menu();
	}
	
	/**
	 * getUserString
	 * @return the stirng to check for language parsing/pattern matching / fun cs buzz word
	 * 	get the string from user input
	 */
	public static String getUserString()
	{
		//fun variables
		Scanner input = new Scanner(System.in);
		String theUserString = null;

		//prompt user to enter the current date
		System.out.print("\n Enter the string please: ");
		theUserString = input.nextLine();
		theUserString = theUserString.toUpperCase();
		
		return theUserString;
	}
	
	//-----------------------------------------------------------------------------------
	
	//-----------------------------------------------------------------------------------
	// Method Name; languageCheckL1
	// Parameters:  userstring - the string the user supplied
	// Notes:       
	public static boolean languageCheckL1(String userString)
	{
		//variables
		CoolerStack  aStack = new CoolerStack(); //empty stack
		char current;
		
		//for blank string
		if (userString.length() <= 1)
			return false;
		
		//continue until end of string
		for (int x = 0; x <= (userString.length() -1) ; x++)
		{
			//get current character
			current = userString.charAt(x);
			
			//-------------------------- debug
			//Print out Status of the stack
			
			/*
			//System.out.println("--------------------------------------------------");
			System.out.print("Current Char: " + current + "  " );
			if (aStack.peek() != null)
				System.out.println("  Stack: "+ aStack.peek().getValue());
			else
				System.out.println("Stack: Empty");
			System.out.println("--------------------------------------------------");
			*/
			
			
			//if stack is empty put value onto the stack
			if (aStack.peek() == null)
			{
				//System.out.println("DEBUG: empty stack, pushing "+ current + "onto the stack");
				aStack.push(current);
			}
			
			//if stack is not empty 
			else if (aStack.peek() != null)
			{
				//if current is the same as a the value on top of the stack put current on stack
				if (current == aStack.peek().getValue())
				{
					//System.out.println("Debug: pushing "+ current + "on stack"); // debug
					aStack.push(current);
				}
				
				//different values cancel out
				else if (current != aStack.peek().getValue() )
				{
					//System.out.println("Debug: popping top value off stack"); // debug
					aStack.pop();
				}
				
				//user is an idiot and put in a weird string
				else if (current != 'A' || current != 'B')
				{
					//System.out.println("String does not meet requirements for L1");
					return false;
				}
			}// end  if for a non empty stack
			
		} // end loop

		//if there is anything on the stack if so return false
		if (aStack.peek() != null)
		{
			//System.out.println("DEBUG stack is not empty returning false");
			return false;
		} //end if for condition not met
		
		//everything thing is happy and worked well stack is empty
		else if (aStack.peek() == null)
		{
			//System.out.println("Debug: stack is empty returning true");
			return true;
		} // end else for condition met
		
		else
			return false;
	}// end method
	//-----------------------------------------------------------------------------------
	
	//-----------------------------------------------------------------------------------
	// Method Name; languageCheckL2
	// Parameters:  userstring - the string the user supplied
	// Notes:  
	public static boolean languageCheckL2(String userString)
	{
		//variables
		CoolerStack  aStack = new CoolerStack(); //empty stack
		char current;
		boolean noMoreA = false; //for keeping track of a's and b's
		
		//for blank string
		if (userString.length() <= 1)
			return false;
		
		//continue until end of string
		for (int x = 0; x <= (userString.length() -1) ; x++)
		{
			//get current character
			current = userString.charAt(x);
			
			//if current is A and no more a is false
			if (current == 'A' && noMoreA == false)
			{
				//System.out.println("Debug: pushing "+ current + "on stack"); // debug
				aStack.push(current);
			}
			
			// current is A and no more a's should be present
			else if(current == 'A' && noMoreA == true)
			{
				//System.out.println("Debug: No more A's should be in string, returning false"); // debug
				return false;
			}
			
			//if current is B pop off stack and make sure noMoreA is true;
			else if (current == 'B')
			{
				aStack.pop();
				noMoreA = true;
			}
			
			else if (current != 'A' && current != 'B')
				return false;
		}// end loop
		
		//if there is anything on the stack if so return false
		if (aStack.peek() != null)
		{
			//System.out.println("DEBUG stack is not empty returning false");
			return false;
		} //end if for condition not met
		
		//everything thing is happy and worked well stack is emtpy
		else if (aStack.peek() == null)
		{
			//System.out.println("Debug: stack is empty returning true");
			return true;
		} // end else for condition met
		
		else
			return false;
	} // end method
	//-----------------------------------------------------------------------------------
	
	//-----------------------------------------------------------------------------------
	// Method Name; languageCheckL3
	// Parameters:  userstring - the string the user supplied
	// Notes:  
	public static boolean languageCheckL3(String userString)
	{
		//variables
		CoolerStack  aStack = new CoolerStack(); //empty stack
		char current;
		boolean noMoreA = false; //for keeping track of a's and b's
		
		//for blank string
		if (userString.length() <= 1)
			return false;
		
		//continue until end of string
		for (int x = 0; x <= (userString.length() -1) ; x++)
		{
			//get current character
			current = userString.charAt(x);
			
			//if current is A and no more a is false
			if (current == 'A' && noMoreA == false)
			{
				//push 2 A's on the stack...yep that's right 2 A
				aStack.push(current);
				aStack.push(current);
			}
			
			// current is A and no more a's should be present
			else if(current == 'A' && noMoreA == true)
			{
				//System.out.println("Debug: No more A's should be in string, returning false"); // debug
				return false;
			}
			
			//if current is B pop off stack and make sure noMoreA is true;
			else if (current == 'B')
			{
				aStack.pop();
				noMoreA = true;
			}
	
			//user is dumb
			else if (current != 'A' && current != 'B')
				return false;
		}// end loop
		
		//if there is anything on the stack if so return false
		if (aStack.peek() != null)
		{
			//System.out.println("DEBUG stack is not empty returning false");
			return false;
		} //end if for condition not met
		
		//everything thing is happy and worked well stack is emtpy
		else if (aStack.peek() == null)
		{
			//System.out.println("Debug: stack is empty returning true");
			return true;
		} // end else for condition met
		
		else
			return false;
	}
	//-----------------------------------------------------------------------------------
	
	//-----------------------------------------------------------------------------------
	public static boolean languageCheckL4(String userString)
	{
		//variables
		CoolerStack  aStack = new CoolerStack(); //empty stack
		char current = '?';		 // iterating through string
		boolean noMoreA = false; //for keeping track of a's and b's
		boolean patternFound = false; // this is true after a 2nd A is found
		boolean firstPatternFound = false;
		String currentPattern = "";   // for storing the 2nd and onward patterns for comparisons
		//String firstPattern = "";	  // every pattern is compared to this

		//for blank string
		if (userString.length() <= 1)
			return false;
		
		
		//continue until end of string
		for (int x = 0; x <= (userString.length() -1) ; x++)
		{
			//get current character
			current = userString.charAt(x);
			
			/*
			//System.out.println("--------------------------------------------------");
			System.out.print("X: " + x + " Current Char: " + current + "  " );
			if (aStack.peek() != null)
				System.out.println("  Stack: "+ aStack.peek().getValue());
			else
				System.out.println("Stack: Empty");
			//System.out.println("--------------------------------------------------");
			 * 
			 */
			
			
			//user is an idiot
			if (current != 'A' && current != 'B')
			{
				//System.out.println("Debug: FALSE non A/B char");
				return false;
			}
			
			//if current is A and no more a is false
			else if (patternFound == false && noMoreA == false && current == 'A' )
			{
				//push current on stack
				aStack.push(current);
			}
								
			// current is A and no more a's should be present pattern is done
			else if(patternFound == false && current == 'A' && noMoreA == true)
			{
				//System.out.println("DEBUG: Pattern found!");
				patternFound = true;
			}
				
			//if current is B push on stack and make sure noMoreA is true;
			else if (patternFound == false && current == 'B')
			{
				aStack.push(current);
				//System.out.println("DEBUG B PUSHED ONTO STACK");
				noMoreA = true;
				
				
				//if the last char then set patternFound to true
				if ((x <= userString.length() -1 )== true)
				{
					aStack.push(current);
					patternFound = true;
					//System.out.println("DEBUG: set to true because last char is b with no a");
					
					/*
					//System.out.println("--------------------------------------------------");
					System.out.print("X: " + x + " Current Char: " + current + "  " );
					if (aStack.peek() != null)
						System.out.println("  Stack: "+ aStack.peek().getValue());
					else
						System.out.println("Stack: Empty");
					//System.out.println("--------------------------------------------------");
					 * 
					 */
				}
				
			}// end else if

			//if a pattern has been completed, time to pop chars off stack and push a pattern on stack
			else if (patternFound == true)
			{	
				
				/*
				System.out.println("DEBUG: PATTERN FOUND CONDITION");
				System.out.print("X: " + x + " Current Char: " + current + "  " );
				if (aStack.peek() != null)
					System.out.println("  Stack: "+ aStack.peek().getValue());
				else
					System.out.println("Stack: Empty");
					*/
				
				//while stack is not empty and top of stack is not a char (ie a pattern/string)
				while (aStack.peek() != null && aStack.peek().getValue() != '\u0000')
				{
					//System.out.println("Debug Pattern: "+ currentPattern);
					currentPattern = currentPattern + aStack.pop().getValue();
					
					
				} // end while for pushing pattern on stack
				
				//push the current pattern on stack
				aStack.pushString(currentPattern);
				//System.out.println("pattern |" + currentPattern + "| pushed on the stack");
				patternFound = false;
				noMoreA = false;
				
				//reset value of current pattern
				currentPattern = "";
				
			}//end if pattern is true;
		}//end loop for reading characters from the string user supplied
		
		
		
		do
		{
			//for a weird case such as "ab"
			if (currentPattern.equals("") == true && aStack.peek().getPattern() == null)
				return true;
			
			//if 1st time through and its not a weird case such as "ab"
			else if (currentPattern.equals("") == true)
				currentPattern = aStack.pop().getPattern();
			
			//if no more patterns on stack
			else if (aStack.peek() == null)
				return true;
			
			//if there's another pattern on stack compare currentPattern and top of stack
			else if (aStack.peek() != null && aStack.peek().getPattern().equals(currentPattern) == false)
				return false; //two patterns do not match
			
			//if there's another pattern on stack compare currentPattern and top of stack
			else if (aStack.peek() != null && aStack.peek().getPattern().equals(currentPattern) == true)
			{
				//the two patterns match...pop off top and save it to current pattern
				currentPattern = aStack.pop().getPattern();
			}	
		}while (aStack.peek() != null);
		
		//made it this far...no more falses so must be true
		return true;
		
		
	}// end method
	
	/**
	 * menu method
	 * 
	 * Launches a menue that gives the usuer the option of doing the 4 language evaluation
	 * 	also added an adorable puppy youtube video because I didnt wait to the last min to do this
	 */
	public static void menu()
	{
		//adorable variables
		Scanner userSelection = new Scanner(System.in);
		String  choice = "0";
		
		while (choice.equals("5") == false)
		{
			// pretty menu
			System.out.println();
			System.out.println("=====================================================================");
			System.out.println("Test strings against each of the following languages:");
			System.out.println();
			System.out.println("1: L1={w: w contains equal numbers of A's and B's in any order,");
			System.out.println("\tand no other characters}");
			System.out.println("2: L2={w: w is of the form AnBn, for some n>=0}");
			System.out.println("3: L3={w: w is of the form AnB2n, for some n>=0}");
			System.out.println("4: L4={w: w is of the form (AnBm)p, for m, n, p>=0}");
			System.out.println("5: exit program");
			System.out.println("6: Launch adorable puppy youtube video");
			System.out.println("Options:");
			System.out.print("Choice: ");
				
			//get user input
			choice = userSelection.next();
			
			//call L3 method
			if (choice.equals("1"))
			{
				//get userstring
				String userString = getUserString();
				
				//call L1 method
				if( languageCheckL1(userString) == true)
					System.out.println("String meets L1 requirements");
				
				else
					System.out.println("String does not meet L1 requirements");
			}
			
			//call l2 method
			else if (choice.equals("2"))
			{
				//get userstring
				String userString = getUserString();
				
				//call L1 method
				if( languageCheckL2(userString) == true)
					System.out.println("String meets L2 requirements");
				
				else
					System.out.println("String does not meet L2 requirements");
			}
			
			//call l3 method
			else if (choice.equals("3"))
			{
				//get userstring
				String userString = getUserString();
				
				//call L1 method
				if( languageCheckL3(userString) == true)
					System.out.println("String meets L3 requirements");
				
				else
					System.out.println("String does not meet L3 requirements");
			}
			
			// call the dreaded L4 method bum bum bum
			else if (choice.equals("4"))
			{
				//get userstring
				String userString = getUserString();
				
				//call L1 method
				if( languageCheckL4(userString) == true)
					System.out.println("String meets L4 requirements");
				
				else
					System.out.println("String does not meet L4 requirements");
			}
			
			
			//puppy video
			else if (choice.equals("6"))
			{
				String url = "http://www.youtube.com/watch?v=MhFjWuUE-xA";
				try {
					java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}// end puppy option
			
		}// end while loop
	}// end menu method
	
	
}
