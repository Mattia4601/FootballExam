package it.polito.oop.futsal;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Represents a infrastructure with a set of playgrounds, it allows teams
 * to book, use, and  leave fields.
 *
 */
public class Fields {
	
	private Map<Integer, Field> fieldsMap = new HashMap();
	private Time openingTime;
	private Time closingTime;
	private Map<Integer,Associate> associates = new HashMap();
    
	public static class Features {
        public final boolean indoor; // otherwise outdoor
        public final boolean heating;
        public final boolean ac;
        public Features(boolean i, boolean h, boolean a) throws FutsalException {
            this.indoor=i; this.heating=h; this.ac = a;
        }
    }
    
	//initialize different footbal pitches with different charateristics
    public void defineFields(Features... features) throws FutsalException {
    	for (Features f : features) { //for each group of features
    		//check if the ac and heating are true so indoor must be
    		if (!f.indoor && (f.ac || f.heating)) {
    			throw new FutsalException("if the ac and heating are present so indoor must be");
    		}
    		
    		// then create a new field giving it an id and the group of features
    		Field newField = new Field(fieldsMap.size()+1,f);
    		//then we add it to the map containing all the fields
    		fieldsMap.put(newField.getFieldId(), newField);
    	}
    }
    // get the total number of fields using the method countFields() 
    public long countFields() {
        return fieldsMap.size();
    }
    //return the total number of indoor fields
    public long countIndoor() {
        return fieldsMap.values().
        		stream().filter(e->e.isIndoor())
        		.count();
    }   
    
    public String getOpeningTime() {
        return openingTime.getTimeString();
    }
    
    public void setOpeningTime(String time) {
    	
    	openingTime = new Time(time);
    }
    
    public String getClosingTime() {
    	if (closingTime == null) {
    		return closingTime.MIDNIGHT;
    	}
        return closingTime.getTimeString();
    }
    
    public void setClosingTime(String time) {
    	closingTime = new Time(time);
    }

    public int newAssociate(String first, String last, String mobile) {
        
    	Associate a = new Associate(first,last,mobile);
    	
    	int idAssociate = associates.size()+1; 
    	associates.put(idAssociate, a);
    	
    	return idAssociate;
    }
    
    public String getFirst(int associate) throws FutsalException {
    	if (associates.containsKey(associate) == false) {
    		throw new FutsalException(); 
    	}
        return associates.get(associate).getFirst();
    }
    
    public String getLast(int associate) throws FutsalException {
        
    	if (associates.containsKey(associate) == false) {
    		throw new FutsalException(); 
    	}
        
    	return associates.get(associate).getLast();
    }
    
    public String getPhone(int associate) throws FutsalException {
        
    	if (associates.containsKey(associate) == false) {
    		throw new FutsalException(); 
    	}
        
    	return associates.get(associate).getPhone();
    }
    
    public int countAssociates() {
        return associates.size();
    }
    
//    Reserving a field is performed using the method bookField(), which gets the field number, 
//    the unique code of an associate, and the starting time.
    public void bookField(int field, int associate, String time) throws FutsalException {
    	
    	if ( ( !fieldsMap.containsKey(field) ) || ( !associates.containsKey(associate) ) ) {
    		throw new FutsalException();
    	}
    	
    	if (!this.openingTime.isAligned(time)  || !this.closingTime.checkTime(time) ) {
    		throw new FutsalException();
    	}
    	
    	
    	Associate ass = associates.get(associate);
    	fieldsMap.get(field).addBooking(time,ass);
    	// we also save that the associate has booked some field
    	ass.setFlag();
    }

    public boolean isBooked(int field, String time) {
    	
    	Field fieldEl = fieldsMap.get(field);
    	
    	
    	return fieldEl.checkIfBooked(time);
    }
    
// gets a field number, and returns the number of reservations made.
    public int getOccupation(int field) {
    	
        Field fieldEl=fieldsMap.get(field); 
    	int nRes = fieldEl.getBookings().size();
    			
    	return nRes;
    }
//    The method findOptions() gets a schedule and an object Features, and returns the list of options
//    available in the fields that have the required characteristics and are free at the specified time.
    public List<FieldOption> findOptions(String time, Features required){

    	return fieldsMap.values().stream()
    			.filter( f -> f.checkIfBooked(time)==false) //check if the time slot is already booked
        		.filter(f -> f.checkFeatures(required)) //check if the required features match 
        		.sorted(Comparator.comparing(FieldOption::getOccupation).reversed().thenComparing(FieldOption::getField))
        		.collect(Collectors.toList());
        		
    }
    //countServedAssociates() returns the total number of associates who have made at least one reservation.
    public long countServedAssociates() {
        return associates.values().stream()
        		.filter(a->a.hasBooked()==true) //we need only the associates who has booked some field
        		.count();
    }
//  fieldTurnover() returns a map with keys the ids of the fields, and values the number of reservations at these fields.
    public Map<Integer,Long> fieldTurnover() {
        return fieldsMap.values().stream()
        		.collect(Collectors.toMap(Field::getField, f->(long)f.getOccupation()));
    }
//  occupation() returns the occupation level of the facility in terms of percentage. 
//  It is calculated as the ratio of the number of reservations in all fields and the number of blocks from one hour available between opening and closing times.
    public double occupation() {
        //get the number of reservable slots
    	double reservableSlots = this.getNumberOfSlots()*fieldsMap.size();
        		
        //get the total number of reservations
    	double totalRes = fieldsMap.values().stream()
    			.mapToInt(f->f.getOccupation())
    			.sum();
        		
        		
        return totalRes/reservableSlots;		
    }
	
	// This method calculate the number of slots we have for a fields facility
	public int getNumberOfSlots() {
		return ( closingTime.diffTime(openingTime) )/60;
	}
 }
