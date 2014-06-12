import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

// Author: Ben Actis
// Date: 03/27/2013
// contact: ben.actis@gmail.com 
//          603-296-5506
// Notes: 2 methods
//			main: gets user input, catches if bad input comes in, calls recursive method
//			calculateD: calculates the determinant through the fun of recursion
//
// Note to Prof /TA: This assignment made me learn how to use the java debugger in eclipse, go debuggers :)


public class Main
{

	//Method: Main
	//Parameters: nothing special, the usual
	//Notes: ok here's how this method works
	//		1: gets input file from user, throws error if doesnt exist
	//		2: continues reading until no more lines can be read in
	//		3: checks to make sure row and column input are the same, if not throw exception
	//		4: reads in ints until size of row and columns are hit
	//		5: 
	public static void main(String[] args) {
		
		//---------variables------------
		int rowSize = 0;
		int columnSize = 0;
		Scanner i = new Scanner(System.in); //for getting file name/path
		Scanner scanner;
		String fileForInput = null;
		int tempForPrint = 0;
				
		//get file + path from user
		System.out.println("========================================================");
		System.out.println("Welcome to the amazing recursion calculator");
		System.out.print("Enter the file for input: ");
		fileForInput = i.nextLine();
		File textFile = new File(fileForInput);
		//int [][] matrixA = null;
		matrix matrixA = new matrix();
		
		//gets ints from the supplied user file. continues until
		// 1: no more lines to read in
		// 2: matrix is full
		// 3: exception, bad data
		try 
		{
			//create new scanner for userinput
			scanner = new Scanner(textFile);
			
			//continue until end of file
			while (scanner.hasNextLine())
	        {
				//get size of matrix
	            rowSize = scanner.nextInt();
	            columnSize = scanner.nextInt();
	            
	           //if input file is stupid throw an exception
	           if (rowSize != columnSize || rowSize <=0 || columnSize <=0)
	           {
	        	   throw new IOException();
	           }
	            
	          //create a matrix object
	         // matrixA = new int[rowSize][columnSize];
	         matrixA = new matrix(rowSize);
	            
	          //read in data (loop here)
	          for (int y = 0; y < columnSize; y++)
	          {
	        	 System.out.println();
	        	  for (int x = 0; x < rowSize; x++)
	        	  {
	        		  int temp = scanner.nextInt();
	        		  
	        		  matrixA.setValueAt(y, x, temp);
	        		 // matrixA[y][x] = scanner.nextInt();
	        		  tempForPrint = matrixA.getValueAt(y, x);
	        		 // tempForPrint = matrixA[y][x];
	        		  System.out.print(" "+ tempForPrint);
	        	  }
	          }// end loop for filling matrix
	        	  
	        }//end loop for reading input
			
			//try recursive test....
			//System.out.print("\nanswer is: ");
			int printFinalAnswer = calculateD(matrixA, rowSize);
			System.out.print("\nFinal Answer is: "+ printFinalAnswer);
			System.out.println();
			
		}// end try
		
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();

		}	

	}
	//Function Name; calcuateD
	//Parameters: int matrix a nxn matrix that holds intergers
	//			  int size size of the matrix
	//returns	the determinant
	//Notes:	this method does all the work here's how it works
	//			1: check to see if base cases are met
	//			2: if they are nto met continue to the big fun else that does a bunch of things
	//			3: start the loop that will iterate through the smaller and smaller matrixes
	//			4: create new matrix to hold smaller matrixes that are -1 smaller
	//			5: go through 2 embdedded loops
	//			6: check if the x (row) is less or greater than index, this plays a role in 
	//				determining if x or x-1 value is stored new in new matrix
	//			7: change sign if needed if its a even or odd column
	//			8: do the recursive call and add to final answer
	//			9: display final answer
	public static int calculateD(matrix theMatrix, int size)
	{
		//variables
		int sign = 1;			//for switching between pos / neg for n x n matrix
		int finalAnswer = 0;
		
		// REMEMBER [Y][X]
		
		if (size == 1)
		{
			//return matrix[0][0];
			return theMatrix.getValueAt(0, 0);
		}// end base case
		
		//base case if matrix is a 2x2
		else if (size == 2)
		{
			//return (matrix[0][0] * matrix[1][1]) - (matrix[0][1] * matrix[1][0]);
			return (theMatrix.getValueAt(0,0) * theMatrix.getValueAt(1,1) - (theMatrix.getValueAt(0,1) * theMatrix.getValueAt(1, 0)));

		}// end base case
		
		//recursive case
		else
		{
			//loop for making smaller matrixes
			for (int index = 1; index < size; index++)
			{
				//create a smaller matrix to store values in, this is smaller than the current matrix
				//int miniMatrix[][]= new int[size -1][size -1];
				matrix miniMatrix = new matrix(size -1);

				
				//Start at 2nd row because 1st row becasue x values are being
				//	multiplied to the new smaller size -1 determinants
				// note: used video https://www.khanacademy.org/math/algebra/algebra-matrices/inverting_matrices/v/finding-the-determinant-of-a-3x3-matrix-method-2
				// note: used this as reference as well http://professorjava.weebly.com/matrix-determinant.html
				for (int y = 1; y < size; y++)
				{
					
					for (int x = 0; x <size; x++)
					{
						if(x < index)
						{
							//miniMatrix[y-1][x] = matrix[y][x];
							miniMatrix.setValueAt(y-1, x, theMatrix.getValueAt(y, x));
				        }
				        else if(x > index)
				        {
				        	 // miniMatrix[y-1][x-1] = matrix[y][x];
							miniMatrix.setValueAt(y-1, x-1, theMatrix.getValueAt(y, x));

				        }
					}// close x loop
				}//close y loop
				
				//if the column that index is on, is even then set the
				//	sign value for negative. see khan academy video for
				//	rational
				if (index % 2 == 0)
				{
					sign = -1;
				}
				else
				{
					sign = 1;
				}
				
				//fun recursive call calculates by
				// 1: taking final answer and adding it to self
				// 2: adding the pos or negav value (sign int above)
				// 3: to the x value in top row
				// 4: to the small matrix determinate
				// 5: lather rinse repeat
				/*
				finalAnswer = finalAnswer + (sign *matrix[0][index]
						* calculateD(miniMatrix, size -1));
						*/
				
				finalAnswer = finalAnswer + (sign * theMatrix.getValueAt(0, index)
						* calculateD(miniMatrix, size -1));
				
			}//close index loop
			
		}//end else
		
		return finalAnswer;

		
	}//end method
	

}// main class
