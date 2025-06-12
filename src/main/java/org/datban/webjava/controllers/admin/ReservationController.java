package org.datban.webjava.controllers.admin;

import org.datban.webjava.services.ReservationService;
import org.datban.webjava.models.Reservation;
import org.datban.webjava.models.Table;
import org.datban.webjava.repositories.ReservationRepository;
import org.datban.webjava.repositories.TableRepository;
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

@WebServlet(name = "AdminReservationController", urlPatterns = {"/admin/reservations", "/admin/reservations/assign-table"})
public class ReservationController extends HttpServlet {
    private ReservationService reservationService;
    private ReservationRepository reservationRepository;
    private TableRepository tableRepository;

    public ReservationController() {
        this.reservationService = new ReservationService();
        try {
            Connection connection = DatabaseConnector.getConnection();
            this.reservationRepository = new ReservationRepository(connection);
            this.tableRepository = new TableRepository(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String servletPath = request.getServletPath();
        if (servletPath != null && servletPath.equals("/admin/reservations/assign-table")) {
            handleAssignTableGet(request, response);
        } else {
            handleReservationsListGet(request, response);
        }
    }

    private void handleReservationsListGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            // Get pagination parameters
            int currentPage = 1;
            int recordsPerPage = 10; // Mặc định 10 mục mỗi trang

            if (request.getParameter("page") != null) {
                currentPage = Integer.parseInt(request.getParameter("page"));
            }
            if (request.getParameter("recordsPerPage") != null) {
                recordsPerPage = Integer.parseInt(request.getParameter("recordsPerPage"));
            }

            // Get filter parameters
            String statusFilter = request.getParameter("status");
            String keyword = request.getParameter("keyword");

            // Get paginated and filtered reservations
            List<Reservation> reservations;
            int totalReservations;

            if (statusFilter != null && !statusFilter.equals("all")) {
                reservations = reservationRepository.getReservationsByPageAndStatus(currentPage, recordsPerPage, statusFilter);
                totalReservations = reservationRepository.getTotalReservationsByStatus(statusFilter);
            } else if (keyword != null && !keyword.isEmpty()) {
                reservations = reservationRepository.searchReservations(keyword, currentPage, recordsPerPage);
                totalReservations = reservationRepository.getTotalSearchResults(keyword);
            }
            else {
                reservations = reservationRepository.getReservationsByPage(currentPage, recordsPerPage);
                totalReservations = reservationRepository.getTotalReservations();
            }

            int totalPages = (int) Math.ceil(totalReservations * 1.0 / recordsPerPage);

            // Set attributes for the view
            request.setAttribute("reservations", reservations);
            request.setAttribute("currentPage", currentPage);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("itemsPerPage", recordsPerPage); // Changed from recordsPerPage to itemsPerPage for consistency with JSP
            request.setAttribute("selectedStatus", statusFilter != null ? statusFilter : "all");
            request.setAttribute("keyword", keyword);

            // Forward to the JSP view
            request.getRequestDispatcher("/WEB-INF/views/admin/reservations/reservations.jsp")
                  .forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error occurred");
        }
    }

    private void handleAssignTableGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            String reservationIdParam = request.getParameter("reservationId");
            Integer reservationId = null;
            if (reservationIdParam != null && !reservationIdParam.isEmpty()) {
                reservationId = Integer.parseInt(reservationIdParam);
                request.setAttribute("reservationId", reservationId);
            }
            
            List<Table> tables = tableRepository.getAll();
            request.setAttribute("tables", tables);

            request.getRequestDispatcher("/WEB-INF/views/admin/reservations/table-diagram.jsp")
                    .forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error occurred");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            String pathInfo = request.getPathInfo();
            if (pathInfo != null && pathInfo.equals("/assign-table")) {
                handleAssignTablePost(request, response);
            } else {
                handleReservationStatusUpdatePost(request, response);
            }
        } catch (Exception e) {
            response.getWriter().write("{\"success\": false, \"message\": \"Đã xảy ra lỗi không mong muốn trong doPost: " + e.getMessage() + "\"}");
            e.printStackTrace();
        }
    }

    private void handleReservationStatusUpdatePost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if ("update-status".equals(action)) {
            try {
                int reservationId = Integer.parseInt(request.getParameter("reservationId"));
                String newStatus = request.getParameter("status");
                
                // Update reservation status
                Reservation reservation = reservationRepository.getById(reservationId);
                if (reservation != null) {
                    reservation.setStatus(newStatus);
                    reservationRepository.update(reservation);
                }
                
                response.sendRedirect(request.getContextPath() + "/admin/reservations");
            } catch (SQLException e) {
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to update reservation status");
            }
        }
    }

    private void handleAssignTablePost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            int reservationId = Integer.parseInt(request.getParameter("reservationId"));
            int tableId = Integer.parseInt(request.getParameter("tableId"));

            // Get the reservation
            Reservation reservation = reservationRepository.getById(reservationId);
            if (reservation == null) {
                response.getWriter().write("{\"success\": false, \"message\": \"Đặt bàn không tồn tại.\"}");
                return;
            }

            // Assign table to reservation
            reservation.setTableId(tableId);
            reservationRepository.update(reservation);

            // Update table status to 'reserved'
            Table table = tableRepository.getById(tableId);
            if (table != null) {
                table.setStatus("reserved");
                tableRepository.update(table);
            }

            response.getWriter().write("{\"success\": true, \"message\": \"Đặt bàn đã được thêm vào bàn thành công.\"}");

        } catch (NumberFormatException e) {
            response.getWriter().write("{\"success\": false, \"message\": \"ID đặt bàn hoặc ID bàn không hợp lệ.\"}");
            e.printStackTrace();
        } catch (SQLException e) {
            response.getWriter().write("{\"success\": false, \"message\": \"Lỗi cơ sở dữ liệu: " + e.getMessage() + "\"}");
            e.printStackTrace();
        } catch (Exception e) {
            response.getWriter().write("{\"success\": false, \"message\": \"Đã xảy ra lỗi không mong muốn: " + e.getMessage() + "\"}");
            e.printStackTrace();
        }
    }
}
