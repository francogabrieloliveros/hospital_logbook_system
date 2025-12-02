package application.pages;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.*;

import application.Main;

public class LabRequestsPage {
	
	public void setStageComponents(Stage stage, Main main) {
		String[] labels = {"STAFF", "PATIENTS", "LAB EXAMS", "LAB REQUESTS", "LOGBOOK"};
		ArrayList<Button> labelButtons = new ArrayList<>();
		for(String label : labels) {
			Button newButton = new Button(label);
			
			if(label.equals("LAB REQUESTS")) {
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
		
	    Label patientLabel = new Label("Patient:");
	    ComboBox<String> patientComboBox = new ComboBox<String>();
	    patientComboBox.getItems().addAll("IDK1", "IDK2", "IDK3", "IDK4", "Others"); //ayusin later sa backend na laman ay patients
	    VBox patientBox = new VBox(5, patientLabel, patientComboBox);
	    patientComboBox.setPrefWidth(400);
	    
	    Label requestLabel = new Label("Request:");
	    ComboBox<String> requestComboBox = new ComboBox<>();
	    requestComboBox.getItems().addAll("CBC", "Urinalysis", "FBS", "Lipid Profile", "PCR", "X-Ray");
	    requestComboBox.setPrefWidth(190);
	    VBox requestBox = new VBox(5, requestLabel, requestComboBox );
	    
	    Label statusLabel = new Label("Status:");
	    ComboBox<String> statusComboBox = new ComboBox<String>();
	    statusComboBox.getItems().addAll("new", "in progress", "done");
	    statusComboBox.setPrefWidth(190);
	    VBox statusBox = new VBox(5, statusLabel, statusComboBox);
	    
	    HBox requestStatusRow = new HBox(20, requestBox, statusBox);
	    
	    Label staffLabel = new Label("Available Staff:");
	    ComboBox<String> staffComboBox = new ComboBox<>();
	    staffComboBox.getItems().addAll("bla", "bla", "bla", "bla", "bla");
	    staffComboBox.setPrefWidth(400);
	    VBox staffBox = new VBox(5, staffLabel, staffComboBox);
	    
	    Button assignButton = new Button("Assign");
	    assignButton.getStyleClass().addAll("page-button-inactive", "page-button");
	    Button updateButton = new Button("Update");
	    updateButton.getStyleClass().addAll("page-button-active", "page-button");
	    Button deleteButton = new Button("Delete");
	    deleteButton.getStyleClass().addAll("page-button-active", "page-button");
	    HBox buttonsBox = new HBox(10, assignButton, updateButton, deleteButton);	    
	    
	    Separator separator = new Separator();
	    
	    Label find = new Label("Find");
	    TextField findField = new TextField();
	    findField.setPromptText("Search patient/request/status");
	    findField.setPrefWidth(260);
	    VBox findBox = new VBox(10, find, findField);
	    
	    Button searchButton = new Button("Search");
	    searchButton.getStyleClass().addAll("page-button-active", "page-button");
	    Button resetButton = new Button("Reset");
	    resetButton.getStyleClass().addAll("page-button-active", "page-button");
	    HBox findButtons = new HBox(10, resetButton, searchButton);
	    findButtons.setAlignment(Pos.BOTTOM_LEFT);
	    
	    HBox findInput = new HBox(20, findBox, findButtons);
		
	    VBox logger = new VBox(20, patientBox, requestStatusRow, staffBox, buttonsBox, separator, findInput);
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
