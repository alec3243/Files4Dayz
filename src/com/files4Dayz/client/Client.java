package com.files4Dayz.client;
import java.io.*;
import java.net.*;

import com.files4Dayz.security.Checksum;
import java.util.*;
public class Client {
	private DataOutputStream outToServer;
	private DataInputStream inFromServer;
	private static Socket s;

	private String username;
	private String password;


	public Client(String ip, int port) throws IOException {
		s = new Socket(ip, port);
		wrapClientStreams();
		// Get login details from server
		getCredentials();
	}

//	public static void main(String[] args) throws UnknownHostException, IOException {
//		//System.out.println(InetAddress.getLocalHost().getHostAddress());
//		s = new Socket("10.110.157.59", 1342);
//		System.out.println("File Location?");
//		Scanner sc = new Scanner(System.in);
//		String name = sc.nextLine();
//		File file = new File(name);
//		Long check = file.length()/1024;
//		InputStream is = new FileInputStream(file);
//		//sendFile(file);
//		s.close();
//		sc.close();
//	}

	public void sendFile(File file) throws IOException {
		int check = (int)(file.length()/1024) + 1;
		System.out.println(file.length());
		FileInputStream is = new FileInputStream(file);
		sendFile(is,check, file.getName());
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	private void getCredentials() throws IOException {
		Scanner sc = new Scanner(System.in);
		String un, pw;
		String message = null;
		while (message == null || message.equals("Wrong combination")) {
			System.out.println("Enter username:");
			un = sc.nextLine();
			outToServer.writeUTF(un);
			outToServer.flush();
			System.out.println("Enter password:");
			pw = sc.nextLine();
			outToServer.writeUTF(pw);
			outToServer.flush();
			message = inFromServer.readUTF();
		}
		sc.close();
	}

	private void wrapClientStreams() throws IOException {
		inFromServer = new DataInputStream(s.getInputStream());
		outToServer = new DataOutputStream(new BufferedOutputStream(s.getOutputStream()));
	}
	
	private void sendFile(FileInputStream x, int size, String filename) throws IOException {
		byte[] buffers = new byte[1024];
		System.out.println(filename);
		System.out.println(size);
		outToServer.writeUTF(filename);
		outToServer.flush();
		while (x.read(buffers) > 0) {
			//getFile.read(buffers);
			outToServer.write(buffers);
			//outToServer.writeUTF(findchecksum(buffers));
//			while (!inFromServer.readBoolean()) {
//				//TODO chunk was corrupt, send it again
//			}
			size--;
		}
		x.close();
		outToServer.close();
	}
}
