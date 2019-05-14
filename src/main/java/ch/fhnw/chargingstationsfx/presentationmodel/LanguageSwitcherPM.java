package ch.fhnw.chargingstationsfx.presentationmodel;

import ch.fhnw.chargingstationsfx.view.MultiLanguageText;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class LanguageSwitcherPM {

    public enum Lang{
        DE, EN
    }
    public enum MultilanguageText{
       //TODO alle Labels von Ladestation müssen hier in DE und EN aufgeführt werden

        LABEL_TEXT("something", "irgendwas"),
        GERMAN_BUTTON_TEXT("German", "Deutsch"),
        ENGLISH_BUTTON_TEXT("English", "Englisch");

        private final String englishLabel;
        private final String germanLablel;

        MultilanguageText(String englishLabel, String germanLablel){
            this.englishLabel = englishLabel;
            this.germanLablel = germanLablel;
        }

        public String getEnglishLabel() {
            return englishLabel;
        }

        public String getGermanLablel() {
            return germanLablel;
        }

        public String getText(Lang lang){
            switch(lang){
                case DE:
                    return getGermanLablel();
                case EN:
                    return getEnglishLabel();
                default:
                    return getEnglishLabel();
            }
        }
    }

    private final StringProperty labelText= new SimpleStringProperty();
    private final StringProperty germanButtonText = new SimpleStringProperty();
    private final StringProperty englishButtonText = new SimpleStringProperty();

    public LanguageSwitcherPM(){
        setLanguage(Lang.EN);
    }
   //TODO Fragen, warum dies nicht so funktioniert

    public void setLanguage(Lang lang){
        setLabelText(MultilanguageText.LABEL_TEXT.getText(lang));
        setGermanButtonText(MultilanguageText.GERMAN_BUTTON_TEXT.getText(lang));
        setEnglishButtonText(MultilanguageText.ENGLISH_BUTTON_TEXT.getText(lang));
    }


    public StringProperty labelTextProperty() {
        return labelText;
    }

    private void setLabelText(String labelText) {
        this.labelText.set(labelText);
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
}

