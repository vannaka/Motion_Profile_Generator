package com.mammen.ui.javafx.factory;

import java.awt.Toolkit;

import com.mammen.ui.javafx.ResourceLoader;
import com.mammen.ui.javafx.dialog.AddWaypointDialogController;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Waypoint;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;

public class DialogFactory
{
	private DialogFactory() { }

    public static Dialog<Boolean> createAboutDialog() {
        Dialog<Boolean> dialog = new Dialog<>();

        try {
            FXMLLoader loader = new FXMLLoader(
                    ResourceLoader.getResource("/com/mammen/ui/javafx/dialog/AboutDialog.fxml")
            );

            dialog.setDialogPane(loader.load());

            dialog.setResultConverter((ButtonType buttonType) ->
                    buttonType.getButtonData() == ButtonBar.ButtonData.CANCEL_CLOSE
            );
        } catch (Exception e) {
            e.printStackTrace();
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        }

        dialog.setTitle("About");

        return dialog;
    }

    public static Dialog<Boolean> createSettingsDialog() {
        Dialog<Boolean> dialog = new Dialog<>();

        try {
            FXMLLoader loader = new FXMLLoader(
                    ResourceLoader.getResource("/com/mammen/ui/javafx/dialog/SettingsDialog.fxml")
            );

            dialog.setDialogPane(loader.load());

            ((Button) dialog.getDialogPane().lookupButton(ButtonType.APPLY)).setDefaultButton(true);
            ((Button) dialog.getDialogPane().lookupButton(ButtonType.CANCEL)).setDefaultButton(false);

            // Some header stuff
            dialog.setTitle("Settings");
            dialog.setHeaderText("Manage settings");

            dialog.setResultConverter((ButtonType buttonType) ->
                    buttonType.getButtonData() == ButtonBar.ButtonData.APPLY
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dialog;
    }

    public static Dialog<Waypoint> createWaypointDialog(String xPos, String yPos) {
        Dialog<Waypoint> dialog = new Dialog<>();

        try {
            FXMLLoader loader = new FXMLLoader(
                    ResourceLoader.getResource("/com/mammen/ui/javafx/dialog/AddWaypointDialog.fxml")
            );
            ButtonType add = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
            DialogPane root = loader.load();

            AddWaypointDialogController controller = null;
            TextField txtWX, txtWY, txtWA;

            dialog.setDialogPane(root);

            controller = loader.getController();

            txtWX = controller.getTxtWX();
            txtWY = controller.getTxtWY();
            txtWA = controller.getTxtWA();

            txtWX.setText(xPos);
            txtWY.setText(yPos);

            // Some header stuff
            dialog.setTitle("Add Waypoint");
            dialog.setHeaderText("Add a new waypoint");

            dialog.getDialogPane().getButtonTypes().add(add);

            dialog.setResultConverter((ButtonType buttonType) -> {
                if (buttonType.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                    double
                        x = Double.parseDouble(txtWX.getText().trim()),
                        y = Double.parseDouble(txtWY.getText().trim()),
                        angle = Double.parseDouble(txtWA.getText().trim());

                    return new Waypoint(x, y, Pathfinder.d2r(angle));
                }

                return null;
            });

            root.lookupButton(add).addEventFilter(ActionEvent.ACTION, ae -> {
                try {
                    Double.parseDouble(txtWX.getText().trim());
                    Double.parseDouble(txtWY.getText().trim());
                    Double.parseDouble(txtWA.getText().trim());
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);

                    alert.setTitle("Invalid Point!");
                    alert.setHeaderText("Invalid point input!");
                    alert.setContentText("Please check your fields and try again.");

                    Toolkit.getDefaultToolkit().beep();
                    alert.showAndWait();
                    ae.consume();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        }

        return dialog;
    }

    public static Dialog<Waypoint> createWaypointDialog() { return createWaypointDialog("", ""); }
}
