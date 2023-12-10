package it.polito.oop.futsal;

public class Associate {
	private String first;
	private String last;
	private String phone;
	private boolean flag=false;
	public Associate(String first, String last, String phone) {
		super();
		this.first=first;
		this.last=last;
		this.phone=phone;
	}

	public String getFirst() {
		return first;
	}

	public void setFirst(String first) {
		this.first = first;
	}

	public String getLast() {
		return last;
	}

	public void setLast(String last) {
		this.last = last;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	//this metod set the flag to true
	public void setFlag() {
		this.flag=true;
	}
	//this method tells us if the associate has booked any fields
	public boolean hasBooked() {
		return flag;
	}
}
