package org.datban.webjava.controllers;

import java.io.*;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.datban.webjava.services.FoodService;

@WebServlet(name="homeServlet", urlPatterns = {"/foods"})
public class FoodController extends HttpServlet{
    private final FoodService foodService = new FoodService();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setAttribute("foods", foodService.getAllFoods());
            req.setAttribute("foodsbr", foodService.getBreakfast());
            req.setAttribute("foodslu", foodService.getLunch());
            req.setAttribute("foodsdn", foodService.getDinner());
            System.out.println("ok");
            req.getRequestDispatcher("/index.jsp").forward(req, resp);
        } catch (SQLException ex) {
            Logger.getLogger(FoodController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

