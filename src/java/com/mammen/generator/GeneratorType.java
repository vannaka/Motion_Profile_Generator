package com.mammen.generator;

public enum GeneratorType
{
    PATHFINDER_V1( "Pathfinder Version 1" );

    private String label;

    GeneratorType( String label )
    {
        this.label = label;
    }

    @Override
    public String toString()
    {
        return label;
    }
}
