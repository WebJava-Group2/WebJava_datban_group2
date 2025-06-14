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
          <div class="card-body">Đơn đặt bàn hôm nay</div>
          <div class="card-footer d-flex align-items-center justify-content-between">
            <div id="todayReservations" class="small text-white">Loading...</div>
            <div class="small text-white">
              <i class="fas fa-calendar-day"></i>
            </div>
          </div>
        </div>
      </div>
      <div class="col-xl-3 col-md-6">
        <div class="card bg-success text-white mb-4">
          <div class="card-body">Đơn đặt bàn đang chờ</div>
          <div class="card-footer d-flex align-items-center justify-content-between">
            <div id="pendingReservations" class="small text-white">Loading...</div>
            <div class="small text-white">
              <i class="fas fa-clock"></i>
            </div>
          </div>
        </div>
      </div>
      <div class="col-xl-3 col-md-6">
        <div class="card bg-danger text-white mb-4">
          <div class="card-body">Doanh thu</div>
          <div class="card-footer d-flex align-items-center justify-content-between">
            <div id="cancelledReservations" class="small text-white">Loading...</div>
            <div class="small text-white">
              <i class="fas fa-ban"></i>
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
document.addEventListener('DOMContentLoaded', function() {
    // Define an array of colors for the bars
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

    // Fetch monthly revenue data
    fetch('/admin/reservations/monthly-revenue')
        .then(response => response.json())
        .then(data => {
            console.log('Raw data from server:', data);
            const months = Object.keys(data);
            const revenues = Object.values(data);
            
            // Format months to be more readable (e.g., "2024-03" to "Tháng 03/2024")
            const formattedMonths = months.map(month => {
                console.log('Processing month:', month, 'type:', typeof month); // Debugging line
                if (typeof month === 'string' && month.includes('-')) {
                    const splitMonth = month.split('-'); // Store the result of split
                    console.log('Result of split:', splitMonth); // Log the split result
                    const year = parseInt(splitMonth[0], 10);   // Parse as integer
                    const monthNum = parseInt(splitMonth[1], 10); // Parse as integer
                    console.log('Extracted year:', year, 'Extracted monthNum:', monthNum); // NEW LOG

                    // Format month number to always have two digits (e.g., 1 -> 01)
                    const formattedMonthNum = monthNum < 10 ? '0' + monthNum : monthNum.toString();

                    const finalFormattedString = 'Tháng ' + formattedMonthNum + '/' + year;
                    console.log('Final formatted string:', finalFormattedString);
                    return finalFormattedString;
                } else {
                    return `Invalid Month: ${month}`;
                }
            });
            console.log('Formatted months for chart:', formattedMonths);

            // Calculate total revenue
            const totalRevenue = revenues.reduce((sum, revenue) => sum + revenue, 0);
            document.getElementById('totalRevenue').textContent = 
                new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(totalRevenue);

            // Create chart
            const ctx = document.getElementById('revenueChart').getContext('2d');
            new Chart(ctx, {
                type: 'bar',
                data: {
                    labels: formattedMonths,
                    datasets: [{
                        label: 'Doanh thu (VND)',
                        data: revenues,
                        backgroundColor: barColors.slice(0, months.length),  // Use different color for each month
                        borderColor: borderColors.slice(0, months.length),   // Matching border colors
                        borderWidth: 1,
                        borderRadius: 5,  // Rounded corners for bars
                        hoverBackgroundColor: barColors.map(color => color.replace('0.7', '0.9')),  // Darker on hover
                        hoverBorderColor: borderColors
                    }]
                },
                options: {
                    responsive: true,
                    scales: {
                        y: {
                            beginAtZero: true,
                            ticks: {
                                callback: function(value) {
                                    return new Intl.NumberFormat('vi-VN', {
                                        style: 'currency',
                                        currency: 'VND',
                                        maximumFractionDigits: 0
                                    }).format(value);
                                }
                            }
                        }
                    },
                    plugins: {
                        tooltip: {
                            callbacks: {
                                label: function(context) {
                                    return new Intl.NumberFormat('vi-VN', {
                                        style: 'currency',
                                        currency: 'VND'
                                    }).format(context.raw);
                                }
                            }
                        },
                        title: {
                            display: true,
                            text: 'Doanh thu từ các đơn đặt bàn đã hủy theo tháng',
                            font: {
                                size: 16,
                                weight: 'bold'
                            }
                        },
                        legend: {
                            display: false  // Hide legend since we have different colors for each month
                        }
                    }
                }
            });
        })
        .catch(error => {
            console.error('Error fetching revenue data:', error);
            document.getElementById('totalRevenue').textContent = 'Error loading data';
        });

    // TODO: Add other dashboard statistics (today's reservations, pending, cancelled)
});
</script>

<%@ include file="layouts/footer.jsp" %>
