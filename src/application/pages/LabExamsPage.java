package application.pages;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.*;

import application.Main;
import application.models.Hospital;

public class LabExamsPage {
	
	private Hospital hospital;
	
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
		//need to fix the format
		Label labelLabTest = new Label("Laboratory Test:");
		TextField txtLabTest = new TextField();
		Label labelTestType = new Label("Test Type:");
		TextField txtTestType= new TextField();
		Label labelRemarks= new Label("Remarks:");
		TextField txtRemark= new TextField();
		VBox recordDetails = new VBox(10, labelLabTest, txtLabTest, labelTestType, txtTestType, labelRemarks, txtRemark);
		
		Label labelDate = new Label("Date:");
		TextField txtDate = new TextField();
		Label labelOrderingPhysician = new Label("Ordering Physician:");
		TextField txtOrderingPhysician= new TextField();
		Label labelPerformingStaff = new Label("Performing Staff:");
		TextField txtPerformingStaff= new TextField();
		VBox recordDetails2 = new VBox(10, labelDate, txtDate, labelOrderingPhysician, txtOrderingPhysician, labelPerformingStaff, txtPerformingStaff);
		
		Button btnRecord = new Button("Record");
		btnRecord.getStyleClass().addAll("page-button","page-button:pressed", "page-button-active", "page-button:hover");
		//onclick here
		Button btnUpdate = new Button("Update");
		btnUpdate.getStyleClass().addAll("page-button","page-button:pressed", "page-button-active", "page-button:hover");
		//onclick here
		
		HBox recordDetailsFull = new HBox(10, recordDetails, recordDetails2, btnRecord, btnUpdate);
		
        ListView<String> listView = new ListView<>();
		listView.getItems().add("STF-0001 | fullName=Mylene | role=MedTe");
		listView.getStyleClass().add("list-view");
		listView.getStyleClass().add("containers-shadow");
		listView.setPrefHeight(400);
		//main container
		VBox root = new VBox(10, pageButtons, recordDetailsFull ,listView); // Add other elements here
		root.getStyleClass().add("default-bg");
		root.setPadding(new Insets(50));
		Scene scene = new Scene(root, 1200, 700);
		// Add LabExams css
		scene.getStylesheets().add(getClass().getResource("/application/styles/application.css").toExternalForm());
		scene.getStylesheets().add(getClass().getResource("/application/styles/LabExamsPage.css").toExternalForm());
		stage.setScene(scene);
		stage.setTitle("Log Book System");
		stage.show();
	}

}
