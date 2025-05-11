package org.datban.webjava.controllers.admin;

import org.datban.webjava.helpers.DatabaseConnector;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

// Check if the database is connected
@WebServlet(urlPatterns = {"/admin/health"})
public class HealthController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");

        try (ServletOutputStream out = response.getOutputStream()) {
            if (checkDatabaseConnection()) {
                out.print("Database connected");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print("Database not connected");
            }
            out.flush();
        }
    }

    private boolean checkDatabaseConnection() {
        try (Connection conn = DatabaseConnector.getConnection()) {
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
} 