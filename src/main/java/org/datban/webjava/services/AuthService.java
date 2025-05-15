package org.datban.webjava.services;

import org.datban.webjava.repositories.UserRepository;
import org.datban.webjava.models.User;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

import org.datban.webjava.helpers.DatabaseConnector;
import org.datban.webjava.helpers.impl.IHashedHelper;
import org.datban.webjava.helpers.HashedHelper;

public class AuthService {
  private UserRepository userRepository;
  private IHashedHelper hashedHelper;

  public AuthService() {
    try {
      Connection connection = DatabaseConnector.getConnection();
      userRepository = new UserRepository(connection);
      hashedHelper = new HashedHelper();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public User authenticate(String email, String password) throws SQLException {
    User user = userRepository.findByEmail(email);
    if (user == null) {
      return null;
    }
    boolean isPasswordCorrect = hashedHelper.isPasswordValid(password, user.getPassword());
    boolean isAdmin = Objects.equals(user.getRole(), "admin");
    if (!isAdmin || !isPasswordCorrect) {
      return null;
    }
    return user;
  }
}
