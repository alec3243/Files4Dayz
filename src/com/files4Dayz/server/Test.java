package com.files4Dayz.server;

import java.io.IOException;

public class Test {

	public static void main(String[] args) throws IOException {
		Server server = new Server(1342);
		server.runAsServer();

	}

}
