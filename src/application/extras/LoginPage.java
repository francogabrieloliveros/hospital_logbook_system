package application.extras;

import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.control.Alert.AlertType;

public class LoginPage {
	private String username = "admin";
	private String password = "1234";
	private ExtraStage extraStage;
	
	public LoginPage(ExtraStage extraStage) {
		this.extraStage = extraStage;
	}
	
	public Scene getScene() {
		Label name = new Label("Log in");
	    
		TextField usernameField = new TextField();
	    usernameField.setPromptText("Username");
	    
	    PasswordField passwordField = new PasswordField();
	    passwordField.setPromptText("Password");
	    
	    Button loginButton = new Button("Log in");
	    loginButton.getStyleClass().addAll("buttons", "page-button", "page-button-active");
	    loginButton.setDefaultButton(true);
	    VBox loginButtonContainer = new VBox(loginButton);
	    loginButtonContainer.setAlignment(Pos.CENTER);
	    
	    loginButton.setOnAction(e -> {
	    	login(usernameField.getText(), passwordField.getText());
	    });
	    
	    VBox loginInput = new VBox(30, name, usernameField, passwordField, loginButtonContainer);
	    HBox.setHgrow(loginInput, Priority.ALWAYS);
	    loginInput.getStyleClass().addAll("logger", "containers-shadow");
		
		HBox root = new HBox(loginInput);
		root.setPadding(new Insets(200, 80, 200, 620));
		root.getStyleClass().add("default-bg");
		
		Scene loginPageScene = new Scene(root, 1080, 720);
		loginPageScene.getStylesheets().add(getClass().getResource("/application/styles/extras.css").toExternalForm());
		
		return loginPageScene;
	}
	
	private void login(String username, String password) {
		if (username.equals(this.username) && password.equals(this.password)) {
			extraStage.openWelcomeScene();
		} else {
			Alert error = new Alert(AlertType.ERROR);
	        error.setHeaderText("Error");
	        error.setContentText("Incorrect username or password.");
	        
	        error.show();
		}
	}
	
}
