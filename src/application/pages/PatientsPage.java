package application.pages;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.*;

import application.Main;
import application.models.Patient;

public class PatientsPage {

	// patients list
	private ArrayList<Patient> patients = new ArrayList<>();

	public void setStageComponents(Stage stage, Main main) {
		// top navigation buttons functionality
		String[] labels = {"STAFF", "PATIENTS", "LAB EXAMS", "LAB REQUESTS", "LOGBOOK"};
		ArrayList<Button> labelButtons = new ArrayList<>();
		for(String label : labels) {
			Button newButton = new Button(label);
			
			if(label.equals("PATIENTS")) {
				newButton.getStyleClass().addAll("page-button-active", "page-button"); 
			} else {
				newButton.getStyleClass().addAll("page-button-inactive", "page-button"); // added functionality (change pages)
				newButton.setOnAction(e -> main.switchPage(label));
			}
			
			labelButtons.add(newButton);
		}
		
		// buttons display
		HBox pageButtons = new HBox(10);
		pageButtons.getChildren().addAll(labelButtons);
		HBox.setMargin(pageButtons, new Insets(20));
		
		// list of patients of left side 
		ListView<String> listView = new ListView<>();
		listView.getItems().add("PAT-0001 | fullName=Mylene | dob=2025-10-01");
		listView.getStyleClass().add("list-view");
		listView.getStyleClass().add("containers-shadow");
		
		// patient name label
	    Label name = new Label("Name");
	    TextField nameField = new TextField();
	    nameField.setPromptText("Enter patient name");
	    VBox nameInput = new VBox(15, name, nameField);
	    
	    // date label
	    Label date = new Label("DOB");
	    DatePicker datePicker = new DatePicker();
	    datePicker.setPrefHeight(8);
	    datePicker.setPrefWidth(300);
	    datePicker.setValue(LocalDate.now());
	    datePicker.setPromptText("Select Date");
		datePicker.getStyleClass().add("styled-date-picker");
	    VBox dateBox = new VBox(5, date, new Label(" "), datePicker); //for alignment
	    dateBox.setAlignment(Pos.BOTTOM_LEFT);
	    
	    // sex label
	    Label sexLabel = new Label("Sex");
	    ComboBox<String> sexCombo = new ComboBox<>();
	    sexCombo.getItems().addAll("M", "F", "Other");
	    sexCombo.setPromptText("Select sex");
	    sexCombo.setPrefWidth(120);
	    sexCombo.getStyleClass().add("text-field");
	    VBox sexBox = new VBox(5, sexLabel, sexCombo);
	    sexBox.setAlignment(Pos.BOTTOM_LEFT);
	    
	    // side by side placement of dob and sex
	    HBox dateSexRow = new HBox(20, dateBox, sexBox);
	    
	    // patient text box
	    TextArea infoArea = new TextArea();
	    infoArea.setPromptText("Enter patient information");
	    infoArea.setPrefRowCount(10);
	    infoArea.setWrapText(true);
	    VBox infoBox = new VBox(5, infoArea);
	    
	    // add, update, and delete buttons
	    Button addButton = new Button("Add");
	    addButton.getStyleClass().addAll("page-button-active", "page-button");
	    Button updateButton = new Button("Update");
	    updateButton.getStyleClass().addAll("page-button-active", "page-button");
	    Button deleteButton = new Button("Delete");
	    deleteButton.getStyleClass().addAll("page-button-active", "page-button");
	    HBox loggerButtons = new HBox(10, addButton, updateButton, deleteButton);
	    
	    // line separator
	    Separator separator = new Separator();
	    
	    // find textfield
	    Label find = new Label("Find");
	    TextField findField = new TextField();
	    findField.setPromptText("Search name/notes/id");
	    findField.setPrefWidth(250);
	    findField.setAlignment(Pos.BOTTOM_LEFT);
	    
	    // search and reset buttons
	    Button searchButton = new Button("Search");
	    searchButton.getStyleClass().addAll("page-button-active", "page-button");
	    Button resetButton = new Button("Reset");
	    resetButton.getStyleClass().addAll("page-button-active", "page-button");
	    
	    // textfield + search/reset HBox
	    HBox findRow = new HBox(20, findField, searchButton, resetButton);
	    findRow.setAlignment(Pos.CENTER_LEFT);
	    
	    VBox findBox = new VBox(5, find, findRow);
		
	    // logger
	    VBox logger = new VBox(30, nameInput, dateSexRow, infoBox, loggerButtons, separator, findBox);
	    logger.getStyleClass().addAll("logger", "containers-shadow");
	    
		HBox mainLedger = new HBox(50, listView, logger); // refactor HBox main -> mainLedger
		HBox.setHgrow(listView, Priority.ALWAYS);
		HBox.setHgrow(logger, Priority.ALWAYS);
		VBox.setVgrow(mainLedger, Priority.ALWAYS);
		
		listView.prefWidthProperty().bind(mainLedger.widthProperty().subtract(500).divide(2));
		logger.prefWidthProperty().bind(mainLedger.widthProperty().subtract(50).divide(2));
		
		VBox root = new VBox(20, pageButtons, mainLedger);
		root.setPadding(new Insets(50));
		root.getStyleClass().add("default-bg");
		
		Scene staffPageScene = new Scene(root, 1080, 720);
		staffPageScene.getStylesheets().add(getClass().getResource("/application/styles/Patients.css").toExternalForm());
		staffPageScene.getStylesheets().add(getClass().getResource("/application/styles/application.css").toExternalForm());
		
		stage.setScene(staffPageScene);
		stage.setResizable(false);
		stage.show();
	}
	
	// Function to add a patient
	public void addPatient(String name, LocalDate dob, String sex, String notes,
							int numPatientCounter, ListView<String> listView) {
		// generate ID
		String paddedNumber = String.format("%04d", numPatientCounter);
		String patientID = "PAT-" + paddedNumber;
		
		// create new patient object
		Patient newPatient = new Patient(patientID, name, dob, sex, notes);
		
		patients.add(newPatient);
		
		// update list
		listView.getItems().add(patientID.toString());
	}
}
