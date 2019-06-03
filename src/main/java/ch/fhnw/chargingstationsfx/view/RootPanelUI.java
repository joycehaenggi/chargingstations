package ch.fhnw.chargingstationsfx.view;

import ch.fhnw.chargingstationsfx.presentationmodel.RootPM;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;

import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class RootPanelUI extends BorderPane implements ViewMixin {
    private final RootPM rootPM;
    private SplitPane splitPane = new SplitPane();
    private EditorUI editor;
    private TabelleUI overview;
    private ToolbarUI toolbar;
    private HeaderUI header;
    private StartUpChart startUpChart;
    private int selectedDateIndex;


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
        startUpChart = new StartUpChart(rootPM.getLocalDates());

        //Anzeige Anzahl Ladestationen in einer Spalte
        startUpChart.setStationsPerBlock(20);


    }

    @Override
    public void layoutControls() {


        setTop(toolbar);
        setCenter(new SplitPane(overview, new VBox(header, editor)));
        setBottom(startUpChart);
    }

    @Override
    public void setupBindings(){
        startUpChart.selectedDateProperty().bindBidirectional(rootPM.selectedDateProperty());
    }
}
