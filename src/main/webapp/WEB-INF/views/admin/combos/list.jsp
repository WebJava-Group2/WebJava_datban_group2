<%@ page contentType="text/html; charset=UTF-8" language="java" %> <%@ taglib
prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> <%@ include
file="../layouts/header.jsp" %> <%@ include file="../layouts/sidebar.jsp" %>

<main>
  <div class="container-fluid px-4">
    <h1 class="mt-4">Quản lý Combo</h1>
    <ol class="breadcrumb mb-4">
      <li class="breadcrumb-item"><a href="index.html">Dashboard</a></li>
      <li class="breadcrumb-item active">Combo</li>
    </ol>
    <a
      href="${pageContext.request.contextPath}/admin/combos/add"
      class="btn btn-primary mb-3"
      >Thêm combo</a
    >

    <div class="d-flex justify-content-between align-items-center mb-3">
      <div class="d-flex align-items-center">
        <label for="statusFilter" class="me-2">Lọc theo trạng thái:</label>
        <select class="form-select" id="statusFilter" style="width: 200px">
          <option value="all">Tất cả</option>
          <option value="Đang bán">Đang bán</option>
          <option value="Hết hàng">Hết hàng</option>
        </select>
      </div>
      <div class="text-muted">
        Hiển thị ${(currentPage - 1) * itemsPerPage + 1} - ${currentPage *
        itemsPerPage > totalItems ? totalItems : currentPage * itemsPerPage} của
        ${totalItems} combo
      </div>
    </div>

    <div class="card mb-4">
      <div class="card-header">
        <i class="fas fa-table me-1"></i>
        Danh sách combo
      </div>
      <div class="card-body">
        <table id="datatablesSimple" class="table table-striped">
          <thead>
            <tr>
              <th>Mã combo</th>
              <th>Hình ảnh</th>
              <th>Tên combo</th>
              <th>Giá combo</th>
              <th>Trạng thái</th>
              <th>Thao tác</th>
            </tr>
          </thead>
          <tfoot>
            <tr>
              <th>Mã combo</th>
              <th>Hình ảnh</th>
              <th>Tên combo</th>
              <th>Giá combo</th>
              <th>Trạng thái</th>
              <th>Thao tác</th>
            </tr>
          </tfoot>
          <tbody>
            <c:forEach items="${combos}" var="combo">
              <tr>
                <td>${combo.id}</td>
                <td>
                  <img
                    src="https://picsum.photos/100/100?random=${combo.id}"
                    alt="Combo ${combo.id}"
                    style="width: 100px; height: 100px; object-fit: cover"
                  />
                </td>
                <td>${combo.name}</td>
                <td>${combo.price}</td>
                <td>
                  <c:choose>
                    <c:when test="${combo.status == 'available'}">
                      <button class="btn btn-success">Đang bán</button>
                    </c:when>
                    <c:otherwise>
                      <button class="btn btn-danger">Hết hàng</button>
                    </c:otherwise>
                  </c:choose>
                </td>
                <td>
                  <a
                    href="${pageContext.request.contextPath}/admin/combos/${combo.id}"
                    class="btn btn-info btn-sm"
                    >Chi tiết</a
                  >
                  <a
                    href="${pageContext.request.contextPath}/admin/combos/${combo.id}/edit"
                    class="btn btn-warning btn-sm"
                    >Sửa</a
                  >
                  <button
                    onclick="deleteCombo(${combo.id})"
                    class="btn btn-danger btn-sm"
                  >
                    Xóa
                  </button>
                </td>
              </tr>
            </c:forEach>
          </tbody>
        </table>

        <!-- Phân trang -->
        <nav aria-label="Page navigation" class="mt-4">
          <ul class="pagination justify-content-center">
            <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
              <a
                class="page-link"
                href="?page=${currentPage - 1}"
                aria-label="Previous"
              >
                <span aria-hidden="true">&laquo;</span>
              </a>
            </li>

            <c:forEach begin="1" end="${totalPages}" var="i">
              <li class="page-item ${currentPage == i ? 'active' : ''}">
                <a class="page-link" href="?page=${i}">${i}</a>
              </li>
            </c:forEach>

            <li
              class="page-item ${currentPage == totalPages ? 'disabled' : ''}"
            >
              <a
                class="page-link"
                href="?page=${currentPage + 1}"
                aria-label="Next"
              >
                <span aria-hidden="true">&raquo;</span>
              </a>
            </li>
          </ul>
        </nav>
      </div>
    </div>
  </div>
</main>

<script>
  function deleteCombo(id) {
    if (confirm("Bạn có chắc chắn muốn xóa combo này?")) {
      fetch("${pageContext.request.contextPath}/admin/combos/" + id, {
        method: "DELETE",
      }).then((response) => {
        if (response.ok) {
          window.location.reload();
        }
      });
    }
  }

  document.addEventListener("DOMContentLoaded", function () {
    const statusFilter = document.getElementById("statusFilter");
    const table = document.getElementById("datatablesSimple");
    const rows = table
      .getElementsByTagName("tbody")[0]
      .getElementsByTagName("tr");

    statusFilter.addEventListener("change", function () {
      const selectedStatus = this.value;

      for (let row of rows) {
        const statusCell = row.cells[4]; // Cột trạng thái
        if (
          selectedStatus === "all" ||
          statusCell.textContent === selectedStatus
        ) {
          row.style.display = "";
        } else {
          row.style.display = "none";
        }
      }
    });
  });
</script>

<%@ include file="../layouts/footer.jsp" %>
