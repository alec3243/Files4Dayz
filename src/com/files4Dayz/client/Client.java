package com.files4Dayz.client;
import java.io.*;
import java.net.*;
import java.util.Iterator;
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
			out.writeChars(findchecksum(buffers));
			pieces++;
		}
		out.close();
		getFile.close();
		
	}
	
	private static String findchecksum(byte[] x) {
		String checksum = "";
		for (int i = 0; i < 8; i ++) {
			byte[] hold = new byte[128];
			for (int j = 0; j < 128; j++) {
				hold[j] = x[(i*128)+j];
			}
			ByteIterator check = new ByteIterator(hold);
			String bits = "";
			for (boolean b : check) {
				if (b) {
				bits += "1";	
				} else {
					bits += "0";
				}
			}
			String A = bits.substring(0, 4);
			String B = "";
			for (int z= 1; z < 256; z++) {
				B = bits.substring(z*4, z*4+4);
				A = addHex(A, B);
				
			}
			checksum+=A;
		}
		return checksum;
	}
	
	private static String addHex(String x, String y) {
		String answer = "";
		boolean overflow = false;
		boolean carryover = false;
		for (int i = 4; i > 0; i--) {
			Integer A = Integer.parseInt(x.substring(i-1, i), 16);
			Integer B = Integer.parseInt(y.substring(i-1, i), 16);
			if (carryover) {
				A++;
				carryover = false;
			}
			Integer C = A + B;
			if ((C / 16)>0) {
				carryover = true;
			}
			C = (A+B)%16;
			answer = Integer.toHexString(C) + answer;
			
		}
		
		if (carryover) {
			for (int i = 4; i > 0; i-- ) {
				if (carryover) { Integer A = Integer.parseInt(answer.substring(i-1, i), 16);
				A++;
				if ((A/16)>0) {
					A = 0;
					StringBuilder stuff = new StringBuilder(answer);
					stuff.setCharAt(i-1, Integer.toHexString(A).charAt(0));
					answer = stuff.toString();
					carryover = true;
				} else {
					carryover = false;
				}
				}
			}
		}
		
		return answer;
	}
	
	
}

class ByteIterator implements Iterable<Boolean> {
	private final byte[] check;
	
	public ByteIterator(byte[] array) {
		check = array;
	}
	
	@Override
	public Iterator<Boolean> iterator() {
		return new Iterator<Boolean>() {
			private int curIndex = 0;
			private int bitIndex = 0;
			
			public boolean hasNext() {
				return ((curIndex < check.length) && (bitIndex < 8));
			}
			
			public Boolean next() {
				Boolean value = (check[curIndex] >> (7 - bitIndex) & 1) == 1;
				bitIndex++;
				if (bitIndex == 8) {
					curIndex++;
					bitIndex = 0;
				}
				return value;
			}
			
			
		};
		
	}
	
}