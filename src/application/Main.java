package application;
	
import application.pages.LogBookViewPage;
import application.pages.StaffPage;
import application.pages.LabExams;
import application.pages.Patients;
import javafx.application.Application;
import javafx.scene.text.Font;
import javafx.stage.Stage;


public class Main extends Application {
	
	//	moved object declarations above here
	private StaffPage staffPage;
	
	private LogBookViewPage logbook;
	private Patients patients;
	private LabExams labExams;
	private Stage primaryStage;
	
	@Override
	public void start(Stage stage) {
		Font.loadFont(getClass().getResourceAsStream("/fonts/Roboto-Regular.ttf"), 14);
		Font.loadFont(getClass().getResourceAsStream("/fonts/Roboto-Bold.ttf"), 14);
		Font.loadFont(getClass().getResourceAsStream("/fonts/Roboto-SemiBold.ttf"), 14);
		
		this.primaryStage = stage;
		stage.setResizable(false);
		
		//	instantiate objects
		staffPage = new StaffPage();
		patients = new Patients();
		labExams = new LabExams();
		logbook = new LogBookViewPage();
		
		switchPage("STAFF");
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
		} else if (pageName.equals("PATIENTS")) {
			patients.setStageComponents(primaryStage, this);
		}
	}
}
