package application.models;

public class LabRequest implements HospitalElement{
	
	public static int lastId = 0;
	
	private Hospital hospital;
	private Patient patient;
	private String request;
	private String status;
	private Staff orderingStaff;
	private String id;
	
	public LabRequest(Hospital hospital, 
			          Patient patient, 
			          String request, 
			          String status, 
			          Staff orderingStaff) {
		this.hospital = hospital;
		this.patient = patient;
		this.request = request;
		this.status = status;
		this.orderingStaff = orderingStaff;
		this.id = generateId();
		
		hospital.addLabRequest(this); // add lab request to hospital
		addLogToHospital(String.format("Added new lab request for patient %s", 
				                        patient.getName()));
	}

	public void update(Patient patient, 
      		   		   String request, 
      		   		   String status, 
      		   		   Staff orderingStaff) {
		this.patient = patient;
		this.request = request;
		this.status = status;
		this.orderingStaff = orderingStaff;
	
		addLogToHospital(String.format("Updated %s information", id));
	}
	
	public void delete() {
		hospital.removeLabRequest(this);
		addLogToHospital(String.format("Deleted %s from labrequests", id));
	}
	
	@Override
	public String toString() {
		return String.format("%s | testType=%s | patient=%s | physician=%s | status=%s", 
				              id, request, patient.getName(), orderingStaff.getName(), status);
	}
	
	@Override
	public void addLogToHospital(String message) {
		hospital.addLogBook(new LogBook("", "labrequest", message));
	}
	
	@Override
	public String generateId() {
		return "LBR-" + String.format("%04d", LabRequest.lastId++);
	}
	
	// Getters
	public String getID() { return this.id; }
	public Patient getPatient() { return this.patient; }
	public String getStatus() { return this.status; }
	public Staff getStaff() { return this.orderingStaff; }
	public String getRequest() { return this.request; }
}
