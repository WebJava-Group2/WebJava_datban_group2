package org.datban.webjava.controllers.admin;

import org.datban.webjava.services.ReservationService;
import org.datban.webjava.services.ReservationTableService;
import org.datban.webjava.models.Reservation;
import org.datban.webjava.models.Table;
import org.datban.webjava.models.ReservationTable;
import org.datban.webjava.repositories.ReservationRepository;
import org.datban.webjava.repositories.TableRepository;
import org.datban.webjava.repositories.ReservationTableRepository;
import org.datban.webjava.helpers.DatabaseConnector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "AdminReservationController", urlPatterns = {
    "/admin/reservations",
    "/admin/reservations/show-table-diagram",
    "/admin/reservations/assign-table",
    "/admin/reservations/delete"
})
public class ReservationController extends HttpServlet {
    private ReservationService reservationService;
    private ReservationRepository reservationRepository;
    private TableRepository tableRepository;
    private ReservationTableRepository reservationTableRepository;
    private ReservationTableService reservationTableService;

    @Override
    public void init() throws ServletException {
        this.reservationService = new ReservationService();
        try {
            Connection connection = DatabaseConnector.getConnection();
            this.reservationRepository = new ReservationRepository(connection);
            this.tableRepository = new TableRepository(connection);
            this.reservationTableRepository = new ReservationTableRepository(connection);
            this.reservationTableService = new ReservationTableService(
                reservationTableRepository,
                reservationRepository,
                tableRepository
            );
        } catch (SQLException e) {
            throw new ServletException("Error initializing repositories", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String path = request.getServletPath();
        
        if (path.equals("/admin/reservations")) {
            handleListReservations(request, response);
        } else if (path.equals("/admin/reservations/show-table-diagram")) {
            handleShowTableDiagram(request, response);
        } else if (path.equals("/admin/reservations/delete")) {
            handleDeleteReservation(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String path = request.getServletPath();
        
        if (path.equals("/admin/reservations/assign-table")) {
            handleAssignTablePost(request, response);
        }
    }

    private void handleListReservations(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            List<Reservation> reservations = reservationService.getAllReservations();
            request.setAttribute("reservations", reservations);
            request.getRequestDispatcher("/WEB-INF/views/admin/reservations/reservations.jsp")
                   .forward(request, response);
        } catch (SQLException e) {
            request.setAttribute("error", "Lỗi khi lấy danh sách đặt bàn: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/admin/reservations/reservations.jsp")
                   .forward(request, response);
        }
    }

    private void handleShowTableDiagram(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            int reservationId = Integer.parseInt(request.getParameter("reservationId"));
            
            // Kiểm tra xem đặt bàn có tồn tại không
            Reservation reservation = reservationRepository.getById(reservationId);
            if (reservation == null) {
                request.getSession().setAttribute("error", "Không tìm thấy đặt bàn");
                response.sendRedirect(request.getContextPath() + "/admin/reservations");
                return;
            }

            // Kiểm tra xem đặt bàn đã được gán cho bàn nào chưa
            List<ReservationTable> existingAssignments = reservationTableRepository.getByReservationId(reservationId);
            if (!existingAssignments.isEmpty()) {
                request.getSession().setAttribute("error", "Đặt bàn đã được gán cho một bàn khác");
                response.sendRedirect(request.getContextPath() + "/admin/reservations");
                return;
            }

            // Lấy danh sách bàn có sẵn
            List<Table> tables = tableRepository.getAll();
            request.setAttribute("tables", tables);
            request.setAttribute("reservationId", reservationId);
            request.getRequestDispatcher("/WEB-INF/views/admin/reservations/table-diagram.jsp")
                   .forward(request, response);
        } catch (SQLException e) {
            request.getSession().setAttribute("error", "Lỗi khi hiển thị sơ đồ bàn: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/admin/reservations");
        }
    }

    private void handleAssignTablePost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            int reservationId = Integer.parseInt(request.getParameter("reservationId"));
            int tableId = Integer.parseInt(request.getParameter("tableId"));

            // Gọi service để gán bàn cho đặt bàn
            reservationTableService.assignTableToReservation(reservationId, tableId);

            // Thêm message thành công
            request.getSession().setAttribute("message", "Gán bàn thành công cho đặt bàn #" + reservationId);
            
            // Chuyển hướng về trang danh sách đặt bàn
            response.sendRedirect(request.getContextPath() + "/admin/reservations");
        } catch (SQLException e) {
            // Thêm message lỗi
            request.getSession().setAttribute("error", "Lỗi: " + e.getMessage());
            // Chuyển hướng về trang danh sách đặt bàn
            response.sendRedirect(request.getContextPath() + "/admin/reservations");
        }
    }

    private void handleDeleteReservation(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            int reservationId = Integer.parseInt(request.getParameter("reservationId"));
            
            // Gọi service để xóa đặt bàn và cập nhật trạng thái bàn
            reservationTableService.removeTableFromReservation(reservationId);

            request.getSession().setAttribute("message", "Xóa đặt bàn #" + reservationId + " thành công.");
            response.sendRedirect(request.getContextPath() + "/admin/reservations");
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("error", "ID đặt bàn không hợp lệ.");
            response.sendRedirect(request.getContextPath() + "/admin/reservations");
        } catch (SQLException e) {
            request.getSession().setAttribute("error", "Lỗi khi xóa đặt bàn: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/admin/reservations");
        } catch (Exception e) {
            request.getSession().setAttribute("error", "Đã xảy ra lỗi không mong muốn: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/admin/reservations");
        }
    }
}
