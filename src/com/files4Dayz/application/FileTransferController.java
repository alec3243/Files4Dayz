package com.files4Dayz.application;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

public class FileTransferController extends Application {

	@FXML
	private Button startButton;

	@FXML
	private RadioButton serverButton;

	@FXML
	private RadioButton clientButton;

	@FXML
	private ToggleGroup toggleGroup;

	public static void main(String[] args) {
		launch();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		final FXMLLoader loader = new FXMLLoader(getClass().getResource("FileTranserApp.fxml"));
		final Parent root = loader.load();
		final Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();

		//Establish nodes
		startButton = (Button) root.lookup("#startButton");
		serverButton = (RadioButton) root.lookup("#serverButton");
		clientButton = (RadioButton) root.lookup("#clientButton");
		toggleGroup = new ToggleGroup();
		serverButton.setToggleGroup(toggleGroup);
		clientButton.setToggleGroup(toggleGroup);

		// Get radio button selection
		final String[] selection = new String[1];
		toggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			@Override
			public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
				selection[0] = ((RadioButton)newValue.getToggleGroup().getSelectedToggle()).getText();
			}
		});

		// Establish start button functionality
		startButton.setOnAction((event) -> {

			System.out.println("Selected: " + selection[0]);
			//TODO some other stuff
		});
	}


}
