package com.mammen.generator.variables;

import com.mammen.generator.DriveBase;
import com.mammen.generator.Units;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import org.w3c.dom.Element;

public abstract class GeneratorVars
{
    // Shared vars
    Property<DriveBase> driveBase   = new SimpleObjectProperty<>( DriveBase.TANK );
    Property<Units> unit            = new SimpleObjectProperty<>( Units.FEET );

    public abstract void writeXMLAttributes( Element element );
    public abstract  void readXMLAttributes( Element element );

    public abstract void setDefaultValues( Units newUnits );
    public abstract void changeUnit( Units newUnits );


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

    public void setUnit(Units unit)
    {
        this.unit.setValue( unit );
    }
}
