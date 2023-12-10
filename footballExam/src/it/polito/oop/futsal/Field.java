package it.polito.oop.futsal;

import java.util.HashMap;
import java.util.Map;

import it.polito.oop.futsal.Fields.Features;

public class Field implements FieldOption {
	private final int id;
	private Features features;
	private Map<String, Associate> bookings = new HashMap();
	
	public Field(int id, Features features) {
		super();
		this.id=id;
		this.features = features;
	}
	
	public int getFieldId() {
		return id;
	}
	
	public boolean isIndoor() {
		return features.indoor;
	}
	
	// this method add a booking 
	public void addBooking(String time, Associate associate) {
		this.bookings.put(time, associate);
	}
	
	// this method check if the field is booked at a given time
	public boolean checkIfBooked(String time) {
		return bookings.containsKey(time);
	}
	//return the booking map
	public Map<String, Associate> getBookings() {
		return bookings;
	}

	@Override
	public int getField() {
		return id;
	}

	@Override
	public int getOccupation() {
		return bookings.size();
	}
	
	//this method check if the field matches the required features
	public boolean checkFeatures(Features requiredFeatures) {
		
		if (this.features.ac == requiredFeatures.ac &&
				this.features.heating == requiredFeatures.heating &&
				this.features.indoor == requiredFeatures.indoor)
			return true;
		return false;
	}
}
