package com.sallylshi;

import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MainServer {

    public static void main(String[] args) {
        MainServer mainServer = new MainServer();
        mainServer.execute(Integer.parseInt(args[0]));
    }

    public void execute(int port) {
        try {
            ServerSocket server = new ServerSocket(port);
            createDatabase();

            while(true) {
                Socket socket = server.accept();
                MapsJsonParser mapsJsonParser = new MapsJsonParser();
                JsonReader reader = new JsonReader((new InputStreamReader(socket.getInputStream())));
                mapsJsonParser.read(reader);
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createDatabase() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:test.db");
            Statement stat = conn.createStatement();
            stat.executeUpdate("drop table placevisit;");
            stat.executeUpdate("create table placevisit (lat, long, placeId, address, name, deviceTag, durationMs, startTimestampMs);");
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
