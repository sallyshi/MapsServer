package com.sallylshi;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;

public class MainClient {
    public static void main(String args[]) {
        if( args.length < 2 ) {
            System.out.println("Not enough arguments.");
            System.exit(0);
        }
        MainClient mainClient = new MainClient();
        mainClient.execute(args[0], Integer.parseInt(args[1]));
    }

    public void execute(String hostName, int port) {
        try {
            Socket socket = new Socket(hostName, port);
            byte[] bytes = new byte[1024];
            FileInputStream fileInputStream = new FileInputStream("/Users/sshii/IdeaProjects/MapsServer/src/resources/2020_JANUARY.json");
            int bytesRead;
            while ((bytesRead = fileInputStream.read(bytes)) > -1) {
                socket.getOutputStream().write(bytes, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
