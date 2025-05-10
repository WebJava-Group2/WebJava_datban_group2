package org.datban.webjava.services;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import org.datban.webjava.models.Reviews;
import org.datban.webjava.repositories.ReviewRepository;
import org.datban.webjava.helpers.DatabaseConnector;

public class ReviewService {
    private ReviewRepository reviewRepository;

    public ReviewService() {
        try {
            Connection connection = DatabaseConnector.getConnection();
            this.reviewRepository = new ReviewRepository(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Reviews> getAllReviews() throws SQLException {
        return reviewRepository.getAll();
    }
}
