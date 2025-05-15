package org.datban.webjava.services;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import org.datban.webjava.models.Reservation;
import org.datban.webjava.repositories.FoodRepository;
import org.datban.webjava.repositories.ReservationRepository;
import org.datban.webjava.helpers.DatabaseConnector;
import org.datban.webjava.models.User;
import org.datban.webjava.repositories.UserRepository;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.sql.Timestamp;

public class ReservationService {
    private  ReservationRepository reservationRepository;
    private UserRepository userRepository;

    public ReservationService() {
        try {
            Connection connection = DatabaseConnector.getConnection();
            this.reservationRepository = new ReservationRepository(connection);
            this.userRepository = new UserRepository(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Timestamp combineDateTime(String date, String time) {
        try {
            // Kiểm tra null hoặc rỗng
            if (date == null || date.trim().isEmpty() || time == null || time.trim().isEmpty()) {
                return null;
            }

            // Kiểm tra định dạng ngày và giờ
            if (!date.matches("\\d{4}-\\d{2}-\\d{2}") || !time.matches("\\d{2}:\\d{2}")) {
                return null;
            }

            // Kết hợp ngày và giờ thành một chuỗi datetime
            String dateTimeStr = date + " " + time;
            // Parse chuỗi thành timestamp
            return Timestamp.valueOf(dateTimeStr + ":00");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean createReservation(String name, String email, String phone, 
                                   String date, String time, int numberOfPeople, 
                                   String orderDetails) {
        try {
            // Kiểm tra các trường bắt buộc
            if (name == null || name.trim().isEmpty() ||
                email == null || email.trim().isEmpty() ||
                phone == null || phone.trim().isEmpty() ||
                date == null || date.trim().isEmpty() ||
                time == null || time.trim().isEmpty() ||
                orderDetails == null || orderDetails.trim().isEmpty() ||
                numberOfPeople <= 0) {
                return false;
            }

            // 1. Tạo user mới với role client
            User user = new User();
            user.setName(name.trim());
            user.setEmail(email.trim());
            user.setPhone(phone.trim());
            user.setRole("client"); // Set role mặc định là client
            user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            
            int userId = userRepository.createUser(user);
            if (userId == -1) {
                return false;
            }

            // 2. Tính tổng tiền và tạo danh sách món ăn
            List<OrderItem> orderItems = parseOrderDetails(orderDetails.trim());
            if (orderItems.isEmpty()) {
                return false;
            }

            double total = 0.0;
            for (OrderItem item : orderItems) {
                int foodId = reservationRepository.getFoodIdByName(item.foodName);
                if (foodId != -1) {
                    double price = reservationRepository.getFoodPrice(foodId);
                    total += price * item.quantity;
                }
            }

            // 3. Tạo reservation với timestamp đã gộp
            Timestamp reservationDateTime = combineDateTime(date.trim(), time.trim());
            if (reservationDateTime == null) {
                return false;
            }

            int reservationId = reservationRepository.createReservation(
                userId,
                reservationDateTime,
                numberOfPeople,
                orderDetails.trim(),
                total
            );

            if (reservationId == -1) {
                return false;
            }

            // 4. Lưu chi tiết món ăn
            for (OrderItem item : orderItems) {
                int foodId = reservationRepository.getFoodIdByName(item.foodName);
                if (foodId != -1) {
                    reservationRepository.createReservationFood(reservationId, foodId, item.quantity);
                }
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private List<OrderItem> parseOrderDetails(String orderDetails) {
        List<OrderItem> items = new ArrayList<>();
        String[] lines = orderDetails.split("\\n");
        Pattern pattern = Pattern.compile("(.*?)\\s*-\\s*(\\d+)\\s*x");

        for (String line : lines) {
            Matcher matcher = pattern.matcher(line.trim());
            if (matcher.find()) {
                String foodName = matcher.group(1).trim();
                int quantity = Integer.parseInt(matcher.group(2));
                items.add(new OrderItem(foodName, quantity));
            }
        }
        return items;
    }

    private static class OrderItem {
        String foodName;
        int quantity;

        public OrderItem(String foodName, int quantity) {
            this.foodName = foodName;
            this.quantity = quantity;
        }
    }

    public List<Reservation> getAllReservations() throws SQLException {
        return reservationRepository.getAll();
    }
}
