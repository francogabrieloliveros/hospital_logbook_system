package application.extras;

import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class WelcomePage {
	private ExtraStage extraStage;
	
	public WelcomePage(ExtraStage extraStage) {
		this.extraStage = extraStage;
	}
	
	public Scene getScene() {
		Label welcomeMessage= new Label("Welcome, admin!");
	    
	    Button startButton = new Button("START");
	    startButton.getStyleClass().addAll("buttons", "page-button", "page-button-active");
	    startButton.setOnAction(e -> { extraStage.openMain(); });
	    
	    Button aboutButton = new Button("About");
	    aboutButton.getStyleClass().addAll("buttons", "page-button", "page-button-inactive");
	    aboutButton.setOnAction(e -> { extraStage.openAboutScene();});
	    
	    Button creditsButton = new Button("Credits");
	    creditsButton.getStyleClass().addAll("buttons", "page-button", "page-button-inactive");
	    creditsButton.setOnAction(e -> { extraStage.openCreditsScene();});
	    
	    VBox buttonsContainer = new VBox(20, startButton, aboutButton, creditsButton);
	    buttonsContainer.setAlignment(Pos.CENTER);
	    
	    VBox loginInput = new VBox(30, welcomeMessage, buttonsContainer);
	    HBox.setHgrow(loginInput, Priority.ALWAYS);
	    loginInput.getStyleClass().addAll("logger", "containers-shadow");
		
		HBox root = new HBox(loginInput);
		root.setPadding(new Insets(200, 80, 200, 620));
		root.getStyleClass().add("default-bg");
		
		Scene loginPageScene = new Scene(root, 1080, 720);
		loginPageScene.getStylesheets().add(getClass().getResource("/application/styles/extras.css").toExternalForm());
		
		return loginPageScene;
	}

}
