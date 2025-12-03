package application.models;

import java.util.*;

public class Hospital {
	
	private ArrayList<Staff> staffs = new ArrayList<Staff>();
	private ArrayList<Patient> patients = new ArrayList<Patient>();
	private ArrayList<LabRequest> labRequests = new ArrayList<LabRequest>();
	private ArrayList<LabExam> labExams = new ArrayList<LabExam>();
	private ArrayList<LogBook> logBooks = new ArrayList<LogBook>();
	
	// Adders
	public void addStaff(Staff staff) { staffs.add(staff); }
	public void addPatient(Patient patient) { patients.add(patient); }
	public void addLabRequest(LabRequest labRequest) { labRequests.add(labRequest); }
	public void addLabExam(LabExam labExam) { labExams.add(labExam); }
	public void addLogBook(LogBook logBook) { logBooks.add(logBook); }

	// Deleters
	public void removeStaff(Staff staff) { staffs.remove(staff); }
	public void removePatient(Patient patient) { patients.remove(patient); }
	public void removeLabRequest(LabRequest labRequest) { labRequests.remove(labRequest); }
	public void removeLabExam(LabExam labExam) { labExams.remove(labExam); }
	public void removeLogBook(LogBook logBook) { logBooks.remove(logBook); }
	
	// Array getters
	public ArrayList<Staff> getStaffs() { return this.staffs; }
	public ArrayList<Patient> getPatients() { return this.patients; }
	public ArrayList<LabRequest> getLabRequests() { return this.labRequests; }
	public ArrayList<LabExam> getLabExams() { return this.labExams; }
	public ArrayList<LogBook> getLogBooks() { return this.logBooks; }
	
}
