package application.pages;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import application.Main;
import application.models.*;

public class LogBookViewPage {
	//initialization so other fxns can access these
	private Hospital hospital;
    private TableView<LogBook> table;
    private ObservableList<LogBook> data;
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
		VBox authorFilterBox = new VBox(5, new Label(" "), authorComboBox); //for alignment
		authorFilterBox.setAlignment(Pos.BOTTOM_LEFT);
		
		//tag dropdown
		tagComboBox = new ComboBox<String>();
		tagComboBox.setPromptText("Tag");
		VBox tagFilterBox = new VBox(5, new Label(" "), tagComboBox); //for alignment
		tagFilterBox.setAlignment(Pos.BOTTOM_LEFT);
		
		//hbox of combo boxes for alignment
		HBox comboBoxes = new HBox(5, authorFilterBox, tagFilterBox);
		
		//date picker
		datePicker = new DatePicker();
		datePicker.setPromptText("Date");
		datePicker.getStyleClass().add("styled-date-picker");
		VBox dateBox = new VBox(5, new Label(" "), datePicker); //for alignment
		dateBox.setAlignment(Pos.BOTTOM_LEFT);
		
		//adding listeners so that even if one has input then the apply button is enabled
		datePicker.valueProperty().addListener((observable, oldValue, newValue) -> { 
		    validateFilterButtons();
		});
		authorComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
		    validateFilterButtons();
		});
		tagComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
		    validateFilterButtons();
		});
		
		//match all keywords checkbox
		Label matchAllLabel = new Label("Match ALL keywords");
		matchAllCheckBox = new CheckBox();
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
		
		updateAuthorComboBox(); //initialize with existing authors
		updateTagComboBox(); //initialize with existing tags
		
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
		
		table = new TableView<>();
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		
		/* TABLE COLUMNS */
		TableColumn<LogBook, LocalDateTime> colTimestamp = new TableColumn<>("Timestamp");
		TableColumn<LogBook, String> colAuthor = new TableColumn<>("Author");
		TableColumn<LogBook, String> colTag = new TableColumn<>("Tag");
		TableColumn<LogBook, String> colMessage = new TableColumn<>("Message");
		
		colTimestamp.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        colAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        colTag.setCellValueFactory(new PropertyValueFactory<>("tag"));
        colMessage.setCellValueFactory(new PropertyValueFactory<>("message"));
        
        colTimestamp.setPrefWidth(40);
        colAuthor.setPrefWidth(30);
        colTag.setPrefWidth(30);
        
        table.getColumns().addAll(colTimestamp, colAuthor, colTag, colMessage);
        
        // Add sample data
        data = FXCollections.observableArrayList(hospital.getLogBooks());
        table.setItems(data);
		
        updateLogBookTableView(); // Initialize table view
        
		//export buttons
		exportTXTButton = new Button("EXPORT TXT");
		exportTXTButton.getStyleClass().addAll("page-button-inactive", "page-button", "long-button");
		exportTXTButton.setDisable(true); //initialization
		exportCSVButton = new Button("EXPORT CSV");
		exportCSVButton.getStyleClass().addAll("page-button-inactive", "page-button", "long-button");
		exportCSVButton.setDisable(true); //initialization
		HBox exportBox = new HBox(5, exportTXTButton, exportCSVButton);
		exportBox.setAlignment(Pos.BOTTOM_LEFT);
		
		//export button listeners
		exportTXTButton.setOnAction(e -> exportToTXT());
		exportCSVButton.setOnAction(e -> exportToCSV());
		
		//initial check for the automatic logging
		if (data.isEmpty()) {
		    exportTXTButton.setDisable(true);
		    exportCSVButton.setDisable(true);
		    exportTXTButton.getStyleClass().setAll("page-button-inactive", "page-button", "long-button");
		    exportCSVButton.getStyleClass().setAll("page-button-inactive", "page-button", "long-button");
		} else {
		    exportTXTButton.setDisable(false);
		    exportCSVButton.setDisable(false);
		    exportTXTButton.getStyleClass().setAll("page-button-active", "page-button", "long-button");
		    exportCSVButton.getStyleClass().setAll("page-button-active", "page-button", "long-button");
		}
		
		//listener to enable or disable the export buttons
		data.addListener((javafx.collections.ListChangeListener.Change<? extends LogBook> change) -> {
		    if (data.isEmpty()) {
		        exportTXTButton.setDisable(true);
		        exportCSVButton.setDisable(true);
		        exportTXTButton.getStyleClass().setAll("page-button-inactive", "page-button", "long-button");
		        exportCSVButton.getStyleClass().setAll("page-button-inactive", "page-button", "long-button");
		    } else {
		        exportTXTButton.setDisable(false);
		        exportCSVButton.setDisable(false);
		        exportTXTButton.getStyleClass().setAll("page-button-active", "page-button", "long-button");
		        exportCSVButton.getStyleClass().setAll("page-button-active", "page-button", "long-button");
		    }
		});

		//main container
		VBox root = new VBox(10, pageButtons ,logger, filterRow, table, exportBox);
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
		data.clear(); //so no duplicate whenever this is called
        data.addAll(hospital.getLogBooks());
	}


	private void updateAuthorComboBox() {     
        //add authors from log books
		authorComboBox.getItems().clear(); //to prevent duplication
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
		tagComboBox.getItems().clear(); //to prevent duplication
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
        //buttons should always be enabled if there are filters applied
        boolean hasFilters = !filterField.getText().isEmpty() || authorComboBox.getValue() != null || tagComboBox.getValue() != null || datePicker.getValue() != null;
        if(hasFilters){
        	applyButton.getStyleClass().setAll("page-button-active", "page-button", "long-button");
            applyButton.setDisable(false);
            resetButton.getStyleClass().setAll("page-button-active", "page-button", "long-button");
            resetButton.setDisable(false);
        }else{
        	applyButton.getStyleClass().setAll("page-button-inactive", "page-button", "long-button");
            applyButton.setDisable(true);
            resetButton.getStyleClass().setAll("page-button-inactive", "page-button", "long-button");
            resetButton.setDisable(true);
        }
	}

	private void validateInputs() {
		boolean allFilled = !authorField.getText().isEmpty() && !tagField.getText().isEmpty() && !messageArea.getText().isEmpty();
		boolean hasInput = !authorField.getText().isEmpty() || !tagField.getText().isEmpty() || !messageArea.getText().isEmpty();
		if(hasInput){
			clearButton.getStyleClass().setAll("page-button-active", "page-button");
			clearButton.setDisable(false);
		}else{
			clearButton.getStyleClass().setAll("page-button-inactive", "page-button");
			clearButton.setDisable(true);
		}
		
		if(allFilled) {
			appendButton.getStyleClass().setAll("page-button-active", "page-button");
			appendButton.setDisable(false);
		}else {
			appendButton.getStyleClass().setAll("page-button-inactive", "page-button");
			appendButton.setDisable(true);
		}
	}

	private void resetFilters() {
		filterField.clear();
		
		//putting it to its initial state
		authorComboBox.getSelectionModel().clearSelection();
	    authorComboBox.setValue(null);
	    authorComboBox.setPromptText("Author");
	    
	    tagComboBox.getSelectionModel().clearSelection();
	    tagComboBox.setValue(null);
	    tagComboBox.setPromptText("Tag");
	    
	    datePicker.getEditor().clear();
	    datePicker.setValue(null);
	    datePicker.setPromptText("Date");
	    
        matchAllCheckBox.setSelected(false);
        updateLogBookTableView();
        validateFilterButtons();
	}


	private void applyFilters() {
		String filterText = filterField.getText().toLowerCase();
        String selectedAuthor = authorComboBox.getValue();
        String selectedTag = tagComboBox.getValue();
        LocalDate selectedDate = datePicker.getValue();
        boolean matchAllKeywords = matchAllCheckBox.isSelected();
        
        data.clear(); //so the only one appearing to the table view is the one that fits the filter
        ArrayList<LogBook> logBooks = hospital.getLogBooks();
        
        //check which filters has value
        boolean hasAuthorFilter = selectedAuthor != null && !selectedAuthor.isEmpty();
        boolean hasTagFilter = selectedTag != null && !selectedTag.isEmpty();
        boolean hasDateFilter = selectedDate != null;
        boolean hasTextFilter = !filterText.isEmpty();
        boolean hasAnyFilter = hasAuthorFilter || hasTagFilter || hasDateFilter || hasTextFilter;
        
        //if no filters, show everything
        if (!hasAnyFilter) {
            data.addAll(logBooks);
            return;
        }
        
        for(LogBook logBook : logBooks){
            boolean matchesAuthor = true;
            boolean matchesTag = true;
            boolean matchesDate = true;
            boolean matchesText = true;
            
            if(hasAuthorFilter){
                matchesAuthor = logBook.getAuthor().equals(selectedAuthor);
            }
            
            if(hasTagFilter){
                matchesTag = logBook.getTag().equals(selectedTag);
            }
            
            if(hasDateFilter){
                LocalDate logDate = logBook.getTimestamp().toLocalDate();
                matchesDate = logDate.equals(selectedDate);
            }
            
            if(hasTextFilter){ //checking the filter text field
                String logDateString = logBook.getTimestamp().toLocalDate().toString();
                String logText = (logBook.getAuthor() + " " + logBook.getTag() + " " + logBook.getMessage() + " " + logDateString).toLowerCase();
                String[] keywords = filterText.split(","); //input in textfield split at commas
                
                if(matchAllKeywords){ //if match all checkbox checked
                    //all key word must be in the log book
                    for (String keyword : keywords) {
                        if (!logText.contains(keyword)) {
                            matchesText = false;
                            break;
                        }
                    }
                }else{ //if match all is not selected
                    boolean foundKeyword = false;
                    for (String keyword : keywords) {
                        if (logText.contains(keyword)) {
                            foundKeyword = true;
                            break;
                        }
                    }
                    matchesText = foundKeyword;
                }
            }
            
            // Determine if log entry should be shown
            boolean shouldShow;
            
            if (matchAllKeywords) { 
                //when match all keywords is checked, all applied filters must match (author AND tag AND date AND text)
            	//either there is no input or it matches are the acceptable outcomes
            	shouldShow = (!hasAuthorFilter || matchesAuthor) && (!hasTagFilter || matchesTag) && (!hasDateFilter || matchesDate) && (!hasTextFilter || matchesText);
            } else {
            	 //when match all keywords is not checked, show if any applied filter matches
                shouldShow = (hasAuthorFilter && matchesAuthor) || (hasTagFilter && matchesTag) || (hasDateFilter && matchesDate) || (hasTextFilter && matchesText);
            }
            
            if(shouldShow){ //add or show the ones that fit the filter
                data.add(logBook);
            }
        }
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

	//persistence
	private void exportToCSV() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Export to CSV");
		fileChooser.setInitialFileName("logs.csv");
		
		// Limit to csv files
		fileChooser.getExtensionFilters().add(
		    new FileChooser.ExtensionFilter("CSV Files", "*.csv")
		);

		// File selection stage
		File file = fileChooser.showSaveDialog(new Stage());
		if (file == null) return;
		
		Path path = file.toPath();

	    try (BufferedWriter w = Files.newBufferedWriter(
	    		path, 
	    		StandardOpenOption.CREATE,
	    		StandardOpenOption.TRUNCATE_EXISTING
	    )) {
	    	
	    	w.write("Timestamp,Author,Tag,Message\n"); // Header
	    	for(LogBook log : data) {
	    		w.write(String.join(",", 
	    				log.getTimestamp().toString(),
	    				log.getAuthor(),
	    				log.getTag(),
	    				log.getMessage()));
	    		w.write("\n");
	    	}
	    } catch (IOException e) { e.printStackTrace(); }
	}


	private void exportToTXT() {
		//specify path
		Path path = Paths.get("src/storage/Logbooks.txt");
		
		try {
			Files.createFile(path); //create file first, if it exists then catch will be used
		}catch(IOException e) {}
		
		try{
			//build one string
			String content =  "Logbook Records\n";
			content += String.format("%-20s | %-20s | %-60s | %-25s\n", "Author", "Tag", "Message", "Timestamp");
			content += String.format("%-20s | %-20s | %-60s | %-25s\n", "--------------------", "--------------------", "--------------------", "-------------------------");
			ArrayList<LogBook> logbooks = this.hospital.getLogBooks();
			for(LogBook lb:logbooks) {
				content += String.format("%-20s | %-20s | %-60s | %-25s\n", lb.getAuthor(), lb.getTag(), lb.getMessage(), lb.getTimestamp().toString());
			}
			
			content += "\nTotal Entries:" + logbooks.size() + "\n";
			content += "Exported on: " + LocalDateTime.now().toString() + "\n";
			
			//write the whole string into the file
			Files.writeString(path, content); //overwrites the prev if there is one
			showAlert("TXT Export Successful", "Logbook exported successfully!");
		}catch(IOException e) {}

	}
}