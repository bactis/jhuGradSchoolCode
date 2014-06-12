//Name: Ben Actis
//Date: 04/13/1987
//Notes: Code is from textbook chap 6 


import java.io.BufferedReader;
import java.io.IOException;
import java.io.Serializable;

//------------------------------------------------------------------------------------------------------
//ClassName:			BinaryTree
//Notes:				Class for a binary tree that stores type E objects.
//						@author Koffman and Wolfgang - from textbook
//						3 constructors 
//							(empty)
//							(node root)
//							(node root, L, R)
//						methods
//							getData
//							getLeftSubtree
//							getRightSubtree
//							isLeaf
//							toString Overide
public class BinaryTree<E> implements Serializable 
{ 
    
    
    //===================Data Field for BinarySearchTree==================
    protected Node<E> root;		//The root of the binary tree

    //===========================Constructors for BinaryTree====================================
    //-----------------------------------------
    //Constructor:	empty constructor
    //notes: 		constructs an empty binary tree
    //					from textbook code
    public BinaryTree()
    {
        root = null;
    }//------------------------------------------

    //--------------------------------------------------------------
    //Constructor:	constructor with root
    //Notes: 		Construct a BinaryTree with a specified root.
    //					Should only be used by subclasses.
    //					from textbook code
    //Parameter:	root -The node that is the root of the tree.
    protected BinaryTree(Node<E> root)
    {
        this.root = root;
    }//----------------------------------------------------------

    //------------------------------------------------------------------------
    //Constructor:  constructor with root, L, R
    //notes:		Constructs a new binary tree with data in its root,
    //					leftTree as its left subtree and
    //				 	rightTree as its right subtree
    public BinaryTree(E data, BinaryTree<E> leftTree, BinaryTree<E> rightTree)
    {
        root = new Node<E>(data);
        if (leftTree != null) {
            root.left = leftTree.root;
        } else {
            root.left = null;
        }
        if (rightTree != null) {
            root.right = rightTree.root;
        } 
        else 
        {
            root.right = null;
        }
    }
    //---------------------------------------------------------------------
    //==========================End constructors for BinaryTree=====================================

    //===============================BinarySearchTree Methods=========================================
    //----------------------------------------------------------
    //MethodName:		GetLeftSubtree
    //Returns:			the left subtree or null if
    //						the root or left subtree is null
    //notes				from book code
    public BinaryTree<E> getLeftSubtree() {
        if (root != null && root.left != null) {
            return new BinaryTree<E>(root.left);
        } else {
            return null;
        }
    }
    //----------------------------------------------------------

  //----------------------------------------------------------
    //MethodName:		GetRightSubtree
    //returns:			the right sub tree or null if
    //						the root or right subtree is null
    //Notes:			also from book code
    public BinaryTree<E> getRightSubtree()
    {
        if (root != null && root.right != null) 
        {
            return new BinaryTree<E>(root.right);
        } 
        else 
        {
            return null;
        }
    }
    //----------------------------------------------------------
    
    //----------------------------------------------------------
    //Name:				getData
    //Returns:			the data field of the root
    //						or null if the root is null
    public E getData() 
    {
        if (root != null) 
        {
            return root.data;
        } 
        else
        {
            return null;
        }
    }
    //---------------------------------------------------------

    //----------------------------------------------------------
    //Name:			isLeaf
    //Returns:		true if the root has no children
    public boolean isLeaf()
    {
        return (root == null || (root.left == null && root.right == null));
    }
    //----------------------------------------------------------
    
    //----------------------------------------------------------
    //Name:			Overide ToString
    //Notes			for printing out string
    @Override
    public String toString() 
    {
        StringBuilder sb = new StringBuilder();
        preOrderTraverse(root, 1, sb);
        return sb.toString();
    }
    //------------------------------------------------------------

    //------------------------------------------------------------
    //Name:			preOrderTraverse
    //Parameters:
    //				node  - the local root
    //				depth - the depth
    //				sb    - string buffer to save output
    //Notes:		textbook code
    private void preOrderTraverse(Node<E> node, int depth,
            StringBuilder sb) {
        for (int i = 1; i < depth; i++) {
            sb.append("  ");
        }
        if (node == null) {
            sb.append("null\n");
        } else {
            sb.append(node.toString());
            sb.append("\n");
            preOrderTraverse(node.left, depth + 1, sb);
            preOrderTraverse(node.right, depth + 1, sb);
        }
    }
    //----------------------------------------------------------------

    //----------------------------------------------------------------
    //Name: 		readbinaryTree
    //Note:			method to read a binary tree, uses preorder traversal
    //					as input. The line "null indicates a null value
    //				also from book code
    //Parameters:	br - the input file
    //Returns:		the binary tree
    //Throws		IOexception - if there is an error with input	
    /*<listing chapter="6" number="2">*/
    public static BinaryTree<String> readBinaryTree(BufferedReader bR) throws IOException
    {
        // Read a line and trim leading and trailing spaces.
        String data = bR.readLine().trim();
        if (data.equals("null"))
        {
            return null;
        } 
        else
        {
            BinaryTree<String> leftTree = readBinaryTree(bR);
            BinaryTree<String> rightTree = readBinaryTree(bR);
            return new BinaryTree<String>(data, leftTree, rightTree);
        }
    }
    //--------------------------------------------------------------------------------------
    /*</listing>*/

// Insert solution to programming exercise 1, section 3, chapter 6 here

// Insert solution to programming exercise 2, section 3, chapter 6 here

// Insert solution to programming exercise 3, section 3, chapter 6 here
}
/*</listing>*/
