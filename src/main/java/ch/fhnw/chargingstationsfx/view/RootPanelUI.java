package ch.fhnw.chargingstationsfx.view;

import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;

import ch.fhnw.chargingstationsfx.presentationmodel.RootPM;

public class RootPanelUI extends BorderPane implements ViewMixin {
    private final RootPM rootPM;
    private SplitPane splitPane = new SplitPane();
    private Editor editor;
    private Overview overview;
    private ToolbarUI header;



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
        splitPane = new SplitPane();
        editor = new Editor(rootPM);
        overview = new Overview(rootPM);
        header = new ToolbarUI(rootPM);
    }

    @Override
    public void layoutControls() {

        header.setMaxWidth(Double.MAX_VALUE);

        splitPane.setPrefSize(200,200);
        splitPane.getItems().addAll(overview, editor);

        splitPane.setMinSize(900, 500);


        setTop(header);
        setCenter(splitPane);


    }

//    @Override
//    public void setupBindings() {
//        button.textProperty().bind(rootPM.greetingProperty());
//    }
}
