import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;


public class LL
{
	private Assignment front;
	private Date currentDate;
	//private SimpleDateFormat simpleDate = new SimpleDateFormat("yyyyMMdd");;

	
	//-------------------------------------------------------------------------
	/**
	 * default constructor
	 */
	public LL()
	{
		front = null;    //first node in LinkedList
		currentDate = null; //the current date
		//simpleDate = null;

	}
	//-------------------------------------------------------------------------

	//-------------------------------------------------------------------------
	//getters and setters
	public Date getCurrentDate() {
		return currentDate;
	}
	
	//public String getCurrentDateSimple()
	//{
		//return simpleDate.format(this.getCurrentDate());
	//}
	
	public void setCurrentDate(Date currentDate) {
		this.currentDate = currentDate;
	}
	//-------------------------------------------------------------------------
	
	public void addAssignmentViaInputFile()
	{
		//variables
		String fileForInput = null; //file name/path
		Scanner i = new Scanner(System.in); //for getting file name/path
		Date dAssignmentDate = null;
        Date dDueDate = null;
        SimpleDateFormat tempAssignmentDate = new SimpleDateFormat("yyyyMMdd"); //temp object
		SimpleDateFormat tempDueDate = new SimpleDateFormat("yyyyMMdd"); //temp object
		String inputTitle = null;
        String inputAssignmentDate = null;
        String inputDueDate = null;

		
		//get file/path from user
		System.out.print("Enter the file for input: ");
		fileForInput = i.nextLine();
		
		//create file object
		File textFile = new File(fileForInput);
		
		//create new scanner object and catch error if user is a dummy
		Scanner scanner;
		try 
		{
			scanner = new Scanner(textFile);
			
			//continue until end of file
			while (scanner.hasNextLine())
	        {
	            String temp = scanner.nextLine();
	            String splitIsFun[] = temp.split(",");
	            inputTitle = splitIsFun[0];
	            inputAssignmentDate = splitIsFun[1];
	            inputDueDate = splitIsFun[2];
	            
	            dAssignmentDate = tempAssignmentDate.parse(inputAssignmentDate);
				dDueDate = tempDueDate.parse(inputDueDate);

	            
	            //create a new assignment
	           this.insert(inputTitle, dAssignmentDate, dDueDate);
	        } // end reading loop
		} 
		
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} // addAssignmentViaInputFile method end
	
	//-------------------------------------------------------------------------
	/**
	 * addAssignment
	 * 
	 * gets assignment name and date, and creates new assignment and inserts
	 * it into linked list
	 */
	public void addAssignmentManually()
	{
		//variables
        String inputTitle = null;
        String inputAssignmentDate = null;
        String inputDueDate = null;
        Date dAssignmentDate = null;
        Date dDueDate = null;
		SimpleDateFormat tempAssignmentDate = new SimpleDateFormat("yyyyMMdd"); //temp object
		SimpleDateFormat tempDueDate = new SimpleDateFormat("yyyyMMdd"); //temp object

        
        
		Scanner b = new Scanner(System.in);
		
		//get 3 values from user to create assignment object
		//get assignment name
		System.out.print("Enter the assignment name: ");
		inputTitle = b.nextLine();
		System.out.println();
		
		//get assignment assign date
		System.out.print("Enter the assignmented date yyyyMMdd: ");
		inputAssignmentDate = b.nextLine();
		try {
			dAssignmentDate = tempAssignmentDate.parse(inputAssignmentDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println();
		
		//get due date
		System.out.print("Enter the due date yyyyMMdd: ");
		inputDueDate = b.nextLine();
		try
		{
			dDueDate = tempDueDate.parse(inputDueDate);
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//create new assignment with data above
				this.insert(inputTitle, dAssignmentDate, dDueDate);	
		
		
	}// end addAssignment method
	//-------------------------------------------------------------------------

	//-------------------------------------------------------------------------
	/**
	 * getEarliestDueDate
	 */
	public void getEarliestDueDate()
	{
		SimpleDateFormat displayDate = new SimpleDateFormat("yyyyMMdd");

		//make sure linkedList is not empty
		if (front == null)
		{
			System.out.println("No assignments in linkedList");
			System.out.println();
		}
		
		else
		{
			//variables
			Assignment soonest = front;
			Assignment a = front;
			
			while(a != null)
			{
				//compare a with soonest
				if (a.getDueDate().before(soonest.getDueDate()))
				{
					//set soonest to a
					soonest = a;
				}
				
				//increment a
				a = a.getNext();
			} //end increment loop
			
			//print out value of soonest
			System.out.println("Closet Assignment Due is...");
			System.out.println("Name: "+ soonest.getName());
			System.out.println("Assigned: " + displayDate.format(soonest.getAssignedDate()));
			System.out.println("Due: "+ displayDate.format(soonest.getDueDate()));
		} // end if the LL is not empty
		
		
	}//end getEarliestDueDate method
	//-------------------------------------------------------------------------
	
	//-------------------------------------------------------------------------
	/**
	 * insert
	 * @param theTitle  the name of the assignment
	 * @param theDate   the date of the assignment
	 * 
	 * inserts assignment in order of due dates into the linked list
	 * 1: can insert in front
	 * 2: middle
	 * 3: at the end
	 */
	public void insert(String theTitle, Date theAssignedDate, Date theDueDate)
	{
		//create a new assignment temp object
		Assignment temp = new Assignment(theTitle, theAssignedDate, theDueDate);
		
		// if list is empty stick it in the front
		if (front == null)
		{
			front = temp;
		}
		
		Assignment a = front;
		Assignment b = null;
		
		//continue until at end of linked list
		while (a!= null)
		{
			b = a;
			a = a.getNext();
			
			//if the new string is less than front..drop it in the front
			if (front.getAssignedDate().compareTo(theAssignedDate) > 0)
			{
				//debug code, enable if needed
				System.out.println("DEBUG: new entry belongs in front");
				
				//stick new entry in front
				temp.setNext(front);
				
				//set front to new entry
				front = temp;
			}
			
			//if if at end of linked list, and still greater, then throw it at end
			else if (a== null && b.getAssignedDate().compareTo(theAssignedDate) <= 0)
			{
				//debug code, enable if needed
				System.out.println("DEBUG: new entry belongs in back");
				
				b.setNext(temp);
				temp.setNext(null);
			}
			
			//if new string is lexicongraphically less than a (iterator) and
			// less than one before the iterator (b)
			else if (a!=null && a.getAssignedDate().compareTo(theAssignedDate) >= 0 
					&& b.getAssignedDate().compareTo(theAssignedDate) <= 0)
			{
				//debug code, enable if needed
				System.out.println("DEBUG: belongs in between a and b");
				
				b.setNext(temp);
				temp.setNext(a);
			}
		}//end loop
	}// end insert method
	//-------------------------------------------------------------------------

	//-------------------------------------------------------------------------
	/**
	 * print()
	 * 
	 * prints out all the elements in the linkedlist
	 */
	public void print()
	{
		SimpleDateFormat displayDate = new SimpleDateFormat("yyyyMMdd");

		
		//if linked list is empty
		if (front == null)
		{
			System.out.println("LinkedList is empty");
		}
		
		//print linkedlist
		else
		{
			Assignment q = front;
			
			//loop until we reach the end, print and increase q
			while (q.getNext()!=null)
			{
				System.out.println("Assignment Name: "+ q.getName());
				System.out.println("Assigned Date: " + displayDate.format(q.getAssignedDate()));
				System.out.println("Due Date: " + displayDate.format(q.getDueDate()));
				System.out.println();
				q = q.getNext(); 
			}
			
			//on last value in linkedList
			if (q.getNext() == null)
			{
				System.out.println("Assignment Name: "+ q.getName());
				System.out.println("Assigned Date: " + displayDate.format(q.getAssignedDate()));
				System.out.println("Due Date: " + displayDate.format(q.getDueDate()));
				System.out.println();
			}
		} //end else
	}//end print method
	//-------------------------------------------------------------------------
	
	//-------------------------------------------------------------------------
	public void removeAssignnment(Assignment deleteMe)
	{
		// if list is empty 
		if (front == null)
		{
			System.out.println("LinkedList is empty, nothing to remove");
		}
		
		Assignment a = front;
		Assignment b = null;
		
		//continue until at end of linkedlist
		while (a!= null)
		{
			b = a;
			a = a.getNext();
			
			//check to see if front is the item to be deleted
			if (front.equals(deleteMe) == true)
			{
				//debug code enable if needed
				System.out.println("DEBUG: removing from front");
				
				//set the 2nd value to front
				front = a;
			}// end if for front delete
			
			//if new string is lexicongraphically less than a (iterator) and
			// less than one before the iterator (b)
			else if (a!=null && a.equals(deleteMe) == true)
			{
				//debug code enable if needed
				System.out.println("removing from middle");
				
				b.setNext(a.getNext());
				a = null;
			} // end if for middle / end delete
		}// end loop
	}//end method removeAssignment
	//-------------------------------------------------------------------------
	
	//-------------------------------------------------------------------------
	/**
	 * removePastDue
	 */
	public void removePastDue()
	{	
		//check to make sure there's something in the linked list
		if (front != null)
		{
			//create assignment object to iterate through list
			Assignment a = front;
			
			//step through list until end
			while(a != null)
			{
				//past due, delete assignment object a
				if (a.getDueDate().before(this.getCurrentDate()))
				{
					System.out.println("Removing Assignment titled: " + a.getName());
					this.removeAssignnment(a);
				}
				
				//increment a
				a= a.getNext();
			}//end loop
		}//end if
	}
	//-------------------------------------------------------------------------

	
	
}
