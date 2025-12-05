package application.models;

public class LabRequest implements HospitalElement{
	
	public static int lastId = 0;
	
	private Hospital hospital;
	private Patient patient;
	private String request;
	private String status;
	private Staff staff;
	private String id;
	
	public LabRequest(Hospital hospital, Patient patient, String request, String status, Staff staff) {
		this.hospital = hospital;
		this.patient = patient;
		this.request = request;
		this.status = status;
		this.staff = staff;
		this.id = generateId();
		
		addLogToHospital("Added new lab request");
	}

	@Override
	public String toString() {
		return String.format("%s | testType=%s | patient=%s | physician=%s | status=%s", this.id, this.request, this.patient.getName(), this.staff.getName(), this.status);
	}
	
	@Override
	public void addLogToHospital(String message) {
		hospital.addLogBook(new LogBook("", "labrequest", message));
	}
	
	@Override
	public String generateId() {
		LabRequest.lastId++;
		String idNumber = String.format("%04d", Staff.lastId);
		
		return "LBR-" + idNumber;
	}
	
	public void update(String status) {
		this.status = status;
		addLogToHospital("Updated lab request information");
	}
	
	public void delete() {
		addLogToHospital("Deleted lab request information");
	}
	
	// Getters
	public String getID() { return this.id; }
	public Patient getPatient() { return this.patient; }
	public String getStatus() { return this.status; }
	public Staff getStaff() { return this.staff; }
	public String getRequest() { return this.request; }
}
