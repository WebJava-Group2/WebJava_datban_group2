package org.datban.webjava.repositories.base;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.datban.webjava.models.base.IBaseModel;

public abstract class BaseRepository<T extends IBaseModel, ID> implements IBaseRepository<T, ID> {
  protected Connection connection;

  public BaseRepository(Connection connection) {
    this.connection = connection;
  }

  @Override
  public List<T> getAll() throws SQLException {
    List<T> resultList = new ArrayList<>();
    String query = getDisplayQuery();
    Statement statement = connection.createStatement();
    ResultSet resultSet = statement.executeQuery(query);

    while (resultSet.next()) {
      T entity = mapResultSetToEntity(resultSet);
      resultList.add(entity);
    }
    return resultList;
  }

  @Override
  public T getById(ID id) throws SQLException {
    String query = getDisplayQuery() + " WHERE id = ?";
    PreparedStatement statement = connection.prepareStatement(query);
    statement.setObject(1, id);
    ResultSet resultSet = statement.executeQuery();
    if (resultSet.next()) {
      return mapResultSetToEntity(resultSet);
    }
    return null;
  }

  @Override
  public List<T> getWithPaginate(int page, int size) throws SQLException {
    List<T> resultList = new ArrayList<>();
    String query = getDisplayQuery() + " LIMIT ? OFFSET ?";
    PreparedStatement statement = connection.prepareStatement(query);
    statement.setInt(1, size);
    statement.setInt(2, (page - 1) * size);
    ResultSet resultSet = statement.executeQuery();

    while (resultSet.next()) {
      T entity = mapResultSetToEntity(resultSet);
      resultList.add(entity);
    }
    return resultList;
  }

  @Override
  public void insert(T entity) throws SQLException {
    String query = getInsertQuery();
    PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
    setEntityParameters(statement, entity);
    int affectedRows = statement.executeUpdate();

    if (affectedRows > 0) {
      try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
        if (generatedKeys.next()) {
          // Giả định ID là Integer, điều chỉnh nếu là loại khác. Và giả định IBaseModel có phương thức setId.
          if (entity instanceof org.datban.webjava.models.base.IBaseModel) {
            ((org.datban.webjava.models.base.IBaseModel) entity).setId(generatedKeys.getInt(1));
          }
        }
      }
    }
  }

  @Override
  public void update(T entity) throws SQLException {
    ID id = (ID) entity.getId();
    String query = getUpdateQuery(id);
    System.out.println("DEBUG: Update query in BaseRepository.update: " + query);
    PreparedStatement statement = connection.prepareStatement(query);
    setEntityParameters(statement, entity);
    statement.setObject(countPlaceholders(query), id);
    statement.executeUpdate();
  }

  @Override
  public void delete(ID id) throws SQLException {
    String query = "DELETE FROM " + getTableName() + " WHERE id = ?";
    PreparedStatement statement = connection.prepareStatement(query);
    statement.setObject(1, id);
    statement.executeUpdate();
  }

  @Override
  public int count() throws SQLException {
    String query = "SELECT COUNT(id) FROM " + getTableName();
    Statement statement = connection.createStatement();
    ResultSet resultSet = statement.executeQuery(query);
    if (resultSet.next()) {
      return resultSet.getInt(1);
    }
    return 0;
  }

  protected abstract String getDisplayQuery();

  protected abstract T mapResultSetToEntity(ResultSet resultSet) throws SQLException;

  protected abstract String getInsertQuery();

  protected abstract String getUpdateQuery(ID id);

  protected abstract void setEntityParameters(PreparedStatement statement, T entity) throws SQLException;

  protected abstract String getTableName();

  private int countPlaceholders(String query) {
    int count = 0;
    for (char c : query.toCharArray()) {
      if (c == '?') {
        count++;
      }
    }
    return count;
  }
}
