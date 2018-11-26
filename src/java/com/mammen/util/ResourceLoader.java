package com.mammen.util;

import java.io.*;
import java.net.URL;
import java.nio.file.StandardCopyOption;
import java.util.jar.Manifest;

public class ResourceLoader 
{
	private ResourceLoader()
    {
    }

    public static URL getResource(String path)
    {
        return ResourceLoader.class.getResource( path );
    }

    public static InputStream getResourceAsStream( String path )
    {
        return ResourceLoader.class.getResourceAsStream(path);
    }

    public static void resourceToFile( String rsPath, File file ) throws IOException
    {
        InputStream is = getResourceAsStream( rsPath );

        java.nio.file.Files.copy( is, file.toPath(), StandardCopyOption.REPLACE_EXISTING );

        is.close();
    }

    // TODO: Figure out what manifest this method is actually getting
    public static Manifest getManifest()
    {
        Manifest mf = new Manifest();

        try
        {
            mf.read(getResourceAsStream("/META-INF/MANIFEST.MF"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return mf;
    }
}
