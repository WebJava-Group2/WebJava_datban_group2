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
        return "SELECT r.id, r.customer_id, r.rating, r.content, r.created_at " +
               "FROM reviews r";
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
        statement.setTimestamp(4, entity.getCreatedAt());
    }

    @Override
    protected String getTableName() {
        return "reviews";
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

    public List<Reviews> getReviewsByRating(int rating) throws SQLException {
        String query = getDisplayQuery() + " WHERE r.rating = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, rating);
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
}
