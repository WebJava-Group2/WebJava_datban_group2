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
import org.datban.webjava.helpers.HashedHelper;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.sql.Timestamp;

public class ReservationService {
    private ReservationRepository reservationRepository;
    private UserRepository userRepository;
    private HashedHelper hashedHelper;

    public ReservationService() {
        try {
            Connection connection = DatabaseConnector.getConnection();
            this.reservationRepository = new ReservationRepository(connection);
            this.userRepository = new UserRepository(connection);
            this.hashedHelper = new HashedHelper();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Timestamp combineDateTime(String date, String time) {
        try {
            if (date == null || date.trim().isEmpty() || time == null || time.trim().isEmpty()) {
                return null;
            }

            if (!date.matches("\\d{4}-\\d{2}-\\d{2}") || !time.matches("\\d{2}:\\d{2}")) {
                return null;
            }

            String dateTimeStr = date + " " + time;
            return Timestamp.valueOf(dateTimeStr + ":00");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean createReservation(String name, String email, String phone, 
                                   String date, String time, int numberOfPeople, 
                                   String orderDetails, String orderType) {
        Connection connection = null;
        try {
            connection = DatabaseConnector.getConnection();
            // Bắt đầu transaction
            connection.setAutoCommit(false);

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

            // Tìm user hiện có hoặc tạo mới
            int userId;
            User existingUser = userRepository.findByEmailOrPhone(email.trim(), phone.trim());
            
            if (existingUser != null) {
                userId = existingUser.getId();
                if (!existingUser.getName().equals(name.trim())) {
                    existingUser.setName(name.trim());
                    userRepository.updateUser(existingUser);
                }
            } else {
                User newUser = new User();
                newUser.setName(name.trim());
                newUser.setEmail(email.trim());
                newUser.setPhone(phone.trim());
                newUser.setRole("customer");
                newUser.setCreatedAt(new Timestamp(System.currentTimeMillis()));
                String hashedPassword = hashedHelper.hashPassword(phone.trim());
                newUser.setPassword(hashedPassword);
                
                userId = userRepository.createUser(newUser);
                if (userId == -1) {
                    throw new SQLException("Không thể tạo người dùng mới");
                }
            }

            double total = 0.0;
            List<OrderItem> orderItems = parseOrderDetails(orderDetails.trim());
            if (orderItems.isEmpty()) {
                throw new SQLException("Danh sách món ăn không hợp lệ");
            }

            // Tính tổng tiền dựa vào loại đơn hàng
            if ("combo".equals(orderType)) {
                for (OrderItem item : orderItems) {
                    int comboId = reservationRepository.getComboIdByName(item.foodName);
                    if (comboId != -1) {
                        double price = reservationRepository.getComboPrice(comboId);
                        total += price * item.quantity;
                    }
                }
            } else {
                for (OrderItem item : orderItems) {
                    int foodId = reservationRepository.getFoodIdByName(item.foodName);
                    if (foodId != -1) {
                        double price = reservationRepository.getFoodPrice(foodId);
                        total += price * item.quantity;
                    }
                }
            }

            // Tạo reservation với timestamp đã gộp
            Timestamp reservationDateTime = combineDateTime(date.trim(), time.trim());
            if (reservationDateTime == null) {
                throw new SQLException("Thời gian đặt bàn không hợp lệ");
            }

            int reservationId = reservationRepository.createReservation(
                userId,
                reservationDateTime,
                numberOfPeople,
                orderDetails.trim(),
                total
            );

            if (reservationId == -1) {
                throw new SQLException("Không còn bàn trống trong thời gian này");
            }

            // Lưu chi tiết đơn hàng dựa vào loại
            if ("combo".equals(orderType)) {
                for (OrderItem item : orderItems) {
                    int comboId = reservationRepository.getComboIdByName(item.foodName);
                    if (comboId != -1) {
                        reservationRepository.createReservationCombo(reservationId, comboId, item.quantity);
                    }
                }
            } else {
                for (OrderItem item : orderItems) {
                    int foodId = reservationRepository.getFoodIdByName(item.foodName);
                    if (foodId != -1) {
                        reservationRepository.createReservationFood(reservationId, foodId, item.quantity);
                    }
                }
            }

            // Commit transaction nếu mọi thứ OK
            connection.commit();
            return true;
        } catch (Exception e) {
            // Rollback transaction nếu có lỗi
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            // Reset auto commit và đóng connection
            try {
                if (connection != null) {
                    connection.setAutoCommit(true);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
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
