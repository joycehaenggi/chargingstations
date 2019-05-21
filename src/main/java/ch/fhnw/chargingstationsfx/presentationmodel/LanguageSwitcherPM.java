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
        FIRMA_TEXT("Company Name", "Betreiber"),
        STRASSE_TEXT("Street*", "Strasse*"),
        PLZ_TEXT("Zip*", "PLZ*"),
        ORT_TEXT(" *", "Ort*"),
        LÄNGENGRAD_TEXT(" ", "Längengrad"),
        BREITENGRAD_TEXT("", "Breitengrad"),
        INBETRIEBNAHME_TEXT("", "Inbetriebnahme"),
        TYP_TEXT("", "Typ"),
        ANZAHL_LADESTATION_TEXT(" ", "Anzahl Ladestationen"),
        ANSCHLUSSLEISTUNG_TEXT(" ", "Anschlussleistung"),
        STECKERTYP1_TEXT("", "1-Steckertypen"),
        LEISTUNG1_TEXT("", "Leistung [kW]"),
        STECKERTYP2_TEXT("", "2-Steckertypen"),
        LEISTUNG2_TEXT("", "Leistung [kW]"),
        STECKERTYP3_TEXT("", "3-Steckertypen"),
        LEISTUNG3_TEXT("", "Leistung [kW]"),
        STECKERTYP4_TEXT("", "4-Steckertypen"),
        LEISTUNG4_TEXT("", "Leistung [kW]");

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

    public LanguageSwitcherPM(){
        setLanguage(Lang.DE);
    }
   //TODO Fragen, warum dies nicht so funktioniert

    public String getGermanButtonText() {
        return germanButtonText.get();
    }

    public String getEnglishButtonText() {
        return englishButtonText.get();
    }


    public void setLanguage(Lang lang){
        setGermanButtonText(MultilanguageText.GERMAN_BUTTON_TEXT.getText(lang));
        setEnglishButtonText(MultilanguageText.ENGLISH_BUTTON_TEXT.getText(lang));
        setFirmaText(MultilanguageText.FIRMA_TEXT.getText(lang));
        setStrasseText(MultilanguageText.STRASSE_TEXT.getText(lang));



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
}


