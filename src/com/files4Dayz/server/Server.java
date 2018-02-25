package com.files4Dayz.server;
import java.net.*;
import java.io.*;
import static com.files4Dayz.security.Checksum.findchecksum;
import static com.files4Dayz.security.XorCipher.encryptDecrypt;

public class Server {
	private DataInputStream dataReadIn;
	private DataOutputStream dataSendOut;
	private FileOutputStream fileToSave;
	private ServerSocket server;
	private Socket client;
	private int failTime;
	private final String userName = "admin";
	private final String password = "abc123";
	
	public Server(int port){
		try {
			server = new ServerSocket(port);
			failTime = 3;
			System.out.println("Server established. Waiting for client to send file");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void runAsServer() {
		while (true) {
			try {
				client = server.accept();
				if (client != null) {
					System.out.println("Got a caller");
				}
				verifyCredentials(client);
				saveFile(client);
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	private void verifyCredentials(Socket client) throws IOException{
		String userNameClient = null;
		String passwordClient = null;
		boolean successVerification = false;
		dataReadIn = new DataInputStream(client.getInputStream());
		dataSendOut = new DataOutputStream(client.getOutputStream());
		
		// verify user name and password
		while (failTime > 0) {
			userNameClient = dataReadIn.readUTF();
			passwordClient = dataReadIn.readUTF();
			if (!userNameClient.equals(userName) || !passwordClient.equals(password)) {
				dataSendOut.writeUTF("Wrong combination");
				failTime -= 1;
			} else {
				successVerification = true;
				System.out.println("Verification success");
				break;
			}
		}
		// if user name is correct, reset failTime
		if (successVerification) {
			dataSendOut.writeUTF("Correct");
			failTime = 3;
		}
		
		if (failTime == 0) {
			System.out.println("Too many fails. Server will close");
			dataSendOut.writeUTF("Close");
			server.close();
		}
	}
	private void saveFile(Socket client) throws IOException {
		dataReadIn = new DataInputStream(client.getInputStream());
		
		// save as fileName sent from client
		String fileName = dataReadIn.readUTF();
		fileToSave = new FileOutputStream(fileName);
		
		// set each reading chunk to be 1024
		byte[] originalChunk = new byte[1024];
		String hashedValueFromClient = null;
		
		// decode
		// byte[] data = decode(buffer);
		
		dataReadIn.read(originalChunk);
		hashedValueFromClient = dataReadIn.readUTF();
		String hashedValueComputed = findchecksum(originalChunk);
		if (hashedValueComputed == hashedValueFromClient) {
			int read = 0;
			int totalRead = 0;
			while ((read = dataReadIn.read(originalChunk)) > 0) {
				fileToSave.write(originalChunk, 0, read);
				totalRead += read;
			}
			System.out.println("File size is " + totalRead + " bytes");
			dataReadIn.close();
			fileToSave.close();
			failTime = 3;
		} else {
			dataSendOut = new DataOutputStream(client.getOutputStream());
			dataSendOut.writeUTF("wrong");
			failTime -= 1;
			if (failTime == 0) {
				System.out.println("Exceed faile time limit! Connection is terminated.");
				server.close();
			}
		}
	}
	
	private byte[] decode(byte[] buffer) {
		// TODO: Decode hashed data
		return new byte[] {};
	}
	
}