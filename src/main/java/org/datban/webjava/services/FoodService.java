package org.datban.webjava.services;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.datban.webjava.models.Food;
import org.datban.webjava.repositories.FoodRepository;
import org.datban.webjava.helpers.DatabaseConnector;

public class FoodService {
  private FoodRepository foodRepository;

  public FoodService() {
    try {
      Connection connection = DatabaseConnector.getConnection();
      this.foodRepository = new FoodRepository(connection);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public List<Food> getAllFoods() throws SQLException {
    return foodRepository.getAll();
  }

  public List<Food> getFoods(int page, int itemsPerPage) throws SQLException {
    return foodRepository.getFoodsByPage(page, itemsPerPage);
  }

  public int getTotalFoods() throws SQLException {
    return foodRepository.getTotalFoods();
  }

  public Food getFoodById(int id) throws SQLException {
    return foodRepository.getById(id);
  }

  public void createFood(Food food) throws SQLException {
    foodRepository.insert(food);
  }

  public void updateFood(Food food) throws SQLException {
    foodRepository.update(food);
  }

  public void deleteFood(int id) throws SQLException {
    foodRepository.delete(id);
  }

  public List<Food> getByMealType(String mealType) throws SQLException {
    return foodRepository.getByMealType(mealType);
  }

  public List<Food> getBreakfast() throws SQLException {
    return getByMealType("breakfast");
  }

  public List<Food> getLunch() throws SQLException {
    return getByMealType("lunch");
  }

  public List<Food> getDinner() throws SQLException {
    return getByMealType("dinner");
  }

  public List<Food> getDessert() throws SQLException {
    return getByMealType("dessert");
  }

  // filter by status
  public List<Food> getFoodsByStatus(int page, int itemsPerPage, String status) throws SQLException {
    return foodRepository.getFoodsByPageAndStatus(page, itemsPerPage, status);
  }

  public int getFoodCountByStatus(String status) throws SQLException {
    return foodRepository.getFoodCountByStatus(status);
  }

  // filter by meal type and status
  public List<Food> getFoodsByStatusAndMealType(int page, int itemsPerPage, String status, String mealType) throws SQLException {
    return foodRepository.getFoodsByPageAndStatusAndMealType(page, itemsPerPage, status, mealType);
  }

  public int getFoodCountByStatusAndMealType(String status, String mealType) throws SQLException {
    return foodRepository.getFoodCountByStatusAndMealType(status, mealType);
  }

  public List<Food> getFoodsByMealType(int page, int itemsPerPage, String mealType) throws SQLException {
    return foodRepository.getFoodsByPageAndMealType(page, itemsPerPage, mealType);
  }

  public int getFoodCountByMealType(String mealType) throws SQLException {
    return foodRepository.getFoodCountByMealType(mealType);
  }

  // Lấy 6 món ăn đầu tiên cho phần gợi ý
  public List<Food> getFirst6Foods() throws SQLException {
    return foodRepository.getFirst6Foods();
  }

  // Lấy 6 món ăn theo loại bữa
  public List<Food> get6FoodsByMealType(String mealType) throws SQLException {
    return foodRepository.get6FoodsByMealType(mealType);
  }
}
