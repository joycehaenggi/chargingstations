package ch.fhnw.chargingstationsfx.view;

import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import javax.swing.*;

//GridPane Layout verwenden, für die Anordnung von "top" in BorderPane
//TODO HBOX
public class LayoutTop extends HBox {

    //Deklaration
    private Button saveButton;
    private Button addButton;
    private Button deleteButton;
    private TextField searchField;
    private Region spaceField;


    public LayoutTop() {
        initializeSelf();
        initializeControls();
        layoutControls();


    }

    private void initializeControls() {
        //Initialisierung
        saveButton = new Button();
        addButton = new Button ("add");
        deleteButton = new Button();
        deleteButton.maxWidth(1048);
        searchField = new TextField();
        spaceField = new Region();

    }

    private void layoutControls() {

        HBox.setHgrow(spaceField, Priority.ALWAYS);
        saveButton.setMaxSize(200,200);
        saveButton.setId("button-save");
        deleteButton.setId("button-delete");
        getChildren().addAll(saveButton,addButton,deleteButton,spaceField, searchField);
        setSpacing(10);


    }

    private void initializeSelf(){
        String stylesheet = getClass().getResource("style.css").toExternalForm();
        getStylesheets().add(stylesheet);
    }


}


