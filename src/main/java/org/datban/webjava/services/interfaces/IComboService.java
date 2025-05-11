package org.datban.webjava.services.interfaces;

import java.sql.SQLException;
import java.util.List;

import org.datban.webjava.models.Combo;

public interface IComboService {
  List<Combo> getAllCombos() throws SQLException;
  Combo getComboById(int id) throws SQLException;
  void createCombo(Combo combo) throws SQLException;
  void updateCombo(Combo combo) throws SQLException;
  void deleteCombo(int id) throws SQLException;
  List<Combo> getCombosByStatus(String status) throws SQLException;
  List<Combo> getCombosByPage(int page, int itemsPerPage) throws SQLException;
  int getTotalCombos() throws SQLException;
}
