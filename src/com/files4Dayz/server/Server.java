package com.files4Dayz.server;
import javax.net.ServerSocketFactory;
import java.net.*;
import java.io.*;

import static com.files4Dayz.security.Checksum.findchecksum;

public class Server {

	private int port;
	private Socket client;
	private DataInputStream inFromClient;
	private DataOutputStream outToClient;

	private final String USERNAME = "username";
	private final String PASSWORD = "password";

	public Server(int port) {
		this.port = port;
	}

	public void runServer() throws IOException {
		ServerSocketFactory factory = ServerSocketFactory.getDefault();
		ServerSocket server = factory.createServerSocket(port);
		if (server == null) {
			System.out.println("Fail to create server at this port");
		} else {
			System.out.println("Server established");
		}
		client = server.accept();
		wrapClientStreams();
		sendCredentials();
		while (true) {
			listen();
		}
	}

	/**
	 * Listens for the next file transmission.
	 * @throws IOException
	 */
	private void listen() throws IOException {
		byte[] bytes = null;
		long size = inFromClient.readLong();
		final int kilobyte = 1024;
		byte[] totalBytes = new byte[(int) size*kilobyte];
		String originalChecksum = null;
		String currentChecksum = null;
		for (int i = 0; i < size; i++) {
			// Read the next chunk
			bytes = new byte[kilobyte];
			inFromClient.read(bytes);
			// Confirm that checksums are equal
			originalChecksum = inFromClient.readUTF();
			currentChecksum = findchecksum(bytes);
			// Checksums are not equal, tell client to send chunk again
			if (!currentChecksum.equals(originalChecksum)) {
				outToClient.writeBoolean(false);
				i--;
			} else {
				// Tell client that chunk is valid, continue
				outToClient.writeBoolean(true);
				//TODO idk lmfao
			} }//TODO write the file
	}


	private void wrapClientStreams() throws IOException {
		inFromClient = new DataInputStream(client.getInputStream());
		outToClient = new DataOutputStream(client.getOutputStream());
	}

	private void sendCredentials() throws IOException {
		outToClient.writeUTF(USERNAME);
		outToClient.writeUTF(PASSWORD);
	}

	public static void main(String[] args) throws IOException {
		Server server = new Server(1432);
		server.runServer();
	}
}