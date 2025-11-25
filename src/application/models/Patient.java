package application.models;

import java.time.LocalDate;

// patient class
public class Patient {
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
}
