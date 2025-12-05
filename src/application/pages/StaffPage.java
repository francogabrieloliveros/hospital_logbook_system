package application.pages;

import application.models.*;
import javafx.collections.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.*;

import application.Main;

public class StaffPage {
	
    private TextField nameField = new TextField();
    private ComboBox<String> roleField = new ComboBox<>();
    private ComboBox<String> statusField = new ComboBox<>();
    private Button addButton = new Button("Add");
    private Button updateButton = new Button("Update");
    private Button deleteButton = new Button("Delete");
    private TextField findField = new TextField();
    private Button searchButton = new Button("Search");
    private Button resetButton = new Button("Reset");
    private ListView<Staff> listView = new ListView<>();
	private Hospital hospital;
	
	public StaffPage(Hospital hospital) { this.hospital = hospital; }
	
	public void setStageComponents(Stage stage, Main main) {
		
		/* ------------------------------------------- HEADER BUTTONS -------------------------------------------*/
		String[] labels = {"STAFF", "PATIENTS", "LAB EXAMS", "LAB REQUESTS", "LOGBOOK"};
		ArrayList<Button> labelButtons = new ArrayList<>();
		for(String label : labels) {
			Button newButton = new Button(label);
			
			// Only STAFF button is active
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
		
		
		/* ------------------------------------------- LIST VIEW -------------------------------------------*/
		// List view
		listView.getStyleClass().add("list-view");
		listView.getStyleClass().add("containers-shadow");
		
		// Set list view items
		ObservableList<Staff> items = FXCollections.observableArrayList();
		items.setAll(hospital.getStaffs());
		listView.setItems(items);
		
		
		/* ------------------------------------------- LOGGER -------------------------------------------*/
		// Name text field
	    Label name = new Label("Name");
	    nameField.setPromptText("Full name");
	    VBox nameInput = new VBox(20, name, nameField);
	    
	    // Role combo box
	    Label role = new Label("Role");
	    roleField.getItems().addAll("Select role", "MedTech", "Pathologist", "Phlebotomist", "Clerk", "Other");
	    roleField.setValue("Select role");
	    VBox roleInput = new VBox(20, role, roleField);
	    roleField.setMaxWidth(Double.MAX_VALUE);
	    
	    // Status combo box
	    Label status = new Label("Status");
	    statusField.getItems().addAll("Select status", "active", "inactive");
	    statusField.setValue("Select status");
	    VBox statusInput = new VBox(20, status, statusField);
	    statusField.setMaxWidth(Double.MAX_VALUE);

	    // Place role and status in hbox for formatting
	    HBox roleAndStatus = new HBox(20, roleInput, statusInput);
	    HBox.setHgrow(roleInput, Priority.ALWAYS);
	    HBox.setHgrow(statusInput, Priority.ALWAYS);
	    
	    // Logger buttons for adding new staff
	    addButton.getStyleClass().addAll("page-button-active", "page-button");
	    updateButton.getStyleClass().addAll("page-button-active", "page-button");
	    deleteButton.getStyleClass().addAll("page-button-active", "page-button");
	    HBox loggerButtons = new HBox(10, addButton, updateButton, deleteButton);
	    
	    // Disable buttons
	    addButton.setDisable(true);
		updateButton.setDisable(true);
		deleteButton.setDisable(true);
	    
	    Separator separator = new Separator();
	    
	    
	    /* ------------------------------------------- FINDING -------------------------------------------*/
	    // Find text input
	    Label find = new Label("Find");
	    findField.setPromptText("Search name/role/status");
	    
	    // Finding/search buttons
	    searchButton.getStyleClass().addAll("page-button-active", "page-button");
	    resetButton.getStyleClass().addAll("page-button-active", "page-button");
	    HBox findButtons = new HBox(10, resetButton, searchButton);
	    
	    // Disable Buttons
	    searchButton.setDisable(true);
	    
	    // Finding section
	    VBox findInput = new VBox(20, find, findField, findButtons);
		
	    // Adding all inputs in a vbox
	    VBox logger = new VBox(30, nameInput, roleAndStatus, loggerButtons, separator, findInput);
	    logger.getStyleClass().addAll("logger", "containers-shadow");
	    
	    // Mainpage contents
		HBox mainLedger = new HBox(50, listView, logger); // refactor HBox main -> mainLedger
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
		
		// Disable buttons if logger inputs is empty
		nameField.textProperty().addListener((a, b, c) -> updateLoggerButtons());
		roleField.valueProperty().addListener((a, b, c) -> updateLoggerButtons());
		statusField.valueProperty().addListener((a, b, c) ->updateLoggerButtons());
		
		// Disable buttons if findField is empty
		findField.textProperty().addListener((a, b, c) -> updateFindButtons());
		
		// Adds new staff to hospital and list view
		addButton.setOnAction(e -> {
			hospital.addStaff(new Staff(hospital, 
										nameField.getText(), 
										roleField.getValue(), 
										statusField.getValue()));
			
			items.setAll(hospital.getStaffs());
			resetInputFields();
		});
		
		// Updates selected staff from hospital staffs and updates list view
		updateButton.setOnAction(e -> {
			Staff selected = listView.getSelectionModel().getSelectedItem();
					
			if (selected != null) {
				selected.update(nameField.getText(), roleField.getValue(), statusField.getValue());
				items.setAll(hospital.getStaffs());
				resetInputFields();
			}
		});
		
		// Removes selected staff from hospital staffs and updates list view
		deleteButton.setOnAction(e -> {
			Staff selected = listView.getSelectionModel().getSelectedItem();
			
			if(selected != null) {
				hospital.removeStaff(selected);
				items.setAll(hospital.getStaffs());
				resetInputFields();
			}
		});
		
		// Sets item view to search results
		searchButton.setOnAction(e -> {
			ArrayList<Staff> searchResults = new ArrayList<Staff>();
			String query = findField.getText();
			
			for(Staff staff : hospital.getStaffs()) {
				if(staff.getName().equals(query) ||
				   staff.getRole().equals(query) ||
				   staff.getStatus().equals(query)) {
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
		
		/* ------------------------------------------- ROOT & SCENE -------------------------------------------*/
		// Create root
		VBox root = new VBox(20, pageButtons, mainLedger);
		root.setPadding(new Insets(50));
		root.getStyleClass().add("default-bg");
		
		// Remove listview selection on keypress ESC
		root.setOnKeyPressed(e -> {
		    if (e.getCode() == javafx.scene.input.KeyCode.ESCAPE) {
		        listView.getSelectionModel().clearSelection();
		    }
		});
		listView.setOnKeyPressed(e -> {
		    if (e.getCode() == javafx.scene.input.KeyCode.ESCAPE) {
		        listView.getSelectionModel().clearSelection();
		    }
		});
		
		
		// Create scene and add styles
		Scene staffPageScene = new Scene(root, 1200, 720);
		staffPageScene.getStylesheets().add(getClass().getResource("/application/styles/StaffPage.css").toExternalForm());
		staffPageScene.getStylesheets().add(getClass().getResource("/application/styles/application.css").toExternalForm());
		
		stage.setScene(staffPageScene);
		stage.show();
	}
	
	private void updateLoggerButtons() {
	    boolean nameFilled = !nameField.getText().isBlank();
	    boolean roleFilled = roleField.getValue() != "Select role";
	    boolean statusFilled = statusField.getValue() != "Select status";
	    boolean listViewSelected = listView.getSelectionModel().getSelectedItem() != null;

	    if (nameFilled && roleFilled && statusFilled && listViewSelected) {
	    	addButton.setDisable(true);
			updateButton.setDisable(false);
			deleteButton.setDisable(false);
	    } else if (nameFilled && roleFilled && statusFilled && !listViewSelected) {
	    	addButton.setDisable(false);
			updateButton.setDisable(true);
			deleteButton.setDisable(true);
	    } else {
	    	addButton.setDisable(true);
			updateButton.setDisable(true);
			deleteButton.setDisable(true);
	    }
	}
	
	private void updateFindButtons() {
	    boolean findFilled = !findField.getText().isBlank();

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
}
