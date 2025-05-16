package org.datban.webjava.controllers.client;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.datban.webjava.services.ReviewService;

@WebServlet("/contact")
public class ContactController extends HttpServlet {
    private ReviewService reviewService;

    public void init() {
        reviewService = new ReviewService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Thiết lập encoding cho request và response
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Lấy thông tin từ form
        String name = request.getParameter("name");
        System.out.println(name);
        String email = request.getParameter("email");
        System.out.println(email);
        String phone = request.getParameter("phone");
        System.out.println(phone);
        String content = request.getParameter("content");
        System.out.println(content);

        try {
            boolean success = reviewService.createReview(name, email, phone, content);
            
            if (success) {
                response.getWriter().write("{\"status\":\"success\",\"message\":\"Đánh giá của bạn đã được gửi thành công!\"}");
            } else {
                response.getWriter().write("{\"status\":\"error\",\"message\":\"Có lỗi xảy ra khi gửi đánh giá. Vui lòng thử lại!\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("{\"status\":\"error\",\"message\":\"" + e.getMessage() + "\"}");
        }
    }
} 