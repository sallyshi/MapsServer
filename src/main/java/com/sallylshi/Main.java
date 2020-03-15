package com.sallylshi;

import com.google.gson.stream.JsonReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.sql.*;

public class Main {

    public static void main(String[] args) {
        Main main = new Main();
        MapsJsonParser mapsJsonParser = new MapsJsonParser();
        main.openDatabase();
        for(String s : args) {
            JsonReader reader = new JsonReader((new InputStreamReader(main.processFile(s))));
            mapsJsonParser.read(reader);
        }
       // main.executeQuery();
    }

    public void openDatabase() {
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

    public void executeQuery() {
        try{
        Connection conn = DriverManager.getConnection("jdbc:sqlite:test.db");
        Statement stat = conn.createStatement();
        ResultSet rs = stat.executeQuery("select name, count(*) as ct from placevisit group by name order by ct desc;");
            while (rs.next()) {
                System.out.println("name = " + rs.getString("name"));
                System.out.println("ct = " + rs.getLong("ct"));
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public FileInputStream processFile(String fileName) {
        try {
            return new FileInputStream("/Users/sshii/IdeaProjects/MapsServer/src/resources/" + fileName +".json");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
