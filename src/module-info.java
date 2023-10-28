module testing {
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.graphics;
	requires com.opencsv;
	requires transitive java.sql;
	requires javafx.base;
	
	
	opens application to javafx.graphics, javafx.fxml, java.base;
	
	exports application;
}
