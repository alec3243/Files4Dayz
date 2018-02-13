package com.files4Dayz.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ServerController extends Application {

	@Override
	public void start(Stage arg0) throws IOException {
		start(arg0,0);
	}

	public void start(Stage primaryStage, int port) throws IOException {
		primaryStage.setTitle("Filez4Dayz Server");
		final FXMLLoader loader = new FXMLLoader(getClass().getResource("ServerApp.fxml"));
		final Parent root = loader.load();
		final Scene scene = new Scene(root);


		primaryStage.show();
	}

}
