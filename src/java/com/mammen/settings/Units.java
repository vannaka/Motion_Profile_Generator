package com.mammen.settings;

// Units of every value
public enum Units
{
    FEET( "Feet" ),
    INCHES( "Inches" ),
    METERS( "Meter" );

    private String label;

    Units( String label )
    {
        this.label = label;
    }

    @Override
    public String toString()
    {
        return label;
    }

}