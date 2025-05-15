package org.datban.webjava.services;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import org.datban.webjava.models.User;
import org.datban.webjava.repositories.UserRepository;
import org.datban.webjava.helpers.DatabaseConnector;

public class UserService {
    private UserRepository userRepository;

    public UserService() {
        try {
            Connection connection = DatabaseConnector.getConnection();
            this.userRepository = new UserRepository(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<User> getAllUsers() throws SQLException {
        return userRepository.getAll();
    }

    public List<User> getUsers(int page, int itemsPerPage) throws SQLException {
        return userRepository.getUsersByPage(page, itemsPerPage);
    }

    public List<User> getUsersByRole(int page, int itemsPerPage, String role) throws SQLException {
        return userRepository.getUsersByPageAndRole(page, itemsPerPage, role);
    }

    public int getTotalUsers() throws SQLException {
        return userRepository.getTotalUsers();
    }

    public int getTotalUsersByRole(String role) throws SQLException {
        return userRepository.getTotalUsersByRole(role);
    }

    public User getUserById(int id) throws SQLException {
        return userRepository.getById(id);
    }

    public void createUser(User user) throws SQLException {
        userRepository.insert(user);
    }

    public void updateUser(User user) throws SQLException {
        userRepository.update(user);
    }

    public void deleteUser(int id) throws SQLException {
        userRepository.delete(id);
    }

    public User findByEmail(String email) throws SQLException {
        return userRepository.findByEmail(email);
    }

    public boolean checkEmailExist(User user) throws SQLException {
        return userRepository.checkEmailExist(user.getEmail(), user.getId());
    }

    public boolean checkPhoneExist(User user) throws SQLException {
        return userRepository.checkPhoneExist(user.getPhone(), user.getId());
    }
}
