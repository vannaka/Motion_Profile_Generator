package com.mammen.ui.javafx.dialog.settings;

import com.mammen.path.Path;
import com.mammen.settings.SettingsModel;
import com.mammen.settings.SourcePathDisplayType;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;

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
    private ChoiceBox<SourcePathDisplayType> choSourceDisplayType;

    @FXML
    private CheckBox chkAddWaypointOnClick;

    @FXML
    private ListView<Path.Elements> lst_availableElements, lst_chosenElements;

    private SettingsModel settings;

    private static DataFormat profileElementFormat = new DataFormat("com.mammen.path.Path.Elements" );

    @FXML
    private void initialize()
    {
        settings = SettingsModel.getInstance();

        btn_gen_styles();

        /******************************************************
         *   Setup ui elements
         ******************************************************/
        choSourceDisplayType.getItems().setAll( SourcePathDisplayType.values() );


        /******************************************************
         *   Setup bindings to the SettingsModel object
         ******************************************************/
        txtOverlayDir.textProperty().bindBidirectional( settings.graphBGImagePathProperty() );
        choSourceDisplayType.valueProperty().bindBidirectional( settings.sourcePathDisplayTypeProperty() );
        chkAddWaypointOnClick.selectedProperty().bindBidirectional( settings.addPointOnClickProperty() );
        lst_chosenElements.itemsProperty().bindBidirectional( settings.chosenCSVElementsProperty() );
        lst_availableElements.itemsProperty().bindBidirectional( settings.availableCSVElementsProperty() );

        // Set visibility of settings panels
        pnl_csv.setVisible( false );
        pnl_general.setVisible( true );


        lst_chosenElements.setCellFactory( lv ->
        {
            ListCell<Path.Elements> cell = new ListCell<Path.Elements>()
            {
                @Override
                protected void updateItem( Path.Elements item, boolean empty)
                {
                    super.updateItem( item, empty );
                    if( empty || item == null )
                    {
                        setText( null );
                    }
                    else
                    {
                        setText( item.toString() );
                    }
                }
            };

            cell.setOnDragOver( event ->
            {
                if( ( ( event.getGestureSource() == lst_chosenElements ) || ( event.getGestureSource() == lst_availableElements) )
                        && ( event.getDragboard().hasContent( profileElementFormat ) ) )
                {
                    event.acceptTransferModes( TransferMode.MOVE );
                }
            });

            cell.setOnDragDropped( event ->
            {
                Dragboard db = event.getDragboard();
                boolean success = false;
                if( db.hasContent( profileElementFormat ) )
                {
                    if( cell.isEmpty() )
                    {
                        lst_chosenElements.getItems().add( (Path.Elements)db.getContent( profileElementFormat ) );
                        lst_chosenElements.getItems().remove( Path.Elements.NULL );
                        success = true;
                    }
                    else
                    {
                        int index = cell.getIndex();

                        lst_chosenElements.getItems().remove( Path.Elements.NULL );

                        lst_chosenElements.getItems().add( index, (Path.Elements)db.getContent( profileElementFormat ) );
                        success = true;
                    }
                }

                event.setDropCompleted( success );
            });

            // highlight cells when drag target
            cell.setOnDragEntered( event -> cell.setStyle("-fx-border-color: DodgerBlue") );
            cell.setOnDragExited( event -> cell.setStyle("") );
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

    //Drag from available to chosen
    @FXML
    private void lst_aval_onDragDetected()
    {
        Dragboard db = lst_availableElements.startDragAndDrop( TransferMode.MOVE );
        ClipboardContent content = new ClipboardContent();
        content.put( profileElementFormat, lst_availableElements.getSelectionModel().getSelectedItem() );
        db.setContent( content );
    }

    @FXML
    private void lst_aval_onDragDone( DragEvent event )
    {
        if (event.getTransferMode() == TransferMode.MOVE)
        {
            if( lst_availableElements.getItems().size() == 1 )
            {
                lst_availableElements.getItems().remove(lst_availableElements.getSelectionModel().getSelectedIndex());
                lst_availableElements.getItems().add( Path.Elements.NULL );
                return;
            }
            lst_availableElements.getItems().remove(lst_availableElements.getSelectionModel().getSelectedIndex());
        }
    }

    /* Drag from chosen to available*/
    @FXML
    private void lst_chos_onDragDetected()
    {
        Dragboard db = lst_chosenElements.startDragAndDrop( TransferMode.MOVE );
        ClipboardContent content = new ClipboardContent();
        content.put( profileElementFormat, lst_chosenElements.getSelectionModel().getSelectedItem() );
        db.setContent( content );
    }

    @FXML
    private void lst_avail_onDragOver( DragEvent event )
    {
        if( ( event.getGestureSource() != lst_availableElements               )
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
            if( lst_availableElements.getItems().contains( Path.Elements.NULL ) )
            {
                lst_availableElements.getItems().remove( Path.Elements.NULL );
            }
            lst_availableElements.getItems().add( (Path.Elements)db.getContent( profileElementFormat ) );
            lst_availableElements.getItems().sort( Comparator.comparing( Path.Elements::ordinal ) );
            success = true;
        }
        /* let the source know whether the string was successfully
         * transferred and used */
        event.setDropCompleted( success );
    }

    @FXML
    private void lst_chos_onDragDone( DragEvent event )
    {
        if( event.getTransferMode() == TransferMode.MOVE )
        {
            if( lst_chosenElements.getItems().size() == 1 )
            {
                lst_chosenElements.getItems().remove( lst_chosenElements.getSelectionModel().getSelectedIndex() );
                lst_chosenElements.getItems().add( Path.Elements.NULL );
                return;
            }
            lst_chosenElements.getItems().remove( lst_chosenElements.getSelectionModel().getSelectedIndex() );
        }
    }

    /* Button Effects */
    @FXML
    private void btn_gen_styles()
    {
        btn_general.setStyle("-fx-background-color: DodgerBlue; " +
                "-fx-text-fill: #FFFFFF");

        btn_csv.setStyle("-fx-border-color: transparent; " +
                "-fx-border-width: 0; " +
                "-fx-background-radius: 0; " +
                "-fx-background-color: transparent;" +
                "-fx-text-fill: #000000");
    }

    @FXML
    private void btn_csv_styles()
    {
        btn_csv.setStyle("-fx-background-color: DodgerBlue; " +
                "-fx-text-fill: #FFFFFF");

        btn_general.setStyle("-fx-border-color: transparent; " +
                "-fx-border-width: 0; " +
                "-fx-background-radius: 0; " +
                "-fx-background-color: transparent;" +
                "-fx-text-fill: #000000");
    }

}
