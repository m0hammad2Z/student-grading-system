package org.studentgradingsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.studentgradingsystem.model.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    // Find by name
    public Role findByName(String name);
}
