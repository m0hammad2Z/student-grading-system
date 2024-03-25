package org.studentgradingsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.studentgradingsystem.model.Role;
import org.studentgradingsystem.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    public List<UserProjection> findAllByRole(Role role);
    public User findByEmail(String email);
}
