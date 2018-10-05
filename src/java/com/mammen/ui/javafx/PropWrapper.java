package com.mammen.ui.javafx;

import java.io.*;
import java.util.Properties;

public class PropWrapper {
    private static Properties propInstance;
    private static File propFile;

    private static final String PROP_NAME = "mpg";
    private static final String DIR_NAME = "motion-profile-generator";
    private static final File APPDATA_DIR = new File(System.getProperty("user.home") + File.separator + "." + DIR_NAME);

    public static Properties getProperties()
    {
        if( propInstance == null )
        {
            try
            {
                propInstance = new Properties();
                propFile = new File( APPDATA_DIR, PROP_NAME + ".properties" );

                if( !propFile.exists() )
                {
                    APPDATA_DIR.mkdirs();

                    propFile.createNewFile();

                    propInstance.put("ui.overlayImg", "");
                    propInstance.put("ui.sourceDisplay", "2");
                    propInstance.put("ui.csvType", "0");
                    propInstance.put("ui.addWaypointOnClick", "false");
                    propInstance.put("csv.avail", "Delta Time, X Point, Y Point, Position, Velocity, Acceleration, Jerk, Heading");
                    propInstance.put("csv.chos", "null");
                }

                propInstance.load( new FileInputStream( propFile ) );
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        return propInstance;
    }

    public static void storeProperties() throws IOException
    {
        propInstance.store( new FileOutputStream( propFile ), "Properties" );
    }
}