package org.datban.webjava.controllers.client;

import org.datban.webjava.models.Food;
import org.datban.webjava.services.FoodService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "ClientHomeController", urlPatterns = {"/client/home"})
public class ClientHomeController extends HttpServlet {
    private final FoodService foodService;

    public ClientHomeController() {
        this.foodService = new FoodService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Lấy 6 món ăn cho phần gợi ý
            List<Food> suggestedFoods = foodService.getFirst6Foods();
            request.setAttribute("foods", suggestedFoods);

            // Lấy 6 món ăn cho bữa sáng
            List<Food> breakfastFoods = foodService.get6FoodsByMealType("breakfast");
            request.setAttribute("foodsbr", breakfastFoods);

            // Lấy 6 món ăn cho bữa trưa
            List<Food> lunchFoods = foodService.get6FoodsByMealType("lunch");
            request.setAttribute("foodslu", lunchFoods);

            // Lấy 6 món ăn cho bữa tối
            List<Food> dinnerFoods = foodService.get6FoodsByMealType("dinner");
            request.setAttribute("foodsdn", dinnerFoods);

            // Forward đến trang index.html trong thư mục client
            request.getRequestDispatcher("/client/index.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error occurred");
        }
    }
} 