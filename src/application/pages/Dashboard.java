package application.pages;

import javafx.collections.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import application.Main;
import application.models.*;

public class Dashboard {
	
	private Hospital hospital;
	
	public Dashboard(Hospital hospital) { this.hospital = hospital; }
	
	public void setStageComponents(Stage stage, Main main) {
		HBox pageButtons = new HeaderButtons(main, "DASHBOARD").get(); // page switching header
		
		/* ------------------------------------------- MAINPAGE CONTENTS-------------------------------------------*/
		VBox staffDash = buildStaff();
		VBox patientDash = buildPatient();
		VBox labRequestDash = buildLabRequest();
		VBox logBookDash = buildLogBook();
		VBox labExamDash = buildLabExam();
		
		HBox topRow = new HBox(30, staffDash, patientDash, labRequestDash);
		HBox.setHgrow(topRow, Priority.ALWAYS);
		staffDash.prefWidthProperty().bind(topRow.widthProperty().multiply(0.33));
		patientDash.prefWidthProperty().bind(topRow.widthProperty().multiply(0.33));
		labRequestDash.prefWidthProperty().bind(topRow.widthProperty().multiply(0.33));
		
		HBox bottomRow = new HBox(30, logBookDash, labExamDash);
		HBox.setHgrow(bottomRow, Priority.ALWAYS);
		logBookDash.prefWidthProperty().bind(topRow.widthProperty().multiply(0.7));
		labExamDash.prefWidthProperty().bind(topRow.widthProperty().multiply(0.3));
		
		VBox mainLedger = new VBox(30, topRow, bottomRow);
		VBox.setVgrow(mainLedger, Priority.ALWAYS);
		topRow.prefHeightProperty().bind(mainLedger.heightProperty().multiply(0.5));
		bottomRow.prefHeightProperty().bind(mainLedger.heightProperty().multiply(0.5));
		
		/* ------------------------------------------- ROOT & SCENE -------------------------------------------*/
		// Create root
		VBox root = new VBox(20, pageButtons, mainLedger);
		root.setPadding(new Insets(50));
		root.getStyleClass().add("default-bg");
		
		// Create scene and add styles
		Scene dashboardScene = new Scene(root, 1200, 720);
		dashboardScene.getStylesheets().add(getClass().getResource("/application/styles/application.css").toExternalForm());
		dashboardScene.getStylesheets().add(getClass().getResource("/application/styles/Dashboard.css").toExternalForm());
		stage.setScene(dashboardScene);
		stage.setTitle("Dashboard");
		stage.show();
	}
	
	/* ------------------------------------------- BUILDERS -------------------------------------------*/
	private VBox buildStaff() {
		int total = hospital.getStaffs().size(), activeCount = 0, inactiveCount;
		
		for (Staff staff : hospital.getStaffs()) {
			if(staff.getStatus().equals("active"))
				activeCount++;
		}
		inactiveCount = total - activeCount;
		double progress = total != 0 ? (double) activeCount / total : 0;
		
		Label staffLabel = new Label("Staff");
		
		Label totalLabel = new Label("Total");
		totalLabel.getStyleClass().addAll("black-label");
		Label totalCountLabel = new Label(String.valueOf(total));
		totalCountLabel.getStyleClass().addAll("black-label", "big-number");
		
		VBox totalBox = new VBox(10, totalLabel, totalCountLabel);
		totalBox.setAlignment(Pos.CENTER);
		
		ProgressBar activeBar = new ProgressBar(progress);
		activeBar.setMaxWidth(Double.MAX_VALUE);
		VBox.setVgrow(activeBar, Priority.ALWAYS);
		
		Text activeLabel = new Text("Active");
		Text activeNumber = new Text(String.valueOf(activeCount));
		activeNumber.getStyleClass().addAll("bold");
		VBox activeBox = new VBox(activeLabel, activeNumber);
		activeBox.setAlignment(Pos.CENTER);
		
		Text inactiveLabel = new Text("Inactive");
		Text inactiveNumber = new Text(String.valueOf(inactiveCount));
		inactiveNumber.getStyleClass().addAll("bold");
		VBox inactiveBox = new VBox(inactiveLabel, inactiveNumber);
		inactiveBox.setAlignment(Pos.CENTER);
		
		HBox legend = new HBox(50, activeBox, inactiveBox);
		legend.setMaxWidth(Double.MAX_VALUE);
		legend.setAlignment(Pos.CENTER);
		
		VBox staffDash = new VBox(10, staffLabel, totalBox, activeBar, legend);
		staffDash.getStyleClass().addAll("box", "containers-shadow");
		HBox.setHgrow(staffDash, Priority.ALWAYS);
		
		return staffDash;
	}
	
	private VBox buildPatient() {
		int total = hospital.getPatients().size();
		
		Label patientLabel = new Label("Patient");
		
		Label totalLabel = new Label("Total");
		totalLabel.getStyleClass().addAll("black-label");
		Label totalCountLabel = new Label(String.valueOf(total));
		totalCountLabel.getStyleClass().addAll("black-label", "big-number");
		
		VBox totalBox = new VBox(10, totalLabel, totalCountLabel);
		totalBox.setAlignment(Pos.CENTER);
		
		VBox patientDash = new VBox(30, patientLabel, totalBox);
		patientDash.getStyleClass().addAll("box", "containers-shadow");
		HBox.setHgrow(patientDash, Priority.ALWAYS);
		
		return patientDash;
	}
	
	private VBox buildLabRequest() {
		int newCount = 0, inProgCount = 0, doneCount = 0;
		
		for (LabRequest request : hospital.getLabRequests()) {
			if (request.getStatus().equals("new")) newCount++;
			else if (request.getStatus().equals("in progress")) inProgCount++;
			else if (request.getStatus().equals("done")) doneCount++;
		}
		
		Label labRequestLabel = new Label("Lab Request");
		
		 ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
				 new PieChart.Data("New", newCount),
				 new PieChart.Data("In progress", inProgCount),
		         new PieChart.Data("Done", doneCount));

        PieChart pieChart = new PieChart(pieChartData);
        pieChart.setTitle("Request Status");
        pieChart.setLegendVisible(true);
        pieChart.setLabelsVisible(true);
		
		VBox labRequestDash = new VBox(labRequestLabel, pieChart);
		labRequestDash.getStyleClass().addAll("box", "containers-shadow");
		HBox.setHgrow(labRequestDash, Priority.ALWAYS);
		
		return labRequestDash;
	}
	
	private VBox buildLogBook() {
		Label logBookLabel = new Label("Log Book History");
		
		ListView<LogBook> listView = new ListView<>();
		listView.getStyleClass().add("list-view");
		
		ObservableList<LogBook> items = FXCollections.observableArrayList();
		items.setAll(hospital.getLogBooks());
		listView.setItems(items);
		
		VBox logBookDash = new VBox(logBookLabel, listView);
		logBookDash.getStyleClass().addAll("box", "containers-shadow");
		
		return logBookDash;
	}
	
	private VBox buildLabExam() {
		int inProgCount = 0, finishedCount = 0, cancelledCount = 0;
		
		for (LabExam exam : hospital.getLabExams()) {
			if (exam.getStatus().equals("In-Progress")) inProgCount++;
			else if (exam.getStatus().equals("Finished")) finishedCount++;
			else if (exam.getStatus().equals("Cancelled")) cancelledCount++;
		}
		
		Label labExamLabel = new Label("Lab Exam");
		
		 ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
				 new PieChart.Data("In-Progress", inProgCount),
				 new PieChart.Data("Finished", finishedCount),
		         new PieChart.Data("Cancelled", cancelledCount));

        PieChart pieChart = new PieChart(pieChartData);
        pieChart.setTitle("Exam Status");
        pieChart.setLegendVisible(true);
        pieChart.setLabelsVisible(true);
        
		VBox labExamDash = new VBox(labExamLabel, pieChart);
		labExamDash.getStyleClass().addAll("box", "containers-shadow");
		HBox.setHgrow(labExamDash, Priority.ALWAYS);
		
		return labExamDash;
	}
}
