package com.mammen.file_io;

import com.mammen.generator.generator_vars.DriveBase;
import com.mammen.path.Path;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

public final class FileIO
{

    public static void savePath( Path path, File savePathName, List<Path.Elements> elements ) throws FileNotFoundException
    {
        File dir = savePathName.getParentFile();

        // Create dir if it does not exist yet
        if( dir != null && !dir.exists() && dir.isDirectory() )
        {
            if( !dir.mkdirs() )
                return;
        }

        DriveBase driveBase = path.getDriveBase();

        PrintWriter flPw;
        PrintWriter frPw;
        PrintWriter blPw = null;
        PrintWriter brPw = null;

        if( driveBase == DriveBase.TANK )
        {
            flPw = new PrintWriter( new File(savePathName + "_left.csv" ) );
            frPw = new PrintWriter( new File(savePathName + "_right.csv" ) );
        }
        else // driveBase == DriveBase.SWERVE
        {
            flPw = new PrintWriter( new File(savePathName + "_frontLeft.csv" ) );
            frPw = new PrintWriter( new File(savePathName + "_frontRight.csv" ) );
            blPw = new PrintWriter( new File(savePathName + "_backLeft.csv" ) );
            brPw = new PrintWriter( new File(savePathName + "_backRight.csv" ) );
        }

        // Label each column in the csv
        for( Path.Elements e : elements )
        {
            flPw.print( e.toString() + ", " );
        }
        flPw.println();


        // Loop over every segment in the path.
        for( int i = 0; i < path.getLength(); i++ )
        {
            // Write each element to the current line.
            for( Path.Elements e : elements )
            {
                switch( e )
                {
                    case POSITION:
                        flPw.print( String.format( "%f, ", path.getFrontLeftSegment( i ).position ) );
                        frPw.print( String.format( "%f, ", path.getFrontRightSegment( i ).position ) );
                        if( blPw == null ) break;
                        blPw.print( String.format( "%f, ", path.getBackLeftSegment( i ).position ) );
                        brPw.print( String.format( "%f, ", path.getBackRightSegment( i ).position ) );
                        break;

                    case X_POINT:
                        flPw.print( String.format( "%f, ", path.getFrontLeftSegment( i ).x ) );
                        frPw.print( String.format( "%f, ", path.getFrontRightSegment( i ).x ) );
                        if( blPw == null ) break;
                        blPw.print( String.format( "%f, ", path.getBackLeftSegment( i ).x ) );
                        brPw.print( String.format( "%f, ", path.getBackRightSegment( i ).x ) );
                        break;

                    case Y_POINT:
                        flPw.print( String.format( "%f, ", path.getFrontLeftSegment( i ).y ) );
                        frPw.print( String.format( "%f, ", path.getFrontRightSegment( i ).y ) );
                        if( blPw == null ) break;
                        blPw.print( String.format( "%f, ", path.getBackLeftSegment( i ).y ) );
                        brPw.print( String.format( "%f, ", path.getBackRightSegment( i ).y ) );
                        break;

                    case HEADING:
                        flPw.print( String.format( "%f, ", path.getFrontLeftSegment( i ).heading ) );
                        frPw.print( String.format( "%f, ", path.getFrontRightSegment( i ).heading ) );
                        if( blPw == null ) break;
                        blPw.print( String.format( "%f, ", path.getBackLeftSegment( i ).heading ) );
                        flPw.print( String.format( "%f, ", path.getBackRightSegment( i ).heading ) );
                        break;

                    case VELOCITY:

                        flPw.print( String.format( "%f, ", path.getFrontLeftSegment( i ).velocity ) );
                        frPw.print( String.format( "%f, ", path.getFrontRightSegment( i ).velocity ) );
                        if( blPw == null ) break;
                        blPw.print( String.format( "%f, ", path.getBackLeftSegment( i ).velocity ) );
                        brPw.print( String.format( "%f, ", path.getBackRightSegment( i ).velocity ) );
                        break;

                    case ACCELERATION:
                        flPw.print( String.format( "%f, ", path.getFrontLeftSegment( i ).acceleration ) );
                        frPw.print( String.format( "%f, ", path.getFrontRightSegment( i ).acceleration ) );
                        if( blPw == null ) break;
                        blPw.print( String.format( "%f, ", path.getBackLeftSegment( i ).acceleration ) );
                        brPw.print( String.format( "%f, ", path.getBackRightSegment( i ).acceleration ) );
                        break;

                    case JERK:
                        flPw.print( String.format( "%f, ", path.getFrontLeftSegment( i ).jerk ) );
                        frPw.print( String.format( "%f, ", path.getFrontRightSegment( i ).jerk ) );
                        if( blPw == null ) break;
                        blPw.print( String.format( "%f, ", path.getBackLeftSegment( i ).jerk ) );
                        brPw.print( String.format( "%f, ", path.getBackRightSegment( i ).jerk ) );
                        break;

                    case DELTA_TIME:
                        flPw.print( String.format( "%d, ", (int)( path.getFrontLeftSegment( i ).dt * 1000 ) ) );
                        frPw.print( String.format( "%d, ", (int)( path.getFrontRightSegment( i ).dt * 1000 ) ) );
                        if( blPw == null ) break;
                        blPw.print( String.format( "%d, ", (int)( path.getBackLeftSegment( i ).dt * 1000 ) ) );
                        brPw.print( String.format( "%d, ", (int)( path.getBackRightSegment( i ).dt * 1000 ) ) );
                        break;

                    default:
                        // Do nothing
                        break;
                }
            }

            // End of line
            flPw.println();
            frPw.println();
            if( blPw != null )
            {
                blPw.println();
                brPw.println();
            }
        }

        // Close the files
        flPw.close();
        frPw.close();
        if( blPw != null )
        {
            blPw.close();
            brPw.close();
        }
    }
}
