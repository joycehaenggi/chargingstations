package ch.fhnw.chargingstationsfx.view;

import ch.fhnw.chargingstationsfx.presentationmodel.RootPM;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.util.converter.NumberStringConverter;


public class Editor extends GridPane implements ViewMixin {
    private final RootPM rootPM;


    public Label betreiberLabel;
    public Label strasseLabel;
    public Label PLZLabel;
    public Label ortLabel;
    public Label longitudeLabel;
    public Label latitudeLabel;
    public Label inbetriebnahmeLabel;
    public Label loaderTypeLabel;
    public Label numberOfChargingPointsLabel;
    public Label connectionPowerKwLabel;
    public Label plugType1Label;
    public Label power1KwLabel;
    public Label plugType2Label;
    public Label power2KwLabel;
    public Label plugType3Label;
    public Label power3KwLabel;
    public Label plugType4Label;
    public Label power4KwLabel;

    public Label connectionPowerKwTextField;


    public TextField betreiberTextField;
    public TextField strasseTextField;
    public TextField PLZTextField;
    public TextField ortTextField;
    public TextField longitudeTextField;
    public TextField latitudeTextField;
    public TextField inbetriebnahmeTextField;
   // public TextField loaderTypeTextField;
   private TypeSwitch typeSwitch;

    public TextField numberOfChargingPointsTextField;
    public TextField plugType1TextField;
    public TextField power1Kw;
    public TextField plugType2TextField;
    public TextField power2Kw;
    public TextField plugType3TextField;
    public TextField power3Kw;
    public TextField plugType4TextField;
    public TextField power4Kw;


    public Editor(RootPM rootPM) {
        this.rootPM = rootPM;
        init();
    }

    @Override
    public void initializeControls() {
        //Initialisierung

        betreiberLabel = new Label();
        strasseLabel = new Label();
        PLZLabel = new Label();
        ortLabel = new Label();
        longitudeLabel = new Label();
        latitudeLabel = new Label();
        inbetriebnahmeLabel = new Label();
        loaderTypeLabel = new Label();
        numberOfChargingPointsLabel = new Label();
        connectionPowerKwLabel = new Label();
        plugType1Label = new Label();
        power1KwLabel = new Label();
        plugType2Label = new Label();
        power2KwLabel = new Label();
        plugType3Label = new Label();
        power3KwLabel = new Label();
        plugType4Label = new Label();
        power4KwLabel = new Label();
        connectionPowerKwTextField = new Label();


        betreiberTextField = new TextField();
        strasseTextField = new TextField();
        PLZTextField = new TextField();
        ortTextField = new TextField();
        longitudeTextField = new TextField();
        latitudeTextField = new TextField();
        inbetriebnahmeTextField = new TextField();
      //  loaderTypeTextField = new TextField();
        typeSwitch = new TypeSwitch();

        numberOfChargingPointsTextField = new TextField();

        plugType1TextField = new TextField();
        power1Kw = new TextField();
        plugType2TextField = new TextField();
        power2Kw = new TextField();
        plugType3TextField = new TextField();
        power3Kw = new TextField();
        plugType4TextField = new TextField();
        power4Kw = new TextField();
    }

    @Override
    public void layoutControls() {
        setVgap(5);
        setHgap(10);

        ColumnConstraints noGrow = new ColumnConstraints();
        noGrow.setHgrow(Priority.NEVER);
        ColumnConstraints grow = new ColumnConstraints();
        grow.setHgrow(Priority.ALWAYS);

        //Anwenden auf 3 Spalten (0,1,2)
        getColumnConstraints().addAll(grow, noGrow, grow, noGrow);

        //Zeile wächst
        RowConstraints neverGrow = new RowConstraints();
        neverGrow.setVgrow(Priority.NEVER);
        //Anwenden auf alle 3 Zeilen (0,1,2)
        getRowConstraints().addAll(neverGrow, neverGrow, neverGrow);


        add(betreiberLabel, 0, 7);
        add(strasseLabel, 0, 8);
        add(PLZLabel, 0, 9);
        add(ortLabel, 2, 9);
        add(longitudeLabel, 0, 10);
        add(latitudeLabel, 2, 10);
        add(inbetriebnahmeLabel, 0, 11);
        add(loaderTypeLabel, 2, 11);
        add(numberOfChargingPointsLabel, 0, 12);
        add(connectionPowerKwLabel, 2, 12);
        add(plugType1Label, 0, 13);
        add(power1KwLabel, 2, 13);
        add(plugType2Label, 0, 14);
        add(power2KwLabel, 2, 14);
        add(plugType3Label, 0, 15);
        add(power3KwLabel, 2, 15);
        add(plugType4Label, 0, 16);
        add(power4KwLabel, 2, 16);

        add(betreiberTextField, 1, 7, 3, 1);
        add(strasseTextField, 1, 8, 3, 1);
        add(PLZTextField, 1, 9);
        add(ortTextField, 3, 9);
        add(longitudeTextField, 1, 10);
        add(latitudeTextField, 3, 10);
        add(inbetriebnahmeTextField, 1, 11);
        add(typeSwitch, 3, 11);
        add(numberOfChargingPointsTextField, 1, 12);
        add(connectionPowerKwTextField, 3, 12);
        add(plugType1TextField, 1, 13);
        add(power1Kw, 3, 13);
        add(plugType2TextField, 1, 14);
        add(power2Kw, 3, 14);
        add(plugType3TextField, 1, 15);
        add(power3Kw, 3, 15);
        add(plugType4TextField, 1, 16);
        add(power4Kw, 3, 16);


    }

    private Button createButton(String text) {
        Button button = new Button(text);
        button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        return button;
    }

    @Override
    public void setupBindings() {
        // Binding Editor MultiLanguage
        betreiberLabel.textProperty().bind(rootPM.getLanguageSwitcherPM().firmaTextProperty());
        strasseLabel.textProperty().bind(rootPM.getLanguageSwitcherPM().strasseTextProperty());
        ortLabel.textProperty().bind(rootPM.getLanguageSwitcherPM().ortTextProperty());
        PLZLabel.textProperty().bind(rootPM.getLanguageSwitcherPM().PLZTextProperty());
        longitudeLabel.textProperty().bind(rootPM.getLanguageSwitcherPM().longitudeTextProperty());
        latitudeLabel.textProperty().bind(rootPM.getLanguageSwitcherPM().latitudeTextProperty());
        numberOfChargingPointsLabel.textProperty().bind(rootPM.getLanguageSwitcherPM().numberOfChargingPointsTextProperty());
        inbetriebnahmeLabel.textProperty().bind(rootPM.getLanguageSwitcherPM().inbetriebnahmeTextProperty());
        loaderTypeLabel.textProperty().bind(rootPM.getLanguageSwitcherPM().loadertypeTextProperty());
        connectionPowerKwLabel.textProperty().bind(rootPM.getLanguageSwitcherPM().connectionPowerKWTextProperty());
        plugType1Label.textProperty().bind(rootPM.getLanguageSwitcherPM().plugtype1TextProperty());
        power1KwLabel.textProperty().bind(rootPM.getLanguageSwitcherPM().power1KWTextProperty());
        plugType2Label.textProperty().bind(rootPM.getLanguageSwitcherPM().plugtype2TextProperty());
        power2KwLabel.textProperty().bind(rootPM.getLanguageSwitcherPM().power2KWTextProperty());
        plugType3Label.textProperty().bind(rootPM.getLanguageSwitcherPM().plugtype3TextProperty());
        power3KwLabel.textProperty().bind(rootPM.getLanguageSwitcherPM().power3KWTextProperty());
        plugType4Label.textProperty().bind(rootPM.getLanguageSwitcherPM().plugtype4TextProperty());
        power4KwLabel.textProperty().bind(rootPM.getLanguageSwitcherPM().power4KWTextProperty());

        // Binding Editor Tabelle
        betreiberTextField.textProperty().bindBidirectional(rootPM.getLadestationProxy().companyNameProperty());
        strasseTextField.textProperty().bindBidirectional(rootPM.getLadestationProxy().strasseNameProperty());
        PLZTextField.textProperty().bindBidirectional(rootPM.getLadestationProxy().PLZProperty(), new NumberStringConverter("####"));
        ortTextField.textProperty().bindBidirectional(rootPM.getLadestationProxy().ortProperty());
        longitudeTextField.textProperty().bindBidirectional(rootPM.getLadestationProxy().longitudeProperty(), new NumberStringConverter());
        latitudeTextField.textProperty().bindBidirectional(rootPM.getLadestationProxy().latitudeProperty(), new NumberStringConverter());
        inbetriebnahmeTextField.textProperty().bindBidirectional(rootPM.getLadestationProxy().startDateProperty());
      //  loaderTypeTextField.textProperty().bindBidirectional(rootPM.getLadestationProxy().loaderTypeProperty());
        typeSwitch.typeProperty().bindBidirectional(rootPM.getProxy().loaderTypeProperty());

        numberOfChargingPointsTextField.textProperty().bindBidirectional(rootPM.getLadestationProxy().numberOfChargingPointsProperty(), new NumberStringConverter());
        connectionPowerKwTextField.textProperty().bind(rootPM.getLadestationProxy().connectionPowerKwProperty().asString("%.2f KW"));

        plugType1TextField.textProperty().bindBidirectional(rootPM.getLadestationProxy().plugType1Property());
        power1Kw.textProperty().bindBidirectional(rootPM.getLadestationProxy().power1KwProperty(), new NumberStringConverter());

        plugType2TextField.textProperty().bindBidirectional(rootPM.getLadestationProxy().plugType2Property());
        power2Kw.textProperty().bindBidirectional(rootPM.getLadestationProxy().power2KwProperty(), new NumberStringConverter());

        plugType3TextField.textProperty().bindBidirectional(rootPM.getLadestationProxy().plugType3Property());
        power3Kw.textProperty().bindBidirectional(rootPM.getLadestationProxy().power3KwProperty(), new NumberStringConverter());

        plugType4TextField.textProperty().bindBidirectional(rootPM.getLadestationProxy().plugType4Property());
        power4Kw.textProperty().bindBidirectional(rootPM.getLadestationProxy().power4KwProperty(), new NumberStringConverter());
    }

    @Override
    public void setupValueChangedListeners() {
        PLZTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*(\\.\\d*)?")) {
                    PLZTextField.setText(oldValue);
                }
            }
        });

        longitudeTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*(\\.\\d*)?")) {
                    longitudeTextField.setText(oldValue);
                }
            }
        });

        latitudeTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*(\\.\\d*)?")) {
                    latitudeTextField.setText(oldValue);
                }
            }
        });

        numberOfChargingPointsTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*(\\.\\d*)?")) {
                    numberOfChargingPointsTextField.setText(oldValue);
                }
            }
        });

        power1Kw.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*(\\.\\d*)?")) {
                    power1Kw.setText(oldValue);
                }
            }
        });

        power2Kw.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*(\\.\\d*)?")) {
                    power2Kw.setText(oldValue);
                }
            }
        });

        power3Kw.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*(\\.\\d*)?")) {
                    power3Kw.setText(oldValue);
                }
            }
        });

        power4Kw.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*(\\.\\d*)?")) {
                    power4Kw.setText(oldValue);
                }
            }
        });


        numberOfChargingPointsTextField.textProperty().addListener((observable, oldValue, newValue) -> {

                       if (newValue == null || newValue.equals("")) {
                        plugType1Label.setVisible(false);
                        plugType1TextField.setVisible(false);
                        power1KwLabel.setVisible(false);
                        power1Kw.setVisible(false);
                        power1Kw.textProperty().setValue("0");

                        plugType2Label.setVisible(false);
                        plugType2TextField.setVisible(false);
                        power2KwLabel.setVisible(false);
                        power2Kw.setVisible(false);
                        power2Kw.textProperty().setValue("0");

                        plugType3Label.setVisible(false);
                        plugType3TextField.setVisible(false);
                        power3KwLabel.setVisible(false);
                        power3Kw.setVisible(false);
                        power3Kw.textProperty().setValue("0");

                        plugType4Label.setVisible(false);
                        plugType4TextField.setVisible(false);
                        power4KwLabel.setVisible(false);
                        power4Kw.setVisible(false);
                        power4Kw.textProperty().setValue("0");
                    } else if (Integer.parseInt(newValue) == 1) {
                        plugType1Label.setVisible(true);
                        plugType1TextField.setVisible(true);
                        power1KwLabel.setVisible(true);
                        power1Kw.setVisible(true);

                        plugType2Label.setVisible(false);
                        plugType2TextField.setVisible(false);
                        power2KwLabel.setVisible(false);
                        power2Kw.setVisible(false);
                        power2Kw.textProperty().setValue("0");

                        plugType3Label.setVisible(false);
                        plugType3TextField.setVisible(false);
                        power3KwLabel.setVisible(false);
                        power3Kw.setVisible(false);
                        power3Kw.textProperty().setValue("0");

                        plugType4Label.setVisible(false);
                        plugType4TextField.setVisible(false);
                        power4KwLabel.setVisible(false);
                        power4Kw.setVisible(false);
                        power4Kw.textProperty().setValue("0");
                    } else if (Integer.parseInt(newValue) == 2) {
                        plugType1Label.setVisible(true);
                        plugType1TextField.setVisible(true);
                        power1KwLabel.setVisible(true);
                        power1Kw.setVisible(true);

                        plugType2Label.setVisible(true);
                        plugType2TextField.setVisible(true);
                        power2KwLabel.setVisible(true);
                        power2Kw.setVisible(true);

                        plugType3Label.setVisible(false);
                        plugType3TextField.setVisible(false);
                        power3KwLabel.setVisible(false);
                        power3Kw.setVisible(false);
                        power3Kw.textProperty().setValue("0");

                        plugType4Label.setVisible(false);
                        plugType4TextField.setVisible(false);
                        power4KwLabel.setVisible(false);
                        power4Kw.setVisible(false);
                        power4Kw.textProperty().setValue("0");

                    } else if (Integer.parseInt(newValue) == 3) {
                        plugType1Label.setVisible(true);
                        plugType1TextField.setVisible(true);
                        power1KwLabel.setVisible(true);
                        power1Kw.setVisible(true);

                        plugType2Label.setVisible(true);
                        plugType2TextField.setVisible(true);
                        power2KwLabel.setVisible(true);
                        power2Kw.setVisible(true);

                        plugType3Label.setVisible(true);
                        plugType3TextField.setVisible(true);
                        power3KwLabel.setVisible(true);
                        power3Kw.setVisible(true);

                        plugType4Label.setVisible(false);
                        plugType4TextField.setVisible(false);
                        power4KwLabel.setVisible(false);
                        power4Kw.setVisible(false);
                        power4Kw.textProperty().setValue("0");

                    } else if (Integer.parseInt(newValue) == 4) {
                        plugType1Label.setVisible(true);
                        plugType1TextField.setVisible(true);
                        power1KwLabel.setVisible(true);
                        power1Kw.setVisible(true);

                        plugType2Label.setVisible(true);
                        plugType2TextField.setVisible(true);
                        power2KwLabel.setVisible(true);
                        power2Kw.setVisible(true);

                        plugType3Label.setVisible(true);
                        plugType3TextField.setVisible(true);
                        power3KwLabel.setVisible(true);
                        power3Kw.setVisible(true);

                        plugType4Label.setVisible(true);
                        plugType4TextField.setVisible(true);
                        power4KwLabel.setVisible(true);
                        power4Kw.setVisible(true);

                    }
                }
        );

    }
}



