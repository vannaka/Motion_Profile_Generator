package com.mammen.settings;

import com.mammen.generator.Path;
import com.mammen.util.SerializeHelpers.ObjectSerializer;
import com.mammen.util.SerializeHelpers.ReadObjectsHelper;
import com.mammen.util.SerializeHelpers.WriteObjectsHelper;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;

public class Settings implements Serializable
{
    /******************************************************
     *   Serializable stuff
     ******************************************************/
    private static final String DIR_NAME = "motion-profile-generator";
    private static final String path = System.getProperty("user.home") + File.separator + "." + DIR_NAME + File.separator + "settings.set";
    private static Settings settings = null;


    /******************************************************
     *   Settings
     ******************************************************/
    private transient StringProperty graphBGImagePath;
    private transient BooleanProperty addPointOnClick;
    private transient Property<SourcePathDisplayType> sourcePathDisplayType;
    private transient ListProperty<Path.Elements> chosenCSVElements;
    private transient ListProperty<Path.Elements> availableCSVElements;
    private transient StringProperty workingDirectory;


    /******************************************************
     *   Constructors
     ******************************************************/
    // Prevent instantiation of this class
    private Settings()
    {
        initialize();

        chosenCSVElements.add( Path.Elements.DELTA_TIME );
        chosenCSVElements.add( Path.Elements.POSITION );
        chosenCSVElements.add( Path.Elements.VELOCITY );

        availableCSVElements.add( Path.Elements.X_POINT );
        availableCSVElements.add( Path.Elements.Y_POINT );
        availableCSVElements.add( Path.Elements.ACCELERATION );
        availableCSVElements.add( Path.Elements.JERK );
        availableCSVElements.add( Path.Elements.HEADING );
    }

    private void initialize()
    {
        graphBGImagePath = new SimpleStringProperty();
        addPointOnClick = new SimpleBooleanProperty( true );
        sourcePathDisplayType = new SimpleObjectProperty<>( SourcePathDisplayType.WP_ONLY );
        chosenCSVElements = new SimpleListProperty<>( FXCollections.observableArrayList() );
        availableCSVElements = new SimpleListProperty<>( FXCollections.observableArrayList() );
        workingDirectory = new SimpleStringProperty( System.getProperty("user.dir") );
    }


    /**************************************************************************
     *  getInstance
     *      Use this method to retrieve the settings for this program.
     * @return If the settings file is found it will return previous settings.
     *         If not then default settings will be returned.
     *************************************************************************/
    public static Settings getSettings()
    {
        if( settings == null )
        {
            settings = (Settings)ObjectSerializer.loadObject( path );

            if( settings == null )
            {
                settings = new Settings();
            }
        }

        return settings;
    }


    /**************************************************************************
     *  saveSettings
     *      Save the settings to a file.
     *************************************************************************/
    public void saveSettings() throws IOException
    {
        ObjectSerializer.saveObject( this, path );
    }


    /******************************************************
     *   Getters and Setters
     ******************************************************/
    public String getGraphBGImagePath()
    {
        return graphBGImagePath.get();
    }

    public StringProperty graphBGImagePathProperty()
    {
        return graphBGImagePath;
    }

    public void setGraphBGImagePath(String graphBGImagePath)
    {
        this.graphBGImagePath.set(graphBGImagePath);
    }

    public boolean isAddPointOnClick()
    {
        return addPointOnClick.get();
    }

    public BooleanProperty addPointOnClickProperty()
    {
        return addPointOnClick;
    }

    public void setAddPointOnClick( boolean addPointOnClick )
    {
        this.addPointOnClick.set( addPointOnClick );

    }

    public SourcePathDisplayType getSourcePathDisplayType()
    {
        return sourcePathDisplayType.getValue();
    }

    public Property<SourcePathDisplayType> sourcePathDisplayTypeProperty()
    {
        return sourcePathDisplayType;
    }

    public void setSourcePathDisplayType( SourcePathDisplayType sourcePathDisplayType )
    {
        this.sourcePathDisplayType.setValue( sourcePathDisplayType );
    }

    public ObservableList<Path.Elements> getChosenCSVElements()
    {
        return chosenCSVElements.get();
    }

    public ListProperty<Path.Elements> chosenCSVElementsProperty()
    {
        return chosenCSVElements;
    }

    public void setChosenCSVElements( ObservableList<Path.Elements> chosenCSVElements )
    {
        this.chosenCSVElements.set( chosenCSVElements );
    }

    public ObservableList<Path.Elements> getAvailableCSVElements()
    {
        return availableCSVElements.get();
    }

    public ListProperty<Path.Elements> availableCSVElementsProperty()
    {
        return availableCSVElements;
    }

    public void setAvailableCSVElements( ObservableList<Path.Elements> availableCSVElements )
    {
        this.availableCSVElements.set( availableCSVElements );
    }

    public String getWorkingDirectory()
    {
        return workingDirectory.get();
    }

    public StringProperty workingDirectoryProperty()
    {
        return workingDirectory;
    }

    public void setWorkingDirectory( String workingDirectory )
    {
        this.workingDirectory.set( workingDirectory );
    }


    /**************************************************************************
     *  THESE TWO METHODS ARE NEEDED TO SERIALIZE THIS CLASS
     *************************************************************************/
    private void writeObject( ObjectOutputStream s ) throws IOException
    {
        WriteObjectsHelper.writeStringProp( s, graphBGImagePath );
        WriteObjectsHelper.writeBoolProp( s, addPointOnClick );
        WriteObjectsHelper.writePropSourcePathDsplyType( s, sourcePathDisplayType );
        WriteObjectsHelper.writeListPropPathElem( s, chosenCSVElements );
        WriteObjectsHelper.writeListPropPathElem( s, availableCSVElements );
        WriteObjectsHelper.writeStringProp( s, workingDirectory );
    }

    private void readObject( ObjectInputStream s ) throws IOException, ClassNotFoundException
    {
        initialize();
        ReadObjectsHelper.readStringProp( s, graphBGImagePath );
        ReadObjectsHelper.readBoolProp( s, addPointOnClick );
        ReadObjectsHelper.readPropSourcePathDsplyType( s, sourcePathDisplayType );
        ReadObjectsHelper.readListPropPathElem( s, chosenCSVElements );
        ReadObjectsHelper.readListPropPathElem( s, availableCSVElements );
        ReadObjectsHelper.readStringProp( s, workingDirectory );
    }
}
