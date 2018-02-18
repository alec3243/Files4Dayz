package com.files4Dayz.server;
import java.net.*;
import java.io.*;

public class Server {
	private ServerSocket server;
	private Socket client;
	private final String userName = "admin";
	private final String passWord = "abc123";
	
	public Server(int port){
		try {
			server = new ServerSocket(port);
			System.out.println("Server established. Waiting for client to send file");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void runServer() {
		while (true) {
			try {
				client = server.accept();
				saveFile(client);
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	private void saveFile(Socket client) throws IOException {
		DataInputStream dataReadIn = new DataInputStream(client.getInputStream());
		
		// save as fileName sent from client
		String fileName = dataReadIn.readUTF();
		FileOutputStream file = new FileOutputStream(fileName);
		
		// set each reading chunk to be 1024
		byte[] buffer = new byte[1024];
		
		// decode
		byte[] data = decode(buffer);
		
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
	private void sendFile(String file) throws IOException {
		DataOutputStream dos = new DataOutputStream(client.getOutputStream());
		FileInputStream fileReadIn = new FileInputStream(file);
		dos.writeUTF(file);
		dos.flush();
		byte[] buffer = new byte[1024];
		
		while (fileReadIn.read(buffer) > 0) {
			dos.write(buffer);
		}
		
		fileReadIn.close();
		dos.close();	
	}
	private byte[] decode(byte[] buffer) {
		// TODO: Decode hashed data
		return new byte[] {};
	}
}
