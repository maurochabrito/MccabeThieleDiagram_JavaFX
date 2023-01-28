module JavaFX1 {
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.graphics;
	
	exports gui;
	opens gui to javafx.fxml;
	
	opens application to javafx.graphics, javafx.fxml;
}
