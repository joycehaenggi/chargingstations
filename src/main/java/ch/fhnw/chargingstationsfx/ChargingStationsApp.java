package ch.fhnw.chargingstationsfx;


import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import ch.fhnw.chargingstationsfx.presentationmodel.RootPM;
import ch.fhnw.chargingstationsfx.view.RootPanelUI;

public class ChargingStationsApp extends Application {

	@Override
	public void start(Stage primaryStage) {
		RootPM rootPM    = new RootPM();
		Parent rootPanel = new RootPanelUI(rootPM);


		Scene scene = new Scene(rootPanel);
		primaryStage.setTitle("ChargingStationsFX");
		primaryStage.titleProperty().bind(rootPM.applicationTitleProperty());
		primaryStage.setScene(scene);



		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
