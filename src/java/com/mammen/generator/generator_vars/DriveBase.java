package com.mammen.generator.generator_vars;

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

    @Override
    public String toString()
    {
        return label;
    }
}
