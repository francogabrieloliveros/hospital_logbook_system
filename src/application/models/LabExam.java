package application.models;

import java.time.LocalDate;

public class LabExam implements HospitalElement{
	
	private static int lastId = 0;
	
	private Hospital hospital;
	private String id;
	private LabRequest labRequest;
	private String testType;
	private LocalDate date;
	private Staff orderingPhysician;
	private Staff performingStaff;
	private Patient patient;
	private String status;
	private String resultsAndRemarks;
	
	public LabExam(Hospital hospital, 
			       LabRequest labRequest, 
			       Staff performingStaff, 
			       LocalDate date, 
			       String status, 
			       String resultAndRemarks) {
		
		this.hospital = hospital;
		this.labRequest = labRequest;
		this.testType = labRequest.getRequest();
		this.date = date;
		this.orderingPhysician = labRequest.getStaff();
		this.performingStaff = performingStaff;
		this.patient = labRequest.getPatient();
		this.status = status;
		this.resultsAndRemarks = resultAndRemarks;
		this.id = this.generateId();
		
		hospital.addLabExam(this);
		this.addLogToHospital("Added new lab exam");
	}
	
	public void update(LabRequest labRequest, 
		       		   Staff performingStaff, 
		       		   LocalDate date, 
		       		   String status, 
		       		   String resultAndRemarks) {
		
		this.labRequest = labRequest;
		this.testType = labRequest.getRequest();
		this.date = date;
		this.orderingPhysician = labRequest.getStaff();
		this.performingStaff = performingStaff;
		this.patient = labRequest.getPatient();
		this.status = status;
		this.resultsAndRemarks = resultAndRemarks;
		
		this.addLogToHospital(String.format("Updated %s information", id));
	}
	
	public void delete() {
		hospital.removeLabExam(this);
		this.addLogToHospital(String.format("Deleted %s from labexams", id));
	}
	
	@Override
	public String toString() {
		return String.format("%s | testType=%s| patient=%s | orderingPhysician=%s | performingStaff=%s | status=%s",
                             id, 
                             testType, 
                             patient.getName(),
                             orderingPhysician.getName(), 
                             performingStaff.getName(),
                             status);
	}
	
	@Override
	public void addLogToHospital(String message) {
		this.hospital.addLogBook(new LogBook("", "labexam", message));
	}

	@Override
	public String generateId() {
		return "LBE-" + String.format("%04d", LabExam.lastId++);
	}
	
	//getters
	public Staff getPerformingStaff() {return performingStaff;}
	public LabRequest getLabRequest() {return labRequest;}
	public LocalDate getDate() {return date;}
	public String getStatus() {return status;}
	public String getResultsAndRemarks() {return resultsAndRemarks;}
}
