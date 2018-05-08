package com.mammen.main;
	
import com.mammen.util.NativeUtils;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;

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
			Pane root = FXMLLoader.load(getClass().getResource("/com/mammen/ui/javafx/MainUI.fxml"));
			Scene scene = new Scene(root);
			Dimension res = Toolkit.getDefaultToolkit().getScreenSize();
	        int width = res.width - 269;
	        int height = res.height - 111;
	        
			primaryStage.setScene(scene);
			primaryStage.setTitle("Motion Profile Generator");
			
			primaryStage.setWidth(width);
	        primaryStage.setHeight(height);
	        primaryStage.centerOnScreen();

	        primaryStage.setResizable(false);
			primaryStage.show();
		} 
		catch(Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) 
	{
		try {
			//NativeUtils.loadLibraryFromJar("/pathfinderjava.dll");
			NativeUtils.loadLibraryFromJar("/pathfinderjava.so");
			//PathfinderJNI.libLoaded = true;
			//System.out.println("Native Lib loaded");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Failed to load lib");
		}
		
		launch(args);
	}
}
