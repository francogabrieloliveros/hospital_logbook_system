package application.models;

import java.time.LocalDate;

public class LabExam implements HospitalElement{
	private String id;
	private Hospital hospital;
	private LabRequest labRequest;
	private String testType;
	private LocalDate date;
	private String orderingPhysician;
	private Staff performingStaff;
	private String patient;
	private String status;
	private String result;
	private String remarks; 
	private static int lastId = 0;
	
	
	public LabExam(Hospital hospital, LabRequest labRequest, Staff performingStaff, LocalDate date, String status, String remarks) {
		this.id = this.generateId();
		this.hospital = hospital;
		this.labRequest = labRequest;
		this.testType = labRequest.getRequest();
		this.date = date;
		this.orderingPhysician = labRequest.getStaff();
		this.performingStaff = performingStaff;
		this.patient = labRequest.getPatient();
		this.status = status;
		this.result = null;
		this.remarks = remarks;
		this.addLogToHospital("Added new lab exam");
	}
	void updateResults(String result, String status, String remarks) {
		this.result = result;
		this.status = status;
		this.remarks = remarks;
		this.addLogToHospital("Result finalized");
	}
	
	
	public String trackStatus(String test) {
		return this.status;
	}
	
	@Override
	public String toString() {
		return String.format("%s | testType=%s | orderingPhysician=%s | performingStaff=%s | status=%s", this.id, this.testType, this.orderingPhysician, this.performingStaff, this.status);
	}
	
	@Override
	public void addLogToHospital(String message) {
		this.hospital.addLogBook(new LogBook("", "staff", message));
	}

	@Override
	public String generateId() {
		String id = String.format("%04d", LabExam.lastId++);
		return "LBE-" + id;
	}
	
	//getters
	public Staff getPerformingStaff() {return performingStaff;}
	public LabRequest getLabRequest() {return labRequest;}
	public LocalDate getDate() {return date;}
	public String getStatus() {return status;}
	public String getResults() {return result;}
	public String getRemarks() {return remarks;}
	
}
