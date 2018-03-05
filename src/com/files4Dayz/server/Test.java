package com.files4Dayz.server;

import org.mindrot.jbcrypt.BCrypt;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Test {

    public static void main(String[] args) throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(new File("credentials.txt"));
        String user = "admin";
        String pass = BCrypt.hashpw("abc123", BCrypt.gensalt());
        pw.write(user);
        pw.write("\n");
        pw.write(pass);
        pw.close();
    }
}
