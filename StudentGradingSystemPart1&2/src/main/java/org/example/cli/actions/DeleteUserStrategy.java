package org.example.cli.actions;

import org.example.cli.util.DataMode;
import org.example.cli.util.Message;
import org.example.cli.util.MessageData;
import org.example.service.UserService;

public class DeleteUserStrategy implements ActionStrategy {
    private final UserService userService;

    public DeleteUserStrategy(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Message execute(Message message) {
        int userId;

        Message.Builder responseBuilder = new Message.Builder();

        try {
            userId = Integer.parseInt((String) message.getData().get("user_id").getContent());
        } catch (NumberFormatException e) {
            responseBuilder.withData("error", new MessageData("Invalid user ID", DataMode.OUTPUT, false));
            return responseBuilder.build();
        }

        try {
            userService.delete(userId);

            responseBuilder.withData("success", new MessageData("User deleted successfully", DataMode.OUTPUT, false));
        }
        catch (IllegalArgumentException e) {
            responseBuilder.withData("error", new MessageData(e.getMessage(), DataMode.OUTPUT, false));
        }catch (Exception e) {
            responseBuilder.withData("error", new MessageData("Failed to delete user", DataMode.OUTPUT, false));
        }
        return responseBuilder.build();
    }

    @Override
    public Message getPreparedData() {
        return new Message.Builder()
                .withData("user_id", new MessageData("Enter user ID", DataMode.INPUT, true))
                .build();
    }
}