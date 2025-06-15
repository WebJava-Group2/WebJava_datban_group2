package org.datban.webjava.repositories;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.datban.webjava.models.Combo;
import org.datban.webjava.models.ComboFood;
import org.datban.webjava.models.Food;
import org.datban.webjava.repositories.base.BaseRepository;

public class ComboRepository extends BaseRepository<Combo, Integer> {

  public ComboRepository(Connection connection) {
    super(connection);
  }

  @Override
  protected String getDisplayQuery() {
    return "SELECT id, name, price, description, status, image_url " +
        "FROM combos";
  }

  @Override
  protected Combo mapResultSetToEntity(ResultSet resultSet) throws SQLException {
    Combo combo = new Combo();
    combo.setId(resultSet.getInt("id"));
    combo.setName(resultSet.getString("name"));
    combo.setPrice(resultSet.getFloat("price"));
    combo.setDescription(resultSet.getString("description"));
    combo.setStatus(resultSet.getString("status"));
    combo.setImageUrl(resultSet.getString("image_url"));
    return combo;
  }

  @Override
  protected String getInsertQuery() {
    return "INSERT INTO combos (name, price, description, status, image_url) VALUES (?, ?, ?, ?, ?)";
  }

  @Override
  protected String getUpdateQuery(Integer id) {
    return "UPDATE combos SET name = ?, price = ?, description = ?, status = ?, image_url = ? WHERE id = " + id;
  }

  @Override
  protected void setEntityParameters(PreparedStatement statement, Combo entity) throws SQLException {
    statement.setString(1, entity.getName());
    statement.setFloat(2, entity.getPrice());
    statement.setString(3, entity.getDescription());
    statement.setString(4, entity.getStatus());
    statement.setString(5, entity.getImageUrl());
  }

  @Override
  protected String getTableName() {
    return "combos";
  }

  public List<Combo> getCombosByPage(int page, int itemsPerPage) throws SQLException {
    return getWithPaginate(page, itemsPerPage);
  }

  public int getTotalCombos() throws SQLException {
    return count();
  }

  public List<Combo> getCombosByPageAndStatus(int page, int itemsPerPage, String status) throws SQLException {
    int offset = (page - 1) * itemsPerPage;
    String query = this.getDisplayQuery() + " WHERE status = ? LIMIT ? OFFSET ?";
    PreparedStatement statement = connection.prepareStatement(query);
    statement.setString(1, status);
    statement.setInt(2, itemsPerPage);
    statement.setInt(3, offset);
    ResultSet resultSet = statement.executeQuery();
    List<Combo> combos = new ArrayList<>();
    while (resultSet.next()) {
      combos.add(mapResultSetToEntity(resultSet));
    }
    return combos;
  }

  public int getComboCountByStatus(String status) throws SQLException {
    String query = "SELECT COUNT(*) FROM " + getTableName() + " WHERE status = ?";
    PreparedStatement statement = connection.prepareStatement(query);
    statement.setString(1, status);
    ResultSet resultSet = statement.executeQuery();
    if (resultSet.next()) {
      return resultSet.getInt(1);
    }
    return 0;
  }

  public List<Combo> findByKeyword(String keyword, int page, int itemsPerPage) throws SQLException {
    int offset = (page - 1) * itemsPerPage;
    String query = getDisplayQuery() +
        " WHERE name LIKE ? " +
        "LIMIT ? OFFSET ?";
    PreparedStatement statement = connection.prepareStatement(query);
    String likePattern = "%" + keyword + "%";
    statement.setString(1, likePattern);
    statement.setInt(2, itemsPerPage);
    statement.setInt(3, offset);
    ResultSet resultSet = statement.executeQuery();
    List<Combo> combos = new ArrayList<>();
    while (resultSet.next()) {
      combos.add(mapResultSetToEntity(resultSet));
    }
    return combos;
  }

  public int getTotalCombosByKeyword(String keyword) throws SQLException {
    String query = "SELECT COUNT(*) FROM " + getTableName() +
        " WHERE name LIKE ?";
    PreparedStatement statement = connection.prepareStatement(query);
    String likePattern = "%" + keyword + "%";
    statement.setString(1, likePattern);
    ResultSet resultSet = statement.executeQuery();
    if (resultSet.next()) {
      return resultSet.getInt(1);
    }
    return 0;
  }

  public List<Combo> findByKeywordAndStatus(String keyword, String status, int page, int itemsPerPage) throws SQLException {
    int offset = (page - 1) * itemsPerPage;
    String query = getDisplayQuery() +
        " WHERE name LIKE ? AND status = ? " +
        "LIMIT ? OFFSET ?";
    PreparedStatement statement = connection.prepareStatement(query);
    String likePattern = "%" + keyword + "%";
    statement.setString(1, likePattern);
    statement.setString(2, status);
    statement.setInt(3, itemsPerPage);
    statement.setInt(4, offset);
    ResultSet resultSet = statement.executeQuery();
    List<Combo> combos = new ArrayList<>();
    while (resultSet.next()) {
      combos.add(mapResultSetToEntity(resultSet));
    }
    return combos;
  }

  public int getTotalCombosByKeywordAndStatus(String keyword, String status) throws SQLException {
    String query = "SELECT COUNT(*) FROM " + getTableName() +
        " WHERE name LIKE ? AND status = ?";
    PreparedStatement statement = connection.prepareStatement(query);
    String likePattern = "%" + keyword + "%";
    statement.setString(1, likePattern);
    statement.setString(2, status);
    ResultSet resultSet = statement.executeQuery();
    if (resultSet.next()) {
      return resultSet.getInt(1);
    }
    return 0;
  }

  public List<Combo> getAvailableCombos() throws SQLException {
    String query = getDisplayQuery() + " WHERE status = 'available' ORDER BY price ASC";
    PreparedStatement statement = connection.prepareStatement(query);
    ResultSet resultSet = statement.executeQuery();

    List<Combo> combos = new ArrayList<>();
    while (resultSet.next()) {
      combos.add(mapResultSetToEntity(resultSet));
    }
    return combos;
  }

  public List<Food> getFoodsByComboId(int comboId, int offset, int itemsPerPage, String foodKeyword, String foodStatus, String foodMealType) throws SQLException {
    StringBuilder query = new StringBuilder(
        "SELECT f.id, f.name, f.image_url, f.price, f.status, f.meal_type, " +
            "COALESCE(SUM(cf.quantity), 0) AS quantity, " +
            "f.price * COALESCE(SUM(cf.quantity), 0) AS total_price, " +
            "CASE WHEN MAX(cf.combo_id) IS NOT NULL THEN 1 ELSE 0 END AS is_main_food " +
            "FROM foods f " +
            "LEFT JOIN combo_food cf ON f.id = cf.food_id AND cf.combo_id = ? "
    );

    List<Object> params = new ArrayList<>();
    params.add(comboId);

    List<String> conditions = new ArrayList<>();
    
    if (foodKeyword != null && !foodKeyword.trim().isEmpty()) {
        conditions.add("f.name LIKE ?");
        params.add("%" + foodKeyword + "%");
    }
    
    if (foodStatus != null && !foodStatus.trim().isEmpty() && !foodStatus.equals("all")) {
        conditions.add("f.status = ?");
        params.add(foodStatus);
    }
    
    if (foodMealType != null && !foodMealType.trim().isEmpty() && !foodMealType.equals("all")) {
        conditions.add("f.meal_type = ?");
        params.add(foodMealType);
    }

    if (!conditions.isEmpty()) {
        query.append(" WHERE ").append(String.join(" AND ", conditions));
    }

    query.append(" GROUP BY f.id, f.name, f.image_url, f.price, f.status, f.meal_type ")
         .append(" ORDER BY is_main_food DESC, f.id ASC ")
         .append(" LIMIT ? OFFSET ?");

    params.add(itemsPerPage);
    params.add(offset);

    PreparedStatement statement = connection.prepareStatement(query.toString());
    for (int i = 0; i < params.size(); i++) {
        statement.setObject(i + 1, params.get(i));
    }

    ResultSet resultSet = statement.executeQuery();
    List<Food> foods = new ArrayList<>();
    while (resultSet.next()) {
        Food food = new Food();
        food.setId(resultSet.getInt("id"));
        food.setName(resultSet.getString("name"));
        food.setImageUrl(resultSet.getString("image_url"));
        food.setPrice(resultSet.getFloat("price"));
        food.setStatus(resultSet.getString("status"));
        food.setMealType(resultSet.getString("meal_type"));
        food.setQuantity(resultSet.getInt("quantity"));
        food.setTotalPrice(resultSet.getFloat("total_price"));
        food.setIsMainFood(resultSet.getBoolean("is_main_food"));
        foods.add(food);
    }
    return foods;
  }

  public int getTotalFoodsByComboId(int comboId, String foodKeyword, String foodStatus, String foodMealType) throws SQLException {
    StringBuilder query = new StringBuilder(
        "SELECT COUNT(DISTINCT f.id) AS total_foods " +
        "FROM foods f " +
        "LEFT JOIN combo_food cf ON f.id = cf.food_id AND cf.combo_id = ? "
    );

    List<Object> params = new ArrayList<>();
    params.add(comboId);

    List<String> conditions = new ArrayList<>();
    
    if (foodKeyword != null && !foodKeyword.trim().isEmpty()) {
        conditions.add("f.name LIKE ?");
        params.add("%" + foodKeyword + "%");
    }
    
    if (foodStatus != null && !foodStatus.trim().isEmpty() && !foodStatus.equals("all")) {
        conditions.add("f.status = ?");
        params.add(foodStatus);
    }
    
    if (foodMealType != null && !foodMealType.trim().isEmpty() && !foodMealType.equals("all")) {
        conditions.add("f.meal_type = ?");
        params.add(foodMealType);
    }

    if (!conditions.isEmpty()) {
        query.append(" WHERE ").append(String.join(" AND ", conditions));
    }

    PreparedStatement statement = connection.prepareStatement(query.toString());
    for (int i = 0; i < params.size(); i++) {
        statement.setObject(i + 1, params.get(i));
    }

    ResultSet resultSet = statement.executeQuery();
    if (resultSet.next()) {
        return resultSet.getInt("total_foods");
    }
    return 0;
  }

  public List<int[]> getFoodQuantitiesByComboId(int comboId) throws SQLException {
    String query = "SELECT food_id, quantity FROM combo_food WHERE combo_id = ?";
    PreparedStatement statement = connection.prepareStatement(query);
    statement.setInt(1, comboId);
    ResultSet resultSet = statement.executeQuery();
    List<int[]> foodQuantities = new ArrayList<>();
    while (resultSet.next()) {
      foodQuantities.add(new int[] { resultSet.getInt("food_id"), resultSet.getInt("quantity") });
    }
    return foodQuantities;
  }

  public void syncFoodQuantitiesByComboId(int comboId, List<int[]> newFoodQuantities) throws SQLException {
    deleteFoodQuantityByComboId(comboId);

    String query = "INSERT INTO combo_food (combo_id, food_id, quantity) VALUES (?, ?, ?)";
    for (int[] newFoodQuantity : newFoodQuantities) {
      PreparedStatement statement = connection.prepareStatement(query);
      statement.setInt(1, comboId);
      statement.setInt(2, newFoodQuantity[0]);
      statement.setInt(3, newFoodQuantity[1]);
      statement.executeUpdate();
    }
  }

  public void deleteFoodQuantityByComboId(int comboId) throws SQLException {
    String query = "DELETE FROM combo_food WHERE combo_id = ?";
    PreparedStatement statement = connection.prepareStatement(query);
    statement.setInt(1, comboId);
    statement.executeUpdate();
  }
}
