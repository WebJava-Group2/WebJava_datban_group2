// Constants
const CONSTANTS = {
  CURRENCY: "VNĐ",
  DEFAULT_IMAGE: "https://picsum.photos/300/300?random=${combo.id}",
  SELECTORS: {
    FORM: "#editForm",
    IMAGE_PREVIEW: "#imagePreview",
    IMAGE_URL: "#imageUrl",
    FOOD_QUANTITIES: "#foodQuantities",
    ORIGINAL_PRICE: "#originalPrice",
    COMBO_PRICE: "#price",
    DISCOUNT_PERCENTAGE: "#discountPercentage",
  },
  CLASSES: {
    SELECTED_ROW: "table-success",
    SELECTED_UNAVAILABLE: "table-danger",
  },
};

// Utility functions
function handleFoodQuantities() {
  if (typeof selectedFoods === "undefined") return "";
  return Object.entries(selectedFoods)
    .filter(([_, quantity]) => quantity > 0)
    .map(([foodId, quantity]) => `${foodId}-${quantity}`)
    .join(",");
}

function previewImageUrl(input) {
  const preview = document.querySelector(CONSTANTS.SELECTORS.IMAGE_PREVIEW);
  const url = input.value?.trim();
  preview.src = url || CONSTANTS.DEFAULT_IMAGE;
}

function getFoodQuantities() {
  return (
    new URLSearchParams(window.location.search).get("foodQuantities") || ""
  );
}

function updateTotalPrice(row, quantity, price) {
  const totalPrice = quantity * price;
  row.querySelector(
    ".total-price"
  ).textContent = `${totalPrice} ${CONSTANTS.CURRENCY}`;
}

function calculateTotalOriginalPrice() {
  let totalPrice = 0;
  document.querySelectorAll("tr[data-food-id]").forEach((row) => {
    const checkbox = row.querySelector('input[type="checkbox"]');
    if (checkbox?.checked) {
      const quantity =
        parseInt(row.querySelector(".quantity-input").value) || 0;
      const price =
        parseFloat(row.querySelector("td:nth-child(8)").textContent) || 0;
      totalPrice += quantity * price;
    }
  });
  return totalPrice;
}

function formatNumber(number) {
  return new Intl.NumberFormat("vi-VN").format(number);
}

function calculateDiscount() {
  const originalPrice = calculateTotalOriginalPrice();
  const comboPrice =
    parseFloat(document.querySelector(CONSTANTS.SELECTORS.COMBO_PRICE).value) ||
    0;

  // Cập nhật giá gốc
  document.querySelector(CONSTANTS.SELECTORS.ORIGINAL_PRICE).value =
    formatNumber(originalPrice);

  // Tính và cập nhật phần trăm giảm giá/tăng giá
  if (originalPrice > 0 && comboPrice > 0) {
    const percentageDiff = (
      ((comboPrice - originalPrice) / originalPrice) *
      100
    ).toFixed(1);
    const discountInput = document.querySelector(
      CONSTANTS.SELECTORS.DISCOUNT_PERCENTAGE
    );

    if (comboPrice > originalPrice) {
      // Tăng giá
      discountInput.value = `+${percentageDiff}`;
      discountInput.classList.remove("text-success");
      discountInput.classList.add("text-danger");
    } else if (comboPrice < originalPrice) {
      // Giảm giá
      discountInput.value = percentageDiff;
      discountInput.classList.remove("text-danger");
      discountInput.classList.add("text-success");
    } else {
      // Giá không đổi
      discountInput.value = "0.0";
      discountInput.classList.remove("text-danger", "text-success");
    }
  } else {
    const discountInput = document.querySelector(
      CONSTANTS.SELECTORS.DISCOUNT_PERCENTAGE
    );
    discountInput.value = "0.0";
    discountInput.classList.remove("text-danger", "text-success");
  }
}

function updateUI() {
  document.querySelectorAll("tr[data-food-id]").forEach((row) => {
    const foodId = parseInt(row.getAttribute("data-food-id"));
    const checkbox = row.querySelector('input[type="checkbox"]');
    const quantityInput = row.querySelector(".quantity-input");
    const statusBadge = row.querySelector(".badge");

    if (!checkbox || !quantityInput) return;

    const isSelected = selectedFoods.hasOwnProperty(foodId);
    const quantity = isSelected ? selectedFoods[foodId] : 0;
    const price =
      parseFloat(row.querySelector("td:nth-child(8)").textContent) || 0;
    const isUnavailable = statusBadge?.textContent.trim() === "Không bán";

    checkbox.checked = isSelected;
    if (isSelected) {
      if (isUnavailable) {
        row.classList.add(CONSTANTS.CLASSES.SELECTED_UNAVAILABLE);
        row.classList.remove(CONSTANTS.CLASSES.SELECTED_ROW);
      } else {
        row.classList.add(CONSTANTS.CLASSES.SELECTED_ROW);
        row.classList.remove(CONSTANTS.CLASSES.SELECTED_UNAVAILABLE);
      }
    } else {
      row.classList.remove(
        CONSTANTS.CLASSES.SELECTED_ROW,
        CONSTANTS.CLASSES.SELECTED_UNAVAILABLE
      );
    }

    quantityInput.disabled = !isSelected;
    quantityInput.value = quantity;
    updateTotalPrice(row, quantity, price);
  });

  updateFoodQuantitiesInput();
  calculateDiscount();
}

// Event handlers
function toggleFoodSelection(checkbox, foodId) {
  const row = checkbox.closest("tr");
  const quantityInput = row.querySelector(".quantity-input");
  const statusBadge = row.querySelector(".badge");
  const isUnavailable = statusBadge?.textContent.trim() === "Không bán";
  foodId = parseInt(foodId);

  if (checkbox.checked) {
    if (isUnavailable) {
      row.classList.add(CONSTANTS.CLASSES.SELECTED_UNAVAILABLE);
      row.classList.remove(CONSTANTS.CLASSES.SELECTED_ROW);
    } else {
      row.classList.add(CONSTANTS.CLASSES.SELECTED_ROW);
      row.classList.remove(CONSTANTS.CLASSES.SELECTED_UNAVAILABLE);
    }
    quantityInput.disabled = false;

    if (parseInt(quantityInput.value) === 0) {
      quantityInput.value = 1;
      selectedFoods[foodId] = 1;
      const price = parseFloat(
        row.querySelector("td:nth-child(8)").textContent
      );
      updateTotalPrice(row, 1, price);
    } else {
      selectedFoods[foodId] = parseInt(quantityInput.value);
    }
  } else {
    row.classList.remove(
      CONSTANTS.CLASSES.SELECTED_ROW,
      CONSTANTS.CLASSES.SELECTED_UNAVAILABLE
    );
    quantityInput.disabled = true;
    quantityInput.value = 0;
    updateTotalPrice(row, 0, 0);
    delete selectedFoods[foodId];
  }

  updateFoodQuantitiesInput();
  calculateDiscount();
}

function updateQuantity(input, foodId, price) {
  const quantity = parseInt(input.value) || 0;
  const row = input.closest("tr");
  foodId = parseInt(foodId);

  updateTotalPrice(row, quantity, price);

  if (quantity > 0) {
    selectedFoods[foodId] = quantity;
  } else {
    delete selectedFoods[foodId];
    const checkbox = row.querySelector('input[type="checkbox"]');
    checkbox.checked = false;
    row.classList.remove(CONSTANTS.CLASSES.SELECTED_ROW);
    input.disabled = true;
  }

  updateFoodQuantitiesInput();
  calculateDiscount();
}

function updateFoodQuantitiesInput() {
  document.querySelector(CONSTANTS.SELECTORS.FOOD_QUANTITIES).value =
    handleFoodQuantities();
}

function redirectToPage(pageNumber) {
  const url = new URL(window.location.href);
  url.searchParams.set("foodPage", pageNumber);
  url.searchParams.set("foodQuantities", handleFoodQuantities());
  window.location.href = url.toString();
}

// Initialize
document.addEventListener("DOMContentLoaded", function () {
  // Khởi tạo selectedFoods object
  selectedFoods = {};

  // Khởi tạo preview ảnh
  const imageUrl = document.querySelector(CONSTANTS.SELECTORS.IMAGE_URL).value;
  if (imageUrl?.trim()) {
    document.querySelector(CONSTANTS.SELECTORS.IMAGE_PREVIEW).src = imageUrl;
  }

  // Ưu tiên khởi tạo từ URL parameters trước
  const foodQuantities = getFoodQuantities();
  if (foodQuantities) {
    foodQuantities.split(",").forEach((pair) => {
      const [foodId, quantity] = pair.split("-").map(Number);
      if (!isNaN(foodId) && !isNaN(quantity)) {
        selectedFoods[foodId] = quantity;
      }
    });
  } else {
    // Nếu không có URL parameters, mới khởi tạo từ DOM
    document.querySelectorAll("tr[data-food-id]").forEach((row) => {
      const checkbox = row.querySelector('input[type="checkbox"]');
      const quantityInput = row.querySelector(".quantity-input");
      if (checkbox?.checked && quantityInput) {
        const foodId = parseInt(row.getAttribute("data-food-id"));
        const quantity = parseInt(quantityInput.value) || 0;
        if (!isNaN(foodId) && !isNaN(quantity) && quantity > 0) {
          selectedFoods[foodId] = quantity;
        }
      }
    });
  }

  // Cập nhật UI
  updateUI();

  // Xử lý form submit
  document
    .querySelector(CONSTANTS.SELECTORS.FORM)
    ?.addEventListener("submit", function (e) {
      e.preventDefault();
      document.querySelector(CONSTANTS.SELECTORS.FOOD_QUANTITIES).value =
        handleFoodQuantities();
      this.submit();
    });
});
