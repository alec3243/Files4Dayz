package com.files4Dayz.security;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

public class XorCipher {

	public static byte[] encryptDecrypt(byte[] inputx, File pass) throws IOException {
		InputStream is = new FileInputStream(pass);
		DataInputStream ds = new DataInputStream(is);
		byte[] password = new byte[1024];
		ds.read(password);

		ByteIterator bi1 = new ByteIterator(inputx);
		ByteIterator bi2 = new ByteIterator(password);

		String first = "";
		String second = "";

		for (boolean b : bi1) {
			if (b) {
				first += "1";
			} else {
				first += "0";
			}

		}
		for (boolean b : bi2) {
			if (b) {
				second += "1";
			} else {
				second += "0";
			}

		}
		StringBuilder output = new StringBuilder();

		for (int i = 0; i < first.length(); i++) {
			if (first.charAt(i) == second.charAt(i)) {
				output.append("0");
			} else {
				output.append("1");
			}
		}
		ds.close();
		return output.toString().getBytes("ISO-8859-1");
	}

	static class ByteIterator implements Iterable<Boolean> {
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
}
