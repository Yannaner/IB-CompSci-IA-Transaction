package application;

import java.util.List;
import javafx.util.Pair;

public class LinearRegression {
    private double slope;
    private double intercept;

    private List<Pair<Double, Double>> data;

    public LinearRegression(List<Pair<Double, Double>> data) {
        this.data = data;
    }
    
    //bascially calculating the slope
    public void fit() {
        double sumX = 0, sumY = 0, sumXY = 0, sumXX = 0;
        int n = data.size();

        for (Pair<Double, Double> point : data) {
            sumX += point.getKey();
            sumY += point.getValue();
            sumXY += point.getKey() * point.getValue();
            sumXX += point.getKey() * point.getKey();
        }
        //Math
        slope = (n * sumXY - sumX * sumY) / (n * sumXX - sumX * sumX);
        intercept = (sumY - slope * sumX) / n;
    }

    public double getSlope() {
        return slope;
    }

    public double getIntercept() {
        return intercept;
    }

    public double predict(double x) {
        return slope * x + intercept;
    }
}

