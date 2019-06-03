package ch.fhnw.chargingstationsfx.view;

import ch.fhnw.chargingstationsfx.presentationmodel.LadestationPM;
import ch.fhnw.chargingstationsfx.presentationmodel.LanguageSwitcherPM;
import ch.fhnw.chargingstationsfx.presentationmodel.RootPM;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.Optional;


public class ToolbarUI extends HBox implements ViewMixin {


    private final RootPM rootPM;

    private LanguageSwitcherPM languageSwitcherPM;
    private LadestationPM ladestationPM;
    private MultiLanguageText multiLanguageText;

    //Deklaration
    private Button saveButton;
    private Button addButton;
    private Button deleteButton;
    private Button undoButton;
    private Button redoButton;
    private Button search;
    private TextField searchField;
    private Region spaceField;


    public ToolbarUI(RootPM rootPM) {
        this.rootPM = rootPM;
        languageSwitcherPM = rootPM.getLanguageSwitcherPM();
        init();


    }

    @Override
    public void initializeControls() {
        //Initialisierung
        saveButton = new Button();
        addButton = new Button();
        deleteButton = new Button();
        undoButton = new Button();
        redoButton = new Button();
        search = new Button();


        searchField = new TextField();
        spaceField = new Region();

        multiLanguageText = new MultiLanguageText(languageSwitcherPM, ladestationPM);
    }

    @Override
    public void layoutControls() {

        HBox.setHgrow(spaceField, Priority.ALWAYS);
        search.setId("search-button");
        getChildren().addAll(saveButton, addButton, deleteButton, undoButton, redoButton, spaceField, multiLanguageText, search, searchField);
        setSpacing(10);
        setPadding(new Insets(0, 0, 5, 0));


    }

    @Override
    public void initializeSelf() {
        String stylesheet = getClass().getResource("style.css").toExternalForm();
        getStylesheets().add(stylesheet);
    }

    @Override
    public void setupEventHandlers() {

        saveButton.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Änderungen speichern");
            alert.setHeaderText(null);
            alert.setContentText("Möchten Sie die Änderung im File CHARGING_STATION.csv speichern?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                rootPM.save();
            }
        });

        addButton.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Änderungen speichern");
            alert.setHeaderText(null);
            alert.setContentText("Möchten Sie eine neue Ladestation hinzufügen?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                rootPM.add();
            }
        });
        deleteButton.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Änderungen speichern");
            alert.setHeaderText(null);
            alert.setContentText("Möchten Sie diese Ladestation wirklich löschen?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                rootPM.delete();
            }
        });


        undoButton.setOnAction(event -> rootPM.undo());
        redoButton.setOnAction(event -> rootPM.redo());


        searchField.setOnKeyTyped(event -> rootPM.filter(searchField.getText()));


    }

    @Override
    public void setupBindings() {
        // Binding EditorUI MultiLanguage

        addButton.textProperty().bind(rootPM.getLanguageSwitcherPM().addTextProperty());
        deleteButton.textProperty().bind(rootPM.getLanguageSwitcherPM().deleteTextProperty());
        saveButton.textProperty().bind(rootPM.getLanguageSwitcherPM().saveTextProperty());
        search.textProperty().bind(rootPM.getLanguageSwitcherPM().searchTextProperty());

        undoButton.disableProperty().bind(rootPM.undoDisabledProperty());
        redoButton.disableProperty().bind(rootPM.redoDisabledProperty());

        undoButton.textProperty().bind(rootPM.getLanguageSwitcherPM().undoTextProperty());
        redoButton.textProperty().bind(rootPM.getLanguageSwitcherPM().redoTextProperty());

    }

    ;


}



