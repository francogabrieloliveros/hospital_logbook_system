package application.pages;

import java.time.LocalDate;
import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LogBookViewPage {
	public void setStageComponents(Stage stage) {
		String[] labels = {"STAFF", "PATIENTS", "LAB EXAMS", "LAB REQUESTS", "LOGBOOK"};
		ArrayList<Button> labelButtons = new ArrayList<>();
		for(String label : labels) {
			Button newButton = new Button(label);
			
			if(label.equals("LOGBOOK")) {
				newButton.getStyleClass().addAll("page-button-active", "page-button");
			} else {
				newButton.getStyleClass().addAll("page-button-inactive", "page-button");
			}
			
			labelButtons.add(newButton);
		}
		
		HBox pageButtons = new HBox(10);
		pageButtons.getChildren().addAll(labelButtons);
		HBox.setMargin(pageButtons, new Insets(20));
		
		//author label
		Label authorLabel = new Label("Author:");
		TextField authorField = new TextField();
		authorField.setPromptText("Enter author name");
		VBox authorBox = new VBox(5, authorLabel, authorField);
		authorBox.setAlignment(Pos.BOTTOM_LEFT);
		
		//tag label
		Label tagLabel = new Label("Tag:");
		TextField tagField = new TextField();
		VBox tagBox = new VBox(5, tagLabel, tagField);
		tagBox.setAlignment(Pos.BOTTOM_LEFT);
		
		//buttons beside the author and tag
		Button appendButton = new Button("Append Entry");
		appendButton.getStyleClass().addAll("page-button-inactive", "page-button");
		Button clearButton = new Button("Clear");
		clearButton.getStyleClass().addAll("page-button-inactive", "page-button");
		HBox actionButtons = new HBox(10, appendButton, clearButton);	
		actionButtons.setAlignment(Pos.BOTTOM_LEFT);
		
		//compiling all the author tag and buttons
		HBox authorRow = new HBox(10);
		authorRow.getChildren().addAll(authorBox, tagBox, actionButtons);
		
		//message box
		Label messageLabel = new Label("Message:");
		TextArea messageArea = new TextArea();
		messageArea.setPromptText("Enter Log Message");
		messageArea.setPrefRowCount(4);
		messageArea.getStyleClass().add("message-textarea");
		VBox messageBox = new VBox(5, messageLabel, messageArea);
		
		//upper box containing the author row and the messagebox
		VBox logger = new VBox(20);
		logger.getStyleClass().addAll("logger", "containers-shadow");
		logger.setPadding(new Insets(30));
		logger.getChildren().addAll(authorRow, messageBox);
		
		//filter box
		Label filterLabel = new Label("Filter:");
		TextField filterField = new TextField();
		VBox filterBox = new VBox(3, filterLabel, filterField);
		filterBox.setAlignment(Pos.BOTTOM_LEFT);
		
		//author dropdown
		ComboBox<String> authorComboBox = new ComboBox<String>();
		authorComboBox.getItems().addAll("IDK1", "IDK2", "IDK3", "IDK4", "Others"); //will depend on the user input, ayusin sa backend later
		authorComboBox.setPromptText("Author");
		VBox authorFilterBox = new VBox(5, new Label(" "), authorComboBox); //for alignment
		authorFilterBox.setAlignment(Pos.BOTTOM_LEFT);
		
		//tag dropdown
		ComboBox<String> tagComboBox = new ComboBox<String>();
		tagComboBox.getItems().addAll("IDK1", "IDK2", "IDK3", "IDK4", "Others"); //will depend on the user input, ayusin sa backend later
		tagComboBox.setPromptText("Tag");
		VBox tagFilterBox = new VBox(5, new Label(" "), tagComboBox); //for alignment
		tagFilterBox.setAlignment(Pos.BOTTOM_LEFT);
		
		//hbox of combo boxes for alignment
		HBox comboBoxes = new HBox(5, authorFilterBox, tagFilterBox);
		
		//date picker
		DatePicker datePicker = new DatePicker();
		datePicker.setValue(LocalDate.now());
		datePicker.setPromptText("Select Date");
		datePicker.getStyleClass().add("styled-date-picker");
		VBox dateBox = new VBox(5, new Label(" "), datePicker); //for alignment
		dateBox.setAlignment(Pos.BOTTOM_LEFT);
		
		//match all keywords checkbox
		CheckBox matchAllCheckBox = new CheckBox();
		Label matchAllLabel = new Label("Match ALL keywords");
		HBox matchAllBox = new HBox(5, matchAllCheckBox, matchAllLabel);
		matchAllBox.setAlignment(Pos.BOTTOM_CENTER);
		
		//apply and reset buttons
		Button applyButton = new Button("   Apply   ");
        applyButton.getStyleClass().addAll("page-button-inactive", "page-button", "long-button");
		Button resetButton = new Button("   Reset   ");
		resetButton.getStyleClass().addAll("page-button-inactive", "page-button", "long-button");
		HBox filterButtonsBox = new HBox(5, applyButton, resetButton);
		filterButtonsBox.setAlignment(Pos.BOTTOM_LEFT);
		
		//hbox for alignment
		HBox boxes = new HBox(10, matchAllBox, filterButtonsBox);
		
        HBox filterRow = new HBox(8);
        filterRow.setAlignment(Pos.BOTTOM_LEFT);
        filterRow.setMaxHeight(70);
        filterRow.setPadding(new Insets(10));
        filterRow.getStyleClass().add("filter-container");
        
        filterRow.getChildren().addAll(filterBox, comboBoxes, dateBox, boxes);
		
        //list view
        ListView<String> listView = new ListView<>();
		listView.getItems().add("STF-0001 | fullName=Mylene | role=MedTe");
		listView.getStyleClass().add("list-view");
		listView.getStyleClass().add("containers-shadow");
		listView.setPrefHeight(400);
		
		//export buttons
		Button exportTXTButton = new Button("EXPORT TXT");
		exportTXTButton.getStyleClass().addAll("page-button-active", "page-button", "long-button");
		Button exportCSVButton = new Button("EXPORT CSV");
		exportCSVButton.getStyleClass().addAll("page-button-active", "page-button", "long-button");
		HBox exportBox = new HBox(5, exportTXTButton, exportCSVButton);
		exportBox.setAlignment(Pos.BOTTOM_LEFT);
        
		//main container
		VBox root = new VBox(10, pageButtons ,logger, filterRow, listView, exportBox);
		root.getStyleClass().add("default-bg");
		root.setPadding(new Insets(50));
		Scene scene = new Scene(root, 1200, 700);
		scene.getStylesheets().add(getClass().getResource("/application/styles/LogBookPage.css").toExternalForm());
		scene.getStylesheets().add(getClass().getResource("/application/styles/application.css").toExternalForm());
		
		stage.setScene(scene);
		stage.setTitle("Log Book System");
		stage.show();
	}
}
