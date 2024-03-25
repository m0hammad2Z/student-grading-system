package org.example.dao;

import org.example.model.Role;
import org.example.model.User;

import java.util.List;

public interface UserDAO {
     User insertUser(User user);
     User updateUser(User user);
     void deleteUser(int id);
     User getUserByEmail(String email);
     User getUserById(int id);
     List<User> getAllUsers();
     List<User> getAllUsersByRole(Role role);
     List<User> getAllinstructors(int courseId);
     List<User> getAllStudents(int courseId);
     List<User> getAllStudents(int courseId, int instructorId);


}
