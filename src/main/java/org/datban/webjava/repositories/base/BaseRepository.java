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
        System.out.println("runhere");
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
    public List<T> getWithPaginate(int page, int size) throws SQLException {
        List<T> resultList = new ArrayList<>();
        String query = getDisplayQuery();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, size); // size = số bản ghi trên mỗi trang
        statement.setInt(2, (page - 1) * size); // offset = (số trang - 1) * size
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
        String query = getDisplayQuery();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setObject(1, id);
        statement.executeUpdate();
    }

    protected abstract String getDisplayQuery(); // Viết các câu serlect
    protected abstract T mapResultSetToEntity(ResultSet resultSet) throws SQLException; // Chuyển đổi từ ResultSet sang Entity
    protected abstract String getInsertQuery(); // Truy vấn INSERT
    protected abstract String getUpdateQuery(); // Truy vấn UPDATE
    protected abstract void setEntityParameters(PreparedStatement statement, T entity) throws SQLException; // Cài đặt tham số cho câu truy vấn
}
