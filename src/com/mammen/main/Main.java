package com.mammen.main;
	
import com.mammen.util.NativeUtils;
import com.mammen.util.OSValidator;

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
			
			if (OSValidator.isWindows()) {
				NativeUtils.loadLibraryFromJar("/pathfinderjava.dll");
			} else if (OSValidator.isMac()) {
				NativeUtils.loadLibraryFromJar("/pathfinderjava.dylib");
			} else if (OSValidator.isUnix()) {
				NativeUtils.loadLibraryFromJar("/pathfinderjava.so");
			} else if (OSValidator.isSolaris()) {
				System.out.println("This is Solaris");
			} else {
				//display os not supported error message
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Failed to load lib");
		}
		
		launch(args);
	}
}
