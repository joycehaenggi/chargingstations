package ch.fhnw.chargingstationsfx.view;

import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

import ch.fhnw.chargingstationsfx.presentationmodel.RootPM;
import javafx.scene.layout.VBox;

public class RootPanelUI extends BorderPane implements ViewMixin {
    private final RootPM rootPM;
    private SplitPane splitPane = new SplitPane();
    private Editor editor;
    private Overview overview;
    private Toolbar toolbar;
    private Header header;


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
        editor = new Editor(rootPM);
        overview = new Overview(rootPM);
        toolbar = new Toolbar(rootPM);
        header = new Header(rootPM);


    }

    @Override
    public void layoutControls() {


        setTop(toolbar);
        setCenter(new SplitPane(overview, new VBox(header,editor)));
//(chargingStationsTable, new VBox(chargingStationsHeader, chargingStationsEditor)));

    }

//    @Override
//    public void setupBindings() {
//        button.textProperty().bind(rootPM.greetingProperty());
//    }
}
