package com.sallylshi;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;

import static java.sql.Types.*;

public class SQLService {

    public static void main (String[] args) {

    }

    public void execute(int port) {
        try {
            ServerSocket server = new ServerSocket(port);

            while(true) {
                Socket socket = server.accept();

                Connection conn = DriverManager.getConnection("jdbc:sqlite:test.db");
                Statement stat = conn.createStatement();

                ByteArrayOutputStream result = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int length;
                while((length = socket.getInputStream().read(buffer)) > -1) {
                    result.write(buffer, 0, length);
                }
                String sqlQuery = result.toString("UTF-8");
                ResultSet rs = stat.executeQuery(sqlQuery);
                PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);

                while(!rs.next()) {
                    int cols = rs.getMetaData().getColumnCount();
                    for(int i = 0; i < cols; i++) {
                        printWriter.write(rs.getString(i) + " ");
                    }
                    printWriter.println();
                }
                socket.close();
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
}
