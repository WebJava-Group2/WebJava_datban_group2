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
    String keyword = getKeyword(request);
    int totalItems;
    List<User> users;

    boolean hasKeyword = keyword != null && !keyword.isEmpty();
    boolean hasRole = role != null && !role.equals("all");

    // Lấy tổng số items trước
    if (hasKeyword && hasRole) {
      totalItems = userService.getTotalUsersByKeywordWithRole(keyword, role);
    } else if (hasKeyword) {
      totalItems = userService.getTotalUsersByKeyword(keyword);
    } else if (hasRole) {
      totalItems = userService.getTotalUsersByRole(role);
    } else {
      totalItems = userService.getTotalUsers();
    }

    // Tính toán số trang và điều chỉnh trang hiện tại nếu cần
    int totalPages = Math.max(1, (int) Math.ceil((double) totalItems / itemsPerPage));
    if (currentPage > totalPages) {
      currentPage = totalPages;
    }

    // Lấy danh sách users sau khi đã điều chỉnh trang
    if (hasKeyword && hasRole) {
      users = userService.findByKeywordWithRole(keyword, role, currentPage, itemsPerPage);
    } else if (hasKeyword) {
      users = userService.findByKeyword(keyword, currentPage, itemsPerPage);
    } else if (hasRole) {
      users = userService.getUsersByRole(currentPage, itemsPerPage, role);
    } else {
      users = userService.getUsers(currentPage, itemsPerPage);
    }

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
    request.setAttribute("keyword", keyword);
    request.setAttribute("message", message);
    request.setAttribute("error", error);
    setTitle(request, "Danh sách người dùng");

    request.getRequestDispatcher("/WEB-INF/views/admin/users/list.jsp")
        .forward(request, response);
  }

  private void handleShowAddForm(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    setTitle(request, "Thêm người dùng");
    HttpSession session = request.getSession();
    User user = (User) session.getAttribute("user");
    if (user != null) {
      request.setAttribute("user", user);
      session.removeAttribute("user");
    }
    String error = (String) session.getAttribute("error");
    if (error != null) {
      request.setAttribute("error", error);
      session.removeAttribute("error");
    }
    request.getRequestDispatcher("/WEB-INF/views/admin/users/add.jsp")
        .forward(request, response);
  }

  private void handleShowEditForm(HttpServletRequest request, HttpServletResponse response, String pathInfo)
      throws SQLException, ServletException, IOException {
    HttpSession session = request.getSession();
    int id = Integer.parseInt(pathInfo.split("/")[1]);
    User user = userService.getUserById(id);
    User tempUser = (User) session.getAttribute("tempUser");
    System.out.println("tempUser in edit: get");
    System.out.println(tempUser);

    request.setAttribute("user", user);
    request.setAttribute("error", session.getAttribute("error"));
    request.setAttribute("tempUser", tempUser);

    session.removeAttribute("error");
    session.removeAttribute("tempUser");

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

  private String getKeyword(HttpServletRequest request) {
    String keyword = request.getParameter("keyword");
    if (keyword == null || keyword.isEmpty()) {
      return null;
    }
    return keyword;
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
    user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
    if (userService.checkEmailExist(user)) {
      session.setAttribute("error", "Email đã tồn tại");
      response.sendRedirect(request.getContextPath() + "/admin/users/add");
      return;
    }
    if (userService.checkPhoneExist(user)) {
      session.setAttribute("error", "Số điện thoại đã tồn tại");
      response.sendRedirect(request.getContextPath() + "/admin/users/add");
      return;
    }
    String password = request.getParameter("password");
    String confirmPassword = request.getParameter("confirmPassword");
    if (!userService.comparePassword(password, confirmPassword)) {
      session.setAttribute("error", "Mật khẩu không khớp");
      response.sendRedirect(request.getContextPath() + "/admin/users/add");
      return;
    }
    String hashedPassword = hashedHelper.hashPassword(password);
    user.setPassword(hashedPassword);
    try {
      userService.createUser(user);
      session.setAttribute("message", "Thêm người dùng thành công");
      response.sendRedirect(request.getContextPath() + "/admin/users");
    } catch (Exception e) {
      System.out.println(e.getMessage());
      if (e instanceof IllegalArgumentException) {
        session.setAttribute("error", e.getMessage());
      } else {
        session.setAttribute("error", "Thêm người dùng thất bại");
      }
      session.setAttribute("user", user);

      response.sendRedirect(request.getContextPath() + "/admin/users/add");
    }
  }

  private void handleUpdateUser(HttpServletRequest request, HttpServletResponse response, String pathInfo)
      throws SQLException, ServletException, IOException {
    int id = Integer.parseInt(pathInfo.split("/")[1]);
    HttpSession session = request.getSession();
    User currentUser = (User) session.getAttribute("adminUser");
    User existingUser = userService.getUserById(id);
    User user = getUserFromRequest(request);
    String oldEmail = existingUser.getEmail();
    String newEmail = user.getEmail();
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
      String confirmPassword = request.getParameter("confirmPassword");
      if (!userService.comparePassword(password, confirmPassword)) {
        session.setAttribute("error", "Mật khẩu không khớp");
        response.sendRedirect(request.getContextPath() + "/admin/users/" + id + "/edit");
        return;
      }

      String hashedPassword = hashedHelper.hashPassword(password);
      user.setPassword(hashedPassword);
    } else {
      user.setPassword(existingUser.getPassword());
    }
    try {
      userService.updateUser(user);
      if (currentUser.getId() == id && !oldEmail.equals(newEmail)) {
        session.setAttribute("message", "Cập nhật người dùng thành công. Vui lòng đăng nhập lại.");
        session.removeAttribute("adminUser");
        response.sendRedirect(request.getContextPath() + "/admin/login");
      } else {
        session.setAttribute("message", "Cập nhật người dùng thành công");
        response.sendRedirect(request.getContextPath() + "/admin/users");
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
      if (e instanceof IllegalArgumentException) {
        session.setAttribute("error", e.getMessage());
      } else {
        session.setAttribute("error", "Cập nhật người dùng thất bại");
      }
      System.out.println("tempUser in update");
      System.out.println(user);
      session.setAttribute("tempUser", user);
      response.sendRedirect(request.getContextPath() + "/admin/users/" + id + "/edit");
    }
  }

  private void handleDeleteUser(HttpServletRequest request, HttpServletResponse response)
      throws SQLException, ServletException, IOException {
    try {
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

      userService.deleteUser(id);
      session.setAttribute("message", "Xóa người dùng thành công");
      response.sendRedirect(request.getContextPath() + "/admin/users");
    } catch (Exception e) {
      System.out.println(e.getMessage());
      request.setAttribute("error", "Xóa người dùng thất bại");
      request.getRequestDispatcher("/WEB-INF/views/admin/users/list.jsp")
          .forward(request, response);
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
