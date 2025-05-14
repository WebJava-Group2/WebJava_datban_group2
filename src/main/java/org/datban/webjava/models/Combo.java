package org.datban.webjava.models;

import org.datban.webjava.models.base.IBaseModel;

public class Combo implements IBaseModel<Integer> {
    private Integer id;
    private String name;
    private Float price;
    private String description;
    private String status;
    private String imageUrl;

    // Constructor mặc định
    public Combo() {
    }

    // Constructor đầy đủ
    public Combo(Integer id, String name, Float price, String description, String status, String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.status = status;
        this.imageUrl = imageUrl;
    }

    // Getters và Setters
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

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
