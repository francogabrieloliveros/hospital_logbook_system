package application.pages;

import application.models.*;
import javafx.beans.value.ChangeListener;
import javafx.collections.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.*;

import application.Main;

public class StaffPage {
	
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
		ListView<Staff> listView = new ListView<>();
		listView.getStyleClass().add("list-view");
		listView.getStyleClass().add("containers-shadow");
		
		// Set list view items
		ObservableList<Staff> items = FXCollections.observableArrayList();
		items.setAll(hospital.getStaffs());
		listView.setItems(items);
		
		
		/* ------------------------------------------- LOGGER -------------------------------------------*/
		// Name text field
	    Label name = new Label("Name");
	    TextField nameField = new TextField();
	    nameField.setPromptText("Full name");
	    VBox nameInput = new VBox(20, name, nameField);
	    
	    // Role combo box
	    Label role = new Label("Role");
	    ComboBox<String> roleField = new ComboBox<>();
	    roleField.getItems().addAll("MedTech", "Pathologist", "Phlebotomist", "Clerk", "Other");
	    roleField.setValue("MedTech");
	    VBox roleInput = new VBox(20, role, roleField);
	    roleField.setMaxWidth(Double.MAX_VALUE);
	    
	    // Status combo box
	    Label status = new Label("Status");
	    ComboBox<String> statusField = new ComboBox<>();
	    statusField.getItems().addAll("active", "inactive");
	    statusField.setValue("active");
	    VBox statusInput = new VBox(20, status, statusField);
	    statusField.setMaxWidth(Double.MAX_VALUE);

	    // Place role and status in hbox for formatting
	    HBox roleAndStatus = new HBox(20, roleInput, statusInput);
	    HBox.setHgrow(roleInput, Priority.ALWAYS);
	    HBox.setHgrow(statusInput, Priority.ALWAYS);
	    
	    // Logger buttons for adding new staff
	    Button addButton = new Button("Add");
	    addButton.getStyleClass().addAll("page-button-active", "page-button");
	    Button updateButton = new Button("Update");
	    updateButton.getStyleClass().addAll("page-button-active", "page-button");
	    Button deleteButton = new Button("Delete");
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
	    TextField findField = new TextField();
	    findField.setPromptText("Search name/role/status");
	    
	    // Finding/search buttons
	    Button searchButton = new Button("Search");
	    searchButton.getStyleClass().addAll("page-button-active", "page-button");
	    Button resetButton = new Button("Reset");
	    resetButton.getStyleClass().addAll("page-button-active", "page-button");
	    HBox findButtons = new HBox(10, resetButton, searchButton);
	    
	    // Disable Buttons
	    searchButton.setDisable(true);
	    resetButton.setDisable(true);
	    
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
		Runnable resetInputFields = () -> {
			nameField.setText("");
			roleField.setValue("MedTech");
			statusField.setValue("active");
			findField.setText("");
		};
		
		// Set input field values to selected values
		listView.getSelectionModel().selectedItemProperty().addListener((obs, old, selected) -> {
		    if (selected != null) {
		        nameField.setText(selected.getName());
		        roleField.setValue(selected.getRole());
		        statusField.setValue(selected.getStatus());
		    }
		});
		
		// Disable buttons if nameField is empty
		nameField.textProperty().addListener((a, oldVal, newVal) -> {
			if(newVal.isBlank()) {
				addButton.setDisable(true);
				updateButton.setDisable(true);
				deleteButton.setDisable(true);
			} else {
				addButton.setDisable(false);
				updateButton.setDisable(false);
				deleteButton.setDisable(false);
			}
		});
		
		// Disable buttons if findField is empty
		findField.textProperty().addListener((a, oldVal, newVal) -> {
			if(newVal.isBlank()) {
				searchButton.setDisable(true);
				resetButton.setDisable(true);
			} else {
				searchButton.setDisable(false);
				resetButton.setDisable(false);
			}
		});
		
		// Adds new staff to hospital and list view
		addButton.setOnAction(e -> {
			hospital.addStaff(new Staff(hospital, 
										nameField.getText(), 
										roleField.getValue(), 
										statusField.getValue()));
			
			items.setAll(hospital.getStaffs());
			resetInputFields.run();
		});
		
		// Updates selected staff from hospital staffs and updates list view
		updateButton.setOnAction(e -> {
			Staff selected = listView.getSelectionModel().getSelectedItem();
					
			if (selected != null) {
				selected.update(nameField.getText(), roleField.getValue(), statusField.getValue());
				items.setAll(hospital.getStaffs());
				resetInputFields.run();
			}
		});
		
		// Removes selected staff from hospital staffs and updates list view
		deleteButton.setOnAction(e -> {
			Staff selected = listView.getSelectionModel().getSelectedItem();
			
			if(selected != null) {
				hospital.removeStaff(selected);
				items.setAll(hospital.getStaffs());
				resetInputFields.run();
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
			resetInputFields.run();
		});
		
		resetButton.setOnAction(e -> { resetInputFields.run(); });

		
		/* ------------------------------------------- ROOT & SCENE -------------------------------------------*/
		// Create root
		VBox root = new VBox(20, pageButtons, mainLedger);
		root.setPadding(new Insets(50));
		root.getStyleClass().add("default-bg");
		
		// Create scene and add styles
		Scene staffPageScene = new Scene(root, 1200, 720);
		staffPageScene.getStylesheets().add(getClass().getResource("/application/styles/StaffPage.css").toExternalForm());
		staffPageScene.getStylesheets().add(getClass().getResource("/application/styles/application.css").toExternalForm());
		
		stage.setScene(staffPageScene);
		stage.show();
	}
}
