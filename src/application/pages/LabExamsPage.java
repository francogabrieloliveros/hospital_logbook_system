package application.pages;

import javafx.collections.*;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import application.Main;
import application.models.*;

public class LabExamsPage {
	
	private Hospital hospital;
	
	//initialize the attributes here pare magamit sa methods
	ObservableList<LabRequest> labRequests;
	ObservableList<LabExam> items;
	ObservableList<Staff> staffs;
	private ListView<LabExam> listView;
	private ComboBox<LabRequest> labRequestCombo;
	private ComboBox<Staff> performingStaffCombo;
	private ComboBox<String> statusCombo;
	private DatePicker datePicker;
	private TextArea txtResultsRemarks;
	private Button btnAdd;
	private Button btnUpdate;
	private Button btnDelete;
	private VBox labRequestBox;
	
	public LabExamsPage(Hospital hospital) { this.hospital = hospital; }
	
	public void setStageComponents(Stage stage, Main main) {
		
		HBox pageButtons = new HeaderButtons(main, "LAB EXAMS").get(); // page switching header
		
		/* ------------------------------------------- MAINPAGE CONTENTS-------------------------------------------*/
		// Left panel
		buildListView();
		
		// Right panel
		labRequestBox = buildLabRequestBox();
		VBox performingStaffBox = buildPerformingStaffBox();
		HBox grpBox = buildDateStatus();
		VBox resultsRemarks = buildResultsRemarks();
		HBox buttonBox = buildButtonBox();
		
		//one final vbuck for the grouping of all elements inside the form
		VBox recordDetailsFull = new VBox(20, labRequestBox, performingStaffBox, grpBox, resultsRemarks, buttonBox);
		recordDetailsFull.getStyleClass().add("record-box");
		recordDetailsFull.setPrefWidth(500); 
		recordDetailsFull.setMaxWidth(400);
		
		//https://stackoverflow.com/questions/29489880/javafx-how-to-make-combobox-hgrow
		HBox mainPage = new HBox(20, listView, recordDetailsFull);
		labRequestCombo.setMaxWidth(Double.MAX_VALUE);
		performingStaffCombo.setMaxWidth(Double.MAX_VALUE);
		HBox.setHgrow(listView, Priority.ALWAYS);
		VBox.setVgrow(mainPage, Priority.ALWAYS);
		
		/* ------------------------------------------- FUNCTIONALITY -------------------------------------------*/
		// Set input field values to selected values
		listView.getSelectionModel().selectedItemProperty().addListener((a, b, selected) -> {
		    if (selected != null) {
	            labRequestCombo.setValue(selected.getLabRequest());
	    		performingStaffCombo.setValue(selected.getPerformingStaff());
	    		datePicker.setValue(selected.getDate());
	    		statusCombo.setValue(selected.getStatus());
	    		txtResultsRemarks.setText(selected.getResultsAndRemarks());
		    } else {
		    	resetInputFields();
		    }
		});
		
		labRequestCombo.valueProperty().addListener((a, b, selected) -> {
			if(selected != null) {
				fieldStatusUpdater();
				staffs.setAll(createStaffList(selected));
			} else {
				staffs.clear();
			}
		});
		performingStaffCombo.valueProperty().addListener((a, b, c) -> fieldStatusUpdater());
		statusCombo.valueProperty().addListener((a, b, c) -> fieldStatusUpdater());
		datePicker.valueProperty().addListener((a, b, c) -> fieldStatusUpdater());
		txtResultsRemarks.textProperty().addListener((a, b, c) -> fieldStatusUpdater());
		
		// onclick, creates a new LabExam, updates the items list and the listView, 
		// resets the input fields. If status is done, update staff and lab request aswell.
		btnAdd.setOnAction(e -> {
			String selectedStatus = statusCombo.getValue(); 
			LabRequest selectedLabRequest = labRequestCombo.getValue();
			
			hospital.addLabExam(new LabExam(hospital, 
					                        selectedLabRequest, 
					                        performingStaffCombo.getValue(), 
					                        datePicker.getValue(), 
					                        selectedStatus, 
					                        txtResultsRemarks.getText()));
			
			
			if(selectedStatus.equals("Finished") || selectedStatus.equals("Cancelled")) {
				selectedLabRequest.setStatus("done");
			} else if (selectedStatus.equals("In-Progress")) {
				selectedLabRequest.setStatus("in progress");
			}
			
			items.setAll(hospital.getLabExams());		    
		    resetInputFields();
		});
		
		//updates the value of the lab exam, updates the status of the staff and lab request if done or cancelled. 
		btnUpdate.setOnAction(e -> {
			LabExam selectedLabExam = listView.getSelectionModel().getSelectedItem();
			
			if(selectedLabExam != null) {
				String selectedStatus = statusCombo.getValue();
				LabRequest selectedLabRequest = labRequestCombo.getValue();
				
				selectedLabExam.update(selectedLabRequest, 
						               performingStaffCombo.getValue(), 
						               datePicker.getValue(), 
						               selectedStatus, 
						               txtResultsRemarks.getText());
				
				if(selectedStatus.equals("Finished") || selectedStatus.equals("Cancelled")) {
					selectedLabRequest.setStatus("done");
				} else if (selectedStatus.equals("In-Progress")) {
					selectedLabRequest.setStatus("in progress");
				}
				
				//updates the listView
			    items.setAll(hospital.getLabExams());
			    resetInputFields();
			}
		});
		
		btnDelete.setOnAction(e -> {
			LabExam selected = listView.getSelectionModel().getSelectedItem();
			
			if(selected != null) {
				selected.delete();
				items.setAll(hospital.getLabExams());
				resetInputFields();
			}
		});
		
		listView.setOnKeyPressed(e -> deselectOnEsc(e));
		
		/* ------------------------------------------- ROOT & SCENE -------------------------------------------*/
		//main container
		VBox root = new VBox(20, pageButtons, mainPage); // Add other elements here
		root.getStyleClass().add("default-bg");
		root.setOnKeyPressed(e -> deselectOnEsc(e));
		root.setPadding(new Insets(50));
		Scene scene = new Scene(root, 1200, 700);
		// Add LabExams css
		scene.getStylesheets().add(getClass().getResource("/application/styles/application.css").toExternalForm());
		scene.getStylesheets().add(getClass().getResource("/application/styles/LabExamsPage.css").toExternalForm());
		stage.setScene(scene);
		stage.setTitle("Lab Exams");
		stage.show();
	}
	
	// Verifies staff compatibility
	private boolean isCompatibleStaff(Staff staff, LabRequest labRequest){
		String role = staff.getRole();
		String testType = labRequest.getRequest();

		//cases for compatiblity
		switch(role) {
	    	case "MedTech" -> {
	        	if(testType.equals("CBC") || testType.equals("PCR"))
	        		return true;
	        	else
	        		return false;
	    	}
	    	case "Pathologist" -> {
	    		if(testType.equals("Urinalysis"))
	        		return true;
	        	else
	        		return false;
	    	}
	    	case "Phlebotomist" -> {
	    		if(testType.equals("FBS") || testType.equals("Lipid Profile"))
	        		return true;
	        	else
	        		return false;
	    	}
	    	case "RadTech" -> {
	    		if(testType.equals("X-Ray"))
	        		return true;
	        	else
	        		return false;
	    	}
		}
		return false;
	}
	
	private void deselectOnEsc(KeyEvent e) {
		if (e.getCode() == javafx.scene.input.KeyCode.ESCAPE) {
	        listView.getSelectionModel().clearSelection();
	    }
	}
	
	private void fieldStatusUpdater() {
	    boolean labRequestSelected = labRequestCombo.getValue() != null;
	    boolean performingStaffSelected = performingStaffCombo.getValue() != null;
	    boolean dateSelected = datePicker.getValue() != null;
	    boolean statusSelected = statusCombo.getValue() != "Select status";
	    boolean examSelected = listView.getSelectionModel().getSelectedItem() != null;
	    
	    if (labRequestSelected &&
	    	performingStaffSelected &&
	    	dateSelected &&
	    	statusSelected &&
	    	examSelected) {
	    	btnAdd.setDisable(true);
	    	btnUpdate.setDisable(false);
	    	btnDelete.setDisable(false);
	    }
	    else if(labRequestSelected &&
		    	performingStaffSelected &&
		    	dateSelected &&
		    	statusSelected &&
		    	!examSelected){
	    	btnAdd.setDisable(false);
	    	btnUpdate.setDisable(true);
	    	btnDelete.setDisable(true);
	    } else {
	    	btnAdd.setDisable(true);
	    	btnUpdate.setDisable(true);
	    	btnDelete.setDisable(true);
	    }
	}
	
	//resets inputFields for buttonActions
	private void resetInputFields() {
		labRequests.setAll(createLabRequestList());
		labRequestCombo.setValue(null);
		performingStaffCombo.setValue(null);
		datePicker.setValue(null);
		statusCombo.setValue("Select status");
		txtResultsRemarks.setText("");
		listView.getSelectionModel().clearSelection();
	}
	
	private ObservableList<Staff> createStaffList(LabRequest request){
		ObservableList<Staff> activeStaff = FXCollections.observableArrayList();
		
		if (request == null) return activeStaff;
		
		for(Staff staff : hospital.getStaffs()) {
			if(staff.getStatus().equals("active") && isCompatibleStaff(staff, request)) {
				activeStaff.add(staff);	
			}
		}
		
		return activeStaff;
	}
	
	private ObservableList<LabRequest> createLabRequestList(){
		ObservableList<LabRequest> activeLabRequests = FXCollections.observableArrayList();
		for(LabRequest labRequest : hospital.getLabRequests()) {
			if(!labRequest.getStatus().equals("done")) {
				activeLabRequests.add(labRequest);	
			}
		}
		
		return activeLabRequests;
	}
	
	/* ------------------------------------------- BUILDERS -------------------------------------------*/
	public void buildListView() {
		//listView
	    listView = new ListView<>();
		listView.getStyleClass().add("list-view");
		listView.getStyleClass().add("containers-shadow");
		
		items = FXCollections.observableArrayList();
		items.setAll(hospital.getLabExams());
		listView.setItems(items);	
	}
	
	public VBox buildLabRequestBox() {
		//Labels and TextFields and ComboBoxes and DatePicker and yes
		Label labelLabRequest = new Label("Lab Request");
		labRequestCombo = new ComboBox<LabRequest>();
		labRequestCombo.setPromptText("Select request");
		
		labRequests = createLabRequestList();
		labRequestCombo.setItems(labRequests);
		
		VBox labRequestBox = new VBox(5, labelLabRequest, labRequestCombo);
		labRequestCombo.setMaxWidth(Double.MAX_VALUE);
		
		labRequestCombo.setButtonCell(new ListCell<LabRequest>() {
		    @Override
		    protected void updateItem(LabRequest item, boolean empty) {
		        super.updateItem(item, empty);
		        setText(item == null ? "Select request" : String.format("%s request by %s for %s", 
		        		                                                 item.getRequest(), 
		        		                                                 item.getStaff().getName(), 
		        		                                                 item.getPatient().getName()));
		    }
		});
	    
	    labRequestCombo.setCellFactory(list -> new ListCell<LabRequest>() {
	        @Override
	        protected void updateItem(LabRequest item, boolean empty) {
	            super.updateItem(item, empty);
	            setText(item == null ? "Select request" : String.format("%s request by %s for %s", 
                        												 item.getRequest(), 
                        												 item.getStaff().getName(), 
                        												 item.getPatient().getName()));	
	        }
	    });
		
		return labRequestBox;
	}
	
	private VBox buildPerformingStaffBox() {
		Label labelPerformingStaff = new Label("Performing Staff");
		performingStaffCombo = new ComboBox<Staff>();
		performingStaffCombo.setPromptText("Select performing staff");
		
		staffs = FXCollections.observableArrayList();
		staffs.setAll(createStaffList(labRequestCombo.getValue()));
		performingStaffCombo.setItems(staffs);
		
		VBox performingStaffBox = new VBox(5, labelPerformingStaff, performingStaffCombo);
		
		performingStaffCombo.setButtonCell(new ListCell<Staff>() {
		    @Override
		    protected void updateItem(Staff item, boolean empty) {
		        super.updateItem(item, empty);
		        setText(item == null ? "Select staff" : item.getName());
		    }
		});
	    
	    performingStaffCombo.setCellFactory(list -> new ListCell<Staff>() {
	        @Override
	        protected void updateItem(Staff item, boolean empty) {
	            super.updateItem(item, empty);
	            setText(item == null ? "Select staff" : item.getName());	
	        }
	    });
		
		return performingStaffBox;
	}
	
	private HBox buildDateStatus() {
		Label labelDate = new Label("Date");
		datePicker = new DatePicker();
		datePicker.setValue(null);
		datePicker.setPromptText("Select Date");
		datePicker.getStyleClass().add("styled-date-picker");
		VBox dateBox= new VBox(5, labelDate, datePicker);
		
		Label labelStatus = new Label("Status");
		statusCombo = new ComboBox<String>();
		statusCombo.getItems().setAll("Select status", "Finished", "In-Progress", "Cancelled");
		statusCombo.setValue("Select status");
		VBox statusBox = new VBox(5, labelStatus, statusCombo);
		
		HBox grpBox = new HBox(10, dateBox, statusBox);
		return grpBox;
	}
	
	private VBox buildResultsRemarks() {
		Label labelResultsRemarks = new Label("Results and Remarks");
		txtResultsRemarks = new TextArea();
		VBox resultsRemarks = new VBox(5, labelResultsRemarks, txtResultsRemarks);
		
		return resultsRemarks;
	}

	private HBox buildButtonBox() {
		//Buttons
		btnAdd = new Button("Add");
		btnAdd.getStyleClass().addAll("page-button","page-button:pressed", "page-button-active", "page-button:hover");
		btnAdd.setDisable(true);
		
		btnUpdate = new Button("Update");
		btnUpdate.getStyleClass().addAll("page-button","page-button:pressed", "page-button-active", "page-button:hover");
		btnUpdate.setDisable(true);
		
		btnDelete = new Button("Delete");
		btnDelete.getStyleClass().addAll("page-button","page-button:pressed", "page-button-active", "page-button:hover");
		btnDelete.setDisable(true);
		
		//Grouping buttons
		HBox buttonBox = new HBox(10, btnAdd, btnUpdate, btnDelete);
		buttonBox.setAlignment(Pos.CENTER_RIGHT); // Align buttons to the right
		
		return buttonBox;
	}
}
