package org.example.service;

import org.example.dao.RoleDAO;
import org.example.model.Role;
import org.example.util.Validator;

import java.util.List;

public class RoleService {
    private final RoleDAO roleDAO;
    private final Validator validator;

    public RoleService(RoleDAO roleDAO) {
        this.roleDAO = roleDAO;
        this.validator = new Validator();
    }

    public Role create(Role role) {
        validator.validObject(role)
                .validName(role.getName())
                .validate();

        Role createdRole = roleDAO.createRole(role);
        validator.validObject(createdRole).validate();
        return createdRole;
    }

    public void delete(int id) {
        validator.validId(id).validate();
        if (roleDAO.getRoleById(id) == null) {
            throw new IllegalArgumentException("Role not found");
        }
        roleDAO.deleteRole(id);
    }

    public List<Role> getAll() {
        return roleDAO.getAllRoles();
    }

    public Role getById(int id) {
        validator.validId(id).validate();
        Role role = roleDAO.getRoleById(id);
        validator.validObject(role).validate();
        return role;
    }

    public void update(Role role) {
        validator.validObject(role)
                .validName(role.getName())
                .validate();

        roleDAO.updateRole(role);
    }

    public Role getRoleByName(String name) {
        validator.validName(name).validate();
        Role role = roleDAO.getRoleByName(name);
        validator.validObject(role).validate();
        return role;
    }

}
