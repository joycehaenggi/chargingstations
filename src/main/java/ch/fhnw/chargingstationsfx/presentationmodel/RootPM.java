package ch.fhnw.chargingstationsfx.presentationmodel;

import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.swing.event.ChangeListener;
import java.io.*;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

//Europe PM

public class RootPM {
    private static final String FILE_NAME = "/data/CHARGING_STATION.csv";
    private static final String DELIMITER = ";";

    private final StringProperty applicationTitle = new SimpleStringProperty("ChargingStationsFX");

    private final ObservableList<Command> undoStack = FXCollections.observableArrayList();
    private final ObservableList<Command> redoStack = FXCollections.observableArrayList();

    private final IntegerProperty selectedCountryId = new SimpleIntegerProperty(-1);

    private final BooleanProperty undoDisabled = new SimpleBooleanProperty();
    private final BooleanProperty redoDisabled = new SimpleBooleanProperty();

    private final ObservableList<LadestationPM> ladestationen = FXCollections.observableArrayList();

    private LadestationPM actualLadestation = new LadestationPM();
    private final LadestationPM proxy = new LadestationPM();

    private final LanguageSwitcherPM languageSwitcherPM = new LanguageSwitcherPM();


    public LadestationPM getProxy() {
        return proxy;
    }

    private final ChangeListener propertyChangeListenerForUndoSupport = (observable, oldValue, newValue) -> {
        redoStack.clear();
        System.out.println(observable);
        undoStack.add(0, new ValueChangeCommand(RootPM.this, (Property) observable, oldValue , newValue));
    };


    public RootPM() {
        ladestationen.addAll(readFromFile());

        undoDisabled.bind(Bindings.isEmpty(undoStack));
        redoDisabled.bind(Bindings.isEmpty(redoStack));



        selectedCountryId.addListener((observable, oldValue, newValue) -> {
            LadestationPM oldSelection = getLadestation(oldValue.intValue());
            LadestationPM newSelection = getLadestation(newValue.intValue());

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

    <T> void setPropertyValueWithoutUndoSupport(Property<T> property, T newValue) {
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

        //todo alle einfügen

    }
    private void enableUndoSupport(LadestationPM ladestationPM) {
        ladestationPM.companyNameProperty().addListener(propertyChangeListenerForUndoSupport);
    }


            private void bindToProxy(LadestationPM ladestationPM){
                proxy.companyNameProperty().bindBidirectional(ladestationPM.companyNameProperty());
                proxy.strasseNameProperty().bindBidirectional(ladestationPM.strasseNameProperty());
                proxy.PLZProperty().bindBidirectional(ladestationPM.PLZProperty());
                proxy.ortProperty().bindBidirectional(ladestationPM.ortProperty());
                proxy.longitudeProperty().bindBidirectional(ladestationPM.longitudeProperty());
                proxy.latitudeProperty().bindBidirectional(ladestationPM.latitudeProperty());
                proxy.startDateProperty().bindBidirectional(ladestationPM.startDateProperty());
                proxy.loaderTypeProperty().bindBidirectional(ladestationPM.loaderTypeProperty());
                proxy.numberOfChargingPointsProperty().bindBidirectional(ladestationPM.numberOfChargingPointsProperty());
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

    private void unbindFromProxy(LadestationPM ladestationPM){
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

    private LadestationPM getLadestation(int d){
        return ladestationen.stream()
                .filter(LadestationPM -> LadestationPM.getENTITY_ID() == d)
                .findAny().orElse(null);

    }

    //added
    public LadestationPM getLadestationProxy(){
        return proxy;
    }

    private List<LadestationPM> readFromFile() {
        //um etwas herauslesen zu können --> bufferedReader
        try (BufferedReader reader = getReader(FILE_NAME)) {
            //1. Zeile muss übersprungen werden, da hier keine Wahlergebnisse ausgegeben werden
            return reader.lines()
                    .skip(1)
                    // ich kriege eine Zeile ein und aus dieser Zeile mache ich eine neue Instanz von LadestationPM
                    //von einem grossen String ein Array von String machen mittels .split
                    //Parameter 2 ist Anzahl der Kolonnen / Spalten -> muss man jedoch nicht umebdingt schreiben
                    .map(line -> new LadestationPM(line.split(DELIMITER, 22)))
                    .collect(Collectors.toList());
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

    private Path getPath(String fileName)  {
        try {
            return Paths.get(getClass().getResource(fileName).toURI());
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    }


    public void save() {
        try(BufferedWriter writer = Files.newBufferedWriter(getPath(FILE_NAME))){
            writer.write("header");
            writer.newLine();
            ladestationen.stream()
                    .map(allLocations -> allLocations.infoAsLine(DELIMITER))
                    .forEach(line ->{
                        try {
                            writer.write(line);
                            writer.newLine();
                        } catch (IOException e){
                            throw new IllegalStateException(e);
                        }
                    });
        }catch (IOException e){
            throw new IllegalStateException("could not save");
        }
    }

    //TODO
    public void delete() {
        ladestationen.remove(getLadestation(selectedCountryIdProperty().get()));


}

    public void add() {
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

        ladestationen.add(newChargingStation);

        setSelectedCountryId(uniqueID);


        // all getters and setters
    }

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

    public ObservableList<Command> getUndoStack() {
        return undoStack;
    }

    public ObservableList<Command> getRedoStack() {
        return redoStack;
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

    public ObservableList<LadestationPM> getLadestationen() {
        return ladestationen;
    }

    public LadestationPM getActualLadestation() {
        return actualLadestation;
    }

    public void setActualLadestation(LadestationPM actualLadestation) {
        this.actualLadestation = actualLadestation;
    }

    public LanguageSwitcherPM getLanguageSwitcherPM() {
        return languageSwitcherPM;
    }

    public ChangeListener getPropertyChangeListenerForUndoSupport() {
        return propertyChangeListenerForUndoSupport;
    }
}
