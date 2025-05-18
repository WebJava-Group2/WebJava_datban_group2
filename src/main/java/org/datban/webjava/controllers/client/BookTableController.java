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
            request.setCharacterEncoding("UTF-8");

            // Lấy thông tin từ form
            String name = request.getParameter("name");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            String date = request.getParameter("date");
            String time = request.getParameter("time");
            int numberOfPeople = Integer.parseInt(request.getParameter("people"));
            String orderDetails = request.getParameter("orderDetails");
            String orderType = request.getParameter("orderType");

            // Validate dữ liệu
            if (name == null || name.trim().isEmpty() ||
                email == null || email.trim().isEmpty() ||
                phone == null || phone.trim().isEmpty() ||
                date == null || date.trim().isEmpty() ||
                time == null || time.trim().isEmpty() ||
                orderDetails == null || orderDetails.trim().isEmpty()) {
                
                request.setAttribute("message", "Vui lòng điền đầy đủ thông tin!");
                request.setAttribute("status", "error");
                request.getRequestDispatcher("/WEB-INF/views/client/index.jsp").forward(request, response);
                return;
            }

            // Gọi service để xử lý đặt bàn
            boolean success = reservationService.createReservation(
                name, email, phone, date, time, numberOfPeople, orderDetails, orderType
            );

            // Trả về kết quả
            if (success) {
                request.setAttribute("status", "success");
                request.setAttribute("message", "Đặt bàn thành công!");
            } else {
                request.setAttribute("status", "error");
                request.setAttribute("message", "Có lỗi xảy ra khi đặt bàn. Vui lòng thử lại!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("status", "error");
            request.setAttribute("message", e.getMessage());
        }
        request.getRequestDispatcher("/WEB-INF/views/client/index.jsp").forward(request, response);
    }
} 