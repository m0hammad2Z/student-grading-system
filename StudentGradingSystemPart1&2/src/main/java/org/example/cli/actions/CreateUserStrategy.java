package org.example.cli.actions;

import org.example.cli.util.DataMode;
import org.example.cli.util.Message;
import org.example.cli.util.MessageData;
import org.example.model.Role;
import org.example.model.User;
import org.example.service.UserService;
import org.example.service.RoleService;

public class CreateUserStrategy implements ActionStrategy {
    private final UserService userService;
    private final RoleService roleService;

    public CreateUserStrategy(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @Override
    public Message execute(Message message) {
        String firstName = (String) message.getData().get("firstName").getContent();
        String lastName = (String) message.getData().get("lastName").getContent();
        String email = (String) message.getData().get("email").getContent();
        String password = (String) message.getData().get("password").getContent();
        int roleId;

        Message.Builder responseBuilder = new Message.Builder();

        try {
            roleId = Integer.parseInt((String) message.getData().get("role_id").getContent());
        } catch (NumberFormatException e) {
            responseBuilder.withData("error", new MessageData("Invalid role ID", DataMode.OUTPUT, false));
            return responseBuilder.build();
        }

        try {
            Role role = roleService.getById(roleId);

            User user = new User(firstName, lastName, email, password);
            user.setRole(role);
            userService.create(user);

            responseBuilder.withData("success", new MessageData("User created successfully", DataMode.OUTPUT, false));
        }
        catch (IllegalArgumentException e) {
            responseBuilder.withData("error", new MessageData(e.getMessage(), DataMode.OUTPUT, false));
        }catch (Exception e) {
            responseBuilder.withData("error", new MessageData("Failed to create user", DataMode.OUTPUT, false));
        }
        return responseBuilder.build();
    }

    @Override
    public Message getPreparedData() {
        return new Message.Builder()
                .withData("firstName", new MessageData("Enter first name", DataMode.INPUT, true))
                .withData("lastName", new MessageData("Enter last name", DataMode.INPUT, true))
                .withData("email", new MessageData("Enter email", DataMode.INPUT, true))
                .withData("password", new MessageData("Enter password", DataMode.INPUT, true))
                .withData("role_id", new MessageData("Enter role id", DataMode.INPUT, true))
                .build();
    }
}