package com.mammen.ui.javafx.factory;

import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import javafx.scene.chart.XYChart;

public class SeriesFactory
{
	private SeriesFactory() { }

    public static XYChart.Series<Double, Double> buildPositionSeries(Trajectory t) {
        XYChart.Series<Double, Double> series = new XYChart.Series<>();

        for (int i = 0; i < t.segments.length; i++) {
            XYChart.Data<Double, Double> data = new XYChart.Data<>();

            data.setXValue(t.get(i).x);
            data.setYValue(t.get(i).y);

            series.getData().add(data);
        }
        return series;
    }

    public static XYChart.Series<Double, Double> buildVelocitySeries(Trajectory t) {
        XYChart.Series<Double, Double> series = new XYChart.Series<>();

        for (int i = 0; i < t.segments.length; i++) {
            XYChart.Data<Double, Double> data = new XYChart.Data<>();

            data.setXValue(t.get(i).dt * i);
            data.setYValue(t.get(i).velocity);

            series.getData().add(data);
        }
        return series;
    }

    public static XYChart.Series<Double, Double> buildWaypointsSeries(Waypoint[] waypoints) {
        XYChart.Series<Double, Double> series = new XYChart.Series<>();

        for (Waypoint w : waypoints) {
            XYChart.Data<Double, Double> data = new XYChart.Data<>();

            data.setXValue(w.x);
            data.setYValue(w.y);

            series.getData().add(data);
        }
        return series;
    }
}
