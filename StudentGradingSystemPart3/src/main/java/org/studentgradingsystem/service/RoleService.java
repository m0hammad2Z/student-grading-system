package org.studentgradingsystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.studentgradingsystem.model.Role;
import org.studentgradingsystem.repository.RoleRepository;

import java.util.List;
import java.util.Set;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public List<Role> getAllRoles() {
        List<Role> roles = roleRepository.findAll();

        if (roles.isEmpty()) {
            throw new IllegalArgumentException("No roles found");
        }

        return roles;
    }

    public Role getRoleById(int id) {
        Role role = roleRepository.findById(id).orElse(null);

        if (role == null) {
            throw new IllegalArgumentException("Role not found");
        }

        return role;
    }

    public Role updateRole(Role role) {
        Role existingRole = roleRepository.findById(role.getId()).orElse(null);

        if (existingRole == null) {
            throw new IllegalArgumentException("Role not found");
        }

        existingRole.setName(role.getName());
        return roleRepository.save(existingRole);
    }


    public Role getRoleByName(String name) {
        Role role = roleRepository.findByName(name);

        if (role == null) {
            throw new IllegalArgumentException("Role not found");
        }

        if (role.getName() == null) {
            throw new IllegalArgumentException("Role name cannot be null");
        }
        return role;
    }
}