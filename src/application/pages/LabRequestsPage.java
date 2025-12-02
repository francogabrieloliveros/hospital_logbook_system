package application.pages;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.*;

import application.Main;
import application.models.Hospital;

public class LabRequestsPage {
	//butotns above
	
	private Hospital hospital;
	
	public LabRequestsPage(Hospital hospital) { this.hospital = hospital; }
	
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
		
		// Work here
		
		//main container
		VBox root = new VBox(10, pageButtons); // Add other elements here
		root.getStyleClass().add("default-bg");
		root.setPadding(new Insets(50));
		Scene scene = new Scene(root, 1200, 700);
		// add LabRequests css
		scene.getStylesheets().add(getClass().getResource("/application/styles/application.css").toExternalForm());
		
		stage.setScene(scene);
		stage.setTitle("Log Book System");
		stage.show();
	}

}
