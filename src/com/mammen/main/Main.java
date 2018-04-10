package com.mammen.main;
	
import java.awt.Dimension;
import java.awt.Toolkit;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.fxml.FXMLLoader;


public class Main extends Application 
{
	@Override
	public void start(Stage primaryStage) 
	{
		try
		{
			VBox root = (VBox)FXMLLoader.load(getClass().getResource("/com/mammen/ui/javafx/MainUI.fxml"));
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
		launch(args);
	}
}
