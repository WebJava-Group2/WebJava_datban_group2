package org.datban.webjava.repositories;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.datban.webjava.models.Combo;
import org.datban.webjava.repositories.base.BaseRepository;

public class ComboRepository extends BaseRepository<Combo, Integer> {

  public ComboRepository(Connection connection) {
    super(connection);
  }

  @Override
  protected String getDisplayQuery() {
    return "SELECT id, name, price, description, status, image_url " +
            "FROM combos";
  }

  @Override
  protected Combo mapResultSetToEntity(ResultSet resultSet) throws SQLException {
    Combo combo = new Combo();
    combo.setId(resultSet.getInt("id"));
    combo.setName(resultSet.getString("name"));
    combo.setPrice(resultSet.getFloat("price"));
    combo.setDescription(resultSet.getString("description"));
    combo.setStatus(resultSet.getString("status"));
    combo.setImageUrl(resultSet.getString("image_url"));
    return combo;
  }

  @Override
  protected String getInsertQuery() {
    return "INSERT INTO combos (name, price, description, status, image_url) VALUES (?, ?, ?, ?, ?)";
  }

  @Override
  protected String getUpdateQuery(Integer id) {
    return "UPDATE combos SET name = ?, price = ?, description = ?, status = ?, image_url = ? WHERE id = " + id;
  }

  @Override
  protected void setEntityParameters(PreparedStatement statement, Combo entity) throws SQLException {
    statement.setString(1, entity.getName());
    statement.setFloat(2, entity.getPrice());
    statement.setString(3, entity.getDescription());
    statement.setString(4, entity.getStatus());
    statement.setString(5, entity.getImageUrl());
  }

  @Override
  protected String getTableName() {
    return "combos";
  }

  public List<Combo> getCombosByPage(int page, int itemsPerPage) throws SQLException {
    return getWithPaginate(page, itemsPerPage);
  }

  public int getTotalCombos() throws SQLException {
    return count();
  }

  public List<Combo> getCombosByPageAndStatus(int page, int itemsPerPage, String status) throws SQLException {
    int offset = (page - 1) * itemsPerPage;
    String query = this.getDisplayQuery() + " WHERE status = ? LIMIT ? OFFSET ?";
    PreparedStatement statement = connection.prepareStatement(query);
    statement.setString(1, status);
    statement.setInt(2, itemsPerPage);
    statement.setInt(3, offset);
    ResultSet resultSet = statement.executeQuery();
    List<Combo> combos = new ArrayList<>();
    while (resultSet.next()) {
      combos.add(mapResultSetToEntity(resultSet));
    }
    return combos;
  }

  public int getComboCountByStatus(String status) throws SQLException {
    String query = "SELECT COUNT(*) FROM " + getTableName() + " WHERE status = ?";
    PreparedStatement statement = connection.prepareStatement(query);
    statement.setString(1, status);
    ResultSet resultSet = statement.executeQuery();
    if (resultSet.next()) {
      return resultSet.getInt(1);
    }
    return 0;
  }

  public List<Combo> findByKeyword(String keyword, int page, int itemsPerPage) throws SQLException {
    int offset = (page - 1) * itemsPerPage;
    String query = getDisplayQuery() + 
                  " WHERE name LIKE ? " +
                  "LIMIT ? OFFSET ?";
    PreparedStatement statement = connection.prepareStatement(query);
    String likePattern = "%" + keyword + "%";
    statement.setString(1, likePattern);
    statement.setInt(2, itemsPerPage);
    statement.setInt(3, offset);
    ResultSet resultSet = statement.executeQuery();
    List<Combo> combos = new ArrayList<>();
    while (resultSet.next()) {
      combos.add(mapResultSetToEntity(resultSet));
    }
    return combos;
  }

  public int getTotalCombosByKeyword(String keyword) throws SQLException {
    String query = "SELECT COUNT(*) FROM " + getTableName() + 
                  " WHERE name LIKE ?";
    PreparedStatement statement = connection.prepareStatement(query);
    String likePattern = "%" + keyword + "%";
    statement.setString(1, likePattern);
    ResultSet resultSet = statement.executeQuery();
    if (resultSet.next()) {
      return resultSet.getInt(1);
    }
    return 0;
  }

  public List<Combo> findByKeywordAndStatus(String keyword, String status, int page, int itemsPerPage) throws SQLException {
    int offset = (page - 1) * itemsPerPage;
    String query = getDisplayQuery() + 
                  " WHERE name LIKE ? AND status = ? " +
                  "LIMIT ? OFFSET ?";
    PreparedStatement statement = connection.prepareStatement(query);
    String likePattern = "%" + keyword + "%";
    statement.setString(1, likePattern);
    statement.setString(2, status);
    statement.setInt(3, itemsPerPage);
    statement.setInt(4, offset);
    ResultSet resultSet = statement.executeQuery();
    List<Combo> combos = new ArrayList<>();
    while (resultSet.next()) {
      combos.add(mapResultSetToEntity(resultSet));
    }
    return combos;
  }

  public int getTotalCombosByKeywordAndStatus(String keyword, String status) throws SQLException {
    String query = "SELECT COUNT(*) FROM " + getTableName() + 
                  " WHERE name LIKE ? AND status = ?";
    PreparedStatement statement = connection.prepareStatement(query);
    String likePattern = "%" + keyword + "%";
    statement.setString(1, likePattern);
    statement.setString(2, status);
    ResultSet resultSet = statement.executeQuery();
    if (resultSet.next()) {
      return resultSet.getInt(1);
    }
    return 0;
  }
}
