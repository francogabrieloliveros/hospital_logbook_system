package application;

import application.models.*;
import application.pages.*;

import java.time.LocalDate;

import application.extras.*;
import javafx.application.Application;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {
	
	//	moved object declarations above here
	private Hospital hospital = new Hospital();
	private StaffPage staffPage = new StaffPage(hospital);
	private PatientsPage patients= new PatientsPage(hospital);
	private LabExamsPage labExams = new LabExamsPage(hospital);
	private LogBookViewPage logbook= new LogBookViewPage(hospital);
	private LabRequestsPage labRequests = new LabRequestsPage(hospital);
	private ExtraStage extraStage = new ExtraStage(this);
	private Stage primaryStage;
	
	@Override
	public void start(Stage stage) {
		this.primaryStage = stage;

		stage.setResizable(false); // Make stage unresizable
		
		// load fonts
		Font.loadFont(getClass().getResourceAsStream("/fonts/Roboto-Regular.ttf"), 14);
		Font.loadFont(getClass().getResourceAsStream("/fonts/Roboto-Bold.ttf"), 14);
		Font.loadFont(getClass().getResourceAsStream("/fonts/Roboto-SemiBold.ttf"), 14);
		seedHospitalData(hospital);
		extraStage.start(); // Open intro stage first
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	//	method that calls setStageComponents given a string pageName
	//	so we are able to switch scenes
	public void switchPage(String pageName) {
		if (pageName.equals("STAFF")) {
			staffPage.setStageComponents(primaryStage, this);
		} else if (pageName.equals("LOGBOOK")) {
			logbook.setStageComponents(primaryStage, this);
		} else if (pageName.equals("LAB EXAMS")) {
			labExams.setStageComponents(primaryStage, this);
		} else if (pageName.equals("LAB REQUESTS")) {
			labRequests.setStageComponents(primaryStage, this);
		} else if (pageName.equals("PATIENTS")) {
			patients.setStageComponents(primaryStage, this);
		} 
	}
	
	private void seedHospitalData(Hospital hospital) {

	    // -------------------------
	    // 1. STAFF
	    // -------------------------
	    Staff medTech1 = new Staff(hospital, "Melinda Cruz", "MedTech", "active");
	    Staff medTech2 = new Staff(hospital, "Arvin Reyes", "MedTech", "active");

	    Staff pathologist1 = new Staff(hospital, "Dr. Sunga", "Pathologist", "active");

	    Staff phleb1 = new Staff(hospital, "Jake Dela Rosa", "Phlebotomist", "active");
	    Staff phleb2 = new Staff(hospital, "Ana Marie Lopez", "Phlebotomist", "active");

	    Staff radtech = new Staff(hospital, "Ramon Tan", "RadTech", "active");

	    Staff physician1 = new Staff(hospital, "Dr. Evans", "Physician", "active");
	    Staff physician2 = new Staff(hospital, "Dr. Morales", "Physician", "active");

	    hospital.addStaff(medTech1);
	    hospital.addStaff(medTech2);
	    hospital.addStaff(pathologist1);
	    hospital.addStaff(phleb1);
	    hospital.addStaff(phleb2);
	    hospital.addStaff(radtech);
	    hospital.addStaff(physician1);
	    hospital.addStaff(physician2);


	    // -------------------------
	    // 2. PATIENTS
	    // -------------------------
	    Patient p1 = new Patient(hospital, "Alice Johnson", LocalDate.of(1985, 10, 20), "Female", "Diabetic");
	    Patient p2 = new Patient(hospital, "Robert Lee", LocalDate.of(1972, 5, 15), "Male", "High BP");
	    Patient p3 = new Patient(hospital, "Maria Santos", LocalDate.of(1999, 2, 17), "Female", "Routine check");
	    Patient p4 = new Patient(hospital, "Juan Dela Cruz", LocalDate.of(2001, 11, 10), "Male", "-");
	    Patient p5 = new Patient(hospital, "Sarah Kim", LocalDate.of(1990, 3, 30), "Female", "Pregnant");
	    Patient p6 = new Patient(hospital, "Michael Reyes", LocalDate.of(1981, 7, 5), "Male", "-");
	    Patient p7 = new Patient(hospital, "Rhea Gonzales", LocalDate.of(2004, 4, 1), "Female", "-");
	    Patient p8 = new Patient(hospital, "Leo Francisco", LocalDate.of(1994, 1, 20), "Male", "-");
	    Patient p9 = new Patient(hospital, "Kimberly Cruz", LocalDate.of(1988, 6, 12), "Female", "-");
	    Patient p10 = new Patient(hospital, "Kevin Tan", LocalDate.of(1977, 12, 25), "Male", "-");


	    hospital.addPatient(p1);
	    hospital.addPatient(p2);
	    hospital.addPatient(p3);
	    hospital.addPatient(p4);
	    hospital.addPatient(p5);
	    hospital.addPatient(p6);
	    hospital.addPatient(p7);
	    hospital.addPatient(p8);
	    hospital.addPatient(p9);
	    hospital.addPatient(p10);


	    // -------------------------
	    // 3. LAB REQUESTS
	    // -------------------------
	    LabRequest r1 = new LabRequest(hospital, p1, "Lipid Profile", "new", physician1);
	    LabRequest r2 = new LabRequest(hospital, p2, "CBC", "in progress", physician1);
	    LabRequest r3 = new LabRequest(hospital, p3, "Urinalysis", "new", physician2);
	    LabRequest r4 = new LabRequest(hospital, p4, "FBS", "new", physician2);
	    LabRequest r5 = new LabRequest(hospital, p5, "PCR", "new", physician1);
	    LabRequest r6 = new LabRequest(hospital, p6, "X-RAY", "new", physician2);
	    LabRequest r7 = new LabRequest(hospital, p7, "CBC", "done", physician1);
	    LabRequest r8 = new LabRequest(hospital, p8, "Urinalysis", "new", physician1);
	    LabRequest r9 = new LabRequest(hospital, p9, "Lipid Profile", "in progress", physician2);
	    LabRequest r10 = new LabRequest(hospital, p10, "PCR", "new", physician1);

	    hospital.addLabRequest(r1);
	    hospital.addLabRequest(r2);
	    hospital.addLabRequest(r3);
	    hospital.addLabRequest(r4);
	    hospital.addLabRequest(r5);
	    hospital.addLabRequest(r6);
	    hospital.addLabRequest(r7);
	    hospital.addLabRequest(r8);
	    hospital.addLabRequest(r9);
	    hospital.addLabRequest(r10);


	    // -------------------------
	    // 4. LAB EXAMS (for testing listView + editing logic)
	    // -------------------------
	    LabExam e1 = new LabExam(hospital, r2, medTech1, LocalDate.now().minusDays(1), "In-Progress", "Processing", ""); 
	    LabExam e2 = new LabExam(hospital, r7, medTech2, LocalDate.now().minusDays(2), "Done", "Normal results", ""); 
	    LabExam e3 = new LabExam(hospital, r8, pathologist1, LocalDate.now(), "Cancelled", "", "Sample contaminated"); 
	    LabExam e4 = new LabExam(hospital, r9, phleb1, LocalDate.now(), "In-Progress", "Initial sample ok", ""); 
	    LabExam e5 = new LabExam(hospital, r6, radtech, LocalDate.now(), "Done", "Clear X-ray", ""); 

	    hospital.addLabExam(e1);
	    hospital.addLabExam(e2);
	    hospital.addLabExam(e3);
	    hospital.addLabExam(e4);
	    hospital.addLabExam(e5);
	}
}
