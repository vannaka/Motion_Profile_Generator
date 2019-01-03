package com.mammen.ui.javafx.dialog.add_waypoint;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.DoubleStringConverter;

public class AddWaypointDialogController
{
	@FXML
    private TextField
        txtWX, // X-Coord
        txtWY, // Y-Coord
        txtWA; // Angle

    @FXML
    private void initialize() {
        txtWX.setTextFormatter( new TextFormatter<>( new DoubleStringConverter() ) );
        txtWY.setTextFormatter( new TextFormatter<>( new DoubleStringConverter() ) );
        txtWA.setTextFormatter( new TextFormatter<>( new DoubleStringConverter() ) );
    }

    public TextField getTxtWX()
    {
        return txtWX;
    }

    public TextField getTxtWY()
    {
        return txtWY;
    }

    public TextField getTxtWA()
    {
        return txtWA;
    }
}
