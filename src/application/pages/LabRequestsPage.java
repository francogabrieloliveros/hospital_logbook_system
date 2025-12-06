package application.pages;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.*;

import application.Main;
import application.models.*;

public class LabRequestsPage {
    private Hospital hospital;
    //initialization so other fxns can use it
    private ListView<LabRequest> listView;
    private ComboBox<String> patientComboBox; 
    private ComboBox<String> requestComboBox;
    private ComboBox<String> statusComboBox;
    private ComboBox<String> staffComboBox;
    private Button assignButton;
    private Button updateButton;
    private Button deleteButton;
    private TextField findField;
    private Button searchButton;
    private Button resetButton;
    
	public LabRequestsPage(Hospital hospital) {
		this.hospital = hospital;
	}
	
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
		
		//listview specifications
		listView = new ListView<>();
		listView.getStyleClass().addAll("list-view", "containers-shadow");
		updateListView();
		
		//patient dropdown with label
	    Label patientLabel = new Label("Patient:");
	    patientComboBox = new ComboBox<String>();
	    //gets the patients array from the hospital and make it the values inside the combo box
	    ArrayList<Patient> patientsArray = hospital.getPatients();
	    for(Patient patient:patientsArray) {
	    	patientComboBox.getItems().add(patient.getName()); 
	    }
	    VBox patientBox = new VBox(5, patientLabel, patientComboBox);
	    patientComboBox.setPrefWidth(400);
	    
	    //request label and dropdown
	    Label requestLabel = new Label("Request:");
	    requestComboBox = new ComboBox<>();
	    requestComboBox.getItems().addAll("CBC", "Urinalysis", "FBS", "Lipid Profile", "PCR", "X-Ray");
	    requestComboBox.setPrefWidth(190);
	    VBox requestBox = new VBox(5, requestLabel, requestComboBox );
	    
	    //status label and dropdown
	    Label statusLabel = new Label("Status:");
	    statusComboBox = new ComboBox<String>();
	    statusComboBox.getItems().addAll("new", "in progress", "done");
	    statusComboBox.setPrefWidth(190);
	    VBox statusBox = new VBox(5, statusLabel, statusComboBox);
	    
	    //container for request and status
	    HBox requestStatusRow = new HBox(20, requestBox, statusBox);
	    
	    //staff dropdown and label 
	    Label staffLabel = new Label("Available Staff:");
	    staffComboBox = new ComboBox<>();
	    refreshStaffComboBox();
	    staffComboBox.setPrefWidth(400);
	    VBox staffBox = new VBox(5, staffLabel, staffComboBox);
	    
	    patientComboBox.setPromptText("Patient");
	    requestComboBox.setPromptText("Request");
	    statusComboBox.setPromptText("Status");
	    staffComboBox.setPromptText("Staff");
		
		//refresh when combo box is clicked
        staffComboBox.setOnMouseClicked(e -> refreshStaffComboBox());
	    
	    //buttons specifications
	    assignButton = new Button("Assign");
	    assignButton.getStyleClass().addAll("page-button-inactive", "page-button");
	    assignButton.setDisable(true); //initialize then change if input is complete
	    
	    updateButton = new Button("Update");
	    updateButton.getStyleClass().addAll("page-button-inactive", "page-button");
	    updateButton.setDisable(true);
	    
	    deleteButton = new Button("Delete");
	    deleteButton.getStyleClass().addAll("page-button-inactive", "page-button");
	    deleteButton.setDisable(true);
	    
	    //container of all the buttons
	    HBox buttonsBox = new HBox(10, assignButton, updateButton, deleteButton);	    
	    
	    //listeners to user input to check validity
	    patientComboBox.setOnAction(e -> validateInputs());
	    requestComboBox.setOnAction(e -> validateInputs());
        statusComboBox.setOnAction(e -> validateInputs());
        staffComboBox.setOnAction(e -> validateInputs());
        
        //assign button functionality
        assignButton.setOnAction(e -> createLabRequest());
        
        //update button functionality
        updateButton.setOnAction(e -> {
        	LabRequest selectedItem = listView.getSelectionModel().getSelectedItem();
        	if(selectedItem!=null) {
        		updateLabRequest(selectedItem);
        	}
        });
        
        //delete button functionality
        deleteButton.setOnAction(e -> {
        	LabRequest selectedItem = listView.getSelectionModel().getSelectedItem();
        	if(selectedItem!=null) {
        		deleteLabRequest(selectedItem);
        	}
        });
	    
	    Separator separator = new Separator();
	    
	    //find specifications
	    Label find = new Label("Find");
	    findField = new TextField();
	    findField.setPromptText("Search patient/request/status");
	    findField.setPrefWidth(260);
	    findField.textProperty().addListener((observable, oldValue, newValue) -> { //functionalities of the search buttons
            validateSearchButtons();
        });
	    VBox findBox = new VBox(10, find, findField);
	    
	    //search button specifications with funcionality
	    searchButton = new Button("Search");
	    searchButton.getStyleClass().addAll("page-button-active", "page-button");
	    searchButton.setDisable(true);
	    searchButton.setOnAction(e -> searchLabRequests());
	    
	    //reset button specifications with functionalities
	    resetButton = new Button("Reset");
	    resetButton.getStyleClass().addAll("page-button-active", "page-button");
	    resetButton.setDisable(true);
	    resetButton.setOnAction(e -> {
	    	findField.clear();
	    	updateListView();
	    });
	    
	    //find buttons container
	    HBox findButtons = new HBox(10, resetButton, searchButton);
	    findButtons.setAlignment(Pos.BOTTOM_LEFT);
	    
	    VBox findInput = new VBox(20, findBox, findButtons);
		
	    VBox logger = new VBox(20, patientBox, requestStatusRow, staffBox, buttonsBox, separator, findInput);
	    logger.getStyleClass().addAll("logger", "containers-shadow");
	    
		HBox mainLedger = new HBox(50, listView, logger); // refactor HBox main -> mainLedger
		HBox.setHgrow(listView, Priority.ALWAYS);
		HBox.setHgrow(logger, Priority.ALWAYS);
		VBox.setVgrow(mainLedger, Priority.ALWAYS);
		
		listView.prefWidthProperty().bind(mainLedger.widthProperty().multiply(0.60));
		logger.prefWidthProperty().bind(mainLedger.widthProperty().multiply(0.40));
		
		//adding a listener in the listview for the update and delete buttons
		listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if(newValue!= null) { //if there is an item selected
				loadLabRequestData(newValue); //load the selected value
				//disable combo boxes that should not be changed except the status
				patientComboBox.setDisable(true);
				requestComboBox.setDisable(true);
				staffComboBox.setDisable(true);
				assignButton.getStyleClass().setAll("page-button-inactive", "page-button");
				assignButton.setDisable(true);
				
				//enable buttons that are needed
				updateButton.getStyleClass().setAll("page-button-active", "page-button");
                deleteButton.getStyleClass().setAll("page-button-active", "page-button");
                updateButton.setDisable(false);
                deleteButton.setDisable(false);
			}else { //if there is no selected
				clearInputs();
				assignButton.getStyleClass().setAll("page-button-inactive", "page-button");
				assignButton.setDisable(true);
				updateButton.getStyleClass().setAll("page-button-inactive", "page-button");
                deleteButton.getStyleClass().setAll("page-button-inactive", "page-button");
                updateButton.setDisable(true);
                deleteButton.setDisable(true);
			}
		});
		
		VBox root = new VBox(20, pageButtons, mainLedger);
		root.setPadding(new Insets(50));
		root.getStyleClass().add("default-bg");
		
		Scene labReqPageScene = new Scene(root, 1080, 720);
		labReqPageScene.getStylesheets().add(getClass().getResource("/application/styles/LabRequest.css").toExternalForm());
		labReqPageScene.getStylesheets().add(getClass().getResource("/application/styles/application.css").toExternalForm());
		
		stage.setScene(labReqPageScene);
		stage.setResizable(false);
		stage.show();
	}
	
	private void refreshStaffComboBox() {
		staffComboBox.getItems().clear();
		ArrayList<Staff> staffArray = hospital.getStaffs();
		for(Staff staff:staffArray) {
	    	if(staff.getStatus().equals("active")) {
	    		staffComboBox.getItems().add(staff.getName());
	    	}    	
	    }
	}
	
	private void clearInputs() {
	    patientComboBox.setValue(null);
	    requestComboBox.setValue(null);
	    statusComboBox.setValue(null);
	    staffComboBox.setValue(null);
	    
	    patientComboBox.setPromptText("Patient");
	    requestComboBox.setPromptText("Request");
	    statusComboBox.setPromptText("Status");
	    staffComboBox.setPromptText("Staff");
	}
	
	private void validateSearchButtons() {
		boolean isFieldEmpty = findField.getText().isEmpty();
        
        if(isFieldEmpty){
            //disable buttons when empty find field
            searchButton.getStyleClass().setAll("page-button-inactive", "page-button");
            resetButton.getStyleClass().setAll("page-button-inactive", "page-button");
            searchButton.setDisable(true);
            resetButton.setDisable(true);
        }else{ //when find field is not empty
            searchButton.getStyleClass().setAll("page-button-active", "page-button");
            resetButton.getStyleClass().setAll("page-button-active", "page-button");
            searchButton.setDisable(false);
            resetButton.setDisable(false);
        }
	}
	

	private void validateInputs() { //check if all input is complete
	    //only validate if combo boxes are enabled
	    if (patientComboBox.isDisabled() || requestComboBox.isDisabled() || staffComboBox.isDisabled()) {
	        assignButton.getStyleClass().setAll("page-button-inactive", "page-button");
	        assignButton.setDisable(true);
	        return;
	    }
	    
	    boolean allFilled = patientComboBox.getValue() != null && requestComboBox.getValue() != null && statusComboBox.getValue() != null && staffComboBox.getValue() != null;
	    
	    if(allFilled) {
	        assignButton.getStyleClass().setAll("page-button-active", "page-button");
	        assignButton.setDisable(false);
	    } else {
	        assignButton.getStyleClass().setAll("page-button-inactive", "page-button");
	        assignButton.setDisable(true);
	    }
	}
	
	private void updateListView() { //updates the listview depending on the array list in the hospital
		 listView.getItems().clear();
	        List<LabRequest> labRequests = hospital.getLabRequests();
	        for (LabRequest labRequest : labRequests) {
	            listView.getItems().add(labRequest);
	        }
	}

	private void loadLabRequestData(LabRequest selectedItem) { //gets the values of the lab request selected from the hospital and sets the values of the combo box to it
        //find the lab request in hospital
        LabRequest labRequest = null;
        List<LabRequest> labRequests = hospital.getLabRequests();
        for(LabRequest lr : labRequests){
            if(lr.getID().equals(selectedItem.getID())){
                labRequest = lr;
                break;
            }
        }
        
        if(labRequest != null){
            patientComboBox.setValue(labRequest.getPatient().getName());
            requestComboBox.setValue(labRequest.getRequest());
            statusComboBox.setValue(labRequest.getStatus());
            staffComboBox.setValue(labRequest.getStaff().getName());
        }
	}

	private void searchLabRequests() {
		String searchText = findField.getText().toLowerCase();
        
        if(searchText.isEmpty()) {
            updateListView();
            return;
        }
        
        List<LabRequest> filteredItems = new ArrayList<>();
        List<LabRequest> labRequests = hospital.getLabRequests();
        for (LabRequest labRequest : labRequests) {
            String itemString = labRequest.toString().toLowerCase();
            if (itemString.contains(searchText)) {
                filteredItems.add(labRequest);
            }
        }
        listView.getItems().clear();
        listView.getItems().addAll(filteredItems);
	}

	private void deleteLabRequest(LabRequest selectedItem) {
        //remove the labrequest fromt the hospital
        boolean removed = false;
        List<LabRequest> labRequests = hospital.getLabRequests();
        for(LabRequest lr:labRequests){
            if (lr.getID().equals(selectedItem.getID())) {
                labRequests.remove(lr);
                removed = true;
                break;
            }
        }
        
        if (removed) {
            updateListView();
            showAlert("Success", "Lab Request deleted successfully!");
        }
        listView.getSelectionModel().clearSelection();
        clearInputs();
	}

	private void updateLabRequest(LabRequest selectedItem) {
        //find the lab request
        LabRequest labRequest = null;
        List<LabRequest> labRequestArray = hospital.getLabRequests();
        for(LabRequest lr : labRequestArray){
            if (lr.getID().equals(selectedItem.getID())) {
                labRequest = lr;
                break;
            }
        }
        
        if (labRequest != null && statusComboBox.getValue() != null) {
            labRequest.update(statusComboBox.getValue());
            updateListView();
            showAlert("Success", "Lab Request updated successfully!");
        }	
        listView.getSelectionModel().clearSelection();
        clearInputs();
	}

	private void createLabRequest() {
		String patientName = patientComboBox.getValue();
        String requestType = requestComboBox.getValue();
        String status = statusComboBox.getValue();
        String staffName = staffComboBox.getValue();
        
        //find the patient in the array in hospital
        Patient patient = null; //initialization
        ArrayList<Patient> patientsArray = this.hospital.getPatients();
        for(Patient p:patientsArray) {
        	if(p.getName().equals(patientName)) {
        		patient = p;
        		break;
        	}
        }
        
        //find staff in staffarray in hospital
        Staff staff = null;
        ArrayList<Staff> staffArray = this.hospital.getStaffs();
        for(Staff s:staffArray) {
        	if(s.getName().equals(staffName)) {
        		staff = s;
        		break;
        	}
        }
        
        if(patient!=null && staff!= null) {
        	hospital.addLabRequest(new LabRequest(hospital, patient, requestType, status, staff));
        	
        	//clear the previous inputs
        	patientComboBox.setValue(null);
            requestComboBox.setValue(null);
            statusComboBox.setValue(null);
            staffComboBox.setValue(null);
            
            updateListView();
            showAlert("Success", "Lab Request created successfully!");
        }else {
        	showAlert("Error", "Could not find patient or staff");
        }
        clearInputs();
	}

	private void showAlert(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
	}


}