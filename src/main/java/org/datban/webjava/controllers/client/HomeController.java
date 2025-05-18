package org.datban.webjava.controllers.client;

import org.datban.webjava.models.Food;
import org.datban.webjava.services.FoodService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.datban.webjava.models.Combo;
import org.datban.webjava.services.ComboService;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


@WebServlet("/home")
public class HomeController extends HttpServlet {
    private final FoodService foodService;
    private ComboService comboService;

    public HomeController() {
        this.foodService = new FoodService();
        this.comboService = new ComboService();
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

            List<Combo> combos = comboService.getAvailableCombos();
            request.setAttribute("combos", combos);


            // Forward đến trang index.html trong thư mục client
            request.getRequestDispatcher("/WEB-INF/views/client/index.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error occurred");
        }
    }
}
