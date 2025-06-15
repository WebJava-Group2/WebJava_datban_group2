package org.datban.webjava.services;

import org.datban.webjava.models.Combo;
import org.datban.webjava.models.ComboFood;
import org.datban.webjava.models.Food;
import org.datban.webjava.repositories.ComboRepository;
import org.datban.webjava.helpers.DatabaseConnector;
import org.datban.webjava.services.interfaces.IComboService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ComboService implements IComboService {
  private ComboRepository comboRepository;

  private Integer countOffset(int page, int itemsPerPage) {
    return (page - 1) * itemsPerPage;
  }

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

  public List<Combo> getCombosByPageAndStatus(int page, int itemsPerPage, String status) throws SQLException {
    return comboRepository.getCombosByPageAndStatus(page, itemsPerPage, status);
  }

  public int getComboCountByStatus(String status) throws SQLException {
    return comboRepository.getComboCountByStatus(status);
  }

  public List<Combo> findByKeyword(String keyword, int page, int itemsPerPage) throws SQLException {
    return comboRepository.findByKeyword(keyword, page, itemsPerPage);
  }

  public int getTotalCombosByKeyword(String keyword) throws SQLException {
    return comboRepository.getTotalCombosByKeyword(keyword);
  }

  public List<Combo> findByKeywordAndStatus(String keyword, String status, int page, int itemsPerPage) throws SQLException {
    return comboRepository.findByKeywordAndStatus(keyword, status, page, itemsPerPage);
  }

  public int getTotalCombosByKeywordAndStatus(String keyword, String status) throws SQLException {
    return comboRepository.getTotalCombosByKeywordAndStatus(keyword, status);
  }

  public List<Combo> getAvailableCombos() throws SQLException {
    return comboRepository.getAvailableCombos();
  }

  public List<Food> getFoodsByComboId(int comboId, int page, int itemsPerPage, String foodKeyword, String foodStatus, String foodMealType) throws SQLException {
    return comboRepository.getFoodsByComboId(comboId, countOffset(page, itemsPerPage), itemsPerPage, foodKeyword, foodStatus, foodMealType);
  }

  public int getTotalFoodsByComboId(int comboId, String foodKeyword, String foodStatus, String foodMealType) throws SQLException {
    return comboRepository.getTotalFoodsByComboId(comboId, foodKeyword, foodStatus, foodMealType);
  }

  public void updateFoodQuantitiesByComboId(int comboId, String foodQuantitiesQuery) throws SQLException {
    List<int[]> newFoodQuantities = getFoodQuantitiesFromRequest(foodQuantitiesQuery);
    comboRepository.syncFoodQuantitiesByComboId(comboId, newFoodQuantities);
  }

  private List<int[]> getFoodQuantitiesFromRequest(String foodQuantitiesQuery) {
    List<int[]> foodQuantities = new ArrayList<>();
    for (String foodQuantityString : foodQuantitiesQuery.split(",")) {
      String[] parts = foodQuantityString.split("-");
      foodQuantities.add(new int[]{Integer.parseInt(parts[0]), Integer.parseInt(parts[1])});
    }
    return foodQuantities;
  }
}
