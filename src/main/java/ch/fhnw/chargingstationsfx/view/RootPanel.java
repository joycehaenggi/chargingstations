package ch.fhnw.chargingstationsfx.view;

import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

import ch.fhnw.chargingstationsfx.presentationmodel.RootPM;

public class RootPanel extends StackPane implements ViewMixin {
    private final RootPM rootPM;

    private Button button;

    public RootPanel(RootPM model) {
        this.rootPM = model;

        init();
    }

    @Override
    public void initializeSelf() {
        addStylesheetFiles("style.css");
    }

    @Override
    public void initializeControls() {
        button = new Button();
    }

    @Override
    public void layoutControls() {
        getChildren().add(button);
    }

    @Override
    public void setupBindings() {
        button.textProperty().bind(rootPM.greetingProperty());
    }
}
