package org.datban.webjava.controllers.admin;

import org.datban.webjava.helpers.HashedHelper;
import org.datban.webjava.models.User;
import org.datban.webjava.services.UserService;

import java.io.*;
import java.sql.Timestamp;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

@WebServlet("/admin/users/*")
public class UserController extends HttpServlet {
  private UserService userService;
  private HashedHelper hashedHelper;
  private static final int ITEMS_PER_PAGE = 10;

  @Override
  public void init() throws ServletException {
    userService = new UserService();
    hashedHelper = new HashedHelper();
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
    String pathInfo = request.getPathInfo();

    try {
      if (pathInfo == null || pathInfo.equals("/")) {
        handleListUsers(request, response);
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
        handleUpdateUser(request, response, pathInfo);
      } else {
        handleCreateUser(request, response);
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  @Override
  protected void doDelete(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
    try {
      handleDeleteUser(request, response);
    } catch (Exception e) {
      System.out.println(e.getMessage());
      request.setAttribute("error", "Xóa người dùng thất bại");
      request.getRequestDispatcher("/WEB-INF/views/admin/users/list.jsp")
              .forward(request, response);
    }
  }

  private void handleListUsers(HttpServletRequest request, HttpServletResponse response)
          throws SQLException, ServletException, IOException {
    int currentPage = getCurrentPage(request);
    int itemsPerPage = getItemsPerPage(request);
    String role = getRole(request);

    int totalItems;
    List<User> users;

    if (role != null && !role.equals("all")) {
      totalItems = userService.getTotalUsersByRole(role);
      users = userService.getUsersByRole(currentPage, itemsPerPage, role);
    } else {
      totalItems = userService.getTotalUsers();
      users = userService.getUsers(currentPage, itemsPerPage);
    }

    int totalPages = (int) Math.ceil((double) totalItems / itemsPerPage);

    // Lấy message và error từ session
    HttpSession session = request.getSession();
    String message = (String) session.getAttribute("message");
    String error = (String) session.getAttribute("error");
    
    // Xóa message và error khỏi session sau khi đã lấy
    session.removeAttribute("message");
    session.removeAttribute("error");

    // Set attributes
    setPaginationAttributes(request, currentPage, totalPages, totalItems, itemsPerPage);
    request.setAttribute("users", users);
    request.setAttribute("selectedRole", role);
    request.setAttribute("message", message);
    request.setAttribute("error", error);
    setTitle(request, "Danh sách người dùng");
    
    request.getRequestDispatcher("/WEB-INF/views/admin/users/list.jsp")
            .forward(request, response);
  }

  private void handleShowAddForm(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
    setTitle(request, "Thêm người dùng");
    request.getRequestDispatcher("/WEB-INF/views/admin/users/add.jsp")
            .forward(request, response);
  }

  private void handleShowEditForm(HttpServletRequest request, HttpServletResponse response, String pathInfo)
          throws SQLException, ServletException, IOException {
    HttpSession session = request.getSession();
    int id = Integer.parseInt(pathInfo.split("/")[1]);
    User user = userService.getUserById(id);

    request.setAttribute("user", user);
    request.setAttribute("fieldErrors", session.getAttribute("fieldErrors"));
    request.setAttribute("error", session.getAttribute("error"));

    session.removeAttribute("error");
    session.removeAttribute("fieldErrors");

    setTitle(request, "Chỉnh sửa người dùng " + user.getName());
    request.getRequestDispatcher("/WEB-INF/views/admin/users/edit.jsp")
            .forward(request, response);
  }

  private void handleShowDetail(HttpServletRequest request, HttpServletResponse response, String pathInfo)
          throws SQLException, ServletException, IOException {
    int id = Integer.parseInt(pathInfo.substring(1));
    User user = userService.getUserById(id);
    request.setAttribute("user", user);
    setTitle(request, "Người dùng " + user.getName());
    request.getRequestDispatcher("/WEB-INF/views/admin/users/detail.jsp")
            .forward(request, response);
  }

  private int getCurrentPage(HttpServletRequest request) {
    String pageParam = request.getParameter("page");
    if (pageParam != null && !pageParam.isEmpty()) {
      return Integer.parseInt(pageParam);
    }
    return 1;
  }

  private int getItemsPerPage(HttpServletRequest request) {
    String itemsPerPageParam = request.getParameter("itemsPerPage");
    if (itemsPerPageParam != null && !itemsPerPageParam.isEmpty()) {
      return Integer.parseInt(itemsPerPageParam);
    }
    return ITEMS_PER_PAGE;
  }

  private String getRole(HttpServletRequest request) {
    String role = request.getParameter("role");
    if (role == null || role.isEmpty()) {
      return null;
    }
    return role;
  }

  private void setPaginationAttributes(HttpServletRequest request, int currentPage, int totalPages, int totalItems, int itemsPerPage) {
    request.setAttribute("currentPage", currentPage);
    request.setAttribute("totalPages", totalPages);
    request.setAttribute("totalItems", totalItems);
    request.setAttribute("itemsPerPage", itemsPerPage);
  }

  private void handleCreateUser(HttpServletRequest request, HttpServletResponse response)
          throws SQLException, ServletException, IOException {
    HttpSession session = request.getSession();
    User user = getUserFromRequest(request);
    String password = request.getParameter("password");
    String hashedPassword = hashedHelper.hashPassword(password);
    user.setPassword(hashedPassword);
    user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
    try {
      userService.createUser(user);
      session.setAttribute("message", "Thêm người dùng thành công");
      response.sendRedirect(request.getContextPath() + "/admin/users");
    } catch (Exception e) {
      System.out.println(e.getMessage());
      session.setAttribute("error", "Thêm người dùng thất bại");
      response.sendRedirect(request.getContextPath() + "/admin/users");
    }
  }

  private void handleUpdateUser(HttpServletRequest request, HttpServletResponse response, String pathInfo)
          throws SQLException, ServletException, IOException {
    HttpSession session = request.getSession();
    User user = getUserFromRequest(request);
    int id = Integer.parseInt(pathInfo.split("/")[1]);
    user.setId(id);
    if (userService.checkEmailExist(user)) {
      session.setAttribute("error", "Email đã tồn tại");
      response.sendRedirect(request.getContextPath() + "/admin/users/" + id + "/edit");
      return;
    }
    if (userService.checkPhoneExist(user)) {
      session.setAttribute("error", "Số điện thoại đã tồn tại");
      response.sendRedirect(request.getContextPath() + "/admin/users/" + id + "/edit");
      return;
    }
    String password = request.getParameter("password");
    if (password != null && !password.trim().isEmpty()) {
      String hashedPassword = hashedHelper.hashPassword(password);
      user.setPassword(hashedPassword);
    } else {
      User existingUser = userService.getUserById(id);
      user.setPassword(existingUser.getPassword());
    }
    try {
      userService.updateUser(user);
      session.setAttribute("message", "Cập nhật người dùng thành công");
      response.sendRedirect(request.getContextPath() + "/admin/users");
    } catch (Exception e) {
      System.out.println(e.getMessage());
      session.setAttribute("error", "Cập nhật người dùng thất bại");
      response.sendRedirect(request.getContextPath() + "/admin/users/" + id + "/edit");
    }
  }

  private void handleDeleteUser(HttpServletRequest request, HttpServletResponse response)
          throws SQLException, ServletException, IOException {
    String pathInfo = request.getPathInfo();
    int id = Integer.parseInt(pathInfo.split("/")[1]);

    // Lấy thông tin user đang đăng nhập từ session
    HttpSession session = request.getSession();
    User currentUser = (User) session.getAttribute("adminUser");

    if (currentUser != null && currentUser.getId() == id) {
      session.setAttribute("error", "Không thể xóa tài khoản đang đăng nhập");
      response.sendRedirect(request.getContextPath() + "/admin/users");
      return;
    }

    try {
      userService.deleteUser(id);
      session.setAttribute("message", "Xóa người dùng thành công");
      response.sendRedirect(request.getContextPath() + "/admin/users");
    } catch (Exception e) {
      System.out.println(e.getMessage());
      session.setAttribute("error", "Xóa người dùng thất bại");
      response.sendRedirect(request.getContextPath() + "/admin/users");
    }
  }

  private User getUserFromRequest(HttpServletRequest request) {
    User user = new User();
    user.setName(request.getParameter("name"));
    user.setEmail(request.getParameter("email"));
    user.setPhone(request.getParameter("phone"));
    user.setRole(request.getParameter("role"));
    return user;
  }

  private void setTitle(HttpServletRequest request, String title) {
    request.setAttribute("title", title);
  }
}
