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
    protected String getUpdateQuery(Integer id) {
        return "UPDATE reservations SET total_people = ?, status = ?, reservation_at = ?, note = ?, total_price = ?, customer_id = ? WHERE id = " + id;
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

    @Override
    protected String getTableName() {
        return "reservation";
    }

    public List<Reservation> getReservationsByUserId(int userId) throws SQLException {
        String query = getDisplayQuery() + " WHERE r.user_id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, userId);
        ResultSet resultSet = statement.executeQuery();
        
        List<Reservation> reservations = new ArrayList<>();
        while (resultSet.next()) {
            reservations.add(mapResultSetToEntity(resultSet));
        }
        return reservations;
    }

    public List<Reservation> getReservationsByStatus(String status) throws SQLException {
        String query = getDisplayQuery() + " WHERE r.status = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, status);
        ResultSet resultSet = statement.executeQuery();
        
        List<Reservation> reservations = new ArrayList<>();
        while (resultSet.next()) {
            reservations.add(mapResultSetToEntity(resultSet));
        }
        return reservations;
    }

    public List<Reservation> getReservationsByPage(int page, int itemsPerPage) throws SQLException {
        return getWithPaginate(page, itemsPerPage);
    }

    public int getTotalReservations() throws SQLException {
        String query = "SELECT COUNT(*) FROM " + getTableName();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        
        if (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return 0;
    }

    public int createReservation(int userId, Timestamp reservationDateTime,
                               int numberOfPeople, String note, double total) {
        String sql = "INSERT INTO reservation (user_id, reservation_at, " +
                    "number_of_people, note, total, status) VALUES (?, ?, ?, ?, ?, 'pending')";
        try {
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, userId);
            statement.setTimestamp(2, reservationDateTime);
            statement.setInt(3, numberOfPeople);
            statement.setString(4, note);
            statement.setDouble(5, total);

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating reservation failed, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating reservation failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void createReservationFood(int reservationId, int foodId, int quantity) {
        String sql = "INSERT INTO reservation_food (reservation_id, food_id, quantity) VALUES (?, ?, ?)";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, reservationId);
            statement.setInt(2, foodId);
            statement.setInt(3, quantity);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public double getFoodPrice(int foodId) {
        String sql = "SELECT price FROM food WHERE id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, foodId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getDouble("price");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public int getFoodIdByName(String foodName) {
        String sql = "SELECT id FROM food WHERE name = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, foodName);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
