package com.mammen.generator;

import com.mammen.path.Path;
import com.mammen.path.Waypoint;

import java.util.List;

@FunctionalInterface
public interface Generator
{
    enum Type
    {
        PATHFINDER_V1( "Pathfinder Version 1" );

        private String label;

        Type(String label )
        {
            this.label = label;
        }

        @Override
        public String toString()
        {
            return label;
        }
    }

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

    Path generate( List<Waypoint> waypointList ) throws PathGenerationException, NotEnoughPointsException;

}