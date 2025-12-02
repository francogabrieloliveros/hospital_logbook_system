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

public class LabExamsPage {
	
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
        ListView<String> listView = new ListView<>();
		listView.getItems().add("LBE-0001 | testType=X-ray| orderingPhysician=Evan | performingStaff=Thea | status=completed\"");
		listView.getStyleClass().add("list-view");
		listView.getStyleClass().add("containers-shadow");
		
		//Labels and TextFields and ComboBoxes and DatePicker and yes
		Label labelLabRequest = new Label("Lab Request:");
		ComboBox<String> labRequest = new ComboBox<>();
		labRequest.getItems().addAll("Request#1", "Request#2", "Request#3");
		labRequest.getSelectionModel().selectFirst();
		
		Label labelPerformingStaff = new Label("Performing Staff:");
		ComboBox<String> performingStaff = new ComboBox<>();
		performingStaff.getItems().addAll("Staff#1", "Staff#2", "Staff#3");
		performingStaff.getSelectionModel().selectFirst();
		
		
		Label labelDate = new Label("Date:");
		DatePicker datePicker = new DatePicker();
		datePicker.setValue(LocalDate.now());
		datePicker.setPromptText("Select Date");
		datePicker.getStyleClass().add("styled-date-picker");
					
		Label labelStatus = new Label("Status:");
		ComboBox<String> cmbStatus = new ComboBox<>();
		cmbStatus.getItems().addAll("Status#1", "Status#2", "Status#3");
		cmbStatus.getSelectionModel().selectFirst();
		
		Label labelRemarks = new Label("Remarks:");
		TextArea txtRemarks = new TextArea();
		txtRemarks.setPrefHeight(150); 
		
		//Buttons
		Button btnRecord = new Button("Record");
		btnRecord.getStyleClass().addAll("page-button","page-button:pressed", "page-button-active", "page-button:hover");
		//onclick here
		Button btnUpdate = new Button("Update");
		btnUpdate.getStyleClass().addAll("page-button","page-button:pressed", "page-button-active", "page-button:hover");
		//onclick here
		
		//vbucks for the form
		VBox labRequestBox = new VBox(5, labelLabRequest, labRequest);
		labRequest.setMaxWidth(Double.MAX_VALUE);
		VBox performingStaffBox = new VBox(5, labelPerformingStaff, performingStaff);
		VBox dateBox= new VBox(5, labelDate, datePicker);
		VBox statusBox = new VBox(5, labelStatus, cmbStatus);
		VBox remarks = new VBox(5, labelRemarks, txtRemarks);
		
		//Grouping buttons
		HBox buttonBox = new HBox(10, btnRecord, btnUpdate);
		buttonBox.setAlignment(Pos.CENTER_RIGHT); // Align buttons to the right

		//Grouping date and status
		HBox grpBox = new HBox(10, dateBox, statusBox);
		
		//one final vbuck for the grouping of all elements inside the form
		VBox recordDetailsFull = new VBox(20, labRequestBox, performingStaffBox, grpBox, remarks, buttonBox);
		recordDetailsFull.getStyleClass().add("record-box");
		recordDetailsFull.setPrefWidth(500); 
		recordDetailsFull.setMaxWidth(400);
		
		//https://stackoverflow.com/questions/29489880/javafx-how-to-make-combobox-hgrow
		HBox mainPage = new HBox(20, listView, recordDetailsFull);
		labRequest.setMaxWidth(Double.MAX_VALUE);
		performingStaff.setMaxWidth(Double.MAX_VALUE);
		HBox.setHgrow(listView, Priority.ALWAYS);
		VBox.setVgrow(mainPage, Priority.ALWAYS);
		
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
		stage.show();
	}

}
