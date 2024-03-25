package org.studentgradingsystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.studentgradingsystem.model.Role;
import org.studentgradingsystem.model.User;
import org.studentgradingsystem.repository.UserProjection;
import org.studentgradingsystem.repository.UserRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder bCryptPasswordEncoder;


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> getUsersByIds(List<Integer> ids) {
        return userRepository.findAllById(ids);
    }

    public User getUserById(int id) {
        User user = userRepository.findById(id).orElse(null);
        return user;
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    public User addUser(User user) throws IllegalArgumentException{
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User updateUser(User user) {
        if (user.getPassword() != null) {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        }

        return userRepository.save(user);
    }



    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }



    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) {
        try {
            User user = userRepository.findByEmail(email);
            if (user == null) {
                throw new UsernameNotFoundException("User not found");
            }
            return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), Collections.singletonList(new SimpleGrantedAuthority(user.getRole().getName())));
        } catch (IllegalArgumentException e) {
            throw new UsernameNotFoundException("Invalid email format. Please provide a valid email.");
        }
    }


    public List<UserProjection> getInstructors() {
        Role role = roleService.getRoleByName("INSTRUCTOR");
        if (role == null) {
            throw new IllegalArgumentException("Role not found");
        }
        return userRepository.findAllByRole(role);
    }

    public List<UserProjection> getStudents() {
        Role role = roleService.getRoleByName("STUDENT");
        if (role == null) {
            throw new IllegalArgumentException("Role not found");
        }
        return userRepository.findAllByRole(role);
    }

}

