package ch.fhnw.chargingstationsfx.presentationmodel;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

//Europe PM

public class RootPM {
    private static final String FILE_NAME = "/data/CHARGING_STATION.csv";
    private static final String DELIMITER = ";";

    private final StringProperty applicationTitle = new SimpleStringProperty("ChargingStationsFX");

    private final IntegerProperty selectedCountryId = new SimpleIntegerProperty(-1);
    private final ObservableList<LadestationPM> resultate = FXCollections.observableArrayList();

    private LadestationPM actualLadestation = new LadestationPM();
    private final LadestationPM proxy = new LadestationPM();

    private final LanguageSwitcherPM languageSwitcherPM = new LanguageSwitcherPM();

    private String HEADER;

    public int getSelectedCountryId() {
        return selectedCountryId.get();
    }

    public LanguageSwitcherPM getLanguageSwitcherPM() {
        return languageSwitcherPM;
    }

    public IntegerProperty selectedCountryIdProperty() {
        return selectedCountryId;
    }

    public void setSelectedCountryId(int selectedCountryId) {
        this.selectedCountryId.set(selectedCountryId);
    }

    public LadestationPM getActualLadestation() {
        return actualLadestation;
    }

    public void setActualLadestation(LadestationPM actualLadestation) {
        this.actualLadestation = actualLadestation;
    }

    public LadestationPM getProxy() {
        return proxy;
    }

    public RootPM() {
        resultate.addAll(readFromFile());

        selectedCountryId.addListener((observable, oldValue, newValue) -> {
            LadestationPM oldSelection = getLadestation(oldValue.intValue());
            LadestationPM newSelection = getLadestation(newValue.intValue());

            if (oldSelection != null) {
                proxy.companyNameProperty().unbindBidirectional(oldSelection.companyNameProperty());
                proxy.strasseNameProperty().unbindBidirectional(oldSelection.strasseNameProperty());
                proxy.PLZProperty().unbindBidirectional(oldSelection.PLZProperty());
                proxy.ortProperty().unbindBidirectional(oldSelection.ortProperty());
                proxy.longitudeProperty().unbindBidirectional(oldSelection.longitudeProperty());
                proxy.latitudeProperty().unbindBidirectional(oldSelection.latitudeProperty());
                proxy.startDateProperty().unbindBidirectional(oldSelection.startDateProperty());
                proxy.loaderTypeProperty().unbindBidirectional(oldSelection.loaderTypeProperty());
                proxy.numberOfChargingPointsProperty().unbindBidirectional(oldSelection.numberOfChargingPointsProperty());
                proxy.startDateProperty().unbindBidirectional(oldSelection.startDateProperty());
                proxy.loaderTypeProperty().unbindBidirectional(oldSelection.loaderTypeProperty());
                proxy.numberOfChargingPointsProperty().unbindBidirectional(oldSelection.numberOfChargingPointsProperty());
                proxy.plugType1Property().unbindBidirectional(oldSelection.plugType1Property());
                proxy.power1KwProperty().unbindBidirectional(oldSelection.power1KwProperty());
                proxy.plugType2Property().unbindBidirectional(oldSelection.plugType2Property());
                proxy.power2KwProperty().unbindBidirectional(oldSelection.power2KwProperty());
                proxy.plugType3Property().unbindBidirectional(oldSelection.plugType3Property());
                proxy.power3KwProperty().unbindBidirectional(oldSelection.power3KwProperty());
                proxy.plugType4Property().unbindBidirectional(oldSelection.plugType4Property());
                proxy.power4KwProperty().unbindBidirectional(oldSelection.power4KwProperty());

            }

            if (newSelection != null) {
                proxy.companyNameProperty().bindBidirectional(newSelection.companyNameProperty());
                proxy.strasseNameProperty().bindBidirectional(newSelection.strasseNameProperty());
                proxy.PLZProperty().bindBidirectional(newSelection.PLZProperty());
                proxy.ortProperty().bindBidirectional(newSelection.ortProperty());
                proxy.longitudeProperty().bindBidirectional(newSelection.longitudeProperty());
                proxy.latitudeProperty().bindBidirectional(newSelection.latitudeProperty());
                proxy.startDateProperty().bindBidirectional(newSelection.startDateProperty());
                proxy.loaderTypeProperty().bindBidirectional(newSelection.loaderTypeProperty());
                proxy.numberOfChargingPointsProperty().bindBidirectional(newSelection.numberOfChargingPointsProperty());
                proxy.startDateProperty().bindBidirectional(newSelection.startDateProperty());
                proxy.loaderTypeProperty().bindBidirectional(newSelection.loaderTypeProperty());
                proxy.numberOfChargingPointsProperty().bindBidirectional(newSelection.numberOfChargingPointsProperty());

                proxy.plugType1Property().bindBidirectional(newSelection.plugType1Property());
                proxy.power1KwProperty().bindBidirectional(newSelection.power1KwProperty());
                proxy.plugType2Property().bindBidirectional(newSelection.plugType2Property());
                proxy.power2KwProperty().bindBidirectional(newSelection.power2KwProperty());
                proxy.plugType3Property().bindBidirectional(newSelection.plugType3Property());
                proxy.power3KwProperty().bindBidirectional(newSelection.power3KwProperty());
                proxy.plugType4Property().bindBidirectional(newSelection.plugType4Property());
                proxy.power4KwProperty().bindBidirectional(newSelection.power4KwProperty());


            }
        });
//

    }

    private LadestationPM getLadestation(int d){
        return resultate.stream()
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





    public void save() {
        //todo implement
        try (BufferedWriter writer = getWriter(FILE_NAME)) {
            writer.write(HEADER);
            writer.newLine();
            resultate.stream()
                    .map(chargingStations -> chargingStations.infoAsLine(DELIMITER))
                    .forEach(line -> {
                        try {
                            writer.write(line);
                            writer.newLine();
                        } catch (IOException e) {
                            throw new IllegalStateException(e);
                        }
                    });
        } catch (IOException e) {
            throw new IllegalStateException("save failed");
        }
    }


    public void delete() {
        //todo implement
        resultate.remove(resultate.indexOf(getLadestation(selectedCountryId.intValue())));


}

    public void add() {
        //todo implement
        //Fügt eindeutige Id hinzu
        int uniqueId =  resultate.size() + 100000; // todo: nicht sicher unique, bessere id finden

      //todo doesnt work!! ask
      //  this.resultate.add(new LadestationPM(uniqueId);

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

        // all getters and setters
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

//    public String getGreeting() {
//        return greeting.get();
//    }
//
//    public StringProperty greetingProperty() {
//        return greeting;
//    }
//
//    public void setGreeting(String greeting) {
//        this.greeting.set(greeting);
//      }

    public ObservableList<LadestationPM> getResultate() {
        return resultate;
    }
}
