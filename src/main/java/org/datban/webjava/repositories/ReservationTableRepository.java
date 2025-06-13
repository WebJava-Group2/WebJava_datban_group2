package org.datban.webjava.repositories;

import org.datban.webjava.models.ReservationTable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationTableRepository {
    private Connection connection;

    public ReservationTableRepository(Connection connection) {
        this.connection = connection;
    }

    public void insert(ReservationTable reservationTable) throws SQLException {
        String sql = "INSERT INTO reservation_table (reservation_id, table_id) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, reservationTable.getReservationId());
            stmt.setInt(2, reservationTable.getTableId());
            stmt.executeUpdate();

            // Lấy ID được tạo tự động
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    reservationTable.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public List<ReservationTable> getByReservationId(int reservationId) throws SQLException {
        List<ReservationTable> reservationTables = new ArrayList<>();
        String sql = "SELECT * FROM reservation_table WHERE reservation_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, reservationId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ReservationTable rt = new ReservationTable(
                        rs.getInt("id"),
                        rs.getInt("reservation_id"),
                        rs.getInt("table_id")
                    );
                    reservationTables.add(rt);
                }
            }
        }
        return reservationTables;
    }

    public List<ReservationTable> getByTableId(int tableId) throws SQLException {
        List<ReservationTable> reservationTables = new ArrayList<>();
        String sql = "SELECT * FROM reservation_table WHERE table_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, tableId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ReservationTable rt = new ReservationTable(
                        rs.getInt("id"),
                        rs.getInt("reservation_id"),
                        rs.getInt("table_id")
                    );
                    reservationTables.add(rt);
                }
            }
        }
        return reservationTables;
    }

    // Phương thức mới để kiểm tra bàn đã được gán cho một đặt bàn đang hoạt động hay chưa
    public boolean isTableAssignedToActiveReservation(int tableId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM reservation_table rt " +
                     "JOIN reservations r ON rt.reservation_id = r.id " +
                     "WHERE rt.table_id = ? AND (r.status = 'pending' OR r.status = 'confirmed')";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, tableId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM reservation_table WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public void deleteByReservationId(int reservationId) throws SQLException {
        String sql = "DELETE FROM reservation_table WHERE reservation_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, reservationId);
            stmt.executeUpdate();
        }
    }

    public void deleteByTableId(int tableId) throws SQLException {
        String sql = "DELETE FROM reservation_table WHERE table_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, tableId);
            stmt.executeUpdate();
        }
    }
} 