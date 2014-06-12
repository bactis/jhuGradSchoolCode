/**
 * 
 */

/**
 * @author Ben Actis
 *
 */
public class CoolerStack
{
	private CoolerNode top;
	
	//-------------------------------------------------------------------------
	/**
	 * default constructor
	 */
	public CoolerStack()
	{
		this.top = null;
	}
	//-------------------------------------------------------------------------

	//-------------------------------------------------------------------------
	public CoolerNode peek()
	{
		CoolerNode returnThis = new CoolerNode();

		
		//check if stack is empty
		if (top == null)
		{
			//System.out.println("The Stack is empty. Can't Peek");
			return null;
		}
		
		//if the stack is not empty
		else
		{
			//print out value on the stack
			//System.out.println(top.getValue());
			returnThis = top;

			return returnThis;
			
			
		}
	}
	//-------------------------------------------------------------------------
	
	//-------------------------------------------------------------------------
	/**
	 * pop method
	 * prints out what is at the top of the stack and removes it.
	 */
	public CoolerNode pop()
	{
		CoolerNode returnThis = new CoolerNode();
		
		//check if stack is empty
		if (this.top == null)
		{
			System.out.println("The Stack is empty. Can't do a pop"); //debug
			
			return null;
		}
		
		//if the stack is not empty
		else
		{
			//print out value on the stack
			//System.out.println(top.getValue());
			returnThis = this.top;
			
			
			//delete top node by setting top to the next node in stack
			this.top = this.top.getNext();	
			
			return returnThis;

		} 
		
		
		
	}
	//-------------------------------------------------------------------------
	
	//-------------------------------------------------------------------------
	/**
	 * push method
	 * @param value -value being added a char primitive
	 */
	public void push(char value)
	{
		//Create a new CoolerNode Object
		CoolerNode newNode = new CoolerNode(value);
		
		//check if top is null, if so newNode is at the top of stack
		if (top == null)
		{
			this.top = newNode;
		}
		
		//if top is not null...push top down one on the stack and set newNode to top
		else
		{
			newNode.setNext(top);
			top = newNode;
		}
	}// end push method
	//-------------------------------------------------------------------------
	
	//-------------------------------------------------------------------------
	
	//-------------------------------------------------------------------------
	/**
	 * push method
	 * @param value -value being added a char primitive
	 */
	public void pushString(String pattern)
	{
		//Create a new CoolerNode Object
		CoolerNode newNode = new CoolerNode(pattern);
		
		//check if top is null, if so newNode is at the top of stack
		if (top == null)
		{
			this.top = newNode;
		}
		
		//if top is not null...push top down one on the stack and set newNode to top
		else
		{
			newNode.setNext(top);
			top = newNode;
		}
	}// end push method
	//-------------------------------------------------------------------------
	
	//-------------------------------------------------------------------------


}
