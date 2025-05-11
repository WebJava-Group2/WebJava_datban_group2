<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Danh sách Combo</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-4">
        <h2>Danh sách Combo</h2>
        
        <div class="row mt-4">
            <c:forEach items="${combos}" var="combo">
                <div class="col-md-4 mb-4">
                    <div class="card">
                        <img src="${combo.imageUrl}" class="card-img-top" alt="${combo.name}">
                        <div class="card-body">
                            <h5 class="card-title">${combo.name}</h5>
                            <p class="card-text">${combo.description}</p>
                            <p class="card-text">
                                <strong>Giá:</strong> ${combo.price} VNĐ
                            </p>
                            <div class="d-flex justify-content-between">
                                <a href="#" class="btn btn-primary">Chi tiết</a>
                                <a href="#" class="btn btn-warning">Chỉnh sửa</a>
                            </div>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
