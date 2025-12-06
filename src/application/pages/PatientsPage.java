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
import application.models.LabExam;
import application.models.Patient;
import application.models.Staff;
import application.models.LabRequest;

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
		VBox dateBox = new VBox(5, date, datePicker);
		
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
		infoArea.setPrefRowCount(4);
		
		// Lab exams list
		Label labExamLabel = new Label ("Lab Exams");
		ListView<String> labExamListView = new ListView<>();
		labExamListView.getStyleClass().addAll("list-view", "containers=shadow");
		labExamListView.setPrefHeight(100);
		
			// add lab exam button
		Button addLabExamButton = new Button("Add Lab Exam");
		addLabExamButton.getStyleClass().addAll("page-button-active", "page-button");
		
			// Layout for Lab Exams section
		VBox labExamBox = new VBox(5, labExamLabel, labExamListView, addLabExamButton);
		
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
	    
		// listView listener
		// this will make selecting a patient auto-fill the form (QOF)
		listView.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
			int index = newVal.intValue();
			if (index >= 0) {
				Patient selectedPatient = hospital.getPatients().get(index);
				
				// auto-fill form
				nameField.setText(selectedPatient.getName());
				datePicker.setValue(selectedPatient.getDob());
				sexCombo.setValue(selectedPatient.getSex());
				infoArea.setText(selectedPatient.getNotes());
				
				// update lab exams list
				refreshLabExamsListView(selectedPatient, labExamListView);
			}
		});

		// VBOX logger
	    VBox logger = new VBox(15, nameInput, dateSexRow, infoArea, loggerButtons, new Separator(), labExamBox, new Separator(), findBox);
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
			
			// Create patient (should auto-add to hospital and l)
			Patient newPatient = new Patient(hospital, patientName, patientDob, patientSex, patientNotes);
			
			// update listView
			listView.getItems().add(newPatient.toString());
			
			// clear form
			nameField.clear();
			datePicker.setValue(null);
			sexCombo.getSelectionModel().clearSelection();
			infoArea.clear();
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
			
			// add to log
			selectedPatient.addLogToHospital("Updated patient information");
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
			
			// add to log
			selectedPatient.addLogToHospital("Deleted patient");
			// refresh list
			listView.getItems().set(selectedIndex, selectedPatient.toString());
		});
		
		// Add Lab Exam button logic
		addLabExamButton.setOnAction(e -> {
			int selectedIndex = listView.getSelectionModel().getSelectedIndex();
			if (selectedIndex < 0) {
				showAlert("No Selecton", "Please select a patient to add a lab exam.");
				return;
			}
			
			Patient selectedPatient = hospital.getPatients().get(selectedIndex);
			
			// for demonstration: dummy lab request and staff object
			// replace with actual form inputs later
			
			// public Staff (Hospital hospital, String name, String role, String status)
			// public LabRequest(Hospital hospital, Patient patient, String request, String status, Staff staff)
			Staff dummyStaff = new Staff(hospital, "Dr. Thea", "test role", "active");
			if (!hospital.getStaffs().contains(dummyStaff)) { // add dummy staff to hospital
				hospital.addStaff(dummyStaff);
			}
			
			LabRequest dummyRequest = new LabRequest(hospital, selectedPatient, "test request", "X-ray", dummyStaff);
			
			// create new lab exam
			// public LabExam (Hospital hospital, LabRequest labRequest, Staff performingStaff, String status)
			LabExam newExam = new LabExam(hospital, dummyRequest, dummyStaff, "Pending");
			
			// patient already adds this exam automatically
			refreshLabExamsListView(selectedPatient, labExamListView);
		});
		
		return logger;
	}
	
	// helper method to update/refresh the lab exams list
	private void refreshLabExamsListView(Patient patient, ListView<String> labExamListView) {
		labExamListView.getItems().clear();
		for (LabExam le : patient.getLabExams()) {
			labExamListView.getItems().add(le.toString());
		}
	}
}
