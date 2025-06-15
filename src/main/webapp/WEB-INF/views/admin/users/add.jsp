<%@ page contentType="text/html; charset=UTF-8" language="java"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c"
           uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include
        file="../layouts/header.jsp" %>
<%@ include file="../layouts/sidebar.jsp" %>

<main>
    <div class="container-fluid px-4">
        <h1 class="mt-4">Thêm người dùng mới</h1>
        <ol class="breadcrumb mb-4">
            <li class="breadcrumb-item">
                <a href="${pageContext.request.contextPath}/admin">Dashboard</a>
            </li>
            <li class="breadcrumb-item">
                <a href="${pageContext.request.contextPath}/admin/users">Người dùng</a>
            </li>
            <li class="breadcrumb-item active">Thêm mới</li>
        </ol>
        <c:if test="${error != null}">
            <div class="alert alert-danger">${error}</div>
        </c:if>
        <div class="card mb-4">
            <div class="card-header">
                <i class="fas fa-user-plus me-1"></i>
                Thông tin người dùng mới
            </div>
            <div class="card-body">
                <form
                        id="addUserForm"
                        action="${pageContext.request.contextPath}/admin/users"
                        method="post"
                >
                    <div class="row mb-3">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label for="name" class="form-label">Tên người dùng</label>
                                <input
                                        type="text"
                                        class="form-control"
                                        id="name"
                                        name="name"
                                        placeholder="Nhập tên người dùng"
                                        required
                                        value="${user.name}"
                                />
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label for="email" class="form-label">Email</label>
                                <input
                                        type="email"
                                        class="form-control"
                                        id="email"
                                        name="email"
                                        placeholder="Nhập địa chỉ email"
                                        required
                                        value="${user.email}"
                                />
                            </div>
                        </div>
                    </div>

                    <div class="row mb-3">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label for="phone" class="form-label">Số điện thoại</label>
                                <input
                                        type="tel"
                                        class="form-control"
                                        id="phone"
                                        name="phone"
                                        placeholder="Nhập số điện thoại"
                                        required
                                        value="${user.phone}"
                                />
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label for="role" class="form-label">Vai trò</label>
                                <select class="form-select" id="role" name="role" required>
                                    <option value="">Chọn vai trò</option>
                                    <option value="admin" ${user.role == 'admin' ? 'selected' : ''}>Admin</option>
                                    <option value="customer" ${user.role == 'customer' ? 'selected' : ''}>Customer
                                    </option>
                                </select>
                            </div>
                        </div>
                    </div>

                    <div class="row mb-3">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label for="password" class="form-label">Mật khẩu</label>
                                <input
                                        type="password"
                                        class="form-control"
                                        id="password"
                                        name="password"
                                        placeholder="Nhập mật khẩu"
                                        required
                                />
                                <div class="form-text">Mật khẩu phải có ít nhất 8 ký tự, bao gồm chữ hoa, chữ thường, số
                                    và ký tự đặc biệt
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label for="confirmPassword" class="form-label"
                                >Xác nhận mật khẩu</label
                                >
                                <input
                                        type="password"
                                        class="form-control"
                                        id="confirmPassword"
                                        name="confirmPassword"
                                        placeholder="Nhập lại mật khẩu"
                                        required
                                />
                            </div>
                        </div>
                    </div>

                    <div class="mt-4 mb-0">
                        <button type="submit" class="btn btn-primary">Thêm mới</button>
                        <a
                                href="${pageContext.request.contextPath}/admin/users"
                                class="btn btn-secondary"
                        >Hủy</a
                        >
                    </div>
                </form>
            </div>
        </div>
    </div>
</main>

<%@ include file="../layouts/footer.jsp" %>

<script>
    document.addEventListener('DOMContentLoaded', function () {
        const form = document.getElementById('addUserForm');
        form.addEventListener('submit', function (event) {
            const name = document.getElementById('name').value.trim();
            const email = document.getElementById('email').value.trim();
            const phone = document.getElementById('phone').value.trim();
            const role = document.getElementById('role').value;
            const password = document.getElementById('password').value;
            const confirmPassword = document.getElementById('confirmPassword').value;

            if (!name || !email || !phone || !role || !password || !confirmPassword) {
                alert('Vui lòng điền đầy đủ các trường bắt buộc.');
                event.preventDefault();
                return;
            }

            // Validate email format (basic)
            const emailRegex = /^[\w-.]+@([\w-]+\.)+[\w-]{2,4}$/;
            if (!emailRegex.test(email)) {
                alert('Vui lòng nhập địa chỉ email hợp lệ.');
                event.preventDefault();
                return;
            }

            // Validate Vietnamese phone number format
            const phoneRegex = /^(?:(?:\+84|0084|0))[235789][0-9]{1,2}[0-9]{7}(?:[^\d]+|$)$/;
            if (!phoneRegex.test(phone)) {
                alert('Vui lòng nhập số điện thoại Việt Nam hợp lệ.');
                event.preventDefault();
                return;
            }

            // Validate password complexity
            const passwordRegex = /^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$/;
            if (!passwordRegex.test(password)) {
                alert('Mật khẩu phải có ít nhất 8 ký tự, bao gồm chữ hoa, chữ thường, số và ký tự đặc biệt.');
                event.preventDefault();
                return;
            }

            if (password !== confirmPassword) {
                alert('Mật khẩu và xác nhận mật khẩu không khớp.');
                event.preventDefault();
                return;
            }

            if (!confirm('Bạn có chắc chắn muốn thêm người dùng mới?')) {
                event.preventDefault();
            }
        });
    });
</script>
