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
    }

    protected void setInsertParameters(PreparedStatement statement, User entity) throws SQLException {
        statement.setString(1, entity.getName());
        statement.setString(2, entity.getEmail());
        statement.setString(3, entity.getPhone());
        statement.setString(4, entity.getPassword());
        statement.setString(5, entity.getRole());
        statement.setTimestamp(6, entity.getCreatedAt());
    }

    @Override
    public void insert(User entity) throws SQLException {
        String query = getInsertQuery();
        PreparedStatement statement = connection.prepareStatement(query);
        setInsertParameters(statement, entity);
        statement.executeUpdate();
    }
    
    @Override
    protected String getTableName() {
        return "users";
    }

    public User findByEmail(String email) throws SQLException {
        String query = getDisplayQuery() + " WHERE email = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, email);
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

    public List<User> getUsersByPageAndRole(int page, int itemsPerPage, String role) throws SQLException {
        String query = getDisplayQuery() + " WHERE role = ? LIMIT ? OFFSET ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, role);
        statement.setInt(2, itemsPerPage);
        statement.setInt(3, getOffset(page, itemsPerPage));
        ResultSet resultSet = statement.executeQuery();
        
        List<User> users = new ArrayList<>();
        while (resultSet.next()) {
            users.add(mapResultSetToEntity(resultSet));
        }
        return users;
    }

    public int getTotalUsersByRole(String role) throws SQLException {
        String query = "SELECT COUNT(*) FROM " + getTableName() + " WHERE role = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, role);
        ResultSet resultSet = statement.executeQuery();
        
        if (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return 0;
    }

    public boolean checkEmailExist(String email, int id) throws SQLException {
        String query = "SELECT COUNT(*) FROM " + getTableName() + " WHERE email = ? AND id != ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, email);
        statement.setInt(2, id);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            return resultSet.getInt(1) > 0;
        }
        return false;
    }

    public boolean checkPhoneExist(String phone, int id) throws SQLException {
        String query = "SELECT COUNT(*) FROM " + getTableName() + " WHERE phone = ? AND id != ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, phone);
        statement.setInt(2, id);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            return resultSet.getInt(1) > 0;
        }
        return false;
    }

    public List<User> findByNameEmailPhone(String keyword, int page, int itemsPerPage) throws SQLException {
        String query = getDisplayQuery() + 
                      " WHERE name LIKE ? OR email LIKE ? OR phone LIKE ? " +
                      "LIMIT ? OFFSET ?";
        PreparedStatement statement = connection.prepareStatement(query);
        String likePattern = "%" + keyword + "%";
        statement.setString(1, likePattern);
        statement.setString(2, likePattern);
        statement.setString(3, likePattern);
        statement.setInt(4, itemsPerPage);
        statement.setInt(5, getOffset(page, itemsPerPage));
        ResultSet resultSet = statement.executeQuery();
        List<User> users = new ArrayList<>();
        while (resultSet.next()) {
            users.add(mapResultSetToEntity(resultSet));
        }
        return users;
    }

    public int getTotalUsersByKeyword(String keyword) throws SQLException {
        String query = "SELECT COUNT(*) FROM " + getTableName() + 
                      " WHERE name LIKE ? OR email LIKE ? OR phone LIKE ?";
        PreparedStatement statement = connection.prepareStatement(query);
        String likePattern = "%" + keyword + "%";
        statement.setString(1, likePattern);
        statement.setString(2, likePattern);
        statement.setString(3, likePattern);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return 0;
    }

    public List<User> findByNameEmailPhoneWithRole(String keyword, String role, int page, int itemsPerPage) throws SQLException {
        String query = getDisplayQuery() + 
                      " WHERE (name LIKE ? OR email LIKE ? OR phone LIKE ?) AND role = ? " +
                      "LIMIT ? OFFSET ?";
        PreparedStatement statement = connection.prepareStatement(query);
        String likePattern = "%" + keyword + "%";
        statement.setString(1, likePattern);
        statement.setString(2, likePattern);
        statement.setString(3, likePattern);
        statement.setString(4, role);
        statement.setInt(5, itemsPerPage);
        statement.setInt(6, getOffset(page, itemsPerPage));
        ResultSet resultSet = statement.executeQuery();
        List<User> users = new ArrayList<>();
        while (resultSet.next()) {
            users.add(mapResultSetToEntity(resultSet));
        }
        return users;
    }

    public int getTotalUsersByNameEmailPhoneWithRole(String keyword, String role) throws SQLException {
        String query = "SELECT COUNT(*) FROM " + getTableName() + 
                      " WHERE (name LIKE ? OR email LIKE ? OR phone LIKE ?) AND role = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        String likePattern = "%" + keyword + "%";
        statement.setString(1, likePattern);
        statement.setString(2, likePattern);
        statement.setString(3, likePattern);
        statement.setString(4, role);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return 0;
    }

    private int getOffset(int page, int itemsPerPage) {
        return (page - 1) * itemsPerPage;
    }
}
