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
    # Chuyển đổi dữ liệu thành mảng numpy
    dates = []
    revenues = []
    
    for date_str, revenue in historical_data.items():
        year, month = map(int, date_str.split('-'))
        # Chuyển đổi tháng thành số tháng từ một mốc thời gian cố định
        date_num = year * 12 + month
        dates.append(date_num)
        revenues.append(revenue)
    
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
        result[date_str] = round(float(pred), 2)
    
    return result

if __name__ == "__main__":
    # Đọc dữ liệu từ stdin
    input_data = json.loads(sys.stdin.read())
    predictions = predict_revenue(input_data)
    print(json.dumps(predictions)) 