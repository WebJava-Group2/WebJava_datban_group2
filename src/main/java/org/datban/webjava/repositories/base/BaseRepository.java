package org.datban.webjava.repositories.base;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseRepository<T, ID> implements IBaseRepository<T, ID> {
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
        String query = "SELECT * FROM " + getTableName() + " WHERE id = ?";
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
        PreparedStatement statement = connection.prepareStatement(query);
        setEntityParameters(statement, entity);
        statement.executeUpdate();
    }

    @Override
    public void update(T entity) throws SQLException {
        String query = getUpdateQuery();
        PreparedStatement statement = connection.prepareStatement(query);
        setEntityParameters(statement, entity);
        statement.executeUpdate();
    }

    @Override
    public void delete(ID id) throws SQLException {
        String query = "DELETE FROM " + getTableName() + " WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setObject(1, id);
        statement.executeUpdate();
    }

    protected abstract String getDisplayQuery();
    protected abstract T mapResultSetToEntity(ResultSet resultSet) throws SQLException;
    protected abstract String getInsertQuery();
    protected abstract String getUpdateQuery();
    protected abstract void setEntityParameters(PreparedStatement statement, T entity) throws SQLException;
    protected abstract String getTableName();
}
