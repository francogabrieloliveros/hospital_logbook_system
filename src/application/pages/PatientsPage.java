package application.pages;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.*;

import application.Main;
import application.models.Hospital;
import application.models.Patient;

public class PatientsPage {

	// patients data (hospital)
	private Hospital hospital;
	
	// form input components
	private TextField nameField;
	private DatePicker datePicker;
	private ComboBox<String> sexCombo;
	private TextArea infoArea;
	private TextField findField;
	
	public PatientsPage(Hospital hospital) { this.hospital = hospital; }

	int[] patientCounter = {1}; // value stored inside array lets the add button increment the counter

	public void setStageComponents(Stage stage, Main main) {
		// buttons display
		HBox pageButtons = buildPageButtons(main);

		// left panel: list of patients
		ListView<String> listView = buildPatientListView();

		// right panel: patient form UI
		VBox logger = buildLoggerSection(listView);
		
		HBox mainLedger = new HBox(50, listView, logger); // refactor HBox main -> mainLedger
		HBox.setHgrow(listView, Priority.ALWAYS);
		HBox.setHgrow(logger, Priority.ALWAYS);
		VBox.setVgrow(mainLedger, Priority.ALWAYS);
		
		listView.prefWidthProperty().bind(mainLedger.widthProperty().subtract(500).divide(2));
		logger.prefWidthProperty().bind(mainLedger.widthProperty().subtract(50).divide(2));
		
		VBox root = new VBox(20, pageButtons, mainLedger);
		root.setPadding(new Insets(50));
		root.getStyleClass().add("default-bg");
		
		Scene staffPageScene = new Scene(root, 1200, 720);
		staffPageScene.getStylesheets().add(getClass().getResource("/application/styles/Patients.css").toExternalForm());
		staffPageScene.getStylesheets().add(getClass().getResource("/application/styles/application.css").toExternalForm());

		stage.setScene(staffPageScene);
		stage.setResizable(false);
		stage.show();
	}
	
	// method to add a patient
	public void addPatient(String name, LocalDate dob, String sex, String notes,
							int numPatientCounter, ListView<String> listView) {
		// generate ID
		String paddedNumber = String.format("%04d", numPatientCounter);
		String patientID = "PAT-" + paddedNumber;
		
		// create new patient object
		Patient newPatient = new Patient(patientID, name, dob, sex, notes);
		
		hospital.addPatient(newPatient);
		
		// update list
		listView.getItems().add(newPatient.toString());
	}
	
	// helper method to show an alert
	private void showAlert(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.WARNING);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}
	
	// helper method to build top navigation buttons
	private HBox buildPageButtons(Main main) {
		// top navigation buttons functionality
		String[] labels = {"STAFF", "PATIENTS", "LAB EXAMS", "LAB REQUESTS", "LOGBOOK"};
		ArrayList<Button> labelButtons = new ArrayList<>();
		
		for(String label : labels) {
			Button newButton = new Button(label);
			
			if(label.equals("PATIENTS")) {
				newButton.getStyleClass().addAll("page-button-active", "page-button"); 
			} else {
				newButton.getStyleClass().addAll("page-button-inactive", "page-button");
				newButton.setOnAction(e -> main.switchPage(label));
			}
			
			labelButtons.add(newButton);
		}
		
		HBox pageButtons = new HBox(10);
		pageButtons.getChildren().addAll(labelButtons);
		HBox.setMargin(pageButtons, new Insets(20));
		
		return pageButtons;
	}
	
	// helper method to set up left panel (patients list)
	private ListView<String> buildPatientListView() {
		// list of patients of left side 
		ListView<String> listView = new ListView<>();
		// format: "PAT-0001 | fullName=Mylene | dob=2025-10-01"
		for (Patient p: hospital.getPatients()) {
			listView.getItems().add(p.toString());
		}
		
		listView.getStyleClass().add("list-view");
		listView.getStyleClass().add("containers-shadow");
		
		return listView;
	}
	
	// helper method to set up left panel (input fields)
	private VBox buildLoggerSection(ListView<String> listView) {
		// name input
		Label name = new Label("Name");
		nameField = new TextField();
		nameField.setPromptText("Enter patient name");
		VBox nameInput = new VBox(15, name, nameField);
		
		// date of birth (dob)
		Label date = new Label("DOB");
		datePicker = new DatePicker(LocalDate.now());
		datePicker.setPrefWidth(300);
		VBox dateBox = new VBox(5, date, new Label(" "), datePicker);
		
		// sex ComboBox
		Label sexLabel = new Label("Sex");
		sexCombo = new ComboBox<>();
		sexCombo.setPromptText("Select Sex");
		sexCombo.getItems().addAll("M", "F", "Other");
		sexCombo.setPrefWidth(120);
		VBox sexBox = new VBox(5, sexLabel, sexCombo);
		
		HBox dateSexRow = new HBox(20, dateBox, sexBox);
		
		// info area
		infoArea = new TextArea();
		infoArea.setPromptText("Enter patient information");
		infoArea.setPrefRowCount(10);
		
		// CRUD buttons
		Button addButton = new Button("Add");
		Button updateButton = new Button("Update");
		Button deleteButton = new Button("Delete");
		HBox loggerButtons = new HBox(10, addButton, updateButton, deleteButton);
		
		// Search section
		Label find = new Label("find");
		findField = new TextField();
		Button searchButton = new Button("Search");
		Button resetButton = new Button("Reset");
		HBox findRow = new HBox(20, findField, searchButton, resetButton);
		VBox findBox = new VBox(5, find, findRow);
		
		// styling
		addButton.getStyleClass().addAll("page-button-active", "page-button");    
	    updateButton.getStyleClass().addAll("page-button-active", "page-button");
	    deleteButton.getStyleClass().addAll("page-button-active", "page-button");
	    searchButton.getStyleClass().addAll("page-button-active", "page-button");
	    resetButton.getStyleClass().addAll("page-button-active", "page-button");
	    datePicker.getStyleClass().add("styled-date-picker");
	    
	    
	    VBox logger = new VBox(30, nameInput, dateSexRow, infoArea, loggerButtons, new Separator(), findBox);
	    logger.getStyleClass().addAll("logger", "containers-shadow");
	    
	    // ~~~ event handlers ~~~
	    // add button logic
		addButton.setOnAction(e -> {
			String patientName = nameField.getText().trim();
			LocalDate patientDob = datePicker.getValue();
			String patientSex = sexCombo.getValue();
			String patientNotes = infoArea.getText().trim();
			
			// check for empty fields
			if (patientName.isEmpty() || patientDob == null || patientSex == null) {
				showAlert("Missing Fields", "Please fill out name, date of birth, and sex.");
				return;
			}
			
			addPatient(patientName, patientDob, patientSex, patientNotes, patientCounter[0], listView);
			patientCounter[0]++; // increment
		});
		
		// reset button logic
		resetButton.setOnAction(e -> {
		    nameField.clear();
		    datePicker.setValue(null);
		    sexCombo.getSelectionModel().clearSelection();
		    infoArea.clear();
		    findField.clear();
		});
		
		// update button logic
		updateButton.setOnAction(e -> {
			int selectedIndex = listView.getSelectionModel().getSelectedIndex();
			if (selectedIndex < 0) {
				showAlert("No Selection", "Please select a patient to update.");
				return;
			}
			
			Patient selectedPatient = hospital.getPatients().get(selectedIndex);
			
			// validation
			String updatedName = nameField.getText().trim();
			LocalDate updatedDob = datePicker.getValue();
			String updatedSex = sexCombo.getValue();
			
			if (updatedName.isEmpty() || updatedDob == null || updatedSex == null) {
				showAlert("Missing Fields", "Please fill out name, date of birth, and sex.");
				return;
			}
			
			selectedPatient.setName(updatedName);
			selectedPatient.setDob(updatedDob);
			selectedPatient.setSex(updatedSex);
			selectedPatient.setNotes(infoArea.getText().trim());
			
			// refresh list
			listView.getItems().set(selectedIndex, selectedPatient.toString());
		});
		
		// delete button logic
		deleteButton.setOnAction(e -> {
			int selectedIndex = listView.getSelectionModel().getSelectedIndex();
			if (selectedIndex < 0) {
				showAlert("No Selection", "Please select a patient to delete.");
				return;
			}
			
			Patient selectedPatient = hospital.getPatients().get(selectedIndex);
			
			hospital.removePatient(selectedPatient);
			listView.getItems().remove(selectedIndex);
			
			// refresh list
			listView.getItems().set(selectedIndex, selectedPatient.toString());
		});
		return logger;
	}
}
