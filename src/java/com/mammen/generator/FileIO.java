package com.mammen.generator;

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

        Path.Segment[] center = path.getCenter();
        Path.Segment[] frontLeft = path.getFrontLeft();
        Path.Segment[] frontRight = path.getFrontRight();
        Path.Segment[] backLeft = path.getBackLeft();
        Path.Segment[] backRight = path.getBackRight();

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


        // Loop over every segment in the path.
        for( int i = 0; i < path.getLength(); i++ )
        {
            // Write each element to the current line.
            for( Path.Elements e : elements )
            {
                switch( e )
                {
                    case POSITION:
                        writeElement( flPw, String.format( "%f,", path.getFrontLeftSegment( i ).position ) );
                        writeElement( frPw, String.format( "%f,", path.getFrontRightSegment( i ).position ) );
                        writeElement( blPw, String.format( "%f,", path.getBackLeftSegment( i ).position ) );
                        writeElement( brPw, String.format( "%f,", path.getBackRightSegment( i ).position ) );
                        break;

                    case X_POINT:
                        writeElement( flPw, String.format( "%f,", path.getFrontLeftSegment( i ).x ) );
                        writeElement( frPw, String.format( "%f,", path.getFrontRightSegment( i ).x ) );
                        writeElement( blPw, String.format( "%f,", path.getBackLeftSegment( i ).x ) );
                        writeElement( brPw, String.format( "%f,", path.getBackRightSegment( i ).x ) );
                        break;

                    case Y_POINT:
                        writeElement( flPw, String.format( "%f,", path.getFrontLeftSegment( i ).y ) );
                        writeElement( frPw, String.format( "%f,", path.getFrontRightSegment( i ).y ) );
                        writeElement( blPw, String.format( "%f,", path.getBackLeftSegment( i ).y ) );
                        writeElement( brPw, String.format( "%f,", path.getBackRightSegment( i ).y ) );
                        break;

                    case HEADING:
                        writeElement( flPw, String.format( "%f,", path.getFrontLeftSegment( i ).heading ) );
                        writeElement( frPw, String.format( "%f,", path.getFrontRightSegment( i ).heading ) );
                        writeElement( blPw, String.format( "%f,", path.getBackLeftSegment( i ).heading ) );
                        writeElement( flPw, String.format( "%f,", path.getBackRightSegment( i ).heading ) );
                        break;

                    case VELOCITY:

                        writeElement( flPw, String.format( "%f,", path.getFrontLeftSegment( i ).velocity ) );
                        writeElement( frPw, String.format( "%f,", path.getFrontRightSegment( i ).velocity ) );
                        writeElement( blPw, String.format( "%f,", path.getBackLeftSegment( i ).velocity ) );
                        writeElement( brPw, String.format( "%f,", path.getBackRightSegment( i ).velocity ) );
                        break;

                    case ACCELERATION:
                        writeElement( flPw, String.format( "%f,", path.getFrontLeftSegment( i ).acceleration ) );
                        writeElement( frPw, String.format( "%f,", path.getFrontRightSegment( i ).acceleration ) );
                        writeElement( blPw, String.format( "%f,", path.getBackLeftSegment( i ).acceleration ) );
                        writeElement( brPw, String.format( "%f,", path.getBackRightSegment( i ).acceleration ) );
                        break;

                    case JERK:
                        writeElement( flPw, String.format( "%f,", path.getFrontLeftSegment( i ).jerk ) );
                        writeElement( frPw, String.format( "%f,", path.getFrontRightSegment( i ).jerk ) );
                        writeElement( blPw, String.format( "%f,", path.getBackLeftSegment( i ).jerk ) );
                        writeElement( brPw, String.format( "%f,", path.getBackRightSegment( i ).jerk ) );
                        break;

                    case DELTA_TIME:
                        writeElement( flPw, String.format( "%d,", (int)( path.getFrontLeftSegment( i ).dt * 1000 ) ) );
                        writeElement( frPw, String.format( "%d,", (int)( path.getFrontRightSegment( i ).dt * 1000 ) ) );
                        writeElement( blPw, String.format( "%d,", (int)( path.getBackLeftSegment( i ).dt * 1000 ) ) );
                        writeElement( brPw, String.format( "%d,", (int)( path.getBackRightSegment( i ).dt * 1000 ) ) );
                        break;

                    default:
                        // Do nothing
                        break;
                }
            }

            // End of line
            writeElement( flPw, "\n" );
            writeElement( frPw, "\n" );
            writeElement( blPw, "\n" );
            writeElement( brPw, "\n" );

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

    private static void writeElement( PrintWriter writer, String element )
    {
        if( writer != null )
        {
            writer.print( element );
        }
    }

}
