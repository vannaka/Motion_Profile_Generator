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
			Pane root = FXMLLoader.load(getClass().getResource("/com/mammen/ui/javafx/MainUI.fxml"));
			// Scene scene = new Scene(root);
			// Dimension res = Toolkit.getDefaultToolkit().getScreenSize();
	        // int width = res.width - 269;
	        // int height = res.height - 111;
	        root.autosize();
	        
			primaryStage.setScene(new Scene(root));
			primaryStage.sizeToScene();
			primaryStage.setTitle("Motion Profile Generator");
			
			// primaryStage.setWidth(width);
	        // primaryStage.setHeight(height);
	        // primaryStage.centerOnScreen();

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
