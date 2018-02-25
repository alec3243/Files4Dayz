package com.files4Dayz.stuff;
import com.files4Dayz.server.Server;

public class Main {

	public static void main(String[] args) {
		Server server = new Server(1342);
		server.runAsServer();
	}
}
