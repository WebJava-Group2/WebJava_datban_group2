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

    <div class="card">
      <div class="card-body">
        <div class="row">
          <div class="col-md-4">
            <img
              src="https://picsum.photos/100/100?random=${combo.id}"
              alt="Combo ${combo.id}"
              class="img-fluid mb-3"
              style="width: 100%; height: 100%; object-fit: cover"
            />
          </div>
          <div class="col-md-8">
            <h5 class="card-title">${combo.name}</h5>
            <p class="card-text">
              <strong>Mô tả:</strong> ${combo.description}
            </p>
            <p class="card-text"><strong>Giá:</strong> ${combo.price}</p>
            <div class="mt-3">
              <a
                href="${pageContext.request.contextPath}/admin/combos/${combo.id}/edit"
                class="btn btn-primary"
                >Chỉnh sửa</a
              >
              <button onclick="deleteCombo(${combo.id})" class="btn btn-danger">
                Xóa
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>

  <script>
    function deleteCombo(id) {
      if (confirm("Bạn có chắc chắn muốn xóa combo này?")) {
        fetch("${pageContext.request.contextPath}/admin/combos/" + id, {
          method: "DELETE",
        }).then((response) => {
          if (response.ok) {
            window.location.href =
              "${pageContext.request.contextPath}/admin/combos";
          }
        });
      }
    }
  </script>
</main>
<%@ include file="../layouts/footer.jsp" %>
