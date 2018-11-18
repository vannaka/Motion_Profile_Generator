package com.mammen.generator.wrappers;

import com.mammen.generator.DriveBase;

public class Path
{
    /**
     * Reresents Each var in a Segment
     */
    public enum Elements
    {
        DELTA_TIME( "DELTA_TIME", "Delta Time", 1 ),
        X_POINT( "X_POINT", "X Point", 2 ),
        Y_POINT( "Y_POINT", "Y Point", 3 ),
        POSITION( "POSITION", "Position", 4 ),
        VELOCITY( "VELOCITY", "Velocity", 5 ),
        ACCELERATION( "ACCELERATION", "Acceleration", 6 ),
        JERK( "JERK", "Jerk", 7 ),
        HEADING( "HEADING", "Heading", 8 ),
        NULL(null, null, 0);

        private String internalLabel;
        private String label;
        private int index;

        Elements( String internalLabel, String label, int index )
        {
            this.internalLabel = internalLabel;
            this.label = label;
            this.index = index;
        }

        public int getIndex() {return index;}

        public String toString()
        {
            return label;
        }

        public String getInternalLabel()
        {
            return internalLabel;
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

}
