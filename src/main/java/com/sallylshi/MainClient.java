package com.sallylshi;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

public class MainClient {
    public static void main(String args[]) {
        if( args.length < 3 ) {
            System.out.println("Not enough arguments.");
            System.exit(0);
        }
        MainClient mainClient = new MainClient();
        for (int i = 2; i < args.length - 1; i++) {
            mainClient.execute(args[0], Integer.parseInt(args[1]), args[i]);
        }
    }

    public void execute(String hostName, int port, String filePath) {
        //"/Users/sshii/IdeaProjects/MapsServer/src/resources/2020_JANUARY.json"
        try {
            Socket socket = new Socket(hostName, port);
            byte[] bytes = new byte[1024];
            FileInputStream fileInputStream = new FileInputStream(filePath);
            int bytesRead;
            while ((bytesRead = fileInputStream.read(bytes)) > -1) {
                socket.getOutputStream().write(bytes, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
