package org.datban.webjava.models;

import org.datban.webjava.models.base.IBaseModel;

public class ComboFood implements IBaseModel<Integer> {
    private Integer id;
    private Integer comboId;
    private Integer foodId;
    private Integer quantity;

    // Constructor mặc định
    public ComboFood() {
    }

    // Constructor đầy đủ
    public ComboFood(Integer id, Integer comboId, Integer foodId, Integer quantity) {
        this.id = id;
        this.comboId = comboId;
        this.foodId = foodId;
        this.quantity = quantity;
    }

    // Getters và tableId
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getComboId() {
        return comboId;
    }

    public void setComboId(Integer comboId) {
        this.comboId = comboId;
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
