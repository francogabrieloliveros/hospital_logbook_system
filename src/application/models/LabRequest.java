package application.models;

public class LabRequest implements HospitalElement{
	private Hospital hospital;
	private Patient patient;
	private String request;
	private String status;
	private Staff staff;
	private String id;
	private static int counter = 0;
	
	public LabRequest(Hospital hospital, Patient patient, String request, String status, Staff staff) {
		this.hospital = hospital;
		this.patient = patient;
		this.request = request;
		this.status = status;
		this.staff = staff;
		this.id = generateId();
		LabRequest.counter++;
	}

	@Override
	public void addLogToHospital(String message) {
		this.hospital.addLogBook(new LogBook("", "staff", message));
	}

	@Override
	public String toString() {
		return String.format("%s | testType=%s | patient=%s | physician=%s | status=%s", this.id, this.request, this.patient.getName(), this.staff.getName(), this.status);
	}

	@Override
	public String generateId() {
		String id = String.format("%04d", LabRequest.counter++);
		return "LBR-" + id;
	}
	
	public void update(String status) {
		this.status = status;
		addLogToHospital("Updated staff information");
	}
	
	// Getters
	public String getID() { return this.id; }
	public String getPatient() { return this.patient.getName(); }
	public String getStatus() { return this.status; }
	public String getStaff() { return this.staff.getName(); }
	public String getRequest() { return this.request; }
}
