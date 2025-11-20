package application;
	
import application.pages.*;
import javafx.application.Application;
import javafx.scene.text.Font;
import javafx.stage.Stage;


public class Main extends Application {
	@Override
	public void start(Stage stage) {
		Font.loadFont(getClass().getResourceAsStream("/fonts/Roboto-Regular.ttf"), 14);
		LabExams labExams = new LabExams();
		labExams.setStageComponents(stage);
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
