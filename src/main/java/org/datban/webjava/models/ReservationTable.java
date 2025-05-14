package org.datban.webjava.models;

import org.datban.webjava.models.base.IBaseModel;

public class ReservationTable implements IBaseModel<Integer> {
    private Integer id;
    private Integer reservationId;
    private Integer tableId;

    // Constructor mặc định
    public ReservationTable() {
    }

    // Constructor đầy đủ
    public ReservationTable(Integer id, Integer reservationId, Integer tableId) {
        this.id = id;
        this.reservationId = reservationId;
        this.tableId = tableId;
    }

    // Getters và Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getReservationId() {
        return reservationId;
    }

    public void setReservationId(Integer reservationId) {
        this.reservationId = reservationId;
    }

    public Integer getTableId() {
        return tableId;
    }

    public void setTableId(Integer tableId) {
        this.tableId = tableId;
    }
}
