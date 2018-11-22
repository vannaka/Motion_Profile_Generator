package com.mammen.util.SerializeHelpers;

import java.io.*;

public class ObjectSerializer
{
    public static void saveObject( Object obj, String filePath ) throws IOException
    {
        ObjectOutputStream out = new ObjectOutputStream( new FileOutputStream( filePath ) );
        out.writeObject( obj );
        out.close();
    }

    public static Object loadObject( String filePath )
    {
        Object obj;

        try
        {
            ObjectInputStream in = new ObjectInputStream( new FileInputStream( filePath ) );
            obj = in.readObject();
            in.close();
        }
        catch( IOException | ClassNotFoundException i )
        {
            return null;
        }

        return obj;
    }
}
