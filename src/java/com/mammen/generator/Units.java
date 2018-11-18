package com.mammen.generator;

// Units of every value
public enum Units
{
    FEET( "FEET", "Feet" ),
    INCHES( "INCHES", "Inches" ),
    METERS( "METERS", "Meter" );

    private String label;
    private String internalLabel;

    Units( String internalLabel, String label )
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