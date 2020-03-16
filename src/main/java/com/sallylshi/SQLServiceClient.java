package com.sallylshi;

import java.io.*;
import java.net.Socket;

public class SQLServiceClient {

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Not enough arguments.");
            System.exit(0);
        }
        SQLServiceClient sqlServiceClient = new SQLServiceClient();
        sqlServiceClient.execute(args[0], Integer.parseInt(args[1]));
    }

    public void execute(String hostName, int port) {
        try {
            Console console = System.console();

            while (true) {
                Socket socket = new Socket(hostName, port);

                // Write query to socket for server.
                //Example Query: "select strftime('%Y-%m', startTimestampMs / 1000, 'unixepoch') as yearmonth, count(*) as ct from placevisit group by yearmonth order by ct desc;\n";
                String sqlQuery = console.readLine("Enter SQLite query: ");
                PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
                printWriter.println(sqlQuery);
                System.out.println("Finished writing sql query to socket.");

                // Read query results from socket.
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String r;
                while ((r = reader.readLine()) != null) {
                    System.out.println(r);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
