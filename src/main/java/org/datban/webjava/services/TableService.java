package org.datban.webjava.services;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import org.datban.webjava.models.Table;
import org.datban.webjava.repositories.TableRepository;
import org.datban.webjava.helpers.DatabaseConnector;

public class TableService {
    private TableRepository tableRepository;

    public TableService() {
        try {
            Connection connection = DatabaseConnector.getConnection();
            this.tableRepository = new TableRepository(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Table> getAllTables() throws SQLException {
        return tableRepository.getAll();
    }
}
