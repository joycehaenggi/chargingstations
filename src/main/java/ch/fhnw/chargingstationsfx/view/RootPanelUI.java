package ch.fhnw.chargingstationsfx.view;

import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;

import ch.fhnw.chargingstationsfx.presentationmodel.RootPM;

public class RootPanelUI extends BorderPane implements ViewMixin {
    private final RootPM rootPM;
    private SplitPane splitPane = new SplitPane();
    private Editor editor;
    private Overview overview;



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
    }

    @Override
    public void layoutControls() {
        editor = new Editor ();
        overview = new Overview(rootPM);

        setTop(new Header());
        splitPane.getItems().addAll(overview,editor);
        setLeft(splitPane);

        //setCenter(new Center());


    }

//    @Override
//    public void setupBindings() {
//        button.textProperty().bind(rootPM.greetingProperty());
//    }
}
