package org.datban.webjava.controllers.admin;

import org.datban.webjava.models.Food;
import org.datban.webjava.services.FoodService;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/admin/foods/*")
public class FoodController extends HttpServlet {
  private FoodService foodService;
  private static final int ITEMS_PER_PAGE = 10;

  @Override
  public void init() throws ServletException {
    foodService = new FoodService();
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
    String pathInfo = request.getPathInfo();

    try {
      if (pathInfo == null || pathInfo.equals("/")) {
        handleListFoods(request, response);
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
        handleUpdateFood(request, response, pathInfo);
      } else {
        handleCreateFood(request, response);
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  @Override
  protected void doDelete(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
    try {
      handleDeleteFood(request, response);
      response.setStatus(HttpServletResponse.SC_OK);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  private void handleListFoods(HttpServletRequest request, HttpServletResponse response)
          throws SQLException, ServletException, IOException {
    int currentPage = getCurrentPage(request);
    int itemsPerPage = getItemsPerPage(request);
    String status = getStatus(request);
    String mealType = getMealType(request);

    int totalItems;
    List<Food> foods;

    if (status != null && mealType != null && !"all".equals(status) && !"all".equals(mealType)) {
      foods = foodService.getFoodsByStatusAndMealType(currentPage, itemsPerPage, status, mealType);
      totalItems = foodService.getFoodCountByStatusAndMealType(status, mealType);
    } else if (status != null && !"all".equals(status)) {
      foods = foodService.getFoodsByStatus(currentPage, itemsPerPage, status);
      totalItems = foodService.getFoodCountByStatus(status);
    } else if (mealType != null && !"all".equals(mealType)) {
      foods = foodService.getFoodsByMealType(currentPage, itemsPerPage, mealType);
      totalItems = foodService.getFoodCountByMealType(mealType);
    } else {
      foods = foodService.getFoods(currentPage, itemsPerPage);
      totalItems = foodService.getTotalFoods();
    }

    int totalPages = (int) Math.ceil((double) totalItems / itemsPerPage);

    setPaginationAttributes(request, currentPage, totalPages, totalItems, itemsPerPage);
    request.setAttribute("foods", foods);
    request.setAttribute("selectedMealType", mealType);
    request.setAttribute("selectedStatus", status);
    setTitle(request, "Danh sách món ăn");
    request.getRequestDispatcher("/WEB-INF/views/admin/foods/list.jsp")
            .forward(request, response);
  }

  private void handleShowAddForm(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
    setTitle(request, "Thêm món ăn");
    request.getRequestDispatcher("/WEB-INF/views/admin/foods/add.jsp")
            .forward(request, response);
  }

  private void handleShowEditForm(HttpServletRequest request, HttpServletResponse response, String pathInfo)
          throws SQLException, ServletException, IOException {
    int id = Integer.parseInt(pathInfo.split("/")[1]);
    Food food = foodService.getFoodById(id);
    request.setAttribute("food", food);
    setTitle(request, "Chỉnh sửa món ăn " + food.getName());
    request.getRequestDispatcher("/WEB-INF/views/admin/foods/edit.jsp")
            .forward(request, response);
  }

  private void handleShowDetail(HttpServletRequest request, HttpServletResponse response, String pathInfo)
          throws SQLException, ServletException, IOException {
    int id = Integer.parseInt(pathInfo.substring(1));
    Food food = foodService.getFoodById(id);
    request.setAttribute("food", food);
    setTitle(request, "Món ăn " + food.getName());
    request.getRequestDispatcher("/WEB-INF/views/admin/foods/detail.jsp")
            .forward(request, response);
  }

  private int getCurrentPage(HttpServletRequest request) {
    String pageParam = request.getParameter("page");
    if (pageParam != null && !pageParam.isEmpty()) {
      return Integer.parseInt(pageParam);
    }
    return 1;
  }

  private String getStatus(HttpServletRequest request) {
    String status = request.getParameter("status");
    if (status == null || status.isEmpty()) {
      return null;
    }
    return status;
  }

  private String getMealType(HttpServletRequest request) {
    String mealType = request.getParameter("mealType");
    if (mealType == null || mealType.isEmpty()) {
      return null;
    }
    return mealType;
  }

  private int getItemsPerPage(HttpServletRequest request) {
    String itemsPerPageParam = request.getParameter("itemsPerPage");
    if (itemsPerPageParam != null && !itemsPerPageParam.isEmpty()) {
      return Integer.parseInt(itemsPerPageParam);
    }
    return ITEMS_PER_PAGE;
  }

  private void setPaginationAttributes(HttpServletRequest request, int currentPage, int totalPages, int totalItems, int itemsPerPage) {
    request.setAttribute("currentPage", currentPage);
    request.setAttribute("totalPages", totalPages);
    request.setAttribute("totalItems", totalItems);
    request.setAttribute("itemsPerPage", itemsPerPage);
  }

  private void handleCreateFood(HttpServletRequest request, HttpServletResponse response)
          throws SQLException, ServletException, IOException {
    Food food = getFoodFromRequest(request);
    foodService.createFood(food);
    response.sendRedirect(request.getContextPath() + "/admin/foods");
  }

  private void handleUpdateFood(HttpServletRequest request, HttpServletResponse response, String pathInfo)
          throws SQLException, ServletException, IOException {
    int id = Integer.parseInt(pathInfo.split("/")[1]);
    Food food = getFoodFromRequest(request);
    food.setId(id);
    foodService.updateFood(food);
    response.sendRedirect(request.getContextPath() + "/admin/foods");
  }

  private void handleDeleteFood(HttpServletRequest request, HttpServletResponse response)
          throws SQLException, ServletException, IOException {
    String pathInfo = request.getPathInfo();
    int id = Integer.parseInt(pathInfo.split("/")[1]);
    foodService.deleteFood(id);
    response.sendRedirect(request.getContextPath() + "/admin/foods");
  }

  private Food getFoodFromRequest(HttpServletRequest request) {
    Food food = new Food();
    food.setName(request.getParameter("name"));
    food.setDescription(request.getParameter("description"));
    food.setPrice(Float.parseFloat(request.getParameter("price")));
    food.setStatus(request.getParameter("status"));
    food.setImageUrl(request.getParameter("imageUrl"));
    food.setMealType(request.getParameter("mealType"));
    return food;
  }

  private void setTitle(HttpServletRequest request, String title) {
    request.setAttribute("title", title);
  }
}

