package com.mammen.settings;

public enum SourcePathDisplayType
{
    NONE( "None" ),
    WP_ONLY( "Waypoints only" ),
    WP_PLUS_PATH( "Waypoints + Source" );

    private String label;

    SourcePathDisplayType( String label )
    {
        this.label = label;
    }

    @Override
    public String toString()
    {
        return label;
    }
}
