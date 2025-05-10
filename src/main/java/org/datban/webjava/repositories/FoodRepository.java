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
        return "SELECT id, name, description, price, image_url, status, meal_type " +
               "FROM foods";
    }

    @Override
    protected Food mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        Food food = new Food();
        food.setId(resultSet.getInt("id"));
        food.setName(resultSet.getString("name"));
        food.setDescription(resultSet.getString("description"));
        food.setPrice(resultSet.getFloat("price"));
        food.setImageUrl(resultSet.getString("image_url"));
        food.setStatus(resultSet.getString("status"));
        food.setMealType(resultSet.getString("meal_type"));
        return food;
    }
    @Override
    protected String getInsertQuery() {
        return "INSERT INTO foods (name, description, price, image_url, status, meal_type) VALUES (?, ?, ?, ?, ?, ?)";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE foods SET name = ?, description = ?, price = ?, image_url = ?, status = ?, meal_type = ? WHERE id = ?";
    }

    @Override
    protected void setEntityParameters(PreparedStatement statement, Food entity) throws SQLException {
        statement.setString(1, entity.getName());
        statement.setString(2, entity.getDescription());
        statement.setFloat(3, entity.getPrice());
        statement.setString(4, entity.getImageUrl());
        statement.setString(5, entity.getStatus());
        statement.setString(6, entity.getMealType());
    }

    //other method
    public List<Food> getByMealType(String mealType) throws SQLException {
        String query = this.getDisplayQuery() + " WHERE meal_type = ?";  // Truy vấn với meal_type
        List<Food> food = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, mealType);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            food.add(mapResultSetToEntity(resultSet));
        }
        return food;
    }

}
