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

  public List<Table> getTablesByPage(int page, int itemsPerPage) throws SQLException {
    return tableRepository.getTablesByPage(page, itemsPerPage);
  }

  public int getTotalTables() throws SQLException {
    return tableRepository.getTotalTables();
  }

  public Table getTableById(int id) throws SQLException {
    return tableRepository.getById(id);
  }

  public void createTable(Table table) throws SQLException {
    tableRepository.insert(table);
  }

  public void updateTable(Table table) throws SQLException {
    tableRepository.update(table);
  }

  public void deleteTable(int id) throws SQLException {
    tableRepository.delete(id);
  }

  public List<Table> getTablesByStatus(String status) throws SQLException {
    return tableRepository.getTablesByStatus(status);
  }

  public List<Table> getTablesByPageAndStatus(int page, int itemsPerPage, String status) throws SQLException {
    return tableRepository.getTablesByPageAndStatus(page, itemsPerPage, status);
  }

  public int getTableCountByStatus(String status) throws SQLException {
    return tableRepository.getTableCountByStatus(status);
  }

  public List<Table> findByKeyword(String keyword, int page, int itemsPerPage) throws SQLException {
    return tableRepository.findByKeyword(keyword, page, itemsPerPage);
  }

  public int getTotalTablesByKeyword(String keyword) throws SQLException {
    return tableRepository.getTotalTablesByKeyword(keyword);
  }

  public List<Table> findByKeywordAndStatus(String keyword, String status, int page, int itemsPerPage) throws SQLException {
    return tableRepository.findByKeywordAndStatus(keyword, status, page, itemsPerPage);
  }

  public int getTotalTablesByKeywordAndStatus(String keyword, String status) throws SQLException {
    return tableRepository.getTotalTablesByKeywordAndStatus(keyword, status);
  }
}
