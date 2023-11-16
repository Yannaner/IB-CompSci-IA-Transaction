package application;

import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.List;


public class LinearRegressionAnalyse {

    public void displayLinearRegressionChart(DatabaseManager dbManager) {
        List<Pair<Double, Double>> data = dbManager.getPriceAndAmountData();

        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Amount");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Price");

        ScatterChart<Number, Number> scatterChart = new ScatterChart<>(xAxis, yAxis);
        scatterChart.setTitle("Linear Regression Analysis");

        XYChart.Series<Number, Number> regressionLineSeries = new XYChart.Series<>();
        regressionLineSeries.setName("Regression Line");
        
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Transaction Data");

        LinearRegression lr = new LinearRegression(data);
        lr.fit();

        for (Pair<Double, Double> point : data) {
            series.getData().add(new XYChart.Data<>(point.getKey(), point.getValue()));
        }

        double minX = xAxis.getLowerBound();
        double maxX = xAxis.getUpperBound();
        for (double x = minX; x <= maxX; x += (maxX - minX) / 100) {
            double y = lr.getSlope() * x + lr.getIntercept();
            regressionLineSeries.getData().add(new XYChart.Data<>(x, y));
        }
        
        scatterChart.getData().add(series);
        scatterChart.getData().add(regressionLineSeries);
        
        //linear equation
        String equation = String.format("y = %.2fx + %.2f", lr.getSlope(), lr.getIntercept());
        scatterChart.setTitle("Linear Regression: " + equation);

        Scene scene = new Scene(scatterChart, 800, 600);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }

}
