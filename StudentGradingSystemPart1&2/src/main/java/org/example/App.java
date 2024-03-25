package org.example;

import org.example.cli.client.CommandLine;
import org.example.cli.server.SocketServer;


import java.util.Scanner;

public class App
{
    public static void main( String[] args )
    {
        Scanner scanner = new Scanner(System.in);
        // ----------------- CLI -----------------
        System.out.println("Enter 1 to start the server or 2 to start the client");


        int choice = scanner.nextInt();

        if (choice == 1) {
            SocketServer server = new SocketServer(8080);
            server.start();
        } else if (choice == 2) {
            try {
                CommandLine client = new CommandLine("localhost", 8080);
                client.start();
            } catch (Exception e) {
                System.out.println("Something went wrong: " + e.getMessage());
            }
        } else {
            System.out.println("Invalid choice");
        }
    }
}
