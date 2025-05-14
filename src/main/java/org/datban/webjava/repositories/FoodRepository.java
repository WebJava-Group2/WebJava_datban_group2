package org.datban.webjava.repositories;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.datban.webjava.models.Food;
import org.datban.webjava.repositories.base.BaseRepository;


public class FoodRepository extends BaseRepository<Food, Integer> {

    public FoodRepository(Connection connection) {
        super(connection);
    }

    @Override
    protected String getDisplayQuery() {
        return "SELECT id, name, price, description, status, image_url, meal_type " +
               "FROM foods";
    }

    @Override
    protected Food mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        Food food = new Food();
        food.setId(resultSet.getInt("id"));
        food.setName(resultSet.getString("name"));
        food.setPrice(resultSet.getFloat("price"));
        food.setDescription(resultSet.getString("description"));
        food.setStatus(resultSet.getString("status"));
        food.setImageUrl(resultSet.getString("image_url"));
        food.setMealType(resultSet.getString("meal_type"));
        return food;
    }
    @Override
    protected String getInsertQuery() {
        return "INSERT INTO foods (name, price, description, status, image_url, meal_type) VALUES (?, ?, ?, ?, ?, ?)";
    }

    @Override
    protected String getUpdateQuery(Integer id) {
        return "UPDATE foods SET name = ?, price = ?, description = ?, status = ?, image_url = ?, meal_type = ? WHERE id = " + id;
    }

    @Override
    protected void setEntityParameters(PreparedStatement statement, Food entity) throws SQLException {
        statement.setString(1, entity.getName());
        statement.setFloat(2, entity.getPrice());
        statement.setString(3, entity.getDescription());
        statement.setString(4, entity.getStatus());
        statement.setString(5, entity.getImageUrl());
        statement.setString(6, entity.getMealType());
    }

    @Override
    protected String getTableName() {
        return "foods";
    }

    //other method
    public List<Food> getByMealType(String mealType) throws SQLException {
        String query = getDisplayQuery() + " WHERE meal_type = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, mealType);
        ResultSet resultSet = statement.executeQuery();
        
        List<Food> foods = new ArrayList<>();
        while (resultSet.next()) {
            foods.add(mapResultSetToEntity(resultSet));
        }
        return foods;
    }

    public List<Food> getFoodsByPage(int page, int itemsPerPage) throws SQLException {
        return getWithPaginate(page, itemsPerPage);
    }

    public int getTotalFoods() throws SQLException {
        return count();
    }

    public List<Food> getFoodsByPageAndStatus(int page, int itemsPerPage, String status) throws SQLException {
        int offset = (page - 1) * itemsPerPage;
        String query = getDisplayQuery() + " WHERE status = ? LIMIT ? OFFSET ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, status);
        statement.setInt(2, itemsPerPage);
        statement.setInt(3, offset);
        ResultSet resultSet = statement.executeQuery();
        
        List<Food> foods = new ArrayList<>();
        while (resultSet.next()) {
            foods.add(mapResultSetToEntity(resultSet));
        }
        return foods;
    }

    public int getFoodCountByStatus(String status) throws SQLException {
        String query = "SELECT COUNT(*) as total FROM " + getTableName() + " WHERE status = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, status);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt("total");
        }
        return 0;
    }

    public List<Food> getFoodsByPageAndMealType(int page, int itemsPerPage, String mealType) throws SQLException {
        int offset = (page - 1) * itemsPerPage;
        String query = getDisplayQuery() + " WHERE meal_type = ? LIMIT ? OFFSET ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, mealType);
        statement.setInt(2, itemsPerPage);
        statement.setInt(3, offset);
        ResultSet resultSet = statement.executeQuery();
        
        List<Food> foods = new ArrayList<>();
        while (resultSet.next()) {
            foods.add(mapResultSetToEntity(resultSet));
        }
        return foods;
    }

    public int getFoodCountByMealType(String mealType) throws SQLException {
        String query = "SELECT COUNT(*) as total FROM " + getTableName() + " WHERE meal_type = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, mealType);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt("total");
        }
        return 0;
    }

    public List<Food> getFoodsByPageAndStatusAndMealType(int page, int itemsPerPage, String status, String mealType) throws SQLException {
        int offset = (page - 1) * itemsPerPage;
        String query = getDisplayQuery() + " WHERE status = ? AND meal_type = ? LIMIT ? OFFSET ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, status);
        statement.setString(2, mealType);
        statement.setInt(3, itemsPerPage);
        statement.setInt(4, offset);
        ResultSet resultSet = statement.executeQuery();
        
        List<Food> foods = new ArrayList<>();
        while (resultSet.next()) {
            foods.add(mapResultSetToEntity(resultSet));
        }
        return foods;
    }

    public int getFoodCountByStatusAndMealType(String status, String mealType) throws SQLException {
        String query = "SELECT COUNT(*) as total FROM " + getTableName() + " WHERE status = ? AND meal_type = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, status);
        statement.setString(2, mealType);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt("total");
        }
        return 0;
    }
}
