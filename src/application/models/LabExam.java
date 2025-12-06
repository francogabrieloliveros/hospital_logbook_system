package application.models;

public class LabExam implements HospitalElement{
	
	public static int lastId = 0;
	
	private Hospital hospital;
	private String id;
	private String testType;
	private Patient patient;
	private Staff orderingPhysician;
	private Staff performingStaff;
	private String status;
	private LabRequest labRequest;
	
	public LabExam (Hospital hospital,
					LabRequest labRequest,
			        Staff performingStaff, 
			        String status) {
		
		this.hospital = hospital;
		this.labRequest = labRequest;
		this.testType = labRequest.getRequest();
		this.patient = labRequest.getPatient();
		this.orderingPhysician = labRequest.getStaff();
		this.performingStaff = performingStaff;
		this.status = status;
		
		this.patient.addLabExam(this);
		
		this.id = generateId();
		addLogToHospital("Added new lab exam");
	}
	
	public void update (Hospital hospital,
						Patient patient,
						LabRequest labRequest,
						Staff performingStaff, 
						String status) {

		this.hospital = hospital;
		this.labRequest = labRequest;
		this.testType = labRequest.getRequest();
		this.patient = labRequest.getPatient();
		this.orderingPhysician = labRequest.getStaff();
		this.performingStaff = performingStaff;
		this.status = status;
		
		addLogToHospital("Updated lab exam information");
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
		hospital.addLogBook(new LogBook("", "labexam", message));
	}
	
	@Override
	public String generateId() {
		LabExam.lastId++;
		String idNumber = String.format("%04d", LabExam.lastId);
		
		return "LBE-" + idNumber;
	}
	
	// Getters
	public String getTestType() { return this.testType; }
	public Staff getOrderingPhysician() { return this.orderingPhysician; }
	public Staff getPerformingStaff() { return this.performingStaff; }
	public String getStatus() { return this.status; }
}
