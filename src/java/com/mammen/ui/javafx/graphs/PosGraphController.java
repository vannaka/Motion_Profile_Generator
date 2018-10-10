package com.mammen.ui.javafx.graphs;

import com.mammen.generator.ProfileGenerator;
import com.mammen.generator.WaypointInternal;
import com.mammen.ui.javafx.dialog.factory.DialogFactory;
import com.mammen.util.Mathf;
import com.mammen.util.OSValidator;
import jaci.pathfinder.Trajectory;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.input.MouseEvent;

import java.io.File;
import java.util.Optional;

public class PosGraphController
{
    @FXML
    private NumberAxis
            axisPosX,
            axisPosY;

    @FXML
    private LineChart<Double, Double> posGraph;

    private ProfileGenerator backend;

    private boolean dsplyCenterPath;
    private boolean dsplyWaypoints;


    /**************************************************************************
     *  setup
     *      Setup backend linkages stuff here.
     *
     * @param backend Reference to the backend of the program.
     *************************************************************************/
    public void setup( ProfileGenerator backend )
    {
        this.backend = backend;

        // Watch this to know when a new path has been generated
        backend.numberOfGenerations().addListener( ( o, oldValue, newValue ) ->
        {
            // Update graph when the trajectory changes
            refresh();
            refreshPoints();
        });

        backend.unitsProperty().addListener( ( o, oldValue, newValue ) ->
        {
            // Updata axis to reflect the new unit
            updateAxis( newValue );
        });

        backend.waypointListProperty().addListener( ( o, oldValue, newValue ) ->
        {
            refresh();
            refreshPoints();
        });
    } /* setup() */


    /**************************************************************************
     *  initialize
     *      Setup gui stuff here.
     *************************************************************************/
    @FXML public void initialize()
    {
        dsplyCenterPath = false;
        dsplyWaypoints = true;
    }


    /**
     * Displays the given image behind the graph.
     * @param imgLocation Path to image; Absolute or relative.
     */
    public void setBGImg( String imgLocation )
    {
        if( imgLocation != null && !imgLocation.isEmpty() )
        {
            File img = new File( imgLocation );

            // Set background image via css styles
            posGraph.lookup(".chart-plot-background").setStyle( "-fx-background-image: url(" + img.toURI().toString() + ");" +
                                                                        "-fx-background-size: stretch;" +
                                                                        "-fx-background-position: top right;" +
                                                                        "-fx-background-repeat: no-repeat;" );
        }
    }

    public void updateAxis( ProfileGenerator.Units unit )
    {
        switch( unit )
        {
            case FEET:
                axisPosX.setUpperBound(32);
                axisPosX.setTickUnit(1);
                axisPosX.setLabel("X-Position (ft)");

                axisPosY.setUpperBound(27);
                axisPosY.setTickUnit(1);
                axisPosY.setLabel("Y-Position (ft)");
                break;

            case METERS:
                axisPosX.setUpperBound(10);
                axisPosX.setTickUnit(0.5);
                axisPosX.setLabel("X-Position (m)");

                axisPosY.setUpperBound(8.23);
                axisPosY.setTickUnit(0.5);
                axisPosY.setLabel("Y-Position (m)");
                break;

            case INCHES:
                axisPosX.setUpperBound(384);
                axisPosX.setTickUnit(12);
                axisPosX.setLabel("X-Position (in)");

                axisPosY.setUpperBound(324);
                axisPosY.setTickUnit(12);
                axisPosY.setLabel("Y-Position (in)");
                break;
        }
    }

    /**
     * Populates the graph with the newest path data.
     */
    public void refresh()
    {
        XYChart.Series<Double, Double> flSeries, frSeries, blSeries, brSeries;

        // Clear data from position graph
        posGraph.getData().clear();

        // Start by drawing drive train trajectories
        if( backend.getNumWaypoints() > 1 )
        {
            flSeries = buildSeries( backend.getFrontLeftTrajectory() );
            frSeries = buildSeries( backend.getFrontRightTrajectory() );

            if( backend.getDriveBase() == ProfileGenerator.DriveBase.SWERVE )
            {
                blSeries = buildSeries( backend.getBackLeftTrajectory() );
                brSeries = buildSeries( backend.getBackRightTrajectory() );

                posGraph.getData().addAll( blSeries, brSeries, flSeries, frSeries );
                flSeries.getNode().setStyle("-fx-stroke: red");
                frSeries.getNode().setStyle("-fx-stroke: red");
                blSeries.getNode().setStyle("-fx-stroke: blue");
                brSeries.getNode().setStyle("-fx-stroke: blue");

                for( XYChart.Data<Double, Double> data : blSeries.getData() )
                    data.getNode().setVisible(false);

                for( XYChart.Data<Double, Double> data : brSeries.getData() )
                    data.getNode().setVisible(false);
            }
            else
            {
                posGraph.getData().addAll( flSeries, frSeries );

                flSeries.getNode().setStyle("-fx-stroke: magenta");
//                flSeries.getNode().setStyle("-fx-stroke: linear-gradient(from 0% 0% to 100% 100%, red, green);" +
//                                            "-fx-stroke-width: 10px;" +
//                                            "-fx-stroke-line-cap: round");
                frSeries.getNode().setStyle("-fx-stroke: magenta");
//                frSeries.getNode().setStyle("-fx-stroke: linear-gradient(from 0% 0% to 100% 100%, red, green);" +
//                                            "-fx-stroke-width: 10px;" +
//                                            "-fx-stroke-line-cap: round");
            }

            for (XYChart.Data<Double, Double> data : flSeries.getData())
                data.getNode().setVisible(false);

            for (XYChart.Data<Double, Double> data : frSeries.getData())
                data.getNode().setVisible(false);
        }

        // Display center path
        if( dsplyCenterPath && backend.getNumWaypoints() > 1 )
        {
            XYChart.Series<Double, Double> sourceSeries = buildSeries( backend.getSourceTrajectory() );
            posGraph.getData().add( sourceSeries );
            sourceSeries.getNode().setStyle("-fx-stroke: orange");

            for( XYChart.Data<Double, Double> data : sourceSeries.getData() )
            {
                data.getNode().setVisible( false );
            }
        }
    }

    private void refreshPoints()
    {
        // TODO: This only adds new points to the graph; It will not remove points that do not exist anymore in the waypoints list.
        // Display waypoints
        if( dsplyWaypoints && !backend.isWaypointListEmpty() )
        {
            // Display waypoints
            XYChart.Series<Double, Double> waypointSeries = buildSeries( backend.getWaypointList().toArray( new WaypointInternal[1] ) );
            posGraph.getData().add( waypointSeries );
            waypointSeries.getNode().setStyle("-fx-stroke: transparent");

            for( XYChart.Data<Double, Double> data : waypointSeries.getData() )
            {
                data.getNode().setStyle( "-fx-background-color: orange, white" );
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
            data.setXValue( traj.get(i).x );
            data.setYValue( traj.get(i).y );

            // Add the data to the series.
            series.getData().add( data );
        }
        return series;
    }

    /**
     * Builds a series from the given Waypoints array that is ready to be displayed on a LineChart.
     * @param waypoints Array of waypoints to build a series for.
     * @return The created series to display.
     */
    private static XYChart.Series<Double, Double> buildSeries( WaypointInternal[] waypoints )
    {
        XYChart.Series<Double, Double> series = new XYChart.Series<>();

        for( WaypointInternal w : waypoints )
        {
            // Holds x, y data for a single entry in the series.
            XYChart.Data<Double, Double> data = new XYChart.Data<>();

            // Set the x, y data.
            data.setXValue( w.getX() );
            data.setYValue( w.getY() );

            // Add the data to the series.
            series.getData().add( data );
        }
        return series;
    }

    @FXML
    private void addPointOnClick( MouseEvent event )
    {
        boolean addWaypointOnClick = true;

        if( addWaypointOnClick )
        {
            // get pixel location
            Point2D mouseSceneCoords = new Point2D(event.getSceneX(), event.getSceneY());
            double xLocal = axisPosX.sceneToLocal(mouseSceneCoords).getX();
            double yLocal = axisPosY.sceneToLocal(mouseSceneCoords).getY();

            // get location in units (ft, m, in)
            double raw_x = axisPosX.getValueForDisplay( xLocal ).doubleValue();
            double raw_y = axisPosY.getValueForDisplay( yLocal ).doubleValue();

            // round location
            double rnd_x;
            double rnd_y;

            // Snap to grid
            if( backend.getUnits() == ProfileGenerator.Units.FEET )
            {
                rnd_x = Mathf.round( raw_x, 0.5 );
                rnd_y = Mathf.round( raw_y, 0.5 );
            }
            else if( backend.getUnits() == ProfileGenerator.Units.METERS )
            {
                rnd_x = Mathf.round( raw_x, 0.25 );
                rnd_y = Mathf.round( raw_y, 0.25 );
            }
            else // Inches
            {
                rnd_x = Mathf.round( raw_x, 6.0 );
                rnd_y = Mathf.round( raw_y, 6.0 );
            }

            // If rounded x,y coordinate is on the graph
            if( rnd_x >= axisPosX.getLowerBound() && rnd_x <= axisPosX.getUpperBound() &&
                rnd_y >= axisPosY.getLowerBound() && rnd_y <= axisPosY.getUpperBound() )
            {
                // TODO: Clicking to add point not working on Mac???
                if (OSValidator.isMac())
                {
                    Optional<WaypointInternal> result;
                    result = DialogFactory.createWaypointDialog( String.valueOf(rnd_x), String.valueOf(rnd_y) ).showAndWait();
                    result.ifPresent( (WaypointInternal w) -> backend.addPoint( w ) );
                }
                else
                {
                    backend.addPoint( rnd_x, rnd_y, 0.0 );
                }
            }
        }
        else
        {
            event.consume();
        }

    }

}
