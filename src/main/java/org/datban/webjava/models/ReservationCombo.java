package org.datban.webjava.models;

import org.datban.webjava.models.base.IBaseModel;

public class ReservationCombo implements IBaseModel<Integer> {
    private Integer id;
    private Integer reservationId;
    private Integer comboId;
    private Integer quantity;

    // Constructor mặc định
    public ReservationCombo() {
    }

    // Constructor đầy đủ
    public ReservationCombo(Integer id, Integer reservationId, Integer comboId, Integer quantity) {
        this.id = id;
        this.reservationId = reservationId;
        this.comboId = comboId;
        this.quantity = quantity;
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

    public Integer getComboId() {
        return comboId;
    }

    public void setComboId(Integer comboId) {
        this.comboId = comboId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
