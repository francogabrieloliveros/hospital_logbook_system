package application.pages;

import java.time.LocalDate;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import application.Main;
import application.models.*;

public class LogBookViewPage {
	//initialization so other fxns can access these
	private Hospital hospital;
    private TableView<LogBook> tableView;
    private ObservableList<LogBook> logBookData = FXCollections.observableArrayList();
    private TextField authorField;
    private TextField tagField;
    private TextArea messageArea;
    private TextField filterField;
    private ComboBox<String> authorComboBox;
    private ComboBox<String> tagComboBox;
    private DatePicker datePicker;
    private CheckBox matchAllCheckBox;
    private Button appendButton;
    private Button clearButton;
    private Button applyButton;
    private Button resetButton;
    private Button exportTXTButton;
    private Button exportCSVButton;
    
	public LogBookViewPage(Hospital hospital) { this.hospital = hospital; }
	
	
	// 	NOTE:	Main is added as a parameter in setStageComponents so we 
	//			have a reference when main is called (for the buttons to work)
	public void setStageComponents(Stage stage, Main main) {
		String[] labels = {"STAFF", "PATIENTS", "LAB EXAMS", "LAB REQUESTS", "LOGBOOK"};
		ArrayList<Button> labelButtons = new ArrayList<>();
		for(String label : labels) {
			Button newButton = new Button(label);
			
			if(label.equals("LOGBOOK")) {
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
		
		//author label
		Label authorLabel = new Label("Author:");
		authorField = new TextField();
		authorField.setPromptText("Author");
		VBox authorBox = new VBox(5, authorLabel, authorField);
		authorBox.setAlignment(Pos.BOTTOM_LEFT);
		
		//tag label
		Label tagLabel = new Label("Tag:");
		tagField = new TextField();
		tagField.setPromptText("Tag (e.g., incident, note)");
		VBox tagBox = new VBox(5, tagLabel, tagField);
		tagBox.setAlignment(Pos.BOTTOM_LEFT);
		
		//buttons beside the author and tag
		appendButton = new Button("Append Entry");
		appendButton.getStyleClass().addAll("page-button-inactive", "page-button");
		appendButton.setDisable(true); //initialization
		clearButton = new Button("Clear");
		clearButton.getStyleClass().addAll("page-button-inactive", "page-button");
		clearButton.setDisable(true);//initialization
		HBox actionButtons = new HBox(10, appendButton, clearButton);	
		actionButtons.setAlignment(Pos.BOTTOM_LEFT);
		
		//compiling all the author tag and buttons
		HBox authorRow = new HBox(10);
		authorRow.getChildren().addAll(authorBox, tagBox, actionButtons);
		
		//message box
		Label messageLabel = new Label("Message:");
		messageArea = new TextArea();
		messageArea.setPromptText("Message (single paragraph; newlines flattened)");
		messageArea.setPrefRowCount(4);
		messageArea.getStyleClass().add("message-textarea");
		VBox messageBox = new VBox(5, messageLabel, messageArea);
		
		//listeners to know if user has input
        authorField.textProperty().addListener((observable, oldValue, newValue) -> validateInputs());
        tagField.textProperty().addListener((observable, oldValue, newValue) -> validateInputs());
        messageArea.textProperty().addListener((observable, oldValue, newValue) -> validateInputs());
        
        //append and clear buttons functionality
        appendButton.setOnAction(e -> appendLogEntry());
        clearButton.setOnAction(e -> clearInputs());
		
		//upper box containing the author row and the messagebox
		VBox logger = new VBox(20);
		logger.getStyleClass().addAll("logger", "containers-shadow");
		logger.setPadding(new Insets(30));
		logger.getChildren().addAll(authorRow, messageBox);
		
		//filter box
		Label filterLabel = new Label("Filter:");
		filterField = new TextField();
		filterField.setPromptText("keyword(s)");
		VBox filterBox = new VBox(3, filterLabel, filterField);
		filterBox.setAlignment(Pos.BOTTOM_LEFT);
		
		//author dropdown
		authorComboBox = new ComboBox<String>();
		authorComboBox.setPromptText("Author");
		updateAuthorComboBox(); //initialize with existing authors
		VBox authorFilterBox = new VBox(5, new Label(" "), authorComboBox); //for alignment
		authorFilterBox.setAlignment(Pos.BOTTOM_LEFT);
		
		//tag dropdown
		tagComboBox = new ComboBox<String>();
		tagComboBox.setPromptText("Tag");
		updateTagComboBox(); //initialize with existing tags
		VBox tagFilterBox = new VBox(5, new Label(" "), tagComboBox); //for alignment
		tagFilterBox.setAlignment(Pos.BOTTOM_LEFT);
		
		//hbox of combo boxes for alignment
		HBox comboBoxes = new HBox(5, authorFilterBox, tagFilterBox);
		
		//date picker
		datePicker.setValue(LocalDate.now());
		datePicker.setPromptText("Date");
		datePicker.getStyleClass().add("styled-date-picker");
		VBox dateBox = new VBox(5, new Label(" "), datePicker); //for alignment
		dateBox.setAlignment(Pos.BOTTOM_LEFT);
		
		//match all keywords checkbox
		Label matchAllLabel = new Label("Match ALL keywords");
		HBox matchAllBox = new HBox(5, matchAllCheckBox, matchAllLabel);
		matchAllBox.setAlignment(Pos.BOTTOM_CENTER);
		
		//apply and reset buttons
		applyButton = new Button("   Apply   ");
        applyButton.getStyleClass().addAll("page-button-inactive", "page-button", "long-button");
        applyButton.setDisable(true); //initialization
		resetButton = new Button("   Reset   ");
		resetButton.getStyleClass().addAll("page-button-inactive", "page-button", "long-button");
		resetButton.setDisable(true); //initialization
		HBox filterButtonsBox = new HBox(5, applyButton, resetButton);
		filterButtonsBox.setAlignment(Pos.BOTTOM_LEFT);
		
		//apply and reset buttons functionality
		applyButton.setOnAction(e -> applyFilters());
		resetButton.setOnAction(e -> resetFilters());
		
		//filter field listener
		filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            validateFilterButtons();
        });
		
		//hbox for alignment
		HBox boxes = new HBox(10, matchAllBox, filterButtonsBox);
		
        HBox filterRow = new HBox(8);
        filterRow.setAlignment(Pos.BOTTOM_LEFT);
        filterRow.setMaxHeight(70);
        filterRow.setPadding(new Insets(10));
        filterRow.getStyleClass().add("filter-container");
        
        filterRow.getChildren().addAll(filterBox, comboBoxes, dateBox, boxes);
		
        //table view
        tableView = new TableView<>();
        tableView.getStyleClass().addAll("list-view", "containers-shadow");
        tableView.setPrefHeight(400);
		
        //cols of the table view
        TableColumn<LogBook, String> timestampColumn = new TableColumn<>("Timestamp");
        timestampColumn.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        timestampColumn.setPrefWidth(250);
        
        TableColumn<LogBook, String> authorColumn = new TableColumn<>("Author");
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        authorColumn.setPrefWidth(150);
        
        TableColumn<LogBook, String> tagColumn = new TableColumn<>("Tag");
        tagColumn.setCellValueFactory(new PropertyValueFactory<>("tag"));
        tagColumn.setPrefWidth(150);
        
        TableColumn<LogBook, String> messageColumn = new TableColumn<>("Message");
        messageColumn.setCellValueFactory(new PropertyValueFactory<>("message"));
        messageColumn.setPrefWidth(500);
        
        tableView.getColumns().addAll(timestampColumn, authorColumn, tagColumn, messageColumn);
        
        //putting the observable list in the table view
        tableView.setItems(logBookData);
        
        updateLogBookTableView(); // Initialize table view
        
		//export buttons
		exportTXTButton = new Button("EXPORT TXT");
		exportTXTButton.getStyleClass().addAll("page-button-active", "page-button", "long-button");
		exportTXTButton.setDisable(true); //initialization
		exportCSVButton = new Button("EXPORT CSV");
		exportCSVButton.getStyleClass().addAll("page-button-active", "page-button", "long-button");
		exportCSVButton.setDisable(true); //initialization
		HBox exportBox = new HBox(5, exportTXTButton, exportCSVButton);
		exportBox.setAlignment(Pos.BOTTOM_LEFT);
		
		//enabling export when there are log entries
		if (hospital.getLogBooks().size() > 0) {
            exportTXTButton.setDisable(false);
            exportCSVButton.setDisable(false);
        }
        
		//main container
		VBox root = new VBox(10, pageButtons ,logger, filterRow, tableView, exportBox);
		root.getStyleClass().add("default-bg");
		root.setPadding(new Insets(50));
		Scene scene = new Scene(root, 1200, 700);
		scene.getStylesheets().add(getClass().getResource("/application/styles/LogBookPage.css").toExternalForm());
		scene.getStylesheets().add(getClass().getResource("/application/styles/application.css").toExternalForm());
		
		stage.setScene(scene);
		stage.setTitle("Log Book System");
		stage.show();
	}

	private void updateLogBookTableView() { //used in different fxns
		logBookData.clear(); //so no duplicate whenever this is called
        logBookData.addAll(hospital.getLogBooks());
	}


	private void updateAuthorComboBox() {     
        //add authors from log books
        ArrayList<LogBook> logBooks = hospital.getLogBooks();
        ArrayList<String> authors = new ArrayList<>();
        for(LogBook logBook:logBooks){
            String author = logBook.getAuthor();
            if(!authors.contains(author)){
                authors.add(author);
                authorComboBox.getItems().add(author);
            }
        }
        validateFilterButtons(); 
	}


	private void updateTagComboBox() {
		ArrayList<LogBook> logBooks = hospital.getLogBooks();
        ArrayList<String> tags = new ArrayList<>();
        for(LogBook logBook:logBooks){
            String tag = logBook.getTag();
            if(!tags.contains(tag)){
                tags.add(tag);
                tagComboBox.getItems().add(tag);
            }
        }
        validateFilterButtons();
	}

	private void validateFilterButtons() {
		boolean hasFilterText = !filterField.getText().isEmpty();
        if(hasFilterText){
            applyButton.getStyleClass().setAll("page-button-active", "page-button", "long-button");
            applyButton.setDisable(false);
        } else {
            applyButton.getStyleClass().setAll("page-button-inactive", "page-button", "long-button");
            applyButton.setDisable(true);
        }
        
        //reset button should always be enabled if there are filters applied
        boolean hasFilters = hasFilterText || authorComboBox.getValue() != null || tagComboBox.getValue() != null || (datePicker.getValue() != null && !datePicker.getValue().equals(LocalDate.now()));
        if(hasFilters){
            resetButton.getStyleClass().setAll("page-button-active", "page-button", "long-button");
            resetButton.setDisable(false);
        }else{
            resetButton.getStyleClass().setAll("page-button-inactive", "page-button", "long-button");
            resetButton.setDisable(true);
        }
	}


	private Object resetFilters() {
		// TODO Auto-generated method stub
		return null;
	}


	private Object applyFilters() {
		// TODO Auto-generated method stub
		return null;
	}


	private void clearInputs() {
		authorField.clear();
        tagField.clear();
        messageArea.clear();
        validateInputs(); //calling again to disable the buttons again after clearing
	}


	private void appendLogEntry() {
		String author = authorField.getText().trim();
		String tag = tagField.getText().trim();
		String message = messageArea.getText().trim().replaceAll("\n", " "); // Flatten newlines
		
		LogBook logBook = new LogBook(author, tag, message);
		hospital.addLogBook(logBook);
		
		//update the ui elements
		updateLogBookTableView();
		updateAuthorComboBox();
		updateTagComboBox();
		
		clearInputs();
		showAlert("Success", "Log entry added successfully!");
	}


	private void showAlert(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
	}


	private void validateInputs() {
		boolean allFilled = !authorField.getText().isEmpty() && !tagField.getText().isEmpty() && !messageArea.getText().isEmpty();
		if(allFilled){
		 appendButton.getStyleClass().setAll("page-button-active", "page-button");
		 clearButton.getStyleClass().setAll("page-button-active", "page-button");
		 appendButton.setDisable(false);
		 clearButton.setDisable(false);
		}else{
		 appendButton.getStyleClass().setAll("page-button-inactive", "page-button");
		 clearButton.getStyleClass().setAll("page-button-inactive", "page-button");
		 appendButton.setDisable(true);
		 clearButton.setDisable(true);
		}
	}
}
