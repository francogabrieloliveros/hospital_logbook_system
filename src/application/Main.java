package application;
	
import application.pages.LogBookViewPage;
import javafx.application.Application;
import javafx.scene.text.Font;
import javafx.stage.Stage;


public class Main extends Application {
	@Override
	public void start(Stage stage) {
		Font.loadFont(getClass().getResourceAsStream("/fonts/Roboto-Regular.ttf"), 14);
		Font.loadFont(getClass().getResourceAsStream("/fonts/Roboto-Bold.ttf"), 14);
		Font.loadFont(getClass().getResourceAsStream("/fonts/Roboto-SemiBold.ttf"), 14);
		//StaffPage staffPage = new StaffPage();
		//staffPage.setStageComponents(stage);
		stage.setResizable(false);
		LogBookViewPage logbook = new LogBookViewPage();
		logbook.setStageComponents(stage);
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
