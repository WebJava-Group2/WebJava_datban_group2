<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8" %> <%@ include file="layouts/header.jsp" %> <%@ include
file="layouts/sidebar.jsp" %>
<main>
  <div class="container-fluid px-4">
    <h1 class="mt-4">Dashboard</h1>
    <ol class="breadcrumb mb-4">
      <li class="breadcrumb-item active">Dashboard</li>
    </ol>
    <div class="row">
      <div class="col-xl-3 col-md-6">
        <div class="card bg-primary text-white mb-4">
          <div class="card-body">Tổng doanh thu</div>
          <div class="card-footer d-flex align-items-center justify-content-between">
            <div id="totalRevenue" class="small text-white">Loading...</div>
            <div class="small text-white">
              <i class="fas fa-money-bill-wave"></i>
            </div>
          </div>
        </div>
      </div>
      <div class="col-xl-3 col-md-6">
        <div class="card bg-warning text-white mb-4">
          <div class="card-body">Dự báo tháng tiếp theo</div>
          <div class="card-footer d-flex align-items-center justify-content-between">
            <div id="predictionMonth1" class="small text-white">Loading...</div>
            <div class="small text-white">
              <i class="fas fa-chart-line"></i>
            </div>
          </div>
        </div>
      </div>
      <div class="col-xl-3 col-md-6">
        <div class="card bg-success text-white mb-4">
          <div class="card-body">Dự báo 2 tháng tiếp theo</div>
          <div class="card-footer d-flex align-items-center justify-content-between">
            <div id="predictionMonth2" class="small text-white">Loading...</div>
            <div class="small text-white">
              <i class="fas fa-chart-line"></i>
            </div>
          </div>
        </div>
      </div>
      <div class="col-xl-3 col-md-6">
        <div class="card bg-danger text-white mb-4">
          <div class="card-body">Dự báo 3 tháng tiếp theo</div>
          <div class="card-footer d-flex align-items-center justify-content-between">
            <div id="predictionMonth3" class="small text-white">Loading...</div>
            <div class="small text-white">
              <i class="fas fa-chart-line"></i>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="row">
      <div class="col-xl-12">
        <div class="card mb-4">
          <div class="card-header">
            <i class="fas fa-chart-bar me-1"></i>
            Doanh thu các đơn theo tháng
          </div>
          <div class="card-body">
            <canvas id="revenueChart" width="100%" height="40"></canvas>
          </div>
        </div>
      </div>
    </div>
  </div>
</main>

<script>
let revenueChart;

// Define an array of colors for the bars in a global scope
const barColors = [
    'rgba(255, 99, 132, 0.7)',   // Red
    'rgba(54, 162, 235, 0.7)',   // Blue
    'rgba(255, 206, 86, 0.7)',   // Yellow
    'rgba(75, 192, 192, 0.7)',   // Teal
    'rgba(153, 102, 255, 0.7)',  // Purple
    'rgba(255, 159, 64, 0.7)',   // Orange
    'rgba(199, 199, 199, 0.7)',  // Gray
    'rgba(83, 102, 255, 0.7)',   // Indigo
    'rgba(40, 159, 64, 0.7)',    // Green
    'rgba(210, 199, 199, 0.7)',  // Light Gray
    'rgba(78, 205, 196, 0.7)',   // Turquoise
    'rgba(255, 99, 255, 0.7)'    // Pink
];

const borderColors = barColors.map(color => color.replace('0.7', '1'));

document.addEventListener('DOMContentLoaded', function() {
    // Fetch historical revenue data
    fetch('${pageContext.request.contextPath}/admin/dashboard/monthly-revenue')
        .then(response => response.json())
        .then(data => {
            console.log("Historical monthly revenue data received:", data); // Debug log
            updateTotalRevenue(data);
            createRevenueChart(data);
        })
        .catch(error => console.error('Error fetching monthly revenue:', error));

    // Fetch prediction data
    fetch('${pageContext.request.contextPath}/admin/dashboard/predict-revenue')
        .then(response => response.json())
        .then(data => {
            console.log("Prediction data received:", data); // Debug log
            updatePredictionBoxes(data);
        })
        .catch(error => console.error('Error fetching prediction:', error));
});

function updateTotalRevenue(data) {
    const totalRevenue = Object.values(data).reduce((a, b) => a + b, 0);
    document.getElementById('totalRevenue').textContent = 
        new Intl.NumberFormat('vi-VN', {
            style: 'currency',
            currency: 'VND'
        }).format(totalRevenue);
}

function updatePredictionBoxes(predictionData) {
    console.log("Entering updatePredictionBoxes. predictionData:", predictionData); // Debug log
    const predictionMonths = Object.keys(predictionData).sort();
    console.log("predictionMonths (sorted keys):", predictionMonths); // Debug log

    if (predictionMonths.length >= 1) {
        const month1 = predictionMonths[0];
        const value1 = predictionData[month1];
        console.log(`Prediction for ${month1}: ${value1}`); // Debug log
        document.getElementById('predictionMonth1').textContent = 
            new Intl.NumberFormat('vi-VN', {
                style: 'currency',
                currency: 'VND'
            }).format(value1);
    }

    if (predictionMonths.length >= 2) {
        const month2 = predictionMonths[1];
        const value2 = predictionData[month2];
        console.log(`Prediction for ${month2}: ${value2}`); // Debug log
        document.getElementById('predictionMonth2').textContent = 
            new Intl.NumberFormat('vi-VN', {
                style: 'currency',
                currency: 'VND'
            }).format(value2);
    }

    if (predictionMonths.length >= 3) {
        const month3 = predictionMonths[2];
        const value3 = predictionData[month3];
        console.log(`Prediction for ${month3}: ${value3}`); // Debug log
        document.getElementById('predictionMonth3').textContent = 
            new Intl.NumberFormat('vi-VN', {
                style: 'currency',
                currency: 'VND'
            }).format(value3);
    }
}

function createRevenueChart(data) {
    console.log("Entering createRevenueChart. data:", data); // Debug log
    const ctx = document.getElementById('revenueChart').getContext('2d');
    const labels = Object.keys(data);
    const values = Object.values(data);
    console.log("Chart Labels:", labels); // Debug log
    console.log("Chart Values:", values); // Debug log

    revenueChart = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: labels,
            datasets: [{
                label: 'Doanh thu thực tế',
                data: values,
                backgroundColor: barColors.slice(0, values.length),
                borderColor: borderColors.slice(0, values.length),
                borderWidth: 1
            }]
        },
        options: {
            responsive: true,
            scales: {
                y: {
                    beginAtZero: true,
                    title: {
                        display: true,
                        text: 'Doanh thu (VNĐ)'
                    }
                },
                x: {
                    title: {
                        display: true,
                        text: 'Tháng'
                    }
                }
            },
            plugins: {
                tooltip: {
                    callbacks: {
                        label: function(context) {
                            let label = context.dataset.label || '';
                            if (label) {
                                label += ': ';
                            }
                            if (context.parsed.y !== null) {
                                label += new Intl.NumberFormat('vi-VN', {
                                    style: 'currency',
                                    currency: 'VND'
                                }).format(context.parsed.y);
                            }
                            return label;
                        }
                    }
                }
            }
        }
    });
}
</script>

<%@ include file="layouts/footer.jsp" %>
