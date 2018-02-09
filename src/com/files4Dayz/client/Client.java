package com.files4Dayz.client;
import java.io.*;
import java.net.*;

<<<<<<< Updated upstream
public class Client {
	public static void main(String[] args) throws UnknownHostException, IOException {
		Socket s = new Socket("127.0.0.1", 1342);
=======
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client implements Runnable {

	private Socket socket;
	private String targetAddress;
	private int port;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;

	private Client() {
	}

	public Client(String targetAddress, int port) throws UnknownHostException,
			IOException {
		this.targetAddress = targetAddress;
		this.port = port;
	}

	@Override
	public void run() {
		try {
			socket = new Socket(targetAddress, port);
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
>>>>>>> Stashed changes
	}
}
