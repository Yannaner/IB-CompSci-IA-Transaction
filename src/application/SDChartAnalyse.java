package application;

import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import javafx.util.Pair;

public class SDChartAnalyse {
	public void displayStandardDeviationChart(DatabaseManager dbManager) {
	    Pair<Double, Double> stdDeviations = dbManager.getStandardDeviations();
	    double priceStdDev = stdDeviations.getKey();
	    double amountStdDev = stdDeviations.getValue();

	    NumberAxis xAxis = new NumberAxis();
	    xAxis.setLabel("Price Standard Deviation");

	    NumberAxis yAxis = new NumberAxis();
	    yAxis.setLabel("Amount Standard Deviation");

	    ScatterChart<Number, Number> scatterChart = new ScatterChart<>(xAxis, yAxis);
	    scatterChart.setTitle("Standard Deviation Analysis");

	    XYChart.Series<Number, Number> series = new XYChart.Series<>();
	    series.setName("Data Series");
	    series.getData().add(new XYChart.Data<>(priceStdDev, amountStdDev));

	    scatterChart.getData().add(series);

	    Scene scene = new Scene(scatterChart, 800, 600);
	    Stage stage = new Stage();
	    stage.setScene(scene);
	    stage.show();
	}

}
