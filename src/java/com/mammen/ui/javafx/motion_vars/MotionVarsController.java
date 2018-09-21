package com.mammen.ui.javafx.motion_vars;

import com.mammen.main.ProfileGenerator;
import com.mammen.ui.javafx.graphs.PosGraphController;
import com.mammen.ui.javafx.graphs.VelGraphController;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Waypoint;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.converter.DoubleStringConverter;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MotionVarsController
{
    @FXML
    private Label
            lblWheelBaseD;

    @FXML
    private TextField
            txtTimeStep,
            txtVelocity,
            txtAcceleration,
            txtJerk,
            txtWheelBaseW,
            txtWheelBaseD;

    @FXML
    private ChoiceBox<ProfileGenerator.FitMethod> choFitMethod;

    @FXML
    private ChoiceBox<ProfileGenerator.DriveBase> choDriveBase;

    @FXML
    private ChoiceBox<ProfileGenerator.Units> choUnits;

    private PosGraphController posGraph;
    private VelGraphController velGraph;
    private ProfileGenerator backend;
    private ObservableList<Waypoint> waypointsList;

    // Prevent unit conversion for next unit change event.
    private boolean disblUnitConv = false;

    public void setup( ProfileGenerator backend, ObservableList<Waypoint> waypointsList, PosGraphController posGraph, VelGraphController velGraph )
    {
        this.backend = backend;
        this.waypointsList = waypointsList;
        this.velGraph = velGraph;
        this.posGraph = posGraph;
    } /* setup() */

    @FXML
    private void initialize()
    {
        // Populate drive base ChoiceBox
        choDriveBase.getItems().setAll( ProfileGenerator.DriveBase.values() );
        choDriveBase.setValue( ProfileGenerator.DriveBase.TANK );
        choDriveBase.getSelectionModel().selectedItemProperty().addListener( this::updateDriveBase );

        // Populate fit method ChoiceBox
        choFitMethod.getItems().setAll( ProfileGenerator.FitMethod.values() );
        choFitMethod.setValue( ProfileGenerator.FitMethod.HERMITE_CUBIC );
        choFitMethod.getSelectionModel().selectedItemProperty().addListener( this::updateFitMethod );

        // Populate units ChoiceBox
        choUnits.getItems().setAll( ProfileGenerator.Units.values() );
        choUnits.setValue( ProfileGenerator.Units.FEET );
        choUnits.getSelectionModel().selectedItemProperty().addListener( this::updateUnits );


        txtTimeStep.setTextFormatter(new TextFormatter<>(new DoubleStringConverter()));
        txtVelocity.setTextFormatter(new TextFormatter<>(new DoubleStringConverter()));
        txtAcceleration.setTextFormatter(new TextFormatter<>(new DoubleStringConverter()));
        txtJerk.setTextFormatter(new TextFormatter<>(new DoubleStringConverter()));
        txtWheelBaseW.setTextFormatter(new TextFormatter<>(new DoubleStringConverter()));
        txtWheelBaseD.setTextFormatter(new TextFormatter<>(new DoubleStringConverter()));

        txtTimeStep.focusedProperty().addListener((observable, oldValue, newValue) ->
        {
            if (!newValue) // On unfocus
            {
                String val = txtTimeStep.getText().trim();
                double d = 0;

                if (val.isEmpty())
                {
                    val = "0.02";
                    txtTimeStep.setText(val);
                }
                else
                {
                    d = Double.parseDouble(val);
                    if (d != 0)
                    {
                        txtTimeStep.setText("" + Math.abs(d));
                        //generateTrajectories();
                    }
                }
            }
        });

        txtVelocity.focusedProperty().addListener((observable, oldValue, newValue) ->
        {
            if (!newValue) // On unfocus
            {
                String val = txtVelocity.getText().trim();
                double d = 0;

                if (val.isEmpty())
                {
                    val = "4.0";
                    txtVelocity.setText(val);
                }
                else
                {
                    d = Double.parseDouble(val);
                    if (d != 0)
                    {
                        txtVelocity.setText("" + Math.abs(d));
                        //generateTrajectories();
                    }
                }
            }
        });

        txtAcceleration.focusedProperty().addListener((observable, oldValue, newValue) ->
        {
            if (!newValue) // On unfocus
            {
                String val = txtAcceleration.getText().trim();
                double d = 0;

                if (val.isEmpty())
                {
                    val = "3.0";
                    txtAcceleration.setText(val);
                }
                else
                {
                    d = Double.parseDouble(val);
                    if (d != 0)
                    {
                        txtAcceleration.setText("" + Math.abs(d));
                        //generateTrajectories();
                    }
                }
            }
        });

        txtJerk.focusedProperty().addListener((observable, oldValue, newValue) ->
        {
            if (!newValue) // On unfocus
            {
                String val = txtJerk.getText().trim();
                double d = 0;

                if (val.isEmpty())
                {
                    val = "60.0";
                    txtJerk.setText(val);
                }
                else
                {
                    d = Double.parseDouble(val);
                    if (d != 0)
                    {
                        txtJerk.setText("" + Math.abs(d));
                        //generateTrajectories();
                    }
                }
            }
        });

        txtWheelBaseW.focusedProperty().addListener((observable, oldValue, newValue) ->
        {
            if (!newValue) // On unfocus
            {
                String val = txtWheelBaseW.getText().trim();
                double d = 0;

                if (val.isEmpty())
                {
                    val = "1.464";
                    txtWheelBaseW.setText(val);
                }
                else
                {
                    d = Double.parseDouble(val);
                    if (d != 0)
                    {
                        txtWheelBaseW.setText("" + Math.abs(d));
                        //generateTrajectories();
                    }
                }
            }
        });

        txtWheelBaseD.focusedProperty().addListener((observable, oldValue, newValue) ->
        {
            if (!newValue) // On unfocus
            {
                String val = txtWheelBaseD.getText().trim();
                double d = 0;

                if (val.isEmpty())
                {
                    val = "1.464";
                    txtWheelBaseD.setText(val);
                }
                else
                {
                    d = Double.parseDouble(val);
                    if (d != 0)
                    {
                        txtWheelBaseD.setText( "" + Math.abs(d) );
                        //generateTrajectories();
                    }
                }
            }
        });
    }

    public void disableUnitConv()
    {
        disblUnitConv = false;
    }

    /**
     * Updates all fields and views in the UI from data from the backend.
     */
    public void updateFrontend()
    {
        txtTimeStep.setText("" + backend.getTimeStep());
        txtVelocity.setText("" + backend.getVelocity());
        txtAcceleration.setText("" + backend.getAcceleration());
        txtJerk.setText("" + backend.getJerk());
        txtWheelBaseW.setText("" + backend.getWheelBaseW());
        txtWheelBaseD.setText("" + backend.getWheelBaseD());

        choDriveBase.setValue(choDriveBase.getItems().get(backend.getDriveBase().ordinal()));
        choFitMethod.setValue(choFitMethod.getItems().get(backend.getFitMethod().ordinal()));
        choUnits.setValue( backend.getUnits() );

        refreshWaypointTable();
    } /* updateFrontend() */

    public void updateBackend()
    {
        backend.setTimeStep( Double.parseDouble( txtTimeStep.getText().trim() ) );
        backend.setVelocity( Double.parseDouble( txtVelocity.getText().trim() ) );
        backend.setAcceleration( Double.parseDouble( txtAcceleration.getText().trim() ) );
        backend.setJerk( Double.parseDouble( txtJerk.getText().trim() ) );
        backend.setWheelBaseW( Double.parseDouble( txtWheelBaseW.getText().trim() ) );
        backend.setWheelBaseD( Double.parseDouble( txtWheelBaseD.getText().trim() ) );
    } /* updateBackend() */

    private void updateDriveBase(ObservableValue<? extends ProfileGenerator.DriveBase> observable, ProfileGenerator.DriveBase oldValue, ProfileGenerator.DriveBase newValue )
    {
        backend.setDriveBase( newValue );

        // Disable for tank drive
        txtWheelBaseD.setDisable( newValue == ProfileGenerator.DriveBase.TANK );
        lblWheelBaseD.setDisable( newValue == ProfileGenerator.DriveBase.TANK );

        generateTrajectories();
    } /* updateDriveBase() */

    private void updateFitMethod( ObservableValue<? extends ProfileGenerator.FitMethod> observable, ProfileGenerator.FitMethod oldValue, ProfileGenerator.FitMethod newValue )
    {
        backend.setFitMethod( newValue );

        generateTrajectories();
    } /* updateFitMethod() */

    private void updateUnits( ObservableValue<? extends ProfileGenerator.Units> observable, ProfileGenerator.Units oldValue, ProfileGenerator.Units newValue )
    {
        if( disblUnitConv )
        {
            // Only re-enable when the problematic event comes through
            disblUnitConv = false;
        }
        else
        {
            backend.setUnits( newValue );

            backend.updateVarUnits( oldValue, newValue );

            posGraph.updateAxis( newValue );
            velGraph.updateAxis( newValue );
            updateFrontend();
        }
    } /* updateUnits() */

    @FXML
    private void validateFieldEdit(ActionEvent event)
    {
        String val = ((TextField) event.getSource()).getText().trim();
        double d = 0;
        boolean validInput = true;

        try
        {
            d = Double.parseDouble(val);

            validInput = d > 0;
        }
        catch (NumberFormatException e)
        {
            validInput = false;
        }
        finally
        {
            if (validInput)
            {
                //generateTrajectories();
            }
            else
                Toolkit.getDefaultToolkit().beep();
        }
    } /* validateFieldEdit() */


    /**
     * Refreshes the waypoints table by clearing the waypoint list and repopulating it.
     */
    public void refreshWaypointTable()
    {
        // Bad way to update the waypoint list...
        // However, TableView.refresh() is apparently borked?
        List<Waypoint> tmp = new ArrayList<>( backend.getWaypointsList() );
        waypointsList.clear();
        waypointsList.addAll(tmp);
    } /* refreshWaypointTable() */

    // TODO: Move this method somewhere else.
    /**
     * Generates a path from the current set of waypoints and updates the graph with the new path.
     * @return True if the path was successfully generated. False otherwise.
     */
    private boolean generateTrajectories()
    {
        // Need at least two points to generate a path.
        if( waypointsList.size() > 1 )
        {
            try
            {
                backend.updateTrajectories();
            }
            // The given points cannot form a valid path
            catch( Pathfinder.GenerationException e )
            {
                Toolkit.getDefaultToolkit().beep();

                Alert alert = new Alert( Alert.AlertType.WARNING );

                alert.setTitle( "Invalid Trajectory" );
                alert.setHeaderText( "Invalid trajectory point!" );
                alert.setContentText( "The trajectory point is invalid because one of the waypoints is invalid! " +
                        "Please check the waypoints and try again." );
                alert.showAndWait();

                return false;
            }

            // Update the chart with the new path.
            posGraph.refresh();
            velGraph.refresh();

            return true;
        }
        // Not enough points to generate a path.
        else
        {
            return false;
        }
    } /* generateTrajectories() */
}

