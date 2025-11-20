module CMSC22_FINAL_PROJECT {
	requires javafx.controls;
	requires javafx.graphics;
	
	opens application to javafx.base, javafx.graphics, javafx.fxml;
}
