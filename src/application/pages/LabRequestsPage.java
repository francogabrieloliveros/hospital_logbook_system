package application.pages;

import javafx.collections.*;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.*;

import application.Main;
import application.models.*;

public class LabRequestsPage {
	
    private Hospital hospital;
    
    // Form input components
    private ObservableList<LabRequest> items;
    private ObservableList<Patient> patients;
    private ObservableList<Staff> activeStaffs;
    private ListView<LabRequest> listView;
    private ComboBox<Patient> patientComboBox; 
    private ComboBox<String> requestComboBox;
    private ComboBox<String> statusComboBox;
    private ComboBox<Staff> staffComboBox;
    private TextField findField;
    private Button addButton;
    private Button updateButton;
    private Button deleteButton;
    private Button searchButton;
    private Button resetButton;
    
	public LabRequestsPage(Hospital hospital) { this.hospital = hospital; }
	
	public void setStageComponents(Stage stage, Main main) {
		
		HBox pageButtons = new HeaderButtons(main, "LAB REQUESTS").get(); // page switching header
		
		/* ------------------------------------------- MAINPAGE CONTENTS-------------------------------------------*/
		// Left panel
		buildListView();
		
		// Right panel
	    VBox patientBox = buildPatientBox(); //patient dropdown with label
	    HBox requestStatusRow = buildRequestStatusRow(); //container for request and status
	    VBox staffBox = buildStaffBox(); //staff dropdown and label
	    HBox buttonsBox = buildButtonsBox();//container of all the buttons	    
	    VBox findInput = buildFindInput();
	    
	    VBox logger = new VBox(20, patientBox, requestStatusRow, staffBox, buttonsBox, new Separator(), findInput);
	    logger.getStyleClass().addAll("logger", "containers-shadow");
	    
		HBox mainLedger = new HBox(50, listView, logger);
		HBox.setHgrow(listView, Priority.ALWAYS);
		HBox.setHgrow(logger, Priority.ALWAYS);
		VBox.setVgrow(mainLedger, Priority.ALWAYS);
		
		listView.prefWidthProperty().bind(mainLedger.widthProperty().multiply(0.60));
		logger.prefWidthProperty().bind(mainLedger.widthProperty().multiply(0.40));
		
		/* ------------------------------------------- FUNCTIONALITY -------------------------------------------*/
		// Set input field values to selected values
		listView.getSelectionModel().selectedItemProperty().addListener((a, b, selected) -> {
		    if (selected != null) {
		    	patientComboBox.setValue(selected.getPatient());
	            requestComboBox.setValue(selected.getRequest());
	            statusComboBox.setValue(selected.getStatus());
	            staffComboBox.setValue(selected.getStaff());
		    } else {
		    	clearInputs();
		    }
		});
		
		//listeners to user input to check validity
	    patientComboBox.valueProperty().addListener((a, b, c) -> validateInputs());
	    requestComboBox.valueProperty().addListener((a, b, c) -> validateInputs());
        statusComboBox.valueProperty().addListener((a, b, c) -> validateInputs());
        staffComboBox.valueProperty().addListener((a, b, c) -> validateInputs());
	    findField.textProperty().addListener((a, b, c) -> validateSearchButtons());
	    
	    addButton.setOnAction(e -> {
			hospital.addLabRequest(new LabRequest(hospital, 
					                              patientComboBox.getValue(), 
					                              requestComboBox.getValue(), 
					                              statusComboBox.getValue(), 
					                              staffComboBox.getValue()));
			patientComboBox.getValue().isOwned = true;
			staffComboBox.getValue().isOwned = true;
			
			items.setAll(hospital.getLabRequests());
			clearInputs();
			showAlert("Successful!","Successfully added new lab request.");
		});
		
		updateButton.setOnAction(e -> {
			LabRequest selected = listView.getSelectionModel().getSelectedItem();
					
			if (selected != null) {
				selected.update(patientComboBox.getValue(), 
                                requestComboBox.getValue(), 
                                statusComboBox.getValue(), 
                                staffComboBox.getValue());
				items.setAll(hospital.getLabRequests());
				clearInputs();
				showAlert("Successful!","Successfully updated lab request information.");
			}
		});
		
		deleteButton.setOnAction(e -> {
			LabRequest selected = listView.getSelectionModel().getSelectedItem();
			
			if(selected != null) {
				selected.delete();
				selected.getStaff().isOwned = selected.getStaff().stillOwned();
				selected.getPatient().isOwned = selected.getStaff().stillOwned();
				items.setAll(hospital.getLabRequests());
				clearInputs();
				showAlert("Successful!","Successfully deleted lab request.");
			}
		});
		
		// Sets item view to search results
		searchButton.setOnAction(e -> {
			ArrayList<LabRequest> searchResults = new ArrayList<LabRequest>();
			String query = findField.getText().trim();
			
			for(LabRequest labRequest: hospital.getLabRequests()) {
				if(labRequest.getPatient().getName().contains(query) ||
				   labRequest.getRequest().contains(query) ||
				   labRequest.getStatus().contains(query) ||
				   labRequest.getStaff().getName().contains(query)) {
					searchResults.add(labRequest);
				}
			}
			
			items.setAll(searchResults);
			clearInputs();
		});
		
		resetButton.setOnAction(e -> {
			clearInputs();
			
			items.setAll(hospital.getLabRequests());
			listView.setItems(items);
		});
		
		listView.setOnKeyPressed(e -> deselectOnEsc(e));
	    
		/* ------------------------------------------- ROOT & SCENE -------------------------------------------*/
		VBox root = new VBox(20, pageButtons, mainLedger);
		root.setPadding(new Insets(50));
		root.getStyleClass().add("default-bg");
		root.setOnKeyPressed(e -> deselectOnEsc(e));
		
		Scene labReqPageScene = new Scene(root, 1200, 720);
		labReqPageScene.getStylesheets().add(getClass().getResource("/application/styles/LabRequest.css").toExternalForm());
		labReqPageScene.getStylesheets().add(getClass().getResource("/application/styles/application.css").toExternalForm());
		stage.setScene(labReqPageScene);
		stage.setTitle("Lab Requests");
		stage.show();
	}
	
	private void clearInputs() {
	    patientComboBox.setValue(null);
	    requestComboBox.setValue("Select request");
	    statusComboBox.setValue("Select status");
	    staffComboBox.setValue(null);
	    
	    listView.getSelectionModel().clearSelection();
	}

	private void showAlert(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
	}
	
	private void deselectOnEsc(KeyEvent e) {
		if (e.getCode() == javafx.scene.input.KeyCode.ESCAPE) {
	        listView.getSelectionModel().clearSelection();
	    }
	}
	
	private void validateSearchButtons() {
		boolean isFieldEmpty = findField.getText().isEmpty();
        
        if(isFieldEmpty){
            //disable buttons when empty find field
            searchButton.setDisable(true);
        }else{ //when find field is not empty
            searchButton.setDisable(false);
        }
	}
	
	private void validateInputs() { //check if all input is complete
		LabRequest selected = listView.getSelectionModel().getSelectedItem();
		boolean patientFilled = patientComboBox.getValue() != null;
		boolean requestFilled = requestComboBox.getValue() != "Select request";
		boolean statusFilled = statusComboBox.getValue() != "Select status";
		boolean staffFilled = staffComboBox.getValue() != null;
		boolean listViewSelected =  selected != null;
		boolean isDoneSelected = listViewSelected ? selected.getStatus().equals("done") : false;
		boolean isOwnedSelected = listViewSelected ? selected.getIsOwned() : false;
		
	    if(patientFilled &&
	       requestFilled &&
	       statusFilled &&
	       staffFilled &&
	       listViewSelected) {
	    	patientComboBox.setDisable(true);
	    	requestComboBox.setDisable(true);
	    	staffComboBox.setDisable(true);
	    	statusComboBox.setDisable(isDoneSelected || isOwnedSelected);
	        addButton.setDisable(true);
	        updateButton.setDisable(isDoneSelected || isOwnedSelected);
	        deleteButton.setDisable(false || selected.isOwned);
	    } else if (patientFilled &&
	 	       	   requestFilled &&
		           statusFilled &&
		           staffFilled &&
		           !listViewSelected) {
	    	patientComboBox.setDisable(false);
	    	requestComboBox.setDisable(false);
	    	staffComboBox.setDisable(false);
	    	addButton.setDisable(false);
	        updateButton.setDisable(true);
	        deleteButton.setDisable(true);
	    } else {
	    	patientComboBox.setDisable(false);
	    	requestComboBox.setDisable(false);
	    	staffComboBox.setDisable(false);
	        addButton.setDisable(true);
	        updateButton.setDisable(true);
	        deleteButton.setDisable(true);
	    }
	}
	
	private ObservableList<Staff> createStaffList(){
		ObservableList<Staff> activeStaff = FXCollections.observableArrayList();
		for(Staff staff : hospital.getStaffs()) {
			if(staff.getStatus().equals("active")) {
				activeStaff.add(staff);	
			}
		}
		
		return activeStaff;
	}
	
	/* ------------------------------------------- BUILDERS -------------------------------------------*/
	private void buildListView(){
		listView = new ListView<>();
		listView.getStyleClass().addAll("list-view", "containers-shadow");
		
		// Set list view items
		items = FXCollections.observableArrayList();
		items.setAll(hospital.getLabRequests());
		listView.setItems(items);
	}
	
	private VBox buildPatientBox() {
		//patient dropdown with label
	    Label patientLabel = new Label("Patient");
	    patientComboBox = new ComboBox<Patient>();
	    patients = FXCollections.observableArrayList();
	    patients.setAll(hospital.getPatients());
	    patientComboBox.setPrefWidth(400);
	    patientComboBox.setItems(patients);
	    patientComboBox.setPromptText("Select patient");
	    VBox patientBox = new VBox(5, patientLabel, patientComboBox);
	    
	    patientComboBox.setButtonCell(new ListCell<Patient>() {
		    @Override
		    protected void updateItem(Patient item, boolean empty) {
		        super.updateItem(item, empty);
		        setText(item == null ? "Select patient" : item.getName());
		    }
		});
	    
	    patientComboBox.setCellFactory(list -> new ListCell<Patient>() {
	        @Override
	        protected void updateItem(Patient item, boolean empty) {
	            super.updateItem(item, empty);
	            setText(item == null ? "Select patient" : item.getName());
	        }
	    });
	    
	    return patientBox;
	}
	
	private HBox buildRequestStatusRow() {
	    //request label and dropdown
	    Label requestLabel = new Label("Request");
	    requestComboBox = new ComboBox<>();
	    requestComboBox.getItems().setAll("Select request", "CBC", "Urinalysis", "FBS", "Lipid Profile", "PCR", "X-Ray");
	    requestComboBox.setValue("Select request");
	    requestComboBox.setPrefWidth(190);
	    VBox requestBox = new VBox(5, requestLabel, requestComboBox );
	    
	    //status label and dropdown
	    Label statusLabel = new Label("Status:");
	    statusComboBox = new ComboBox<String>();
	    statusComboBox.getItems().addAll("Select status", "new", "in progress", "done");
	    statusComboBox.setValue("Select status");
	    statusComboBox.setPrefWidth(190);
	    VBox statusBox = new VBox(5, statusLabel, statusComboBox);
	    
	    //container for request and status
	    HBox requestStatusRow = new HBox(20, requestBox, statusBox);
	    
	    return requestStatusRow;
	}
 
	private VBox buildStaffBox() {
		//staff dropdown and label 
	    Label staffLabel = new Label("Available Staff");
	    staffComboBox = new ComboBox<Staff>();
	    activeStaffs = createStaffList();
	    staffComboBox.setPrefWidth(400);
	    staffComboBox.setItems(activeStaffs);
	    staffComboBox.setPromptText("Select staff");
	    VBox staffBox = new VBox(5, staffLabel, staffComboBox);
	    
	    staffComboBox.setButtonCell(new ListCell<Staff>() {
		    @Override
		    protected void updateItem(Staff item, boolean empty) {
		        super.updateItem(item, empty);
		        setText(item == null ? "Select staff" : item.getName());
		    }
		});
	    
	    staffComboBox.setCellFactory(list -> new ListCell<Staff>() {
	        @Override
	        protected void updateItem(Staff item, boolean empty) {
	            super.updateItem(item, empty);
	            setText(item == null ? "Select staff" : item.getName());
	        }
	    });
	    
	    return staffBox;
	}
		    
	private HBox buildButtonsBox() {
		 //buttons specifications
	    addButton = new Button("Add");
	    addButton.getStyleClass().addAll("page-button-active", "page-button");
	    addButton.setDisable(true); //initialize then change if input is complete
	    
	    updateButton = new Button("Update");
	    updateButton.getStyleClass().addAll("page-button-active", "page-button");
	    updateButton.setDisable(true);
	    
	    deleteButton = new Button("Delete");
	    deleteButton.getStyleClass().addAll("page-button-active", "page-button");
	    deleteButton.setDisable(true);
		
	    //container of all the buttons
	    HBox buttonsBox = new HBox(10, addButton, updateButton, deleteButton);
	    
	    return buttonsBox;
	}
	
	private VBox buildFindInput() {
		//find specifications
	    Label find = new Label("Find");
	    findField = new TextField();
	    findField.setPromptText("Search patient/request/status");
	    findField.setPrefWidth(260);
	    VBox findBox = new VBox(10, find, findField);
	    
	    //search button specifications with funcionality
	    searchButton = new Button("Search");
	    searchButton.getStyleClass().addAll("page-button-active", "page-button");
	    searchButton.setDisable(true);
	    
	    //reset button specifications with functionalities
	    resetButton = new Button("Reset");
	    resetButton.getStyleClass().addAll("page-button-active", "page-button");
	    
	    //find buttons container
	    HBox findButtons = new HBox(10, resetButton, searchButton);
	    findButtons.setAlignment(Pos.BOTTOM_LEFT);
	    
	    VBox findInput = new VBox(20, findBox, findButtons);
	    
	    return findInput;
	}
}