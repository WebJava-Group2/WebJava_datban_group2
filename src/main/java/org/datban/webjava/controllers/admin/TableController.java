package org.datban.webjava.controllers.admin;

import org.datban.webjava.models.Table;
import org.datban.webjava.services.TableService;

import java.io.*;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/admin/tables/*")
public class TableController extends HttpServlet {
  private TableService tableService;
  private static final int ITEMS_PER_PAGE = 10;

  @Override
  public void init() throws ServletException {
    tableService = new TableService();
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
    String pathInfo = request.getPathInfo();

    try {
      if (pathInfo == null || pathInfo.equals("/")) {
        handleListTables(request, response);
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
        handleUpdateTable(request, response, pathInfo);
      } else {
        handleCreateTable(request, response);
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  @Override
  protected void doDelete(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
    try {
      handleDeleteTable(request, response);
      response.setStatus(HttpServletResponse.SC_OK);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  private void handleListTables(HttpServletRequest request, HttpServletResponse response)
          throws SQLException, ServletException, IOException {
    int currentPage = getCurrentPage(request);
    int itemsPerPage = getItemsPerPage(request);
    String status = getStatus(request);

    int totalItems;
    List<Table> tables;

    if (status != null && !status.equals("all")) {
      tables = tableService.getTablesByStatus(status);
      totalItems = tables.size();
    } else {
      tables = tableService.getTablesByPage(currentPage, itemsPerPage);
      totalItems = tableService.getTotalTables();
    }

    int totalPages = (int) Math.ceil((double) totalItems / itemsPerPage);

    // Lấy message và error từ session
    HttpSession session = request.getSession();
    String message = (String) session.getAttribute("message");
    String error = (String) session.getAttribute("error");

    // Xóa message và error khỏi session sau khi đã lấy
    session.removeAttribute("message");
    session.removeAttribute("error");

    setPaginationAttributes(request, currentPage, totalPages, totalItems, itemsPerPage);
    request.setAttribute("tables", tables);
    request.setAttribute("selectedStatus", status);
    request.setAttribute("message", message);
    request.setAttribute("error", error);
    setTitle(request, "Danh sách bàn");
    request.getRequestDispatcher("/WEB-INF/views/admin/tables/list.jsp")
            .forward(request, response);
  }

  private void handleShowAddForm(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
    setTitle(request, "Thêm bàn");
    request.getRequestDispatcher("/WEB-INF/views/admin/tables/add.jsp")
            .forward(request, response);
  }

  private void handleShowEditForm(HttpServletRequest request, HttpServletResponse response, String pathInfo)
          throws SQLException, ServletException, IOException {
    int id = Integer.parseInt(pathInfo.split("/")[1]);
    Table table = tableService.getTableById(id);
    request.setAttribute("table", table);
    setTitle(request, "Chỉnh sửa bàn " + table.getName());
    request.getRequestDispatcher("/WEB-INF/views/admin/tables/edit.jsp")
            .forward(request, response);
  }

  private void handleShowDetail(HttpServletRequest request, HttpServletResponse response, String pathInfo)
          throws SQLException, ServletException, IOException {
    int id = Integer.parseInt(pathInfo.substring(1));
    Table table = tableService.getTableById(id);
    request.setAttribute("table", table);
    setTitle(request, "Bàn " + table.getName());
    request.getRequestDispatcher("/WEB-INF/views/admin/tables/detail.jsp")
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

  private void handleCreateTable(HttpServletRequest request, HttpServletResponse response)
          throws SQLException, ServletException, IOException {
    HttpSession session = request.getSession();
    try {
      Table table = getTableFromRequest(request);
      tableService.createTable(table);
      session.setAttribute("message", "Thêm bàn thành công");
      response.sendRedirect(request.getContextPath() + "/admin/tables");
    } catch (Exception e) {
      System.out.println(e.getMessage());
      session.setAttribute("error", "Thêm bàn thất bại");
      response.sendRedirect(request.getContextPath() + "/admin/tables/add");
    }
  }

  private void handleUpdateTable(HttpServletRequest request, HttpServletResponse response, String pathInfo)
          throws SQLException, ServletException, IOException {
    HttpSession session = request.getSession();
    int id = Integer.parseInt(pathInfo.split("/")[1]);
    try {
      Table table = getTableFromRequest(request);
      table.setId(id);
      tableService.updateTable(table);
      session.setAttribute("message", "Cập nhật bàn thành công");
      response.sendRedirect(request.getContextPath() + "/admin/tables");
    } catch (Exception e) {
      System.out.println(e.getMessage());
      session.setAttribute("error", "Cập nhật bàn thất bại");
      response.sendRedirect(request.getContextPath() + "/admin/tables/" + id + "/edit");
    }
  }

  private void handleDeleteTable(HttpServletRequest request, HttpServletResponse response)
          throws SQLException, ServletException, IOException {
    HttpSession session = request.getSession();
    String pathInfo = request.getPathInfo();
    int id = Integer.parseInt(pathInfo.split("/")[1]);
    try {
      tableService.deleteTable(id);
      session.setAttribute("message", "Xóa bàn thành công");
      response.sendRedirect(request.getContextPath() + "/admin/tables");
    } catch (Exception e) {
      System.out.println(e.getMessage());
      session.setAttribute("error", "Xóa bàn thất bại");
      response.sendRedirect(request.getContextPath() + "/admin/tables");
    }
  }

  private Table getTableFromRequest(HttpServletRequest request) {
    Table table = new Table();
    table.setName(request.getParameter("name"));
    table.setCapacity(Integer.parseInt(request.getParameter("capacity")));
    table.setStatus(request.getParameter("status"));
    table.setLocation(request.getParameter("location"));

    return table;
  }

  private void setTitle(HttpServletRequest request, String title) {
    request.setAttribute("title", title);
  }
}
