package application;

import java.util.List;
import java.util.stream.Collectors;

import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Pair;

public class SDChartAnalyse {
	
	public void displayStandardDeviationChart(DatabaseManager dbManager) {
        List<Transaction> bitcoinTransactions = dbManager.getCCYTransactions();

        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Price Standard Deviation");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Amount Standard Deviation");

        ScatterChart<Number, Number> scatterChart = new ScatterChart<>(xAxis, yAxis);
        scatterChart.setTitle("Bitcoin Standard Deviation Analysis");

        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Transsaction Standard Deviation");
        
        for (int i = 0; i < bitcoinTransactions.size(); i++) {
        	
            List<Double> pricesUpToCurrent = bitcoinTransactions.stream()
                .limit(i + 1)
                .map(Transaction::getPrice)
                .collect(Collectors.toList());

            List<Double> amountsUpToCurrent = bitcoinTransactions.stream()
                .limit(i + 1)
                .map(Transaction::getAmount)
                .collect(Collectors.toList());

            double priceStdDev = dbManager.calculateStandardDeviation(pricesUpToCurrent);
            double amountStdDev = dbManager.calculateStandardDeviation(amountsUpToCurrent);
            XYChart.Data<Number, Number> dataPoint = new XYChart.Data<>(priceStdDev, amountStdDev);
            series.getData().add(dataPoint);
            StackPane node = new StackPane();
            Label label = new Label(String.format("P: %.2f, A: %.2f", priceStdDev, amountStdDev));
            node.getChildren().add(label);
            dataPoint.setNode(node);
            series.getData().add(new XYChart.Data<>(priceStdDev, amountStdDev));
        }

        scatterChart.getData().add(series);

        Scene scene = new Scene(scatterChart, 800, 600);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }
}
