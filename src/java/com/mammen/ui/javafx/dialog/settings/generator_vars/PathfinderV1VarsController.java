package com.mammen.ui.javafx.dialog.settings.generator_vars;

import com.mammen.generator.generator_vars.SharedGeneratorVars;
import com.mammen.generator.generator_vars.DriveBase;
import com.mammen.generator.generator_vars.Units;
import com.mammen.generator.generator_vars.PfV1GeneratorVars;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;

import java.util.function.UnaryOperator;

public class PathfinderV1VarsController
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
    private ChoiceBox<PfV1GeneratorVars.FitMethod> choFitMethod;

    @FXML
    private ChoiceBox<DriveBase> choDriveBase;

    @FXML
    private ChoiceBox<Units> choUnits;

    private PfV1GeneratorVars vars;
    private SharedGeneratorVars sharedVars;


    /**************************************************************************
     *  initialize
     *      Setup gui stuff here.
     *************************************************************************/
    @FXML private void initialize()
    {
        sharedVars = SharedGeneratorVars.getInstance();
        vars = PfV1GeneratorVars.getInstance();

        // Populate ChoiceBox's
        choDriveBase.getItems().setAll( DriveBase.values() );
        choFitMethod.getItems().setAll( PfV1GeneratorVars.FitMethod.values() );
        choUnits.getItems().setAll( Units.values() );

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


        // Converts formatted string in TextField to format of bounded property.
        StringConverter<Number> converter = new NumberStringConverter();

        // Setup Bindings
        choFitMethod    .valueProperty().bindBidirectional( vars.fitMethodProperty() );
        choDriveBase    .valueProperty().bindBidirectional( sharedVars.driveBaseProperty() );
        choUnits        .valueProperty().bindBidirectional( sharedVars.unitProperty()      );

        txtTimeStep     .textProperty().bindBidirectional( sharedVars.timeStepProperty(),   converter );
        txtWheelBaseW   .textProperty().bindBidirectional( sharedVars.wheelBaseWProperty(), converter );
        txtWheelBaseD   .textProperty().bindBidirectional( sharedVars.wheelBaseDProperty(), converter );
        txtVelocity     .textProperty().bindBidirectional( vars.velocityProperty(),   converter );
        txtAcceleration .textProperty().bindBidirectional( vars.accelProperty(),      converter );
        txtJerk         .textProperty().bindBidirectional( vars.jerkProperty(),       converter );


        // Disable WheelBaseD for Tank DriveBase
        sharedVars.driveBaseProperty().addListener( ( o, oldValue, newValue ) ->
        {
            boolean dis = newValue == DriveBase.TANK;
            lblWheelBaseD.disableProperty().setValue( dis );
            txtWheelBaseD.disableProperty().setValue( dis );
        });

    }
}

