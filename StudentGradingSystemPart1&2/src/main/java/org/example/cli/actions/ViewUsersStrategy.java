package org.example.cli.actions;

import org.example.cli.util.DataMode;
import org.example.cli.util.Message;
import org.example.cli.util.MessageData;
import org.example.model.User;
import org.example.service.UserService;

public class ViewUsersStrategy implements ActionStrategy {
    private final UserService userService;

    public ViewUsersStrategy(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Message execute(Message message) {
        return new Message.Builder()
                .withData("empty", new MessageData("", DataMode.OUTPUT, false))
                .build();
    }

    @Override
    public Message getPreparedData() {
        Message.Builder responseBuilder = new Message.Builder();
        try {
            StringBuilder users = new StringBuilder();
            for (User user : userService.getAll()) {
                users.append(user).append("\n");
            }

            responseBuilder.withData("users", new MessageData(users.toString(), DataMode.OUTPUT, false));
        }catch (IllegalArgumentException e) {
            responseBuilder.withData("error", new MessageData(e.getMessage(), DataMode.OUTPUT, false));
        } catch (Exception e) {
            responseBuilder.withData("error", new MessageData("Failed to retrieve users", DataMode.OUTPUT, false));
        }

        return responseBuilder.build();

    }
}