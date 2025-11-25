package application;
	
import application.pages.*;
import application.extras.*;
import javafx.application.Application;
import javafx.scene.text.Font;
import javafx.stage.Stage;


public class Main extends Application {
	
	//	moved object declarations above here
	private StaffPage staffPage;
	private LogBookViewPage logbook;
	private PatientsPage patients;
	private LabExamsPage labExams;
	private LabRequestsPage labRequests;
	private Stage primaryStage;
	private ExtraStage extraStage;
	
	@Override
	public void start(Stage stage) {
		Font.loadFont(getClass().getResourceAsStream("/fonts/Roboto-Regular.ttf"), 14);
		Font.loadFont(getClass().getResourceAsStream("/fonts/Roboto-Bold.ttf"), 14);
		Font.loadFont(getClass().getResourceAsStream("/fonts/Roboto-SemiBold.ttf"), 14);
		
		extraStage = new ExtraStage(this);
		stage.setResizable(false);
		extraStage.start();
		
		this.primaryStage = stage;
		stage.setResizable(false);
		
		//	instantiate objects
		staffPage = new StaffPage();
		patients = new PatientsPage();
		labExams = new LabExamsPage();
		logbook = new LogBookViewPage();
		labRequests = new LabRequestsPage();
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
