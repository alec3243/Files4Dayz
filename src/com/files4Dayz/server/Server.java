package com.files4Dayz.server;
import java.net.*;
import java.io.*;

public class Server {
	private ServerSocket server;
	private Socket client;
	public void runServer(int port) throws IOException{
		server = new ServerSocket(port);
		if (server != null) {
			System.out.println("Server established. Waiting for client to send file");
		}
		client = server.accept();
		saveFile(client);
	}
	private void saveFile(Socket client) throws IOException {
		DataInputStream dataReadIn = new DataInputStream(client.getInputStream());
		FileOutputStream file = new FileOutputStream("copy.jpg");
		// set each reading chunk to be 1024
		byte[] buffer = new byte[1024];
		dataReadIn.read(buffer);
		int read = 0;
		int totalRead = 0;
		while ((read = dataReadIn.read(buffer)) > 0) {
			file.write(buffer, 0, read);
			totalRead += read;
		}
		System.out.println("File size is " + totalRead + " bytes");
		dataReadIn.close();
		file.close();
	}
}
