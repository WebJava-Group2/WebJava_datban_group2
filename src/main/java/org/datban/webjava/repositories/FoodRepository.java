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
        return "select * from food";
    }

    @Override
    protected Food mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        Food food = new Food();
        food.setId(resultSet.getInt("id"));
        System.out.println(food.getName());
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
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    protected String getUpdateQuery() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    protected void setEntityParameters(PreparedStatement statement, Food entity) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    //other method
    public List<Food> getByMealType(String mealType) throws SQLException {
        String query = "SELECT * FROM food WHERE meal_type = ?";  // Truy vấn với meal_type
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
