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
        return "SELECT id, name, capacity, status, location " +
               "FROM tables";
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
}
