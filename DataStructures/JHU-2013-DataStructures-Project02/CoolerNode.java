/**
 * 
 */

/**
 * @author Ben Actis
 * date: 2/21/2012
 * This is a node class. Simple setters, getters, and constructors. Nothing fancy
 *
 */
public class CoolerNode
{
	private char value;
	private CoolerNode next;
	private String pattern;
	
	/**
	 * default constructor
	 * Citation: default value of char in java found at this site
	 * 		http://stackoverflow.com/questions/9909333/whats-the-default-value-of-char
	 */
	public CoolerNode()
	{
		this.value = '\u0000';
		this.next = null;
		this.pattern = null;
	}
	
	/**
	 * constructor for coolerNode
	 * @param value - the char
	 */
	public CoolerNode(char value)
	{
		this.value = value;
		this.next = null;
		this.pattern = null;
	}
	
	public CoolerNode(String pattern)
	{
		this.value = '\u0000';
		this.next = null;
		this.pattern = pattern;
	}

	/**
	 * @return the value
	 */
	public char getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(char value) {
		this.value = value;
	}

	/**
	 * @return the next
	 */
	public CoolerNode getNext() {
		return next;
	}

	/**
	 * @param next the next to set
	 */
	public void setNext(CoolerNode next) {
		this.next = next;
	}

	/**
	 * @return the pattern
	 */
	public String getPattern() {
		return pattern;
	}

	/**
	 * @param pattern the pattern to set
	 */
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	
	
	
	
}
