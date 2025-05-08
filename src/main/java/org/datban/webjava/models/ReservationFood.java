package org.datban.webjava.models;

public class ReservationFood {
    private Integer id;
    private Integer reservationId;
    private Integer foodId;
    private Integer quantity;

    // Constructor mặc định
    public ReservationFood() {
    }

    // Constructor đầy đủ
    public ReservationFood(Integer id, Integer reservationId, Integer foodId, Integer quantity) {
        this.id = id;
        this.reservationId = reservationId;
        this.foodId = foodId;
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

    public Integer getFoodId() {
        return foodId;
    }

    public void setFoodId(Integer foodId) {
        this.foodId = foodId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
