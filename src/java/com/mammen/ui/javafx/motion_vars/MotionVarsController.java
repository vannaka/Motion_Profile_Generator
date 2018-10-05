package com.mammen.ui.javafx.motion_vars;

import com.mammen.generator.ProfileGenerator;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;

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

    /**************************************************************************
     *  setup
     *      Setup backend linkages stuff here.
     *
     * @param backend Reference to the backend of the program.
     *************************************************************************/
    public void setup( ProfileGenerator backend )
    {
        this.backend = backend;

        // Converts formatted string in TextField to format of bounded property.
        StringConverter<Number> converter = new NumberStringConverter();

        // Setup Bindings
        choFitMethod    .valueProperty().bindBidirectional( backend.fitMethodProperty() );
        choDriveBase    .valueProperty().bindBidirectional( backend.driveBaseProperty() );
        choUnits        .valueProperty().bindBidirectional( backend.unitsProperty()     );

        txtTimeStep     .textProperty().bindBidirectional( backend.timeStepProperty(),   converter );
        txtVelocity     .textProperty().bindBidirectional( backend.velocityProperty(),   converter );
        txtAcceleration .textProperty().bindBidirectional( backend.accelProperty(),      converter );
        txtJerk         .textProperty().bindBidirectional( backend.jerkProperty(),       converter );
        txtWheelBaseW   .textProperty().bindBidirectional( backend.wheelBaseWProperty(), converter );
        txtWheelBaseD   .textProperty().bindBidirectional( backend.wheelBaseDProperty(), converter );

        // Disable WheelBaseD for Tank DriveBase
        backend.driveBaseProperty().addListener( ( o, oldValue, newValue ) ->
        {
            boolean dis = newValue == ProfileGenerator.DriveBase.TANK;
            lblWheelBaseD.disableProperty().setValue( dis );
            txtWheelBaseD.disableProperty().setValue( dis );
        });

    } /* setup() */


    /**************************************************************************
     *  initialize
     *      Setup gui stuff here.
     *************************************************************************/
    @FXML private void initialize()
    {
        // Populate drive base ChoiceBox
        choDriveBase.getItems().setAll( ProfileGenerator.DriveBase.values() );

        // Populate fit method ChoiceBox
        choFitMethod.getItems().setAll( ProfileGenerator.FitMethod.values() );

        // Populate units ChoiceBox
        choUnits.getItems().setAll( ProfileGenerator.Units.values() );


        // Formats number typed into TextFields
        UnaryOperator<TextFormatter.Change> filter = t ->
        {
            if( t.isReplaced() )
                // If new text isn't a digit
                if( t.getText().matches("[^0-9]") )
                {
                    // Keep original text
                    t.setText( t.getControlText().substring( t.getRangeStart(), t.getRangeEnd() ) );
                }

            if( t.isAdded() )
            {
                // If a period is already present.
                if ( t.getControlText().contains(".") )
                {
                    // If new text isn't a digit
                    if( t.getText().matches("[^0-9]") )
                    {
                        t.setText("");
                    }
                }
                // If we are adding a period to an empty field, prepend it with a zero.
                else if( ( t.getControlNewText().length() == 1         )
                      && ( t.getControlNewText().matches( "\\.") ) )
                {
                    t.setText("0.");
                    //t.setCaretPosition( 2 );
                }
                // If new text isn't a digit or a period
                else if ( t.getText().matches("[^0-9\\.]") )
                {
                    t.setText("");
                }
            }

            return t;
        };

        // Setup formatter to only allow doubles to be typed into these TextFields
        txtTimeStep     .setTextFormatter( new TextFormatter<>( filter ) );
        txtVelocity     .setTextFormatter( new TextFormatter<>( filter ) );
        txtAcceleration .setTextFormatter( new TextFormatter<>( filter ) );
        txtJerk         .setTextFormatter( new TextFormatter<>( filter ) );
        txtWheelBaseW   .setTextFormatter( new TextFormatter<>( filter ) );
        txtWheelBaseD   .setTextFormatter( new TextFormatter<>( filter ) );
    }
}

