package com.files4Dayz.security;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;

public class XorCipher {

    public static byte[] encryptDecrypt(byte[] inputx, String pass) throws UnsupportedEncodingException {
        char[] key = pass.toCharArray();
        String input = inputx.toString();
        StringBuilder output = new StringBuilder();

        for (int i = 0; i < input.length(); i++) {
            output.append((char) (input.charAt(i) ^ key[i % key.length]));
        }

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


