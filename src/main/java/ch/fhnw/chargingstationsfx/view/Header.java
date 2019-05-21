package ch.fhnw.chargingstationsfx.view;

import ch.fhnw.chargingstationsfx.presentationmodel.RootPM;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.List;

public class Header extends VBox implements ViewMixin{
    private final RootPM rootPM;

    ///hier könnte ich das obere von Editor implementieren

    public Label titel;
    public Label strasse;
    public Label ort;

    public Header(RootPM rootPM) {
        this.rootPM = rootPM;
        init();
    }

    @Override
    public void initializeControls() {
        titel = new Label("Beispiel Typ");
        strasse = new Label("Beispiel Strasse");
        ort = new Label("Beispiel Ort");
    }

    @Override
    public void layoutControls() {

    }

   }
