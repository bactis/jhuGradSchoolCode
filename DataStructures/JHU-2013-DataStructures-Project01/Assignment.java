import java.util.Date;


public class Assignment
{
	//attributes
	private Date assignedDate;
	private Date dueDate;
	private String name;
	private Assignment next;
	
	/**
	 * default constructor
	 */
	public Assignment()
	{
		next = null;
		assignedDate = null;
		dueDate = null;
		name = null;
	}
	
	/**
	 * Assignment Constructor
	 * @param dueDate -date in which assignment is due ex: 20120223
	 * @param name - name of assignment
	 */
	public Assignment(String name, Date assignedDate, Date dueDate) 
	{
		this.name = name;
		this.assignedDate = assignedDate;
		this.dueDate = dueDate;
		this.next = null;
	}

	
	public Date getAssignedDate() {
		return assignedDate;
	}

	public void setAssignedDate(Date assignedDate) {
		this.assignedDate = assignedDate;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Assignment getNext() {
		return next;
	}

	public void setNext(Assignment next) {
		this.next = next;
	}
	
	
	
	
}
