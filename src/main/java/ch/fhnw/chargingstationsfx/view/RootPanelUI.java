package ch.fhnw.chargingstationsfx.view;

import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;

import ch.fhnw.chargingstationsfx.presentationmodel.RootPM;

public class RootPanelUI extends BorderPane implements ViewMixin {
    private final RootPM rootPM;
    private SplitPane splitPane = new SplitPane();
    private SplitPane splitPane1 = new SplitPane();


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
        setTop(new Header());
        splitPane.getItems().addAll(new Overview(rootPM));
        setLeft(splitPane);
        splitPane1.getItems().add(new Editor());
        setRight(splitPane1);
        //setCenter(new Center());


    }

//    @Override
//    public void setupBindings() {
//        button.textProperty().bind(rootPM.greetingProperty());
//    }
}
