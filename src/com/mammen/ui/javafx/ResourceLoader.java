package com.mammen.ui.javafx;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.jar.Manifest;

public class ResourceLoader 
{
	private ResourceLoader() {
    }

    public static URL getResource(String path) {
        return ResourceLoader.class.getResource(path);
    }

    public static InputStream getResourceAsStream(String path) {
        return ResourceLoader.class.getResourceAsStream(path);
    }

    // TODO: Figure out what manifest this method is actually getting
    public static Manifest getManifest() {
        Manifest mf = new Manifest();

        try {
            mf.read(getResourceAsStream("/META-INF/MANIFEST.MF"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return mf;
    }
}
