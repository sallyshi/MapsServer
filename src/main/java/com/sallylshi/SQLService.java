package com.sallylshi;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;

public class SQLService {

    public static void main (String[] args) {
        SQLService sqlService = new SQLService();
        sqlService.execute(Integer.parseInt(args[0]));
    }

    public void execute(int port) {
        try {
            ServerSocket server = new ServerSocket(port);

            while(true) {
                Socket socket = server.accept();

                // Connect to database.
                Connection conn = DriverManager.getConnection("jdbc:sqlite:test.db");
                Statement stat = conn.createStatement();
                System.out.println("Connected to SQLite DB.");

                // Read SQL Query from server and execute query.
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String sqlQuery = reader.readLine();
                ResultSet rs = stat.executeQuery(sqlQuery);
                System.out.println("Finished executing sql query.");

                // Print out query results.
                PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
                while(!rs.next()) {
                    int cols = rs.getMetaData().getColumnCount();
                    for(int i = 0; i < cols; i++) {
                        printWriter.write(rs.getString(i) + " ");
                    }
                    printWriter.println();
                }
                System.out.println("Finished printing sql query results.");
                socket.close();
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
}
