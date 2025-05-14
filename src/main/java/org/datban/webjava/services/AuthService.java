package org.datban.webjava.services;

import org.datban.webjava.repositories.UserRepository;
import org.datban.webjava.models.User;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

import org.datban.webjava.helpers.DatabaseConnector;

public class AuthService {
  private UserRepository userRepository;

  public AuthService() {
    try {
      Connection connection = DatabaseConnector.getConnection();
      userRepository = new UserRepository(connection);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public User authenticate(String email, String password) throws SQLException {
    User user = userRepository.findByEmail(email);
    if (user == null) {
      return null;
    }
    boolean isPasswordCorrect = BCrypt.checkpw(password, user.getPassword());
    boolean isAdmin = Objects.equals(user.getRole(), "admin");
    if (!isAdmin || !isPasswordCorrect) {
      return null;
    }
    return user;
  }
}
