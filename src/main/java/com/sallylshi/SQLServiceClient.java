package com.sallylshi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class SQLServiceClient {

    public static void main(String[] args) {
        if( args.length < 2 ) {
            System.out.println("Not enough arguments.");
            System.exit(0);
        }
        SQLServiceClient sqlServiceClient = new SQLServiceClient();
        sqlServiceClient.execute(args[0], Integer.parseInt(args[1]));
    }

    public void execute(String hostName, int port) {
        try {
            Socket socket = new Socket(hostName, port);

            // Write query to socket for server.
            String sqlQuery = "select strftime('%Y-%m', startTimestampMs / 1000, 'unixepoch') as yearmonth, count(*) as ct from placevisit group by yearmonth order by ct desc;\n";
            socket.getOutputStream().write(sqlQuery.getBytes());

            // Read query results from socket.
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String r;
            while((r = reader.readLine()) != null) {
                System.out.println(r);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
