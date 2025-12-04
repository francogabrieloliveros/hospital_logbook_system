package application.models;

import java.time.LocalDate;
import java.util.ArrayList;

// patient class
public class Patient implements HospitalElement {
	// class fields
	public static int lastId = 0;
	private Hospital hospital;
	private String id;
	private String name;
	private LocalDate dob; // date of birth
	private String sex;
	private String notes;
	private ArrayList<LabExam> labExams = new ArrayList<>();
	
	// constructor
	public Patient(Hospital hospital, String name, LocalDate dob, String sex, String notes) {
		this.hospital = hospital;
		this.id = generateId();
		this.name = name;
		this.dob = dob;
		this.sex = sex;
		this.notes = notes;
		
		hospital.addPatient(this);
		addLogToHospital("Added new patient");
	}
	
	// Turn info into a string for the list
    @Override
    public String toString() {
    	return id + " | fullName=" + name + " | dob=" + dob + " | sex=" + sex + 
    			" | info=" + notes + " | exams=" + labExams.size();
    }
    
    // Log to LogBook
	@Override
	public void addLogToHospital(String message) {
		hospital.addLogBook(new LogBook("", "patient", message));
		
	}

	// Generate an id for the patient
	@Override
	public String generateId() {
		Patient.lastId++;
		return "PAT-" + String.format("%04d", Patient.lastId);
	}
	
	// Add a lab exam to this patient
	public void addLabExam(LabExam exam) {
		labExams.add(exam);
	}
	
	// Get all lab exams for this patient
	public ArrayList<LabExam> getLabExams() {
		return labExams;
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
}
