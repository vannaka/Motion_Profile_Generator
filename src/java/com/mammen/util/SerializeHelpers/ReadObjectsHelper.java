package com.mammen.util.SerializeHelpers;

import com.mammen.path.Path;
import com.mammen.settings.SourcePathDisplayType;
import javafx.beans.property.*;

import java.io.IOException;
import java.io.ObjectInputStream;

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

    public static void readDoubleProp( ObjectInputStream s, DoubleProperty prop ) throws IOException
    {
        prop.set( s.readDouble() );
    }

    public static <T> void readObjectProp( ObjectInputStream s, Property<T> prop, Class<T> type ) throws IOException, ClassNotFoundException
    {
       prop.setValue( type.cast( s.readObject() ) );
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
