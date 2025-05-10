package org.datban.webjava.services;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import org.datban.webjava.models.Combo;
import org.datban.webjava.repositories.ComboRepository;
import org.datban.webjava.helpers.DatabaseConnector;

public class ComboService {
    private ComboRepository comboRepository;

    public ComboService() {
        try {
            Connection connection = DatabaseConnector.getConnection();
            this.comboRepository = new ComboRepository(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Combo> getAllCombos() throws SQLException {
        return comboRepository.getAll();
    }
}
