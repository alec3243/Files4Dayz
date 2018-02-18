package com.files4Dayz.stuff;
import com.files4Dayz.server.Server;

public class Main {

	public static void main(String[] args) {
		Server s = new Server(1343);
		s.runServer();
	}
}
