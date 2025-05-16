package org.datban.webjava.models;

import java.sql.Timestamp;
import org.datban.webjava.models.base.IBaseModel;

public class Reservation implements IBaseModel<Integer> {
    private Integer id;
    private Integer totalPeople;
    private String status;
    private Timestamp reservationAt;
    private String note;
    private Float totalPrice;
    private Timestamp createdAt;
    private Integer customerId;
    private Integer tableId;

    // Constructor mặc định
    public Reservation() {
    }

    // Constructor đầy đủ
    public Reservation(Integer id, Integer totalPeople, String status, Timestamp reservationAt, 
                      String note, Float totalPrice, Timestamp createdAt, Integer customerId, Integer tableId) {
        this.id = id;
        this.totalPeople = totalPeople;
        this.status = status;
        this.reservationAt = reservationAt;
        this.note = note;
        this.totalPrice = totalPrice;
        this.createdAt = createdAt;
        this.customerId = customerId;
        this.tableId = tableId;
    }

    // Getters và Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTotalPeople() {
        return totalPeople;
    }

    public void setTotalPeople(Integer totalPeople) {
        this.totalPeople = totalPeople;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getReservationAt() {
        return reservationAt;
    }

    public void setReservationAt(Timestamp reservationAt) {
        this.reservationAt = reservationAt;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getTableId() {
        return tableId;
    }

    public void setTableId(Integer tableId) {
        this.tableId = tableId;
    }
}
