package com.files4Dayz.server;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import com.files4Dayz.security.XorCipher;

public class Test {

	public static void main(String[] args) throws IOException {
		File file1 = new File("JPG.jpg");
		File file2 = new File("test");
		InputStream file = new FileInputStream(file1);
		DataInputStream ds = new DataInputStream(file);
		byte[] x = new byte[1];
		ds.read(x);
		byte[] y = XorCipher.encryptDecrypt(x, file2);
		ByteIterator hold = new ByteIterator(y);
		String bits = "";
		for (boolean b : hold) {
			if (b) {
				bits += "1";
		} else {
			bits += "0";
		}}
		System.out.print(bits);
			
		
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


