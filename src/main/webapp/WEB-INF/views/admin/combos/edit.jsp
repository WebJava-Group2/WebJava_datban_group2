<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="../layouts/header.jsp" %>
<%@ include file="../layouts/sidebar.jsp" %>

<main>
    <div class="container-fluid px-4">
        <h1 class="mt-4">Chỉnh sửa Combo</h1>
        <ol class="breadcrumb mb-4">
            <li class="breadcrumb-item"><a href="index.html">Dashboard</a></li>
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/admin/combos">Combo</a></li>
            <li class="breadcrumb-item active">Chỉnh sửa</li>
        </ol>

        <div class="card mb-4">
            <div class="card-header">
                <i class="fas fa-edit me-1"></i>
                Chỉnh sửa thông tin combo
            </div>
            <div class="card-body">
                <form action="${pageContext.request.contextPath}/admin/combos/${combo.id}" method="POST" enctype="multipart/form-data">
                    <input type="hidden" name="_method" value="PUT">
                    
                    <div class="mb-3">
                        <label for="name" class="form-label">Tên combo</label>
                        <input type="text" class="form-control" id="name" name="name" value="${combo.name}" required>
                    </div>

                    <div class="mb-3">
                        <label for="description" class="form-label">Mô tả</label>
                        <textarea class="form-control" id="description" name="description" rows="3">${combo.description}</textarea>
                    </div>

                    <div class="mb-3">
                        <label for="price" class="form-label">Giá</label>
                        <input type="number" class="form-control" id="price" name="price" value="${combo.price}" required>
                    </div>

                    <div class="mb-3">
                        <label for="status" class="form-label">Trạng thái</label>
                        <select class="form-select" id="status" name="status">
                            <option value="available" ${combo.status == 'available' ? 'selected' : ''}>Đang bán</option>
                            <option value="unavailable" ${combo.status == 'unavailable' ? 'selected' : ''}>Hết hàng</option>
                        </select>
                    </div>

                    <div class="mb-3">
                        <label for="image" class="form-label">Hình ảnh</label>
                        <input type="file" class="form-control" id="image" name="image" accept="image/*">
                        <div class="mt-2">
                            <img src="https://picsum.photos/100/100?random=${combo.id}" alt="Combo ${combo.id}" 
                                 style="width: 100px; height: 100px; object-fit: cover">
                        </div>
                    </div>

                    <div class="mt-3">
                        <button type="submit" class="btn btn-primary">Lưu thay đổi</button>
                        <a href="${pageContext.request.contextPath}/admin/combos" class="btn btn-secondary">Hủy</a>
                    </div>
                </form>
            </div>
        </div>
    </div>
</main>

<%@ include file="../layouts/footer.jsp" %> 