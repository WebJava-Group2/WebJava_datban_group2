package org.datban.webjava.services;

import org.datban.webjava.models.Combo;
import org.datban.webjava.repositories.ComboRepository;
import org.datban.webjava.helpers.DatabaseConnector;
import org.datban.webjava.services.interfaces.IComboService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ComboService implements IComboService {
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

    public List<Combo> getCombosByPage(int page, int itemsPerPage) throws SQLException {
        return comboRepository.getCombosByPage(page, itemsPerPage);
    }

    public int getTotalCombos() throws SQLException {
        return comboRepository.getTotalCombos();
    }

    public Combo getComboById(int id) throws SQLException {
        return comboRepository.getById(id);
    }

    public void createCombo(Combo combo) throws SQLException {
        comboRepository.insert(combo);
    }

    public void updateCombo(Combo combo) throws SQLException {
        comboRepository.update(combo);
    }

    public void deleteCombo(int id) throws SQLException {
        comboRepository.delete(id);
    }

    public List<Combo> getCombosByStatus(String status) throws SQLException {
        return comboRepository.getCombosByStatus(status);
    }
}
