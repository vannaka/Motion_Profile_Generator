package com.mammen.ui.javafx.dialog.factory;

import java.io.PrintWriter;
import java.io.StringWriter;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class AlertFactory 
{
	public static Alert createExceptionAlert( Exception e )
    {
        return createExceptionAlert( e, e.getMessage() );
    }

    public static Alert createExceptionAlert( Exception e, String msg )
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Exception Dialog");
        alert.setHeaderText("Whoops!");
        alert.setContentText(msg);

        // Create expandable Exception.
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label("The exception stacktrace was:");

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        // Set expandable Exception into the dialog pane.
        alert.getDialogPane().setExpandableContent(expContent);

        return alert;
    }
    
    public static Alert createInvalidOSAlert( String msg )
    {
    	Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid OS");
        alert.setHeaderText("Whoops!");
        alert.setContentText(msg);
    	
    	return alert;
    }
}
