package com.mammen.generator;

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

    public String toString()
    {
        return label;
    }

}