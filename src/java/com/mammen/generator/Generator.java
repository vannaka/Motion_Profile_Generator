package com.mammen.generator;

import com.mammen.generator.wrappers.GeneratorVars;
import com.mammen.generator.wrappers.Path;
import com.mammen.generator.wrappers.Waypoint;

import java.util.List;

public interface Generator
{
    class PathGenerationException extends Exception
    {
        PathGenerationException( String message )
        {
            super( message );
        }
    }

    class NotEnoughPointsException extends Exception
    {
        NotEnoughPointsException( String message )
        {
            super( message );
        }
    }

    Path generate(List<Waypoint> waypointList, GeneratorVars vars ) throws PathGenerationException, NotEnoughPointsException;
}