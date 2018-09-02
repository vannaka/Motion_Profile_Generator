package com.mammen.ui.javafx.dialog;

import com.mammen.ui.javafx.PropWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.beans.value.ObservableValue;

import java.io.File;
import java.util.Properties;

public class SettingsDialogController {
    @FXML
    private Pane root, pnl_general, pnl_csv;

    @FXML
    private TextField txtOverlayDir;

    @FXML
    private Button btnChooseOverlay, btn_general, btn_csv;

    @FXML
    private ChoiceBox<String> choSourceDisplay;

    @FXML
    private ChoiceBox<String> choCSVType;

    @FXML
    private CheckBox chkAddWaypointOnClick;

    @FXML
    private ListView lst_availabel_vals, lst_chosen_vals;

    private Properties properties;

    @FXML
    private void initialize()
    {
        properties = PropWrapper.getProperties();

        txtOverlayDir.setText(properties.getProperty("ui.overlayDir", ""));

        choSourceDisplay.setItems(FXCollections.observableArrayList("None", "Waypoints only", "Waypoints + Source"));
        choSourceDisplay.getSelectionModel().select(Integer.parseInt(properties.getProperty("ui.sourceDisplay", "2")));

        choCSVType.setItems(FXCollections.observableArrayList("Jaci", "Talon SRX", "Custom"));
        choCSVType.getSelectionModel().select(Integer.parseInt(properties.getProperty("ui.csvType", "0")));

        chkAddWaypointOnClick.setSelected(Boolean.parseBoolean(properties.getProperty("ui.addWaypointOnClick", "false")));

        lst_availabel_vals.setItems(FXCollections.observableArrayList("Delta Time", "X Point", "Y Point", "Position", "Velocity", "Acceleration", "Jerk", "Heading"));

        pnl_csv.setVisible(false);
        pnl_general.setVisible(true);

        if( (choCSVType.getSelectionModel().getSelectedItem() ).toUpperCase().equals("CUSTOM") )
        {
            lst_availabel_vals.setDisable(false);
            lst_chosen_vals.setDisable(false);
        }
        else
        {
            lst_availabel_vals.setDisable(true);
            lst_chosen_vals.setDisable(true);
        }

        choCSVType.getSelectionModel().selectedItemProperty().addListener( this::disableSettings );
    }

    @FXML
    private void showChooseOverlayDialog()
    {
        FileChooser fileChooser = new FileChooser();

        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        fileChooser.setTitle("Find Position Map Overlay");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(
                        "Image Files",
                        "*.jpg",
                        "*.jpeg",
                        "*.png"
                )
        );

        File result = fileChooser.showOpenDialog(root.getScene().getWindow());

        if (result != null && result.exists() && !result.isDirectory()) {
            txtOverlayDir.setText(result.getAbsolutePath());
        }
    }

    @FXML
    private void showGeneralSettings()
    {
        pnl_general.toFront();
        pnl_csv.setVisible(false);
        pnl_general.setVisible(true);
    }

    @FXML
    private void showCSVSettings()
    {
        pnl_csv.toFront();
        pnl_csv.setVisible(true);
        pnl_general.setVisible(false);
    }

    @FXML
    private void disableSettings( ObservableValue<? extends String> observable, Object oldValue, Object newValue )
    {
        if( ( (String)newValue ).toUpperCase().equals("CUSTOM") )
        {
            lst_availabel_vals.setDisable(false);
            lst_chosen_vals.setDisable(false);
        }
        else
        {
            lst_availabel_vals.setDisable(true);
            lst_chosen_vals.setDisable(true);
        }
    }

    @FXML
    private void lst_aval_onDragDetected()
    {
        Dragboard db = lst_availabel_vals.startDragAndDrop( TransferMode.MOVE );
        ClipboardContent content = new ClipboardContent();
        content.putString( lst_availabel_vals.getSelectionModel().getSelectedItems().toString() );
        db.setContent( content );
    }

    @FXML
    private void lst_chos_onDragOver( DragEvent event )
    {
        if( ( event.getGestureSource() != lst_chosen_vals )
         && ( event.getDragboard().hasString()            ) )
        {
            event.acceptTransferModes( TransferMode.MOVE );
        }
    }

    @FXML
    private void lst_chos_onDragEnter( DragEvent event )
    {
        if( ( event.getGestureSource() != lst_chosen_vals )
         && ( event.getDragboard().hasString()            ) )
        {
            // Set blue border
            lst_chosen_vals.setStyle( "-fx-border-color: DodgerBlue" );
        }
    }

    @FXML
    private void lst_chos_onDragExit( DragEvent event )
    {
        if( ( event.getGestureSource() != lst_chosen_vals )
         && ( event.getDragboard().hasString()            ) )
        {
            // Remove blue border
            lst_chosen_vals.setStyle("");
        }
    }

    @FXML
    private void lst_chos_onDragDrop( DragEvent event )
    {
        /* data dropped */
        /* if there is a string data on dragboard, read it and use it */
        Dragboard db = event.getDragboard();
        boolean success = false;
        if( db.hasString() )
        {
            lst_chosen_vals.getItems().add( db.getString() );
            success = true;
        }
        /* let the source know whether the string was successfully
         * transferred and used */
        event.setDropCompleted( success );
    }

    @FXML
    private void lst_aval_onDragDone( DragEvent event )
    {
        if (event.getTransferMode() == TransferMode.MOVE) {
            lst_availabel_vals.getItems().remove( lst_availabel_vals.getSelectionModel().getSelectedIndex() );
        }
    }

}
