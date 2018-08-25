package com.mammen.main;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;


public class Main extends Application 
{
	@Override
	public void start(Stage primaryStage)
	{
		try
		{
			Pane root = FXMLLoader.load( getClass().getResource("/com/mammen/ui/javafx/MainUI.fxml") );
	        root.autosize();
	        
			primaryStage.setScene( new Scene(root) );
			primaryStage.sizeToScene();
			primaryStage.setTitle("Motion Profile Generator");
			primaryStage.setMinWidth( 1170 );
	        primaryStage.setMinHeight( 790 );
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
