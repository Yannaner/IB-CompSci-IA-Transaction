package application;


import java.util.List;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
public class LineChartAnalyse {
	
	public void displayLineChart(List<Transaction> dataList) {
       
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Price");

        
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Amount");

        
        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        
        lineChart.setTitle("Price vs Amount Analysis");

        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Data Series");

        for (Transaction data : dataList) {
            series.getData().add(new XYChart.Data<>(data.getPrice(), data.getMarketWorth()));
        }

        lineChart.getData().add(series);
        Scene scene = new Scene(lineChart, 800, 600);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }
}