import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Main
{

	/**
	 * main
	 * @param args
	 */
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		//prompt user for date
		Date date = null;
		LL testLL = new LL();
		
		date = getDate();
		testLL.setCurrentDate(date);
		
		menu(testLL);

	}
	//-------------------------------------------------------------------------

	//-------------------------------------------------------------------------
	/**
	 * getDate
	 * @return the date the user entered
	 */
	public static Date getDate()
	{
		
		//fun variables
		Scanner input = new Scanner(System.in);
		String theDate = null;
		Date date = null;
		SimpleDateFormat tempDate = new SimpleDateFormat("yyyyMMdd"); //temp object


		//prompt user to enter the current date
		System.out.println("\n Enter the current date yyyyMMdd");
		System.out.print("\n Date: ");
		theDate = input.nextLine();
		
		//convert string to date format
    	try {
			date = tempDate.parse(theDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		

		
		return date;
	}
	//-------------------------------------------------------------------------

	//-------------------------------------------------------------------------
	/**
	 * menu
	 * @param theLL linkedlist that holds all of the assignment objects
	 */
	public static void menu(LL theLL)
	{
		//adorable variables
		Scanner userSelection = new Scanner(System.in);
		int  choice = 0;
		SimpleDateFormat displayDate = new SimpleDateFormat("yyyyMMdd");

		
		while (choice != 7)
		{
			
			System.out.println();
			System.out.println("Welcome to The Amazing Assignment Binary of Amazingness");
			
			// call assignment alert method for 2 days within due date
			//call assignment for past due method
			
			// pretty menu
			System.out.println("Options:");
			System.out.println("1: Change the Date. Current date: " +
					displayDate.format(theLL.getCurrentDate()) +  " ");
			System.out.println("2: Populate LinkedList with Input File");
			System.out.println("3: Add one assignment manually");
			System.out.println("4: Find Assignment with earliest due date");
			System.out.println("5: Remove assignments past due date");
			System.out.println("6: List assignments in the order they were assigned");
			System.out.println("7: Exit");
			System.out.println();
			System.out.print("Choice: ");
		
			choice = userSelection.nextInt();

		
			
			//user wants to change the date
			if (choice == 1)
			{
				Date newDate = null;
				newDate = getDate();
				theLL.setCurrentDate(newDate);
			}
			
			//user wants to populate via a file
			else if (choice == 2)
			{
				theLL.addAssignmentViaInputFile();
			}
			
			//add an assignment manually
			else if (choice == 3)
			{
				theLL.addAssignmentManually();
			}
			
			//get earliest assignment due
			else if (choice == 4)
			{
				theLL.getEarliestDueDate();
			}
			
			//remove past due items
			else if (choice == 5)
			{
				theLL.removePastDue();
			}
			
			//print out linked list
			else if (choice == 6)
			{
				theLL.print();
			}
			
			//exit
			else if (choice == 7)
			{
				System.out.println("Program closing");
			}
			
			//user ate far too many lead paint chips as a child case
			else
			{
				System.out.println("Invalid option");
			}
		} //end loop
	} // end menu method
	
}
