package com.mammen.generator.wrappers;

import com.mammen.generator.DriveBase;
import com.mammen.generator.Units;
import javafx.beans.property.Property;

public interface GeneratorVars
{
    void setDefaultValues( Units newUnits );
    void changeUnit( Units newUnits );

    DriveBase getDriveBase();
    void setDriveBase( DriveBase driveBase );
    Property<DriveBase> driveBaseProperty();

    Units getUnit();
    void setUnit( Units unit );
    Property<Units> unitProperty();
}
