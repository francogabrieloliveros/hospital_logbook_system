package application.extras;

import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class AboutPage {
	private ExtraStage extraStage;
	
	public AboutPage(ExtraStage extraStage) {
		this.extraStage = extraStage;
	}
	
	public Scene getScene() {
		Button returnButton = new Button("←");
	    returnButton.getStyleClass().add("return-button");
	    returnButton.setOnAction(e -> {extraStage.openWelcomeScene();});
	    
		Label name = new Label("About");
	    
		Label titleHeader = new Label("Hospital Laboratory Digital Logbook");
		titleHeader.getStyleClass().add("header-text");
		
		Label titleParagraph = new Label("The Hospital Laboratory Digital Logbook is a JavaFX- based system "
				+ "created to aid hospital laboratories in organizing and managing their daily operations. "
				+ "It provides a structured way to record staff, patients, laboratory exams, and requests "
				+ "while maintaining a complete audit trail for transparency and accountability."); 
		titleParagraph.getStyleClass().add("paragraph-text");
		titleParagraph.setWrapText(true);	
		
		Label purposeHeader = new Label("Purpose");
		purposeHeader.getStyleClass().add("header-text");
		
		Label purposeParagraph = new Label("This system helps laboratory personnel track activities more efficiently, "
				+ "reduce manual errors, and maintain accurate, timestamped records of all actions performed."); 
		purposeParagraph.getStyleClass().add("paragraph-text");
		purposeParagraph.setWrapText(true);
		
		Label technologiesHeader = new Label("Technologies");
		technologiesHeader.getStyleClass().add("header-text");
		
		Label technologiesParagraph = new Label("Java 21, JavaFX21, Plain text file storage (TXT/CSV), CSS for ui styling."); 
		technologiesParagraph.getStyleClass().add("paragraph-text");
		technologiesParagraph.setWrapText(true);
		
		VBox contents = new VBox(20, titleHeader, 
				                     titleParagraph, 
				                     purposeHeader, 
				                     purposeParagraph, 
				                     technologiesHeader, 
				                     technologiesParagraph);
	    
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
