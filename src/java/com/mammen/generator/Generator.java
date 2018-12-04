package com.mammen.generator;

import com.mammen.settings.generator_vars.GeneratorVars;
import com.mammen.path.Path;
import com.mammen.path.Waypoint;

import java.util.List;

@FunctionalInterface
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

    Path generate( List<Waypoint> waypointList, GeneratorVars vars ) throws PathGenerationException, NotEnoughPointsException;
}