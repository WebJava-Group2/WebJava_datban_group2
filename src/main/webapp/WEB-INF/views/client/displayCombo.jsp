<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="container">
  <div class="row gy-5">
    <c:forEach items="${combos}" var="combo">
      <div class="col-lg-3 menu-item" data-aos="fade-up" data-aos-delay="100">
        <a href="${combo.imageUrl}" class="glightbox">
          <img src="${combo.imageUrl}" class="menu-img img-fluid" alt="${combo.name}">
        </a>
        <h4>${combo.name}</h4>
        <p class="ingredients">${combo.description}</p>
        <p class="price">$${combo.price}</p>
        <button class="btn btn-primary" onclick="addCombo('${combo.name}', ${combo.price})">Thêm vào đơn</button>
      </div>
    </c:forEach>
  </div>
</div>

<script>
function addCombo(comboName, comboPrice) {
    var orderTextArea = document.getElementById('orderTextArea');
    
    // Check if combo already exists in order
    if (window.order[comboName]) {
        window.order[comboName].quantity += 1;
    } else {
        window.order[comboName] = {
            quantity: 1,
            price: comboPrice
        };
    }
    
    // Update textarea with current order
    var orderText = '';
    var total = 0;
    for (var item in window.order) {
        var itemTotal = window.order[item].quantity * window.order[item].price;
        orderText += item + ' - ' + window.order[item].quantity + ' x $' + window.order[item].price + ' = $' + itemTotal + '\n';
        total += itemTotal;
    }
    orderText += '\nTổng cộng: $' + total;
    orderTextArea.value = orderText;
}
</script> 