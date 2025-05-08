package org.datban.webjava.models;

import java.sql.Timestamp;

public class Reviews {
    private Integer id;
    private Integer customerId;
    private Integer rating;
    private String content;
    private Timestamp createdAt;

    // Constructor mặc định
    public Reviews() {
    }

    // Constructor đầy đủ
    public Reviews(Integer id, Integer customerId, Integer rating, String content, Timestamp createdAt) {
        this.id = id;
        this.customerId = customerId;
        this.rating = rating;
        this.content = content;
        this.createdAt = createdAt;
    }

    // Getters và Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
