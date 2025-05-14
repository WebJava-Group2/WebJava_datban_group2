package org.datban.webjava.controllers.admin;
import org.datban.webjava.services.UserService;

import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.sql.SQLException;

@WebServlet(name="userServlet", urlPatterns = {"/admin/login"})
public class UserController extends HttpServlet {

    private UserService userService;

    @Override
    public void init() throws ServletException {
        // Khởi tạo UserService
        userService = new UserService();
    }

    // Xử lý đăng nhập (POST)
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Lấy thông tin từ form đăng nhập
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try {
            // Gọi phương thức authentication từ UserService để xác thực người dùng
            boolean isAuthenticated = userService.authentication(email, password);

            if (isAuthenticated) {
                // Nếu đăng nhập thành công, lưu thông tin người dùng vào session
                HttpSession session = request.getSession();
                session.setAttribute("userEmail", email);  // Lưu email của người dùng vào session
                response.sendRedirect("home.jsp");  // Chuyển hướng đến trang home.jsp
            } else {
                // Nếu đăng nhập thất bại, trả về trang login.jsp và hiển thị thông báo lỗi
                request.setAttribute("errorMessage", "Sai email hoặc mật khẩu.");
                RequestDispatcher dispatcher = request.getRequestDispatcher("/login.jsp");
                dispatcher.forward(request, response);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi khi xác thực người dùng");
        }
    }

    // Xử lý GET (có thể dùng để hiển thị trang đăng nhập)
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/admin/login.html");
        dispatcher.forward(request, response);
    }
}
