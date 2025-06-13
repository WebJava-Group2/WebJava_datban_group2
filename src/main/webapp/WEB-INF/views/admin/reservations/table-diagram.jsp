<%@ page contentType="text/html; charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="../layouts/header.jsp" %>
<%@ include file="../layouts/sidebar.jsp" %>

<main>
    <div class="container-fluid px-4">
        <h1 class="mt-4">Chọn bàn cho đặt bàn #<c:out value="${reservationId}"/></h1>
        <ol class="breadcrumb mb-4">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/admin">Dashboard</a></li>
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/admin/reservations">Đặt bàn</a></li>
            <li class="breadcrumb-item active">Chọn bàn</li>
        </ol>

        <c:if test="${error != null}">
            <div class="alert alert-danger" role="alert">
                <c:out value="${error}"/>
            </div>
        </c:if>

        <c:if test="${tables.size() == 0}">
            <div class="alert alert-warning" role="alert">
                Không có bàn nào có sẵn.
            </div>
        </c:if>

        <c:if test="${tables.size() > 0}">
            <form action="${pageContext.request.contextPath}/admin/reservations/assign-table" method="post">
                <input type="hidden" name="reservationId" value="${reservationId}">
                <div class="row">
                    <c:forEach items="${tables}" var="table">
                        <div class="col-md-4 mb-3">
                            <div class="card table-card table-status-<c:out value="${table.status}"/>">
                                <div class="card-body">
                                    <h5 class="card-title">Bàn #<c:out value="${table.id}"/> - <c:out value="${table.name}"/></h5>
                                    <p class="card-text">
                                        <strong>Trạng thái:</strong> 
                                        <c:choose>
                                            <c:when test="${table.status == 'available'}">
                                                <span class="badge bg-success">Còn trống</span>
                                            </c:when>
                                            <c:when test="${table.status == 'reserved'}">
                                                <span class="badge bg-warning">Đã đặt</span>
                                            </c:when>
                                            <c:when test="${table.status == 'occupied'}">
                                                <span class="badge bg-danger">Đang sử dụng</span>
                                            </c:when>
                                            <c:when test="${table.status == 'maintenance'}">
                                                <span class="badge bg-secondary">Bảo trì</span>
                                            </c:when>
                                        </c:choose>
                                    </p>
                                    <c:if test="${table.status == 'available'}">
                                        <button type="submit" name="tableId" value="${table.id}" 
                                                class="btn btn-primary">
                                            <i class="fas fa-check me-1"></i> Chọn bàn này
                                        </button>
                                    </c:if>
                                    <c:if test="${table.status != 'available'}">
                                        <button type="button" class="btn btn-secondary" disabled>
                                            <i class="fas fa-times me-1"></i> Không thể chọn
                                        </button>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </form>
        </c:if>

        <div class="mt-3">
            <a href="${pageContext.request.contextPath}/admin/reservations" class="btn btn-secondary">
                <i class="fas fa-arrow-left me-1"></i> Quay lại danh sách
            </a>
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
        background-color: #e6ffe6;
        border-color: #28a745;
    }
    .table-status-reserved {
        background-color: #fff8e6;
        border-color: #ffc107;
    }
    .table-status-occupied {
        background-color: #ffe6e6;
        border-color: #dc3545;
    }
    .table-status-maintenance {
        background-color: #e6e6e6;
        border-color: #6c757d;
    }
</style>

<%@ include file="../layouts/footer.jsp" %>