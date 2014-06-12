
public class matrix 
{

	private int size;
	private int [][] matrixA;
	
	//default constructor
	public matrix()
	{
		this.size = 1;
		matrixA = new int[1][1];
		matrixA[0][0] = 0;
	}
	
	public matrix(int size)
	{
		this.size = size;
		
		//create a matrix object
        matrixA = new int[size][size];
          
        for (int y = 0; y < size; y++)
        {
      	  for (int x = 0; x < size; x++)
      	  {
      		  matrixA[y][x] = 0;
      	  }
        }// end loop for filling matrix
	}

	/**
	 * @return the size
	 */
	public int getSize() {
		return size;
	}

	/**
	 * @param size the size to set
	 */
	public void setSize(int size) {
		this.size = size;
	}

	/**
	 * @return the matrixA
	 */
	public int getValueAt(int a, int b) {
		return matrixA[a][b];
	}

	/**
	 * @param matrixA the matrixA to set
	 */
	public void setValueAt(int a, int b, int value) {
		this.matrixA[a][b] = value;
	}

	
	
	
}
