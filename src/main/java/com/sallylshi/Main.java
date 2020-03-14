package com.sallylshi;

import com.google.gson.stream.JsonReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {

    public static void main(String[] args) {
        Main main = new Main();
        MapsJsonParser mapsJsonParser = new MapsJsonParser();
        JsonReader reader = new JsonReader((new InputStreamReader(main.processFile())));

        main.openDatabase();
        mapsJsonParser.read(reader);
    }

    public void openDatabase() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:test.db");
            Statement stat = conn.createStatement();
            stat.executeUpdate("drop table placevisit;");
            stat.executeUpdate("create table placevisit (lat, long, placeId, address, name, deviceTag, durationMs);");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public FileInputStream processFile() {
        try {
            return new FileInputStream("/Users/sshii/IdeaProjects/MapsServer/src/resources/2020_JANUARY.json");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
