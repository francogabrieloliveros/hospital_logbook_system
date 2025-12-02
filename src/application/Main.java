package application;

import application.models.*;
import application.pages.*;
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
}
