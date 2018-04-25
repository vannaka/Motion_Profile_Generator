package com.mammen.main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Trajectory.Config;
import jaci.pathfinder.Trajectory.FitMethod;
import jaci.pathfinder.Waypoint;
import jaci.pathfinder.modifiers.SwerveModifier;
import jaci.pathfinder.modifiers.TankModifier;


public class ProfileGenerator 
{
	public static final String PROJECT_EXTENSION = "xml";
	
	public enum DriveBase {
        TANK("TANK"),
        SWERVE("SWERVE");
        
        private String label;

		DriveBase(String label)
		{
            this.label = label;
        }

        public String toString()
        {
            return label;
        }
    }

    public enum Units {
        IMPERIAL("IMPERIAL"),
        METRIC("METRIC");
        
        private String label;
        
        Units(String label)
        {
        	this.label = label;
        }
        
        public String toString()
        {
            return label;
        }
    }
    
    private double timeStep;
    private double velocity;
    private double acceleration;
    private double jerk;
    private double wheelBaseW;
    private double wheelBaseD;
    
    private DriveBase driveBase;
    private FitMethod fitMethod;
    private Units units;
    
    private final List<Waypoint> POINTS;
    
    // Trajectories for both bases
    // Use front-left and front-right for tank drive L and R
    private Trajectory fl;
    private Trajectory fr;
    private Trajectory bl;
    private Trajectory br;
    
    // Source trajectory
    // i.e. the center trajectory
    private Trajectory source;
    
    // File stuff
    private DocumentBuilderFactory dbFactory;
    private File workingProject;
    
    public ProfileGenerator()
    {
    	POINTS = new ArrayList<>();
    	dbFactory = DocumentBuilderFactory.newInstance();
    	resetValues();
    }
    
    /**
     * Exports all trajectories to the parent folder, with the given root name and file extension.
     *
     * @param parentPath the absolute file path to save to, excluding file extension
     * @param ext        the file extension to save to, can be {@code *.csv} or {@code *.traj}
     * @throws Pathfinder.GenerationException
     */
    public void exportTrajectories(File parentPath, String ext) throws Pathfinder.GenerationException {
        updateTrajectories();

        File dir = parentPath.getParentFile();

        if (dir != null && !dir.exists() && dir.isDirectory()) {
            if (!dir.mkdirs())
                return;
        }

        switch (ext) {
            case ".csv":
                Pathfinder.writeToCSV(new File(parentPath + "_source_detailed.csv"), source);

                if (driveBase == DriveBase.SWERVE) {
                    Pathfinder.writeToCSV(new File(parentPath + "_fl_detailed.csv"), fl);
                    Pathfinder.writeToCSV(new File(parentPath + "_fr_detailed.csv"), fr);
                    Pathfinder.writeToCSV(new File(parentPath + "_bl_detailed.csv"), bl);
                    Pathfinder.writeToCSV(new File(parentPath + "_br_detailed.csv"), br);
                } else {
                    Pathfinder.writeToCSV(new File(parentPath + "_left_detailed.csv"), fl);
                    Pathfinder.writeToCSV(new File(parentPath + "_right_detailed.csv"), fr);
                }
            break;
            case ".traj":
                Pathfinder.writeToFile(new File(parentPath + "_source_detailed.traj"), source);

                if (driveBase == DriveBase.SWERVE) {
                    Pathfinder.writeToFile(new File(parentPath + "_fl_detailed.traj"), fl);
                    Pathfinder.writeToFile(new File(parentPath + "_fr_detailed.traj"), fr);
                    Pathfinder.writeToFile(new File(parentPath + "_bl_detailed.traj"), bl);
                    Pathfinder.writeToFile(new File(parentPath + "_br_detailed.traj"), br);
                } else {
                    Pathfinder.writeToFile(new File(parentPath + "_left_detailed.traj"), fl);
                    Pathfinder.writeToFile(new File(parentPath + "_right_detailed.traj"), fr);
                }
            break;
            default:
                throw new IllegalArgumentException("Invalid file extension");
        }
    }
    
    /**
     * Resets configuration to default values
     */
    public void resetValues() 
    {
        timeStep = 0.05;
        velocity = 4;
        acceleration = 3;
        jerk = 60;
        wheelBaseW = 1.464;
        wheelBaseD = 0;

        fitMethod = FitMethod.HERMITE_CUBIC;
        driveBase = DriveBase.TANK;
        units = Units.IMPERIAL;
    }
    
    /**
     * Adds a waypoint to the list of waypoints
     *
     * @param x     the x-location of the waypoint
     * @param y     the y-location of the waypoint
     * @param angle the angle of direction at the point, in radians
     */
    public void addPoint( double x, double y, double angle ) 
    {
        POINTS.add(new Waypoint(x, y, angle));
    }
    
    /**
     * Adds a waypoint to the list of waypoints
     *
     * @param index the index of the waypoint to edit
     * @param x     the x-location of the waypoint
     * @param y     the y-location of the waypoint
     * @param angle the angle of direction at the point, in radians
     */
    public void editWaypoint( int index, double x, double y, double angle )
    {
        POINTS.get(index).x = x;
        POINTS.get(index).y = y;
        POINTS.get(index).angle = angle;
    }
    
    public void removePoint(int index) {
        POINTS.remove(index);
    }

    public int getNumWaypoints()
    {
        return POINTS.size();
    }

    /**
     * Clears all the existing waypoints in the list.
     * This also clears all trajectories generated by the waypoints.
     */
    public void clearPoints() 
    {
        POINTS.clear();

        fl = null;
        fr = null;
        bl = null;
        br = null;
    }
    
    /**
     * Updates the trajectories
     */
    public void updateTrajectories() throws Pathfinder.GenerationException 
    {
        Config config = new Config( fitMethod, Config.SAMPLES_HIGH, timeStep, velocity, acceleration, jerk );
        source = Pathfinder.generate( POINTS.toArray(new Waypoint[1]), config );

        if (driveBase == DriveBase.SWERVE) 
        {
            SwerveModifier swerve = new SwerveModifier(source);

            // There is literally no other swerve mode other than the default can someone please explain this to me
            swerve.modify( wheelBaseW, wheelBaseD, SwerveModifier.Mode.SWERVE_DEFAULT );

            fl = swerve.getFrontLeftTrajectory();
            fr = swerve.getFrontRightTrajectory();
            bl = swerve.getBackLeftTrajectory();
            br = swerve.getBackRightTrajectory();
        } 
        else  // By default, treat everything as tank drive.
        {
            TankModifier tank = new TankModifier(source);
            tank.modify(wheelBaseW);

            fl = tank.getLeftTrajectory();
            fr = tank.getRightTrajectory();
            bl = null;
            br = null;
        }
    }
    
    
    public double getTimeStep()
    {
        return timeStep;
    }

    public void setTimeStep(double timeStep)
    {
        this.timeStep = timeStep;
    }

    public double getVelocity() 
    {
        return velocity;
    }

    public void setVelocity(double velocity)
    {
        this.velocity = velocity;
    }

    public double getAcceleration() 
    {
        return acceleration;
    }

    public void setAcceleration(double acceleration) 
    {
        this.acceleration = acceleration;
    }

    public DriveBase getDriveBase()
    {
        return driveBase;
    }

    public void setDriveBase(DriveBase driveBase)
    {
        this.driveBase = driveBase;
    }

    public FitMethod getFitMethod()
    {
        return fitMethod;
    }

    public void setFitMethod(FitMethod fitMethod)
{
        this.fitMethod = fitMethod;
    }

    public Units getUnits()
    {
        return units;
    }

    public void setUnits(Units units)
    {
        this.units = units;
    }

    public double getJerk()
    {
        return jerk;
    }

    public void setJerk(double jerk) 
    {
        this.jerk = jerk;
    }

    public double getWheelBaseW() 
    {
        return wheelBaseW;
    }

    public void setWheelBaseW(double wheelBaseW)
    {
        this.wheelBaseW = wheelBaseW;
    }

    public double getWheelBaseD() {
        return wheelBaseD;
    }

    public void setWheelBaseD(double wheelBaseD)
    {
        this.wheelBaseD = wheelBaseD;
    }

    public List<Waypoint> getWaypointsList()
    {
        return POINTS;
    }

    public Trajectory getSourceTrajectory()
    {
        return source;
    }

    public Trajectory getFrontLeftTrajectory() 
    {
        return fl;
    }

    public Trajectory getFrontRightTrajectory() 
    {
        return fr;
    }

    public Trajectory getBackLeftTrajectory() 
    {
        return bl;
    }

    public Trajectory getBackRightTrajectory() 
    {
        return br;
    }
}










