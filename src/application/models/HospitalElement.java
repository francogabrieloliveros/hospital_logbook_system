package application.models;

public interface HospitalElement {
	
	// Returns a string containing info of the element for viewing
	// Automatically used by listview
	// Staff Example: "STF-0001 | fullName=Evan | role=Phlebotomist | status=active"
	// Patient Example: "PAT-0001 | fullName=Kurt | dob=1565-12-25 | info=gastrointestinal inflammation"
	// LabRequest Example: "LBR-0001 | testType=CBC| patient=Thea  | physician=Evan | status=done"
	// LabExam Example: "LBE-0001 | testType=X-ray| orderingPhysician=Evan | performingStaff=Thea | status=completed"
	public String toString();
	

	// Creates a new LogBook element containing details
	// Access the hospital the element is registered to and add them to LogBook array list
	// Use the function in the constructor function of all elements
	public void addLogToHospital(String message);
	
	// Returns a String ID for the element
	// Access the static ID of each class, then add 1
	// Place the number in the element code (STF, PAT, LBR, LBE) followed by a dash
	// Use four digit numbering (Ex.: 0001, 0023, 0345, 5432)
	public String generateId();
}
