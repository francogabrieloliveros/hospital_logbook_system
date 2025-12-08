package application.extras;

import application.Main;

import javafx.scene.text.Font;
import javafx.stage.Stage;

public class ExtraStage {
	
	//	moved object declarations above here
	private AboutPage about;
	private CreditsPage credits;
	private LoginPage login;
	private WelcomePage welcome;
	private Stage stage;
	private Main main;
	
	public ExtraStage (Main main) {
		this.main = main;
	}
	
	public void start() {
		Font.loadFont(getClass().getResourceAsStream("/fonts/Roboto-Regular.ttf"), 14);
		Font.loadFont(getClass().getResourceAsStream("/fonts/Roboto-Bold.ttf"), 14);
		Font.loadFont(getClass().getResourceAsStream("/fonts/Roboto-SemiBold.ttf"), 14);
		
		stage = new Stage();
		stage.setResizable(false);
		
		//	instantiate objects
		about = new AboutPage(this);
		credits = new CreditsPage(this);
		login = new LoginPage(this);
		welcome = new WelcomePage(this);
		
		stage.setScene(login.getScene());
		stage.setTitle("Login");
		stage.show();
	}
	
	public void openWelcomeScene() { 
		stage.setScene(welcome.getScene());
		stage.setTitle("Welcome");
	}
	public void openCreditsScene() { 
		stage.setScene(credits.getScene());
		stage.setTitle("Credits");
	}
	public void openAboutScene() { 
		stage.setScene(about.getScene());
		stage.setTitle("About");
	}
	
	public void openMain() { 
		main.switchPage("STAFF");
		this.stage.close();
	};

}
