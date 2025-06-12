<%@ page contentType="text/html; charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include
        file="../layouts/header.jsp" %>
<%@ include file="../layouts/sidebar.jsp" %>

<main>
    <div class="container-fluid px-4">
        <h1 class="mt-4">Sơ đồ bàn ăn</h1>
        <ol class="breadcrumb mb-4">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/admin">Dashboard</a></li>
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/admin/reservations">Đặt bàn</a></li>
            <li class="breadcrumb-item active">Sơ đồ bàn ăn</li>
        </ol>

        <c:if test="${error != null}">
            <div class="alert alert-danger" role="alert">
                <c:out value="${error}"/>
            </div>
        </c:if>

        <c:if test="${message != null}">
            <div class="alert alert-success">
                <c:out value="${message}"/>
            </div>
        </c:if>

        <div class="card mb-4">
            <div class="card-header">
                <i class="fas fa-chair me-1"></i>
                Danh sách bàn ăn
            </div>
            <div class="card-body">
                <div class="row row-cols-1 row-cols-md-3 g-4">
                    <c:if test="${empty tables}">
                        <div class="col">
                            <div class="alert alert-info text-center" role="alert">
                                Không có bàn nào để hiển thị.
                            </div>
                        </div>
                    </c:if>
                    <c:forEach items="${tables}" var="table">
                        <div class="col">
                            <div class="card h-100 table-card table-status-${table.status}">
                                <div class="card-body d-flex flex-column justify-content-between align-items-center">
                                    <h5 class="card-title text-center mb-0">${table.name}</h5>
                                    <p class="card-text text-center">Sức chứa: ${table.capacity} người</p>
                                    <p class="card-text text-center">Trạng thái:
                                        <span class="badge ${table.status == 'available' ? 'bg-success' : (table.status == 'reserved' ? 'bg-danger' : 'bg-warning')}">
                                            <c:choose>
                                                <c:when test="${table.status == 'available'}">Trống</c:when>
                                                <c:when test="${table.status == 'occupied'}">Đang có khách</c:when>
                                                <c:when test="${table.status == 'reserved'}">Đã đặt trước</c:when>
                                                <c:otherwise>Đang bảo trì</c:otherwise>
                                            </c:choose>
                                        </span>
                                    </p>
                                    <c:if test="${reservationId != null && table.status == 'available'}">
                                        <button class="btn btn-primary assign-button" data-table-id="${table.id}" data-table-name="${table.name}">Thêm vào bàn</button>
                                    </c:if>
                                    <c:if test="${reservationId != null && table.status != 'available'}">
                                        <button class="btn btn-secondary" disabled>Không thể thêm</button>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>
    </div>
</main>

<style>
    .table-card {
        border: 2px solid #ddd;
        border-radius: 8px;
        transition: all 0.3s ease;
    }
    .table-card:hover {
        transform: translateY(-5px);
        box-shadow: 0 4px 8px rgba(0,0,0,0.1);
    }
    .table-status-available {
        background-color: #e6ffe6; /* Light green */
        border-color: #28a745; /* Green border */
    }
    .table-status-reserved {
        background-color: #ffe6e6; /* Light red */
        border-color: #dc3545; /* Red border */
    }
    .table-status-occupied {
        background-color: #fff8e6; /* Light orange */
        border-color: #ffc107; /* Orange border */
    }
    .table-status-maintenance {
        background-color: #e6e6e6; /* Light gray */
        border-color: #6c757d; /* Gray border */
    }
</style>

<script>
    document.querySelectorAll('.assign-button').forEach(button => {
        button.addEventListener('click', function() {
            const tableId = this.dataset.tableId;
            const tableName = this.dataset.tableName;
            const reservationId = '${reservationId}'; // Lấy reservationId từ JSP

            console.log("Reservation ID:", reservationId);
            console.log("Table ID:", tableId);
            console.log("Table Name:", tableName);

            if (confirm(`Bạn có chắc chắn muốn thêm đặt bàn #${reservationId} vào ${tableName} (ID: ${tableId})?`)) {
                fetch('${pageContext.request.contextPath}/admin/reservations/assign-table', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded',
                    },
                    body: `reservationId=${reservationId}&tableId=${tableId}`
                })
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        alert(data.message);
                        window.location.reload(); // Tải lại trang để cập nhật trạng thái bàn
                    } else {
                        alert(data.message);
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert('Đã xảy ra lỗi khi thêm đặt bàn vào bàn.');
                });
            }
        });
    });
</script>

<%@ include file="../layouts/footer.jsp" %>