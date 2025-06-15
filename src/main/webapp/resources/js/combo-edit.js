// Constants
const CONSTANTS = {
  CURRENCY: "VNĐ",
  DEFAULT_IMAGE: "https://picsum.photos/300/300?random=${combo.id}",
  SELECTORS: {
    FORM: "#editForm",
    IMAGE_PREVIEW: "#imagePreview",
    IMAGE_URL: "#imageUrl",
    FOOD_QUANTITIES: "#foodQuantities",
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
