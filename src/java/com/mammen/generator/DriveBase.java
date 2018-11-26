package com.mammen.generator;

// Types of drive bases
public enum DriveBase
{
    TANK( "Tank" ),
    SWERVE( "Swerve" );

    private String label;

    DriveBase( String label )
    {
        this.label = label;
    }

    public String toString()
    {
        return label;
    }
}
