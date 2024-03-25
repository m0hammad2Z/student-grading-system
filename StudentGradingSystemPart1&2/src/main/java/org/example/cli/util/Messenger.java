package org.example.cli.util;

import java.io.*;
import java.net.Socket;

public class Messenger {
    private final ObjectInputStream objectInputStream;
    private final ObjectOutputStream objectOutputStream;

    public Messenger(Socket socket) throws IOException {

        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectInputStream = new ObjectInputStream(socket.getInputStream());
    }

    // Send a message
    public void send(Message message) {
        try {
            objectOutputStream.writeObject(message);
        } catch (Exception e) {
            System.out.println("Error sending message: " + e.getMessage());
        }
    }

    // Receive a message
    public Message receive() {
        try {
            return (Message) objectInputStream.readObject();
        } catch (Exception e) {
            System.out.println("Error receiving message: " + e.getMessage());
            return null;
        }
    }

    // Send wake up message
    public void wakeUp() {
        try {
            Message message = new Message.Builder().
                    withData("wakeUp", new MessageData("wakeUp", DataMode.OUTPUT, false)).
                    build();
            objectOutputStream.writeObject(message);
        } catch (Exception e) {
            System.out.println("Error sending wake up message: " + e.getMessage());
        }
    }


    public void close() {
        try {
            objectInputStream.close();
            objectOutputStream.close();
        } catch (Exception e) {
            System.out.println("Error closing messenger: " + e.getMessage());
        }
    }

}
