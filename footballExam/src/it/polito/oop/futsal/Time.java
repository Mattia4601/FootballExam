package it.polito.oop.futsal;

public class Time {
	private int hh;
	private int mm;
	
	public Time(String time) {
		super();
		String[] temp = time.split(":");
		hh = Integer.parseInt(temp[0]);
		mm = Integer.parseInt(temp[1]);
		this.hh = hh;
		this.mm = mm;
	}

	final static String MIDNIGHT = "00:00";
	
	//check if a given time preceed this time, if so it returns true
	public boolean checkTime(String time) {
		String[] temp=time.split(":");
		if (Integer.parseInt(temp[0]) < this.hh) {
			return true;
		}
		else if (Integer.parseInt(temp[0]) == this.hh){
			if (Integer.parseInt(temp[1]) < this.mm)
				return true;
		}
		
		return false;
	}
	
	// this method will check if a given time is aligned to the opening time
	public boolean isAligned(String time) {
		
		if (this.checkTime(time)) {
			return false; // time preceeds opening time so it's not aligned
		}
		
		String[] temp=time.split(":");
		
		int minsTime = Integer.parseInt(temp[1]) + Integer.parseInt(temp[0]) * 60 ;
		int openingTimeMins = this.mm + 60 * this.hh;
		int diff = minsTime - openingTimeMins;
		
		if (diff%60==0)
			return true; 
		
		return false;
	}

	public String getTimeString() {
		return String.format("%02d:%02d", hh,mm);
	}

	public int getHh() {
		return hh;
	}

	public void setHh(int hh) {
		this.hh = hh;
	}

	public int getMm() {
		return mm;
	}

	public void setMm(int mm) {
		this.mm = mm;
	}
}
