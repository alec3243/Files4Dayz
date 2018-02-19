package com.files4Dayz.client;
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
	static byte[] buffers;
	static DataOutputStream out;
	static Socket s;
	public static void main(String[] args) throws UnknownHostException, IOException {
		Socket s = new Socket("127.0.0.1", 1342);
		System.out.println("File Location?");
		Scanner sc = new Scanner(System.in);
		String name = sc.nextLine();
		File file = new File(name);
		Long check = file.length()/1024;
		InputStream is = new FileInputStream(file);
		SendFile(is, check);
		s.close();
		sc.close();
		
		
	}
	
	private static void SendFile(InputStream x, Long size) throws IOException {
		DataInputStream getFile = new DataInputStream(x);
		out = new DataOutputStream(new BufferedOutputStream(s.getOutputStream()));
		long pieces = 0;
		while (size > pieces) {
			buffers = new byte[1024];
			getFile.read(buffers);
			out.write(buffers, 0, buffers.length);
			pieces++;
		}
		out.close();
		getFile.close();
		
	}
	
	private static void hash() {
		
	}
}