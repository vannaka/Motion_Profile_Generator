package com.mammen.util.SerializeHelpers;

import com.mammen.generator.Path;
import com.mammen.settings.SourcePathDisplayType;
import javafx.beans.property.*;
import javafx.collections.FXCollections;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ReadObjectsHelper
{
    public static void readStringProp( ObjectInputStream s, StringProperty prop ) throws IOException
    {
        prop.set( s.readUTF() );
    }

    public static void readBoolProp( ObjectInputStream s, BooleanProperty prop ) throws IOException
    {
        prop.set( s.readBoolean() );
    }

    public static void readPropSourcePathDsplyType( ObjectInputStream s, Property<SourcePathDisplayType> prop ) throws IOException, ClassNotFoundException
    {
        prop.setValue( (SourcePathDisplayType)s.readObject() );
    }

    // Read a ListProperty from ObjectInputStream (and return it)
    public static void readListPropPathElem( ObjectInputStream s, ListProperty<Path.Elements> lst ) throws IOException, ClassNotFoundException
    {
        int loop = s.readInt();
        for( int i = 0; i < loop; i++ )
        {
            lst.add( (Path.Elements)s.readObject() );
        }
    }
}
