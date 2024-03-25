package org.example.service;

import org.example.dao.UserDAOImpl;
import org.example.model.User;
import org.example.util.PasswordHash;
import org.example.util.Validator;

import java.util.List;

public class UserService  {
    private final UserDAOImpl userDAO;
    private final RoleService roleService;
    private final Validator validator;

    public UserService(UserDAOImpl userDAO, RoleService roleService) {
        this.userDAO = userDAO;
        this.roleService = roleService;
        this.validator = new Validator();
    }

    public User create(User user) {
        validator.validObject(user)
                .validEmail(user.getEmail())
                .validPassword(user.getPassword())
                .validName(user.getFirstName())
                .validName(user.getLastName())
                .validId(user.getRole().getId())
                .validate();
        if (userDAO.getUserByEmail(user.getEmail()) != null) {
            throw new IllegalArgumentException("Email already exists");
        }

        if(roleService.getById(user.getRole().getId()) == null){
            throw new IllegalArgumentException("Role not found");
        }

        user.setPassword(PasswordHash.hash(user.getPassword()));
        User createdUser = userDAO.insertUser(user);
        validator.validObject(createdUser).validate();
        return createdUser;
    }


    public void delete(int id) {
        validator.validId(id).validate();

        if (userDAO.getUserById(id) == null) {
            throw new IllegalArgumentException("User not found");
        }

        userDAO.deleteUser(id);
    }

    public List<User> getAll() {
        List<User> users = userDAO.getAllUsers();
        validator.validObject(users).validate();

        for (User user : users) {
            user.setRole(roleService.getById(user.getRole().getId()));
        }
        return users;
    }

    public User getById(int id) {
        validator.validId(id).validate();
        User user = userDAO.getUserById(id);
        if (user != null) {
            user.setRole(roleService.getById(user.getRole().getId()));
        }
        return user;
    }

    public User update(User user) {
        validator.validObject(user)
                .validId(user.getId())
                .validEmail(user.getEmail())
                .validName(user.getFirstName())
                .validName(user.getLastName())
                .validId(user.getRole().getId())
                .validate();

           if (userDAO.getUserById(user.getId()) == null) {
               throw new IllegalArgumentException("User not found");
           }

        if(user.getPassword() != null && !user.getPassword().isEmpty()){
            user.setPassword(PasswordHash.hash(user.getPassword()));
        }else{
            user.setPassword(userDAO.getUserById(user.getId()).getPassword());
        }

        User updatedUser = userDAO.updateUser(user);
        validator.validObject(updatedUser).validate();
        return updatedUser;
    }


    public User login(String email, String password) {
        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Email and password are required");
        }

        User user = userDAO.getUserByEmail(email);
        if (user != null && PasswordHash.verify(password, user.getPassword())) {
            user.setRole(roleService.getById(user.getRole().getId()));
        }else{
            throw new IllegalArgumentException("Invalid email or password");
        }
        return user;
    }

    public List<User> getAllInstructors(int courseId) {
        validator.validId(courseId, "Course ID is required").validate();
        List<User> users = userDAO.getAllinstructors(courseId);

        for (User user : users) {
            user.setRole(roleService.getById(user.getRole().getId()));
        }
        return users;
    }

    public List<User> getAllUsersByRole(int roleId) {
        validator.validId(roleId, "Role ID is required").validate();
        List<User> users = userDAO.getAllUsersByRole(roleService.getById(roleId));

        for (User user : users) {
            user.setRole(roleService.getById(user.getRole().getId()));
        }
        return users;
    }

    public List<User> getAllStudents(int courseId) {
        validator.validId(courseId, "Course ID is required").validate();
        List<User> users = userDAO.getAllStudents(courseId);

        for (User user : users) {
            user.setRole(roleService.getById(user.getRole().getId()));
        }
        return users;
    }
}
