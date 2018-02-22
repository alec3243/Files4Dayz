package com.files4Dayz.client;
import java.io.*;
import java.net.*;

import static com.files4Dayz.checksum.Checksum.findchecksum;


public class Client {
	private DataOutputStream outToServer;
	private DataInputStream inFromServer;
	private Socket s;

	private String username;
	private String password;

	public Client() {}

	public Client(String ip, int port) throws IOException {
		s = new Socket(ip, port);
		wrapClientStreams();
		// Get login details from server
		getCredentials();
	}

	public void main(String[] args) throws UnknownHostException, IOException {
		s = new Socket("127.0.0.1", 1342);
		wrapClientStreams();
		// Get login details from server
		getCredentials();
//		System.out.println("File Location?");
//		Scanner sc = new Scanner(System.in);
//		String name = sc.nextLine();
//		File file = new File(name);
//		Long check = file.length()/1024;
//		InputStream is = new FileInputStream(file);
//		sendFile(is, check);
//		s.close();
//		sc.close();
	}

	public void sendFile(File file) throws IOException {
		Long check = file.length()/1024;
		InputStream is = new FileInputStream(file);
		sendFile(is,check, file.getName());
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	private void getCredentials() throws IOException {
		username = inFromServer.readUTF();
		password = inFromServer.readUTF();
	}

	private void wrapClientStreams() throws IOException {
		inFromServer = new DataInputStream(s.getInputStream());
		outToServer = new DataOutputStream(new BufferedOutputStream(s.getOutputStream()));
	}
	
	private void sendFile(InputStream x, Long size, String filename) throws IOException {
		DataInputStream getFile = new DataInputStream(x);
		long pieces = 0;
		byte[] buffers = null;
		System.out.println(filename);
		System.out.println(size);
		outToServer.writeLong(size);
		//outToServer.writeUTF(filename);
		while (size > pieces) {
			buffers = new byte[1024];
			getFile.read(buffers);
			outToServer.write(buffers, 0, buffers.length);
			outToServer.writeUTF(findchecksum(buffers));
			while (!inFromServer.readBoolean()) {
				//TODO chunk was corrupt, send it again
			}
			pieces++;
		}
		getFile.close();
	}
	
}

