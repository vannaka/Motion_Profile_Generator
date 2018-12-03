package com.mammen.main;

import com.mammen.file_io.FileIO;
import com.mammen.generator.*;
import com.mammen.generator.variables.GeneratorVars;
import com.mammen.generator.variables.PfV1GeneratorVars;
import com.mammen.path.Path;
import com.mammen.path.Waypoint;
import com.mammen.settings.SettingsModel;
import com.mammen.util.Mathf;

import javafx.beans.Observable;
import javafx.beans.property.*;
import javafx.collections.FXCollections;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;

import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.io.*;
import java.util.List;


/******************************************************************************
 *   MainUIModel
 *       This is the model for the program. It contains all the data needed to
 *       generate a path as well as the path itself. We use the JavaBeans model
 *       of properties. The trajectories are setup so that
 *       they are recalculated whenever any of their dependencies changes value.
 *       These dependencies (velocity, accel, jerk, ect. ) are bound to gui
 *       elements in their respective fxml controllers.
 ******************************************************************************/
public class MainUIModel
{
	private static final String PROJECT_EXTENSION = "xml";

    /******************************************************
     *   Property variables
     ******************************************************/
//    private Property<variables> generatorVars = new SimpleObjectProperty<>();
    private Generator generator;
    private GeneratorVars generatorVars;

    private ListProperty<Waypoint> waypointList = new SimpleListProperty<>(
                                                                FXCollections.observableArrayList(
                                                                        p -> new Observable[]{ p.xProperty(), p.yProperty(), p.angleProperty() } ) );

    /******************************************************
     *   Generated path.
     ******************************************************/
    private Property<Path> path = new SimpleObjectProperty<>();
    
    // File stuff
    private DocumentBuilderFactory dbFactory;
    private File workingProject;

    /******************************************************
     *   Program settings.
     ******************************************************/
    private SettingsModel settings;

    /******************************************************
     *   The one and only instance of this class.
     ******************************************************/
    private static MainUIModel backend = null;

    /**************************************************************************
     *   Constructor
     *************************************************************************/
    public MainUIModel()
    {
        generator = new PfV1Generator();
        generatorVars = new PfV1GeneratorVars();

        settings = SettingsModel.getInstance();

    	dbFactory = DocumentBuilderFactory.newInstance();

    }   /* MainUIModel() */


    /**************************************************************************
     * <p>Returns the backend model.</p>
     *
     * @return The one and only instance of the backend model.
     *************************************************************************/
    public static MainUIModel getInstance()
    {
        if( backend == null )
        {
            backend = new MainUIModel();
        }

        return backend;
    }


    /**************************************************************************
     * <p>Generates a Path that fits the given waypoints.</p>
     *
     * @throws com.mammen.generator.Generator.PathGenerationException
     *      The path failed to generate.
     * @throws com.mammen.generator.Generator.NotEnoughPointsException
     *      There were not enough points to generate a path.
     *************************************************************************/
    public void generatePath() throws Generator.PathGenerationException, Generator.NotEnoughPointsException
    {
        Path newPath = generator.generate( waypointList, generatorVars );

        if( newPath != null )
        {
            path.setValue( newPath );
        }
    }   /* generatePath() */


    /**************************************************************************
     *  updateVarUnits
     *      Converts the variables from one Unit to another.
     *
     * @param old_unit The current Unit.
     * @param new_unit The Unit to convert to.
     **************************************************************************/
    private void updateVarUnits(Units old_unit, Units new_unit )
    {
        // TODO: Find a better way of doing this!!!
        //          Maybe storing the values in the backend in feet
        //          and only convert it for display.

    	// Convert each point in the waypoints list
        for( Waypoint wp : waypointList )
        {
            double tmp_x = 0, tmp_y = 0;

            // convert to intermediate unit of feet
            switch( old_unit )
            {
                case FEET:
                    tmp_x = wp.getX();
                    tmp_y = wp.getY();
                    break;

                case INCHES:
                    tmp_x = Mathf.inchesToFeet( wp.getX() );
                    tmp_y = Mathf.inchesToFeet( wp.getY() );
                    break;

                case METERS:
                    tmp_x = Mathf.meterToFeet( wp.getX() );
                    tmp_y = Mathf.meterToFeet( wp.getY() );
                    break;
            }

            // convert from intermediate unit of feet
            switch( new_unit )
            {
                case FEET:
                    wp.setX( tmp_x );
                    wp.setY( tmp_y );
                    break;

                case INCHES:
                    wp.setX( Mathf.feetToInches( tmp_x ) );
                    wp.setY( Mathf.feetToInches( tmp_y ) );
                    break;

                case METERS:
                    wp.setX( Mathf.feetToMeter( tmp_x ) );
                    wp.setY( Mathf.feetToMeter( tmp_y ) );
                    break;
            }
        }

        generatorVars.changeUnit( new_unit );

    }   /* updateVarUnits() */


    /**************************************************************************
     * <p>Exports the Path to the parent folder, with the given root
     *      name and file extension.</p>
     *
     * @param parentPath The .csv file to save to. This method will write to
     *                   multiple files depending on the drivebase of the Path.
     *                   Each filename will be an appended version of the .csv
     *                   that parentPath references.
     *************************************************************************/
    public void exportPath( File parentPath ) throws FileNotFoundException
    {
        FileIO.savePath( path.getValue(), parentPath, settings.getChosenCSVElements() );
    }   /* exportPath() */


    /**
     * Saves the project in XML format.
     */
    public void saveProjectAs( File path ) throws ParserConfigurationException
    {
        if( !path.getAbsolutePath().endsWith("." + PROJECT_EXTENSION ) )
            path = new File(path + "." + PROJECT_EXTENSION );

        File dir = path.getParentFile();

        if( dir != null && !dir.exists() && dir.isDirectory() )
        {
            if (!dir.mkdirs())
                return;
        }

        if( path.exists() && !path.delete() )
            return;

        workingProject = path;

        saveWorkingProject();
    }

    /**
     * Saves the working project.
     */
    public void saveWorkingProject() throws ParserConfigurationException
    {
        if( workingProject != null )
        {
            // Create document
            DocumentBuilder db = dbFactory.newDocumentBuilder();
            Document dom = db.newDocument();

            // XML entry for the path waypoints and vars
            Element pathElement = dom.createElement("Path" );

            // Write generator vars to xml file
            generatorVars.writeXMLAttributes( pathElement );
            dom.appendChild( pathElement );

            // Write waypoints to xml file
            for( Waypoint wp : waypointList )
            {
                Element waypointEle = dom.createElement("Waypoint" );
                Element xEle = dom.createElement("X" );
                Element yEle = dom.createElement("Y" );
                Element angleEle = dom.createElement("Angle" );
                Text xText = dom.createTextNode("" + wp.getX() );
                Text yText = dom.createTextNode("" + wp.getY() );
                Text angleText = dom.createTextNode("" + wp.getAngle() );

                xEle.appendChild( xText );
                yEle.appendChild( yText );
                angleEle.appendChild( angleText );

                waypointEle.appendChild( xEle );
                waypointEle.appendChild( yEle );
                waypointEle.appendChild( angleEle );

                pathElement.appendChild( waypointEle );
            }

            FileOutputStream fos = null;
            try
            {
                fos = new FileOutputStream( workingProject );
                DOMImplementationRegistry reg = DOMImplementationRegistry.newInstance();
                DOMImplementationLS impl = (DOMImplementationLS) reg.getDOMImplementation("LS" );
                LSSerializer serializer = impl.createLSSerializer();
                
                serializer.getDomConfig().setParameter("format-pretty-print", Boolean.TRUE );
                
                LSOutput lso = impl.createLSOutput();
                lso.setByteStream( fos );
                serializer.write( dom, lso );
               
            }
            catch( Exception e )
            {
                throw new RuntimeException( e );
            }
        }
    }
    
    /**
     * Loads a project from file.
     */
    public void loadProject( File path ) throws IOException, ParserConfigurationException, SAXException
    {
        if( !path.exists() || path.isDirectory() )
            return;

        if( path.getAbsolutePath().toLowerCase().endsWith( "." + PROJECT_EXTENSION ) )
        {
            DocumentBuilder db = dbFactory.newDocumentBuilder();
            Document dom = db.parse( path );

            Element docEle = dom.getDocumentElement();

            // Get generator vars from xml file.
            generatorVars.readXMLAttributes( docEle );

            // Get waypoints from xml file.
            NodeList waypointEleList = docEle.getElementsByTagName( "Waypoint" );

            waypointList.clear();
            if( waypointEleList != null && waypointEleList.getLength() > 0 )
            {
                for( int i = 0; i < waypointEleList.getLength(); i++ )
                {
                    Element waypointEle = (Element) waypointEleList.item( i );

                    String xText = waypointEle.getElementsByTagName("X").item(0).getTextContent();
                    String yText = waypointEle.getElementsByTagName("Y").item(0).getTextContent();
                    String angleText = waypointEle.getElementsByTagName("Angle").item(0).getTextContent();

                    waypointList.add( new Waypoint(
                                            Double.parseDouble( xText ),
                                            Double.parseDouble( yText ),
                                            Double.parseDouble( angleText )
                                        ) );
                }
            }

            workingProject = path;
        }
    }
    
    /**
     * Clears the working project files
     */
    public void clearWorkingFiles()
    {
        workingProject = null;
    }


    public GeneratorVars getGeneratorVars()
    {
        return generatorVars;
    }

    /**
     * Adds a waypoint to the list of waypoints
     */
    public void addPoint( double x, double y, double angle ) 
    {
        waypointList.add( new Waypoint( x, y, angle ) );
    }

    /**
     * Adds a waypoint to the list of waypoints
     */
    public void addPoint( Waypoint wp )
    {
        waypointList.add( wp );
    }
    
    public void removePoint( int index )
    {
        waypointList.remove( index );
    }

    public void removeLastPoint()
    {
        removePoint( waypointList.get().size() - 1 );
    }

    public void removePoints( int first, int last )
    {
        waypointList.remove( first, last + 1 );
    }

    public int getNumWaypoints()
    {
        return waypointList.size();
    }

    public Waypoint getWaypoint(int index )
    {
        return waypointList.get( index );
    }

    /**
     * Clears all the existing waypoints in the list.
     * This also clears all trajectories generated by the waypoints.
     */
    public void clearPoints() 
    {
        waypointList.clear();
        path = null;
    }

    public ListProperty<Waypoint> waypointListProperty()
    {
        return waypointList;
    }

    public List<Waypoint> getWaypointList()
    {
        return waypointList;
    }

    public boolean isWaypointListEmpty()
    {
        return waypointList.isEmpty();
    }

    public Path getPath()
    {
        return path.getValue();
    }

    public Property<Path> pathProperty()
    {
        return path;
    }
}










