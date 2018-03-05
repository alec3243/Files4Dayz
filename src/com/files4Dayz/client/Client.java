package com.files4Dayz.client;
import java.io.*;
import java.net.*;

import com.files4Dayz.security.AsciiArmor;
import com.files4Dayz.security.Checksum;
import java.util.*;

import static com.files4Dayz.security.Checksum.findchecksum;

public class Client {
	private DataOutputStream outToServer;
	private DataInputStream inFromServer;
	private static Socket s;
	private int corruptedChunks;
	private String username;
	private String password;


	public Client(String ip, int port) throws IOException {
		s = new Socket(ip, port);
		wrapClientStreams();
		// Get login details from server
		//getCredentials();
		corruptedChunks = 0;
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

	public void sendFile(File file, boolean isArmored) throws IOException {
		int check = (int)(file.length()/1024) + 1;
		System.out.println(file.length());
		FileInputStream is = new FileInputStream(file);
		sendFile(is,check, file.getName(), isArmored);
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public boolean sendCredentials(String user, String pass) throws IOException {
		String message = null;
		outToServer.writeUTF(user);
		outToServer.flush();
		outToServer.writeUTF(pass);
		outToServer.flush();
		message = inFromServer.readUTF();
		if (message.equals("Wrong combination")) {
			return false;
		} else {
			return true;
		}
	}

	private void wrapClientStreams() throws IOException {
		inFromServer = new DataInputStream(s.getInputStream());
		outToServer = new DataOutputStream(new BufferedOutputStream(s.getOutputStream()));
	}
	
	private void sendFile(FileInputStream x, int size, String filename, boolean isArmored) throws IOException {

		outToServer.writeUTF(filename);
		outToServer.flush();
		if (isArmored) {
			outToServer.writeUTF("armored");
		} else {
			outToServer.writeUTF("not armored");
		}
		outToServer.flush();
		byte[] corrupted= new byte[1024];
		byte[] buffers = new byte[1024];

		while (x.read(buffers) > 0) {
			//getFile.read(buffers);

			if (corruptedChunks > 0) {
				for (int i = 0; i < corrupted.length; i++) {
					corrupted[i] = (byte) (i % Byte.MAX_VALUE);
				}
				if (isArmored) {
					corrupted = AsciiArmor.armor(corrupted);
				}
				outToServer.write(corrupted);
				corruptedChunks--;
			} else {
				if (isArmored) {
					System.out.println("Armoring...");
					buffers = AsciiArmor.armor(buffers);
					System.out.println("ARMORED.");
				}
				outToServer.write(buffers);
			}
			outToServer.writeUTF(findchecksum(buffers));
			System.out.println("Sent hash of chunk");
			outToServer.flush();
			String input = null;
			while ((input = inFromServer.readUTF()).equals("wrong")) {
				System.out.println("Chunk is resent");
				outToServer.write(buffers);
				outToServer.writeUTF(findchecksum(buffers));
				outToServer.flush();
			}
			System.out.println(input);
			size--;
		}
		x.close();
	}

	public void sendCorrupted(int i) {
		corruptedChunks = i;
	}
}
