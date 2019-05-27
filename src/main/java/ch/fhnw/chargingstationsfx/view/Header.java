package ch.fhnw.chargingstationsfx.view;

import ch.fhnw.chargingstationsfx.presentationmodel.RootPM;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class Header extends GridPane implements ViewMixin{
    private final RootPM rootPM;
    private VBox  vBox = new VBox();

    ///hier könnte ich das obere von Editor implementieren

    public Label type;
    public Label strasse;
    public Label plz;
    public Label ort;

    public Label anzahlLadepunkte;
    public Label anschlussLeistung;
    private Region spaceField;

   public Button karte;


    public Header(RootPM rootPM) {
        this.rootPM = rootPM;
        init();
    }

    @Override
    public void initializeControls() {
        type = new Label();
        strasse = new Label();
        plz = new Label();
        ort = new Label();
        anzahlLadepunkte = new Label();
        anschlussLeistung = new Label();
        spaceField = new Region();
    }

    @Override
    public void layoutControls() {
        setVgap(5);
        setHgap(10);


        ColumnConstraints noGrow = new ColumnConstraints();
        noGrow.setHgrow(Priority.NEVER);
        ColumnConstraints grow = new ColumnConstraints();
        grow.setHgrow(Priority.ALWAYS);

        //Anwenden auf 3 Spalten (0,1,2)
        getColumnConstraints().addAll(grow, noGrow, grow, noGrow);

        //Zeile wächst
        RowConstraints neverGrow = new RowConstraints();
        neverGrow.setVgrow(Priority.NEVER);
        //Anwenden auf alle 3 Zeilen (0,1,2)
        getRowConstraints().addAll(neverGrow, neverGrow, neverGrow);


        add(type,0,0);
        type.setId("button-id");
        add(strasse,0,1);
        add(plz, 0,2);
        //ToDo, zur Zeit übereinander, wie schaffe ich abstand oder beides in eine Spalte?
        add(ort,0,2);
        ort.setId("button-ort");
        add(spaceField, 0,3,2,1);
        add(anzahlLadepunkte,0,5);
        add(anschlussLeistung,0,6);

        karte = new Button();
        karte.setMaxSize(2000, 2000);
        add(karte, 2,1,3,4);
        karte.setId("button-karte");


    }

    @Override
    public void initializeSelf(){
        String stylesheet = getClass().getResource("style.css").toExternalForm();
        getStylesheets().add(stylesheet);
    }

    @Override
    public void setupBindings(){
        type.textProperty().bind(rootPM.getLadestationProxy().loaderTypeProperty());
        strasse.textProperty().bind(rootPM.getLadestationProxy().strasseNameProperty());
      //  ort.textProperty().bindBidirectional(rootPM.getLadestationProxy().ortProperty());
        plz.textProperty().bind(rootPM.getLadestationProxy().PLZProperty().asString().concat("  ").concat(rootPM.getLadestationProxy().ortProperty()));
        anzahlLadepunkte.textProperty().bind(rootPM.getLadestationProxy().numberOfChargingPointsProperty().asString().concat(" Ladestation(en)"));
        anschlussLeistung.textProperty().bind(rootPM.getLadestationProxy().connectionPowerKwProperty().asString("%.2f KW"));


    }


}
