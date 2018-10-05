package com.mammen.main;

import com.mammen.ui.javafx.factory.AlertFactory;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;
import org.scijava.nativelib.NativeLoader;

import java.io.IOException;


public class Main extends Application 
{
	@Override
	public void start( Stage primaryStage )
	{
		// Load Pathfinder native lib
		try
		{
			NativeLoader.loadLibrary("pathfinderjava" );
		}
		catch( IOException e )
		{
			e.printStackTrace();
			Alert alert = AlertFactory.createExceptionAlert(e, "Failed to load Pathfinder lib!" );

			alert.showAndWait();
		}

		try
		{
			Pane root = FXMLLoader.load( getClass().getResource("/com/mammen/ui/javafx/MainUI.fxml") );
	        root.autosize();

	        FXMLLoader loader = new FXMLLoader( getClass().getResource("/com/mammen/ui/javafx/graphs/PosGraphController.fxml") );
	        loader.getController();

			primaryStage.setScene( new Scene( root ) );
			primaryStage.sizeToScene();
			primaryStage.setTitle("Motion Profile generator");
			primaryStage.setMinWidth( 1280 ); //1170
	        primaryStage.setMinHeight( 720 ); //790
	        primaryStage.setResizable( true );

			primaryStage.show();
		} 
		catch(Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) 
	{	
		launch(args);
	}
}
