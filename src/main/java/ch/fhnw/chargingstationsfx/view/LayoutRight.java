package ch.fhnw.chargingstationsfx.view;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;



public class LayoutRight extends GridPane {
        public Label titel;
        public Label strasse;
        public Label ort;
        public Label anzahlLadepunkte;
        public Label anschlussLeistung;
        public Label betreiberLabel;
        public Label strasseLabel;
        public Label PLZLabel;
        public Label ortLabel;
        public Label longitudeLabel;
        public Label latitudeLabel;
        public Label inbetriebnahmeLabel;
        public Label loaderTypeLabel;
        public Label numberOfChargingPointsLabel;
        public Label connectionPowerKwLabel;
        public Label plugType1Label;
        public Label power1KwLabel;
        public Label plugType2Label;
        public Label power2KwLabel;
        public Label plugType3Label;
        public Label power3KwLabel;
        public Label plugType4Label;
        public Label power4KwLabel;




        public TextField betreiberTextField;
        public TextField strasseTextField;
        public TextField PLZTextField;
        public TextField ortTextField;
        public TextField longitudeTextField;
        public TextField latitudeTextField;
        public TextField inbetriebnahmeTextField;
        public TextField loaderTypeTextField;
        public TextField numberOfChargingPointsTextField;
        public TextField connectionPowerKwTextField;
        public TextField plugType1TextField;
        public TextField power1KwTextField;
        public TextField plugType2TextField;
        public TextField power2KwTextField;
        public TextField plugType3TextField;
        public TextField power3KwTextField;
        public TextField plugType4TextField;
        public TextField power4KwTextField;



    public LayoutRight() {
        initializeControls();
        layoutControls();
    }

    private void initializeControls() {
        //Initialisierung
        //ToDo Titel Binding mit Typ!!
        titel = new Label("Schnellladeeinrichtung");
        strasse = new Label();
        ort = new Label();
        anzahlLadepunkte = new Label();
        anschlussLeistung = new Label();
        betreiberLabel = new Label("Betreiber");
        strasseLabel = new Label("Strasse*");
        PLZLabel = new Label("PLZ*");
        ortLabel = new Label("Ort*");
        longitudeLabel = new Label("Längengrad");
        latitudeLabel = new Label("Breitengrad");
        inbetriebnahmeLabel = new Label ("Inbetriebnahme");
        loaderTypeLabel = new Label ("Typ");
        numberOfChargingPointsLabel = new Label ("Anzahl Ladepunkte");
        connectionPowerKwLabel = new Label("Anschlussleistung [kW]");
        plugType1Label = new Label ("1-Steckertypen");
        power1KwLabel = new Label ("Leistung [kW]");
        plugType2Label = new Label ("2-Steckertypen");
        power2KwLabel = new Label ("Leistung [kW]");
        plugType3Label = new Label ("3-Steckertypen");
        power3KwLabel = new Label ("Leistung [kW]");
        plugType4Label = new Label ("4-Steckertypen");
        power4KwLabel = new Label ("Leistung [kW]");


        betreiberTextField = new TextField();
        strasseTextField = new TextField();
        PLZTextField = new TextField();
        ortTextField = new TextField();
        longitudeTextField = new TextField();
        latitudeTextField = new TextField();
        inbetriebnahmeTextField = new TextField();
        loaderTypeTextField = new TextField();
        numberOfChargingPointsTextField = new TextField();
        connectionPowerKwTextField = new TextField();
        plugType1TextField = new TextField();
        power1KwTextField = new TextField();
        plugType2TextField = new TextField();
        power2KwTextField = new TextField();
        plugType3TextField = new TextField();
        power3KwTextField = new TextField();
        plugType4TextField = new TextField();
        power4KwTextField = new TextField();






    }

    private void layoutControls() {
        setVgap(5);
        setHgap(10);


        ColumnConstraints noGrow = new ColumnConstraints();
        noGrow.setHgrow(Priority.NEVER);

        //Anwenden auf 3 Spalten (0,1,2)
        getColumnConstraints().addAll(noGrow, noGrow, noGrow);

        //Zeile wächst
        RowConstraints neverGrow = new RowConstraints();
        neverGrow.setVgrow(Priority.NEVER);
        //Anwenden auf alle 3 Zeilen (0,1,2)
        getRowConstraints().addAll(neverGrow, neverGrow, neverGrow);


        //saveButton soll im Grid mit Spalte 0 und Zeile 0 hinzugefügt werden
        add(titel,0,0,3,1);
        add(strasse,0,1);
        add(ort,0,2);
        add(anzahlLadepunkte,0,4);
        add(anschlussLeistung,0,5);
        add(betreiberLabel,0,7);
        add(strasseLabel,0,8);
        add(PLZLabel, 0,9);
        add(ortLabel,2,9);
        add(longitudeLabel,0,10);
        add(latitudeLabel,2,10);
        add(inbetriebnahmeLabel,0,11);
        add(loaderTypeLabel, 2,11);
        add(numberOfChargingPointsLabel, 0,12);
        add(connectionPowerKwLabel, 2,12);
        add(plugType1Label, 0,13);
        add(power1KwLabel, 2,13);
        add(plugType2Label, 0,14);
        add(power2KwLabel,2,14);
        add(plugType3Label, 0,15);
        add(power3KwLabel,2,15);
        add(plugType4Label, 0,16);
        add(power4KwLabel,2,16);



        //ToDo muss über mehrere Spalte gehen
        add(betreiberTextField,1,7,3,1);
        //ToDo muss über mehrere Spalte gehen
        add(strasseTextField,1,8,3,1);
        add(PLZTextField,1,9);
        add(ortTextField,3,9);
        add(longitudeTextField,1,10);
        add(latitudeTextField,3,10);
        add(inbetriebnahmeTextField,1,11);
        add(loaderTypeTextField,3,11);
        add(numberOfChargingPointsTextField, 1,12);
        add(connectionPowerKwTextField, 3,12);
        add(plugType1TextField,1,13);
        add(power1KwTextField,3,13);
        add(plugType2TextField,1,14);
        add(power2KwTextField,3,14);
        add(plugType3TextField,1,15);
        add(power3KwTextField,3,15);
        add(plugType4TextField,1,16);
        add(power4KwTextField,3,16);





    }

    private Button createButton(String text){
        Button button = new Button(text);
        button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        return button;
    }

}



