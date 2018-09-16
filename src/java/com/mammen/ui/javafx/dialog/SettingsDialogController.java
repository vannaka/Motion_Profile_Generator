package com.mammen.ui.javafx.dialog;

import com.mammen.main.ProfileGenerator;
import com.mammen.ui.javafx.PropWrapper;
import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.beans.value.ObservableValue;

import java.io.File;
import java.util.*;

public class SettingsDialogController
{
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
    private ListView<ProfileGenerator.ProfileElements> lst_availabel_vals, lst_chosen_vals;

    private Properties properties;

    private static DataFormat profileElementFormat = new DataFormat("com.mammen.ProfileGenerator.ProfileElements");

    @FXML
    private void initialize()
    {
        properties = PropWrapper.getProperties();

        txtOverlayDir.setText( properties.getProperty("ui.overlayDir", "") );

        choSourceDisplay.setItems( FXCollections.observableArrayList("None", "Waypoints only", "Waypoints + Source") );
        choSourceDisplay.getSelectionModel().select( Integer.parseInt( properties.getProperty("ui.sourceDisplay", "2") ) );

        choCSVType.setItems( FXCollections.observableArrayList("Jaci", "Talon SRX", "Custom") );
        choCSVType.getSelectionModel().select( Integer.parseInt( properties.getProperty("ui.csvType", "0") ) );

        chkAddWaypointOnClick.setSelected( Boolean.parseBoolean( properties.getProperty("ui.addWaypointOnClick", "false") ) );

        List<String> s_ListAvail = new LinkedList<String>(Arrays.asList(properties.getProperty("csv.avail").split(",")));

        List<ProfileGenerator.ProfileElements> p_ListAvail = new ArrayList<>();

        if(s_ListAvail.size() > 1) {
            for (int i = 0; i < s_ListAvail.size(); i++) {
                String s = s_ListAvail
                        .get(i)
                        .toUpperCase()
                        .trim()
                        .replace(" ", "_");
                ProfileGenerator.ProfileElements s1 = ProfileGenerator.ProfileElements.valueOf(s);
                p_ListAvail.add(i, s1);
            }
            lst_availabel_vals.getItems().setAll(p_ListAvail);
        }else {
            lst_availabel_vals.getItems().setAll(ProfileGenerator.ProfileElements.NULL);
        }

        List<String> s_ListChose = new LinkedList<String>(Arrays.asList(properties.getProperty("csv.chos").split(",")));

        List<ProfileGenerator.ProfileElements> p_ListChose = new ArrayList<>();

        if(s_ListChose.size() > 1 || !s_ListChose.contains(null)) {

            for (int i = 0; i < s_ListChose.size(); i++) {
                String s = s_ListChose
                        .get(i)
                        .toUpperCase()
                        .trim()
                        .replace(" ", "_");
                ProfileGenerator.ProfileElements s1 = ProfileGenerator.ProfileElements.valueOf(s);
                p_ListChose.add(i, s1);
            }
            lst_chosen_vals.getItems().setAll(p_ListChose);
        }else {
            lst_chosen_vals.getItems().setAll(ProfileGenerator.ProfileElements.NULL);
        }

        pnl_csv.setVisible( false );
        pnl_general.setVisible( true );

        if( choCSVType.getSelectionModel().getSelectedItem().toUpperCase().equals("CUSTOM") )
        {
            lst_availabel_vals.setDisable( false );
            lst_chosen_vals.setDisable( false );
        }
        else
        {
            lst_availabel_vals.setDisable(  true );
            lst_chosen_vals.setDisable( true );
        }

        choCSVType.getSelectionModel().selectedItemProperty().addListener( this::disableSettings );

        lst_chosen_vals.setCellFactory(lv -> {
            ListCell<ProfileGenerator.ProfileElements> cell = new ListCell<ProfileGenerator.ProfileElements>() {

                @Override
                protected void updateItem(ProfileGenerator.ProfileElements item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.toString());
                    }
                }
            };
            cell.setOnDragOver(event -> {
                if( ( event.getGestureSource() != lst_chosen_vals               )
                        && ( event.getDragboard().hasContent( profileElementFormat ) ) )
                {
                    event.acceptTransferModes( TransferMode.MOVE );
                }
            });
            cell.setOnDragDropped(event -> {
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasContent( profileElementFormat )) {
                    if (cell.isEmpty()) {
                        lst_chosen_vals.getItems().add( (ProfileGenerator.ProfileElements)db.getContent( profileElementFormat ) );
                        lst_chosen_vals.getItems().remove(ProfileGenerator.ProfileElements.NULL);
                        success = true;
                    } else {
                        int index = cell.getIndex();
                        if (lst_chosen_vals.getItems().contains(ProfileGenerator.ProfileElements.NULL)){
                            lst_chosen_vals.getItems().remove(ProfileGenerator.ProfileElements.NULL);
                        }
                        lst_chosen_vals.getItems().add(index, (ProfileGenerator.ProfileElements)db.getContent( profileElementFormat ));
                        success = true;
                    }
                    event.setDropCompleted( success);
                }
            });

            // highlight cells when drag target
            cell.setOnDragEntered(event -> cell.setStyle("-fx-border-color: DodgerBlue"));
            cell.setOnDragExited(event -> cell.setStyle(""));
            return cell ;
        });
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

    //Drag from avialable to chosen

    @FXML
    private void lst_aval_onDragDetected()
    {
        Dragboard db = lst_availabel_vals.startDragAndDrop( TransferMode.MOVE );
        ClipboardContent content = new ClipboardContent();
        //content.putString( lst_availabel_vals.getSelectionModel().getSelectedItems().toString() );
        content.put( profileElementFormat, lst_availabel_vals.getSelectionModel().getSelectedItem() );
        db.setContent( content );
    }

    @FXML
    private void lst_aval_onDragDone( DragEvent event )
    {
        /*
        if (event.getTransferMode() == TransferMode.MOVE) {
            lst_availabel_vals.getItems().remove( lst_availabel_vals.getSelectionModel().getSelectedIndex() );
        }*/

        if (event.getTransferMode() == TransferMode.MOVE) {
            if(lst_availabel_vals.getItems().size() == 1) {
                lst_availabel_vals.getItems().remove(lst_availabel_vals.getSelectionModel().getSelectedIndex());
                lst_availabel_vals.getItems().add(ProfileGenerator.ProfileElements.NULL);
                return;
            }
            lst_availabel_vals.getItems().remove(lst_availabel_vals.getSelectionModel().getSelectedIndex());
        }
    }

    /* Drag from chosen to available*/

    @FXML
    private void lst_chos_onDragDetected()
    {
        Dragboard db = lst_chosen_vals.startDragAndDrop( TransferMode.MOVE );
        ClipboardContent content = new ClipboardContent();
        //content.putString( lst_availabel_vals.getSelectionModel().getSelectedItems().toString() );
        content.put( profileElementFormat, lst_chosen_vals.getSelectionModel().getSelectedItem() );
        db.setContent( content );
    }

    @FXML
    private void lst_avail_onDragOver( DragEvent event )
    {
        if( ( event.getGestureSource() != lst_availabel_vals               )
                && ( event.getDragboard().hasContent( profileElementFormat ) ) )
        {
            event.acceptTransferModes( TransferMode.MOVE );
        }
    }

    @FXML
    private void lst_avail_onDragDrop( DragEvent event )
    {
        /* data dropped */
        /* if there is a string data on dragboard, read it and use it */
        Dragboard db = event.getDragboard();
        boolean success = false;
        if( db.hasContent( profileElementFormat ) )
        {
            if (lst_availabel_vals.getItems().contains(ProfileGenerator.ProfileElements.NULL)){
                lst_availabel_vals.getItems().remove(ProfileGenerator.ProfileElements.NULL);
            }
            lst_availabel_vals.getItems().add((ProfileGenerator.ProfileElements)db.getContent( profileElementFormat ));
            success = true;
        }
        /* let the source know whether the string was successfully
         * transferred and used */
        event.setDropCompleted( success );
    }

    @FXML
    private void lst_chos_onDragDone( DragEvent event )
    {
        if (event.getTransferMode() == TransferMode.MOVE) {
            if(lst_chosen_vals.getItems().size() == 1) {
                lst_chosen_vals.getItems().remove(lst_chosen_vals.getSelectionModel().getSelectedIndex());
                lst_chosen_vals.getItems().add(ProfileGenerator.ProfileElements.NULL);
                return;
            }
            lst_chosen_vals.getItems().remove(lst_chosen_vals.getSelectionModel().getSelectedIndex());
        }
    }

}
