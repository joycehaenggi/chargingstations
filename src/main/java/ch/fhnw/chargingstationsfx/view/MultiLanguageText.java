package ch.fhnw.chargingstationsfx.view;

import ch.fhnw.chargingstationsfx.presentationmodel.LadestationPM;
import ch.fhnw.chargingstationsfx.presentationmodel.LanguageSwitcherPM;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

public class MultiLanguageText extends GridPane implements ViewMixin {
    private final LanguageSwitcherPM languageSwitcherPM;
    private final LadestationPM ladestationPM;
    private Button germanButton;
    private Button englishButton;



    public MultiLanguageText(LanguageSwitcherPM model, LadestationPM ladestationPM) {
        this.languageSwitcherPM = model;
        this.ladestationPM = ladestationPM;
                init();


    }
    @Override
    public void initializeSelf() {
        String stylesheet = getClass().getResource("style.css").toExternalForm();
        getStylesheets().add(stylesheet);
    }
    @Override
    public void initializeControls() {
        germanButton  = new Button();
        englishButton = new Button();

    }
    @Override
    public void layoutControls() {
        englishButton.setMaxWidth(Double.MAX_VALUE);
        germanButton.setMaxWidth(Double.MAX_VALUE);
//
//        label.setMaxHeight(Double.MAX_VALUE);
//        label.setMaxWidth(Double.MAX_VALUE);

//        setPadding(new Insets(10));
//       setVgap(5);
        setHgap(2);

        ColumnConstraints grow = new ColumnConstraints();
        grow.setHgrow(Priority.ALWAYS);
       grow.setPercentWidth(50);

        RowConstraints rGrow = new RowConstraints();
        rGrow.setVgrow(Priority.ALWAYS);

        getColumnConstraints().addAll(grow, grow);
        getRowConstraints().add(rGrow);

        addRow(0);
        add(germanButton, 0,1);
        add(englishButton, 1,1);

    }
    @Override
    public void setupEventHandlers() {
        germanButton.setOnAction(event  -> languageSwitcherPM.setLanguage(LanguageSwitcherPM.Lang.DE));
        englishButton.setOnAction(event -> languageSwitcherPM.setLanguage(LanguageSwitcherPM.Lang.EN));
    }

    @Override
    public void setupBindings() {
        germanButton.textProperty().bind(languageSwitcherPM.germanButtonTextProperty());
        englishButton.textProperty().bind(languageSwitcherPM.englishButtonTextProperty());

    }


}
