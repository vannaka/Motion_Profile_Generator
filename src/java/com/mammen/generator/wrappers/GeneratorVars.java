package com.mammen.generator.wrappers;

import com.mammen.generator.DriveBase;
import com.mammen.generator.Units;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;

public interface GeneratorVars
{
    void setDefaultValues( Units newUnits );

    DriveBase getDriveBase();
    void setDriveBase( DriveBase driveBase );
    Property<DriveBase> driveBaseProperty();

    Units getUnits();
    void setUnits( Units units );
    Property<Units> unitsProperty();
}
