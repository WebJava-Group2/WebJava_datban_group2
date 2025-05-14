package org.datban.webjava.repositories;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.datban.webjava.models.User;
import org.datban.webjava.repositories.base.BaseRepository;

public class UserRepository extends BaseRepository<User, Integer> {

    public UserRepository(Connection connection) {
        super(connection);
    }

    @Override
    protected String getDisplayQuery() {
        return "SELECT id, name, email, phone, password, role, created_at " +
               "FROM users";
    }

    @Override
    protected User mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt("id"));
        user.setName(resultSet.getString("name"));
        user.setEmail(resultSet.getString("email"));
        user.setPhone(resultSet.getString("phone"));
        user.setPassword(resultSet.getString("password"));
        user.setRole(resultSet.getString("role"));
        user.setCreatedAt(resultSet.getTimestamp("created_at"));
        return user;
    }

    @Override
    protected String getInsertQuery() {
        return "INSERT INTO users (name, email, phone, password, role, created_at) VALUES (?, ?, ?, ?, ?, ?)";
    }

    @Override
    protected String getUpdateQuery(Integer id) {
        return "UPDATE users SET name = ?, email = ?, phone = ?, password = ?, role = ? WHERE id = " + id;
    }

    @Override
    protected void setEntityParameters(PreparedStatement statement, User entity) throws SQLException {
        statement.setString(1, entity.getName());
        statement.setString(2, entity.getEmail());
        statement.setString(3, entity.getPhone());
        statement.setString(4, entity.getPassword());
        statement.setString(5, entity.getRole());
        statement.setTimestamp(6, entity.getCreatedAt());
    }
    
    @Override
    protected String getTableName() {
        return "users";
    }

    public User findByUsername(String username) throws SQLException {
        String query = getDisplayQuery() + " WHERE username = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, username);
        ResultSet resultSet = statement.executeQuery();
        
        if (resultSet.next()) {
            return mapResultSetToEntity(resultSet);
        }
        return null;
    }

    public List<User> getUsersByPage(int page, int itemsPerPage) throws SQLException {
        return getWithPaginate(page, itemsPerPage);
    }

    public int getTotalUsers() throws SQLException {
        String query = "SELECT COUNT(*) FROM " + getTableName();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        
        if (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return 0;
    }
}
