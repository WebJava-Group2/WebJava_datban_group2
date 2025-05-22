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

  private boolean isValidVNPhoneNumber(String phone) {
    return phone.matches("(?:(\\+84|0084|0))[235789][0-9]{1,2}[0-9]{7}(?:[^\\d]+|$)");
  }

  private boolean isValidPassword(String password) {
    return password.matches("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$");
  }

  public boolean comparePassword(String password, String confirmPassword) {
    return password.equals(confirmPassword);
  }

  private void checkValidUser(User user) throws IllegalArgumentException {
    if (!isValidVNPhoneNumber(user.getPhone())) {
      throw new IllegalArgumentException("Số điện thoại phải là số điện thoại Việt Nam hợp lệ");
    }
    if (!isValidPassword(user.getPassword())) {
      throw new IllegalArgumentException("Mật khẩu phải có ít nhất 8 ký tự, bao gồm chữ hoa, chữ thường, số và ký tự đặc biệt");
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

  public User getUserByEmail(String email) throws SQLException {
    return userRepository.findByEmail(email);
  }

  public void createUser(User user) throws SQLException, IllegalArgumentException {
    checkValidUser(user);
    userRepository.insert(user);
  }

  public void updateUser(User user) throws SQLException, IllegalArgumentException {
    checkValidUser(user);
    userRepository.update(user);
  }

  public void deleteUser(int id) throws SQLException {
    userRepository.delete(id);
  }

  public User findByEmail(String email) throws SQLException {
    return userRepository.findByEmail(email);
  }

  public boolean checkEmailExist(User user) throws SQLException {
    Integer userId = user.getId();
    return userRepository.checkEmailExist(user.getEmail(), userId == null ? 0 : userId);
  }

  public boolean checkPhoneExist(User user) throws SQLException {
    Integer userId = user.getId();
    return userRepository.checkPhoneExist(user.getPhone(), userId == null ? 0 : userId);
  }

  public List<User> findByKeyword(String keyword, int page, int itemsPerPage) throws SQLException {
    return userRepository.findByNameEmailPhone(keyword, page, itemsPerPage);
  }

  public int getTotalUsersByKeyword(String keyword) throws SQLException {
    return userRepository.getTotalUsersByKeyword(keyword);
  }

  public List<User> findByKeywordWithRole(String keyword, String role, int page, int itemsPerPage) throws SQLException {
    return userRepository.findByNameEmailPhoneWithRole(keyword, role, page, itemsPerPage);
  }

  public int getTotalUsersByKeywordWithRole(String keyword, String role) throws SQLException {
    return userRepository.getTotalUsersByNameEmailPhoneWithRole(keyword, role);
  }
}
