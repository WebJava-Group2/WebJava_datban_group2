package org.datban.webjava.controllers.admin;

import org.datban.webjava.models.Combo;
import org.datban.webjava.models.Food;
import org.datban.webjava.services.ComboService;

import java.io.*;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.sql.SQLException;
import java.util.ArrayList;
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
      System.out.println(e.getMessage());
    }
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    try {
      String pathInfo = request.getPathInfo();
      if (pathInfo != null && pathInfo.matches("/\\d+")) {
        handleUpdateCombo(request, response, pathInfo);
      } else {
        handleCreateCombo(request, response);
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  @Override
  protected void doDelete(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    try {
      handleDeleteCombo(request, response);
      response.setStatus(HttpServletResponse.SC_OK);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  private void handleListCombos(HttpServletRequest request, HttpServletResponse response)
      throws SQLException, ServletException, IOException {
    int currentPage = getIntParam(request, "page", 1);
    int itemsPerPage = getIntParam(request, "itemsPerPage", ITEMS_PER_PAGE);
    String status = getStringParam(request, "status");
    String keyword = getStringParam(request, "keyword");

    int totalItems;
    List<Combo> combos;

    boolean hasKeyword = keyword != null && !keyword.isEmpty();
    boolean hasStatus = status != null && !status.equals("all");

    // Lấy tổng số items trước
    if (hasKeyword && hasStatus) {
      totalItems = comboService.getTotalCombosByKeywordAndStatus(keyword, status);
    } else if (hasKeyword) {
      totalItems = comboService.getTotalCombosByKeyword(keyword);
    } else if (hasStatus) {
      totalItems = comboService.getComboCountByStatus(status);
    } else {
      totalItems = comboService.getTotalCombos();
    }

    // Tính toán số trang và điều chỉnh trang hiện tại nếu cần
    int totalPages = Math.max(1, (int) Math.ceil((double) totalItems / itemsPerPage));
    if (currentPage > totalPages) {
      currentPage = totalPages;
    }

    // Lấy danh sách combos sau khi đã điều chỉnh trang
    if (hasKeyword && hasStatus) {
      combos = comboService.findByKeywordAndStatus(keyword, status, currentPage, itemsPerPage);
    } else if (hasKeyword) {
      combos = comboService.findByKeyword(keyword, currentPage, itemsPerPage);
    } else if (hasStatus) {
      combos = comboService.getCombosByPageAndStatus(currentPage, itemsPerPage, status);
    } else {
      combos = comboService.getCombosByPage(currentPage, itemsPerPage);
    }

    // Lấy message và error từ session
    HttpSession session = request.getSession();
    String message = (String) session.getAttribute("message");
    String error = (String) session.getAttribute("error");

    // Xóa message và error khỏi session sau khi đã lấy
    session.removeAttribute("message");
    session.removeAttribute("error");

    setPaginationAttributes(request, currentPage, totalPages, totalItems, itemsPerPage);
    request.setAttribute("combos", combos);
    request.setAttribute("selectedStatus", status);
    request.setAttribute("keyword", keyword);
    request.setAttribute("message", message);
    request.setAttribute("error", error);
    setTitle(request, "Danh sách combo");
    request.getRequestDispatcher("/WEB-INF/views/admin/combos/list.jsp")
        .forward(request, response);
  }

  private void handleShowAddForm(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    setTitle(request, "Thêm combo");
    request.getRequestDispatcher("/WEB-INF/views/admin/combos/add.jsp")
        .forward(request, response);
  }

  private void handleShowEditForm(HttpServletRequest request, HttpServletResponse response, String pathInfo)
      throws SQLException, ServletException, IOException {
    // Lấy message và error từ session
    HttpSession session = request.getSession();
    String message = (String) session.getAttribute("message");
    String error = (String) session.getAttribute("error");
    session.removeAttribute("message");
    session.removeAttribute("error");

    // thong tin query params
    int id = Integer.parseInt(pathInfo.split("/")[1]);
    int currentFoodPage = getIntParam(request, "foodPage", 1);
    int foodItemsPerPage = getIntParam(request, "foodItemsPerPage", ITEMS_PER_PAGE);
    String foodKeyword = getStringParam(request, "foodKeyword");
    String foodStatus = getStringParam(request, "foodStatus");
    String foodMealType = getStringParam(request, "foodMealType");

    // call service
    Combo combo = comboService.getComboById(id);
    int totalFoods = comboService.getTotalFoodsByComboId(id, foodKeyword, foodStatus, foodMealType);
    int totalFoodPages = Math.max(1, (int) Math.ceil((double) totalFoods / foodItemsPerPage));
    if (currentFoodPage > totalFoodPages) {
      currentFoodPage = totalFoodPages;
    }
    List<Food> foods = comboService.getFoodsByComboId(id, currentFoodPage, foodItemsPerPage, foodKeyword, foodStatus, foodMealType);

    // thong tin combo
    request.setAttribute("combo", combo);
    // thong tin food
    request.setAttribute("foods", foods);
    request.setAttribute("totalFoods", totalFoods);
    request.setAttribute("totalFoodPages", totalFoodPages);
    request.setAttribute("currentFoodPage", currentFoodPage);
    request.setAttribute("foodItemsPerPage", foodItemsPerPage);
    request.setAttribute("foodKeyword", foodKeyword);
    request.setAttribute("foodStatus", foodStatus);
    request.setAttribute("foodMealType", foodMealType);
    request.setAttribute("url", request.getContextPath() + "/admin/combos/" + id + "/edit");
    // message và error
    request.setAttribute("message", message);
    request.setAttribute("error", error);
    setTitle(request, "Chỉnh sửa combo " + combo.getName());
    request.getRequestDispatcher("/WEB-INF/views/admin/combos/edit.jsp")
        .forward(request, response);
  }

  private void handleShowDetail(HttpServletRequest request, HttpServletResponse response, String pathInfo)
      throws SQLException, ServletException, IOException {
    int id = Integer.parseInt(pathInfo.substring(1));
    Combo combo = comboService.getComboById(id);
    request.setAttribute("combo", combo);
    setTitle(request, "Combo " + combo.getName());
    request.getRequestDispatcher("/WEB-INF/views/admin/combos/detail.jsp")
        .forward(request, response);
  }

  private String getStringParam(HttpServletRequest request, String param) {
    String value = request.getParameter(param);
    if (value == null || value.isEmpty()) {
      return null;
    }
    return value;
  }

  private int getIntParam(HttpServletRequest request, String param, int defaultValue) {
    String value = request.getParameter(param);
    if (value != null && !value.isEmpty()) {
      return Integer.parseInt(value);
    }
    return defaultValue;
  }

  private void setPaginationAttributes(HttpServletRequest request, int currentPage, int totalPages, int totalItems, int itemsPerPage) {
    request.setAttribute("currentPage", currentPage);
    request.setAttribute("totalPages", totalPages);
    request.setAttribute("totalItems", totalItems);
    request.setAttribute("itemsPerPage", itemsPerPage);
  }

  private void handleCreateCombo(HttpServletRequest request, HttpServletResponse response)
      throws SQLException, ServletException, IOException {
    HttpSession session = request.getSession();
    try {
      Combo combo = getComboFromRequest(request);
      comboService.createCombo(combo);
      session.setAttribute("message", "Thêm combo thành công");
      response.sendRedirect(request.getContextPath() + "/admin/combos");
    } catch (Exception e) {
      System.out.println(e.getMessage());
      session.setAttribute("error", "Thêm combo thất bại");
      response.sendRedirect(request.getContextPath() + "/admin/combos/add");
    }
  }

/**
 * ?foodQuantities=1-2,3-4
 * => [1, 2], [3, 4]
 * 1: foodId
 * 2: quantity của foodId 1
 * 3: foodId
 * 4: quantity của foodId 3
 */
  private void handleUpdateCombo(HttpServletRequest request, HttpServletResponse response, String pathInfo)
      throws SQLException, ServletException, IOException {
    HttpSession session = request.getSession();
    int id = Integer.parseInt(pathInfo.split("/")[1]);
    try {
      Combo combo = getComboFromRequest(request);
      String foodQuantitiesQuery = request.getParameter("foodQuantities");
      comboService.updateFoodQuantitiesByComboId(id, foodQuantitiesQuery);
      combo.setId(id);
      comboService.updateCombo(combo);
      session.setAttribute("message", "Cập nhật combo thành công");
      response.sendRedirect(request.getContextPath() + "/admin/combos/" + id + "/edit");
    } catch (Exception e) {
      System.out.println(e.getMessage());
      session.setAttribute("error", "Cập nhật combo thất bại");
      response.sendRedirect(request.getContextPath() + "/admin/combos/" + id + "/edit");
    }
  }

  private void handleDeleteCombo(HttpServletRequest request, HttpServletResponse response)
      throws SQLException, ServletException, IOException {
    HttpSession session = request.getSession();
    String pathInfo = request.getPathInfo();
    int id = Integer.parseInt(pathInfo.split("/")[1]);
    try {
      comboService.deleteCombo(id);
      session.setAttribute("message", "Xóa combo thành công");
      response.sendRedirect(request.getContextPath() + "/admin/combos");
    } catch (Exception e) {
      System.out.println(e.getMessage());
      session.setAttribute("error", "Xóa combo thất bại");
      response.sendRedirect(request.getContextPath() + "/admin/combos");
    }
  }

  private Combo getComboFromRequest(HttpServletRequest request) {
    Combo combo = new Combo();
    combo.setName(request.getParameter("name"));
    combo.setDescription(request.getParameter("description"));
    combo.setPrice(Float.parseFloat(request.getParameter("price")));
    combo.setStatus(request.getParameter("status"));
    combo.setImageUrl(request.getParameter("imageUrl"));

    return combo;
  }

  private void setTitle(HttpServletRequest request, String title) {
    request.setAttribute("title", title);
  }
}