package ch.fhnw.chargingstationsfx.presentationmodel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


public class LanguageSwitcherPM {

    public enum Lang{
        DE, EN
    }
    public enum MultilanguageText{
       //alle Labels von LadestationPM müssen hier in DE und EN aufgeführt werden

        GERMAN_BUTTON_TEXT("German", "Deutsch"),
        ENGLISH_BUTTON_TEXT("English", "Englisch"),
        BETREIBER_TEXT("Company Name", "Betreiber"),
        STRASSE_TEXT("Street", "Strasse"),
        PLZ_TEXT("Zip", "PLZ"),
        ORT_TEXT("City", "Ort"),
        LONGITUDE_TEXT("Longitude", "Längengrad"),
        LATITUDE_TEXT("Latitude", "Breitengrad"),
        INBETRIEBNAHME_TEXT("Startup Date", "Inbetriebnahme"),
        LOADERTYPE_TEXT("Loader Type", "Typ"),
        NUMBER_OF_CHARGINGPOINTS_TEXT("Number of Chargingpoints", "Anzahl Ladestationen"),
        CONNECTION_POWER_KW_TEXT("Connection Power [kW]", "Anschlussleistung  [kW]"),
        PLUGTYPE1_TEXT("Plug Typ 1", "1-Steckertypen"),
        POWER1KW_TEXT("Power 1 [kW]", "Leistung [kW]"),
        PLUGTYPE2_TEXT("Plug Typ 2", "2-Steckertypen"),
        POWER2KW_TEXT("Power 2 [kW]", "Leistung [kW]"),
        PLUGTYPE3_TEXT("Plug Typ 3", "3-Steckertypen"),
        POWER3KW_TEXT("Power 3 [kW]", "Leistung [kW]"),
        PLUGTYPE4_TEXT("Plug Typ 4", "4-Steckertypen"),
        POWER4KW_TEXT("Power 4 [kW]", "Leistung [kW]"),

        //HeaderUI
        ADD_TEXT("Add", "Hinzufügen"),
        DELETE_TEXT("Delete", "Löschen"),
        SAVE_TEXT("Save", "Speichern"),
        REDO_TEXT("Redo", "Wiederholen"),
        UNDO_TEXT("Undo", "Rückgängig"),
        SEARCH_TEXT("Search:", "Suchen:");

        private final String englishLabel;
        private final String germanLabel;


        MultilanguageText(String englishLabel, String germanLablel){
            this.englishLabel = englishLabel;
            this.germanLabel = germanLablel;
        }

        public String getEnglishLabel() {
            return englishLabel;
        }

        public String getGermanLabel() {
            return germanLabel;
        }

                public String getText(Lang lang){
            switch(lang){
                case DE:
                    return getGermanLabel();
                case EN:
                    return getEnglishLabel();
                default:
                    return getEnglishLabel();
            }
        }
    }

    private final StringProperty germanButtonText = new SimpleStringProperty();
    private final StringProperty englishButtonText = new SimpleStringProperty();
    private final StringProperty firmaText = new SimpleStringProperty();
    private final StringProperty strasseText = new SimpleStringProperty();
    private final StringProperty PLZText = new SimpleStringProperty();
    private final StringProperty ortText = new SimpleStringProperty();
    private final StringProperty longitudeText = new SimpleStringProperty();
    private final StringProperty latitudeText = new SimpleStringProperty();
    private final StringProperty inbetriebnahmeText = new SimpleStringProperty();
    private final StringProperty loadertypeText = new SimpleStringProperty();
    private final StringProperty numberOfChargingPointsText = new SimpleStringProperty();
    private final StringProperty connectionPowerKWText = new SimpleStringProperty();
    private final StringProperty plugtype1Text = new SimpleStringProperty();
    private final StringProperty power1KWText = new SimpleStringProperty();
    private final StringProperty plugtype2Text = new SimpleStringProperty();
    private final StringProperty power2KWText = new SimpleStringProperty();
    private final StringProperty plugtype3Text = new SimpleStringProperty();
    private final StringProperty power3KWText = new SimpleStringProperty();
    private final StringProperty plugtype4Text = new SimpleStringProperty();
    private final StringProperty power4KWText = new SimpleStringProperty();

    //HeaderUI
    private final StringProperty addText = new SimpleStringProperty();
    private final StringProperty deleteText = new SimpleStringProperty();
    private final StringProperty saveText = new SimpleStringProperty();
    private final StringProperty redoText = new SimpleStringProperty();
    private final StringProperty undoText = new SimpleStringProperty();
    private final StringProperty searchText = new SimpleStringProperty();



    public LanguageSwitcherPM(){
        setLanguage(Lang.DE);
    }


    public String getGermanButtonText() {
        return germanButtonText.get();
    }

    public String getEnglishButtonText() {
        return englishButtonText.get();
    }


    public void setLanguage(Lang lang){
        setGermanButtonText(MultilanguageText.GERMAN_BUTTON_TEXT.getText(lang));
        setEnglishButtonText(MultilanguageText.ENGLISH_BUTTON_TEXT.getText(lang));
        setFirmaText(MultilanguageText.BETREIBER_TEXT.getText(lang));
        setStrasseText(MultilanguageText.STRASSE_TEXT.getText(lang));
        setPLZText(MultilanguageText.PLZ_TEXT.getText(lang));
        setOrtText(MultilanguageText.ORT_TEXT.getText(lang));
        setLongitudeText(MultilanguageText.LONGITUDE_TEXT.getText(lang));
        setLatitudeText(MultilanguageText.LATITUDE_TEXT.getText(lang));
        setInbetriebnahmeText(MultilanguageText.INBETRIEBNAHME_TEXT.getText(lang));
        setLoadertypeText(MultilanguageText.LOADERTYPE_TEXT.getText(lang));
        setNumberOfChargingPointsText(MultilanguageText.NUMBER_OF_CHARGINGPOINTS_TEXT.getText(lang));
        setConnectionPowerKWText(MultilanguageText.CONNECTION_POWER_KW_TEXT.getText(lang));
        setPlugtype1Text(MultilanguageText.PLUGTYPE1_TEXT.getText(lang));
        setPower1KWText(MultilanguageText.POWER1KW_TEXT.getText(lang));
        setPlugtype2Text(MultilanguageText.PLUGTYPE2_TEXT.getText(lang));
        setPower2KWText(MultilanguageText.POWER2KW_TEXT.getText(lang));
        setPlugtype3Text(MultilanguageText.PLUGTYPE3_TEXT.getText(lang));
        setPower3KWText(MultilanguageText.POWER3KW_TEXT.getText(lang));
        setPlugtype4Text(MultilanguageText.PLUGTYPE4_TEXT.getText(lang));
        setPower4KWText(MultilanguageText.POWER4KW_TEXT.getText(lang));
        setAddText(MultilanguageText.ADD_TEXT.getText(lang));
        setDeleteText(MultilanguageText.DELETE_TEXT.getText(lang));
        setSaveText(MultilanguageText.SAVE_TEXT.getText(lang));
        setUndoText(MultilanguageText.UNDO_TEXT.getText(lang));
        setRedoText(MultilanguageText.REDO_TEXT.getText(lang));
        setSearchText(MultilanguageText.SEARCH_TEXT.getText(lang));

    }


    public StringProperty germanButtonTextProperty() {
        return germanButtonText;
    }

    private void setGermanButtonText(String germanButtonText) {
        this.germanButtonText.set(germanButtonText);
    }


    public StringProperty englishButtonTextProperty() {
        return englishButtonText;
    }

    private void setEnglishButtonText(String englishButtonText) {
        this.englishButtonText.set(englishButtonText);
    }

    public String getFirmaText() {
        return firmaText.get();
    }

    public StringProperty firmaTextProperty() {
        return firmaText;
    }

    public void setFirmaText(String firmaText) {
        this.firmaText.set(firmaText);
    }

    public String getStrasseText() {
        return strasseText.get();
    }

    public StringProperty strasseTextProperty() {
        return strasseText;
    }

    public void setStrasseText(String strasseText) {
        this.strasseText.set(strasseText);
    }

    public String getOrtText() {
        return ortText.get();
    }

    public StringProperty ortTextProperty() {
        return ortText;
    }

    public void setOrtText(String ortText) {
        this.ortText.set(ortText);
    }

    public String getLongitudeText() {
        return longitudeText.get();
    }

    public StringProperty longitudeTextProperty() {
        return longitudeText;
    }

    public void setLongitudeText(String longitudeText) {
        this.longitudeText.set(longitudeText);
    }

    public String getLatitudeText() {
        return latitudeText.get();
    }

    public StringProperty latitudeTextProperty() {
        return latitudeText;
    }

    public void setLatitudeText(String latitudeText) {
        this.latitudeText.set(latitudeText);
    }




    public String getLoadertypeText() {
        return loadertypeText.get();
    }

    public StringProperty loadertypeTextProperty() {
        return loadertypeText;
    }

    public void setLoadertypeText(String loadertypeText) {
        this.loadertypeText.set(loadertypeText);
    }

    public String getNumberOfChargingPointsText() {
        return numberOfChargingPointsText.get();
    }

    public StringProperty numberOfChargingPointsTextProperty() {
        return numberOfChargingPointsText;
    }

    public void setNumberOfChargingPointsText(String numberOfChargingPointsText) {
        this.numberOfChargingPointsText.set(numberOfChargingPointsText);
    }

    public String getConnectionPowerKWText() {
        return connectionPowerKWText.get();
    }

    public StringProperty connectionPowerKWTextProperty() {
        return connectionPowerKWText;
    }

    public void setConnectionPowerKWText(String connectionPowerKWText) {
        this.connectionPowerKWText.set(connectionPowerKWText);
    }

    public String getPlugtype1Text() {
        return plugtype1Text.get();
    }

    public StringProperty plugtype1TextProperty() {
        return plugtype1Text;
    }

    public void setPlugtype1Text(String plugtype1Text) {
        this.plugtype1Text.set(plugtype1Text);
    }

    public String getPower1KWText() {
        return power1KWText.get();
    }

    public StringProperty power1KWTextProperty() {
        return power1KWText;
    }

    public void setPower1KWText(String power1KWText) {
        this.power1KWText.set(power1KWText);
    }

    public String getPlugtype2Text() {
        return plugtype2Text.get();
    }

    public StringProperty plugtype2TextProperty() {
        return plugtype2Text;
    }

    public void setPlugtype2Text(String plugtype2Text) {
        this.plugtype2Text.set(plugtype2Text);
    }

    public String getPower2KWText() {
        return power2KWText.get();
    }

    public StringProperty power2KWTextProperty() {
        return power2KWText;
    }

    public void setPower2KWText(String power2KWText) {
        this.power2KWText.set(power2KWText);
    }

    public String getPlugtype3Text() {
        return plugtype3Text.get();
    }

    public StringProperty plugtype3TextProperty() {
        return plugtype3Text;
    }

    public void setPlugtype3Text(String plugtype3Text) {
        this.plugtype3Text.set(plugtype3Text);
    }

    public String getPower3KWText() {
        return power3KWText.get();
    }

    public StringProperty power3KWTextProperty() {
        return power3KWText;
    }

    public void setPower3KWText(String power3KWText) {
        this.power3KWText.set(power3KWText);
    }

    public String getPlugtype4Text() {
        return plugtype4Text.get();
    }

    public StringProperty plugtype4TextProperty() {
        return plugtype4Text;
    }

    public void setPlugtype4Text(String plugtype4Text) {
        this.plugtype4Text.set(plugtype4Text);
    }

    public String getPower4KWText() {
        return power4KWText.get();
    }

    public StringProperty power4KWTextProperty() {
        return power4KWText;
    }

    public void setPower4KWText(String power4KWText) {
        this.power4KWText.set(power4KWText);
    }

    public String getPLZText() {
        return PLZText.get();
    }

    public StringProperty PLZTextProperty() {
        return PLZText;
    }

    public void setPLZText(String PLZText) {
        this.PLZText.set(PLZText);
    }

    public String getInbetriebnahmeText() {
        return inbetriebnahmeText.get();
    }

    public StringProperty inbetriebnahmeTextProperty() {
        return inbetriebnahmeText;
    }

    public void setInbetriebnahmeText(String inbetriebnahmeText) {
        this.inbetriebnahmeText.set(inbetriebnahmeText);
    }

    public String getAddText() {
        return addText.get();
    }

    public StringProperty addTextProperty() {
        return addText;
    }

    public void setAddText(String addText) {
        this.addText.set(addText);
    }

    public String getDeleteText() {
        return deleteText.get();
    }

    public StringProperty deleteTextProperty() {
        return deleteText;
    }

    public void setDeleteText(String deleteText) {
        this.deleteText.set(deleteText);
    }

    public String getSaveText() {
        return saveText.get();
    }

    public StringProperty saveTextProperty() {
        return saveText;
    }

    public void setSaveText(String saveText) {
        this.saveText.set(saveText);
    }

    public String getRedoText() {
        return redoText.get();
    }

    public StringProperty redoTextProperty() {
        return redoText;
    }

    public void setRedoText(String redoText) {
        this.redoText.set(redoText);
    }

    public String getUndoText() {
        return undoText.get();
    }

    public StringProperty undoTextProperty() {
        return undoText;
    }

    public void setUndoText(String undoText) {
        this.undoText.set(undoText);
    }

    public String getSearchText() {
        return searchText.get();
    }

    public StringProperty searchTextProperty() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText.set(searchText);
    }
}


