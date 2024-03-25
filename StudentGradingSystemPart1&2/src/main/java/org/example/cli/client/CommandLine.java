package org.example.cli.client;

import org.example.cli.util.DataMode;
import org.example.cli.util.Message;
import org.example.cli.util.MessageData;
import org.example.cli.util.Messenger;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static org.example.cli.util.DataMode.OUTPUT;

public class CommandLine {
    private Socket socket;
    private Messenger messenger;

    Scanner scanner = new Scanner(System.in);

    public CommandLine(String host, int port) throws Exception {
        socket = new Socket(host, port);
        messenger = new Messenger(socket);

        System.out.println("Connected to server");
    }

    public void start() throws IOException {
        try {
            while (true) {
                Message message = messenger.receive();
                if(message.getData().isEmpty())
                    continue;

                if(message.getData().containsKey("exit")){
                    System.out.println(message.getData().get("exit").getContent());
                    break;
                }

                handleMessage(message);
            }
        } catch (Exception e) {
            System.out.println("Connection closed");
        }
        socket.close();
    }

    private void handleMessage(Message message) {
        Map<String, MessageData> data = new HashMap<>();

        for (Map.Entry<String, MessageData> entry : message.getData().entrySet()) {
            switch (entry.getValue().getStatus()) {
                case INPUT:
                    System.out.println(entry.getValue().getContent() + ": " + (entry.getValue().isRequired() ? "" : " (optional)"));

                    String input = scanner.nextLine();
                    if (entry.getValue().isRequired() && input.isEmpty()) {
                        System.out.println("This field is required");
                        handleMessage(message);
                        return;
                    }
                    data.put(entry.getKey(), new MessageData(input, DataMode.INPUT, false));

                    break;
                case OUTPUT:
                    System.out.println(entry.getValue().getContent());
                    break;
            }
        }

        if (!data.isEmpty()) {
            messenger.send(new Message.Builder().withData(data).build());
        }
    }


}
