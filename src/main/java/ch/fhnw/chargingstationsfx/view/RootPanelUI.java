package ch.fhnw.chargingstationsfx.view;

import ch.fhnw.chargingstationsfx.presentationmodel.RootPM;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;

import javafx.scene.layout.VBox;

public class RootPanelUI extends BorderPane implements ViewMixin {
    private final RootPM rootPM;
    private SplitPane splitPane = new SplitPane();
    private EditorUI editor;
    private TabelleUI overview;
    private ToolbarUI toolbar;
    private HeaderUI header;


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
        editor = new EditorUI(rootPM);
        overview = new TabelleUI(rootPM);
        toolbar = new ToolbarUI(rootPM);
        header = new HeaderUI(rootPM);


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
