/*
 * The Hospital Laboratory Digital Logbook is a JavaFX- based system
 * created to aid hospital laboratories in organizing and managing their daily operations.
 * It provides a structured way to record staff, patients, laboratory exams, and requests
 * while maintaining a complete audit trail for transparency and accountability.
 *
 * @author Evan C. Gregorio
 * @author Kurt Dylan M. Larano
 * @author Franco Gabriel P. Oliveros
 * @author Tuazon M. Thea
 * 
 * @created_date 2025-11-20 19:50
 *   
 * */

package application;

import application.models.*;
import application.pages.*;

import application.extras.*;
import javafx.application.Application;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {
	
    // Declare fields without initializing the pages yet,
    // to prevent them from referencing an empty Hospital object.
	private Hospital hospital;
	private StaffPage staffPage;
	private PatientsPage patients;
	private LabExamsPage labExams;
	private LogBookViewPage logbook;
	private LabRequestsPage labRequests;
	private Dashboard dashboard;
    
	private ExtraStage extraStage;
	private Stage primaryStage;
	
	@Override
	public void start(Stage stage) {
		this.primaryStage = stage;
		stage.setResizable(false); // Make stage unresizable
		
		// load fonts
		Font.loadFont(getClass().getResourceAsStream("/fonts/Roboto-Regular.ttf"), 14);
		Font.loadFont(getClass().getResourceAsStream("/fonts/Roboto-Bold.ttf"), 14);
		Font.loadFont(getClass().getResourceAsStream("/fonts/Roboto-SemiBold.ttf"), 14);

        // 1. INITIALIZE and SEED the Hospital object first
		this.hospital = new Hospital();
		
		hospital.restore();
		
		stage.setOnCloseRequest(e -> {
			hospital.save();
		});
        
        // 2. INITIALIZE all Page objects using the NOW-POPULATED Hospital
        this.staffPage = new StaffPage(hospital);
        this.patients = new PatientsPage(hospital);
        this.labExams = new LabExamsPage(hospital);
        this.logbook = new LogBookViewPage(hospital);
        this.labRequests = new LabRequestsPage(hospital);
        this.dashboard = new Dashboard(hospital);
        this.extraStage = new ExtraStage(this); // extraStage needs 'this' (Main)
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
		} else if (pageName.equals("DASHBOARD")) {
			dashboard.setStageComponents(primaryStage, this);
		} 
	}
}