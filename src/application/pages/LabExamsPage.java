package application.pages;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.*;

import application.Main;
import application.models.Hospital;
import application.models.LabExam;
import application.models.LabRequest;
import application.models.Patient;
import application.models.Staff;

public class LabExamsPage {
	
	//initialize the attributes here pare magamit sa methods
	private Hospital hospital;
	private ComboBox<String> labRequest;
	private ComboBox<String> performingStaff;
	private DatePicker datePicker;
	private ComboBox<String> cmbStatus;
	private TextArea txtResults;
	private TextArea txtRemarks;
	private Button btnRecord;
	private Button btnUpdate;
	private ListView<LabExam> listView;

	
	public LabExamsPage(Hospital hospital) { this.hospital = hospital; }
	
	public void setStageComponents(Stage stage, Main main) {
		String[] labels = {"STAFF", "PATIENTS", "LAB EXAMS", "LAB REQUESTS", "LOGBOOK"};
		ArrayList<Button> labelButtons = new ArrayList<>();
		for(String label : labels) {
			Button newButton = new Button(label);
			
			if(label.equals("LAB EXAMS")) {
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
		
		// Work here
		
		//listView
        listView = new ListView<>();
		listView.getStyleClass().add("list-view");
		listView.getStyleClass().add("containers-shadow");
		
		ObservableList<LabExam> items = FXCollections.observableArrayList();
		items.setAll(this.hospital.getLabExams());
		listView.setItems(items);

		//Labels and TextFields and ComboBoxes and DatePicker and yes
		Label labelLabRequest = new Label("Lab Request:");
		labRequest = new ComboBox<>();
		for (LabRequest labRequests: this.hospital.getLabRequests()) {
			labRequest.getItems().add(String.format("%s | ordering physician: %s",labRequests.getID(), labRequests.getStaff()));
		}
		
		Label labelPerformingStaff = new Label("Performing Staff:");
		performingStaff = new ComboBox<>();
		performingStaff.getItems().add("hotdog");
		
		Label labelDate = new Label("Date:");
		datePicker = new DatePicker();
		datePicker.setValue(LocalDate.now());
		datePicker.setPromptText("Select Date");
		datePicker.getStyleClass().add("styled-date-picker");
		
		Label labelStatus = new Label("Status:");
		cmbStatus = new ComboBox<>();
		cmbStatus.getItems().addAll("Finished", "In-Progress", "Cancelled");
		
		Label labelResults= new Label("Results:");
		txtResults= new TextArea();
		txtResults.setPrefHeight(75); 
		
		Label labelRemarks = new Label("Remarks:");
		txtRemarks = new TextArea();
		txtRemarks.setPrefHeight(150); 
		
		//Buttons
		btnRecord = new Button("Record");
		btnRecord.getStyleClass().addAll("page-button","page-button:pressed", "page-button-active", "page-button:hover");
		btnRecord.setDisable(true);
		btnUpdate = new Button("Update");
		btnUpdate.getStyleClass().addAll("page-button","page-button:pressed", "page-button-active", "page-button:hover");
		btnUpdate.setDisable(true);
		
		//vbucks for the form
		VBox labRequestBox = new VBox(5, labelLabRequest, labRequest);
		labRequest.setMaxWidth(Double.MAX_VALUE);
		VBox performingStaffBox = new VBox(5, labelPerformingStaff, performingStaff);
		VBox dateBox= new VBox(5, labelDate, datePicker);
		VBox statusBox = new VBox(5, labelStatus, cmbStatus);
		VBox results = new VBox(5, labelResults, txtResults);
		VBox remarks = new VBox(5, labelRemarks, txtRemarks);
		
		//Grouping buttons
		HBox buttonBox = new HBox(10, btnRecord, btnUpdate);
		buttonBox.setAlignment(Pos.CENTER_RIGHT); // Align buttons to the right

		//Grouping date and status
		HBox grpBox = new HBox(10, dateBox, statusBox);
		
		//one final vbuck for the grouping of all elements inside the form
		VBox recordDetailsFull = new VBox(20, labRequestBox, performingStaffBox, grpBox, results ,remarks, buttonBox);
		recordDetailsFull.getStyleClass().add("record-box");
		recordDetailsFull.setPrefWidth(500); 
		recordDetailsFull.setMaxWidth(400);
		
		//https://stackoverflow.com/questions/29489880/javafx-how-to-make-combobox-hgrow
		HBox mainPage = new HBox(20, listView, recordDetailsFull);
		labRequest.setMaxWidth(Double.MAX_VALUE);
		performingStaff.setMaxWidth(Double.MAX_VALUE);
		HBox.setHgrow(listView, Priority.ALWAYS);
		VBox.setVgrow(mainPage, Priority.ALWAYS);
		
		
		//functionality
		//disable and enable fields

		txtResults.setDisable(true);
		txtRemarks.setDisable(true);
		
		labRequest.valueProperty().addListener((a, b, c) -> fieldStatusUpdater());
		performingStaff.valueProperty().addListener((a, b, c) -> fieldStatusUpdater());
		cmbStatus.valueProperty().addListener((a, b, c) -> fieldStatusUpdater());
		datePicker.valueProperty().addListener((a, b, c) -> fieldStatusUpdater());
		txtResults.textProperty().addListener((a, b, c) -> fieldStatusUpdater());
		txtRemarks.textProperty().addListener((a, b, c) -> fieldStatusUpdater());
		
		listView.setOnMouseClicked(event -> {
		    LabExam selectedExam = listView.getSelectionModel().getSelectedItem();
		    if (selectedExam != null) {
		        fillExamFields(selectedExam); // fill the fields
		        updateFieldStatusForExam(selectedExam); // enable/disable fields/buttons
		    }//bug found: when u select one cell, then switch back and forth, the performingStaff disappears.
		});
		
		labRequest.valueProperty().addListener((obs, oldVal, newVal) -> {
		    LabRequest selectedLabRequest = getSelectedLabRequest();
		    boolean examSelected = listView.getSelectionModel().getSelectedItem() != null;
		    if(selectedLabRequest != null && !examSelected) {
		        ObservableList<String> compStaff = FXCollections.observableArrayList();
		        compStaff.setAll(getCompStaff(selectedLabRequest));
		        performingStaff.setItems(compStaff);
		        performingStaff.getSelectionModel().clearSelection();
		    }
		});
		
		//main container
		VBox root = new VBox(20, pageButtons, mainPage); // Add other elements here
		root.getStyleClass().add("default-bg");
		root.setPadding(new Insets(50));
		Scene scene = new Scene(root, 1200, 700);
		// Add LabExams css
		scene.getStylesheets().add(getClass().getResource("/application/styles/application.css").toExternalForm());
		scene.getStylesheets().add(getClass().getResource("/application/styles/LabExamsPage.css").toExternalForm());
		stage.setScene(scene);
		stage.setTitle("Lab Exams View");
		fieldStatusUpdater();
		stage.show();
	}
	
	private void fieldStatusUpdater() {
	    boolean labRequestSelected = labRequest.getSelectionModel().getSelectedIndex() >= 0;
	    boolean performingStaffSelected = performingStaff.getSelectionModel().getSelectedIndex() >= 0;
	    boolean dateSelected = datePicker.getValue() != null;
	    boolean statusSelected = cmbStatus.getSelectionModel().getSelectedIndex() >= 0;
	    boolean examSelected = listView.getSelectionModel().getSelectedItem() != null;
	    
	    // ComboBox Selection (Record)
	    if (labRequestSelected && !examSelected) {
	        performingStaff.setDisable(false);
	        datePicker.setDisable(false);
	        cmbStatus.setDisable(false);
	        
	        //optional fields
	        txtResults.setDisable(false); 
	        txtRemarks.setDisable(false); 

	        // Record button enabled only if required fields are filled
	        btnRecord.setDisable(!(performingStaffSelected && dateSelected && statusSelected));
	        btnUpdate.setDisable(true); // nothing selected to update
	    }
	    // nothing selected
	    else if(!labRequestSelected && !examSelected){
	        performingStaff.setDisable(true);
	        datePicker.setDisable(true);
	        cmbStatus.setDisable(true);
	        txtResults.setDisable(true);
	        txtRemarks.setDisable(true);
	        btnRecord.setDisable(true);
	        btnUpdate.setDisable(true);
	        labRequest.setDisable(false);
	    }
	}
	
	//fills the field when theres a selection from the listView
	private void fillExamFields(LabExam exam) {
	    LabRequest request = exam.getLabRequest();
	    labRequest.setValue(String.format("%s | ordering physician: %s", request.getID(), request.getStaff()));
	    performingStaff.setValue(exam.getPerformingStaff().getName());
	    datePicker.setValue(exam.getDate());
	    cmbStatus.setValue(exam.getStatus());
	    
	    if (exam.getResults() != null) {txtResults.setText(exam.getResults());} 
	    else {txtResults.setText("");}

	    if (exam.getRemarks() != null) {txtRemarks.setText(exam.getRemarks());} 
	    else {txtRemarks.setText("");}
	}
	
	//updates the status of the fields and buttons when a selection is made from the listView
	private void updateFieldStatusForExam(LabExam exam) {
		//cannot edit the labRequest and Staff, and also no record button here
	    labRequest.setDisable(true);
	    performingStaff.setDisable(true);
	    btnRecord.setDisable(true);
	    
	    //if the exam is cancelled, no more actions can be done
	    if (exam.getStatus().equalsIgnoreCase("Cancelled")) {
	        datePicker.setDisable(true);
	        cmbStatus.setDisable(true);
	        txtResults.setDisable(true);
	        txtRemarks.setDisable(true);
	        btnUpdate.setDisable(true);
	    } else {
	        datePicker.setDisable(false);
	        cmbStatus.setDisable(false);
	        txtResults.setDisable(false);
	        txtRemarks.setDisable(false);
	        btnUpdate.setDisable(false);
	    }
	}
	
	private ArrayList<String> getCompStaff(LabRequest labRequest){
	    ArrayList<String> compatibleStaffNames = new ArrayList<>();
	    if(labRequest == null) return compatibleStaffNames; // nothing selected yet

	    String testType = labRequest.getRequest(); 

	    for(Staff staff : hospital.getStaffs()) {
	    	if(staff.getStatus().equals("inactive")) {//only compatible if the staff is inactive, and meets the required type of test.
	    		switch(staff.getRole()) {
	            	case "MedTech" -> {
	                	if(testType.equals("CBC") || testType.equals("PCR"))
	                		compatibleStaffNames.add(staff.getName());
	            	}
	            	case "Pathologist" -> {
	                	if(testType.equals("Urinalysis"))
	                		compatibleStaffNames.add(staff.getName());
	            	}
	            	case "Phlebotomist" -> {
	                	if(testType.equals("FBS") || testType.equals("Lipid Profile"))
	                    	compatibleStaffNames.add(staff.getName());
	            	}
	            	case "RadTech" -> {
	                	if(testType.equals("X-RAY"))
	                		compatibleStaffNames.add(staff.getName());
	            	}
	        	}
	    	}
	    }
	    
	    return compatibleStaffNames;
	}
	
	private LabRequest getSelectedLabRequest() {
		String selectedText = labRequest.getSelectionModel().getSelectedItem();
	    String selectedID = selectedText.split(" \\| ")[0]; // extract ID part
        LabRequest selectedLabRequest = null;
        
        for (LabRequest lr : hospital.getLabRequests()) {
            if (lr.getID().equals(selectedID)) {
                selectedLabRequest = lr;
                break;
            }
        }
        if(selectedLabRequest != null) {
        	return selectedLabRequest;
        }
        
		return null;
	}
	//performingStaff need to check if the lab test is appropriate	
}
