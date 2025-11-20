package application.pages;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.*;

public class LabExams {
	
	public void setStageComponents(Stage stage) {
		VBox root = new VBox();
		root.setPadding(new Insets(25));
		
		
		String[] labels = {"STAFF", "PATIENTS", "LAB EXAMS", "LAB REQUESTS", "LOGBOOK"};
		ArrayList<Button> labelButtons = new ArrayList<>();
		
		for(String label : labels) {
			Button newButton = new Button(label);
			
			if(label.equals("LAB EXAMS")) {
				newButton.getStyleClass().addAll("page-button-active", "page-button");
			} else {
				newButton.getStyleClass().addAll("page-button-inactive", "page-button");
			}
			
			labelButtons.add(newButton);
		}
		
		HBox pageButtons = new HBox(10);
		pageButtons.getChildren().addAll(labelButtons);
		
		
		root.getStyleClass().add("default-bg");
		root.getChildren().addAll(pageButtons);
		Scene staffPageScene = new Scene(root, 1080, 720);
		staffPageScene.getStylesheets().add(getClass().getResource("/application/styles/application.css").toExternalForm());
		stage.setScene(staffPageScene);
		stage.show();
	}

}
