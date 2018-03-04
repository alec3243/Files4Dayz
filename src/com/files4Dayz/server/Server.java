package com.files4Dayz.server;
import com.files4Dayz.application.FileInfo;
import com.files4Dayz.security.AsciiArmor;

import java.net.*;
import java.io.*;
import static com.files4Dayz.security.Checksum.findchecksum;
import static com.files4Dayz.security.XorCipher.encryptDecrypt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private DataInputStream dataReadIn;
    private DataOutputStream dataSendOut;
    private FileOutputStream fileToSave;
    private ServerSocket server;
    private Socket client;
    private int failTime;
    private boolean successVerification = false;
    private boolean successFileTransfer = true;
    private boolean keepConnection = true;
    private final String userName = "admin";
    private final String password = "abc123";
    private final File key = new File("key.txt");

    public Server(int port){
        if (!(port == 0)) {
            try {
                server = new ServerSocket(port);
                failTime = 3;
                System.out.println("Server established. Waiting for client to send file");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
    }
    public void runAsServer() throws IOException {
        try {
            client = server.accept();
            if (client != null) {
                System.out.println("Got a caller"); }
                verifyCredentials();
            if (successVerification) {
                System.out.println("Login success");
                } else {
                keepConnection = false;
                System.out.println("Login failed. Server will be closed");
                }
        } catch (IOException e) {
            e.printStackTrace(); }

    }

    private void verifyCredentials() throws IOException{
        String userNameClient = null;
        String passwordClient = null;
        dataReadIn = new DataInputStream(client.getInputStream());
        dataSendOut = new DataOutputStream(client.getOutputStream());

        // verify user name and password
        while (failTime > 0) {
            userNameClient = dataReadIn.readUTF();
            passwordClient = dataReadIn.readUTF();
            if (!userNameClient.equals(userName) || !passwordClient.equals(password)) {
                if (failTime > 1) {
                    dataSendOut.writeUTF("Wrong combination");
                    System.out.println("shits wrong lmao");
                }
                failTime -= 1;
            } else {
                successVerification = true;
                System.out.println("Verification success");
                break;
            }
        }
        // if user name is correct, reset failTime
        if (successVerification) {
            dataSendOut.writeUTF("Correct");
            failTime = 3;
        }

        if (failTime == 0) {
            System.out.println("Too many fails. Server will close");
            dataSendOut.writeUTF("Close");
        }
    }
    public FileInfo saveFile() throws IOException {

        // save as fileName sent from client
        String fileName = dataReadIn.readUTF();
        fileToSave = new FileOutputStream(fileName);

        // set each reading chunk to be 1024
        byte[] originalChunk = new byte[1024];

        // decode
        // byte[] data = decode(buffer);

        int read = 0;
        while ((read = dataReadIn.read(originalChunk)) > 0) {
            String hashedValueFromClient = dataReadIn.readUTF();
           
            // base 64 decode
            //decode(originalChunk);

            // decrypt
            //encryptDecrypt(originalChunk, key);
            if (dataReadIn.readUTF().equals("armored")) {
                originalChunk = AsciiArmor.removeArmor(originalChunk);
            }
            if (checkHash(originalChunk, hashedValueFromClient)) {
                fileToSave.write(originalChunk, 0, read);
                System.out.println("correct");
                dataSendOut.writeUTF("correct");
            } else {
               dataSendOut.writeUTF("wrong");
               System.out.println("hash wrong");
                failTime -= 1;
                if (failTime == 0) {
                    successFileTransfer = false;
                    break;
                }
            }
        }

        if (successFileTransfer) {
            dataReadIn.close();
            fileToSave.close();
            FileInfo f = new FileInfo(fileName);
            return f;
        } else {
            System.out.println("Exceed fail time limit! Connection is terminated.");
            return null;
        }
    }
private boolean checkHash(byte[] originalChunk, String hashedValueFromClient) {
        String hashedValueComputed = findchecksum(originalChunk);
        return hashedValueComputed.equals(hashedValueFromClient);
    }
}
