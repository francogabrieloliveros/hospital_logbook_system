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

public class StaffPage {
	
	private Hospital hospital;
	
	// Form input components
	private ObservableList<Staff> items;
	private ListView<Staff> listView;
	private ComboBox<String> roleField;
    private ComboBox<String> statusField;
    private TextField nameField;
    private TextField findField;
    private Button addButton;
    private Button updateButton;
    private Button deleteButton;
    private Button searchButton;
    private Button resetButton;
    
    
	
	public StaffPage(Hospital hospital) { this.hospital = hospital; }
	
	public void setStageComponents(Stage stage, Main main) {
		
		HBox pageButtons = new HeaderButtons(main, "STAFF").get(); // page switching header
		
	    /* ------------------------------------------- MAINPAGE CONTENTS-------------------------------------------*/
		// Light panel
		buildListView();
		
		// Right panel
		VBox nameInput = buildNameInput();
		HBox roleAndStatus = buildRoleAndStatus();
		HBox loggerButtons = buildLoggerButtons();
	    VBox findInput = buildFindInput();
	    
	    VBox logger = new VBox(30, nameInput, roleAndStatus, loggerButtons, new Separator(), findInput);
	    logger.getStyleClass().addAll("logger", "containers-shadow");
		
	    // Main
		HBox mainLedger = new HBox(50, listView, logger);
		HBox.setHgrow(listView, Priority.ALWAYS);
		HBox.setHgrow(logger, Priority.ALWAYS);
		VBox.setVgrow(mainLedger, Priority.ALWAYS);
		
		// Ensure that listView and Logger occupy 50% of window width
		listView.prefWidthProperty().bind(mainLedger.widthProperty().subtract(50).divide(2));
		logger.prefWidthProperty().bind(mainLedger.widthProperty().subtract(50).divide(2));
		
		/* ------------------------------------------- FUNCTIONALITY -------------------------------------------*/
		// Set input field values to selected values
		listView.getSelectionModel().selectedItemProperty().addListener((a, b, selected) -> {
		    if (selected != null) {
		        nameField.setText(selected.getName());
		        roleField.setValue(selected.getRole());
		        statusField.setValue(selected.getStatus());
		    } else {
		    	resetInputFields();
		    }
		});
		
		// Update buttons if logger inputs is empty
		nameField.textProperty().addListener((a, b, c) -> updateLoggerButtons());
		roleField.valueProperty().addListener((a, b, c) -> updateLoggerButtons());
		statusField.valueProperty().addListener((a, b, c) ->updateLoggerButtons());
		
		// Update buttons if findField is empty
		findField.textProperty().addListener((a, b, c) -> updateFindButtons());
		
		// Adds new staff to hospital and list view
		addButton.setOnAction(e -> {
			hospital.addStaff(new Staff(hospital, 
										nameField.getText(), 
										roleField.getValue(), 
										statusField.getValue()));
			
			items.setAll(hospital.getStaffs());
			resetInputFields();
			showAlert("Successful!","Successfully added new staff.");
		});
		
		// Updates selected staff from hospital staffs and updates list view
		updateButton.setOnAction(e -> {
			Staff selected = listView.getSelectionModel().getSelectedItem();
					
			if (selected != null) {
				selected.update(nameField.getText(), roleField.getValue(), statusField.getValue());
				items.setAll(hospital.getStaffs());
				resetInputFields();
				showAlert("Successful!","Successfully updated staff information.");
			}
		});
		
		// Removes selected staff from hospital staffs and updates list view
		deleteButton.setOnAction(e -> {
			Staff selected = listView.getSelectionModel().getSelectedItem();
			
			if(selected != null) {
				selected.delete();
				items.setAll(hospital.getStaffs());
				resetInputFields();
				showAlert("Successful!","Successfully deleted staff.");
			}
		});
		
		// Sets item view to search results
		searchButton.setOnAction(e -> {
			ArrayList<Staff> searchResults = new ArrayList<Staff>();
			String query = findField.getText().trim();
			
			for(Staff staff : hospital.getStaffs()) {
				if(staff.getName().contains(query) ||
				   staff.getRole().contains(query) ||
				   staff.getStatus().contains(query)) {
					searchResults.add(staff);
				}
			}
			
			items.setAll(searchResults);
			resetInputFields();
		});
		
		resetButton.setOnAction(e -> {
			resetInputFields();
			
			// Reset listview to show all staffs
			items.setAll(hospital.getStaffs());
			listView.setItems(items);
		});
		
		// Remove listview selection on keypress ESC
		listView.setOnKeyPressed(e -> deselectOnEsc(e));
		
		/* ------------------------------------------- ROOT & SCENE -------------------------------------------*/
		// Create root
		VBox root = new VBox(20, pageButtons, mainLedger);
		root.setPadding(new Insets(50));
		root.getStyleClass().add("default-bg");
		root.setOnKeyPressed(e -> deselectOnEsc(e));
		
		// Create scene and add styles
		Scene staffPageScene = new Scene(root, 1200, 720);
		staffPageScene.getStylesheets().add(getClass().getResource("/application/styles/application.css").toExternalForm());
		staffPageScene.getStylesheets().add(getClass().getResource("/application/styles/StaffPage.css").toExternalForm());
		stage.setScene(staffPageScene);
		stage.setTitle("Staff");
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
		roleField.setValue("Select role");
		statusField.setValue("Select status");
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
		Staff selected = listView.getSelectionModel().getSelectedItem();
	    boolean nameFilled = !nameField.getText().isBlank();
	    boolean roleFilled = roleField.getValue() != "Select role";
	    boolean statusFilled = statusField.getValue() != "Select status";
	    boolean listViewSelected =  selected != null;

	    if (nameFilled && roleFilled && statusFilled && listViewSelected) {
	    	// Enable update and delete button when listViewItem selected
	    	nameField.setDisable(true);
	    	roleField.setDisable(true);
	    	statusField.setDisable(false);
	    	addButton.setDisable(true);
			updateButton.setDisable(false || selected.isOwned);
			deleteButton.setDisable(false || selected.isOwned);
	    } else if (nameFilled && roleFilled && statusFilled && !listViewSelected) {
	    	// Enable add button when no listViewItem selected (new item)
	    	nameField.setDisable(false);
	    	roleField.setDisable(false);
	    	statusField.setDisable(false);
	    	addButton.setDisable(false);
			updateButton.setDisable(true);
			deleteButton.setDisable(true);
	    } else {
	    	nameField.setDisable(false);
	    	roleField.setDisable(false);
	    	statusField.setDisable(false);
	    	addButton.setDisable(true);
			updateButton.setDisable(true);
			deleteButton.setDisable(true);
	    }
	}
	
	/* ------------------------------------------- BUILDERS -------------------------------------------*/
	private void buildListView(){
		listView = new ListView<>();
		listView.getStyleClass().add("list-view");
		listView.getStyleClass().add("containers-shadow");
		
		// Set list view items
		items = FXCollections.observableArrayList();
		items.setAll(hospital.getStaffs());
		listView.setItems(items);
	}
	
	private VBox buildNameInput() {
		Label name = new Label("Name");
	    nameField = new TextField();
	    nameField.setPromptText("Full name");
	    VBox nameInput = new VBox(20, name, nameField);
	    
	    return nameInput;
	}
	
	private HBox buildRoleAndStatus() {
		 // Role combo box
	    Label role = new Label("Role");
	    roleField = new ComboBox<>();
	    roleField.getItems().setAll("Select role", 
	    		                    "MedTech", 
	    		                    "Pathologist", 
	    		                    "Phlebotomist", 
	    		                    "RadTech", 
	    		                    "Clerk", 
	    		                    "Other");
	    roleField.setValue("Select role");
	    VBox roleInput = new VBox(20, role, roleField);
	    roleField.setMaxWidth(Double.MAX_VALUE);
	    
	    // Status combo box
	    Label status = new Label("Status");
	    statusField = new ComboBox<>();
	    statusField.getItems().setAll("Select status", "active", "inactive");
	    statusField.setValue("Select status");
	    VBox statusInput = new VBox(20, status, statusField);
	    statusField.setMaxWidth(Double.MAX_VALUE);

	    // Place role and status in hbox for formatting
	    HBox roleAndStatus = new HBox(20, roleInput, statusInput);
	    HBox.setHgrow(roleInput, Priority.ALWAYS);
	    HBox.setHgrow(statusInput, Priority.ALWAYS);
	    
	    return roleAndStatus;
	}
	
	private HBox buildLoggerButtons() {
		// Logger buttons for staff
	    addButton = new Button("Add");
	    updateButton = new Button("Update");
	    deleteButton = new Button("Delete");
	    addButton.getStyleClass().addAll("page-button-active", "page-button");
	    updateButton.getStyleClass().addAll("page-button-active", "page-button");
	    deleteButton.getStyleClass().addAll("page-button-active", "page-button");
	    HBox loggerButtons = new HBox(10, addButton, updateButton, deleteButton);
	    
	    // Disable buttons
	    addButton.setDisable(true);
		updateButton.setDisable(true);
		deleteButton.setDisable(true);
		
		return loggerButtons;
	} 
	
	private VBox buildFindInput() {
		Label find = new Label("Find");
	    findField = new TextField();
	    findField.setPromptText("Search name/role/status");
	    
	    // Finding/search buttons
		searchButton = new Button("Search");
		resetButton = new Button("Reset");
	    searchButton.getStyleClass().addAll("page-button-active", "page-button");
	    resetButton.getStyleClass().addAll("page-button-active", "page-button");
	    HBox findButtons = new HBox(10, resetButton, searchButton);
	    
	    searchButton.setDisable(true);
	    VBox findInput = new VBox(20, find, findField, findButtons);// Finding section
		return findInput;
	}
	
}
