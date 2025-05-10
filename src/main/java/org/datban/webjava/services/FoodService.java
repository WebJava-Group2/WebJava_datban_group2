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

    // Lấy tất cả món ăn
    public List<Food> getAllFoods() throws SQLException {
        return foodRepository.getAll();  // Gọi getAll() từ FoodRepository
    }

    // Lấy món ăn sáng
    public List<Food> getDessert() throws SQLException {
        return foodRepository.getByMealType("dessert");
    }

    public List<Food> getBreakfast() throws SQLException {
        return foodRepository.getByMealType("breakfast");
    }

    // Lấy món ăn trưa
    public List<Food> getLunch() throws SQLException {
        return foodRepository.getByMealType("lunch");
    }

    // Lấy món ăn tối
    public List<Food> getDinner() throws SQLException {
        return foodRepository.getByMealType("dinner");
    }
}
