package org.example.dao;

import org.example.model.*;
import org.example.util.DatabaseConnection;

import java.sql.*;
import java.util.*;


public class RoleDAOImpl implements RoleDAO {
    private final Connection connection;

    public RoleDAOImpl() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public Role createRole(Role role) {
        String sql = "INSERT INTO roles (name) VALUES (?)";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, role.getName().toString());
            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                int roleId = resultSet.getInt(1);
                return getRoleById(roleId);
            }
            return role;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void deleteRole(int id) {

        String sql = "DELETE FROM roles WHERE id = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Role> getAllRoles() {
        List<Role> roles = new ArrayList<>();
        String sql = "Select * from roles";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int roleId = resultSet.getInt("id");
                String roleName = resultSet.getString("name");
                Role role = new Role(roleId, roleName);
                roles.add(role);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roles;
    }


    @Override
    public Role getRoleById(int id) {
        String sql = "select * from roles where id = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int roleId = resultSet.getInt("id");
                String roleName = resultSet.getString("name");
                return new Role(roleId, roleName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }



    @Override
    public Role getRoleByName(String name) {
        String sql = "select * from roles where name = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int roleId = resultSet.getInt("id");
                String roleName = resultSet.getString("name");
                return new Role(roleId, roleName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Role updateRole(Role role) {

        String sql = "UPDATE roles SET name = ? WHERE id = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, role.getName().toString());
            preparedStatement.setInt(2, role.getId());
            preparedStatement.executeUpdate();

            return role;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
