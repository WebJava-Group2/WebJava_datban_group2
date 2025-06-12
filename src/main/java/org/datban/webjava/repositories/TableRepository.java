package org.datban.webjava.repositories;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.datban.webjava.models.Table;
import org.datban.webjava.repositories.base.BaseRepository;

public class TableRepository extends BaseRepository<Table, Integer> {

  public TableRepository(Connection connection) {
    super(connection);
  }

  @Override
  protected String getDisplayQuery() {
    return "SELECT t.id, t.name, t.capacity, t.status, t.location FROM tables t";
  }

  @Override
  protected Table mapResultSetToEntity(ResultSet resultSet) throws SQLException {
    Table table = new Table();
    table.setId(resultSet.getInt("id"));
    table.setName(resultSet.getString("name"));
    table.setCapacity(resultSet.getInt("capacity"));
    table.setStatus(resultSet.getString("status"));
    table.setLocation(resultSet.getString("location"));
    return table;
  }

  @Override
  protected String getInsertQuery() {
    return "INSERT INTO tables (name, capacity, status, location) VALUES (?, ?, ?, ?)";
  }

  @Override
  protected String getUpdateQuery(Integer id) {
    return "UPDATE tables SET name = ?, capacity = ?, status = ?, location = ? WHERE id = " + id;
  }

  @Override
  protected void setEntityParameters(PreparedStatement statement, Table entity) throws SQLException {
    statement.setString(1, entity.getName());
    statement.setInt(2, entity.getCapacity());
    statement.setString(3, entity.getStatus());
    statement.setString(4, entity.getLocation());
  }

  @Override
  protected String getTableName() {
    return "tables";
  }

  @Override
  public List<Table> getAll() throws SQLException {
    return super.getAll();
  }

  @Override
  public Table getById(Integer id) throws SQLException {
    return super.getById(id);
  }

  public List<Table> getTablesByStatus(String status) throws SQLException {
    String query = getDisplayQuery() + " WHERE status = ?";
    PreparedStatement statement = connection.prepareStatement(query);
    statement.setString(1, status);
    ResultSet resultSet = statement.executeQuery();

    List<Table> tables = new ArrayList<>();
    while (resultSet.next()) {
      tables.add(mapResultSetToEntity(resultSet));
    }
    return tables;
  }

  public List<Table> getTablesByPage(int page, int itemsPerPage) throws SQLException {
    return getWithPaginate(page, itemsPerPage);
  }

  public int getTotalTables() throws SQLException {
    String query = "SELECT COUNT(*) FROM " + getTableName();
    Statement statement = connection.createStatement();
    ResultSet resultSet = statement.executeQuery(query);

    if (resultSet.next()) {
      return resultSet.getInt(1);
    }
    return 0;
  }

  public List<Table> getTablesByPageAndStatus(int page, int itemsPerPage, String status) throws SQLException {
    int offset = (page - 1) * itemsPerPage;
    String query = getDisplayQuery() + " WHERE status = ? LIMIT ? OFFSET ?";
    PreparedStatement statement = connection.prepareStatement(query);
    statement.setString(1, status);
    statement.setInt(2, itemsPerPage);
    statement.setInt(3, offset);
    ResultSet resultSet = statement.executeQuery();

    List<Table> tables = new ArrayList<>();
    while (resultSet.next()) {
      tables.add(mapResultSetToEntity(resultSet));
    }
    return tables;
  }

  public int getTableCountByStatus(String status) throws SQLException {
    String query = "SELECT COUNT(*) FROM " + getTableName() + " WHERE status = ?";
    PreparedStatement statement = connection.prepareStatement(query);
    statement.setString(1, status);
    ResultSet resultSet = statement.executeQuery();
    if (resultSet.next()) {
      return resultSet.getInt(1);
    }
    return 0;
  }

  public List<Table> findByKeyword(String keyword, int page, int itemsPerPage) throws SQLException {
    int offset = (page - 1) * itemsPerPage;
    String query = getDisplayQuery() +
            " WHERE name LIKE ? OR location LIKE ? " +
            "LIMIT ? OFFSET ?";
    PreparedStatement statement = connection.prepareStatement(query);
    String likePattern = "%" + keyword + "%";
    statement.setString(1, likePattern);
    statement.setString(2, likePattern);
    statement.setInt(3, itemsPerPage);
    statement.setInt(4, offset);
    ResultSet resultSet = statement.executeQuery();
    List<Table> tables = new ArrayList<>();
    while (resultSet.next()) {
      tables.add(mapResultSetToEntity(resultSet));
    }
    return tables;
  }

  public int getTotalTablesByKeyword(String keyword) throws SQLException {
    String query = "SELECT COUNT(*) FROM " + getTableName() +
            " WHERE name LIKE ? OR location LIKE ?";
    PreparedStatement statement = connection.prepareStatement(query);
    String likePattern = "%" + keyword + "%";
    statement.setString(1, likePattern);
    statement.setString(2, likePattern);
    ResultSet resultSet = statement.executeQuery();
    if (resultSet.next()) {
      return resultSet.getInt(1);
    }
    return 0;
  }

  public List<Table> findByKeywordAndStatus(String keyword, String status, int page, int itemsPerPage) throws SQLException {
    int offset = (page - 1) * itemsPerPage;
    String query = getDisplayQuery() +
            " WHERE (name LIKE ? OR location LIKE ?) AND status = ? " +
            "LIMIT ? OFFSET ?";
    PreparedStatement statement = connection.prepareStatement(query);
    String likePattern = "%" + keyword + "%";
    statement.setString(1, likePattern);
    statement.setString(2, likePattern);
    statement.setString(3, status);
    statement.setInt(4, itemsPerPage);
    statement.setInt(5, offset);
    ResultSet resultSet = statement.executeQuery();
    List<Table> tables = new ArrayList<>();
    while (resultSet.next()) {
      tables.add(mapResultSetToEntity(resultSet));
    }
    return tables;
  }

  public int getTotalTablesByKeywordAndStatus(String keyword, String status) throws SQLException {
    String query = "SELECT COUNT(*) FROM " + getTableName() +
            " WHERE (name LIKE ? OR location LIKE ?) AND status = ?";
    PreparedStatement statement = connection.prepareStatement(query);
    String likePattern = "%" + keyword + "%";
    statement.setString(1, likePattern);
    statement.setString(2, likePattern);
    statement.setString(3, status);
    ResultSet resultSet = statement.executeQuery();
    if (resultSet.next()) {
      return resultSet.getInt(1);
    }
    return 0;
  }
}
