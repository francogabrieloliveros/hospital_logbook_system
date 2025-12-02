package application.pages;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.*;

import application.Main;

public class StaffPage {

	// 	NOTE:	Main is added as a parameter in setStageComponents so we 
	//			have a reference when main is called (for the buttons to work)
	public void setStageComponents(Stage stage, Main main) {
		String[] labels = {"STAFF", "PATIENTS", "LAB EXAMS", "LAB REQUESTS", "LOGBOOK"};
		ArrayList<Button> labelButtons = new ArrayList<>();
		for(String label : labels) {
			Button newButton = new Button(label);
			
			if(label.equals("STAFF")) {
				newButton.getStyleClass().addAll("page-button-active", "page-button"); 
			} else {
				newButton.getStyleClass().addAll("page-button-inactive", "page-button"); // added functionality (change pages)
				newButton.setOnAction(e -> main.switchPage(label));
			}
			
			labelButtons.add(newButton);
		}
		
		HBox pageButtons = new HBox(10);
		pageButtons.getChildren().addAll(labelButtons);
		HBox.setMargin(pageButtons, new Insets(20));
		
		ListView<String> listView = new ListView<>();
		listView.getItems().add("STF-0001 | fullName=Mylene | role=MedTe");
		listView.getStyleClass().add("list-view");
		listView.getStyleClass().add("containers-shadow");
		
	    Label name = new Label("Name");
	    TextField nameField = new TextField();
	    nameField.setPromptText("Full name");
	    VBox nameInput = new VBox(20, name, nameField);
	    
	    Label role = new Label("Role");
	    ComboBox<String> roleField = new ComboBox<>();
	    roleField.getItems().addAll("MedTech", "Pathologist", "Phlebotomist", "Clerk", "Other");
	    roleField.setValue("MedTech");
	    VBox roleInput = new VBox(20, role, roleField);
	    roleField.setMaxWidth(Double.MAX_VALUE);
	    
	    Label status = new Label("Status");
	    ComboBox<String> statusField = new ComboBox<>();
	    statusField.getItems().addAll("active", "inactive");
	    statusField.setValue("active");
	    VBox statusInput = new VBox(20, status, statusField);
	    statusField.setMaxWidth(Double.MAX_VALUE);
	    
	    
	    HBox roleAndStatus = new HBox(20, roleInput, statusInput);
	    HBox.setHgrow(roleInput, Priority.ALWAYS);
	    HBox.setHgrow(statusInput, Priority.ALWAYS);
	    
	    Button addButton = new Button("Add");
	    addButton.getStyleClass().addAll("page-button-active", "page-button");
	    Button updateButton = new Button("Update");
	    updateButton.getStyleClass().addAll("page-button-active", "page-button");
	    Button deleteButton = new Button("Delete");
	    deleteButton.getStyleClass().addAll("page-button-active", "page-button");
	    HBox loggerButtons = new HBox(10, addButton, updateButton, deleteButton);
	    
	    Separator separator = new Separator();
	    
	    Label find = new Label("Find");
	    TextField findField = new TextField();
	    findField.setPromptText("Search name/role/status");
	    
	    Button searchButton = new Button("Search");
	    searchButton.getStyleClass().addAll("page-button-active", "page-button");
	    Button resetButton = new Button("Reset");
	    resetButton.getStyleClass().addAll("page-button-active", "page-button");
	    HBox findButtons = new HBox(10, resetButton, searchButton);
	    
	    VBox findInput = new VBox(20, find, findField, findButtons);
		
	    VBox logger = new VBox(30, nameInput, roleAndStatus, loggerButtons, separator, findInput);
	    logger.getStyleClass().addAll("logger", "containers-shadow");
	    
		HBox mainLedger = new HBox(50, listView, logger); // refactor HBox main -> mainLedger
		HBox.setHgrow(listView, Priority.ALWAYS);
		HBox.setHgrow(logger, Priority.ALWAYS);
		VBox.setVgrow(mainLedger, Priority.ALWAYS);
		
		listView.prefWidthProperty().bind(mainLedger.widthProperty().subtract(50).divide(2));
		logger.prefWidthProperty().bind(mainLedger.widthProperty().subtract(50).divide(2));
		
		VBox root = new VBox(20, pageButtons, mainLedger);
		root.setPadding(new Insets(50));
		root.getStyleClass().add("default-bg");
		
		Scene staffPageScene = new Scene(root, 1080, 720);
		staffPageScene.getStylesheets().add(getClass().getResource("/application/styles/StaffPage.css").toExternalForm());
		staffPageScene.getStylesheets().add(getClass().getResource("/application/styles/application.css").toExternalForm());
		
		stage.setScene(staffPageScene);
		stage.setResizable(false);
		stage.show();
	}
}
