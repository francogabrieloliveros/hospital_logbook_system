package application.models;

public class Staff implements HospitalElement{
	
	public static int lastId = 0;
	
	private Hospital hospital;
	private String id;
	private String name;
	private String role;
	private String status;
	
	public Staff (Hospital hospital, String name, String role, String status) {
		this.hospital = hospital;
		this.name = name;
		this.role = role;
		this.status = status;
		this.id = generateId();
		
		hospital.addStaff(this);
		addLogToHospital(String.format("Added new staff %s", name));
	}
	
	// Update information
	public void update(String name, String role, String status) {
		this.name = name;
		this.role = role;
		this.status = status;
		
		addLogToHospital(String.format("Updated %s information", id));
	}
	
	// Remove class from hospital
	public void delete() {
		addLogToHospital(String.format("Deleted %s from staffs", id));
		hospital.removeStaff(this);
	}
	
	// HospitalElement functions
	@Override
	public String toString() {
		return String.format("%s | fullName=%s | role=%s | status=%s",
                              id, name, role, status);
	}
	
	@Override
	public void addLogToHospital(String message) {
		hospital.addLogBook(new LogBook("", "staff", message));
	}
	
	@Override
	public String generateId() {
		return "STF-" + String.format("%04d", Staff.lastId++);
	}
	
	// Setters
	public void setStatus(String status) {this.status = status;}
	// Getters
	public String getName() { return this.name; }
	public String getRole() { return this.role; }
	public String getStatus() { return this.status; }
	public String getID() {return this.id;}
}
