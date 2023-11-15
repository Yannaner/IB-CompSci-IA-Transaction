package application;

import java.util.List;

import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

public class ScatterChartAnalyse {
	public void displayScatterChart(List<Transaction> dataList) {
	       
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Price");

        
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Amount");

        ScatterChart<Number, Number> scatterChart = new ScatterChart<>(xAxis, yAxis);
        scatterChart.setTitle("Price vs Amount Analysis");
      

        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Data Series");

        for (Transaction data : dataList) {
            series.getData().add(new XYChart.Data<>(data.getPrice(), data.getMarketWorth()));
        }
        
       
        scatterChart.getData().add(series);
        Scene scene = new Scene(scatterChart, 800, 600);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }
}
