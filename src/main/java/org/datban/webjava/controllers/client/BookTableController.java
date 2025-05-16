package org.datban.webjava.controllers.client;

import org.datban.webjava.services.ReservationService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "BookTableController", urlPatterns = {"/book-table"})
public class BookTableController extends HttpServlet {
    private final ReservationService reservationService;

    public BookTableController() {
        this.reservationService = new ReservationService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Lấy thông tin từ form
            System.out.println("vao day");
            String name = request.getParameter("name");
            System.out.println(name);
            String email = request.getParameter("email");
            System.out.println(email);
            String phone = request.getParameter("phone");
            System.out.println(phone);
            String date = request.getParameter("date");
            System.out.println(date);
            String time = request.getParameter("time");
            System.out.println(time);
            int numberOfPeople = Integer.parseInt(request.getParameter("people"));
            System.out.println(numberOfPeople);
            String orderDetails = request.getParameter("message");
            System.out.println(orderDetails);

            // Validate dữ liệu
            if (name == null || name.trim().isEmpty() ||
                email == null || email.trim().isEmpty() ||
                phone == null || phone.trim().isEmpty() ||
                date == null || date.trim().isEmpty() ||
                time == null || time.trim().isEmpty() ||
                orderDetails == null || orderDetails.trim().isEmpty()) {
                
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("{\"status\":\"error\",\"message\":\"Vui lòng điền đầy đủ thông tin!\"}");
                return;
            }

            // Gọi service để xử lý đặt bàn
            boolean success = reservationService.createReservation(
                name, email, phone, date, time, numberOfPeople, orderDetails
            );

            // Trả về kết quả
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            if (success) {
                response.getWriter().write("{\"status\":\"success\",\"message\":\"Đặt bàn thành công!\"}");
            } else {
                response.getWriter().write("{\"status\":\"error\",\"message\":\"Có lỗi xảy ra khi đặt bàn. Vui lòng thử lại!\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            System.out.println("catch");
            response.getWriter().write("{\"status\":\"error\",\"message\":\"" + e.getMessage() + "\"}");
        }
    }
} 