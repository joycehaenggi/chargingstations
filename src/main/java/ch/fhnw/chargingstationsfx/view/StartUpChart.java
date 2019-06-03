package ch.fhnw.chargingstationsfx.view;

import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.*;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This custom control provides a monthly calendar overview of start-ups of all fast charging stations.
 * Selecting a charging station date also gives a visual indication of when that station was put into operation compared
 * to the rest.
 *
 * @author Dieter Holz, Manuel Riedi, Linus Kohler
 */
public class StartUpChart extends Region {
    // needed for StyleableProperties
    private static final StyleablePropertyFactory<StartUpChart> FACTORY = new StyleablePropertyFactory<>(Region.getClassCssMetaData());

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return FACTORY.getCssMetaData();
    }

    private static final double ARTBOARD_WIDTH = 1000;
    private static final double ARTBOARD_HEIGHT = 210;

    private static final double ASPECT_RATIO = ARTBOARD_WIDTH / ARTBOARD_HEIGHT;

    private static final double MINIMUM_WIDTH = 1000;
    private static final double MINIMUM_HEIGHT = MINIMUM_WIDTH / ASPECT_RATIO;

    private static final double MAXIMUM_WIDTH = 2000;

    private Rectangle background;
    private List<SVGPath> tubeHolePatterns = new ArrayList<>();
    private Rectangle foregroundForFirstPattern;
    private Rectangle foregroundForLastPattern;
    private List<Rectangle> fillLevelBars = new ArrayList<>();

    private VBox selectedStationPane = new VBox();
    private Rectangle selectedStationPattern;
    private Label selectedStationLabel;

    private VBox stationPane = new VBox();
    private Rectangle stationPattern;
    private Label stationLabel;

    private VBox timePeriodPane = new VBox();
    private Label timePeriodLabel;
    private HBox timePeriodAxis = new HBox();
    private List<Label> timePeriodAxisLabels = new ArrayList<>();

    private boolean isSelectedDate;
    private int initialCssCalls = 0;

    private ObservableList<LocalDate> startupDates;

    // Public accessible properties (with public getter and setter)
    private Map<String, Long> startupPerMonth = new HashMap<>();
    private ObjectProperty<LocalDate> selectedDate = new SimpleObjectProperty<>();

    // Internal properties
    // Newest visible date on the right side of the custom control
    private ObjectProperty<LocalDate> newestVisibleDate = new SimpleObjectProperty<>();

    // Current oldest and newest Date in the date list, needed for scrolling restrictions
    private ObjectProperty<LocalDate> oldestDate = new SimpleObjectProperty<>();
    private ObjectProperty<LocalDate> newestDate = new SimpleObjectProperty<>();

    private IntegerProperty stationsPerBlock = new SimpleIntegerProperty(5);

    // Legends
    private StringProperty selectedStationLegend = new SimpleStringProperty("Ausgewählt");
    private StringProperty stationLegend = new SimpleStringProperty("x Stationen");
    private StringProperty timePeriodLegend = new SimpleStringProperty("Jahre seit Inbetriebnahme");

    private static final CssMetaData<StartUpChart, Color> SELECTION_COLOR_META_DATA = FACTORY.createColorCssMetaData("-selection-color", s -> s.selectionColor);
    private static final CssMetaData<StartUpChart, Color> STATION_COLOR_META_DATA = FACTORY.createColorCssMetaData("-station-color", s -> s.stationColor);

    private final StyleableObjectProperty<Color> selectionColor = new SimpleStyleableObjectProperty<>(SELECTION_COLOR_META_DATA, this, "selectionColor") {
        @Override
        protected void invalidated() {
            // To correctly load the selection color in the initial case
            if (initialCssCalls < 2) {
                setStyle(getCssMetaData().getProperty() + ": " + colorToCss(get()) + ";");
                initialCssCalls++;
            } else {
                setStyle("-selection-color: " + colorToCss(selectionColor.get()) + ";" + "-station-color: " + colorToCss(stationColor.get()) + ";");
            }
            applyCss();
        }
    };

    private final StyleableObjectProperty<Color> stationColor = new SimpleStyleableObjectProperty<>(STATION_COLOR_META_DATA, this, "stationColor") {
        @Override
        protected void invalidated() {
            setStyle("-selection-color: " + colorToCss(selectionColor.get()) + ";" + "-station-color: " + colorToCss(stationColor.get()) + ";");
            applyCss();
        }
    };

    // needed for resizing
    private Pane drawingPane;

    public StartUpChart(ObservableList<LocalDate> startupDates) {
        this.startupDates = startupDates;

        initializeSelf();
        initializeParts();
        initializeDrawingPane();
        layoutParts();
        setupEventHandlers();
        setupValueChangeListeners();
        setupBindings();

        countStartUpsPerMonths();
        setOldestDate();
        setNewestDate();
    }

    private void initializeSelf() {
        newestVisibleDate.setValue(LocalDate.now());

        // load stylesheets
        String fonts = getClass().getResource("/fonts/fonts.css").toExternalForm();
        getStylesheets().add(fonts);

        String stylesheet = getClass().getResource("style.css").toExternalForm();
        getStylesheets().add(stylesheet);

        getStyleClass().add("start-up-control");
    }

    private void initializeParts() {
        background = new Rectangle();
        background.getStyleClass().add("background");
        background.setWidth(ARTBOARD_WIDTH);
        background.setHeight(ARTBOARD_HEIGHT);

        foregroundForFirstPattern = new Rectangle();
        foregroundForFirstPattern.getStyleClass().add("tube-hole-pattern");
        foregroundForFirstPattern.setWidth(80);
        foregroundForFirstPattern.setHeight(ARTBOARD_HEIGHT);
        foregroundForFirstPattern.setLayoutX(0);

        for (int i = 0; i < 10; i++) {
            SVGPath tubeHolePattern = new SVGPath();
            tubeHolePattern.setContent("M90 0H180V22.5C180 21.1193 178.881 20 177.5 20H172.5C171.119 20 170 21.1193 170 22.5C170 23.8807 171.119 25 172.5 25H177.5C178.881 25 180 23.8807 180 22.5V32.5C180 31.1193 178.881 30 177.5 30H172.5C171.119 30 170 31.1193 170 32.5C170 33.8807 171.119 35 172.5 35H177.5C178.881 35 180 33.8807 180 32.5V42.5C180 41.1193 178.881 40 177.5 40H172.5C171.119 40 170 41.1193 170 42.5C170 43.8807 171.119 45 172.5 45H177.5C178.881 45 180 43.8807 180 42.5V52.5C180 51.1193 178.881 50 177.5 50H172.5C171.119 50 170 51.1193 170 52.5C170 53.8807 171.119 55 172.5 55H177.5C178.881 55 180 53.8807 180 52.5V62.5C180 61.1193 178.881 60 177.5 60H172.5C171.119 60 170 61.1193 170 62.5C170 63.8807 171.119 65 172.5 65H177.5C178.881 65 180 63.8807 180 62.5V72.5C180 71.1193 178.881 70 177.5 70H172.5C171.119 70 170 71.1193 170 72.5C170 73.8807 171.119 75 172.5 75H177.5C178.881 75 180 73.8807 180 72.5V82.5C180 81.1193 178.881 80 177.5 80H172.5C171.119 80 170 81.1193 170 82.5C170 83.8807 171.119 85 172.5 85H177.5C178.881 85 180 83.8807 180 82.5V92.5C180 91.1193 178.881 90 177.5 90H172.5C171.119 90 170 91.1193 170 92.5C170 93.8807 171.119 95 172.5 95H177.5C178.881 95 180 93.8807 180 92.5V102.5C180 101.119 178.881 100 177.5 100H172.5C171.119 100 170 101.119 170 102.5C170 103.881 171.119 105 172.5 105H177.5C178.881 105 180 103.881 180 102.5V112.5C180 111.119 178.881 110 177.5 110H172.5C171.119 110 170 111.119 170 112.5C170 113.881 171.119 115 172.5 115H177.5C178.881 115 180 113.881 180 112.5V122.5C180 121.119 178.881 120 177.5 120H172.5C171.119 120 170 121.119 170 122.5C170 123.881 171.119 125 172.5 125H177.5C178.881 125 180 123.881 180 122.5V132.5C180 131.119 178.881 130 177.5 130H172.5C171.119 130 170 131.119 170 132.5C170 133.881 171.119 135 172.5 135H177.5C178.881 135 180 133.881 180 132.5V142.5C180 141.119 178.881 140 177.5 140H172.5C171.119 140 170 141.119 170 142.5C170 143.881 171.119 145 172.5 145H177.5C178.881 145 180 143.881 180 142.5V152.5C180 151.119 178.881 150 177.5 150H172.5C171.119 150 170 151.119 170 152.5C170 153.881 171.119 155 172.5 155H177.5C178.881 155 180 153.881 180 152.5V162.5C180 161.119 178.881 160 177.5 160H172.5C171.119 160 170 161.119 170 162.5C170 163.881 171.119 165 172.5 165H177.5C178.881 165 180 163.881 180 162.5V172.5C180 171.119 178.881 170 177.5 170H172.5C171.119 170 170 171.119 170 172.5C170 173.881 171.119 175 172.5 175H177.5C178.881 175 180 173.881 180 172.5V210H90V0ZM95 22.5C95 21.1193 96.1193 20 97.5 20H102.5C103.881 20 105 21.1193 105 22.5C105 23.8807 103.881 25 102.5 25H97.5C96.1193 25 95 23.8807 95 22.5ZM97.5 30C96.1193 30 95 31.1193 95 32.5C95 33.8807 96.1193 35 97.5 35H102.5C103.881 35 105 33.8807 105 32.5C105 31.1193 103.881 30 102.5 30H97.5ZM95 42.5C95 41.1193 96.1193 40 97.5 40H102.5C103.881 40 105 41.1193 105 42.5C105 43.8807 103.881 45 102.5 45H97.5C96.1193 45 95 43.8807 95 42.5ZM97.5 50C96.1193 50 95 51.1193 95 52.5C95 53.8807 96.1193 55 97.5 55H102.5C103.881 55 105 53.8807 105 52.5C105 51.1193 103.881 50 102.5 50H97.5ZM95 62.5C95 61.1193 96.1193 60 97.5 60H102.5C103.881 60 105 61.1193 105 62.5C105 63.8807 103.881 65 102.5 65H97.5C96.1193 65 95 63.8807 95 62.5ZM97.5 70C96.1193 70 95 71.1193 95 72.5C95 73.8807 96.1193 75 97.5 75H102.5C103.881 75 105 73.8807 105 72.5C105 71.1193 103.881 70 102.5 70H97.5ZM95 82.5C95 81.1193 96.1193 80 97.5 80H102.5C103.881 80 105 81.1193 105 82.5C105 83.8807 103.881 85 102.5 85H97.5C96.1193 85 95 83.8807 95 82.5ZM97.5 90C96.1193 90 95 91.1193 95 92.5C95 93.8807 96.1193 95 97.5 95H102.5C103.881 95 105 93.8807 105 92.5C105 91.1193 103.881 90 102.5 90H97.5ZM95 102.5C95 101.119 96.1193 100 97.5 100H102.5C103.881 100 105 101.119 105 102.5C105 103.881 103.881 105 102.5 105H97.5C96.1193 105 95 103.881 95 102.5ZM97.5 110C96.1193 110 95 111.119 95 112.5C95 113.881 96.1193 115 97.5 115H102.5C103.881 115 105 113.881 105 112.5C105 111.119 103.881 110 102.5 110H97.5ZM95 122.5C95 121.119 96.1193 120 97.5 120H102.5C103.881 120 105 121.119 105 122.5C105 123.881 103.881 125 102.5 125H97.5C96.1193 125 95 123.881 95 122.5ZM97.5 130C96.1193 130 95 131.119 95 132.5C95 133.881 96.1193 135 97.5 135H102.5C103.881 135 105 133.881 105 132.5C105 131.119 103.881 130 102.5 130H97.5ZM95 142.5C95 141.119 96.1193 140 97.5 140H102.5C103.881 140 105 141.119 105 142.5C105 143.881 103.881 145 102.5 145H97.5C96.1193 145 95 143.881 95 142.5ZM97.5 150C96.1193 150 95 151.119 95 152.5C95 153.881 96.1193 155 97.5 155H102.5C103.881 155 105 153.881 105 152.5C105 151.119 103.881 150 102.5 150H97.5ZM95 162.5C95 161.119 96.1193 160 97.5 160H102.5C103.881 160 105 161.119 105 162.5C105 163.881 103.881 165 102.5 165H97.5C96.1193 165 95 163.881 95 162.5ZM97.5 170C96.1193 170 95 171.119 95 172.5C95 173.881 96.1193 175 97.5 175H102.5C103.881 175 105 173.881 105 172.5C105 171.119 103.881 170 102.5 170H97.5ZM99 12.5V14.5C99 14.7761 99.2239 15 99.5 15H100.5C100.776 15 101 14.7761 101 14.5V12.5C101 12.2239 101.224 12 101.5 12H103.5C103.776 12 104 11.7761 104 11.5V10.5C104 10.2239 103.776 10 103.5 10H101.5C101.224 10 101 9.77614 101 9.5V7.5C101 7.22386 100.776 7 100.5 7H99.5C99.2239 7 99 7.22386 99 7.5V9.5C99 9.77614 98.7761 10 98.5 10H96.5C96.2239 10 96 10.2239 96 10.5V11.5C96 11.7761 96.2239 12 96.5 12H98.5C98.7761 12 99 12.2239 99 12.5ZM112.5 20C111.119 20 110 21.1193 110 22.5C110 23.8807 111.119 25 112.5 25H117.5C118.881 25 120 23.8807 120 22.5C120 21.1193 118.881 20 117.5 20H112.5ZM110 32.5C110 31.1193 111.119 30 112.5 30H117.5C118.881 30 120 31.1193 120 32.5C120 33.8807 118.881 35 117.5 35H112.5C111.119 35 110 33.8807 110 32.5ZM112.5 40C111.119 40 110 41.1193 110 42.5C110 43.8807 111.119 45 112.5 45H117.5C118.881 45 120 43.8807 120 42.5C120 41.1193 118.881 40 117.5 40H112.5ZM110 52.5C110 51.1193 111.119 50 112.5 50H117.5C118.881 50 120 51.1193 120 52.5C120 53.8807 118.881 55 117.5 55H112.5C111.119 55 110 53.8807 110 52.5ZM112.5 60C111.119 60 110 61.1193 110 62.5C110 63.8807 111.119 65 112.5 65H117.5C118.881 65 120 63.8807 120 62.5C120 61.1193 118.881 60 117.5 60H112.5ZM110 72.5C110 71.1193 111.119 70 112.5 70H117.5C118.881 70 120 71.1193 120 72.5C120 73.8807 118.881 75 117.5 75H112.5C111.119 75 110 73.8807 110 72.5ZM112.5 80C111.119 80 110 81.1193 110 82.5C110 83.8807 111.119 85 112.5 85H117.5C118.881 85 120 83.8807 120 82.5C120 81.1193 118.881 80 117.5 80H112.5ZM110 92.5C110 91.1193 111.119 90 112.5 90H117.5C118.881 90 120 91.1193 120 92.5C120 93.8807 118.881 95 117.5 95H112.5C111.119 95 110 93.8807 110 92.5ZM112.5 100C111.119 100 110 101.119 110 102.5C110 103.881 111.119 105 112.5 105H117.5C118.881 105 120 103.881 120 102.5C120 101.119 118.881 100 117.5 100H112.5ZM110 112.5C110 111.119 111.119 110 112.5 110H117.5C118.881 110 120 111.119 120 112.5C120 113.881 118.881 115 117.5 115H112.5C111.119 115 110 113.881 110 112.5ZM112.5 120C111.119 120 110 121.119 110 122.5C110 123.881 111.119 125 112.5 125H117.5C118.881 125 120 123.881 120 122.5C120 121.119 118.881 120 117.5 120H112.5ZM110 132.5C110 131.119 111.119 130 112.5 130H117.5C118.881 130 120 131.119 120 132.5C120 133.881 118.881 135 117.5 135H112.5C111.119 135 110 133.881 110 132.5ZM112.5 140C111.119 140 110 141.119 110 142.5C110 143.881 111.119 145 112.5 145H117.5C118.881 145 120 143.881 120 142.5C120 141.119 118.881 140 117.5 140H112.5ZM110 152.5C110 151.119 111.119 150 112.5 150H117.5C118.881 150 120 151.119 120 152.5C120 153.881 118.881 155 117.5 155H112.5C111.119 155 110 153.881 110 152.5ZM112.5 160C111.119 160 110 161.119 110 162.5C110 163.881 111.119 165 112.5 165H117.5C118.881 165 120 163.881 120 162.5C120 161.119 118.881 160 117.5 160H112.5ZM110 172.5C110 171.119 111.119 170 112.5 170H117.5C118.881 170 120 171.119 120 172.5C120 173.881 118.881 175 117.5 175H112.5C111.119 175 110 173.881 110 172.5ZM114 14.5V12.5C114 12.2239 113.776 12 113.5 12H111.5C111.224 12 111 11.7761 111 11.5V10.5C111 10.2239 111.224 10 111.5 10H113.5C113.776 10 114 9.77614 114 9.5V7.5C114 7.22386 114.224 7 114.5 7H115.5C115.776 7 116 7.22386 116 7.5V9.5C116 9.77614 116.224 10 116.5 10H118.5C118.776 10 119 10.2239 119 10.5V11.5C119 11.7761 118.776 12 118.5 12H116.5C116.224 12 116 12.2239 116 12.5V14.5C116 14.7761 115.776 15 115.5 15H114.5C114.224 15 114 14.7761 114 14.5ZM125 22.5C125 21.1193 126.119 20 127.5 20H132.5C133.881 20 135 21.1193 135 22.5C135 23.8807 133.881 25 132.5 25H127.5C126.119 25 125 23.8807 125 22.5ZM127.5 30C126.119 30 125 31.1193 125 32.5C125 33.8807 126.119 35 127.5 35H132.5C133.881 35 135 33.8807 135 32.5C135 31.1193 133.881 30 132.5 30H127.5ZM125 42.5C125 41.1193 126.119 40 127.5 40H132.5C133.881 40 135 41.1193 135 42.5C135 43.8807 133.881 45 132.5 45H127.5C126.119 45 125 43.8807 125 42.5ZM127.5 50C126.119 50 125 51.1193 125 52.5C125 53.8807 126.119 55 127.5 55H132.5C133.881 55 135 53.8807 135 52.5C135 51.1193 133.881 50 132.5 50H127.5ZM125 62.5C125 61.1193 126.119 60 127.5 60H132.5C133.881 60 135 61.1193 135 62.5C135 63.8807 133.881 65 132.5 65H127.5C126.119 65 125 63.8807 125 62.5ZM127.5 70C126.119 70 125 71.1193 125 72.5C125 73.8807 126.119 75 127.5 75H132.5C133.881 75 135 73.8807 135 72.5C135 71.1193 133.881 70 132.5 70H127.5ZM125 82.5C125 81.1193 126.119 80 127.5 80H132.5C133.881 80 135 81.1193 135 82.5C135 83.8807 133.881 85 132.5 85H127.5C126.119 85 125 83.8807 125 82.5ZM127.5 90C126.119 90 125 91.1193 125 92.5C125 93.8807 126.119 95 127.5 95H132.5C133.881 95 135 93.8807 135 92.5C135 91.1193 133.881 90 132.5 90H127.5ZM125 102.5C125 101.119 126.119 100 127.5 100H132.5C133.881 100 135 101.119 135 102.5C135 103.881 133.881 105 132.5 105H127.5C126.119 105 125 103.881 125 102.5ZM127.5 110C126.119 110 125 111.119 125 112.5C125 113.881 126.119 115 127.5 115H132.5C133.881 115 135 113.881 135 112.5C135 111.119 133.881 110 132.5 110H127.5ZM125 122.5C125 121.119 126.119 120 127.5 120H132.5C133.881 120 135 121.119 135 122.5C135 123.881 133.881 125 132.5 125H127.5C126.119 125 125 123.881 125 122.5ZM127.5 130C126.119 130 125 131.119 125 132.5C125 133.881 126.119 135 127.5 135H132.5C133.881 135 135 133.881 135 132.5C135 131.119 133.881 130 132.5 130H127.5ZM125 142.5C125 141.119 126.119 140 127.5 140H132.5C133.881 140 135 141.119 135 142.5C135 143.881 133.881 145 132.5 145H127.5C126.119 145 125 143.881 125 142.5ZM127.5 150C126.119 150 125 151.119 125 152.5C125 153.881 126.119 155 127.5 155H132.5C133.881 155 135 153.881 135 152.5C135 151.119 133.881 150 132.5 150H127.5ZM125 162.5C125 161.119 126.119 160 127.5 160H132.5C133.881 160 135 161.119 135 162.5C135 163.881 133.881 165 132.5 165H127.5C126.119 165 125 163.881 125 162.5ZM127.5 170C126.119 170 125 171.119 125 172.5C125 173.881 126.119 175 127.5 175H132.5C133.881 175 135 173.881 135 172.5C135 171.119 133.881 170 132.5 170H127.5ZM129 12.5V14.5C129 14.7761 129.224 15 129.5 15H130.5C130.776 15 131 14.7761 131 14.5V12.5C131 12.2239 131.224 12 131.5 12H133.5C133.776 12 134 11.7761 134 11.5V10.5C134 10.2239 133.776 10 133.5 10H131.5C131.224 10 131 9.77614 131 9.5V7.5C131 7.22386 130.776 7 130.5 7H129.5C129.224 7 129 7.22386 129 7.5V9.5C129 9.77614 128.776 10 128.5 10H126.5C126.224 10 126 10.2239 126 10.5V11.5C126 11.7761 126.224 12 126.5 12H128.5C128.776 12 129 12.2239 129 12.5ZM142.5 20C141.119 20 140 21.1193 140 22.5C140 23.8807 141.119 25 142.5 25H147.5C148.881 25 150 23.8807 150 22.5C150 21.1193 148.881 20 147.5 20H142.5ZM140 32.5C140 31.1193 141.119 30 142.5 30H147.5C148.881 30 150 31.1193 150 32.5C150 33.8807 148.881 35 147.5 35H142.5C141.119 35 140 33.8807 140 32.5ZM142.5 40C141.119 40 140 41.1193 140 42.5C140 43.8807 141.119 45 142.5 45H147.5C148.881 45 150 43.8807 150 42.5C150 41.1193 148.881 40 147.5 40H142.5ZM140 52.5C140 51.1193 141.119 50 142.5 50H147.5C148.881 50 150 51.1193 150 52.5C150 53.8807 148.881 55 147.5 55H142.5C141.119 55 140 53.8807 140 52.5ZM142.5 60C141.119 60 140 61.1193 140 62.5C140 63.8807 141.119 65 142.5 65H147.5C148.881 65 150 63.8807 150 62.5C150 61.1193 148.881 60 147.5 60H142.5ZM140 72.5C140 71.1193 141.119 70 142.5 70H147.5C148.881 70 150 71.1193 150 72.5C150 73.8807 148.881 75 147.5 75H142.5C141.119 75 140 73.8807 140 72.5ZM142.5 80C141.119 80 140 81.1193 140 82.5C140 83.8807 141.119 85 142.5 85H147.5C148.881 85 150 83.8807 150 82.5C150 81.1193 148.881 80 147.5 80H142.5ZM140 92.5C140 91.1193 141.119 90 142.5 90H147.5C148.881 90 150 91.1193 150 92.5C150 93.8807 148.881 95 147.5 95H142.5C141.119 95 140 93.8807 140 92.5ZM142.5 100C141.119 100 140 101.119 140 102.5C140 103.881 141.119 105 142.5 105H147.5C148.881 105 150 103.881 150 102.5C150 101.119 148.881 100 147.5 100H142.5ZM140 112.5C140 111.119 141.119 110 142.5 110H147.5C148.881 110 150 111.119 150 112.5C150 113.881 148.881 115 147.5 115H142.5C141.119 115 140 113.881 140 112.5ZM142.5 120C141.119 120 140 121.119 140 122.5C140 123.881 141.119 125 142.5 125H147.5C148.881 125 150 123.881 150 122.5C150 121.119 148.881 120 147.5 120H142.5ZM140 132.5C140 131.119 141.119 130 142.5 130H147.5C148.881 130 150 131.119 150 132.5C150 133.881 148.881 135 147.5 135H142.5C141.119 135 140 133.881 140 132.5ZM142.5 140C141.119 140 140 141.119 140 142.5C140 143.881 141.119 145 142.5 145H147.5C148.881 145 150 143.881 150 142.5C150 141.119 148.881 140 147.5 140H142.5ZM140 152.5C140 151.119 141.119 150 142.5 150H147.5C148.881 150 150 151.119 150 152.5C150 153.881 148.881 155 147.5 155H142.5C141.119 155 140 153.881 140 152.5ZM142.5 160C141.119 160 140 161.119 140 162.5C140 163.881 141.119 165 142.5 165H147.5C148.881 165 150 163.881 150 162.5C150 161.119 148.881 160 147.5 160H142.5ZM140 172.5C140 171.119 141.119 170 142.5 170H147.5C148.881 170 150 171.119 150 172.5C150 173.881 148.881 175 147.5 175H142.5C141.119 175 140 173.881 140 172.5ZM144 14.5V12.5C144 12.2239 143.776 12 143.5 12H141.5C141.224 12 141 11.7761 141 11.5V10.5C141 10.2239 141.224 10 141.5 10H143.5C143.776 10 144 9.77614 144 9.5V7.5C144 7.22386 144.224 7 144.5 7H145.5C145.776 7 146 7.22386 146 7.5V9.5C146 9.77614 146.224 10 146.5 10H148.5C148.776 10 149 10.2239 149 10.5V11.5C149 11.7761 148.776 12 148.5 12H146.5C146.224 12 146 12.2239 146 12.5V14.5C146 14.7761 145.776 15 145.5 15H144.5C144.224 15 144 14.7761 144 14.5ZM155 22.5C155 21.1193 156.119 20 157.5 20H162.5C163.881 20 165 21.1193 165 22.5C165 23.8807 163.881 25 162.5 25H157.5C156.119 25 155 23.8807 155 22.5ZM157.5 30C156.119 30 155 31.1193 155 32.5C155 33.8807 156.119 35 157.5 35H162.5C163.881 35 165 33.8807 165 32.5C165 31.1193 163.881 30 162.5 30H157.5ZM155 42.5C155 41.1193 156.119 40 157.5 40H162.5C163.881 40 165 41.1193 165 42.5C165 43.8807 163.881 45 162.5 45H157.5C156.119 45 155 43.8807 155 42.5ZM157.5 50C156.119 50 155 51.1193 155 52.5C155 53.8807 156.119 55 157.5 55H162.5C163.881 55 165 53.8807 165 52.5C165 51.1193 163.881 50 162.5 50H157.5ZM155 62.5C155 61.1193 156.119 60 157.5 60H162.5C163.881 60 165 61.1193 165 62.5C165 63.8807 163.881 65 162.5 65H157.5C156.119 65 155 63.8807 155 62.5ZM157.5 70C156.119 70 155 71.1193 155 72.5C155 73.8807 156.119 75 157.5 75H162.5C163.881 75 165 73.8807 165 72.5C165 71.1193 163.881 70 162.5 70H157.5ZM155 82.5C155 81.1193 156.119 80 157.5 80H162.5C163.881 80 165 81.1193 165 82.5C165 83.8807 163.881 85 162.5 85H157.5C156.119 85 155 83.8807 155 82.5ZM157.5 90C156.119 90 155 91.1193 155 92.5C155 93.8807 156.119 95 157.5 95H162.5C163.881 95 165 93.8807 165 92.5C165 91.1193 163.881 90 162.5 90H157.5ZM155 102.5C155 101.119 156.119 100 157.5 100H162.5C163.881 100 165 101.119 165 102.5C165 103.881 163.881 105 162.5 105H157.5C156.119 105 155 103.881 155 102.5ZM157.5 110C156.119 110 155 111.119 155 112.5C155 113.881 156.119 115 157.5 115H162.5C163.881 115 165 113.881 165 112.5C165 111.119 163.881 110 162.5 110H157.5ZM155 122.5C155 121.119 156.119 120 157.5 120H162.5C163.881 120 165 121.119 165 122.5C165 123.881 163.881 125 162.5 125H157.5C156.119 125 155 123.881 155 122.5ZM157.5 130C156.119 130 155 131.119 155 132.5C155 133.881 156.119 135 157.5 135H162.5C163.881 135 165 133.881 165 132.5C165 131.119 163.881 130 162.5 130H157.5ZM155 142.5C155 141.119 156.119 140 157.5 140H162.5C163.881 140 165 141.119 165 142.5C165 143.881 163.881 145 162.5 145H157.5C156.119 145 155 143.881 155 142.5ZM157.5 150C156.119 150 155 151.119 155 152.5C155 153.881 156.119 155 157.5 155H162.5C163.881 155 165 153.881 165 152.5C165 151.119 163.881 150 162.5 150H157.5ZM155 162.5C155 161.119 156.119 160 157.5 160H162.5C163.881 160 165 161.119 165 162.5C165 163.881 163.881 165 162.5 165H157.5C156.119 165 155 163.881 155 162.5ZM157.5 170C156.119 170 155 171.119 155 172.5C155 173.881 156.119 175 157.5 175H162.5C163.881 175 165 173.881 165 172.5C165 171.119 163.881 170 162.5 170H157.5ZM159 12.5V14.5C159 14.7761 159.224 15 159.5 15H160.5C160.776 15 161 14.7761 161 14.5V12.5C161 12.2239 161.224 12 161.5 12H163.5C163.776 12 164 11.7761 164 11.5V10.5C164 10.2239 163.776 10 163.5 10H161.5C161.224 10 161 9.77614 161 9.5V7.5C161 7.22386 160.776 7 160.5 7H159.5C159.224 7 159 7.22386 159 7.5V9.5C159 9.77614 158.776 10 158.5 10H156.5C156.224 10 156 10.2239 156 10.5V11.5C156 11.7761 156.224 12 156.5 12H158.5C158.776 12 159 12.2239 159 12.5ZM174 14.5V12.5C174 12.2239 173.776 12 173.5 12H171.5C171.224 12 171 11.7761 171 11.5V10.5C171 10.2239 171.224 10 171.5 10H173.5C173.776 10 174 9.77614 174 9.5V7.5C174 7.22386 174.224 7 174.5 7H175.5C175.776 7 176 7.22386 176 7.5V9.5C176 9.77614 176.224 10 176.5 10H178.5C178.776 10 179 10.2239 179 10.5V11.5C179 11.7761 178.776 12 178.5 12H176.5C176.224 12 176 12.2239 176 12.5V14.5C176 14.7761 175.776 15 175.5 15H174.5C174.224 15 174 14.7761 174 14.5Z");
            tubeHolePattern.setFillRule(FillRule.EVEN_ODD);
            tubeHolePattern.setLayoutX((i * 90) - 10);
            tubeHolePattern.setLayoutY(0);
            tubeHolePattern.getStyleClass().add("tube-hole-pattern");
            tubeHolePatterns.add(tubeHolePattern);
        }

        foregroundForLastPattern = new Rectangle();
        foregroundForLastPattern.getStyleClass().add("tube-hole-pattern");
        foregroundForLastPattern.setWidth(20);
        foregroundForLastPattern.setHeight(ARTBOARD_HEIGHT);
        foregroundForLastPattern.setLayoutX(980);

        initializeLegends();
    }

    private void initializeDrawingPane() {
        drawingPane = new Pane();
        drawingPane.getStyleClass().add("drawing-pane");
        drawingPane.setMaxSize(ARTBOARD_WIDTH, ARTBOARD_HEIGHT);
        drawingPane.setMinSize(ARTBOARD_WIDTH, ARTBOARD_HEIGHT);
        drawingPane.setPrefSize(ARTBOARD_WIDTH, ARTBOARD_HEIGHT);
    }

    private void layoutParts() {
        selectedStationPane.getChildren().addAll(selectedStationPattern, selectedStationLabel);
        stationPane.getChildren().addAll(stationPattern, stationLabel);
        timePeriodPane.getChildren().addAll(timePeriodLabel);

        drawingPane.getChildren().addAll(background, foregroundForFirstPattern, foregroundForLastPattern,
                selectedStationPane, stationPane, timePeriodPane, timePeriodAxis);
        drawingPane.getChildren().addAll(tubeHolePatterns);
        timePeriodAxis.toFront();

        getChildren().add(drawingPane);
    }

    private void setupEventHandlers() {
        this.setOnScroll((ScrollEvent event) -> {
            if (event.getDeltaY() < 0) {
                if (newestVisibleDate.getValue().isBefore(newestDate.getValue().plusMonths(30))) {
                    newestVisibleDate.setValue(newestVisibleDate.getValue().plusMonths(1));
                    generateFillLevelBars();
                    generateTimePeriodAxisLegend();
                }
            } else if (newestVisibleDate.getValue().isAfter(oldestDate.getValue().plusMonths(30))) {
                newestVisibleDate.setValue(newestVisibleDate.getValue().minusMonths(1));
                generateFillLevelBars();
                generateTimePeriodAxisLegend();
            }
        });
    }

    private void setupValueChangeListeners() {
        // Detects changes in the list of dates and updates the internal map for data per month
        startupDates.addListener((ListChangeListener.Change<? extends LocalDate> change) -> {
            while (change.next()) {
                if (change.wasAdded() || change.wasRemoved()) {
                    countStartUpsPerMonths();
                    setOldestDate();
                    setNewestDate();
                }
            }
        });

        stationsPerBlock.addListener(change -> countStartUpsPerMonths());

        selectedDate.addListener(c -> {
            newestVisibleDate.setValue(selectedDate.getValue().plusMonths(30));
            generateFillLevelBars();
            generateTimePeriodAxisLegend();
        });
    }

    private void setupBindings() {
        selectedStationLabel.textProperty().bind(selectedStationLegend);
        stationLabel.textProperty().bind(stationLegend);
        timePeriodLabel.textProperty().bind(timePeriodLegend);
        stationLegend.bind(Bindings.convert(stationsPerBlockProperty()).concat(" Stationen"));
    }

    /*
     * Creates a new map in which months are summarized.
     * Key is the month plus year and the value is the number how often the months was in the startup dates list
     */
    private void countStartUpsPerMonths() {
        startupPerMonth = startupDates.stream().collect(Collectors.groupingBy(e ->
                e.getYear() + "-" + e.getMonthValue(), Collectors.counting()));
        generateFillLevelBars();
    }

    private void setOldestDate() {
        LocalDate oldest = startupDates.get(0);
        for (LocalDate l : startupDates) {
            if (l.isBefore(oldest)) {
                oldest = l;
            }
        }
        oldestDate.setValue(oldest);
    }

    private void setNewestDate() {
        LocalDate newest = startupDates.get(0);
        for (LocalDate l : startupDates) {
            if (l.isAfter(newest)) {
                newest = l;
            }
        }
        newestDate.setValue(newest);
    }

    private void initializeLegends() {
        selectedStationPane = createLegendPane(5, 18, 75, 33);

        selectedStationPattern = new Rectangle();
        selectedStationPattern.setWidth(10);
        selectedStationPattern.setHeight(5);
        selectedStationPattern.getStyleClass().add("selected-station-pattern");

        selectedStationLabel = createLegendLabel();

        stationPane = createLegendPane(5, 60, 75, 33);

        stationPattern = new Rectangle();
        stationPattern.setWidth(10);
        stationPattern.setHeight(5);
        stationPattern.getStyleClass().add("station-pattern");

        stationLabel = createLegendLabel();

        timePeriodPane = createLegendPane(5, 180, 75, 30);

        timePeriodAxis.setPrefWidth(899);
        timePeriodAxis.setPrefHeight(30);
        timePeriodAxis.setLayoutX(83);
        timePeriodAxis.setLayoutY(180);
        timePeriodAxis.setSpacing(1);
        timePeriodAxis.setAlignment(Pos.TOP_RIGHT);

        generateTimePeriodAxisLegend();
        timePeriodLabel = createLegendLabel();
    }

    /**
     * Creates the rectangles in the background to fill the right number of tube holes on the custom control based
     * on the startup date count per month and for the selected station.
     */
    private void generateFillLevelBars() {
        // Remove all old fill bars
        if (!fillLevelBars.isEmpty()) {
            drawingPane.getChildren().removeAll(fillLevelBars);

            // Create a new empty list
            fillLevelBars = new ArrayList<>();
        }

        // Generate fill level bars for all dates in the map
        Iterator iterator = startupPerMonth.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            String timePeriod = (String) entry.getKey();
            long stationCount = startupPerMonth.get(timePeriod);

            // Generate date of period
            int index = timePeriod.indexOf("-");
            int year = Integer.parseInt(timePeriod.substring(0, index));
            int month = Integer.parseInt(timePeriod.substring(index + 1));
            LocalDate oneDate = LocalDate.of(year, month, 1);

            //Checks whether the level bar to be created is currently selected.
            if (selectedDate.get() != null) {
                if (oneDate.getMonth() == selectedDate.getValue().getMonth()
                        && oneDate.getYear() == selectedDate.getValue().getYear()) {
                    isSelectedDate = true;
                } else {
                    isSelectedDate = false;
                }
            }

            // Used for the fill bar position on the custom control
            int totalMonthsBeforeNewestDate = - (calculateMonthsDelta(oneDate));

            // Only create the bars for the latest 60 months
            if (totalMonthsBeforeNewestDate < 60 && totalMonthsBeforeNewestDate >= 0) {
                fillLevelBars.add(createFillLevelBar(stationCount, totalMonthsBeforeNewestDate, isSelectedDate));
            }
        }

        // Add fill bars directly after background (to be behind other elements)
        drawingPane.getChildren().addAll(1, fillLevelBars);
    }

    /**
     * Creates the labels for the time period axis (x-axis) of this control.
     * Call it again to update the labels.
     * Labels are created based with the newestVisibleDate on the right side of the axis.
     */
    private void generateTimePeriodAxisLegend() {
        if (!timePeriodAxisLabels.isEmpty()) {
            timePeriodAxis.getChildren().removeAll(timePeriodAxisLabels);

            // Create a new empty list
            timePeriodAxisLabels = new ArrayList<>();
        }

        int monthDelta = calculateMonthsDelta();

        // Add 60 labels with the time delta
        for (int i = (monthDelta - 1); i >= monthDelta - 60; i--) {
            // Only addNewStation the numbers for full years (after all 12 months)
            if ((i % 12) == 0) {
                timePeriodAxisLabels.add(createTimePeriodLabel("" + (i / 12)));
            } else {
                timePeriodAxisLabels.add(createTimePeriodLabel("."));
            }
        }
        timePeriodAxis.getChildren().addAll(timePeriodAxisLabels);
    }

    //resize by scaling
    @Override
    protected void layoutChildren() {
        super.layoutChildren();
        resize();
    }

    private void resize() {
        Insets padding = getPadding();
        double availableWidth = getWidth() - padding.getLeft() - padding.getRight();
        double availableHeight = getHeight() - padding.getTop() - padding.getBottom();

        double width = Math.max(Math.min(Math.min(availableWidth, availableHeight * ASPECT_RATIO), MAXIMUM_WIDTH), MINIMUM_WIDTH);

        double scalingFactor = width / ARTBOARD_WIDTH;

        if (availableWidth > 0 && availableHeight > 0) {
            relocateDrawingPaneCentered();
            drawingPane.setScaleX(scalingFactor);
            drawingPane.setScaleY(scalingFactor);
        }
    }

    private void relocateDrawingPaneCentered() {
        drawingPane.relocate((getWidth() - ARTBOARD_WIDTH) * 0.5, (getHeight() - ARTBOARD_HEIGHT) * 0.5);
    }

    private void relocateDrawingPaneCenterBottom(double scaleY, double paddingBottom) {
        double visualHeight = ARTBOARD_HEIGHT * scaleY;
        double visualSpace = getHeight() - visualHeight;
        double y = visualSpace + (visualHeight - ARTBOARD_HEIGHT) * 0.5 - paddingBottom;

        drawingPane.relocate((getWidth() - ARTBOARD_WIDTH) * 0.5, y);
    }

    private void relocateDrawingPaneCenterTop(double scaleY, double paddingTop) {
        double visualHeight = ARTBOARD_HEIGHT * scaleY;
        double y = (visualHeight - ARTBOARD_HEIGHT) * 0.5 + paddingTop;

        drawingPane.relocate((getWidth() - ARTBOARD_WIDTH) * 0.5, y);
    }

    // helper functions for control elements

    private VBox createLegendPane(int xPos, int yPos, int width, int height) {
        VBox vBox = new VBox();

        vBox.setPrefWidth(width);
        vBox.setPrefHeight(height);
        vBox.setLayoutX(xPos);
        vBox.setLayoutY(yPos);
        vBox.setSpacing(3);
        vBox.setAlignment(Pos.TOP_CENTER);

        return vBox;
    }

    private Label createLegendLabel() {
        Label label = new Label();
        label.setPrefWidth(75);
        label.setContentDisplay(ContentDisplay.TOP);
        label.setAlignment(Pos.CENTER);
        label.setWrapText(true);
        label.getStyleClass().add("legend-text");

        return label;
    }

    private Label createTimePeriodLabel(String text) {
        Label label = new Label();
        label.setText(text);
        label.setPrefWidth(14);
        label.setContentDisplay(ContentDisplay.TOP);
        label.setAlignment(Pos.CENTER);
        label.getStyleClass().add("legend-text");

        return label;
    }

    /**
     * Creates a fill bar on the right position, with correct dimensions and formatting.
     *
     * @param stationCount number of stations counted with this month
     * @param totalMonthsBeforeNewestDate Number of months
     * @param isSelectedDate if the fill bar is for the selected date
     * @return fill bar rectangle
     */
    private Rectangle createFillLevelBar(long stationCount, int totalMonthsBeforeNewestDate, boolean isSelectedDate) {
        // Maximum number of visible blocks is 17
        // Count limit restricts the max size of the fill level bars
        if (stationCount > (17 * stationsPerBlock.getValue())) {
            stationCount = 17 * stationsPerBlock.getValue();
        }

        // Station count to block count, always round up to have one station visible
        int blockCount = (int) Math.ceil(stationCount / (double) stationsPerBlock.getValue());

        Rectangle fillLevelBar = new Rectangle();
        fillLevelBar.setWidth(14);
        fillLevelBar.setHeight(blockCount * 10);
        fillLevelBar.setLayoutX(968 - (15 * totalMonthsBeforeNewestDate));
        fillLevelBar.setLayoutY(176 - (blockCount * 10));

        if (isSelectedDate) {
            fillLevelBar.getStyleClass().add("selected-fill-level-bar");
        } else {
            fillLevelBar.getStyleClass().add("fill-level-bar");
        }

        return fillLevelBar;
    }

    /**
     * Calculate the months between now and the newest visible date
     *
     * @return number of months between now and the newest visible date
     */
    private int calculateMonthsDelta() {
        Period dateDelta = Period.between(newestVisibleDate.getValue().withDayOfMonth(1), LocalDate.now().withDayOfMonth(1));
        return (dateDelta.getYears() * 12) + dateDelta.getMonths() + 60;
    }

    /**
     * Calculate the months between now and the given date
     *
     * @return number of months between the given date and the newest visible date
     */
    private int calculateMonthsDelta(LocalDate dateForDifference) {
        Period dateDelta = Period.between(newestVisibleDate.getValue().withDayOfMonth(1), dateForDifference.withDayOfMonth(1));
        return (dateDelta.getYears() * 12) + dateDelta.getMonths();
    }

    // some handy functions
    private double percentageToValue(double percentage, double minValue, double maxValue) {
        return ((maxValue - minValue) * percentage) + minValue;
    }

    private double valueToPercentage(double value, double minValue, double maxValue) {
        return (value - minValue) / (maxValue - minValue);
    }

    private double valueToAngle(double value, double minValue, double maxValue) {
        return percentageToAngle(valueToPercentage(value, minValue, maxValue));
    }

    private double mousePositionToValue(double mouseX, double mouseY, double cx, double cy, double minValue, double maxValue) {
        double percentage = angleToPercentage(angle(cx, cy, mouseX, mouseY));

        return percentageToValue(percentage, minValue, maxValue);
    }

    private double angleToPercentage(double angle) {
        return angle / 360.0;
    }

    private double percentageToAngle(double percentage) {
        return 360.0 * percentage;
    }

    private double angle(double cx, double cy, double x, double y) {
        double deltaX = x - cx;
        double deltaY = y - cy;
        double radius = Math.sqrt((deltaX * deltaX) + (deltaY * deltaY));
        double nx = deltaX / radius;
        double ny = deltaY / radius;
        double theta = Math.toRadians(90) + Math.atan2(ny, nx);

        return Double.compare(theta, 0.0) >= 0 ? Math.toDegrees(theta) : Math.toDegrees((theta)) + 360.0;
    }

    private Point2D pointOnCircle(double cX, double cY, double radius, double angle) {
        return new Point2D(cX - (radius * Math.sin(Math.toRadians(angle - 180))),
                cY + (radius * Math.cos(Math.toRadians(angle - 180))));
    }

    private String colorToCss(final Color color) {
        return color.toString().replace("0x", "#");
    }

    // compute sizes
    @Override
    protected double computeMinWidth(double height) {
        Insets padding = getPadding();
        double horizontalPadding = padding.getLeft() + padding.getRight();

        return MINIMUM_WIDTH + horizontalPadding;
    }

    @Override
    protected double computeMinHeight(double width) {
        Insets padding = getPadding();
        double verticalPadding = padding.getTop() + padding.getBottom();

        return MINIMUM_HEIGHT + verticalPadding;
    }

    @Override
    protected double computePrefWidth(double height) {
        Insets padding = getPadding();
        double horizontalPadding = padding.getLeft() + padding.getRight();

        return ARTBOARD_WIDTH + horizontalPadding;
    }

    @Override
    protected double computePrefHeight(double width) {
        Insets padding = getPadding();
        double verticalPadding = padding.getTop() + padding.getBottom();

        return ARTBOARD_HEIGHT + verticalPadding;
    }

    public ObservableList<LocalDate> getStartupDates() {
        return startupDates;
    }

    public LocalDate getSelectedDate() {
        return selectedDate.get();
    }

    public ObjectProperty<LocalDate> selectedDateProperty() {
        return selectedDate;
    }

    public void setSelectedDate(LocalDate selectedDate) {
        this.selectedDate.set(selectedDate);
    }

    public int getStationsPerBlock() {
        return stationsPerBlock.get();
    }

    public IntegerProperty stationsPerBlockProperty() {
        return stationsPerBlock;
    }

    public void setStationsPerBlock(int stationsPerBlock) {
        this.stationsPerBlock.set(stationsPerBlock);
    }

    public Color getSelectionColor() {
        return selectionColor.get();
    }

    public StyleableObjectProperty<Color> selectionColorProperty() {
        return selectionColor;
    }

    public void setSelectionColor(Color selectionColor) {
        this.selectionColor.set(selectionColor);
    }

    public Color getStationColor() {
        return stationColor.get();
    }

    public StyleableObjectProperty<Color> stationColorProperty() {
        return stationColor;
    }

    public void setStationColor(Color stationColor) {
        this.stationColor.set(stationColor);
    }
}
