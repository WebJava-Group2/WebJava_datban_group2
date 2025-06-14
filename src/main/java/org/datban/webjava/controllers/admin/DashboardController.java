package org.datban.webjava.controllers.admin;

import org.datban.webjava.services.ReservationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Map;
import java.sql.SQLException;

@WebServlet(name = "AdminDashboardController", urlPatterns = {
    "/admin",
    "/admin/dashboard",
    "/admin/dashboard/predict-revenue",
    "/admin/dashboard/monthly-revenue"
})
public class DashboardController extends HttpServlet {
    private ReservationService reservationService;
    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        this.reservationService = new ReservationService();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String path = request.getServletPath();
        
        if (path.equals("/admin") || path.equals("/admin/dashboard")) {
            handleDashboard(request, response);
        } else if (path.equals("/admin/dashboard/predict-revenue")) {
            handlePredictRevenue(request, response);
        } else if (path.equals("/admin/dashboard/monthly-revenue")) {
            handleMonthlyRevenue(request, response);
        }
    }

    private void handleDashboard(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/admin/dashboard.jsp")
               .forward(request, response);
    }

    private void handleMonthlyRevenue(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            Map<String, Double> monthlyRevenue = reservationService.getMonthlyRevenue();
            System.out.println("Monthly Revenue Data sent to JSP: " + monthlyRevenue); // Debug log
            
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(monthlyRevenue));
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Error fetching revenue data\"}");
        }
    }

    private void handlePredictRevenue(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            // Lấy dữ liệu doanh thu lịch sử
            Map<String, Double> historicalRevenue = reservationService.getMonthlyRevenue();
            System.out.println("Historical Revenue Data for prediction: " + historicalRevenue); // Debug log
            
            // Chuyển đổi dữ liệu thành JSON
            String inputJson = objectMapper.writeValueAsString(historicalRevenue);
            System.out.println("Input JSON to Python script: " + inputJson); // Debug log
            
            // Gọi Python script để dự báo
            ProcessBuilder pb = new ProcessBuilder("python", 
                getServletContext().getRealPath("/WEB-INF/python/predict_revenue.py"));
            pb.redirectErrorStream(true); // Redirect error stream to output stream
            Process p = pb.start();
            
            // Gửi dữ liệu vào stdin của Python script
            try (OutputStreamWriter writer = new OutputStreamWriter(p.getOutputStream())) {
                writer.write(inputJson);
                writer.flush();
            }
            
            // Đọc kết quả từ stdout của Python script
            StringBuilder output = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line);
                }
            }
            
            System.out.println("Raw output from Python script: " + output.toString()); // Debug log
            
            // Đợi Python script hoàn thành
            int exitCode = p.waitFor();
            if (exitCode != 0) {
                // Read error stream for more details
                StringBuilder errorOutput = new StringBuilder();
                try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(p.getErrorStream()))) {
                    String errorLine;
                    while ((errorLine = errorReader.readLine()) != null) {
                        errorOutput.append(errorLine).append("\n");
                    }
                }
                throw new RuntimeException("Python script failed with exit code " + exitCode + ". Error: " + errorOutput.toString());
            }
            
            // Parse kết quả và gửi về client
            Map<String, Double> predictions = objectMapper.readValue(output.toString(), Map.class);
            
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(predictions));
            
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Error predicting revenue: " + e.getMessage() + "\"}");
        }
    }
}