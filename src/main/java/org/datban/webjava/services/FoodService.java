package org.datban.webjava.services;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import org.datban.webjava.models.Food;
import org.datban.webjava.repositories.FoodRepository;

public class FoodService {
    private FoodRepository foodRepository;

//    public FoodService(Connection connection) {
//        this.foodRepository = new FoodRepository(connection);
//    }

    // Lấy tất cả món ăn
    public List<Food> getAllFoods() throws SQLException {
        return foodRepository.getAll();  // Gọi getAll() từ FoodRepository
    }

    // Lấy món ăn sáng
    public List<Food> getBreakfast() throws SQLException {
        return foodRepository.getByMealType("sáng");
    }

    // Lấy món ăn trưa
    public List<Food> getLunch() throws SQLException {
        return foodRepository.getByMealType("trưa");
    }

    // Lấy món ăn tối
    public List<Food> getDinner() throws SQLException {
        return foodRepository.getByMealType("tối");
    }
}
