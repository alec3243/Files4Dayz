package com.files4Dayz.client;
import java.io.*;
import java.net.*;
import com.files4Dayz.security.XorCipher;

import static com.files4Dayz.security.Checksum.findchecksum;


public class Client {
	private DataOutputStream outToServer;
	private DataInputStream inFromServer;
	private Socket s;
	private File file;
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
	//	file = new File("Key.txt");
		s = new Socket("10.110.180.90", 1342);
		wrapClientStreams();
		// Get login details from server
		getCredentials();

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
		boolean fail = false;
		long pieces = 0;
		byte[] buffers = null;
		System.out.println(filename);
		System.out.println(size);
		outToServer.writeLong(size);
		//outToServer.writeUTF(filename);
		while (size > pieces) {
			int Counter = 0;
			buffers = new byte[1024];
			getFile.read(buffers);
			outToServer.write(XorCipher.encryptDecrypt(buffers, file), 0, buffers.length);
			outToServer.writeUTF(findchecksum(buffers));
			
			while (!inFromServer.readBoolean()) {
				if (Counter < 3) {
					Counter++;
					outToServer.write(XorCipher.encryptDecrypt(buffers, file));
				} else {
					fail = true;
				}
			}
			if (fail) {
				System.out.println("Send Failed!");
				break;
			}
			pieces++;
		}
		getFile.close();
	}
	
	
}

