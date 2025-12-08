package application.pages;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

import java.util.ArrayList;

import application.Main;

public class HeaderButtons {
	
	private ArrayList<Button> labelButtons;
	private HBox pageButtons;
	
	public HeaderButtons(Main main, String pageName){
		this.labelButtons = new ArrayList<>();
		this.pageButtons = new HBox(10);
		
		String[] labels = {"STAFF", "PATIENTS", "LAB EXAMS", "LAB REQUESTS", "LOGBOOK", "DASHBOARD"};
		for(String label : labels) {
			Button newButton = new Button(label);
			
			// Only pageName button is active
			if(label.equals(pageName)) {
				newButton.getStyleClass().addAll("page-button-active", "page-button"); 
			} else {
				newButton.getStyleClass().addAll("page-button-inactive", "page-button"); // added functionality (change pages)
				newButton.setOnAction(e -> main.switchPage(label));
			}
			labelButtons.add(newButton);
		}
		
		pageButtons.getChildren().addAll(labelButtons);
		HBox.setMargin(pageButtons, new Insets(20));
	}
	
	public HBox get() { return this.pageButtons; }
}
