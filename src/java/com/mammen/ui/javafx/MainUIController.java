package com.mammen.ui.javafx;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import com.mammen.ui.javafx.factory.AlertFactory;
import com.mammen.ui.javafx.graphs.PosGraphController;
import com.mammen.ui.javafx.graphs.VelGraphController;
import com.mammen.ui.javafx.factory.DialogFactory;
import com.mammen.main.ProfileGenerator;

import com.mammen.ui.javafx.motion_vars.MotionVarsController;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Waypoint;

import org.scijava.nativelib.NativeLoader;

import javafx.beans.value.ObservableValue;
import javafx.beans.value.ObservableValueBase;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import javafx.util.converter.DoubleStringConverter;

import static com.mammen.util.Mathf.round;

public class MainUIController 
{
	private ProfileGenerator backend;
	
	@FXML
    private Pane root;

    @FXML
    private TableView<Waypoint> tblWaypoints;

    @FXML
    private LineChart<Double, Double>
        chtVelocity;

    @FXML
    private TableColumn<Waypoint, Double>
        colWaypointX,
        colWaypointY,
        colWaypointAngle;
    
    @FXML
    private MenuItem
        mnuOpen,
        mnuFileNew,
        mnuFileSave,
        mnuFileSaveAs,
        mnuFileExport,
        mnuFileExit,
        mnuHelpAbout;

    @FXML
    private Button
        btnAddPoint,
        btnClearPoints,
        btnDelete;

    // Interface for property manipulation.
    private Properties properties;

    // Last directory saved/exported to.
    private File workingDirectory;

    // Reference to the PosGraphController object that JavaFX creates when it loads the fxml file.
    // This has to be named exactly like this
    @FXML
    private PosGraphController posGraphController;
    private PosGraphController posGraph;

    @FXML
    private VelGraphController velGraphController;
    private VelGraphController velGraph;

    @FXML
    private MotionVarsController motionVarsController;
    private MotionVarsController motionVars;
    
    @FXML
    public void initialize() 
    {
        backend = new ProfileGenerator();
        properties = PropWrapper.getProperties();

        // Setup position graph
        posGraph = posGraphController;  // Alias because it looks better.
        posGraph.setup( backend );
        posGraph.setBGImg( properties.getProperty("ui.overlayImg", "") );

        // Setup velocity graph
        velGraph = velGraphController;  // Alias because it looks better.
        velGraph.setup( backend );

        // Setup motion variables
        motionVars = motionVarsController;
        motionVars.setup( backend );


        // Retrieve the working dir from our properties file.
        // If the path isn't a dir for some reason, default to the user directory
		workingDirectory = new File( properties.getProperty("file.workingDir", System.getProperty("user.dir") ) );
        if( !workingDirectory.exists() || !workingDirectory.isDirectory() )
        {
            workingDirectory = new File( System.getProperty("user.dir") );
        }

        // Disable delete btn until we have points to delete.
        btnDelete.setDisable( true );



        // Make sure only doubles are entered for waypoints.
        Callback<TableColumn<Waypoint, Double>, TableCell<Waypoint, Double>> doubleCallback =
            (TableColumn<Waypoint, Double> param) ->
        {
                TextFieldTableCell<Waypoint, Double> cell = new TextFieldTableCell<>();

                cell.setConverter( new DoubleStringConverter() );

                return cell;
        };

        EventHandler<TableColumn.CellEditEvent<Waypoint, Double>> editHandler =
            (TableColumn.CellEditEvent<Waypoint, Double> t) ->
        {
                Waypoint curWaypoint = t.getRowValue();

                if (t.getTableColumn() == colWaypointAngle)
                    curWaypoint.angle = Pathfinder.d2r(t.getNewValue());
                else if (t.getTableColumn() == colWaypointY)
                    curWaypoint.y = t.getNewValue();
                else
                    curWaypoint.x = t.getNewValue();

        };
        
        colWaypointX.setCellFactory(doubleCallback);
        colWaypointY.setCellFactory(doubleCallback);
        colWaypointAngle.setCellFactory(doubleCallback);

        colWaypointX.setOnEditCommit(editHandler);
        colWaypointY.setOnEditCommit(editHandler);
        colWaypointAngle.setOnEditCommit(editHandler);

        colWaypointX.setCellValueFactory( (TableColumn.CellDataFeatures<Waypoint, Double> d) -> new ObservableValueBase<Double>()
        {
            @Override
            public Double getValue()
            {
                return d.getValue().x;
            }
        });

        colWaypointY.setCellValueFactory((TableColumn.CellDataFeatures<Waypoint, Double> d) -> new ObservableValueBase<Double>()
        {
            @Override
            public Double getValue() {
                return d.getValue().y;
            }
        });

        colWaypointAngle.setCellValueFactory((TableColumn.CellDataFeatures<Waypoint, Double> d) -> new ObservableValueBase<Double>()
        {
            @Override
            public Double getValue()
            {
                return round( Pathfinder.r2d( d.getValue().angle ), 2 );
            }
        });

        backend.waypointListProperty().addListener( (ListChangeListener<Waypoint>) c ->
        {
            // Disable btn if no points exist
            btnClearPoints.setDisable( backend.getNumWaywaypoints() == 0 );

            // If the traj failed to generate then remove the problematic point.
            // TODO: fix this
//            if( backend.getNumWaywaypoints() > 1 && !generateTrajectories() )
//            {
//                backend.removeLastPoint();
//            }

            // Redraw new chart to show new changes
            posGraph.refresh();
            velGraph.refresh();
        });

        tblWaypoints.setItems( backend.waypointListProperty() );
        tblWaypoints.getSelectionModel().setSelectionMode( SelectionMode.MULTIPLE );
        tblWaypoints.getSelectionModel().selectedIndexProperty().addListener( (observable, oldValue, newValue) ->
                btnDelete.setDisable( tblWaypoints.getSelectionModel().getSelectedIndices().get(0) == -1 )
        );

        // Populate motion vars with data from the backend.
        //motionVars.updateFrontend();
        
        Runtime.getRuntime().addShutdownHook( new Thread(() -> {
            properties.setProperty( "file.workingDir", workingDirectory.getAbsolutePath() );
            try
            {
                PropWrapper.storeProperties();
            }
            catch( IOException e )
            {
                e.printStackTrace();
            }
        }));
    } /* initialize() */
    
    @FXML
    private void showSettingsDialog()
    {
        Dialog<Boolean> settingsDialog = DialogFactory.createSettingsDialog();
        Optional<Boolean> result = null;

        // Wait for the result
        result = settingsDialog.showAndWait();

        result.ifPresent( (Boolean b) -> {
            if( b )
            {
                try
                {
                    DialogPane pane = settingsDialog.getDialogPane();

                    String overlayDir = ( (TextField)pane.lookup("#txtOverlayDir" ) ).getText().trim();

                    int sourceDisplay = ( (ChoiceBox)pane.lookup("#choSourceDisplay") )
                            .getSelectionModel()
                            .getSelectedIndex();
                    
                    int csvType = ( (ChoiceBox)pane.lookup("#choCSVType") )
                    		.getSelectionModel()
                    		.getSelectedIndex();

                    String availList = ((ListView)pane.lookup("#lst_availabel_vals"))
                            .getItems()
                            .toString()
                            .replace(", ", ",")
                            .replace("[", "")
                            .replace("]", "")
                            .replace(" ", "_");

                    String chosList = ((ListView)pane.lookup("#lst_chosen_vals"))
                            .getItems()
                            .toString()
                            .replace(", ", ",")
                            .replace("[", "")
                            .replace("]", "")
                            .replace(" ", "_");

                    boolean addWaypointOnClick = ((CheckBox) pane.lookup("#chkAddWaypointOnClick")).isSelected();

                    properties.setProperty("ui.overlayImg", overlayDir);
                    properties.setProperty("ui.sourceDisplay", "" + sourceDisplay);
                    properties.setProperty("ui.addWaypointOnClick", "" + addWaypointOnClick);
                    properties.setProperty("ui.csvType", "" + csvType);
                    properties.setProperty("csv.avail", "" + availList);
                    properties.setProperty("csv.chos", "" + chosList);

                    posGraph.setBGImg( overlayDir );
                    posGraph.refresh();

                    PropWrapper.storeProperties();
                }
                catch( IOException e )
                {
                    Alert alert = AlertFactory.createExceptionAlert( e );
                    alert.showAndWait();
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
        if( backend.getNumWaywaypoints() < 2 )
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);

            alert.setTitle("Not enough waypoints");
            alert.setHeaderText("Not enough waypoints");
            alert.setContentText("More than one waypoint needed to export a path.");
            alert.showAndWait();
            return;
        }

        FileChooser fileChooser = new FileChooser();

        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        fileChooser.setTitle("Export");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Comma Separated Values", "*.csv"),
                new FileChooser.ExtensionFilter("Binary Trajectory File", "*.traj")
        );

        File result = fileChooser.showSaveDialog(root.getScene().getWindow());

        if( result != null )
        {
            String parentPath = result.getAbsolutePath(), ext = parentPath.substring(parentPath.lastIndexOf("."));
            parentPath = parentPath.substring(0, parentPath.lastIndexOf(ext));

            String csvTypeStr = properties.getProperty("ui.csvType", "0");
            int csvType = Integer.parseInt(csvTypeStr);
            
            try
            {
            	if( csvType == 0 )
            	{
            		backend.exportTrajectoriesJaci(new File(parentPath), ext);
            	}
            	else
                {
            		backend.exportTrajectoriesTalon(new File(parentPath), ext);
            	}
            }
            catch( Pathfinder.GenerationException e )
            {
                Alert alert = AlertFactory.createExceptionAlert(e, "Invalid Trajectory!");

                alert.showAndWait();
            }
            catch( IOException e )
            {
				e.printStackTrace();
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

                // Temporarily disable unit conversion so we can update the units without 'converting' them unnecessarily.
                motionVars.disableUnitConv();
                //motionVars.updateFrontend();

                posGraph.updateAxis( backend.getUnits() );
                velGraph.updateAxis( backend.getUnits() );

//                generateTrajectories();

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
    private void showImportDialog()
    {
        FileChooser fileChooser = new FileChooser();

        fileChooser.setInitialDirectory(workingDirectory);
        fileChooser.setTitle("Import");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Vannaka Properties File", "*.bot")
        );

        File result = fileChooser.showOpenDialog(root.getScene().getWindow());

        if (result != null)
        {
            Dialog<ProfileGenerator.Units> unitsSelector = new Dialog<>();
            Optional<ProfileGenerator.Units> unitsResult = null;
            GridPane grid = new GridPane();
            ToggleGroup radGroup = new ToggleGroup();
            RadioButton
                radImperial = new RadioButton("Imperial (ft)"),
                radMetric = new RadioButton("Metric (m)");

            // Reset working directory
            workingDirectory = result.getParentFile();

            // Some header stuff
            unitsSelector.setTitle("Select Units");
            unitsSelector.setHeaderText("Select the distance units being used");

            // Some other UI stuff
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            grid.add(radImperial, 0, 0);
            grid.add(radMetric, 0, 1);

            radImperial.setToggleGroup(radGroup);
            radImperial.selectedProperty().set(true);
            radMetric.setToggleGroup(radGroup);

            unitsSelector.getDialogPane().setContent(grid);

            // Add all buttons
            unitsSelector.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
            unitsSelector.setResultConverter(buttonType -> {
                if (buttonType.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                    if (radMetric.selectedProperty().getValue())
                        return ProfileGenerator.Units.METERS;
                    else
                        return ProfileGenerator.Units.FEET;
                }

                return null;
            });

            unitsResult = unitsSelector.showAndWait();

            unitsResult.ifPresent(u -> {
                backend.clearPoints();
                try {
                    backend.importBotFile(result, u);

                    //motionVars.updateFrontend();
                    //generateTrajectories();

                    mnuFileSave.setDisable(!backend.hasWorkingProject());
                } catch (Exception e) {
                    Alert alert = AlertFactory.createExceptionAlert(e);

                    alert.showAndWait();
                }
            });
        }
    } /* showImportDialog() */
    
    @FXML
    private void save()
    {
        //updateBackend();

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

        alert.setTitle("Create New Project?");
        alert.setHeaderText("Confirm Reset");
        alert.setContentText("Are you sure you want to reset all data? Have you saved?");

        Optional<ButtonType> result = alert.showAndWait();

        result.ifPresent((ButtonType t) -> {
            if (t == ButtonType.OK) {
                backend.clearWorkingFiles();
                backend.setDefaultValues( ProfileGenerator.Units.FEET );

                //motionVars.updateFrontend();
                backend.clearPoints();

                posGraph.updateAxis( backend.getUnits() );
                velGraph.updateAxis( backend.getUnits() );

                mnuFileSave.setDisable( true );
            }
        });
    } /* resetData() */

    @FXML
    private void showAddPointDialog() 
    {
        Dialog<Waypoint> waypointDialog = DialogFactory.createWaypointDialog();
        Optional<Waypoint> result = null;

        // Wait for the result
        result = waypointDialog.showAndWait();

        result.ifPresent((Waypoint w) -> backend.addPoint( w ) );
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
