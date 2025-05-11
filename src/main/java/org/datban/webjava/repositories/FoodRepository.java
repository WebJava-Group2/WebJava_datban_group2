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
        return "SELECT id, name, price, description, image_url, category_id, status " +
               "FROM foods";
    }

    @Override
    protected Food mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        Food food = new Food();
        food.setId(resultSet.getInt("id"));
        food.setName(resultSet.getString("name"));
        food.setPrice(resultSet.getFloat("price"));
        food.setDescription(resultSet.getString("description"));
        food.setImageUrl(resultSet.getString("image_url"));
        food.setStatus(resultSet.getString("status"));
        return food;
    }
    @Override
    protected String getInsertQuery() {
        return "INSERT INTO foods (name, price, description, image_url, category_id, status) VALUES (?, ?, ?, ?, ?, ?)";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE foods SET name = ?, price = ?, description = ?, image_url = ?, category_id = ?, status = ? WHERE id = ?";
    }

    @Override
    protected void setEntityParameters(PreparedStatement statement, Food entity) throws SQLException {
        statement.setString(1, entity.getName());
        statement.setFloat(2, entity.getPrice());
        statement.setString(3, entity.getDescription());
        statement.setString(4, entity.getImageUrl());
        statement.setString(5, entity.getStatus());
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
        String query = "SELECT COUNT(*) FROM " + getTableName();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        
        if (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return 0;
    }

}
