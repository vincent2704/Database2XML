package main;

import graphicalinterface.ConversionWindow;
import graphicalinterface.MainWindow;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main start class for Database2Xml converter
 * 
 * @author Jarek
 *
 */
public class App extends Application {

	private Stage stage;
	private MainWindow mainWindow;
	private Scene scMainWindow;
	private ConversionWindow convWindow;
	private Scene scConvWindow;

	@Override
	public void start(Stage primaryStage) throws Exception {
		stage = primaryStage;
		stage.setTitle("Database2XML Converter");
		moveToMainWindow();
		stage.show();
	}

	// switch scene methods
	public void moveToMainWindow() {
		if (mainWindow == null) {
			mainWindow = new MainWindow(this);
		}
		if (scMainWindow == null) {
			scMainWindow = new Scene(mainWindow, 450, 400);
			scMainWindow.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
		}
		stage.setScene(scMainWindow);
	}

	public void moveToConversionWindow() {
		if (convWindow == null) {
			convWindow = new ConversionWindow(this);
		}
		if (scConvWindow == null) {
			scConvWindow = new Scene(convWindow, 700, 620);
			// radiobuttony sa ciemne, trzeba jeszcze css'a pozmieniac
			scConvWindow.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
		}
		stage.setScene(scConvWindow);
	}

	public static void main(String[] args) {
		launch(args);
	}

	public Stage getStage() {
		return stage;
	}

}
