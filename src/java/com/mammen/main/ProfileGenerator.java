package com.mammen.main;

import com.mammen.util.Mathf;

import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Trajectory.Config;
import jaci.pathfinder.Trajectory.FitMethod;
import jaci.pathfinder.Trajectory.Segment;
import jaci.pathfinder.Waypoint;
import jaci.pathfinder.modifiers.SwerveModifier;
import jaci.pathfinder.modifiers.TankModifier;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        FEET("FEET"),
        INCHES("INCHES"),
        METERS("METERS");
        
        
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
    	resetValues("FEET");
    }
    
    public void updateVarUnits( Units old_unit, Units new_unit )
    {
    	// Convert each point in the waypoints list
    	POINTS.forEach((Waypoint wp) -> 
        {
        	double tmp_x = 0, tmp_y = 0;
        	
        	// convert to intermediate unit of feet
        	switch(old_unit)
        	{
        	case FEET:
        		tmp_x = wp.x;
        		tmp_y = wp.y;
        		break;
        		
        	case INCHES:
        		tmp_x = Mathf.inchesToFeet( wp.x );
        		tmp_y = Mathf.inchesToFeet( wp.y );
        		break;
        		
        	case METERS:
        		tmp_x = Mathf.meterToFeet( wp.x );
        		tmp_y = Mathf.meterToFeet( wp.y );
        		break;
        	}
        	
        	// convert from intermediate unit of feet
        	switch(new_unit)
        	{
        	case FEET:
        		wp.x = tmp_x;
        		wp.y = tmp_y;
        		break;
        		
        	case INCHES:
        		wp.x = Mathf.feetToInches( tmp_x );
        		wp.y = Mathf.feetToInches( tmp_y );
        		break;
        		
        	case METERS:
        		wp.x = Mathf.feetToMeter( tmp_x );
        		wp.y = Mathf.feetToMeter( tmp_y );
        		break;
        	}
        	
        	wp.x = Mathf.round( wp.x, 4 );
        	wp.y = Mathf.round( wp.y, 4 );
        });
    	
    	// Convert each MP variable to the new unit
    	double tmp_WBW = 0, tmp_vel = 0, tmp_acc = 0, tmp_jer = 0;
    	
    	// convert to intermediate unit of feet
    	switch(old_unit)
    	{
    	case FEET:
    		tmp_WBW = wheelBaseW;
    		tmp_vel = velocity;
    		tmp_acc = acceleration;
    		tmp_jer = jerk;
    		break;
    		
    	case INCHES:
    		tmp_WBW = Mathf.inchesToFeet( wheelBaseW );
    		tmp_vel = Mathf.inchesToFeet( velocity );
    		tmp_acc = Mathf.inchesToFeet( acceleration );
    		tmp_jer = Mathf.inchesToFeet( jerk );
    		break;
    		
    	case METERS:
    		tmp_WBW = Mathf.meterToFeet( wheelBaseW );
    		tmp_vel = Mathf.meterToFeet( velocity );
    		tmp_acc = Mathf.meterToFeet( acceleration );
    		tmp_jer = Mathf.meterToFeet( jerk );
    		break;
    	}
    	
    	// convert from intermediate unit of feet
    	switch(new_unit)
    	{
    	case FEET:
    		wheelBaseW = tmp_WBW;
    		velocity = tmp_vel;
    		acceleration = tmp_acc;
    		jerk = tmp_jer;
    		break;
    		
    	case INCHES:
    		wheelBaseW = Mathf.feetToInches( tmp_WBW );
    		velocity = Mathf.feetToInches( tmp_vel );
    		acceleration = Mathf.feetToInches( tmp_acc );
    		jerk = Mathf.feetToInches( tmp_jer );
    		
    		break;
    		
    	case METERS:
    		wheelBaseW = Mathf.feetToMeter( tmp_WBW );
    		velocity = Mathf.feetToMeter( tmp_vel );
    		acceleration = Mathf.feetToMeter( tmp_acc );
    		jerk = Mathf.feetToMeter( tmp_jer );
    		break;
    	}
    	
    	wheelBaseW = Mathf.round( wheelBaseW, 4 );
    	velocity = Mathf.round( velocity, 4 );
    	acceleration = Mathf.round( acceleration, 4 );
    	jerk = Mathf.round( jerk, 4 );
    }
    
    /**
     * Exports all trajectories to the parent folder, with the given root name and file extension.
     *
     * @param parentPath the absolute file path to save to, excluding file extension
     * @param ext        the file extension to save to, can be {@code *.csv} or {@code *.traj}
     * @throws Pathfinder.GenerationException
     */
    public void exportTrajectoriesJaci(File parentPath, String ext) throws Pathfinder.GenerationException {
        updateTrajectories();

        File dir = parentPath.getParentFile();

        if (dir != null && !dir.exists() && dir.isDirectory()) {
            if (!dir.mkdirs())
                return;
        }

        switch (ext) {
            case ".csv":
                Pathfinder.writeToCSV(new File(parentPath + "_source_Jaci.csv"), source);

                if (driveBase == DriveBase.SWERVE) {
                    Pathfinder.writeToCSV(new File(parentPath + "_fl_Jaci.csv"), fl);
                    Pathfinder.writeToCSV(new File(parentPath + "_fr_Jaci.csv"), fr);
                    Pathfinder.writeToCSV(new File(parentPath + "_bl_Jaci.csv"), bl);
                    Pathfinder.writeToCSV(new File(parentPath + "_br_Jaci.csv"), br);
                } else {
                    Pathfinder.writeToCSV(new File(parentPath + "_left_Jaci.csv"), fl);
                    Pathfinder.writeToCSV(new File(parentPath + "_right_Jaci.csv"), fr);
                }
            break;
            case ".traj":
                Pathfinder.writeToFile(new File(parentPath + "_source_Jaci.traj"), source);

                if (driveBase == DriveBase.SWERVE) {
                    Pathfinder.writeToFile(new File(parentPath + "_fl_Jaci.traj"), fl);
                    Pathfinder.writeToFile(new File(parentPath + "_fr_Jaci.traj"), fr);
                    Pathfinder.writeToFile(new File(parentPath + "_bl_Jaci.traj"), bl);
                    Pathfinder.writeToFile(new File(parentPath + "_br_Jaci.traj"), br);
                } else {
                    Pathfinder.writeToFile(new File(parentPath + "_left_Jaci.traj"), fl);
                    Pathfinder.writeToFile(new File(parentPath + "_right_Jaci.traj"), fr);
                }
            break;
            default:
                throw new IllegalArgumentException("Invalid file extension");
        }
    }
    
    public void exportTrajectoriesTalon(File parentPath, String ext) throws Pathfinder.GenerationException, IOException {
        updateTrajectories();

        File dir = parentPath.getParentFile();

        if (dir != null && !dir.exists() && dir.isDirectory()) {
            if (!dir.mkdirs())
                return;
        }
        switch (ext) {
            case ".csv":
                if (driveBase == DriveBase.SWERVE) {
                	File flFile = new File(parentPath + "_fl_Talon.csv");
			        File frFile = new File(parentPath + "_fr_Talon.csv");
			        File blFile = new File(parentPath + "_bl_Talon.csv");
			        File brFile = new File(parentPath + "_br_Talon.csv");
			        FileWriter flfw = new FileWriter( flFile );
					FileWriter frfw = new FileWriter( frFile );
					FileWriter blfw = new FileWriter( blFile );
					FileWriter brfw = new FileWriter( brFile );
					PrintWriter flpw = new PrintWriter( flfw );
					PrintWriter frpw = new PrintWriter( frfw );
					PrintWriter blpw = new PrintWriter( blfw );
					PrintWriter brpw = new PrintWriter( brfw );
                	// CSV with position and velocity. To be used with Talon SRX Motion
		    		// save front left path to CSV
			    	for (int i = 0; i < fl.length(); i++) 
			    	{			
			    		Segment seg = fl.get(i);
			    		flpw.printf("%f, %f, %d\n", seg.position, seg.velocity, (int)(seg.dt * 1000));
			    	}
			    			
			    	// save front right path to CSV
			    	for (int i = 0; i < fr.length(); i++) 
			    	{			
			    		Segment seg = fr.get(i);
			    		frpw.printf("%f, %f, %d\n", seg.position, seg.velocity, (int)(seg.dt * 1000));
			    	}
			    	
			    	// save back left path to CSV
			    	for (int i = 0; i < bl.length(); i++) 
			    	{			
			    		Segment seg = bl.get(i);
			    		blpw.printf("%f, %f, %d\n", seg.position, seg.velocity, (int)(seg.dt * 1000));
			    	}
			    			
			    	// save back right path to CSV
			    	for (int i = 0; i < br.length(); i++) 
			    	{			
			    		Segment seg = br.get(i);
			    		brpw.printf("%f, %f, %d\n", seg.position, seg.velocity, (int)(seg.dt * 1000));
			    	}
			    	flpw.close();
			    	frpw.close();
			    	blpw.close();
			    	brpw.close();
                } else {
                	File lFile = new File(parentPath + "_left_Talon.csv");
			        File rFile = new File(parentPath + "_right_Talon.csv");
			        FileWriter lfw = new FileWriter( lFile );
					FileWriter rfw = new FileWriter( rFile );
					PrintWriter lpw = new PrintWriter( lfw );
					PrintWriter rpw = new PrintWriter( rfw );
                	// CSV with position and velocity. To be used with Talon SRX Motion
			    	// save left path to CSV
			    	for (int i = 0; i < fl.length(); i++) 
			    	{			
			    		Segment seg = fl.get(i);
			    		lpw.printf("%f, %f, %d\n", seg.position, seg.velocity, (int)(seg.dt * 1000));
			    	}
			    			
			    	// save right path to CSV
			    	for (int i = 0; i < fr.length(); i++) 
			    	{			
			    		Segment seg = fr.get(i);
			    		rpw.printf("%f, %f, %d\n", seg.position, seg.velocity, (int)(seg.dt * 1000));
			    	}
			    	lpw.close();
			    	rpw.close();
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
     * Saves the project in XML format.
     *
     * @param path the absolute file path to save to, including file name and extension
     * @throws IOException
     * @throws ParserConfigurationException
     */
    public void saveProjectAs(File path) throws IOException, ParserConfigurationException {
        if (!path.getAbsolutePath().endsWith("." + PROJECT_EXTENSION))
            path = new File(path + "." + PROJECT_EXTENSION);

        File dir = path.getParentFile();

        if (dir != null && !dir.exists() && dir.isDirectory()) {
            if (!dir.mkdirs())
                return;
        }

        if (path.exists() && !path.delete())
            return;

        workingProject = path;

        saveWorkingProject();
    }

    /**
     * Saves the working project.
     *
     * @throws IOException
     * @throws ParserConfigurationException
     */
    public void saveWorkingProject() throws IOException, ParserConfigurationException {
        if (workingProject != null) {
            // Create document
            DocumentBuilder db = dbFactory.newDocumentBuilder();
            Document dom = db.newDocument();

            Element trajectoryEle = dom.createElement("Trajectory");

            trajectoryEle.setAttribute("dt", "" + timeStep);
            trajectoryEle.setAttribute("velocity", "" + velocity);
            trajectoryEle.setAttribute("acceleration", "" + acceleration);
            trajectoryEle.setAttribute("jerk", "" + jerk);
            trajectoryEle.setAttribute("wheelBaseW", "" + wheelBaseW);
            trajectoryEle.setAttribute("wheelBaseD", "" + wheelBaseD);
            trajectoryEle.setAttribute("fitMethod", "" + fitMethod.toString());
            trajectoryEle.setAttribute("driveBase", "" + driveBase.toString());
            trajectoryEle.setAttribute("units", "" + units.toString());

            dom.appendChild(trajectoryEle);

            for (Waypoint w : POINTS) {
                Element waypointEle = dom.createElement("Waypoint");
                Element xEle = dom.createElement("X");
                Element yEle = dom.createElement("Y");
                Element angleEle = dom.createElement("Angle");
                Text xText = dom.createTextNode("" + w.x);
                Text yText = dom.createTextNode("" + w.y);
                Text angleText = dom.createTextNode("" + w.angle);

                xEle.appendChild(xText);
                yEle.appendChild(yText);
                angleEle.appendChild(angleText);

                waypointEle.appendChild(xEle);
                waypointEle.appendChild(yEle);
                waypointEle.appendChild(angleEle);

                trajectoryEle.appendChild(waypointEle);
            }

            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(workingProject);
                DOMImplementationRegistry reg = DOMImplementationRegistry.newInstance();
                DOMImplementationLS impl = (DOMImplementationLS) reg.getDOMImplementation("LS");
                LSSerializer serializer = impl.createLSSerializer();
                
                serializer.getDomConfig().setParameter("format-pretty-print", Boolean.TRUE);
                
                LSOutput lso = impl.createLSOutput();
                lso.setByteStream(fos);
                serializer.write(dom,lso);
               
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
    
    /**
     * Loads a project from file.
     *
     * @param path the absolute file path to load the project from
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    public void loadProject(File path) throws IOException, ParserConfigurationException, SAXException {
        if (!path.exists() || path.isDirectory())
            return;

        if (path.getAbsolutePath().toLowerCase().endsWith("." + PROJECT_EXTENSION)) {
            DocumentBuilder db = dbFactory.newDocumentBuilder();

            Document dom = db.parse(path);

            Element docEle = dom.getDocumentElement();

            timeStep = Double.parseDouble(docEle.getAttribute("dt"));
            velocity = Double.parseDouble(docEle.getAttribute("velocity"));
            acceleration = Double.parseDouble(docEle.getAttribute("acceleration"));
            jerk = Double.parseDouble(docEle.getAttribute("jerk"));
            wheelBaseW = Double.parseDouble(docEle.getAttribute("wheelBaseW"));
            wheelBaseD = Double.parseDouble(docEle.getAttribute("wheelBaseD"));

            driveBase = DriveBase.valueOf(docEle.getAttribute("driveBase"));
            fitMethod = FitMethod.valueOf(docEle.getAttribute("fitMethod"));
            units = Units.valueOf(docEle.getAttribute("units"));

            NodeList waypointEleList = docEle.getElementsByTagName("Waypoint");

            POINTS.clear();
            if (waypointEleList != null && waypointEleList.getLength() > 0) {
                for (int i = 0; i < waypointEleList.getLength(); i++) {
                    Element waypointEle = (Element) waypointEleList.item(i);

                    String
                            xText = waypointEle.getElementsByTagName("X").item(0).getTextContent(),
                            yText = waypointEle.getElementsByTagName("Y").item(0).getTextContent(),
                            angleText = waypointEle.getElementsByTagName("Angle").item(0).getTextContent();

                    POINTS.add(new Waypoint(
                            Double.parseDouble(xText),
                            Double.parseDouble(yText),
                            Double.parseDouble(angleText)
                    ));
                }
            }

            workingProject = path;
        }
    }
    
    /**
     * Imports a properties (*.bot) file into the generator.
     * This import method should work with properties files generated from version 2.3.0.
     *
     * @param path     the file path of the bot file
     * @param botUnits the units to use for this bot file
     * @throws IOException
     */
    public void importBotFile(File path, Units botUnits) throws IOException, NumberFormatException {
        if (!path.exists() || path.isDirectory())
            return;

        if (path.getAbsolutePath().toLowerCase().endsWith(".bot")) {
            BufferedReader botReader = new BufferedReader(new FileReader(path));
            Stream<String> botStream = botReader.lines();
            List<String> botLines = botStream.collect(Collectors.toList());

            // First off we need to set the units of distance being used in the file.
            // Unfortunately it is not explicitly saved to file; we will need some user input on that.
            units = botUnits;

            // Now we can read the first 7 lines and assign them accordingly.
            timeStep = Math.abs(Double.parseDouble(botLines.get(0).trim()));
            velocity = Math.abs(Double.parseDouble(botLines.get(1).trim()));
            acceleration = Math.abs(Double.parseDouble(botLines.get(2).trim()));
            jerk = Math.abs(Double.parseDouble(botLines.get(3).trim()));
            wheelBaseW = Math.abs(Double.parseDouble(botLines.get(4).trim()));
            wheelBaseD = Math.abs(Double.parseDouble(botLines.get(5).trim()));

            fitMethod = FitMethod.valueOf("HERMITE_" + botLines.get(6).trim().toUpperCase());

            if (wheelBaseD > 0) // Assume that the wheel base was swerve
                driveBase = DriveBase.SWERVE;

            // GLHF parse the rest of the file I guess...
            for (int i = 7; i < botLines.size(); i++) {
                String[] waypointVals = botLines.get(i).split(",");

                POINTS.add(new Waypoint(
                    Double.parseDouble(waypointVals[0].trim()),
                    Double.parseDouble(waypointVals[1].trim()),
                    Math.toRadians(Double.parseDouble(waypointVals[2].trim()))
                ));
            }

            // Make sure you aren't trying to save to another project file
            clearWorkingFiles();
            botReader.close();
        }
    }
    
    /**
     * Clears the working project files
     */
    public void clearWorkingFiles() {
        workingProject = null;
    }
    
    /**
     * Resets configuration to default values
     */
    public void resetValues(String choUnits) 
    {
    	if(choUnits.equals("FEET")) {
	        timeStep = 0.05;
	        velocity = 4;
	        acceleration = 3;
	        jerk = 60;
	        wheelBaseW = 1.464;
	        wheelBaseD = 0;
	
	        fitMethod = FitMethod.HERMITE_CUBIC;
	        driveBase = DriveBase.TANK;
	        units = Units.FEET;
    	}
    	else if(choUnits.equals("METERS")) {
    		timeStep = 0.05;
	        velocity = 1.2192;
	        acceleration = 0.9144;
	        jerk = 18.288;
	        wheelBaseW = 0.4462272;
	        wheelBaseD = 0;
	
	        fitMethod = FitMethod.HERMITE_CUBIC;
	        driveBase = DriveBase.TANK;
	        units = Units.METERS;
    	}
    	else if(choUnits.equals("INCHES")) {
    		timeStep = 0.05;
	        velocity = 48;
	        acceleration = 36;
	        jerk = 720;
	        wheelBaseW = 17.568;
	        wheelBaseD = 0;
	
	        fitMethod = FitMethod.HERMITE_CUBIC;
	        driveBase = DriveBase.TANK;
	        units = Units.INCHES;
    	}
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
    
    public boolean hasWorkingProject() {
        return workingProject != null;
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










