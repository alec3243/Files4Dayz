package com.files4Dayz.server;
import javax.net.ServerSocketFactory;
import java.net.*;
import java.io.*;


public class Server {

	private Socket client;
	private DataInputStream inFromClient;
	private DataOutputStream outToClient;

	private final String USERNAME = "username";
	private final String PASSWORD = "password";

	public void runServer() throws IOException {
		final int PORT = 1342;
		ServerSocketFactory factory = ServerSocketFactory.getDefault();
		ServerSocket server = factory.createServerSocket(PORT);
		if (server == null) {
			System.out.println("Fail to create server at this port");
		} else {
			System.out.println("Server established");
		}
		client = server.accept();
		wrapClientStreams();
		sendCredentials();
		listen();
		if (client != null) {
			System.out.println("Got a caller");
		} else {
			System.out.println("Not received the caller");
		}
	}

	private void listen() throws IOException {
		while (true) {
			Thread.yield();
			byte[] bytes = new byte[1024];
			inFromClient.read(bytes);

		}
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

	}
}