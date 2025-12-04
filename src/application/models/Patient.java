package application.models;

import java.time.LocalDate;

// patient class
public class Patient implements HospitalElement {
	private String id;
	private String name;
	private LocalDate dob; // date of birth
	private String sex;
	private String notes;
	
	// constructor
	public Patient(String id, String name, LocalDate dob, String sex, String notes) {
		this.id = id;
		this.name = name;
		this.dob = dob;
		this.sex = sex;
		this.notes = notes;
	}
	
	// getters and setters
	public String getId() { return id; }
	public String getName() { return name; }
	public LocalDate getDob() { return dob; }
	public String getSex() { return sex; }
    public String getNotes() { return notes; }

    public void setName(String name) { this.name = name; }
    public void setDob(LocalDate dob) { this.dob = dob; }
    public void setSex(String sex) { this.sex = sex; }
    public void setNotes(String notes) { this.notes = notes; }
    
    // function for for list viewing
    @Override
    public String toString() {
    	return id + " | " + name + " | " + dob;
    }

	@Override
	public void addLogToHospital(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String generateId() {
		// TODO Auto-generated method stub
		return null;
	}
}
