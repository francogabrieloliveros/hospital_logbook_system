package application.models;

import java.io.*;
import java.nio.file.*;
import java.time.*;
import java.util.*;

public class Hospital {
	
	private ArrayList<Staff> staffs;
	private ArrayList<Patient> patients;
	private ArrayList<LabRequest> labRequests;
	private ArrayList<LabExam> labExams;
	private ArrayList<LogBook> logBooks;
	
	public Hospital() {
		this.staffs = new ArrayList<Staff>();
		this.patients = new ArrayList<Patient>();
		this.labRequests = new ArrayList<LabRequest>();
		this.labExams = new ArrayList<LabExam>();
		this.logBooks = new ArrayList<LogBook>();
	}
	
	public void save() {
		Path staffPath = Paths.get("src/storage/Staff.txt");
		Path patientPath = Paths.get("src/storage/Patient.txt");
		Path labRequestPath = Paths.get("src/storage/LabRequest.txt");
		Path labExamPath = Paths.get("src/storage/LabExam.txt");
		Path logBookPath = Paths.get("src/storage/LogBook.txt");
		
		// Staff saving
	    try (BufferedWriter w = Files.newBufferedWriter(staffPath, 
	    		StandardOpenOption.CREATE,
	    		StandardOpenOption.TRUNCATE_EXISTING
	    )) {
	    	w.write(String.format("%d\n", Staff.lastId));
	    	for(Staff staff : staffs) {
	    		w.write("---\n");
	    		w.write(staff.getID() + "\n");
	    		w.write(staff.getName() + "\n");
	    		w.write(staff.getRole() + "\n");
	    		w.write(staff.getStatus() + "\n");
	    		w.write(staff.isOwned ? "true" : "false" + "\n");
	    	}
	    } catch (IOException e) {}
	    
	    // Patient saving
	    try (BufferedWriter w = Files.newBufferedWriter(patientPath, 
	    		StandardOpenOption.CREATE,
	    		StandardOpenOption.TRUNCATE_EXISTING
	    )) {
	    	w.write(String.format("%d\n", Patient.lastId));
	    	for(Patient patient: patients) {
	    		w.write("---\n");
	    		w.write(patient.getID() + "\n");
	    		w.write(patient.getName() + "\n");
	    		w.write(patient.getDob().toString() + "\n");
	    		w.write(patient.getSex() + "\n");
	    		w.write(patient.getNotes() + "\n");
	    		w.write(patient.isOwned ? "true" : "false" + "\n");
	    	}
	    } catch (IOException e) {}
	    
	    // Lab request saving
	    try (BufferedWriter w = Files.newBufferedWriter(labRequestPath, 
	    		StandardOpenOption.CREATE,
	    		StandardOpenOption.TRUNCATE_EXISTING
	    )) {
	    	w.write(String.format("%d\n", LabRequest.lastId));
	    	for(LabRequest request : labRequests) {
	    		w.write("---\n");
	    		w.write(request.getID() + "\n");
	    		w.write(request.getPatient().getID() + "\n");
	    		w.write(request.getRequest() + "\n");
	    		w.write(request.getStatus() + "\n");
	    		w.write(request.getStaff().getID() + "\n");
	    		w.write(request.isOwned ? "true" : "false" + "\n");
	    	}
	    } catch (IOException e) {}
	    
	    // Lab exam saving
	    try (BufferedWriter w = Files.newBufferedWriter(labExamPath, 
	    		StandardOpenOption.CREATE,
	    		StandardOpenOption.TRUNCATE_EXISTING
	    )) {
	    	w.write(String.format("%d\n", LabExam.lastId));
	    	for(LabExam exam : labExams) {
		    	w.write("---\n");
	    		w.write(exam.getID() + "\n");
	    		w.write(exam.getLabRequest().getID() + "\n");
	    		w.write(exam.getPerformingStaff().getID() + "\n");
	    		w.write(exam.getDate().toString() + "\n");
	    		w.write(exam.getStatus() + "\n");
	    		w.write(exam.getResultsAndRemarks() + "\n");
	    	}
	    } catch (IOException e) {}
	    
	    // Log book saving
	    try (BufferedWriter w = Files.newBufferedWriter(logBookPath, 
	    		StandardOpenOption.CREATE,
	    		StandardOpenOption.TRUNCATE_EXISTING
	    )) {
	    	for(LogBook log : logBooks) {
	    		w.write("---\n");
	    		w.write(log.getTimestamp().toString() + "\n");
	    		w.write(log.getAuthor() + "\n");
	    		w.write(log.getTag() + "\n");
	    		w.write(log.getMessage() + "\n");
	    	}
	    } catch (IOException e) {}
	}
	
	public void restore() {
		Path staffPath = Paths.get("src/storage/Staff.txt");
		Path patientPath = Paths.get("src/storage/Patient.txt");
		Path labRequestPath = Paths.get("src/storage/LabRequest.txt");
		Path labExamPath = Paths.get("src/storage/LabExam.txt");
		Path logBookPath = Paths.get("src/storage/LogBook.txt");
		
		// Temporary
		Map<String, Staff> staffMap = new HashMap<>();
		Map<String, Patient> patientMap = new HashMap<>();
		Map<String, LabRequest> labRequestMap = new HashMap<>();
		Map<String, LabExam> labExamMap = new HashMap<>();
		
		// Staff restore
	    try (BufferedReader r = Files.newBufferedReader(staffPath)) {
	    	Staff.lastId = Integer.parseInt(r.readLine()); // static id
	    	String line = r.readLine();
	    	while(line != null) {
	    		Staff staff = new Staff(this,
		                				r.readLine(), // ID
		                				r.readLine(), // Name
		                				r.readLine(), // Role
		                				r.readLine(), // Status
		                				r.readLine() == "true" ? true : false); //isOwned 
	    		this.addStaff(staff);
	    		staffMap.put(staff.getID(), staff);
	    		line = r.readLine();
	    	}
	    } catch (IOException e) {} 
	    catch (NumberFormatException e) {}
	    
	    // Patient restore
	    try (BufferedReader r = Files.newBufferedReader(patientPath)) {
	    	Patient.lastId = Integer.parseInt(r.readLine()); // static id
	    	String line = r.readLine();
	    	while(line != null) {
	    		Patient patient = new Patient(this,
		                					  r.readLine(), // ID
		                					  r.readLine(), // Name
		                					  LocalDate.parse(r.readLine()), // Dob
		                					  r.readLine(), // Sex
		                					  r.readLine(), // Notes
	    									  r.readLine() == "true" ? true : false); //isOwned 
	    		this.addPatient(patient);
	    		patientMap.put(patient.getID(), patient);
	    		line = r.readLine();
	    	}
	    } catch (IOException e) {} 
	    catch (NumberFormatException e) {}
	    
	    
	    // Lab request restore
	    try (BufferedReader r = Files.newBufferedReader(labRequestPath)) {
	    	LabRequest.lastId = Integer.parseInt(r.readLine()); // static id
	    	String line = r.readLine();
	    	while(line != null) {
	    		LabRequest labRequest = new LabRequest(this,
		                					  		   r.readLine(), // ID
		                					  		   patientMap.get(r.readLine()), // Patient
		                					  		   r.readLine(), // Request
		                					  		   r.readLine(), // Status
		                					  		   staffMap.get(r.readLine()), // Staff
	    											   r.readLine() == "true" ? true : false); //isOwned 
	    		this.addLabRequest(labRequest);
	    		labRequestMap.put(labRequest.getID(), labRequest);
	    		line = r.readLine();
	    	}
	    } catch (IOException e) {} 
	    catch (NumberFormatException e) {}
	    
	    // Lab exam restore
	    try (BufferedReader r = Files.newBufferedReader(labExamPath)) {
	    	LabExam.lastId = Integer.parseInt(r.readLine()); // static id
	    	String line = r.readLine();
	    	while(line != null) {
	    		LabExam labExam = new LabExam(this,
                				     		  r.readLine(), // ID
                				     		  labRequestMap.get(r.readLine()), // Lab Request
                				     		  staffMap.get(r.readLine()), // Performing Staff
                				     		  LocalDate.parse(r.readLine()), // Date
                				     		  r.readLine(), // Status
                				     		  r.readLine()); // Results and Remarks
	    		this.addLabExam(labExam);
	    		labExamMap.put(labExam.getID(), labExam);
	    		line = r.readLine();
	    	}
	    } catch (IOException e) {} 
	    catch (NumberFormatException e) {}
	    
	    // Log book restore
	    try (BufferedReader r = Files.newBufferedReader(logBookPath)) {
	    	String line = r.readLine();
	    	while(line != null) {
	    		LogBook logBook = new LogBook(LocalDateTime.parse(r.readLine()), // Timestamp
                				     		  r.readLine(), // Author
                				     		  r.readLine(), // Tag
                				     		  r.readLine()); // Message
	    		this.addLogBook(logBook);
	    		line = r.readLine();
	    	}
	    } catch (IOException e) {}
	} 
	
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
