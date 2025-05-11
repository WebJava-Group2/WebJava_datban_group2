package org.datban.webjava.repositories;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.datban.webjava.models.Combo;
import org.datban.webjava.repositories.base.BaseRepository;

public class ComboRepository extends BaseRepository<Combo, Integer> {

    public ComboRepository(Connection connection) {
        super(connection);
    }

    @Override
    protected String getDisplayQuery() {
        return "SELECT id, name, price, description, status, image_url " +
               "FROM combos";
    }

    @Override
    protected Combo mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        Combo combo = new Combo();
        combo.setId(resultSet.getInt("id"));
        combo.setName(resultSet.getString("name"));
        combo.setPrice(resultSet.getFloat("price"));
        combo.setDescription(resultSet.getString("description"));
        combo.setStatus(resultSet.getString("status"));
        combo.setImageUrl(resultSet.getString("image_url"));
        return combo;
    }

    @Override
    protected String getInsertQuery() {
        return "INSERT INTO combos (name, price, description, status, image_url) VALUES (?, ?, ?, ?, ?)";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE combos SET name = ?, price = ?, description = ?, status = ?, image_url = ? WHERE id = ?";
    }

    @Override
    protected void setEntityParameters(PreparedStatement statement, Combo entity) throws SQLException {
        statement.setString(1, entity.getName());
        statement.setFloat(2, entity.getPrice());
        statement.setString(3, entity.getDescription());
        statement.setString(4, entity.getStatus());
        statement.setString(5, entity.getImageUrl());
    }
}
