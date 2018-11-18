package com.mammen.generator;

// Types of drive bases
public enum DriveBase
{
    TANK( "TANK", "Tank" ),
    SWERVE( "SWERVE", "Swerve" );

    private String label;
    private String internalLabel;

    DriveBase( String internalLabel, String label )
    {
        this.internalLabel = internalLabel;
        this.label = label;
    }

    public String toString()
    {
        return label;
    }

    public String getInternalLabel()
    {
        return internalLabel;
    }
}
