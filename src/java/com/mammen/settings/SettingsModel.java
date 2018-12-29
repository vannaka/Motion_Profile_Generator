package com.mammen.settings;

import com.mammen.generator.Generator;
import com.mammen.generator.PfV1Generator;
import com.mammen.generator.generator_vars.GeneratorVars;
import com.mammen.generator.generator_vars.PfV1GeneratorVars;
import com.mammen.generator.generator_vars.SharedGeneratorVars;
import com.mammen.path.Path;
import com.mammen.util.SerializeHelpers.ObjectSerializer;
import com.mammen.util.SerializeHelpers.ReadObjectsHelper;
import com.mammen.util.SerializeHelpers.WriteObjectsHelper;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;

public class SettingsModel implements Serializable
{
    /******************************************************
     *   File stuff
     ******************************************************/
    private static final String FILE_NAME = "settings.set";
    private static final String DIR_NAME = ".motion-profile-generator";
    private static final String SETTINGS_DIR = System.getProperty("user.home") + File.separator + DIR_NAME;
    private static final String SETTINGS_FILE_PATH = SETTINGS_DIR + File.separator + FILE_NAME;
    private Boolean settingsDirExist;


    /******************************************************
     *   The one and only instance of this class.
     ******************************************************/
    private static SettingsModel settings = null;


    /******************************************************
     *   Settings
     ******************************************************/
    private transient StringProperty graphBGImagePath;
    private transient BooleanProperty addPointOnClick;
    private transient Property<SourcePathDisplayType> sourcePathDisplayType;
    private transient ListProperty<Path.Elements> chosenCSVElements;
    private transient ListProperty<Path.Elements> availableCSVElements;
    private transient StringProperty workingDirectory;
    private transient Property<Generator.Type> generatorType;
    private transient Property<GeneratorVars> generatorVars;
    private transient Property<Generator> generator;


    /******************************************************
     *   Instance of each generator and vars type.
     ******************************************************/
    private transient SharedGeneratorVars sharedVars;
    private transient PfV1GeneratorVars pfV1Vars;
    private transient PfV1Generator pfV1Generator;


    /******************************************************
     *   Constructors
     ******************************************************/
    // Prevent instantiation of this class
    private SettingsModel()
    {
        // Create settings dir if it does not exist yet
        File settingsDir = new File( SettingsModel.getSettingsDir() );
        if( !settingsDir.exists() )
        {
            settingsDirExist = settingsDir.mkdirs();
        }
        else
        {
            settingsDirExist = true;
        }

        initialize();

        chosenCSVElements.add( Path.Elements.DELTA_TIME );
        chosenCSVElements.add( Path.Elements.POSITION );
        chosenCSVElements.add( Path.Elements.VELOCITY );

        availableCSVElements.add( Path.Elements.X_POINT );
        availableCSVElements.add( Path.Elements.Y_POINT );
        availableCSVElements.add( Path.Elements.ACCELERATION );
        availableCSVElements.add( Path.Elements.JERK );
        availableCSVElements.add( Path.Elements.HEADING );

        // Update model references when the uses selects a new generator
        generatorType.addListener( (O, oldValue, newValue) ->
        {
            switch( newValue )
            {
                case PATHFINDER_V1:
                    generatorVars.setValue( pfV1Vars );
                    generator.setValue( pfV1Generator );
                    break;

                default:
                    throw new RuntimeException( "The programmer forgot to add a case for the following generator: " + newValue );
            }
        });
    }

    private void initialize()
    {
        sharedVars = SharedGeneratorVars.getInstance();
        pfV1Vars = PfV1GeneratorVars.getInstance();
        pfV1Generator = new PfV1Generator();

        graphBGImagePath        = new SimpleStringProperty();
        addPointOnClick         = new SimpleBooleanProperty( true );
        sourcePathDisplayType   = new SimpleObjectProperty<>( SourcePathDisplayType.WP_ONLY );
        chosenCSVElements       = new SimpleListProperty<>( FXCollections.observableArrayList() );
        availableCSVElements    = new SimpleListProperty<>( FXCollections.observableArrayList() );
        workingDirectory        = new SimpleStringProperty( System.getProperty( "user.dir" ) );
        generatorType           = new SimpleObjectProperty<>( Generator.Type.PATHFINDER_V1 );
        generatorVars           = new SimpleObjectProperty<>( pfV1Vars );
        generator               = new SimpleObjectProperty<>( pfV1Generator );
    }


    /**************************************************************************
     *  getInstance
     *      Use this method to retrieve the settings for this program.
     * @return If the settings file is found it will return previous settings.
     *         If not then default settings will be returned.
     *************************************************************************/
    public static SettingsModel getInstance()
    {
        if( settings == null )
        {
            settings = (SettingsModel)ObjectSerializer.loadObject( SETTINGS_FILE_PATH );

            if( settings == null )
            {
                settings = new SettingsModel();
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
        if( settingsDirExist )
            ObjectSerializer.saveObject( this, SETTINGS_FILE_PATH );
    }


    /**************************************************************************
     *  getSettingsDir
     *      Get the path to the settings directory
     * @return The path to the settings directory.
     *************************************************************************/
    public static String getSettingsDir()
    {
        return SETTINGS_DIR;
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

    public Generator.Type getGeneratorType()
    {
        return generatorType.getValue();
    }

    public Property<Generator.Type> generatorTypeProperty()
    {
        return generatorType;
    }

    public void setGeneratorType( Generator.Type generatorType )
    {
        this.generatorType.setValue(generatorType);
    }

    public GeneratorVars getGeneratorVars()
    {
        return generatorVars.getValue();
    }

    public Property<GeneratorVars> generatorVarsProperty()
    {
        return generatorVars;
    }

    public void setGeneratorVars( GeneratorVars generatorVars )
    {
        this.generatorVars.setValue(generatorVars);
    }

    public Generator getGenerator()
    {
        return generator.getValue();
    }

    public Property<Generator> generatorProperty()
    {
        return generator;
    }

    public SharedGeneratorVars getSharedGeneratorVars()
    {
        return sharedVars;
    }

    public void setSharedGeneratorVars( SharedGeneratorVars sharedVars )
    {
        this.sharedVars = sharedVars;
    }

    /**************************************************************************
     *  THESE TWO METHODS ARE NEEDED TO SERIALIZE THIS CLASS
     *************************************************************************/
    private void writeObject( ObjectOutputStream s ) throws IOException
    {
        WriteObjectsHelper.writeStringProp( s, graphBGImagePath );
        WriteObjectsHelper.writeBoolProp( s, addPointOnClick );
        WriteObjectsHelper.writeObjectProp( s, sourcePathDisplayType );
        WriteObjectsHelper.writeListPropPathElem( s, chosenCSVElements );
        WriteObjectsHelper.writeListPropPathElem( s, availableCSVElements );
        WriteObjectsHelper.writeStringProp( s, workingDirectory );
        WriteObjectsHelper.writeObjectProp( s, generatorType );
    }

    private void readObject( ObjectInputStream s ) throws IOException, ClassNotFoundException
    {
        settingsDirExist = true;

        initialize();

        ReadObjectsHelper.readStringProp( s, graphBGImagePath );
        ReadObjectsHelper.readBoolProp( s, addPointOnClick );
        ReadObjectsHelper.readObjectProp( s, sourcePathDisplayType, SourcePathDisplayType.class );
        ReadObjectsHelper.readListPropPathElem( s, chosenCSVElements );
        ReadObjectsHelper.readListPropPathElem( s, availableCSVElements );
        ReadObjectsHelper.readStringProp( s, workingDirectory );
        ReadObjectsHelper.readObjectProp( s, generatorType, Generator.Type.class );

        switch( generatorType.getValue() )
        {
            case PATHFINDER_V1:
                generatorVars.setValue( pfV1Vars );
                generator.setValue( pfV1Generator );
                break;

            default:
                throw new RuntimeException( "The programmer forgot to add a case for the following generator: " + generatorType.getValue() );
        }
    }
}
