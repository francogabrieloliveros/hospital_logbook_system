package application.pages;

import javafx.collections.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.*;

import application.Main;
import application.models.*;

public class PatientsPage {

	private Hospital hospital;
	
	// Form input components
	private ObservableList<Patient> items;
	private ListView<Patient> listView;
	private ComboBox<String> sexCombo;
	private DatePicker datePicker;
	private TextField nameField;
	private TextField findField;
	private TextArea infoArea;
	private Button addButton;
    private Button updateButton;
    private Button deleteButton;
    private Button searchButton;
    private Button resetButton;
	
	public PatientsPage(Hospital hospital) { this.hospital = hospital; }

	public void setStageComponents(Stage stage, Main main) {
		
		HBox pageButtons = new HeaderButtons(main, "PATIENTS").get(); // page switching header
		
		/* ------------------------------------------- MAINPAGE CONTENTS-------------------------------------------*/
		// left panel: list of patients
		buildListView();
		
		// right panel: patient form UI
		VBox nameInput = buildNameInput();
		HBox dateSexRow = buildDateAndSexInput();
		buildInfoArea();
		HBox CRUDButtons = buildCRUDButtons();
		VBox findBox = buildFindBox();
		
		VBox logger = new VBox(15, 
				               nameInput, 
				               dateSexRow, 
				               infoArea, 
				               CRUDButtons, 
				               new Separator(), 
				               findBox);
	    logger.getStyleClass().addAll("logger", "containers-shadow");
	    
	    // Main
		HBox mainLedger = new HBox(25, listView, logger);
		HBox.setHgrow(listView, Priority.ALWAYS);
		HBox.setHgrow(logger, Priority.ALWAYS);
		VBox.setVgrow(mainLedger, Priority.ALWAYS);
		
		listView.prefWidthProperty().bind(mainLedger.heightProperty().multiply(0.4));
		logger.prefWidthProperty().bind(mainLedger.heightProperty().multiply(0.6));
		
		/* ------------------------------------------- FUNCTIONALITY -------------------------------------------*/
		listView.getSelectionModel().selectedItemProperty().addListener((a, b, selected) -> {
		    if (selected != null) {
		        nameField.setText(selected.getName());
		        datePicker.setValue(selected.getDob());
		        sexCombo.setValue(selected.getSex());
		        infoArea.setText(selected.getNotes());
		    } else {
		    	resetInputFields();
		    }
		});
		
		nameField.textProperty().addListener((a, b, c) -> updateLoggerButtons());
		datePicker.valueProperty().addListener((a, b, c) -> updateLoggerButtons());
		sexCombo.valueProperty().addListener((a, b, c) ->updateLoggerButtons());
		infoArea.textProperty().addListener((a, b, c) ->updateLoggerButtons());
		
		findField.textProperty().addListener((a, b, c) -> updateFindButtons());
		
		addButton.setOnAction(e -> {
			hospital.addPatient(new Patient(hospital, 
										nameField.getText(), 
										datePicker.getValue(),
										sexCombo.getValue(),
										infoArea.getText()));
			
			items.setAll(hospital.getPatients());
			resetInputFields();
			showAlert("Successful!","Successfully added new patient.");
		});
		 
		updateButton.setOnAction(e -> {
			Patient selected = listView.getSelectionModel().getSelectedItem();
					
			if (selected != null) {
				selected.update(nameField.getText(), datePicker.getValue(), sexCombo.getValue(), infoArea.getText());
				items.setAll(hospital.getPatients());
				resetInputFields();
				showAlert("Successful!","Successfully updated patient information.");
			}
		});
		
		deleteButton.setOnAction(e -> {
			Patient selected = listView.getSelectionModel().getSelectedItem();
			
			if(selected != null) {
				selected.delete();
				items.setAll(hospital.getPatients());
				resetInputFields();
				showAlert("Successful!","Successfully deleted patient.");
			}
		});
		
		// Sets item view to search results
		searchButton.setOnAction(e -> {
			ArrayList<Patient> searchResults = new ArrayList<Patient>();
			String query = findField.getText().trim();
			
			for(Patient patient : hospital.getPatients()) {
				if(patient.getName().contains(query) ||
				   patient.getDob().toString().contains(query) ||
				   patient.getSex().contains(query) ||
				   patient.getNotes().contains(query)) {
					searchResults.add(patient);
				}
			}
			
			items.setAll(searchResults);
			resetInputFields();
		});
		
		resetButton.setOnAction(e -> {
			resetInputFields();
			
			items.setAll(hospital.getPatients());
			listView.setItems(items);
		});
		
		listView.setOnKeyPressed(e -> deselectOnEsc(e));
		
		/* ------------------------------------------- ROOT & SCENE -------------------------------------------*/
		VBox root = new VBox(20, pageButtons, mainLedger);
		root.setPadding(new Insets(50));
		root.getStyleClass().add("default-bg");
		root.setOnKeyPressed(e -> deselectOnEsc(e));
		
		Scene staffPageScene = new Scene(root, 1200, 720);
		staffPageScene.getStylesheets().add(getClass().getResource("/application/styles/Patients.css").toExternalForm());
		staffPageScene.getStylesheets().add(getClass().getResource("/application/styles/application.css").toExternalForm());
		stage.setScene(staffPageScene);
		stage.setTitle("Patients");
		stage.show();
	}
	
	private void showAlert(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
	}
	
	private void updateFindButtons() {
	    boolean findFilled = !findField.getText().isBlank();
	    
	    // Enable search button when findField has text
	    if (findFilled) {
	    	searchButton.setDisable(false);
	    } else {
	    	searchButton.setDisable(true);
	    }
	}
	
	private void resetInputFields() {
		nameField.setText("");
		datePicker.setValue(null);
		sexCombo.setValue("Select sex");
		infoArea.setText("");
		findField.setText("");
		nameField.requestFocus();
		
		listView.getSelectionModel().clearSelection();
	}
	
	private void deselectOnEsc(KeyEvent e) {
		if (e.getCode() == javafx.scene.input.KeyCode.ESCAPE) {
	        listView.getSelectionModel().clearSelection();
	    }
	}
	
	private void updateLoggerButtons() {
		Patient selected = listView.getSelectionModel().getSelectedItem();
	    boolean nameFilled = !nameField.getText().isBlank();
	    boolean dateFilled = datePicker.getValue() != null;
	    boolean sexFilled = sexCombo.getValue() != "Select sex";
	    boolean infoFilled = !infoArea.getText().isBlank();
	    boolean listViewSelected =  selected != null;

	    if (nameFilled && 
	    	dateFilled &&
	    	sexFilled &&
	    	infoFilled &&
	    	listViewSelected) {
	    	// Enable update and delete button when listViewItem selected
	    	addButton.setDisable(true);
			updateButton.setDisable(false);
			deleteButton.setDisable(false || selected.isOwned);
	    } else if (nameFilled && 
		    	   dateFilled &&
		    	   sexFilled &&
		    	   infoFilled &&
		    	   !listViewSelected) {
	    	// Enable add button when no listViewItem selected (new item)
	    	addButton.setDisable(false);
			updateButton.setDisable(true);
			deleteButton.setDisable(true);
	    } else {
	    	addButton.setDisable(true);
			updateButton.setDisable(true);
			deleteButton.setDisable(true);
	    }
	}
	
	/* ------------------------------------------- BUILDERS -------------------------------------------*/
	// helper method to set up left panel (patients list)
	private void buildListView(){
		listView = new ListView<>();
		listView.getStyleClass().add("list-view");
		listView.getStyleClass().add("containers-shadow");
		
		// Set list view items
		items = FXCollections.observableArrayList();
		items.setAll(hospital.getPatients());
		listView.setItems(items);
	}
	
	private VBox buildNameInput() {
		Label name = new Label("Name");
		nameField = new TextField();
		nameField.setPromptText("Enter patient name");
		VBox nameInput = new VBox(15, name, nameField);
		
		return nameInput;
	}
	
	private HBox buildDateAndSexInput() {
		// date of birth input
		Label date = new Label("DOB");
		datePicker = new DatePicker(null);
		datePicker.setPromptText("Date of Birth");
		datePicker.setPrefWidth(300);
		datePicker.getStyleClass().add("styled-date-picker");
		VBox dateBox = new VBox(5, date, datePicker);
		
		
		// sex ComboBox
		Label sexLabel = new Label("Sex");
		sexCombo = new ComboBox<>();
		sexCombo.getItems().addAll("Select sex", "M", "F", "Other");
		sexCombo.setValue("Select sex");
		sexCombo.setPrefWidth(120);
		VBox sexBox = new VBox(5, sexLabel, sexCombo);
		
		HBox dateSexRow = new HBox(20, dateBox, sexBox);
		
		return dateSexRow;
	}
	
	private void buildInfoArea() {
		infoArea = new TextArea();
		infoArea.setPromptText("Enter patient information");
		infoArea.setPrefRowCount(4);
	}
	
	private HBox buildCRUDButtons() {
		addButton = new Button("Add");
		updateButton = new Button("Update");
		deleteButton = new Button("Delete");
		HBox loggerButtons = new HBox(10, addButton, updateButton, deleteButton);
		addButton.setDisable(true);
		updateButton.setDisable(true);
		deleteButton.setDisable(true);
		
		// styling
		addButton.getStyleClass().addAll("page-button-active", "page-button");    
	    updateButton.getStyleClass().addAll("page-button-active", "page-button");
	    deleteButton.getStyleClass().addAll("page-button-active", "page-button");
		
		return loggerButtons;
	}
	
	private VBox buildFindBox() {
		// Search section
		Label find = new Label("find");
		findField = new TextField();
		findField.setPromptText("Search name/dob/sex/info");
		findField.setPrefWidth(300);
		searchButton = new Button("Search");
		resetButton = new Button("Reset");
		HBox findRow = new HBox(20, findField, searchButton, resetButton);
		VBox findBox = new VBox(5, find, findRow);
		searchButton.setDisable(true);
		
		//styling
		searchButton.getStyleClass().addAll("page-button-active", "page-button");
	    resetButton.getStyleClass().addAll("page-button-active", "page-button");
		
		return findBox;
	}
		
}
