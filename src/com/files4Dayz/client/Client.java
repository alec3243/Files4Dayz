package com.files4Dayz.client;
import java.io.*;
import java.net.*;


public class Client {
	public static void main(String[] args) throws UnknownHostException, IOException {
		Socket s = new Socket("127.0.0.1", 1342);
		s.close();
	}
}
