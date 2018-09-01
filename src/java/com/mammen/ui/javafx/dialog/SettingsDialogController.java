package com.mammen.ui.javafx.dialog;

import com.mammen.ui.javafx.PropWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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
    private ListView lstview_1, lstview_2;

    private Properties properties;

    @FXML
    private void initialize() {
        properties = PropWrapper.getProperties();

        txtOverlayDir.setText(properties.getProperty("ui.overlayDir", ""));

        choSourceDisplay.setItems(FXCollections.observableArrayList("None", "Waypoints only", "Waypoints + Source"));
        choSourceDisplay.getSelectionModel().select(Integer.parseInt(properties.getProperty("ui.sourceDisplay", "2")));

        choCSVType.setItems(FXCollections.observableArrayList("Jaci", "Talon SRX", "Custom"));
        choCSVType.getSelectionModel().select(Integer.parseInt(properties.getProperty("ui.csvType", "0")));

        chkAddWaypointOnClick.setSelected(Boolean.parseBoolean(properties.getProperty("ui.addWaypointOnClick", "false")));

        lstview_1.setItems(FXCollections.observableArrayList("Delta Time", "X Point", "Y Point", "Position", "Velocity", "Acceleration", "Jerk", "Heading"));

        pnl_csv.setVisible(false);
        pnl_general.setVisible(true);

        if( (choCSVType.getSelectionModel().getSelectedItem() ).toUpperCase().equals("CUSTOM") )
        {
            lstview_1.setDisable(false);
            lstview_2.setDisable(false);
        }
        else
        {
            lstview_1.setDisable(true);
            lstview_2.setDisable(true);
        }

        choCSVType.getSelectionModel().selectedItemProperty().addListener( this::disableSettings );
    }

    @FXML
    private void showChooseOverlayDialog() {
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
    private void showGeneralSettings(){
        pnl_general.toFront();
        pnl_csv.setVisible(false);
        pnl_general.setVisible(true);
    }

    @FXML
    private void showCSVSettings() {
        pnl_csv.toFront();
        pnl_csv.setVisible(true);
        pnl_general.setVisible(false);
    }

    @FXML
    private void disableSettings( ObservableValue<? extends String> observable, Object oldValue, Object newValue )
    {
        if( ( (String)newValue ).toUpperCase().equals("CUSTOM") )
        {
            lstview_1.setDisable(false);
            lstview_2.setDisable(false);
        }
        else
        {
            lstview_1.setDisable(true);
            lstview_2.setDisable(true);
        }
    }
}
