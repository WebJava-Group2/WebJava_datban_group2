<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8" %>
<div id="layoutSidenav_nav">
  <nav class="sb-sidenav accordion sb-sidenav-dark" id="sidenavAccordion">
    <div class="sb-sidenav-menu">
      <div class="nav">
        <div class="sb-sidenav-menu-heading">Core</div>
        <a class="nav-link" href="${pageContext.request.contextPath}/admin">
          <div class="sb-nav-link-icon">
            <i class="fas fa-tachometer-alt"></i>
          </div>
          Dashboard
        </a>
        <div class="sb-sidenav-menu-heading">Quản lý</div>
        <a
            class="nav-link collapsed"
            href="#"
            data-bs-toggle="collapse"
            data-bs-target="#adminUser"
            aria-expanded="false"
            aria-controls="adminUser"
        >
          <div class="sb-nav-link-icon">
            <i class="fas fa-columns"></i>
          </div>
          Người dùng
          <div class="sb-sidenav-collapse-arrow">
            <i class="fas fa-angle-down"></i>
          </div>
        </a>
        <div
            class="collapse"
            id="adminUser"
            aria-labelledby="headingOne"
            data-bs-parent="#sidenavAccordion"
        >
          <nav class="sb-sidenav-menu-nested nav">
            <a
                class="nav-link"
                href="${pageContext.request.contextPath}/admin/users"
            >Danh sách người dùng</a
            >
            <a
                class="nav-link"
                href="${pageContext.request.contextPath}/admin/users/add"
            >Thêm người dùng</a
            >
          </nav>
        </div>
        <a
            class="nav-link collapsed"
            href="#"
            data-bs-toggle="collapse"
            data-bs-target="#adminTable"
            aria-expanded="false"
            aria-controls="adminTable"
        >
          <div class="sb-nav-link-icon">
            <i class="fas fa-columns"></i>
          </div>
          Bàn ăn
          <div class="sb-sidenav-collapse-arrow">
            <i class="fas fa-angle-down"></i>
          </div>
        </a>
        <div
            class="collapse"
            id="adminTable"
            aria-labelledby="headingOne"
            data-bs-parent="#sidenavAccordion"
        >
          <nav class="sb-sidenav-menu-nested nav">
            <a
                class="nav-link"
                href="${pageContext.request.contextPath}/admin/tables"
            >Danh sách bàn ăn</a
            >
            <a
                class="nav-link"
                href="${pageContext.request.contextPath}/admin/tables/add"
            >Thêm bàn ăn</a
            >
          </nav>
        </div>
        <a
          class="nav-link collapsed"
          href="#"
          data-bs-toggle="collapse"
          data-bs-target="#adminCombo"
          aria-expanded="false"
          aria-controls="adminCombo"
        >
          <div class="sb-nav-link-icon">
            <i class="fas fa-columns"></i>
          </div>
          Combo
          <div class="sb-sidenav-collapse-arrow">
            <i class="fas fa-angle-down"></i>
          </div>
        </a>
        <div
          class="collapse"
          id="adminCombo"
          aria-labelledby="headingOne"
          data-bs-parent="#sidenavAccordion"
        >
          <nav class="sb-sidenav-menu-nested nav">
            <a
              class="nav-link"
              href="${pageContext.request.contextPath}/admin/combos"
              >Danh sách combo</a
            >
            <a
              class="nav-link"
              href="${pageContext.request.contextPath}/admin/combos/add"
              >Thêm combo</a
            >
          </nav>
        </div>
        <a
          class="nav-link collapsed"
          href="#"
          data-bs-toggle="collapse"
          data-bs-target="#adminFood"
          aria-expanded="false"
          aria-controls="adminFood"
        >
          <div class="sb-nav-link-icon">
            <i class="fas fa-columns"></i>
          </div>
          Món ăn
          <div class="sb-sidenav-collapse-arrow">
            <i class="fas fa-angle-down"></i>
          </div>
        </a>
        <div
          class="collapse"
          id="adminFood"
          aria-labelledby="headingOne"
          data-bs-parent="#sidenavAccordion"
        >
          <nav class="sb-sidenav-menu-nested nav">
            <a
              class="nav-link"
              href="${pageContext.request.contextPath}/admin/foods"
              >Danh sách món ăn</a
            >
            <a
              class="nav-link"
              href="${pageContext.request.contextPath}/admin/foods/add"
              >Thêm món ăn</a
            >
          </nav>
        </div>
      </div>
    </div>
  </nav>
</div>
<div id="layoutSidenav_content">
