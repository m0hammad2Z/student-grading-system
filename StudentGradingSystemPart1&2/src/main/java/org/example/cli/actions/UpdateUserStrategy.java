package org.example.cli.actions;

import org.example.cli.util.DataMode;
import org.example.cli.util.Message;
import org.example.cli.util.MessageData;
import org.example.model.User;
import org.example.service.RoleService;
import org.example.service.UserService;

public class UpdateUserStrategy implements ActionStrategy{
    private final UserService userService;
    private final RoleService roleService;

    public UpdateUserStrategy(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @Override
    public Message execute(Message message) {
        int userId;
        String firstName = (String) message.getData().get("firstName").getContent();
        String lastName = (String) message.getData().get("lastName").getContent();
        String email = (String) message.getData().get("email").getContent();
        String password = (String) message.getData().get("password").getContent();
        int roleId;

        Message.Builder responseBuilder = new Message.Builder();

        try {
            userId = Integer.parseInt((String) message.getData().get("user_id").getContent());
            roleId = Integer.parseInt((String) message.getData().get("role_id").getContent());
        } catch (NumberFormatException e) {
            responseBuilder.withData("error", new MessageData("Invalid user ID or role ID", DataMode.OUTPUT, false));
            return responseBuilder.build();
        }

        try {
            User user = userService.getById(userId);
            firstName = firstName.isEmpty() ? user.getFirstName() : firstName;
            user.setFirstName(firstName);
            lastName = lastName.isEmpty() ? user.getLastName() : lastName;
            user.setLastName(lastName);
            email = email.isEmpty() ? user.getEmail() : email;
            user.setEmail(email);
            user.setPassword(password);
            user.setRole(roleService.getById(roleId));

            userService.update(user);

            responseBuilder.withData("success", new MessageData("User updated successfully", DataMode.OUTPUT, false));
        }
        catch (IllegalArgumentException e) {
            responseBuilder.withData("error", new MessageData(e.getMessage(), DataMode.OUTPUT, false));
        }catch (Exception e) {
            responseBuilder.withData("error", new MessageData("Failed to update user", DataMode.OUTPUT, false));
        }
        return responseBuilder.build();
    }

    @Override
    public Message getPreparedData() {
        return new Message.Builder()
                .withData("user_id", new MessageData("Enter user ID", DataMode.INPUT, true))
                .withData("firstName", new MessageData("Enter first name", DataMode.INPUT, false))
                .withData("lastName", new MessageData("Enter last name", DataMode.INPUT, false))
                .withData("email", new MessageData("Enter email", DataMode.INPUT, false))
                .withData("password", new MessageData("Enter password", DataMode.INPUT, false))
                .withData("role_id", new MessageData("Enter role ID", DataMode.INPUT, false))
                .build();
    }
}
