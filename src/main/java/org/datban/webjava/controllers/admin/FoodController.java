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
      if (pathInfo != null) {
        if (pathInfo.matches("/\\d+/status")) {
          handleUpdateFoodStatus(request, response, pathInfo);
        } else if (pathInfo.matches("/\\d+")) {
          handleUpdateFood(request, response, pathInfo);
        } else {
          handleCreateFood(request, response);
        }
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
    String keyword = getKeyword(request);

    int totalItems;
    List<Food> foods;

    boolean hasKeyword = keyword != null && !keyword.isEmpty();
    boolean hasStatus = status != null && !status.equals("all");
    boolean hasMealType = mealType != null && !mealType.equals("all");

    // Lấy tổng số items trước
    if (hasKeyword && hasStatus && hasMealType) {
      totalItems = foodService.getTotalFoodsByKeywordAndStatusAndMealType(keyword, status, mealType);
    } else if (hasKeyword && hasStatus) {
      totalItems = foodService.getTotalFoodsByKeywordAndStatus(keyword, status);
    } else if (hasKeyword && hasMealType) {
      totalItems = foodService.getTotalFoodsByKeywordAndMealType(keyword, mealType);
    } else if (hasStatus && hasMealType) {
      totalItems = foodService.getFoodCountByStatusAndMealType(status, mealType);
    } else if (hasKeyword) {
      totalItems = foodService.getTotalFoodsByKeyword(keyword);
    } else if (hasStatus) {
      totalItems = foodService.getFoodCountByStatus(status);
    } else if (hasMealType) {
      totalItems = foodService.getFoodCountByMealType(mealType);
    } else {
      totalItems = foodService.getTotalFoods();
    }

    // Tính toán số trang và điều chỉnh trang hiện tại nếu cần
    int totalPages = Math.max(1, (int) Math.ceil((double) totalItems / itemsPerPage));
    if (currentPage > totalPages) {
      currentPage = totalPages;
    }

    // Lấy danh sách foods sau khi đã điều chỉnh trang
    if (hasKeyword && hasStatus && hasMealType) {
      foods = foodService.findByKeywordAndStatusAndMealType(keyword, status, mealType, currentPage, itemsPerPage);
    } else if (hasKeyword && hasStatus) {
      foods = foodService.findByKeywordAndStatus(keyword, status, currentPage, itemsPerPage);
    } else if (hasKeyword && hasMealType) {
      foods = foodService.findByKeywordAndMealType(keyword, mealType, currentPage, itemsPerPage);
    } else if (hasStatus && hasMealType) {
      foods = foodService.getFoodsByStatusAndMealType(currentPage, itemsPerPage, status, mealType);
    } else if (hasKeyword) {
      foods = foodService.findByKeyword(keyword, currentPage, itemsPerPage);
    } else if (hasStatus) {
      foods = foodService.getFoodsByStatus(currentPage, itemsPerPage, status);
    } else if (hasMealType) {
      foods = foodService.getFoodsByMealType(currentPage, itemsPerPage, mealType);
    } else {
      foods = foodService.getFoods(currentPage, itemsPerPage);
    }

    // Lấy message và error từ session
    HttpSession session = request.getSession();
    String message = (String) session.getAttribute("message");
    String error = (String) session.getAttribute("error");
    
    // Xóa message và error khỏi session sau khi đã lấy
    session.removeAttribute("message");
    session.removeAttribute("error");

    setPaginationAttributes(request, currentPage, totalPages, totalItems, itemsPerPage);
    request.setAttribute("foods", foods);
    request.setAttribute("selectedMealType", mealType);
    request.setAttribute("selectedStatus", status);
    request.setAttribute("keyword", keyword);
    request.setAttribute("message", message);
    request.setAttribute("error", error);
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

  private String getKeyword(HttpServletRequest request) {
    String keyword = request.getParameter("keyword");
    if (keyword == null || keyword.isEmpty()) {
      return null;
    }
    return keyword;
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
    HttpSession session = request.getSession();
    try {
      Food food = getFoodFromRequest(request);
      foodService.createFood(food);
      session.setAttribute("message", "Thêm món ăn thành công");
      response.sendRedirect(request.getContextPath() + "/admin/foods");
    } catch (Exception e) {
      System.out.println(e.getMessage());
      session.setAttribute("error", "Thêm món ăn thất bại");
      response.sendRedirect(request.getContextPath() + "/admin/foods/add");
    }
  }

  private void handleUpdateFood(HttpServletRequest request, HttpServletResponse response, String pathInfo)
          throws SQLException, ServletException, IOException {
    HttpSession session = request.getSession();
    int id = Integer.parseInt(pathInfo.split("/")[1]);
    try {
      // Lấy thông tin món ăn hiện tại
      Food existingFood = foodService.getFoodById(id);
      if (existingFood == null) {
        session.setAttribute("error", "Không tìm thấy món ăn");
        response.sendRedirect(request.getContextPath() + "/admin/foods");
        return;
      }

      // Lấy thông tin mới từ form
      Food updatedFood = getFoodFromRequest(request);
      updatedFood.setId(id);
      
      // Nếu meal_type không hợp lệ (null), giữ nguyên giá trị cũ
      if (updatedFood.getMealType() == null) {
        // Thêm kiểm tra an toàn cho mealType hiện có
        String existingMealType = existingFood.getMealType();
        if (existingMealType != null) {
          switch (existingMealType) {
            case "breakfast":
            case "lunch":
            case "dinner":
            case "dessert":
              updatedFood.setMealType(existingMealType);
              break;
            default:
              // Nếu giá trị cũ cũng không hợp lệ, đặt là null
              updatedFood.setMealType(null);
          }
        } else {
          updatedFood.setMealType(null); // Nếu giá trị cũ là null, giữ nguyên null
        }
      }

      foodService.updateFood(updatedFood);
      session.setAttribute("message", "Cập nhật món ăn thành công");
      response.sendRedirect(request.getContextPath() + "/admin/foods");
    } catch (Exception e) {
      System.out.println(e.getMessage());
      session.setAttribute("error", "Cập nhật món ăn thất bại: " + e.getMessage());
      response.sendRedirect(request.getContextPath() + "/admin/foods/" + id + "/edit");
    }
  }

  private void handleDeleteFood(HttpServletRequest request, HttpServletResponse response)
          throws SQLException, ServletException, IOException {
    HttpSession session = request.getSession();
    String pathInfo = request.getPathInfo();
    int id = Integer.parseInt(pathInfo.split("/")[1]);
    try {
      foodService.deleteFood(id);
      session.setAttribute("message", "Xóa món ăn thành công");
      response.sendRedirect(request.getContextPath() + "/admin/foods");
    } catch (Exception e) {
      System.out.println(e.getMessage());
      session.setAttribute("error", "Xóa món ăn thất bại");
      response.sendRedirect(request.getContextPath() + "/admin/foods");
    }
  }

  private Food getFoodFromRequest(HttpServletRequest request) {
    Food food = new Food();
    food.setName(request.getParameter("name"));
    food.setDescription(request.getParameter("description"));
    food.setPrice(Float.parseFloat(request.getParameter("price")));
    food.setStatus(request.getParameter("status"));
    food.setImageUrl(request.getParameter("imageUrl"));
    
    // Xử lý mealType
    String mealType = request.getParameter("mealType");
    if (mealType != null && !mealType.isEmpty()) {
      mealType = mealType.trim(); // Loại bỏ khoảng trắng thừa
      System.out.println("DEBUG: Received mealType after trim: " + mealType);
      
      // Chuyển đổi giá trị tiếng Việt sang tiếng Anh nếu cần
      switch (mealType) {
        case "Bữa sáng":
          food.setMealType("breakfast");
          break;
        case "Bữa trưa":
          food.setMealType("lunch");
          break;
        case "Bữa tối":
          food.setMealType("dinner");
          break;
        case "Tráng miệng":
          food.setMealType("dessert");
          break;
        case "breakfast":
        case "lunch":
        case "dinner":
        case "dessert":
          food.setMealType(mealType);
          break;
        default:
          food.setMealType(null); // Giá trị không hợp lệ
      }
    } else {
      food.setMealType(null);
    }
    return food;
  }

  private void setTitle(HttpServletRequest request, String title) {
    request.setAttribute("title", title);
  }

  private void handleUpdateFoodStatus(HttpServletRequest request, HttpServletResponse response, String pathInfo)
          throws SQLException, ServletException, IOException {
    HttpSession session = request.getSession();
    int id = Integer.parseInt(pathInfo.split("/")[1]);
    String newStatus = request.getParameter("status");
    
    try {
      Food food = foodService.getFoodById(id);
      if (food != null) {
        food.setStatus(newStatus);
        foodService.updateFood(food);
        session.setAttribute("message", "Cập nhật trạng thái món ăn thành công");
      } else {
        session.setAttribute("error", "Không tìm thấy món ăn");
      }
      response.setStatus(HttpServletResponse.SC_OK);
    } catch (Exception e) {
      System.out.println(e.getMessage());
      session.setAttribute("error", "Cập nhật trạng thái món ăn thất bại");
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }
}

