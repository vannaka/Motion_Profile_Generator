package com.mammen.ui.javafx.main.graphs;

import com.mammen.generator.generator_vars.SharedGeneratorVars;
import com.mammen.generator.generator_vars.Units;
import com.mammen.path.Path;
import com.mammen.path.Waypoint;
import com.mammen.main.MainUIModel;
import com.mammen.settings.SettingsModel;
import com.mammen.settings.SourcePathDisplayType;
import com.mammen.ui.javafx.dialog.factory.DialogFactory;
import com.mammen.util.Mathf;
import com.mammen.util.OSValidator;
import com.mammen.util.ResourceLoader;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.input.MouseEvent;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class PosGraphController
{
    @FXML
    private NumberAxis
            axisPosX,
            axisPosY;

    @FXML
    private LineChart<Double, Double> posGraph;

    private MainUIModel backend;
    private SharedGeneratorVars vars;

    // Graph display data
    private XYChart.Series<Double, Double> waypointSeries;
    private XYChart.Series<Double, Double> sourceSeries;
    private XYChart.Series<Double, Double> flSeries, frSeries, blSeries, brSeries;

    private SettingsModel settings;


    /**************************************************************************
     *  initialize
     *      Setup gui stuff here.
     *************************************************************************/
    @FXML public void initialize()
    {
        backend = MainUIModel.getInstance();
        settings = SettingsModel.getInstance();
        vars = settings.getSharedGeneratorVars();

        setBGImg();

        settings.graphBGImagePathProperty().addListener( (o, oldValue, newValue) ->
        {
            setBGImg();
        });

        // Watch this to know when a new path has been generated
        backend.pathProperty().addListener( ( o, oldValue, newValue ) ->
        {
            // Build path series'
            buildPathSeries( newValue );

            // Display series'.
            refresh();
        });

        vars.unitProperty().addListener( ( o, oldValue, newValue ) ->
        {
            // Update axis to reflect the new unit
            updateAxis( newValue );
        });

        backend.waypointListProperty().addListener( ( o, oldValue, newValue ) ->
        {
            buildWaypointSeries( newValue );

            // Display series.
            refresh();
        });

        settings.sourcePathDisplayTypeProperty().addListener( (o, oldValue, newValue) ->
        {
            buildWaypointSeries( backend.getWaypointList() );
            buildPathSeries( backend.getPath() );

            // Display series.
            refresh();
        });

        posGraph.lookup(".chart-vertical-grid-lines").setStyle( "-fx-stroke: rgba( 150, 153, 158, 0.5 );" );
        posGraph.lookup(".chart-horizontal-grid-lines").setStyle( "-fx-stroke: rgba( 150, 153, 158, 0.5 );" );

    }

    /**
     * Displays the given image behind the graph.
     */
    private void setBGImg()
    {
        String path;

        if( settings.getGraphBGImagePath() == null )
        {
            File imgFile = new File( SettingsModel.getSettingsDir() + "/FRC2018.jpg" );

            if( !imgFile.exists() )
            {
                try
                {
                    ResourceLoader.resourceToFile("/images/FRC2018.jpg", imgFile );
                }
                catch( IOException e )
                {
                    return;
                }
            }

            // Get path to image
            path = imgFile.toURI().toString();

            // Update settings with new path
            settings.setGraphBGImagePath( SettingsModel.getSettingsDir() + "/FRC2018.jpg" );
        }
        else
        {
            File imgFile = new File( settings.getGraphBGImagePath() );
            path = imgFile.toURI().toString();
        }

        // Set background image via css styles
        posGraph.lookup(".chart-plot-background").setStyle( "-fx-background-image: url(" + path + ");" +
                                                                    "-fx-background-size: stretch;" +
                                                                    "-fx-background-position: top right;" +
                                                                    "-fx-background-repeat: no-repeat;" );
    }

    private void updateAxis( Units unit )
    {
        switch( unit )
        {
            case FEET:
                axisPosX.setUpperBound(54);
                axisPosX.setTickUnit(1);
                axisPosX.setLabel("X-Position (ft)");

                axisPosY.setUpperBound(27);
                axisPosY.setTickUnit(1);
                axisPosY.setLabel("Y-Position (ft)");
                break;

            case METERS:
                axisPosX.setUpperBound(16.5);
                axisPosX.setTickUnit(0.5);
                axisPosX.setLabel("X-Position (m)");

                axisPosY.setUpperBound(8.23);
                axisPosY.setTickUnit(0.5);
                axisPosY.setLabel("Y-Position (m)");
                break;

            case INCHES:
                axisPosX.setUpperBound(648);
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
    private void refresh()
    {
        // Clear data from position graph
        posGraph.getData().clear();

        if( null != sourceSeries )
        {
            posGraph.getData().add( sourceSeries );
            setSeriesVisuals( sourceSeries, "orange" );
        }

        if( null != flSeries )
        {
            posGraph.getData().add( flSeries );
            setSeriesVisuals( flSeries, "red" );
        }

        if( null != frSeries )
        {
            posGraph.getData().add( frSeries );
            setSeriesVisuals( frSeries, "red" );
        }

        if( null != blSeries )
        {
            posGraph.getData().add( blSeries );
            setSeriesVisuals( blSeries, "blue" );
        }

        if( null != brSeries )
        {
            posGraph.getData().add( brSeries );
            setSeriesVisuals( brSeries, "blue" );
        }

        if( null != waypointSeries )
        {
            posGraph.getData().add( waypointSeries );

            int counter = 0;
            for( XYChart.Data<Double, Double> data : waypointSeries.getData() )
            {
                // Add event handlers to each Point
                data.getNode().setId( String.valueOf( counter ) );
                setOnPointEvent( data );

                // Set Point color
                data.getNode().setStyle( "-fx-background-color: orange, white" );

                // Increment counter
                counter += 1;
            }

            waypointSeries.getNode().setStyle("-fx-stroke: transparent");
        }
    }

    private void setSeriesVisuals( XYChart.Series<Double, Double> series, String color )
    {
        // Set line color
        series.getNode().setStyle("-fx-stroke: " + color );

        // We only want to display the line connecting the points
        //  not the points themselves.
        for(  XYChart.Data<Double, Double> data : series.getData() )
        {
            data.getNode().setVisible(false);
        }
    }

    private void buildPathSeries( Path path )
    {
        sourceSeries = null;
        flSeries = null;
        frSeries = null;
        blSeries = null;
        brSeries = null;

        if( null == path )
            return;

        flSeries = buildSegmentsSeries( path.getFrontLeft() );
        frSeries = buildSegmentsSeries( path.getFrontRight() );
        blSeries = buildSegmentsSeries( path.getBackLeft() );
        brSeries = buildSegmentsSeries( path.getBackRight() );

        if( SourcePathDisplayType.WP_PLUS_PATH == settings.getSourcePathDisplayType() )
            sourceSeries = buildSegmentsSeries( path.getCenter() );

    }

    /**
     * Builds a series from the given trajectory that is ready to display on a LineChart.
     * @param segments Collection of Segments to build a series for.
     * @return The created series to display.
     */
    private static XYChart.Series<Double, Double> buildSegmentsSeries( Path.Segment[] segments )
    {
        if( ( null == segments     )
         || ( 0 == segments.length ) )
        {
            return null;
        }

        XYChart.Series<Double, Double> series = new XYChart.Series<>();

        for( Path.Segment segment : segments )
        {
            // Holds x, y data for a single entry in the series.
            XYChart.Data<Double, Double> data = new XYChart.Data<>();

            // Set the x, y data.
            data.setXValue( segment.x );
            data.setYValue( segment.y );

            // Add the data to the series.
            series.getData().add( data );
        }

        return series;
    }

    /**
     * Builds a series from the given Waypoints array that is ready to be displayed on a LineChart.
     * @param waypoints Array of waypoints to build a series for.
     */
    private void buildWaypointSeries( List<Waypoint> waypoints )
    {
        if( ( null == waypoints                                                 )
         || ( 0 == waypoints.size()                                             )
         || ( SourcePathDisplayType.NONE == settings.getSourcePathDisplayType() ) )
        {
            waypointSeries = null;
            return;
        }

        waypointSeries = new XYChart.Series<>();

        for( Waypoint w : waypoints )
        {
            // Holds x, y data for a single entry in the series.
            XYChart.Data<Double, Double> data = new XYChart.Data<>();

            // Set the x, y data.
            data.setXValue( w.getX() );
            data.setYValue( w.getY() );

            // Add the data to the series.
            waypointSeries.getData().add( data );
        }
    }

    private void setOnPointEvent( XYChart.Data data )
    {
        Node node = data.getNode();

        node.setOnMouseEntered( event ->
        {
            node.setCursor( Cursor.HAND );
        });

        node.setOnMouseDragged( event ->
        {
            // get pixel location
            Point2D mouseSceneCoords = new Point2D( event.getSceneX(), event.getSceneY() );
            double xLocal = axisPosX.sceneToLocal( mouseSceneCoords ).getX();
            double yLocal = axisPosY.sceneToLocal( mouseSceneCoords ).getY();

            // get location in units (ft, m, in)
            double raw_x = axisPosX.getValueForDisplay( xLocal ).doubleValue();
            double raw_y = axisPosY.getValueForDisplay( yLocal ).doubleValue();

            // round location
            double rnd_x;
            double rnd_y;

            // Snap to grid
            if( vars.getUnit() == Units.FEET )
            {
                rnd_x = Mathf.round( raw_x, 0.5 );
                rnd_y = Mathf.round( raw_y, 0.5 );
            }
            else if( vars.getUnit() == Units.METERS )
            {
                rnd_x = Mathf.round( raw_x, 0.25 );
                rnd_y = Mathf.round( raw_y, 0.25 );
            }
            else // Inches
            {
                rnd_x = Mathf.round( raw_x, 6.0 );
                rnd_y = Mathf.round( raw_y, 6.0 );
            }

            if( rnd_x >= axisPosX.getLowerBound() && rnd_x <= axisPosX.getUpperBound() )
            {

                data.setXValue( rnd_x );

            }

            if( rnd_y >= axisPosY.getLowerBound() && rnd_y <= axisPosY.getUpperBound() )
            {
                data.setYValue( rnd_y );
            }
        });

        node.setOnMouseReleased( event ->
        {
            int index = Integer.parseInt( node.getId() );

            Waypoint tmp = backend.getWaypoint( index  );
            tmp.setX( (Double) data.getXValue() );
            tmp.setY( (Double) data.getYValue() );
        });
    }

    @FXML
    private void addPointOnClick( MouseEvent event )
    {
        if( settings.isAddPointOnClick() )
        {
            // Only add a point if mouse has not moved since clicking. This filters out the mouse event from dragging a point.
            if ( event.isStillSincePress() )
            {
                // get pixel location
                Point2D mouseSceneCoords = new Point2D( event.getSceneX(), event.getSceneY() );
                double xLocal = axisPosX.sceneToLocal( mouseSceneCoords ).getX();
                double yLocal = axisPosY.sceneToLocal( mouseSceneCoords ).getY();

                // get location in units (ft, m, in)
                double raw_x = axisPosX.getValueForDisplay( xLocal ).doubleValue();
                double raw_y = axisPosY.getValueForDisplay( yLocal ).doubleValue();

                // round location
                double rnd_x;
                double rnd_y;

                if( vars.getUnit() == Units.FEET )
                {
                    rnd_x = Mathf.round( raw_x, 0.5 );
                    rnd_y = Mathf.round( raw_y, 0.5 );
                }
                else if( vars.getUnit() == Units.METERS )
                {
                    rnd_x = Mathf.round( raw_x, 0.25 );
                    rnd_y = Mathf.round( raw_y, 0.25 );
                }
                else if( vars.getUnit() == Units.INCHES )
                {
                    rnd_x = Mathf.round( raw_x, 6.0 );
                    rnd_y = Mathf.round( raw_y, 6.0 );
                }
                else
                {
                    rnd_x = Mathf.round( raw_x, 2 );
                    rnd_y = Mathf.round( raw_y, 2 );
                }

                if( ( rnd_x >= axisPosX.getLowerBound() && rnd_x <= axisPosX.getUpperBound() )
                 && ( rnd_y >= axisPosY.getLowerBound() && rnd_y <= axisPosY.getUpperBound() ) )
                {

                    // Clicking to add point not working on Mac???
                    if ( OSValidator.isMac() )
                    {
                        Optional<Waypoint> result;
                        result = DialogFactory.createWaypointDialog( String.valueOf( rnd_x ), String.valueOf( rnd_y ) ).showAndWait();
                        result.ifPresent( ( Waypoint w ) -> backend.addPoint( w ) );
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
        else
        {
            event.consume();
        }

    }

}
