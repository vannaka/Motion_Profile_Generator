package com.mammen.path;

import com.mammen.generator.generator_vars.DriveBase;

public class Path
{
    /**
     * Reresents Each var in a Segment
     */
    public enum Elements
    {
        DELTA_TIME( "Delta Time" ),
        X_POINT( "X Point" ),
        Y_POINT( "Y Point" ),
        POSITION( "Position" ),
        VELOCITY( "Velocity" ),
        ACCELERATION( "Acceleration" ),
        JERK( "Jerk" ),
        HEADING( "Heading" );

        private String label;

        Elements( String label )
        {
            this.label = label;
        }

        @Override
        public String toString()
        {
            return label;
        }

    }

    public static class Segment
    {
        public double dt;
        public double x;
        public double y;
        public double position;
        public double velocity;
        public double acceleration;
        public double jerk;
        public double heading;

        public Segment( double dt, double x, double y, double position, double velocity, double acceleration, double jerk, double heading )
        {
            this.dt = dt;
            this.x = x;
            this.y = y;
            this.position = position;
            this.velocity = velocity;
            this.acceleration = acceleration;
            this.jerk = jerk;
            this.heading = heading;
        }
    }

    // Generated paths
    private Segment[] center;
    private Segment[] frontLeft;
    private Segment[] frontRight;
    private Segment[] backLeft;
    private Segment[] backRight;

    private DriveBase driveBase;

    public Path( DriveBase driveBase, Segment[] left, Segment[] right )
    {
        this.driveBase = driveBase;
        this.frontLeft = left;
        this.frontRight = right;
    }

    public Path( DriveBase driveBase, Segment[] frontLeft, Segment[] frontRight, Segment[] backLeft, Segment[] backRight )
    {
        this( driveBase, frontLeft, frontRight );

        this.backLeft = backLeft;
        this.backRight = backRight;
    }

    public Path( DriveBase driveBase, Segment[] frontLeft, Segment[] frontRight, Segment[] backLeft, Segment[] backRight, Segment[] center )
    {
        this( driveBase, frontLeft, frontRight, backLeft, backRight );
        this.center = center;
    }

    // Getters and Setters
    public DriveBase getDriveBase()
    {
        return driveBase;
    }

    public Segment[] getCenter()
    {
        return center;
    }

    public Segment[] getFrontLeft()
    {
        return frontLeft;
    }

    public Segment[] getFrontRight()
    {
        return frontRight;
    }

    public Segment[] getBackLeft()
    {
        return backLeft;
    }

    public Segment[] getBackRight()
    {
        return backRight;
    }

    public int getLength()
    {
        return center.length;
    }

    public Segment getCenterSegment( int i )
    {
        return center[ i ];
    }

    public Segment getFrontLeftSegment( int i )
    {
        return frontLeft[ i ];
    }

    public Segment getFrontRightSegment( int i )
    {
        return frontRight[ i ];
    }

    public Segment getBackLeftSegment( int i )
    {
        return backLeft[ i ];
    }

    public Segment getBackRightSegment( int i )
    {
        return backRight[ i ];
    }

}
