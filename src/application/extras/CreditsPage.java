package application.extras;

import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class CreditsPage {
	private ExtraStage extraStage;
	
	public CreditsPage(ExtraStage extraStage) {
		this.extraStage = extraStage;
	}
	
	public Scene getScene() {
		Button returnButton = new Button("←");
	    returnButton.getStyleClass().add("return-button");
	    returnButton.setOnAction(e -> {extraStage.openWelcomeScene();});
	    
		Label name = new Label("Credits");
	    
		Label developersHeader = new Label("Developers");
		developersHeader.getStyleClass().add("header-text");
		
		Label developersParagraph = new Label("""
				Evan C. Gregorio 
				Franco Gabriel P. Oliveros
				Kurt Dylan M. Laraño
				Tuazon M. Thea
				"""); 
		developersParagraph.getStyleClass().add("paragraph-text");
		
		Label courseHeader = new Label("Course Information");
		courseHeader.getStyleClass().add("header-text");
		
		Label courseParagraph = new Label("""
				CMSC22 – Object-Oriented Programming
				University of the Philippines Los Baños
				Academic Year 2025
				"""); 
		courseParagraph.getStyleClass().add("paragraph-text");
		
		Label assetsHeader = new Label("Assets & References");
		assetsHeader.getStyleClass().add("header-text");
		
		Label assetsParagraph = new Label("""
				Roboto Font – Google Fonts
				JavaFX Documentation – openjfx.io
				Backgrounds - canva.com
				"""); 
		assetsParagraph.getStyleClass().add("paragraph-text");
		
		VBox contents = new VBox(20, developersHeader, 
				                     developersParagraph, 
				                     courseHeader, 
				                     courseParagraph, 
				                     assetsHeader,
				                     assetsParagraph);
	    
	    VBox loginInput = new VBox(10, returnButton, name, contents);
	    HBox.setHgrow(loginInput, Priority.ALWAYS);
	    loginInput.getStyleClass().addAll("logger", "containers-shadow");
		
		HBox root = new HBox(loginInput);
		root.setPadding(new Insets(50, 50, 50, 570));
		root.getStyleClass().add("default-bg");
		
		Scene loginPageScene = new Scene(root, 1080, 720);
		loginPageScene.getStylesheets().add(getClass().getResource("/application/styles/extras.css").toExternalForm());
		
		return loginPageScene;
	}
}
