package com.files4Dayz.application;

import com.files4Dayz.client.Client;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ClientController extends Application {

	private Client client;

	@FXML
	private TableView<File> fileTable;

	@FXML
	private TableColumn fileColumn;

	private ObservableList<File> data;

	ClientController() {
		data = FXCollections.observableArrayList();
		//TODO establish Client object for connection
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		start(primaryStage,null,0);

	}

	public void start(final Stage primaryStage, String ip, int port) throws IOException {
		primaryStage.setTitle("Filez4Dayz Client");
		final FXMLLoader loader = new FXMLLoader(getClass().getResource("ClientApp.fxml"));
		final Parent root = loader.load();
		final Scene scene = new Scene(root);

		final Label ipPortLabel = (Label) root.lookup("#ipPort");
		ipPortLabel.setText(ip + ":" + port);
		// Establish table objects
		fileTable = (TableView<File>) root.lookup("#fileTable");
		fileTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		fileColumn = fileTable.getColumns().get(0);

		fileColumn.setCellValueFactory(new PropertyValueFactory<File,String>("absolutePath"));

		fileTable.setItems(data);

		final Button sendButton = (Button) root.lookup("#sendButton");
		sendButton.setOnAction((event) -> {
			sendFiles();
		});
		// Establish menu objects
		final MenuBar menuBar = (MenuBar) root.lookup("#menuBar");
		final MenuItem addFile = menuBar.getMenus().get(0).getItems().get(0);
		final MenuItem addDir = menuBar.getMenus().get(0).getItems().get(1);
		final MenuItem removeButton = menuBar.getMenus().get(1).getItems().get(0);
		addFile.setOnAction((event) -> {
			addFile(primaryStage);
		});

		addDir.setOnAction((event) -> {
			try {
				addDirectory(primaryStage);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

		removeButton.setOnAction((event) -> {
			removeSelected();
		});

		//Establish key listener
		scene.setOnKeyPressed((key) -> {
			switch(key.getCode()) {
				case DELETE:
					removeSelected();
			}
		});
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	private void addFile(Stage primaryStage) {
		final FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Choose file");
		final File file = fileChooser.showOpenDialog(primaryStage);
		if (file != null) {
			data.add(file);
		}
	}

	private void addDirectory(Stage primaryStage) throws IOException {
		final DirectoryChooser dirChooser = new DirectoryChooser();
		dirChooser.setTitle("Choose directory");
		final File dir = dirChooser.showDialog(primaryStage);
		if (dir != null) {
			traverseDirectory(dir);
		}
	}

	private void traverseDirectory(File dir) throws IOException {
		Files.walk(Paths.get(dir.toURI()))
				.filter(Files::isRegularFile)
				.forEach((x) -> {
					data.add(x.toFile());
				});
	}

	private void removeSelected() {
		File[] selected = fileTable.getSelectionModel().getSelectedItems().toArray(new File[0]);
		for (int i = 0; i < selected.length; i++) {
			data.remove(selected[i]);
		}
	}

	private void sendFiles() {
		// TODO tell Client to send files

		// TODO check if file sent successfully
		//TODO remove successfully transferred files from UI
	}

}
