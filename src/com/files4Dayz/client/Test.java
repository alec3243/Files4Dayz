package com.files4Dayz.client;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class Test {
	public static void main(String[] args) throws IOException {
		Client c = new Client("192.168.0.2", 1342);
		System.out.println("File Location?");
		File file = new File("wtf.jpg");
		c.sendFile(file);
	}
}
