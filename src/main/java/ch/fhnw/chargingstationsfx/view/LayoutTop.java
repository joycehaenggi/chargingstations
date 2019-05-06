package ch.fhnw.chargingstationsfx.view;

import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

import javax.swing.*;

//GridPane Layout verwenden, für die Anordnung von "top" in BorderPane
//TODO HBOX
public class LayoutTop extends GridPane {

    //Deklaration
    private Button saveButton;

    private Button addButton;
    private Button deleteButton;


    public LayoutTop() {
        initializeControls();
        layoutControls();
    }

    private void initializeControls() {
        //Initialisierung
        saveButton = createButton("save");
        addButton = createButton("add");
        deleteButton = createButton("delete");

    }

    private void layoutControls() {
        //Spalte wächst horizontal
                //Spezielle Spalte darf nicht wachsen
        ColumnConstraints noGrow = new ColumnConstraints();
        noGrow.setHgrow(Priority.NEVER);

        //Anwenden auf 3 Spalten (0,1,2)
        getColumnConstraints().addAll(noGrow, noGrow, noGrow);

        //Zeile wächst
        RowConstraints neverGrow = new RowConstraints();
        neverGrow.setVgrow(Priority.ALWAYS);
        //Anwenden auf alle 3 Zeilen (0,1,2)
        getRowConstraints().addAll(neverGrow, neverGrow, neverGrow);


        //saveButton soll im Grid mit Spalte 0 und Zeile 0 hinzugefügt werden
        add(saveButton,0,0);
        add(addButton, 1, 0);
        add(deleteButton, 2, 0);

    }

    private Button createButton(String text){
        Button button = new Button(text);
        button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        return button;
    }
}


