package com.mammen.generator;

import jaci.pathfinder.Waypoint;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class WaypointInternal
{
    private DoubleProperty x;
    private DoubleProperty y;
    private DoubleProperty angle;

    public WaypointInternal( double x, double y, double angle )
    {
        this.x = new SimpleDoubleProperty( x );
        this.y = new SimpleDoubleProperty( y );
        this.angle = new SimpleDoubleProperty( angle );
    }

    public Waypoint getWaypoint()
    {
        return new Waypoint( x.get(), y.get(), angle.get() );
    }

    public double getX()
    {
        return x.get();
    }

    public void setX( double x )
    {
        this.x.set( x );
    }

    public DoubleProperty xProperty()
    {
        return x;
    }

    public double getY()
    {
        return y.get();
    }

    public void setY( double y )
    {
        this.y.set( y );
    }

    public DoubleProperty yProperty()
    {
        return y;
    }

    public double getAngle()
    {
        return angle.get();
    }

    public void setAngle( double angle )
    {
        this.angle.set( angle );
    }

    public DoubleProperty angleProperty()
    {
        return angle;
    }
}
