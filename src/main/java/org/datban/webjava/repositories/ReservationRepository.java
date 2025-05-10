package org.datban.webjava.repositories;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.datban.webjava.models.Reservation;
import org.datban.webjava.repositories.base.BaseRepository;

public class ReservationRepository extends BaseRepository<Reservation, Integer> {

    public ReservationRepository(Connection connection) {
        super(connection);
    }

    @Override
    protected String getDisplayQuery() {
        return "SELECT id, total_people, status, reservation_at, note, total_price, created_at, customer_id " +
               "FROM reservations";
    }

    @Override
    protected Reservation mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        Reservation reservation = new Reservation();
        reservation.setId(resultSet.getInt("id"));
        reservation.setTotalPeople(resultSet.getInt("total_people"));
        reservation.setStatus(resultSet.getString("status"));
        reservation.setReservationAt(resultSet.getTimestamp("reservation_at"));
        reservation.setNote(resultSet.getString("note"));
        reservation.setTotalPrice(resultSet.getFloat("total_price"));
        reservation.setCreatedAt(resultSet.getTimestamp("created_at"));
        reservation.setCustomerId(resultSet.getInt("customer_id"));
        return reservation;
    }

    @Override
    protected String getInsertQuery() {
        return "INSERT INTO reservations (total_people, status, reservation_at, note, total_price, created_at, customer_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE reservations SET total_people = ?, status = ?, reservation_at = ?, note = ?, total_price = ?, customer_id = ? WHERE id = ?";
    }

    @Override
    protected void setEntityParameters(PreparedStatement statement, Reservation entity) throws SQLException {
        statement.setInt(1, entity.getTotalPeople());
        statement.setString(2, entity.getStatus());
        statement.setTimestamp(3, entity.getReservationAt());
        statement.setString(4, entity.getNote());
        statement.setFloat(5, entity.getTotalPrice());
        statement.setTimestamp(6, entity.getCreatedAt());
        statement.setInt(7, entity.getCustomerId());
    }
}
