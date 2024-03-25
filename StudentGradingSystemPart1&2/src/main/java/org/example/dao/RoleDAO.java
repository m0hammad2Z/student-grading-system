package org.example.dao;

import org.example.model.Role;
import java.util.List;

public interface RoleDAO {
    Role createRole(Role role);
    void deleteRole(int id);
    List<Role> getAllRoles();
    Role getRoleById(int id);
    Role getRoleByName(String name);
    Role updateRole(Role role);
}
