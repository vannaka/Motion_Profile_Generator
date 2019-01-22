package com.mammen.ui.javafx.dialog.about;

import com.mammen.util.ResourceLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;

import java.awt.*;
import java.net.URI;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

/**
 * Controller for the About dialog
 * Mainly just to initialize the links and version number
 */
public class AboutDialogController {
    @FXML
    private Label lblVersion;

    @FXML
    private Hyperlink
            hlLukeGithub,
            hlBlakeGithub,
            hlPathfinder,
            hlMITLicense;

    @FXML
    private Button btnViewRepo;

    @FXML
    private void initialize() {
        Manifest manifest = ResourceLoader.getManifest();
        String versionNum = "4.0.1";

        if (manifest != null) {
            Attributes mfAttr = manifest.getMainAttributes();
            String mfVersion = mfAttr.getValue("Version");

            if (mfVersion != null)
                versionNum = mfVersion;
        }

        lblVersion.setText("v" + versionNum);

        hlLukeGithub.setOnAction((ActionEvent e) -> openLink("https://github.com/vannaka"));
        hlBlakeGithub.setOnAction((ActionEvent e) -> openLink("https://github.com/blake1029384756"));
        hlPathfinder.setOnAction((ActionEvent e) -> openLink("https://github.com/JacisNonsense/Pathfinder"));
        hlMITLicense.setOnAction((ActionEvent e) -> openLink("https://opensource.org/licenses/MIT"));

        btnViewRepo.setOnAction((ActionEvent e) -> openLink("https://github.com/vannaka/Motion_Profile_Generator"));
    }

    private void openLink(String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
