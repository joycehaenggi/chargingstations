package ch.fhnw.chargingstationsfx.presentationmodel;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

import java.time.LocalDate;

/**
 * @author Dieter Holz
 */
//CountryPM
public class LadestationPM {


    private final StringProperty companyName = new SimpleStringProperty();
    private final StringProperty strasseName = new SimpleStringProperty();
    private final StringProperty ort = new SimpleStringProperty();
    private final StringProperty loaderType = new SimpleStringProperty();
    private final StringProperty plugType1 = new SimpleStringProperty();
    private final StringProperty plugType2 = new SimpleStringProperty();
    private final StringProperty plugType3 = new SimpleStringProperty();
    private final StringProperty plugType4 = new SimpleStringProperty();

    private final StringProperty startDate = new SimpleStringProperty();

    private final IntegerProperty ENTITY_ID = new SimpleIntegerProperty();
    private final IntegerProperty PLZ = new SimpleIntegerProperty();
    private final IntegerProperty numberOfChargingPoints = new SimpleIntegerProperty();

    private final DoubleProperty longitude = new SimpleDoubleProperty();
    private final DoubleProperty latitude = new SimpleDoubleProperty();

    private final DoubleProperty power1Kw = new SimpleDoubleProperty();
    private final DoubleProperty power2Kw = new SimpleDoubleProperty();
    private final DoubleProperty power3Kw = new SimpleDoubleProperty();
    private final DoubleProperty power4Kw = new SimpleDoubleProperty();


    private final DoubleBinding connectionPowerKw = power1Kw.add(power2Kw).add(power3Kw).add(power4Kw);



    //für Proxy
    public LadestationPM() {

    }

    //aus 1 Zeile wird ein Array von Strings gemacht, für jede Kolonne ein Array
    // Array von Strings
    public LadestationPM(String[] line) {
        // Stelle 0: Nr.
        setENTITY_ID(Integer.valueOf(line[0]));
        setCompanyName(line[1]);
        setStrasseName(line[2]);
        setPLZ(Integer.valueOf(line[3]));
        setOrt(line[4]);
        setLongitude(Double.valueOf(line[5]));
        setLatitude(Double.valueOf(line[6]));
        setStartDate(line[7]);
        setLoaderType(line[8]);
        setNumberOfChargingPoints(Integer.valueOf(line[9]));
        setPlugType1(line[11]);
        setPower1Kw(Double.valueOf(line[12]));
        setPlugType2(line[13]);
        setPower2Kw(Double.valueOf(line[14]));
        setPlugType3(line[15]);
        setPower3Kw(Double.valueOf(line[16]));
        setPlugType4(line[17]);
        setPower4Kw(Double.valueOf(line[18]));


    }


    public String infoAsLine(String delimiter) {
        return String.join(delimiter,
                getENTITY_ID().toString(),
                 getCompanyName(),
                getStrasseName(),
                getPLZ().toString(),
                getOrt(),
                getLongitude().toString(),
                Double.toString(getLatitude()),
                getStartDate(),
                getLoaderType(),
                Integer.toString(getNumberOfChargingPoints()),
                Double.toString(getConnectionPowerKw()),
                getPlugType1(),
                Double.toString(getPower1Kw()),
                getPlugType2(),
                Double.toString(getPower2Kw()),
                getPlugType3(),
                Double.toString(getPower3Kw()),
                getPlugType4(),
                Double.toString(getPower4Kw())


        );
    }

    //getters and setters


    public String getCompanyName() {
        return companyName.get();
    }

    public StringProperty companyNameProperty() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName.set(companyName);
    }

    public String getStrasseName() {
        return strasseName.get();
    }

    public StringProperty strasseNameProperty() {
        return strasseName;
    }

    public void setStrasseName(String strasseName) {
        this.strasseName.set(strasseName);
    }

    public String getOrt() {
        return ort.get();
    }

    public StringProperty ortProperty() {
        return ort;
    }

    public void setOrt(String ort) {
        this.ort.set(ort);
    }

    public String getLoaderType() {
        return loaderType.get();
    }

    public StringProperty loaderTypeProperty() {
        return loaderType;
    }

    public void setLoaderType(String loaderType) {
        this.loaderType.set(loaderType);
    }

    public String getPlugType1() {
        return plugType1.get();
    }

    public StringProperty plugType1Property() {
        return plugType1;
    }

    public void setPlugType1(String plugType1) {
        this.plugType1.set(plugType1);
    }

    public String getPlugType2() {
        return plugType2.get();
    }

    public StringProperty plugType2Property() {
        return plugType2;
    }

    public void setPlugType2(String plugType2) {
        this.plugType2.set(plugType2);
    }

    public String getPlugType3() {
        return plugType3.get();
    }

    public StringProperty plugType3Property() {
        return plugType3;
    }

    public void setPlugType3(String plugType3) {
        this.plugType3.set(plugType3);
    }

    public String getPlugType4() {
        return plugType4.get();
    }

    public StringProperty plugType4Property() {
        return plugType4;
    }

    public void setPlugType4(String plugType4) {
        this.plugType4.set(plugType4);
    }

    public String getStartDate() {
        return startDate.get();
    }

    public StringProperty startDateProperty() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate.set(startDate);
    }

    public Integer getENTITY_ID() {
        return ENTITY_ID.get();
    }

    public IntegerProperty ENTITY_IDProperty() {
        return ENTITY_ID;
    }

    public void setENTITY_ID(int ENTITY_ID) {
        this.ENTITY_ID.set(ENTITY_ID);
    }

    public Integer getPLZ() {
        return PLZ.get();
    }

    public IntegerProperty PLZProperty() {
        return PLZ;
    }

    public void setPLZ(int PLZ) {
        this.PLZ.set(PLZ);
    }

    public int getNumberOfChargingPoints() {
        return numberOfChargingPoints.get();
    }

    public IntegerProperty numberOfChargingPointsProperty() {
        return numberOfChargingPoints;
    }

    public void setNumberOfChargingPoints(int numberOfChargingPoints) {
        this.numberOfChargingPoints.set(numberOfChargingPoints);
    }

    public Double getLongitude() {
        return longitude.get();
    }

    public DoubleProperty longitudeProperty() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude.set(longitude);
    }

    public double getLatitude() {
        return latitude.get();
    }

    public DoubleProperty latitudeProperty() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude.set(latitude);
    }

    public double getConnectionPowerKw() {
        return connectionPowerKw.get();
    }

    public DoubleBinding connectionPowerKwProperty() {
        return connectionPowerKw;
    }

    public double getPower1Kw() {
        return power1Kw.get();
    }

    public DoubleProperty power1KwProperty() {
        return power1Kw;
    }

    public void setPower1Kw(double power1Kw) {
        this.power1Kw.set(power1Kw);
    }

    public double getPower2Kw() {
        return power2Kw.get();
    }

    public DoubleProperty power2KwProperty() {
        return power2Kw;
    }

    public void setPower2Kw(double power2Kw) {
        this.power2Kw.set(power2Kw);
    }

    public double getPower3Kw() {
        return power3Kw.get();
    }

    public DoubleProperty power3KwProperty() {
        return power3Kw;
    }

    public void setPower3Kw(double power3Kw) {
        this.power3Kw.set(power3Kw);
    }

    public double getPower4Kw() {
        return power4Kw.get();
    }

    public DoubleProperty power4KwProperty() {
        return power4Kw;
    }

    public void setPower4Kw(double power4Kw) {
        this.power4Kw.set(power4Kw);
    }

}
