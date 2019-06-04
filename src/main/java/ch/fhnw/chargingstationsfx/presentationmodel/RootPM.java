package ch.fhnw.chargingstationsfx.presentationmodel;

import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RootPM {
    private static final String FILE_NAME = "/data/CHARGING_STATION.csv";
    private static final String DELIMITER = ";";

    private final StringProperty applicationTitle = new SimpleStringProperty("ChargingStationsFX");
    private final IntegerProperty selectedCountryId = new SimpleIntegerProperty(-1);

    private final ObservableList<LadestationPM> ladestationen = FXCollections.observableArrayList();

    private final LadestationPM proxy = new LadestationPM();

    private final LanguageSwitcherPM languageSwitcherPM = new LanguageSwitcherPM();

    private FilteredList<LadestationPM> filteredList;

    private String HEADER;

    //Custom Control
    private ObservableList<LocalDate> localDates = FXCollections.observableArrayList();
    private final ObjectProperty<LocalDate> selectedDate = new SimpleObjectProperty<>();

    //Counting
    private IntegerProperty count = new SimpleIntegerProperty();
    private IntegerProperty totalCount = new SimpleIntegerProperty();

    // Redo / Undo
    private final ObservableList<Command> undoStack = FXCollections.observableArrayList();
    private final ObservableList<Command> redoStack = FXCollections.observableArrayList();
    private final BooleanProperty undoDisabled = new SimpleBooleanProperty();
    private final BooleanProperty redoDisabled = new SimpleBooleanProperty();


    private final ChangeListener propertyChangeListenerForUndoSupport = (observable, oldValue, newValue) -> {
        redoStack.clear();
        undoStack.add(0, new ValueChangeCommand(this, (Property) observable, oldValue, newValue));
    };

    public RootPM() {
        ladestationen.addAll(readFromFile());
        this.filteredList = new FilteredList<>(ladestationen);


        //TODO SAVE GEHT NICHT MEHR

        //Custom Control
        for (LadestationPM ladestation : ladestationen) {
            if (!ladestation.getStartDate().isEmpty() && ladestation.getStartDate() != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy");
                //Ausgabe der Daten auf Konsole
                // System.out.println(ladestation.getStartDate());
                LocalDate localDate = LocalDate.parse(ladestation.getStartDate(), formatter);

                localDates.add(localDate);
            }
        }
        //Count
        totalCount.bind(Bindings.size(ladestationen));
        count.bind(Bindings.size(filteredList));

        //Redo Undo
        undoDisabled.bind(Bindings.isEmpty(undoStack));
        redoDisabled.bind(Bindings.isEmpty(redoStack));


        selectedCountryId.addListener((observable, oldValue, newValue) -> {
            LadestationPM oldSelection = getLadestation(oldValue.intValue());
            LadestationPM newSelection = getLadestation(newValue.intValue());

            //TODO Durch diese 3 Zeilen funktioniert Hinzufügen nicht mehr ohne NullPointer Exception
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy");
            LocalDate localDate = LocalDate.parse(newSelection.getStartDate(), formatter);


            setSelectedDate(localDate);

            if (oldSelection != null) {
                unbindFromProxy(oldSelection);
                disableUndoSupport(oldSelection);
            }
            if (newSelection != null) {
                bindToProxy(newSelection);
                enableUndoSupport(newSelection);
            }
            undoStack.clear();
            redoStack.clear();
    });

}

    <T> void setPropertyChangeListenerForUndoSupport(Property<T> property, T newValue) {
        property.removeListener(propertyChangeListenerForUndoSupport);
        property.setValue(newValue);
        property.addListener(propertyChangeListenerForUndoSupport);
    }


    public void undo() {
        if (undoStack.isEmpty()) {
            return;
        }
        Command lastCommand = undoStack.get(0);
        undoStack.remove(0);

        // abspeichern auf RedoStack damit redo ausgeführt werden kann
        redoStack.add(0, lastCommand);

        lastCommand.undo();
    }

    public void redo() {
        if (redoStack.isEmpty()) {
            return;
        }

        Command lastCommand = redoStack.get(0);
        redoStack.remove(0);

        // abspeichern auf  Undo-Stack, damit das Redo wieder zurückgenommen werden kann
        undoStack.add(0, lastCommand);

        lastCommand.redo();
    }


    private void disableUndoSupport(LadestationPM ladestationPM) {
        ladestationPM.companyNameProperty().removeListener(propertyChangeListenerForUndoSupport);
        ladestationPM.strasseNameProperty().removeListener(propertyChangeListenerForUndoSupport);
        ladestationPM.PLZProperty().removeListener(propertyChangeListenerForUndoSupport);
        ladestationPM.ortProperty().removeListener(propertyChangeListenerForUndoSupport);
        ladestationPM.longitudeProperty().removeListener(propertyChangeListenerForUndoSupport);
        ladestationPM.latitudeProperty().removeListener(propertyChangeListenerForUndoSupport);
        ladestationPM.startDateProperty().removeListener(propertyChangeListenerForUndoSupport);
        ladestationPM.loaderTypeProperty().removeListener(propertyChangeListenerForUndoSupport);
        ladestationPM.numberOfChargingPointsProperty().removeListener(propertyChangeListenerForUndoSupport);
        ladestationPM.plugType1Property().removeListener(propertyChangeListenerForUndoSupport);
        ladestationPM.power1KwProperty().removeListener(propertyChangeListenerForUndoSupport);
        ladestationPM.plugType2Property().removeListener(propertyChangeListenerForUndoSupport);
        ladestationPM.power2KwProperty().removeListener(propertyChangeListenerForUndoSupport);
        ladestationPM.plugType3Property().removeListener(propertyChangeListenerForUndoSupport);
        ladestationPM.power3KwProperty().removeListener(propertyChangeListenerForUndoSupport);
        ladestationPM.plugType4Property().removeListener(propertyChangeListenerForUndoSupport);
        ladestationPM.power4KwProperty().removeListener(propertyChangeListenerForUndoSupport);

    }

    private void enableUndoSupport(LadestationPM ladestationPM) {
        ladestationPM.companyNameProperty().addListener(propertyChangeListenerForUndoSupport);
        ladestationPM.strasseNameProperty().addListener(propertyChangeListenerForUndoSupport);
        ladestationPM.PLZProperty().addListener(propertyChangeListenerForUndoSupport);
        ladestationPM.ortProperty().addListener(propertyChangeListenerForUndoSupport);
        ladestationPM.longitudeProperty().addListener(propertyChangeListenerForUndoSupport);
        ladestationPM.latitudeProperty().addListener(propertyChangeListenerForUndoSupport);
        ladestationPM.startDateProperty().addListener(propertyChangeListenerForUndoSupport);
        ladestationPM.loaderTypeProperty().addListener(propertyChangeListenerForUndoSupport);
        ladestationPM.numberOfChargingPointsProperty().addListener(propertyChangeListenerForUndoSupport);
        ladestationPM.plugType1Property().addListener(propertyChangeListenerForUndoSupport);
        ladestationPM.power1KwProperty().addListener(propertyChangeListenerForUndoSupport);
        ladestationPM.plugType2Property().addListener(propertyChangeListenerForUndoSupport);
        ladestationPM.power2KwProperty().addListener(propertyChangeListenerForUndoSupport);
        ladestationPM.plugType3Property().addListener(propertyChangeListenerForUndoSupport);
        ladestationPM.power3KwProperty().addListener(propertyChangeListenerForUndoSupport);
        ladestationPM.plugType4Property().addListener(propertyChangeListenerForUndoSupport);
        ladestationPM.power4KwProperty().addListener(propertyChangeListenerForUndoSupport);
    }

    private void bindToProxy(LadestationPM ladestationPM) {
        proxy.companyNameProperty().bindBidirectional(ladestationPM.companyNameProperty());
        proxy.strasseNameProperty().bindBidirectional(ladestationPM.strasseNameProperty());
        proxy.PLZProperty().bindBidirectional(ladestationPM.PLZProperty());
        proxy.ortProperty().bindBidirectional(ladestationPM.ortProperty());
        proxy.longitudeProperty().bindBidirectional(ladestationPM.longitudeProperty());
        proxy.latitudeProperty().bindBidirectional(ladestationPM.latitudeProperty());
        proxy.startDateProperty().bindBidirectional(ladestationPM.startDateProperty());
        proxy.loaderTypeProperty().bindBidirectional(ladestationPM.loaderTypeProperty());
        proxy.numberOfChargingPointsProperty().bindBidirectional(ladestationPM.numberOfChargingPointsProperty());

        proxy.plugType1Property().bindBidirectional(ladestationPM.plugType1Property());
        proxy.power1KwProperty().bindBidirectional(ladestationPM.power1KwProperty());
        proxy.plugType2Property().bindBidirectional(ladestationPM.plugType2Property());
        proxy.power2KwProperty().bindBidirectional(ladestationPM.power2KwProperty());
        proxy.plugType3Property().bindBidirectional(ladestationPM.plugType3Property());
        proxy.power3KwProperty().bindBidirectional(ladestationPM.power3KwProperty());
        proxy.plugType4Property().bindBidirectional(ladestationPM.plugType4Property());
        proxy.power4KwProperty().bindBidirectional(ladestationPM.power4KwProperty());

    }

    private void unbindFromProxy(LadestationPM ladestationPM) {
        proxy.companyNameProperty().unbindBidirectional(ladestationPM.companyNameProperty());
        proxy.strasseNameProperty().unbindBidirectional(ladestationPM.strasseNameProperty());
        proxy.PLZProperty().unbindBidirectional(ladestationPM.PLZProperty());
        proxy.ortProperty().unbindBidirectional(ladestationPM.ortProperty());
        proxy.longitudeProperty().unbindBidirectional(ladestationPM.longitudeProperty());
        proxy.latitudeProperty().unbindBidirectional(ladestationPM.latitudeProperty());
        proxy.startDateProperty().unbindBidirectional(ladestationPM.startDateProperty());
        proxy.loaderTypeProperty().unbindBidirectional(ladestationPM.loaderTypeProperty());
        proxy.numberOfChargingPointsProperty().unbindBidirectional(ladestationPM.numberOfChargingPointsProperty());
        proxy.startDateProperty().unbindBidirectional(ladestationPM.startDateProperty());
        proxy.loaderTypeProperty().unbindBidirectional(ladestationPM.loaderTypeProperty());
        proxy.numberOfChargingPointsProperty().unbindBidirectional(ladestationPM.numberOfChargingPointsProperty());
        proxy.plugType1Property().unbindBidirectional(ladestationPM.plugType1Property());
        proxy.power1KwProperty().unbindBidirectional(ladestationPM.power1KwProperty());
        proxy.plugType2Property().unbindBidirectional(ladestationPM.plugType2Property());
        proxy.power2KwProperty().unbindBidirectional(ladestationPM.power2KwProperty());
        proxy.plugType3Property().unbindBidirectional(ladestationPM.plugType3Property());
        proxy.power3KwProperty().unbindBidirectional(ladestationPM.power3KwProperty());
        proxy.plugType4Property().unbindBidirectional(ladestationPM.plugType4Property());
        proxy.power4KwProperty().unbindBidirectional(ladestationPM.power4KwProperty());

    }


    private LadestationPM getLadestation(int d) {
        return ladestationen.stream()
                .filter(LadestationPM -> LadestationPM.getENTITY_ID() == d)
                .findAny().orElse(null);

    }


    public LadestationPM getLadestationProxy() {
        return proxy;
    }

    private List<LadestationPM> readFromFile() {
        //um etwas herauslesen zu können --> bufferedReader
        try (BufferedReader reader = getReader(FILE_NAME)) {
            //1. Zeile muss übersprungen werden, da hier keine Wahlergebnisse ausgegeben werden
            HEADER = reader.readLine();
            return reader.lines()
                    //.skip(1)
                    // ich kriege eine Zeile ein und aus dieser Zeile mache ich eine neue Instanz von LadestationPM
                    //von einem grossen String ein Array von String machen mittels .split
                    //Parameter 2 ist Anzahl der Kolonnen / Spalten -> muss man jedoch nicht umebdingt schreiben
                    .map(line -> new LadestationPM(line.split(DELIMITER))).collect(Collectors.toList());
            //.collect(Collectors.toList());
        }

        //IO Exception sind Programmierfehler
        catch (IOException e) {
            throw new IllegalArgumentException("read failed");

        }
    }


    private BufferedReader getReader(String fileName) {
        InputStream inputStream = getClass().getResourceAsStream(fileName);  // damit kann man vom File lesen
        InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8); // lesen von Text-File

        return new BufferedReader(reader);  // damit man zeilenweise lesen kann
    }


    //man kann sich hiermit vom FileName ein Writer geben lassen
    private BufferedWriter getWriter(String fileName) {
        try {
            String file = getClass().getResource(fileName).getFile();
            return new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new IllegalStateException("wrong file " + fileName);
        }
    }


    private Stream<String> getStreamOfLines(String fileName) {
        try {
            return Files.lines(getPath(fileName), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }


    private Path getPath(String fileName) {
        try {
            return Paths.get(getClass().getResource(fileName).toURI());
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    }


    public void save() {
        try (BufferedWriter writer = getWriter(FILE_NAME)) {
            writer.write(HEADER);
            writer.newLine();
            ladestationen.stream()
                    .map(allLocations -> allLocations.infoAsLine(DELIMITER))
                    .forEach(line -> {
                        try {
                            writer.write(line);
                            writer.newLine();
                        } catch (IOException e) {
                            throw new IllegalStateException(e);
                        }
                    });
        } catch (IOException e) {
            throw new IllegalStateException("unable to save");
        }
    }


    public void delete() {
        // ladestationen.remove(getLadestation(selectedCountryIdProperty().get()));
        ladestationen.remove(ladestationen.indexOf(getLadestation(selectedCountryId.intValue())));

    }


    public void addNewStation() {
        LadestationPM newChargingStation = new LadestationPM();

        int uniqueID = ladestationen.size() + 1;
        boolean existing = true;
        while (existing) {
            uniqueID++;
            existing = false;
            for (LadestationPM station : ladestationen) {
                if (station.getENTITY_ID() == uniqueID) {
                    existing = true;
                }
            }
        }
        newChargingStation.setENTITY_ID(uniqueID);
        //Default setzten beim Hinzufügen neuer Station
        newChargingStation.setLoaderType("Normalladeeinrichtung");

        ladestationen.add(newChargingStation);
        setSelectedCountryId(uniqueID);

    }

    public void filter(String text) {
        if (text != null) {
            //Filter auf Strasse
            Predicate<LadestationPM> strassePredicate = ladestationen -> ladestationen.getStrasseName().contains(text);
            Predicate<LadestationPM> ortPredicate = ladestationen -> ladestationen.getOrt().contains(text);
            Predicate<LadestationPM> PLZPredicate = ladestationen -> Integer.toString(ladestationen.getPLZ()).contains(text);
            Predicate<LadestationPM> numberOfCharginPointsPredicate = ladestationen -> Integer.toString(ladestationen.getNumberOfChargingPoints()).contains(text);
            filteredList.setPredicate(strassePredicate.or(ortPredicate).or(PLZPredicate).or(numberOfCharginPointsPredicate));
        } else {
            filteredList.setPredicate(null);
        }


    }


    //Setter und Getter


    public static String getFileName() {
        return FILE_NAME;
    }

    public static String getDELIMITER() {
        return DELIMITER;
    }

    public String getApplicationTitle() {
        return applicationTitle.get();
    }

    public StringProperty applicationTitleProperty() {
        return applicationTitle;
    }

    public void setApplicationTitle(String applicationTitle) {
        this.applicationTitle.set(applicationTitle);
    }

    public int getSelectedCountryId() {
        return selectedCountryId.get();
    }

    public IntegerProperty selectedCountryIdProperty() {
        return selectedCountryId;
    }

    public void setSelectedCountryId(int selectedCountryId) {
        this.selectedCountryId.set(selectedCountryId);
    }

    public ObservableList<LadestationPM> getLadestationen() {
        return ladestationen;
    }

    public LadestationPM getProxy() {
        return proxy;
    }

    public LanguageSwitcherPM getLanguageSwitcherPM() {
        return languageSwitcherPM;
    }

    public ObservableList<Command> getUndoStack() {
        return undoStack;
    }

    public ObservableList<Command> getRedoStack() {
        return redoStack;
    }

    public boolean isUndoDisabled() {
        return undoDisabled.get();
    }

    public BooleanProperty undoDisabledProperty() {
        return undoDisabled;
    }

    public void setUndoDisabled(boolean undoDisabled) {
        this.undoDisabled.set(undoDisabled);
    }

    public boolean isRedoDisabled() {
        return redoDisabled.get();
    }

    public BooleanProperty redoDisabledProperty() {
        return redoDisabled;
    }

    public void setRedoDisabled(boolean redoDisabled) {
        this.redoDisabled.set(redoDisabled);
    }

    public ChangeListener getPropertyChangeListenerForUndoSupport() {
        return propertyChangeListenerForUndoSupport;
    }


    public FilteredList<LadestationPM> getFilteredList() {
        return filteredList;
    }

    public void setFilteredList(FilteredList<LadestationPM> filteredList) {
        this.filteredList = filteredList;
    }

    public int getCount() {
        return count.get();
    }

    public IntegerProperty countProperty() {
        return count;
    }

    public void setCount(int count) {
        this.count.set(count);
    }

    public int getTotalCount() {
        return totalCount.get();
    }

    public IntegerProperty totalCountProperty() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount.set(totalCount);
    }

    public String getHEADER() {
        return HEADER;
    }

    public void setHEADER(String HEADER) {
        this.HEADER = HEADER;
    }

    public ObservableList<LocalDate> getLocalDates() {
        return localDates;
    }

    public void setLocalDates(ObservableList<LocalDate> localDates) {
        this.localDates = localDates;
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
}
