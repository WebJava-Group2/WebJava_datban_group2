package org.datban.webjava.controllers.admin;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.datban.webjava.models.Reservation;
import org.datban.webjava.models.User;
import org.datban.webjava.services.ReservationService;
import org.datban.webjava.services.UserService;
import org.datban.webjava.payload.ResponeData;

import java.sql.SQLException;
import java.sql.Timestamp;


import com.google.gson.Gson;

@WebServlet(name="Resservation", urlPatterns = {"/books"})
public class ReservationController extends HttpServlet{
    private UserService userService = new UserService();
    private ReservationService reservationService = new ReservationService();
    private Gson gson = new Gson();
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Reservation reservation = new Reservation();
        reservation.setTotalPeople(Integer.parseInt(request.getParameter("totalPeople")));
        reservation.setStatus("pending");
        String date = request.getParameter("date"); // dd/mm/yyyy
        String time = request.getParameter("time"); // hh:mm
        // Gộp thành Timestamp
        String fullDateTime = date + " " + time;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        java.util.Date parsedDate = null;
        try {
            parsedDate = sdf.parse(fullDateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        reservation.setReservationAt(new Timestamp(parsedDate.getTime()));
        reservation.setNote("no");
        reservation.setTotalPrice(0f);
        reservation.setCustomerId(userId);

        try {
            isSuccess=reservationService.createReservation(reservation);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ResponeData data = new ResponeData();
        data.setSusccess(isSuccess);
        data.setDescription("");
        data.setData("");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter printWriter = response.getWriter();
        String json = gson.toJson(data);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter printWriter1 = response.getWriter();
        printWriter1.print(json);
        printWriter1.flush();
    }
}
