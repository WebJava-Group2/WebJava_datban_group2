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
        return "reservations";
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

    public int getAvailableTable(Timestamp reservationDateTime, int numberOfPeople) {
        // Tìm bàn trống phù hợp với số người
        String sql = "SELECT t.id, t.capacity FROM tables t " +
                    "WHERE t.status = 'available' " +
                    "AND t.capacity >= ? " +
                    "AND NOT EXISTS (" +
                    "    SELECT 1 FROM reservations r " +
                    "    WHERE r.table_id = t.id " +
                    "    AND r.status != 'cancelled' " +
                    "    AND r.reservation_at BETWEEN DATE_SUB(?, INTERVAL 2 HOUR) AND DATE_ADD(?, INTERVAL 2 HOUR)" +
                    ") " +
                    "ORDER BY t.capacity ASC " +
                    "LIMIT 1";
        try {
            System.out.println("Searching for table with capacity >= " + numberOfPeople);
            System.out.println("For reservation time: " + reservationDateTime);
            
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, numberOfPeople);
            statement.setTimestamp(2, reservationDateTime);
            statement.setTimestamp(3, reservationDateTime);
            
            System.out.println("Executing query: " + statement.toString());
            
            ResultSet rs = statement.executeQuery();
            
            if (rs.next()) {
                int tableId = rs.getInt("id");
                int capacity = rs.getInt("capacity");
                System.out.println("Found table: ID=" + tableId + ", Capacity=" + capacity);
                return tableId;
            } else {
                System.out.println("No available table found");
            }
        } catch (SQLException e) {
            System.out.println("Error finding available table: " + e.getMessage());
            e.printStackTrace();
        }
        return -1; // Không còn bàn trống phù hợp
    }

    public boolean updateTableStatus(int tableId, String status) {
        String sql = "UPDATE tables SET status = ? WHERE id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, status);
            statement.setInt(2, tableId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int createReservation(int userId, Timestamp reservationDateTime,
                               int numberOfPeople, String note, double total) {
        // Tìm bàn trống phù hợp
        int tableId = getAvailableTable(reservationDateTime, numberOfPeople);
        if (tableId == -1) {
            return -1; // Không còn bàn trống phù hợp
        }

        String sql = "INSERT INTO reservations (customer_id, reservation_at, " +
                    "total_people, note, total_price, status, created_at, table_id) " +
                    "VALUES (?, ?, ?, ?, ?, 'pending', NOW(), ?)";
        try {
            // Bắt đầu transaction
            connection.setAutoCommit(false);

            // Tạo reservation
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, userId);
            statement.setTimestamp(2, reservationDateTime);
            statement.setInt(3, numberOfPeople);
            statement.setString(4, note);
            statement.setDouble(5, total);
            statement.setInt(6, tableId);

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                connection.rollback();
                return -1;
            }

            // Cập nhật trạng thái bàn thành 'reserved'
            if (!updateTableStatus(tableId, "reserved")) {
                connection.rollback();
                return -1;
            }

            // Lấy ID của reservation vừa tạo
            ResultSet generatedKeys = statement.getGeneratedKeys();
            int reservationId = -1;
            if (generatedKeys.next()) {
                reservationId = generatedKeys.getInt(1);
            }

            // Commit transaction
            connection.commit();
            return reservationId;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return -1;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
// đẩy dữ liệu vào reservationFood
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
        String sql = "SELECT price FROM foods WHERE id = ?";
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
        String sql = "SELECT id FROM foods WHERE name = ?";
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

    public void createReservationCombo(int reservationId, int comboId, int quantity) {
        String sql = "INSERT INTO reservation_combo (reservation_id, combo_id, quantity) VALUES (?, ?, ?)";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, reservationId);
            statement.setInt(2, comboId);
            statement.setInt(3, quantity);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public double getComboPrice(int comboId) {
        String sql = "SELECT price FROM combos WHERE id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, comboId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getDouble("price");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public int getComboIdByName(String comboName) {
        String sql = "SELECT id FROM combos WHERE name = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, comboName);
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
