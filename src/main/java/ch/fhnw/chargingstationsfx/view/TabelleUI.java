package ch.fhnw.chargingstationsfx.view;

import ch.fhnw.chargingstationsfx.presentationmodel.LadestationPM;
import ch.fhnw.chargingstationsfx.presentationmodel.RootPM;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.time.LocalDate;


public class TabelleUI extends VBox implements ViewMixin {


    private final RootPM rootPM;
    private TableView<LadestationPM> tabelle;
    private Label counter;
    private SelectionModel<LocalDate> selectionModel;
    private int selectedDateIndex;


    public TabelleUI(RootPM rootPM) {
        this.rootPM = rootPM;
        init();
    }


    @Override
    public void initializeControls() {

        counter = new Label();

        tabelle = new TableView<>(rootPM.getFilteredList());

        //Im Konstruktor wird die Überschrift der Spalte angegeben
        TableColumn<LadestationPM, String> strasseCol = new TableColumn<>();
        strasseCol.textProperty().bind(rootPM.getLanguageSwitcherPM().strasseTextProperty());
        strasseCol.setCellValueFactory(cel -> cel.getValue().strasseNameProperty());

        TableColumn<LadestationPM, Number> PLZCol = new TableColumn<>();
        PLZCol.textProperty().bind(rootPM.getLanguageSwitcherPM().PLZTextProperty());
        PLZCol.setCellValueFactory(cel -> cel.getValue().PLZProperty());

        TableColumn<LadestationPM, String> ortCole = new TableColumn<>();
        ortCole.textProperty().bind(rootPM.getLanguageSwitcherPM().ortTextProperty());
        ortCole.setCellValueFactory(cel -> cel.getValue().ortProperty());

        TableColumn<LadestationPM, Number> anzahlLadestationCol = new TableColumn<>();
        anzahlLadestationCol.textProperty().bind(rootPM.getLanguageSwitcherPM().numberOfChargingPointsTextProperty());
        anzahlLadestationCol.setCellValueFactory(cel -> cel.getValue().numberOfChargingPointsProperty());

        tabelle.getColumns().addAll(strasseCol, PLZCol, ortCole, anzahlLadestationCol);

    }

    @Override
    public void layoutControls() {
        setVgrow(tabelle, Priority.ALWAYS);

        getChildren().addAll(tabelle, counter);

        counter.setPrefHeight(30);
        counter.setAlignment(Pos.TOP_CENTER);
        setVgrow(counter, Priority.NEVER);
    }

    @Override
    public void setupEventHandlers() {
        tabelle.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                rootPM.setSelectedCountryId(newValue.getENTITY_ID());

            }
        });
    }


    @Override
    public void setupBindings() {
        counter.textProperty().bind(rootPM.countProperty().asString()
                .concat(" /")
                .concat(rootPM.totalCountProperty().asString()));

    }

    public RootPM getRootPM() {
        return rootPM;
    }

    public TableView<LadestationPM> getTabelle() {
        return tabelle;
    }

    public void setTabelle(TableView<LadestationPM> tabelle) {
        this.tabelle = tabelle;
    }


}
