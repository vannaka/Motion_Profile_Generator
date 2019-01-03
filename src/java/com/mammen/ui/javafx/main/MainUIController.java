package com.mammen.ui.javafx.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import com.mammen.generator.Generator;
import com.mammen.path.Waypoint;
import com.mammen.settings.SettingsModel;
import com.mammen.ui.javafx.dialog.factory.AlertFactory;
import com.mammen.ui.javafx.dialog.factory.DialogFactory;
import com.mammen.main.MainUIModel;
import com.mammen.util.Mathf;

import javafx.application.Platform;
import javafx.beans.value.ObservableValueBase;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.MenuItem;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import javafx.util.converter.DoubleStringConverter;

public class MainUIController 
{
	private MainUIModel backend;
	
	@FXML
    private Pane root;

    @FXML
    private TableView<Waypoint> tblWaypoints;

    @FXML
    private TableColumn<Waypoint, Double>
        colWaypointX,
        colWaypointY,
        colWaypointAngle;
    
    @FXML
    private MenuItem
        mnuFileSave;

    @FXML
    private Button
        btnClearPoints,
        btnDelete;

    @FXML
    private LineChart<Double, Double> posGraph;

    @FXML
    private TabPane graphTabs;

    // Reference to the PosGraphController object that JavaFX creates when it loads the fxml file.
    // This has to be named exactly like this
//    @FXML
//    private PosGraphController posGraphController;
//
//    @FXML
//    private VelGraphController velGraphController;

    private SettingsModel settings;

    // Last directory saved/exported to.
    private File workingDirectory;


    /**************************************************************************
     *  initialize
     *      Setup gui stuff here.
     *************************************************************************/
    @FXML public void initialize()
    {
        backend = MainUIModel.getInstance();
        settings = SettingsModel.getInstance();

        // Retrieve the working dir from our properties file.
        // If the path isn't a dir for some reason, default to the user directory
		workingDirectory = new File( settings.getWorkingDirectory() );

        if( !workingDirectory.exists() || !workingDirectory.isDirectory() )
        {
            workingDirectory = new File( System.getProperty( "user.dir" ) );
        }

        // Disable delete btn until we have points to delete.
        btnDelete.setDisable( true );

        // Make sure only doubles are entered for waypoints.
        Callback<TableColumn<Waypoint, Double>, TableCell<Waypoint, Double>> doubleCallback =
            ( TableColumn<Waypoint, Double> param ) ->
        {
                TextFieldTableCell<Waypoint, Double> cell = new TextFieldTableCell<>();

                cell.setConverter( new DoubleStringConverter() );

                return cell;
        };

        // Handle editing waypoints table elements
        EventHandler<TableColumn.CellEditEvent<Waypoint, Double>> editHandler = ( TableColumn.CellEditEvent<Waypoint, Double> t ) ->
        {
                Waypoint curWaypoint = t.getRowValue();

                if( t.getTableColumn() == colWaypointAngle )
                    curWaypoint.setAngle( t.getNewValue() );
                else if (t.getTableColumn() == colWaypointY )
                    curWaypoint.setY( t.getNewValue() );
                else
                    curWaypoint.setX( t.getNewValue() );

        };
        
        colWaypointX.setCellFactory( doubleCallback );
        colWaypointY.setCellFactory( doubleCallback );
        colWaypointAngle.setCellFactory( doubleCallback );

        colWaypointX.setOnEditCommit( editHandler );
        colWaypointY.setOnEditCommit( editHandler );
        colWaypointAngle.setOnEditCommit( editHandler );

        colWaypointX.setCellValueFactory( ( TableColumn.CellDataFeatures<Waypoint, Double> d ) -> new ObservableValueBase<Double>()
        {
            @Override
            public Double getValue()
            {
                return d.getValue().getX();
            }
        });

        colWaypointY.setCellValueFactory( (TableColumn.CellDataFeatures<Waypoint, Double> d) -> new ObservableValueBase<Double>()
        {
            @Override
            public Double getValue() {
                return d.getValue().getY();
            }
        });

        colWaypointAngle.setCellValueFactory( (TableColumn.CellDataFeatures<Waypoint, Double> d) -> new ObservableValueBase<Double>()
        {
            @Override
            public Double getValue()
            {
                return Mathf.round( d.getValue().getAngle(), 2 );
            }
        });


        /* ************************************************
         *  Waypoints Table Stuff
         *************************************************/
        backend.waypointListProperty().addListener( ( ListChangeListener<Waypoint> ) c ->
        {
            // Disable btn if no points exist
            btnClearPoints.setDisable( backend.isWaypointListEmpty() );
            tblWaypoints.refresh();

            try
            {
                // Generate new path with new settings
                if( backend.getNumWaypoints() > 1 )
                    backend.generatePath();
            }
            catch( Generator.PathGenerationException | Generator.NotEnoughPointsException e )
            {
                Alert alert = new Alert( Alert.AlertType.INFORMATION );
                alert.setTitle( "Invalid point" );
                alert.setHeaderText( "Invalid point" );
                alert.setContentText( "The point you entered was invalid.");
                alert.showAndWait();

                // Run later because we don't want to modify a list inside it's own change listener.
                Platform.runLater( () ->
                {
                    // Remove problem point.
                    backend.removeLastPoint();
                });
            }
        });

        tblWaypoints.itemsProperty().bindBidirectional( backend.waypointListProperty() );

        tblWaypoints.getSelectionModel().setSelectionMode( SelectionMode.MULTIPLE );
        tblWaypoints.getSelectionModel().selectedIndexProperty().addListener( (observable, oldValue, newValue) ->
            btnDelete.setDisable( tblWaypoints.getSelectionModel().getSelectedIndices().get(0) == -1 )
        );
        
        Runtime.getRuntime().addShutdownHook( new Thread( () ->
        {
            settings.setWorkingDirectory( workingDirectory.getAbsolutePath() );

            try
            {
                settings.saveSettings();
            }
            catch( IOException e )
            {
                e.printStackTrace();
            }
        }));

        // Maintains the aspect ratio of the position graph
        graphTabs.widthProperty().addListener( (o, oldValue, newValue) ->
        {
            double tabWidth = graphTabs.getWidth();
            double tabHeight = graphTabs.getHeight();

            double newHeight = tabWidth / 2;

            if( newHeight > tabHeight )
            {
                posGraph.setPrefHeight( tabHeight );
                posGraph.setPrefWidth( tabHeight * 2 );
            }
            else
            {
                posGraph.setPrefHeight( newHeight );
                posGraph.setPrefWidth( tabWidth );
            }

        });

        graphTabs.heightProperty().addListener( (o, oldValue, newValue) ->
        {
            double tabWidth = graphTabs.getWidth();
            double tabHeight = graphTabs.getHeight();

            double newWidth = tabHeight * 2;

            if( newWidth > tabWidth )
            {
                posGraph.setPrefHeight( tabWidth / 2 );
                posGraph.setPrefWidth( tabWidth );
            }
            else
            {
                posGraph.setPrefHeight( tabHeight );
                posGraph.setPrefWidth( newWidth );
            }
        });


    } /* initialize() */
    
    @FXML
    private void showSettingsDialog()
    {
        Dialog<Boolean> settingsDialog = DialogFactory.createSettingsDialog();
        Optional<Boolean> result;

        // Wait for the result
        result = settingsDialog.showAndWait();

        result.ifPresent( (Boolean b) ->
        {
            if( b )
            {
                try
                {
                    // Generate new path with new settings
                    backend.generatePath();

                    //posGraphController.setBGImg();

                    settings.saveSettings();
                }
                catch( IOException e )
                {
                    Alert alert = AlertFactory.createExceptionAlert( e );
                    alert.showAndWait();
                }
                catch( Generator.PathGenerationException | Generator.NotEnoughPointsException e )
                {
                    // Don't do anything.
                    // TODO: Probably should do something here
                }
            }
        });
    } /* showSettingsDialog() */
    
    @FXML
    private void openAboutDialog()
    {
        Dialog<Boolean> aboutDialog = DialogFactory.createAboutDialog();

        aboutDialog.showAndWait();
    } /* openAboutDialog() */
    
    @FXML
    private void showExportDialog()
    {
        if( backend.getNumWaypoints() < 2 )
        {
            Alert alert = new Alert( Alert.AlertType.WARNING );

            alert.setTitle("Not enough waypoints");
            alert.setHeaderText("Not enough waypoints");
            alert.setContentText("More than one waypoint needed to export a path.");
            alert.showAndWait();
            return;
        }

        FileChooser fileChooser = new FileChooser();

        fileChooser.setInitialDirectory( new File( System.getProperty("user.dir") ) );
        fileChooser.setTitle("Export");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Comma Separated Values", "*.csv" )
        );

        File result = fileChooser.showSaveDialog( root.getScene().getWindow() );

        if( result != null )
        {
            String parentPath = result.getAbsolutePath();
            String ext = parentPath.substring( parentPath.lastIndexOf(".") );

            parentPath = parentPath.substring( 0, parentPath.lastIndexOf( ext ) );
            
            try
            {
                backend.exportPath( new File( parentPath ) );
            }
            catch( FileNotFoundException e )
            {
                Alert alert = new Alert( Alert.AlertType.ERROR);

                alert.setTitle( "FileNotFoundException" );
                alert.setHeaderText( "FileNotFoundException" );
                alert.setContentText( e.getLocalizedMessage() );
                alert.showAndWait();
			}
        }
    } /* showExportDialog() */
    
    @FXML
    private void showSaveAsDialog()
    {
        FileChooser fileChooser = new FileChooser();

        fileChooser.setInitialDirectory(workingDirectory);
        fileChooser.setTitle("Save As");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Extensive Markup Language", "*.xml")
        );

        File result = fileChooser.showSaveDialog( root.getScene().getWindow() );

        if (result != null)
        {
            try
            {
                workingDirectory = result.getParentFile();
                backend.saveProjectAs( result );
                mnuFileSave.setDisable( false );
            }
            catch( Exception e )
            {
                Alert alert = AlertFactory.createExceptionAlert( e );
                alert.showAndWait();
            }
        }
    } /* showSaveAsDialog() */
    
    @FXML
    private void showOpenDialog()
    {
        FileChooser fileChooser = new FileChooser();

        fileChooser.setInitialDirectory( workingDirectory );
        fileChooser.setTitle("Open Project");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Extensive Markup Language", "*.xml")
        );

        File result = fileChooser.showOpenDialog( root.getScene().getWindow() );

        if( result != null )
        {
            try
            {
                workingDirectory = result.getParentFile();
                backend.loadProject( result );

                mnuFileSave.setDisable( false );
            }
            catch( Exception e )
            {
                Alert alert = AlertFactory.createExceptionAlert( e );
                alert.showAndWait();
            }
        }
    } /* showOpenDialog() */
    
    @FXML
    private void save()
    {
        try
        {
            backend.saveWorkingProject();
        }
        catch( Exception e )
        {
            Alert alert = AlertFactory.createExceptionAlert( e );
            alert.showAndWait();
        }
    } /* save() */
    
    @FXML
    private void exit()
    {
        System.exit(0 );
    } /* exit() */
    
    @FXML
    private void resetData()
    {
        Alert alert = new Alert( Alert.AlertType.CONFIRMATION );

        alert.setTitle( "Create New Project?" );
        alert.setHeaderText( "Confirm Reset" );
        alert.setContentText( "Are you sure you want to reset all data? All unsaved work will be lost." );

        Optional<ButtonType> result = alert.showAndWait();

        result.ifPresent((ButtonType t) ->
        {
            if( t == ButtonType.OK )
            {
                backend.clearWorkingFiles();
                backend.clearPoints();

                settings.getGeneratorVars().setDefaultValues();

                mnuFileSave.setDisable( true );
            }
        });
    } /* resetData() */

    @FXML
    private void showAddPointDialog() 
    {
        Dialog<Waypoint> waypointDialog = DialogFactory.createWaypointDialog();
        Optional<Waypoint> result;

        // Wait for the result
        result = waypointDialog.showAndWait();

        result.ifPresent( (Waypoint w) ->
            backend.addPoint( w )
        );

    } /* showAddPointDialog() */
    
    @FXML
    private void showClearPointsDialog() 
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        alert.setTitle("Clear Points");
        alert.setHeaderText("Clear All Points?");
        alert.setContentText("Are you sure you want to clear all points?");
        
        Optional<ButtonType> result = alert.showAndWait();
        
        result.ifPresent( (ButtonType t) ->
        {
            if( t.getButtonData() == ButtonBar.ButtonData.OK_DONE )
                backend.clearPoints();
        });
    } /* showClearPointsDialog() */
    
    @FXML
    private void deletePoints() 
    {
        List<Integer> selectedIndices = tblWaypoints.getSelectionModel().getSelectedIndices();

        int firstIndex = selectedIndices.get( 0 );
        int lastIndex = selectedIndices.get( selectedIndices.size() - 1 );

        backend.removePoints( firstIndex, lastIndex );
    } /* deletePoints() */
}
