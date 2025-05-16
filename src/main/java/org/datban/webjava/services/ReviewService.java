package org.datban.webjava.services;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import org.datban.webjava.models.User;
import org.datban.webjava.repositories.UserRepository;
import org.datban.webjava.repositories.ReviewRepository;
import org.datban.webjava.helpers.DatabaseConnector;
import org.datban.webjava.helpers.HashedHelper;
import org.datban.webjava.models.Reviews;

public class ReviewService {
    private ReviewRepository reviewRepository;
    private UserRepository userRepository;
    private HashedHelper hashedHelper;

    public ReviewService() {
        try {
            Connection connection = DatabaseConnector.getConnection();
            this.reviewRepository = new ReviewRepository(connection);
            this.userRepository = new UserRepository(connection);
            this.hashedHelper = new HashedHelper();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean createReview(String name, String email, String phone, String content) {
        Connection connection = null;
        try {
            connection = DatabaseConnector.getConnection();
            // Bắt đầu transaction
            connection.setAutoCommit(false);

            // Kiểm tra các trường bắt buộc
            if (name == null || name.trim().isEmpty() ||
                email == null || email.trim().isEmpty() ||
                phone == null || phone.trim().isEmpty() ||
                content == null || content.trim().isEmpty()) {
                return false;
            }

            // Tìm user hiện có hoặc tạo mới
            int customerId;
            User existingUser = userRepository.findByEmailOrPhone(email.trim(), phone.trim());
            
            if (existingUser != null) {
                // Nếu user đã tồn tại, sử dụng user đó
                customerId = existingUser.getId();
                // Cập nhật thông tin nếu cần
                if (!existingUser.getName().equals(name.trim())) {
                    existingUser.setName(name.trim());
                    userRepository.updateUser(existingUser);
                }
            } else {
                // Tạo user mới nếu chưa tồn tại
                User newUser = new User();
                newUser.setName(name.trim());
                newUser.setEmail(email.trim());
                newUser.setPhone(phone.trim());
                newUser.setRole("customer");
                newUser.setCreatedAt(new Timestamp(System.currentTimeMillis()));
                String hashedPassword = hashedHelper.hashPassword(phone.trim());
                newUser.setPassword(hashedPassword);
                
                customerId = userRepository.createUser(newUser);
                if (customerId == -1) {
                    throw new SQLException("Không thể tạo người dùng mới");
                }
            }

            // Tạo review mới với rating mặc định là 5
            Reviews review = new Reviews();
            review.setCustomerId(customerId);
            review.setRating(5); // Mặc định 5 sao
            review.setContent(content.trim());
            review.setCreatedAt(new Timestamp(System.currentTimeMillis()));

            int reviewId = reviewRepository.createReview(review);
            if (reviewId == -1) {
                throw new SQLException("Không thể tạo đánh giá");
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
}
