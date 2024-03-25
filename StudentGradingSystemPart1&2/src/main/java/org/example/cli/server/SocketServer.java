package org.example.cli.server;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketServer {
    private final int port;
    private ServerSocket serverSocket;

    private final ExecutorService executorService = Executors.newCachedThreadPool();


    public SocketServer(int port) {
        this.port = port;
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started on port " + port);

            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    ClientHandler clientHandler = new ClientHandler(clientSocket);
                    System.out.println("Client connected: " + clientSocket.getRemoteSocketAddress());
                    executorService.execute(clientHandler);
                } catch (SocketTimeoutException e) {
                    System.out.println("Socket timeout");
                }
            }

        } catch (IOException e) {
            System.out.println("Error starting server: " + e.getMessage());
        }

        stop();
    }

    public void stop() {
        try {
            serverSocket.close();
            System.out.println("Server stopped");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}