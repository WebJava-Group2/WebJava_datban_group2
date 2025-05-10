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
        return "SELECT id, customer_id, rating, content, created_at " +
               "FROM reviews";
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
    protected String getUpdateQuery() {
        return "UPDATE reviews SET customer_id = ?, rating = ?, content = ? WHERE id = ?";
    }

    @Override
    protected void setEntityParameters(PreparedStatement statement, Reviews entity) throws SQLException {
        statement.setInt(1, entity.getCustomerId());
        statement.setInt(2, entity.getRating());
        statement.setString(3, entity.getContent());
        statement.setTimestamp(4, entity.getCreatedAt());
    }
}
