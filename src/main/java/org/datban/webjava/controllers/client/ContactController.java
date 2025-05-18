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
        request.setCharacterEncoding("UTF-8");
        
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String content = request.getParameter("content");

        try {
            boolean success = reviewService.createReview(name, email, phone, content);
            
            if (success) {
                request.setAttribute("message", "Đánh giá của bạn đã được gửi thành công!");
                request.setAttribute("status", "success");
            } else {
                request.setAttribute("message", "Có lỗi xảy ra khi gửi đánh giá. Vui lòng thử lại!");
                request.setAttribute("status", "error");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("message", e.getMessage());
            request.setAttribute("status", "error");
        }
        
        // Forward về trang index.jsp
        request.getRequestDispatcher("/WEB-INF/views/client/index.jsp").forward(request, response);
    }
} 