package org.datban.webjava.repositories;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.datban.webjava.models.Reviews;
import org.datban.webjava.repositories.base.BaseRepository;

public class ReviewRepository extends BaseRepository<Reviews, Integer> {

    public ReviewRepository(Connection connection) {
        super(connection);
    }

    @Override
    protected String getDisplayQuery() {
        return "SELECT r.id, r.customer_id, r.rating, r.content, r.created_at, " +
               "u.name as customer_name, u.email as customer_email " +
               "FROM reviews r " +
               "LEFT JOIN users u ON r.customer_id = u.id";
    }

    @Override
    protected Reviews mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        Reviews review = new Reviews();
        review.setId(resultSet.getInt("id"));
        review.setCustomerId(resultSet.getInt("customer_id"));
        review.setRating(resultSet.getInt("rating"));
        review.setContent(resultSet.getString("content"));
        review.setCreatedAt(resultSet.getTimestamp("created_at"));
        return review;
    }

    @Override
    protected String getInsertQuery() {
        return "INSERT INTO reviews (customer_id, rating, content, created_at) VALUES (?, ?, ?, ?)";
    }

    @Override
    protected String getUpdateQuery(Integer id) {
        return "UPDATE reviews SET customer_id = ?, rating = ?, content = ? WHERE id = " + id;
    }

    @Override
    protected void setEntityParameters(PreparedStatement statement, Reviews entity) throws SQLException {
        statement.setInt(1, entity.getCustomerId());
        statement.setInt(2, entity.getRating());
        statement.setString(3, entity.getContent());
    }

    @Override
    protected String getTableName() {
        return "reviews";
    }

    public int createReview(Reviews review) {
        String sql = "INSERT INTO reviews (customer_id, rating, content, created_at) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, review.getCustomerId());
            statement.setInt(2, review.getRating());
            statement.setString(3, review.getContent());
            statement.setTimestamp(4, review.getCreatedAt());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating review failed, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating review failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public List<Reviews> getReviewsByCustomerId(int customerId) throws SQLException {
        String query = getDisplayQuery() + " WHERE r.customer_id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, customerId);
        ResultSet resultSet = statement.executeQuery();
        
        List<Reviews> reviews = new ArrayList<>();
        while (resultSet.next()) {
            reviews.add(mapResultSetToEntity(resultSet));
        }
        return reviews;
    }

    public List<Reviews> getReviewsByPage(int page, int itemsPerPage) throws SQLException {
        return getWithPaginate(page, itemsPerPage);
    }

    public int getTotalReviews() throws SQLException {
        String query = "SELECT COUNT(*) FROM " + getTableName();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        
        if (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return 0;
    }

    public List<Reviews> getLatestReviews(int limit) throws SQLException {
        String query = getDisplayQuery() + " ORDER BY r.created_at DESC LIMIT ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, limit);
        ResultSet resultSet = statement.executeQuery();
        
        List<Reviews> reviews = new ArrayList<>();
        while (resultSet.next()) {
            reviews.add(mapResultSetToEntity(resultSet));
        }
        return reviews;
    }
}
