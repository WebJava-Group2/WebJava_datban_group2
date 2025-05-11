package org.datban.webjava.controllers.admin;

import org.datban.webjava.models.Combo;
import org.datban.webjava.services.ComboService;

import java.io.*;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/admin/combos")
public class ComboController extends HttpServlet {
  private ComboService comboService;

  @Override
  public void init() throws ServletException {
    comboService = new ComboService();
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
    List<Combo> combos = null;
    try {
      combos = comboService.getAllCombos();
      request.setAttribute("combos", combos);
      request.getRequestDispatcher("/WEB-INF/views/admin/combos/list.jsp")
              .forward(request, response);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

  }
}