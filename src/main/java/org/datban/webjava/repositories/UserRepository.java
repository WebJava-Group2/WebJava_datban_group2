package org.datban.webjava.repositories;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.datban.webjava.models.User;
import org.datban.webjava.repositories.base.BaseRepository;
import org.springframework.security.crypto.bcrypt.BCrypt;


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
    protected String getUpdateQuery() {
        return "UPDATE users SET name = ?, email = ?, phone = ?, password = ?, role = ? WHERE id = ?";
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

    public List<User> findUsersByEmailAndPassword(String email, String password) {
        List<User> users = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // Truy vấn để lấy thông tin người dùng theo email
            String sql = "SELECT id, name, email, phone, password, role, created_at FROM users WHERE email = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, email);
            rs = stmt.executeQuery();

            // Nếu tìm thấy người dùng
            while (rs.next()) {
                // Lấy mật khẩu đã mã hóa trong cơ sở dữ liệu
                String hashedPassword = rs.getString("password");

                // So sánh mật khẩu người dùng nhập vào với mật khẩu đã mã hóa trong cơ sở dữ liệu
                if (BCrypt.checkpw(password, hashedPassword)) {
                    // Nếu mật khẩu đúng, tạo đối tượng User và thêm vào danh sách
                    User user = new User(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("phone"),
                            rs.getString("password"),
                            rs.getString("role"),
                            rs.getTimestamp("created_at")
                    );
                    users.add(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return users;
    }

    public int insertUsers(User user) throws SQLException {
        String query = "INSERT INTO user(name, email, phone, role) VALUES (?, ?, ?, ?)";
        int isSuccess = 0;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getPhone());
            preparedStatement.setString(4, user.getRole());
            preparedStatement.executeUpdate();

            ResultSet rs = preparedStatement.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return isSuccess;
    }

    public int findUserIdByEmailOrPhone(String email, String phone) throws SQLException {
        String query = "SELECT id FROM user WHERE email = ? OR phone = ? LIMIT 1";
        int isSuccess = 0;
        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, email);
            stmt.setString(2, phone);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return isSuccess;
    }

}
