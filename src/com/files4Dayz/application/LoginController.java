package com.files4Dayz.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController extends Application {

    @Override
    public void start(final Stage primaryStage) throws IOException {
        primaryStage.setTitle("Login");
        final FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
        final Parent root = loader.load();
        final Scene scene = new Scene(root);

        final Label warningLabel = (Label) loader.getNamespace().get("warningLabel");
        warningLabel.setVisible(false);

        final Button loginButton = (Button) loader.getNamespace().get("loginButton");
        final Button cancelButton = (Button) loader.getNamespace().get("cancelButton");
        loginButton.setOnAction((event) -> {
            final String userName = ((TextField) loader.getNamespace().get("usernameField")).getText();
            final String password = ((TextField) loader.getNamespace().get("passwordField")).getText();
            //TODO if username && password are correct -> allow client to send files
            // else -> warningLabel.setVisible(true);
        });
        cancelButton.setOnAction((event -> primaryStage.close()));


        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
