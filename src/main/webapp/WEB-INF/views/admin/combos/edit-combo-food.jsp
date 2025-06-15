<%@ page contentType="text/html; charset=UTF-8" language="java"
pageEncoding="UTF-8" %> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %>
<div class="card mb-4">
  <div class="card-header">
    <i class="fas fa-table me-1"></i>
    Danh sách món ăn trong combo
  </div>

  <div class="card-body">
    <!-- Form lọc -->
    <form action="${pageContext.request.contextPath}/admin/combos/${combo.id}/edit" method="GET" class="mb-4">
      <div class="row g-3">
        <div class="col-md-4">
          <input type="text" class="form-control" name="foodKeyword" placeholder="Tìm kiếm theo tên..." value="${foodKeyword}">
        </div>
        <div class="col-md-2">
          <select class="form-select" name="foodStatus">
            <option value="all" ${empty foodStatus || foodStatus == 'all' ? 'selected' : ''}>Tất cả trạng thái</option>
            <option value="available" ${foodStatus == 'available' ? 'selected' : ''}>Đang bán</option>
            <option value="unavailable" ${foodStatus == 'unavailable' ? 'selected' : ''}>Không bán</option>
          </select>
        </div>
        <div class="col-md-2">
          <select class="form-select" name="foodMealType">
            <option value="all" ${empty foodMealType || foodMealType == 'all' ? 'selected' : ''}>Tất cả loại món</option>
            <option value="breakfast" ${foodMealType == 'breakfast' ? 'selected' : ''}>Bữa sáng</option>
            <option value="lunch" ${foodMealType == 'lunch' ? 'selected' : ''}>Bữa trưa</option>
            <option value="dinner" ${foodMealType == 'dinner' ? 'selected' : ''}>Bữa tối</option>
            <option value="dessert" ${foodMealType == 'dessert' ? 'selected' : ''}>Tráng miệng</option>
          </select>
        </div>
        <div class="col-md-2">
          <select class="form-select" name="foodItemsPerPage">
            <option value="10" ${foodItemsPerPage == 10 ? 'selected' : ''}>10</option>
            <option value="20" ${foodItemsPerPage == 20 ? 'selected' : ''}>20</option>
            <option value="50" ${foodItemsPerPage == 50 ? 'selected' : ''}>50</option>
            <option value="100" ${foodItemsPerPage == 100 ? 'selected' : ''}>100</option>
          </select>
        </div>
        <div class="col-md-1">
          <button type="submit" class="btn btn-primary w-100">Lọc</button>
        </div>
        <div class="col-md-1">
          <button type="button" class="btn btn-danger w-100" onclick="clearFilters()">Hoàn tác</button>
        </div>
      </div>
    </form>

    <!-- Hiển thị kết quả lọc -->
    <c:if test="${not empty foodKeyword || (not empty foodStatus && foodStatus != 'all') || (not empty foodMealType && foodMealType != 'all')}">
      <div class="alert alert-info">
        Kết quả lọc: 
        <c:if test="${not empty foodKeyword}">
          <span class="badge bg-secondary">Từ khóa: ${foodKeyword}</span>
        </c:if>
        <c:if test="${not empty foodStatus && foodStatus != 'all'}">
          <span class="badge bg-secondary">Trạng thái: ${foodStatus == 'available' ? 'Đang bán' : 'Không bán'}</span>
        </c:if>
        <c:if test="${not empty foodMealType && foodMealType != 'all'}">
          <span class="badge bg-secondary">
            Loại món: 
            <c:choose>
              <c:when test="${foodMealType == 'breakfast'}">Bữa sáng</c:when>
              <c:when test="${foodMealType == 'lunch'}">Bữa trưa</c:when>
              <c:when test="${foodMealType == 'dinner'}">Bữa tối</c:when>
              <c:otherwise>Tráng miệng</c:otherwise>
            </c:choose>
          </span>
        </c:if>
      </div>
    </c:if>

    <div class="table-responsive">
      <table class="table table-striped table-bordered">
        <thead>
          <tr>
            <th class="text-center" style="width: 50px;">Chọn</th>
            <th class="text-center" style="width: 50px;">STT</th>
            <th class="text-center" style="width: 80px;">Mã món</th>
            <th class="text-center" style="width: 100px;">Ảnh</th>
            <th class="text-center">Tên món</th>
            <th class="text-center" style="width: 120px;">Trạng thái</th>
            <th class="text-center" style="width: 100px;">Số lượng</th>
            <th class="text-center" style="width: 120px;">Đơn giá</th>
            <th class="text-center" style="width: 120px;">Tổng giá</th>
            <th class="text-center" style="width: 120px;">Loại món</th>
          </tr>
        </thead>
        <tbody>
          <c:if test="${foods.size() == 0}">
            <tr>
              <td colspan="10" class="text-center text-danger">Không có món ăn nào</td>
            </tr>
          </c:if>
          
          <c:forEach items="${foods}" var="food" varStatus="loop">
            <tr class="${food.isMainFood ? 'table-success' : ''}" data-food-id="${food.id}">
              <td class="text-center align-middle">
                <input type="checkbox" class="form-check-input" 
                       ${food.isMainFood ? 'checked' : ''} 
                       onchange="toggleFoodSelection(this, ${food.id})">
              </td>
              <td class="text-center align-middle">${(currentFoodPage - 1) * foodItemsPerPage + loop.index + 1}</td>
              <td class="text-center align-middle food-id">${food.id}</td>
              <td class="text-center align-middle">
                <img src="${not empty food.imageUrl ? food.imageUrl : 'https://picsum.photos/100/100?random='.concat(food.id)}"
                     alt="Food ${food.id}"
                     style="width: 80px; height: 80px; object-fit: cover">
              </td>
              <td class="align-middle">${food.name}</td>
              <td class="text-center align-middle">
                <span class="badge ${food.status == 'available' ? 'bg-success' : 'bg-danger'}">
                  ${food.status == 'available' ? 'Đang bán' : 'Không bán'}
                </span>
              </td>
              <td class="text-center align-middle">
                <input type="number" class="form-control form-control-sm quantity-input" 
                       value="${food.quantity}" min="0"
                       ${food.isMainFood ? '' : 'disabled'}
                       onchange="updateQuantity(this, ${food.id}, ${food.price})">
              </td>
              <td class="text-center align-middle">${food.price} VNĐ</td>
              <td class="text-center align-middle total-price">${food.totalPrice} VNĐ</td>
              <td class="text-center align-middle">
                <span class="badge 
                  ${food.mealType == 'breakfast' ? 'bg-primary' : 
                    food.mealType == 'lunch' ? 'bg-success' : 
                    food.mealType == 'dinner' ? 'bg-info' : 'bg-warning'}">
                  ${food.mealType == 'breakfast' ? 'Bữa sáng' : 
                    food.mealType == 'lunch' ? 'Bữa trưa' : 
                    food.mealType == 'dinner' ? 'Bữa tối' : 'Tráng miệng'}
                </span>
              </td>
            </tr>
          </c:forEach>
        </tbody>
      </table>
    </div>

    <!-- Phân trang -->
        <c:if test="${totalFoods > 0}">
          <nav aria-label="Page navigation" class="mt-4">
            <ul class="pagination justify-content-center">
              <li class="page-item ${currentFoodPage == 1 ? 'disabled' : ''}">
                <a class="page-link" href="javascript:void(0)" 
                   onclick="redirectToPage(${currentFoodPage - 1})"
                   aria-label="Previous">
                  <span aria-hidden="true">&laquo;</span>
                </a>
              </li>
              
              <li class="page-item ${currentFoodPage == 1 ? 'active' : ''}">
                <a class="page-link" href="javascript:void(0)"
                   onclick="redirectToPage(1)">
                  1
                </a>
              </li>
              
              <c:if test="${currentFoodPage > 4}">
                <li class="page-item disabled">
                  <span class="page-link">...</span>
                </li>
              </c:if>
              
              <c:forEach begin="${Math.max(2, currentFoodPage - 2)}"
                         end="${Math.min(totalFoodPages - 1, currentFoodPage + 2)}"
                         var="i">
                <li class="page-item ${currentFoodPage == i ? 'active' : ''}">
                  <a class="page-link" href="javascript:void(0)"
                     onclick="redirectToPage(${i})">
                      ${i}
                  </a>
                </li>
              </c:forEach>
              
              <c:if test="${currentFoodPage < totalFoodPages - 3}">
                <li class="page-item disabled">
                  <span class="page-link">...</span>
                </li>
              </c:if>
              
              <c:if test="${totalFoodPages > 1}">
                <li class="page-item ${currentFoodPage == totalFoodPages ? 'active' : ''}">
                  <a class="page-link" href="javascript:void(0)"
                     onclick="redirectToPage(${totalFoodPages})">
                      ${totalFoodPages}
                  </a>
                </li>
              </c:if>
              
              <li class="page-item ${currentFoodPage == totalFoodPages ? 'disabled' : ''}">
                <a class="page-link" href="javascript:void(0)"
                   onclick="redirectToPage(${currentFoodPage + 1})"
                   aria-label="Next">
                  <span aria-hidden="true">&raquo;</span>
                </a>
              </li>
            </ul>
          </nav>
        </c:if>
  </div>
</div>
<script>
function clearFilters() {
  if (
    confirm(
      "Thao tác này sẽ xóa tất cả các lọc và dữ liệu đang sửa. Bạn có chắc chắn muốn hoàn tác?"
    )
  ) {
    document.querySelector("form").reset();
    window.location.href =
      "${pageContext.request.contextPath}/admin/combos/${combo.id}/edit";
  }
}
</script>