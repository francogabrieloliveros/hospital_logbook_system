package application.extras;

import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

public class CreditsPage {
	private ExtraStage extraStage;
	
	public CreditsPage(ExtraStage extraStage) {
		this.extraStage = extraStage;
	}
	
	public Scene getScene() {
		Button returnButton = new Button("");
	    returnButton.getStyleClass().add("return-button");
	    returnButton.setOnAction(e -> {extraStage.openWelcomeScene();});
	    
		Label name = new Label("Credits");
	    
		Text text = new Text("Sussy");
		
		VBox contents = new VBox(text);
	    
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
