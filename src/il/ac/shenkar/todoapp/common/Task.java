package il.ac.shenkar.todoapp.common;

public class Task {
	
	private int id;
	private String itemDescription;
	private int year;
	private int month;
	private int day;
	private int hourOfDay; 
	private int minute;
	private int status;
	
	public Task() {
		super();
		this.status=0; //Means that the task is undone
	}
	
	public Task(String itemDescription, int year, int month, int day, int hourOfDay, int minute) {
		super();
	//	this.id = ? ;
		this.itemDescription = itemDescription;
		this.year = year;
		this.month = month;
		this.day = day;
		this.hourOfDay = hourOfDay;
		this.minute = minute;
		this.status = 0;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getItemDescription() {
		return itemDescription;
	}

	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getHourOfDay() {
		return hourOfDay;
	}

	public void setHourOfDay(int hourOfDay) {
		this.hourOfDay = hourOfDay;
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}

