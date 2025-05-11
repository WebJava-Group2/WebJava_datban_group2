package org.datban.webjava.controllers.admin;

import org.datban.webjava.models.Combo;
import org.datban.webjava.services.ComboService;

import java.io.*;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/admin/combos/*")
public class ComboController extends HttpServlet {
  private ComboService comboService;
  private static final int ITEMS_PER_PAGE = 10;

  @Override
  public void init() throws ServletException {
    comboService = new ComboService();
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
    String pathInfo = request.getPathInfo();

    try {
      if (pathInfo == null || pathInfo.equals("/")) {
        handleListCombos(request, response);
      } else if (pathInfo.equals("/add")) {
        handleShowAddForm(request, response);
      } else if (pathInfo.matches("/\\d+/edit")) {
        handleShowEditForm(request, response, pathInfo);
      } else if (pathInfo.matches("/\\d+")) {
        handleShowDetail(request, response, pathInfo);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private void handleListCombos(HttpServletRequest request, HttpServletResponse response) 
          throws SQLException, ServletException, IOException {
    int currentPage = getCurrentPage(request);
    int totalItems = comboService.getTotalCombos();
    int totalPages = (int) Math.ceil((double) totalItems / ITEMS_PER_PAGE);
    List<Combo> combos = comboService.getCombosByPage(currentPage, ITEMS_PER_PAGE);

    setPaginationAttributes(request, currentPage, totalPages, totalItems);
    request.setAttribute("combos", combos);
    request.getRequestDispatcher("/WEB-INF/views/admin/combos/list.jsp")
            .forward(request, response);
  }

  private void handleShowAddForm(HttpServletRequest request, HttpServletResponse response) 
          throws ServletException, IOException {
    request.getRequestDispatcher("/WEB-INF/views/admin/combos/add.jsp")
            .forward(request, response);
  }

  private void handleShowEditForm(HttpServletRequest request, HttpServletResponse response, String pathInfo) 
          throws SQLException, ServletException, IOException {
    int id = Integer.parseInt(pathInfo.split("/")[1]);
    Combo combo = comboService.getComboById(id);
    request.setAttribute("combo", combo);
    request.getRequestDispatcher("/WEB-INF/views/admin/combos/edit.jsp")
            .forward(request, response);
  }

  private void handleShowDetail(HttpServletRequest request, HttpServletResponse response, String pathInfo) 
          throws SQLException, ServletException, IOException {
    int id = Integer.parseInt(pathInfo.substring(1));
    Combo combo = comboService.getComboById(id);
    request.setAttribute("combo", combo);
    request.getRequestDispatcher("/WEB-INF/views/admin/combos/detail.jsp")
            .forward(request, response);
  }

  private int getCurrentPage(HttpServletRequest request) {
    String pageParam = request.getParameter("page");
    if (pageParam != null && !pageParam.isEmpty()) {
      return Integer.parseInt(pageParam);
    }
    return 1;
  }

  private void setPaginationAttributes(HttpServletRequest request, int currentPage, int totalPages, int totalItems) {
    request.setAttribute("currentPage", currentPage);
    request.setAttribute("totalPages", totalPages);
    request.setAttribute("totalItems", totalItems);
    request.setAttribute("itemsPerPage", ITEMS_PER_PAGE);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
    try {
      String name = request.getParameter("name");
      String description = request.getParameter("description");
      float price = Float.parseFloat(request.getParameter("price"));
      
      Combo combo = new Combo();
      combo.setName(name);
      combo.setDescription(description);
      combo.setPrice(price);
      combo.setStatus("available");
      comboService.createCombo(combo);
      response.sendRedirect(request.getContextPath() + "/admin/combos");
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  protected void doPut(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
    try {
      String pathInfo = request.getPathInfo();
      int id = Integer.parseInt(pathInfo.substring(1));
      
      String name = request.getParameter("name");
      String description = request.getParameter("description");
      float price = Float.parseFloat(request.getParameter("price"));
      String status = request.getParameter("status");
      
      Combo combo = new Combo();
      combo.setId(id);
      combo.setName(name);
      combo.setDescription(description);
      combo.setPrice(price);
      combo.setStatus(status);
      
      comboService.updateCombo(combo);
      response.sendRedirect(request.getContextPath() + "/admin/combos");
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  protected void doDelete(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
    try {
      String pathInfo = request.getPathInfo();
      int id = Integer.parseInt(pathInfo.substring(1));
      
      comboService.deleteCombo(id);
      response.setStatus(HttpServletResponse.SC_OK);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}