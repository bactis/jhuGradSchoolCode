import java.io.Serializable;


//------------------------------Class Node<E>------------------------------------------
//ClassName:		Node<E>
//Notes:			Class to encapsulate a tree node.
//						uses textbook code
public class Node <E> implements Serializable 
{
	 //--------------------------Data Fields for Node<E>------------------
    public E data;			//The information stored in this node
    public Node<E> left;	//Reference to the left child.
    public Node<E> right;	//Reference to the right child

    //-------------------------Node<E> Constructor-----------------------
    //Constructor:     a node with given data and no children.
    //Parameters:		data The data to store in this node
    public Node(E data)
    {
        this.data = data;
        left = null;
        right = null;
    }// -----------------end Node Constructor ---------------------------

    //-----------------------toString Method----------------------------
    //MethodName:    toString
    //Returns:       a string representation of the node.
    @Override
    public String toString()
    {
        return data.toString();
    }// ------------------end  toString method---------------------------
}//------------------------ end node class------------------------------------------------------

