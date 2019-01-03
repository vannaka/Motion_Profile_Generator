package com.mammen.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MainApp extends Application
{
    @Override
    public void start( Stage primaryStage )
    {
        try
        {
            Pane root = FXMLLoader.load( getClass().getResource("/com/mammen/ui/javafx/main/MainUI.fxml") );
            root.autosize();

            primaryStage.setScene( new Scene( root ) );
            primaryStage.sizeToScene();
            primaryStage.setTitle("Motion Profile Generator");
            primaryStage.setMinWidth( 1280 ); //1170
            primaryStage.setMinHeight( 720 ); //790
            primaryStage.setResizable( true );

            primaryStage.show();
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
    }

    public static void main(String[] args)
    {
        // JavaFX 11+ uses GTK3 by default, and has problems on some display servers
        // This flag forces JavaFX to use GTK2
        System.setProperty( "jdk.gtk.version", "2" );

        launch( args );
    }
}
