import numpy as np
from sklearn.linear_model import LinearRegression
from datetime import datetime, timedelta
import json
import sys

def predict_revenue(historical_data):
    """
    Dự báo doanh thu cho các tháng tiếp theo dựa trên dữ liệu lịch sử
    historical_data: dict với key là 'YYYY-MM' và value là doanh thu
    """
    # Kiểm tra nếu không có đủ dữ liệu để huấn luyện mô hình
    if len(historical_data) < 2:
        # Trả về 0 cho tất cả các tháng dự báo nếu không đủ dữ liệu
        result = {}
        # Lấy tháng cuối cùng trong dữ liệu lịch sử hoặc tháng hiện tại nếu không có
        if historical_data:
            last_date_str = max(historical_data.keys())
            last_year, last_month = map(int, last_date_str.split('-'))
        else:
            # Nếu không có dữ liệu, bắt đầu từ tháng hiện tại
            now = datetime.now()
            last_year = now.year
            last_month = now.month
            
        last_date_num = last_year * 12 + last_month

        for i in range(1, 7):
            future_month_num = last_date_num + i
            year = future_month_num // 12
            month = future_month_num % 12
            if month == 0:
                year -= 1
                month = 12
            date_str = f"{year}-{month:02d}"
            result[date_str] = 0.0 # Gán 0 nếu không đủ dữ liệu
        return result

    # Chuyển đổi dữ liệu thành mảng numpy
    dates = []
    revenues = []
    
    # Sắp xếp dữ liệu theo thứ tự thời gian
    sorted_dates = sorted(historical_data.keys())
    for date_str in sorted_dates:
        year, month = map(int, date_str.split('-'))
        # Chuyển đổi tháng thành số tháng từ một mốc thời gian cố định
        date_num = year * 12 + month
        dates.append(date_num)
        revenues.append(historical_data[date_str])
    
    X = np.array(dates).reshape(-1, 1)
    y = np.array(revenues)
    
    # Tạo và huấn luyện mô hình
    model = LinearRegression()
    model.fit(X, y)
    
    # Dự báo cho 6 tháng tiếp theo
    last_date = max(dates)
    future_dates = np.array(range(last_date + 1, last_date + 7)).reshape(-1, 1)
    predictions = model.predict(future_dates)
    
    # Chuyển đổi kết quả thành định dạng mong muốn
    result = {}
    for i, pred in enumerate(predictions):
        future_month = last_date + i + 1
        year = future_month // 12
        month = future_month % 12
        if month == 0:
            year -= 1
            month = 12
        date_str = f"{year}-{month:02d}"
        # Đảm bảo dự báo không âm
        result[date_str] = round(max(0.0, float(pred)), 2)
    
    return result

if __name__ == "__main__":
    # Đọc dữ liệu từ stdin
    input_data = json.loads(sys.stdin.read())
    predictions = predict_revenue(input_data)
    print(json.dumps(predictions)) 