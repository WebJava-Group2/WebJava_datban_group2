package org.datban.webjava.services;

import org.datban.webjava.models.ReservationTable;
import org.datban.webjava.models.Reservation;
import org.datban.webjava.models.Table;
import org.datban.webjava.repositories.ReservationTableRepository;
import org.datban.webjava.repositories.ReservationRepository;
import org.datban.webjava.repositories.TableRepository;

import java.sql.SQLException;
import java.util.List;

public class ReservationTableService {
    private ReservationTableRepository reservationTableRepository;
    private ReservationRepository reservationRepository;
    private TableRepository tableRepository;

    public ReservationTableService(ReservationTableRepository reservationTableRepository,
                                 ReservationRepository reservationRepository,
                                 TableRepository tableRepository) {
        this.reservationTableRepository = reservationTableRepository;
        this.reservationRepository = reservationRepository;
        this.tableRepository = tableRepository;
    }

    public void assignTableToReservation(int reservationId, int tableId) throws SQLException {
        // Kiểm tra xem bàn có tồn tại và có trống không
        Table table = tableRepository.getById(tableId);
        if (table == null) {
            throw new SQLException("Bàn không tồn tại");
        }
        if (!"available".equals(table.getStatus())) {
            throw new SQLException("Bàn không còn trống");
        }

        // (MỚI) Kiểm tra xem bàn đã được gán cho một đặt bàn đang hoạt động nào khác chưa
        if (reservationTableRepository.isTableAssignedToActiveReservation(tableId)) {
            throw new SQLException("Bàn này đã được gán cho một đặt bàn khác đang hoạt động.");
        }

        // Kiểm tra xem đặt bàn có tồn tại không
        Reservation reservation = reservationRepository.getById(reservationId);
        if (reservation == null) {
            throw new SQLException("Đặt bàn không tồn tại");
        }

        // Kiểm tra xem đặt bàn đã được gán cho bàn nào chưa
        List<ReservationTable> existingAssignments = reservationTableRepository.getByReservationId(reservationId);
        if (!existingAssignments.isEmpty()) {
            throw new SQLException("Đặt bàn đã được gán cho một bàn khác");
        }

        // Lưu thông tin vào bảng trung gian
        ReservationTable reservationTable = new ReservationTable(null, reservationId, tableId);
        reservationTableRepository.insert(reservationTable);

        // Cập nhật trạng thái đặt bàn thành "confirmed"
        reservation.setStatus("confirmed");
        reservationRepository.update(reservation);

        // Cập nhật trạng thái bàn thành "reserved"
        table.setStatus("reserved");
        tableRepository.update(table);
    }

    public List<ReservationTable> getReservationTablesByReservationId(int reservationId) throws SQLException {
        return reservationTableRepository.getByReservationId(reservationId);
    }

    public List<ReservationTable> getReservationTablesByTableId(int tableId) throws SQLException {
        return reservationTableRepository.getByTableId(tableId);
    }

    public void removeTableFromReservation(int reservationId) throws SQLException {
        // Lấy danh sách bàn đã gán cho đặt bàn
        List<ReservationTable> reservationTables = reservationTableRepository.getByReservationId(reservationId);
        
        // Xóa các bản ghi trong bảng trung gian
        reservationTableRepository.deleteByReservationId(reservationId);

        // Cập nhật trạng thái của các bàn thành "available"
        for (ReservationTable rt : reservationTables) {
            Table table = tableRepository.getById(rt.getTableId());
            if (table != null) {
                table.setStatus("available");
                tableRepository.update(table);
            }
        }

        // (MỚI) Xóa các mục liên quan trong reservation_foods và reservation_combos
        reservationRepository.deleteReservationFoodByReservationId(reservationId);
        reservationRepository.deleteReservationComboByReservationId(reservationId);

        // Xóa đặt bàn chính
        reservationRepository.delete(reservationId);
    }
} 