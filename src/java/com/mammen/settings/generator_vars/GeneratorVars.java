package com.mammen.settings.generator_vars;

import com.mammen.settings.DriveBase;
import com.mammen.settings.Units;
import com.mammen.util.SerializeHelpers.ReadObjectsHelper;
import com.mammen.util.SerializeHelpers.WriteObjectsHelper;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.w3c.dom.Element;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public abstract class GeneratorVars
{
    // Shared vars
    DoubleProperty timeStep         = new SimpleDoubleProperty( 0.05 );
    Property<DriveBase> driveBase   = new SimpleObjectProperty<>( DriveBase.TANK );
    Property<Units> unit            = new SimpleObjectProperty<>( Units.FEET );
    DoubleProperty wheelBaseW       = new SimpleDoubleProperty( 1.5 );
    DoubleProperty wheelBaseD       = new SimpleDoubleProperty( 2.0 );

    GeneratorVars()
    {
        unit.addListener( (o, oldValue, newValue) ->
        {
            changeUnit( oldValue, newValue );
        });
    }

    public abstract void writeXMLAttributes( Element element );
    public abstract  void readXMLAttributes( Element element );

    public abstract void setDefaultValues( Units newUnits );
    protected abstract void changeUnit( Units oldUnit, Units newUnit );


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
}
