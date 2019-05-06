package ch.fhnw.chargingstationsfx.presentationmodel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public class RootPM {
    private static final String FILE_NAME = "/data/CHARGING_STATION.csv";
    private static final String DELIMITER = ";";

    private final StringProperty applicationTitle = new SimpleStringProperty("ChargingStationsFX");
//    private final StringProperty greeting = new SimpleStringProperty("Hello World!");

    private final ObservableList<Ladestation> resultate = FXCollections.observableArrayList();

    public RootPM() {         resultate.addAll(readFromFile());}

    private List<Ladestation> readFromFile() {
        //um etwas herauslesen zu können --> bufferedReader
        try (BufferedReader reader = getReader(FILE_NAME)) {
            //1. Zeile muss übersprungen werden, da hier keine Wahlergebnisse ausgegeben werden
            return reader.lines()
                    .skip(1)
                    // ich kriege eine Zeile ein und aus dieser Zeile mache ich eine neue Instanz von Ladestation
                    //von einem grossen String ein Array von String machen mittels .split
                    //Parameter 2 ist Anzahl der Kolonnen / Spalten -> muss man jedoch nicht umebdingt schreiben
                    .map(line -> new Ladestation(line.split(DELIMITER, 22)))
                    .collect(Collectors.toList());
        }

        //IO Exception sind Programmierfehler
        catch (IOException e) {
            throw new IllegalArgumentException("read failed");

        }
    }



    public void save() {
        //todo implement
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

    public ObservableList<Ladestation> getResultate() {
        return resultate;
    }
}
