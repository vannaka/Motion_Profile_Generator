package com.mammen.util.SerializeHelpers;

import com.mammen.path.Path;
import com.mammen.settings.SourcePathDisplayType;
import javafx.beans.property.*;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class WriteObjectsHelper
{

    // write a StringProperty to ObjectOutputStream
    public static void writeStringProp( ObjectOutputStream s, StringProperty strProp) throws IOException
    {
        s.writeUTF( strProp.getValueSafe() );
    }

    // write a ListProperty to ObjectOutputStream
    public static void writeListPropPathElem( ObjectOutputStream s, ListProperty<Path.Elements> lstProp) throws IOException
    {
        if( lstProp == null || lstProp.getValue() == null )
        {
            s.writeInt(0 );
            return;
        }
        s.writeInt( lstProp.size() );
        for( Path.Elements elt : lstProp.getValue() )
            s.writeObject( elt );
    }

    public static void writeBoolProp( ObjectOutputStream s, BooleanProperty boolProp ) throws IOException
    {
        s.writeBoolean( boolProp.get() );
    }

    public static void writeDoubleProp( ObjectOutputStream s, DoubleProperty doubleProp ) throws IOException
    {
        s.writeDouble( doubleProp.get() );
    }

    public static void writeObjectProp( ObjectOutputStream s, Property prop ) throws IOException
    {
        s.writeObject( prop.getValue() );
    }
}
