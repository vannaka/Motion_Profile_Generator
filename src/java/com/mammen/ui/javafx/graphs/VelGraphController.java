package com.mammen.ui.javafx.graphs;

import com.mammen.main.ProfileGenerator;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class VelGraphController
{
    @FXML
    private LineChart<Double, Double> velGraph;

    @FXML
    private NumberAxis axisTime, axisVel;

    private ProfileGenerator backend;
    private ObservableList<Waypoint> waypointsList;

    @FXML
    public void initialize()
    {
    }

    public void setup( ProfileGenerator backend, ObservableList<Waypoint> waypointsList )
    {
        this.backend = backend;
        this.waypointsList = waypointsList;
    } /* setup() */

    public void updateAxis( ProfileGenerator.Units units )
    {
        switch( units )
        {
            case FEET:
                axisVel.setLabel("Velocity (ft/s)");
                break;

            case METERS:
                axisVel.setLabel("Velocity (m/s)");
                break;

            case INCHES:
                axisVel.setLabel("Velocity (in/s)");
                break;
        }
    }

    /**
     * Populates the graph with the newest path data.
     */
    public void refresh()
    {
        XYChart.Series<Double, Double> flSeries, frSeries;
        XYChart.Series<Double, Double> blSeries, brSeries;

        // Clear data from velocity graph
        velGraph.getData().clear();

        if( waypointsList.size() > 1 )
        {
            flSeries = buildSeries( backend.getFrontLeftTrajectory() );
            frSeries = buildSeries( backend.getFrontRightTrajectory() );

            velGraph.getData().addAll( flSeries, frSeries );

            if( backend.getDriveBase() == ProfileGenerator.DriveBase.SWERVE )
            {
                blSeries = buildSeries( backend.getBackLeftTrajectory() );
                brSeries = buildSeries( backend.getBackRightTrajectory() );

                velGraph.getData().addAll( blSeries, brSeries );

                flSeries.setName("Front Left Trajectory");
                frSeries.setName("Front Right Trajectory");
                blSeries.setName("Back Left Trajectory");
                brSeries.setName("Back Right Trajectory");
            }
            else
            {
                flSeries.setName("Left Trajectory");
                frSeries.setName("Right Trajectory");
            }
        }
    }

    /**
     * Builds a series from the given trajectory that is ready to display on a LineChart.
     * @param traj Trajectory to build a series for.
     * @return The created series to display.
     */
    private static XYChart.Series<Double, Double> buildSeries( Trajectory traj )
    {
        XYChart.Series<Double, Double> series = new XYChart.Series<>();

        for( int i = 0; i < traj.segments.length; i++ )
        {
            // Holds x, y data for a single entry in the series.
            XYChart.Data<Double, Double> data = new XYChart.Data<>();

            // Set the x, y data.
            data.setXValue( traj.get(i).dt * i );
            data.setYValue( traj.get(i).velocity );

            // Add the data to the series.
            series.getData().add( data );
        }
        return series;
    }
}
