package ch.fhnw.chargingstationsfx.view;

import ch.fhnw.chargingstationsfx.presentationmodel.Ladestation;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;

import ch.fhnw.chargingstationsfx.presentationmodel.RootPM;

public class RootPanelUI extends BorderPane implements ViewMixin {
    private final RootPM rootPM;
    private TableView<Ladestation> tabelle;
    private Object GridPaneStarter;


    public RootPanelUI(RootPM model) {
        this.rootPM = model;

        init();
    }

    @Override
    public void initializeSelf() {
        addStylesheetFiles("style.css");
    }

    @Override
    public void initializeControls() {


        tabelle = new TableView<>(rootPM.getResultate());

        //Im Konstruktor wird die Überschrift der Spalte angegeben
        TableColumn<Ladestation, String> strasseCol = new TableColumn<>("Strasse");
        strasseCol.setCellValueFactory(cel -> cel.getValue().strasseNameProperty());

        TableColumn<Ladestation, Number> PLZCol = new TableColumn<>("PLZ");
        PLZCol.setCellValueFactory(cel -> cel.getValue().PLZProperty());

        TableColumn<Ladestation, String> ortCole = new TableColumn<>("Ort");
        ortCole.setCellValueFactory(cel -> cel.getValue().ortProperty());

        TableColumn<Ladestation, Number> anzahlLadestationCol = new TableColumn<>("Anzahl Ladestationen");
        anzahlLadestationCol.setCellValueFactory(cel -> cel.getValue().numberOfChargingPointsProperty());

        tabelle.getColumns().addAll(strasseCol, PLZCol, ortCole, anzahlLadestationCol);


    }

    @Override
    public void layoutControls() {
        //setVgrow(tabelle, Priority.ALWAYS);

        //getChildren().addAll(tabelle);
        setTop(new LayoutTop());
        setLeft(tabelle);
        setRight(new LayoutRight());
        setCenter(new LayoutCenter());


    }

//    @Override
//    public void setupBindings() {
//        button.textProperty().bind(rootPM.greetingProperty());
//    }
}
