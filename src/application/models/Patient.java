package application.models;

import java.time.LocalDate;

// patient class
public class Patient implements HospitalElement {
	// class fields
	public static int lastId = 0;
	public boolean isOwned;
	
	private Hospital hospital;
	private String id;
	private String name;
	private LocalDate dob; // date of birth
	private String sex;
	private String notes;
	
	// constructor
	public Patient(Hospital hospital, 
			       String name, 
			       LocalDate dob, 
			       String sex, 
			       String notes) {
		
		this.hospital = hospital;
		this.name = name;
		this.dob = dob;
		this.sex = sex;
		this.notes = notes;
		this.id = generateId();
		
		addLogToHospital(String.format("Added new patient %s", name));
	}
	
	// Restore constructor
	public Patient(Hospital hospital, 
				   String ID,
		           String name, 
		           LocalDate dob, 
		           String sex, 
		           String notes,
		           boolean isOwned) {
	
		this.hospital = hospital;
		this.name = name;
		this.dob = dob;
		this.sex = sex;
		this.notes = notes;
		this.isOwned = isOwned;
		this.id = ID;
	}
	
	// Update information
	public void update(String name, 
		       	   	   LocalDate dob, 
		       	   	   String sex, 
		       	   	   String notes) {
		this.name = name;
		this.dob = dob;
		this.sex = sex;
		this.notes = notes;
		
		addLogToHospital(String.format("Updated %s information", id));
	}
	
	// Remove patient from hospital
	public void delete() {
		addLogToHospital(String.format("Deleted %s from patients", id));
		hospital.removePatient(this);
	}
	
	public boolean stillOwned() {
		boolean stillOwnedLR = false, stillOwnedLB = false;
		
		for(LabRequest request : hospital.getLabRequests()) {
			if(request.getPatient().equals(this)) {
				stillOwnedLR = true;
				break;
			}
		}
		
		for(LabExam exam : hospital.getLabExams()) {
			if(exam.getPatient().equals(this)) {
				stillOwnedLB = true;
				break;
			}
		}
		
		return stillOwnedLR || stillOwnedLB;
	}
	
	// Turn info into a string for the list
    @Override
    public String toString() {
    	return String.format("%s | fullName=%s | dob=%s | sex=%s | info=%s", 
    			              id, name, dob, sex, notes);
    }
    
    // Log to LogBook
	@Override
	public void addLogToHospital(String message) {
		hospital.addLogBook(new LogBook("", "patient", message));
	}

	// Generate an id for the patient
	@Override
	public String generateId() {
		return "PAT-" + String.format("%04d", ++Patient.lastId);
	}
	
	// getters
	public String getID() { return this.id; }
	public String getName() { return this.name; }
	public LocalDate getDob() { return this.dob; }
	public String getSex() { return this.sex; }
    public String getNotes() { return this.notes; }
}
