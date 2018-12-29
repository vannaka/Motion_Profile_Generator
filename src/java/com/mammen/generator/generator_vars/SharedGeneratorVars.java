package com.mammen.generator.generator_vars;

import com.mammen.util.Mathf;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.w3c.dom.Element;

public class SharedGeneratorVars implements GeneratorVars
{
    private static SharedGeneratorVars sharedGeneratorVars = null;

    // Shared vars
    private DoubleProperty timeStep         = new SimpleDoubleProperty( 0.05 );
    private Property<DriveBase> driveBase   = new SimpleObjectProperty<>( DriveBase.TANK );
    private Property<Units> unit            = new SimpleObjectProperty<>( Units.FEET );
    private DoubleProperty wheelBaseW       = new SimpleDoubleProperty( 1.5 );
    private DoubleProperty wheelBaseD       = new SimpleDoubleProperty( 2.0 );


    /**************************************************************************
     *   Constructor
     *************************************************************************/
    private SharedGeneratorVars()
    {
        unit.addListener( (o, oldValue, newValue) ->
        {
            changeUnit( oldValue, newValue );
        });
    }


    /**************************************************************************
     * <p>Returns the SharedGeneratorVars model.</p>
     *
     * @return The one and only instance of the SharedGeneratorVars model.
     *************************************************************************/
    public static SharedGeneratorVars getInstance()
    {
        if( sharedGeneratorVars == null )
        {
            sharedGeneratorVars = new SharedGeneratorVars();
        }

        return sharedGeneratorVars;
    }

    // getters and setters
    public double getTimeStep()
    {
        return timeStep.get();
    }

    public DoubleProperty timeStepProperty()
    {
        return timeStep;
    }

    public void setTimeStep( double timeStep )
    {
        this.timeStep.set( timeStep );
    }

    public DriveBase getDriveBase()
    {
        return driveBase.getValue();
    }

    public Property<DriveBase> driveBaseProperty()
    {
        return driveBase;
    }

    public void setDriveBase( DriveBase driveBase )
    {
        this.driveBase.setValue( driveBase );
    }

    public Units getUnit()
    {
        return unit.getValue();
    }

    public Property<Units> unitProperty()
    {
        return unit;
    }

    public void setUnit( Units unit )
    {
        this.unit.setValue( unit );
    }

    public double getWheelBaseW()
    {
        return wheelBaseW.get();
    }

    public DoubleProperty wheelBaseWProperty()
    {
        return wheelBaseW;
    }

    public void setWheelBaseW( double wheelBaseW )
    {
        this.wheelBaseW.set( wheelBaseW );
    }

    public double getWheelBaseD()
    {
        return wheelBaseD.get();
    }

    public DoubleProperty wheelBaseDProperty()
    {
        return wheelBaseD;
    }

    public void setWheelBaseD( double wheelBaseD )
    {
        this.wheelBaseD.set( wheelBaseD );
    }

    @Override
    public void writeXMLAttributes( Element element )
    {
        element.setAttribute("unit",            "" + unit.getValue().name()      );
        element.setAttribute("driveBase",       "" + driveBase.getValue().name() );
        element.setAttribute("dt",              "" + timeStep.getValue()         );
        element.setAttribute("wheelBaseW",      "" + wheelBaseW.getValue()       );
        element.setAttribute("wheelBaseD",      "" + wheelBaseD.getValue()       );
    }

    @Override
    public void readXMLAttributes( Element element )
    {
        unit        .setValue( Units    .valueOf( element.getAttribute("unit"        ) ) );
        driveBase   .setValue( DriveBase.valueOf( element.getAttribute("driveBase"   ) ) );
        timeStep    .set( Double.parseDouble( element.getAttribute("dt"              ) ) );
        wheelBaseW  .set( Double.parseDouble( element.getAttribute("wheelBaseW"      ) ) );
        wheelBaseD  .set( Double.parseDouble( element.getAttribute("wheelBaseD"      ) ) );
    }

    @Override
    public void setDefaultValues()
    {
        driveBase.setValue( DriveBase.TANK );

        switch( unit.getValue() )
        {
            case FEET:
                timeStep.set( 0.05 );
                wheelBaseW.set( 1.5 );
                wheelBaseD.set( 2.0 );
                break;

            case METERS:
                timeStep.set( 0.05 );
                wheelBaseW.set( 0.4462272 );
                wheelBaseD.set( 0.4462272 );
                break;

            case INCHES:
                timeStep.set( 0.05 );
                wheelBaseW.set( 17.568 );
                wheelBaseD.set( 17.568 );
                break;
        }
    }

    @Override
    public void changeUnit( Units oldUnit, Units newUnit )
    {
        // Convert each MP variable to the new unit
        double tmp_WBW = 0, tmp_WBD = 0, tmp_vel = 0, tmp_acc = 0, tmp_jer = 0;

        // convert to intermediate unit of feet
        switch( oldUnit )
        {
            case FEET:
                tmp_WBW = wheelBaseW.get();
                tmp_WBD = wheelBaseD.get();
                break;

            case INCHES:
                tmp_WBW = Mathf.inchesToFeet( wheelBaseW.get() );
                tmp_WBD = Mathf.inchesToFeet( wheelBaseD.get() );
                break;

            case METERS:
                tmp_WBW = Mathf.meterToFeet( wheelBaseW.get() );
                tmp_WBD = Mathf.meterToFeet( wheelBaseD.get() );
                break;
        }

        // convert from intermediate unit of feet
        switch( newUnit )
        {
            case FEET:
                wheelBaseW  .set( tmp_WBW );
                wheelBaseD  .set( tmp_WBD );
                break;

            case INCHES:
                wheelBaseW  .set( Mathf.round( Mathf.feetToInches( tmp_WBW ),4 ) );
                wheelBaseD  .set( Mathf.round( Mathf.feetToInches( tmp_WBD ),4 ) );

                break;

            case METERS:
                wheelBaseW  .set( Mathf.round( Mathf.feetToMeter( tmp_WBW ),4 ) );
                wheelBaseD  .set( Mathf.round( Mathf.feetToMeter( tmp_WBD ),4 ) );
                break;
        }
    }
}
