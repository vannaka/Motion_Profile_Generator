package com.mammen.generator;

import com.mammen.generator.variables.GeneratorVars;
import com.mammen.generator.variables.PfV1GeneratorVars;
import com.mammen.path.Path;
import com.mammen.path.Waypoint;
import com.mammen.ui.javafx.dialog.factory.AlertFactory;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.modifiers.SwerveModifier;
import jaci.pathfinder.modifiers.TankModifier;
import javafx.scene.control.Alert;
import org.scijava.nativelib.NativeLoader;

import java.io.IOException;
import java.util.List;

public class PfV1Generator implements Generator
{
    public PfV1Generator()
    {
        // Load Pathfinder native lib
        try
        {
            NativeLoader.loadLibrary("pathfinderjava" );
        }
        catch( IOException e )
        {
            e.printStackTrace();
            Alert alert = AlertFactory.createExceptionAlert(e, "Failed to load Pathfinder lib!" );

            alert.showAndWait();
        }
    }

    public Path generate(List<Waypoint> waypointList, GeneratorVars variables  ) throws PathGenerationException, NotEnoughPointsException
    {
        PfV1GeneratorVars vars = (PfV1GeneratorVars)variables;
        Trajectory source, fr, fl, br, bl;

        // We need at least 2 points to generate a trajectory.
        if( waypointList.size() > 1 )
        {
            Trajectory.Config config = new Trajectory.Config( vars.getFitMethod().pfFitMethod(), Trajectory.Config.SAMPLES_HIGH, vars.getTimeStep(), vars.getVelocity(), vars.getAccel(), vars.getJerk() );

            try
            {
                source = Pathfinder.generate( wp2pf( waypointList ), config );
            }
            catch( Exception e )
            {
                throw new PathGenerationException( "Pathfinder V1 failed to generate the path." );
            }

            if( vars.getDriveBase() == DriveBase.SWERVE )
            {
                SwerveModifier swerve = new SwerveModifier( source );
                swerve.modify( vars.getWheelBaseW(), vars.getWheelBaseD(), SwerveModifier.Mode.SWERVE_DEFAULT );

                fr = swerve.getFrontRightTrajectory();
                fl = swerve.getFrontLeftTrajectory();
                br = swerve.getBackRightTrajectory();
                bl = swerve.getBackLeftTrajectory();
            }
            else  // DriveBase.Tank
            {
                TankModifier tank = new TankModifier( source );
                tank.modify( vars.getWheelBaseW() );

                fr = tank.getRightTrajectory();
                fl = tank.getLeftTrajectory();
                br = null;
                bl = null;
            }

            return new Path( vars.getDriveBase(), traj2Path( fl ), traj2Path( fr ), traj2Path( bl ), traj2Path( br ), traj2Path( source ) );
        }

        throw new NotEnoughPointsException( "There are not enough points to generate a Path." );
    }

    private static jaci.pathfinder.Waypoint[] wp2pf( List<Waypoint> wpList )
    {
        jaci.pathfinder.Waypoint[] wpArray = new jaci.pathfinder.Waypoint[ wpList.size() ];

        for( int i = 0; i < wpList.size(); i++ )
        {

            wpArray[ i ] = new jaci.pathfinder.Waypoint( wpList.get( i ).getX(), wpList.get( i ).getY(), wpList.get( i ).getAngle() );
        }

        return wpArray;
    }

    private static Path.Segment[] traj2Path(Trajectory traj )
    {
        if( traj == null )
            return null;

        Path.Segment[] segments = new Path.Segment[ traj.length() ];

        for( int i = 0; i < traj.length(); i++ )
        {
            segments[ i ] = new Path.Segment(
                    traj.segments[ i ].dt,
                    traj.segments[ i ].x,
                    traj.segments[ i ].y,
                    traj.segments[ i ].position,
                    traj.segments[ i ].velocity,
                    traj.segments[ i ].acceleration,
                    traj.segments[ i ].jerk,
                    traj.segments[ i ].heading
            );
        }

        return segments;
    }

}
