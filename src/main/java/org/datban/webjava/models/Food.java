package org.datban.webjava.models;

import java.sql.Timestamp;
import org.datban.webjava.models.base.IBaseModel;

public class Food implements IBaseModel<Integer> {
    private Integer id;
    private String name;
    private String description;
    private Float price;
    private String imageUrl;
    private String status;
    private Integer createdBy;
    private Timestamp createdAt;
    private String mealType;
    // temp fields
    private Integer quantity;
    private Float totalPrice;
    private Boolean isMainFood;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getMealType() {
        return mealType;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Boolean getIsMainFood() {
        return isMainFood;
    }

    public void setIsMainFood(Boolean isMainFood) {
        this.isMainFood = isMainFood;
    }
}
