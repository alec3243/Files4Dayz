package com.files4Dayz.server;
import java.net.*;
import java.io.*;

public class Server {
	private static ServerSocket server;
	private static Socket client;
	public static void runServer() throws IOException{
		server = new ServerSocket(1342);
		if (server == null) {
			System.out.println("Fail to create server at this port");
		} else {
			System.out.println("Server established");
		}
		client = server.accept();
		if (client != null) {
			System.out.println("Got a caller");
		} else {
			System.out.println("Not received the caller");
		}
	}
	public static void main(String[] args) throws IOException {
		runServer();
	}
}
