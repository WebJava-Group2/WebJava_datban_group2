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
}
