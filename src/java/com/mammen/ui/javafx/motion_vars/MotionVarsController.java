package com.mammen.ui.javafx.motion_vars;

import com.mammen.main.ProfileGenerator;
import com.mammen.ui.javafx.graphs.PosGraphController;
import com.mammen.ui.javafx.graphs.VelGraphController;
import com.mammen.util.Mathf;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Waypoint;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.NumberStringConverter;

import java.awt.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

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

    private ProfileGenerator backend;

    StringConverter<Number> converter;

    // Prevent unit conversion for next unit change event.
    private boolean disblUnitConv = false;

    public void setup( ProfileGenerator backend )
    {
        this.backend = backend;

        // Converts formatted string in TextField to format of bounded property.
        StringConverter<Number> converter = new NumberStringConverter();
//        converter = new StringConverter<Number>() {
//            @Override
//            public String toString(Number object)
//            {
//                if( object != null )
//                {
//                    return Double.toString( Mathf.round( (Double)object, 2 ) );
//                }
//                else
//                    return null;
//            }
//
//            @Override
//            public Number fromString(String string)
//            {
//                double d;
//
//                try
//                {
//                    d = Double.parseDouble( string );
//                }
//                catch( NumberFormatException e )
//                {
//                    // Keep old value
//                    d = backend.getWheelBaseW();
//                }
//
//                return d;
//            }
//        };

        // Setup Bindings
        choFitMethod    .valueProperty().bindBidirectional( this.backend.fitMethodProperty() );
        choDriveBase    .valueProperty().bindBidirectional( this.backend.driveBaseProperty() );
        choUnits        .valueProperty().bindBidirectional( this.backend.unitsProperty()     );
        txtTimeStep     .textProperty().bindBidirectional( backend.timeStepProperty(),   converter );
        txtVelocity     .textProperty().bindBidirectional( backend.velocityProperty(),   converter );
        txtAcceleration .textProperty().bindBidirectional( backend.accelProperty(),      converter );
        txtJerk         .textProperty().bindBidirectional( backend.jerkProperty(),       converter );
        txtWheelBaseW   .textProperty().bindBidirectional( backend.wheelBaseWProperty(), converter );
        txtWheelBaseD   .textProperty().bindBidirectional( backend.wheelBaseDProperty(), converter );

    } /* setup() */

    @FXML
    private void initialize()
    {
        // Populate drive base ChoiceBox
        choDriveBase.getItems().setAll( ProfileGenerator.DriveBase.values() );

        // Populate fit method ChoiceBox
        choFitMethod.getItems().setAll( ProfileGenerator.FitMethod.values() );

        // Populate units ChoiceBox
        choUnits.getItems().setAll( ProfileGenerator.Units.values() );


        // Formats number typed into TextFields
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String text = change.getText();
            System.out.println( "Changed Text: " + text );
            if (text.matches("[0-9\\.]*")) {
                return change;
            }
            return null;
        };

        UnaryOperator<TextFormatter.Change> filter2 = new UnaryOperator<TextFormatter.Change>() {

            @Override
            public TextFormatter.Change apply( TextFormatter.Change t )
            {
                if( t.isReplaced() )
                    if( t.getText().matches("[^0-9]") )
                        t.setText( t.getControlText().substring( t.getRangeStart(), t.getRangeEnd() ) );

                if( t.isAdded() )
                {
                    if ( t.getControlText().contains("."))
                    {
                        if( t.getText().matches("[^0-9]") )
                        {
                            t.setText("");
                        }
                    }
                    else if ( t.getText().matches("[^0-9\\.]") )
                    {
                        t.setText("");
                    }
                }

                return t;
            }
        };

        txtTimeStep     .setTextFormatter( new TextFormatter<>( filter ) );
        txtVelocity     .setTextFormatter( new TextFormatter<>( filter ) );
        txtAcceleration .setTextFormatter( new TextFormatter<>( filter ) );
        txtJerk         .setTextFormatter( new TextFormatter<>( filter ) );
        txtWheelBaseW   .setTextFormatter( new TextFormatter<>( filter ) );
        txtWheelBaseD   .setTextFormatter( new TextFormatter<>( filter ) );

//        txtTimeStep.focusedProperty().addListener( (observable, oldValue, newValue) ->
//        {
//            if( !newValue ) // On unfocus
//            {
//                String val = txtTimeStep.getText().trim();
//                double d = 0;
//
//                if (val.isEmpty())
//                {
//                    val = "0.02";
//                    txtTimeStep.setText(val);
//                }
//                else
//                {
//                    d = Double.parseDouble(val);
//                    if (d != 0)
//                    {
//                        txtTimeStep.setText("" + Math.abs(d));
//                        //generateTrajectories();
//                    }
//                }
//            }
//        });

//        txtVelocity.focusedProperty().addListener((observable, oldValue, newValue) ->
//        {
//            if (!newValue) // On unfocus
//            {
//                String val = txtVelocity.getText().trim();
//                double d = 0;
//
//                if (val.isEmpty())
//                {
//                    val = "4.0";
//                    txtVelocity.setText(val);
//                }
//                else
//                {
//                    d = Double.parseDouble(val);
//                    if (d != 0)
//                    {
//                        txtVelocity.setText("" + Math.abs(d));
//                        //generateTrajectories();
//                    }
//                }
//            }
//        });

//        txtAcceleration.focusedProperty().addListener((observable, oldValue, newValue) ->
//        {
//            if (!newValue) // On unfocus
//            {
//                String val = txtAcceleration.getText().trim();
//                double d = 0;
//
//                if (val.isEmpty())
//                {
//                    val = "3.0";
//                    txtAcceleration.setText(val);
//                }
//                else
//                {
//                    d = Double.parseDouble(val);
//                    if (d != 0)
//                    {
//                        txtAcceleration.setText("" + Math.abs(d));
//                        //generateTrajectories();
//                    }
//                }
//            }
//        });

//        txtJerk.focusedProperty().addListener((observable, oldValue, newValue) ->
//        {
//            if (!newValue) // On unfocus
//            {
//                String val = txtJerk.getText().trim();
//                double d = 0;
//
//                if (val.isEmpty())
//                {
//                    val = "60.0";
//                    txtJerk.setText(val);
//                }
//                else
//                {
//                    d = Double.parseDouble(val);
//                    if (d != 0)
//                    {
//                        txtJerk.setText("" + Math.abs(d));
//                        //generateTrajectories();
//                    }
//                }
//            }
//        });

//        txtWheelBaseW.focusedProperty().addListener((observable, oldValue, newValue) ->
//        {
//            if (!newValue) // On unfocus
//            {
//                String val = txtWheelBaseW.getText().trim();
//                double d = 0;
//
//                if (val.isEmpty())
//                {
//                    val = "1.464";
//                    txtWheelBaseW.setText(val);
//                }
//                else
//                {
//                    d = Double.parseDouble(val);
//                    if (d != 0)
//                    {
//                        txtWheelBaseW.setText("" + Math.abs(d));
//                        //generateTrajectories();
//                    }
//                }
//            }
//        });

//        txtWheelBaseD.focusedProperty().addListener((observable, oldValue, newValue) ->
//        {
//            if (!newValue) // On unfocus
//            {
//                String val = txtWheelBaseD.getText().trim();
//                double d = 0;
//
//                if (val.isEmpty())
//                {
//                    val = "1.464";
//                    txtWheelBaseD.setText(val);
//                }
//                else
//                {
//                    d = Double.parseDouble(val);
//                    if (d != 0)
//                    {
//                        txtWheelBaseD.setText( "" + Math.abs(d) );
//                        //generateTrajectories();
//                    }
//                }
//            }
//        });
    }

    public void disableUnitConv()
    {
        disblUnitConv = false;
    }

//    /**
//     * Updates all fields and views in the UI from data from the backend.
//     */
//    public void updateFrontend()
//    {
//        txtTimeStep.setText("" + backend.getTimeStep());
//        txtVelocity.setText("" + backend.getVelocity());
//        txtAcceleration.setText("" + backend.getAcceleration());
//        txtJerk.setText("" + backend.getJerk());
//        txtWheelBaseW.setText("" + backend.getWheelBaseW());
//        txtWheelBaseD.setText("" + backend.getWheelBaseD());
//
//        choDriveBase.setValue(choDriveBase.getItems().get(backend.getDriveBase().ordinal()));
//        choFitMethod.setValue(choFitMethod.getItems().get(backend.getFitMethod().ordinal()));
//        choUnits.setValue( backend.getUnits() );
//
//        refreshWaypointTable();
//    } /* updateFrontend() */

//    public void updateBackend()
//    {
//        backend.setTimeStep( Double.parseDouble( txtTimeStep.getText().trim() ) );
//        backend.setVelocity( Double.parseDouble( txtVelocity.getText().trim() ) );
//        backend.setAcceleration( Double.parseDouble( txtAcceleration.getText().trim() ) );
//        backend.setJerk( Double.parseDouble( txtJerk.getText().trim() ) );
//        backend.setWheelBaseW( Double.parseDouble( txtWheelBaseW.getText().trim() ) );
//        backend.setWheelBaseD( Double.parseDouble( txtWheelBaseD.getText().trim() ) );
//    } /* updateBackend() */

//    private void updateDriveBase(ObservableValue<? extends ProfileGenerator.DriveBase> observable, ProfileGenerator.DriveBase oldValue, ProfileGenerator.DriveBase newValue )
//    {
//        //backend.setDriveBase( newValue );
//
//        // Disable for tank drive
//        txtWheelBaseD.setDisable( newValue == ProfileGenerator.DriveBase.TANK );
//        lblWheelBaseD.setDisable( newValue == ProfileGenerator.DriveBase.TANK );
//
//        generateTrajectories();
//    } /* updateDriveBase() */

//    private void updateFitMethod( ObservableValue<? extends ProfileGenerator.FitMethod> observable, ProfileGenerator.FitMethod oldValue, ProfileGenerator.FitMethod newValue )
//    {
//        backend.setFitMethod( newValue );
//
//        generateTrajectories();
//    } /* updateFitMethod() */

//    private void updateUnits( ObservableValue<? extends ProfileGenerator.Units> observable, ProfileGenerator.Units oldValue, ProfileGenerator.Units newValue )
//    {
//        if( disblUnitConv )
//        {
//            // Only re-enable when the problematic event comes through
//            disblUnitConv = false;
//        }
//        else
//        {
//            //backend.setUnits( newValue );
//
//            //backend.updateVarUnits( oldValue, newValue );
//
//            //posGraph.updateAxis( newValue );
//            //velGraph.updateAxis( newValue );
//            //updateFrontend();
//        }
//    } /* updateUnits() */

    @FXML
    private void validateFieldEdit(ActionEvent event)
    {
//        // TODO: Why is this here; Appears to be useless???
//        String val = ((TextField) event.getSource()).getText().trim();
//        double d = 0;
//        boolean validInput = true;
//
//        try
//        {
//            d = Double.parseDouble(val);
//
//            validInput = d > 0;
//        }
//        catch (NumberFormatException e)
//        {
//            validInput = false;
//        }
//        finally
//        {
//            if (validInput)
//            {
//                //generateTrajectories();
//            }
//            else
//                Toolkit.getDefaultToolkit().beep();
//        }
    } /* validateFieldEdit() */


//    /**
//     * Refreshes the waypoints table by clearing the waypoint list and repopulating it.
//     */
//    public void refreshWaypointTable()
//    {
//        // Bad way to update the waypoint list...
//        // However, TableView.refresh() is apparently borked?
//        List<Waypoint> tmp = new ArrayList<>( backend.getWaypointList() );
//        waypointsList.clear();
//        waypointsList.addAll(tmp);
//    } /* refreshWaypointTable() */

}

