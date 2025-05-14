package org.datban.webjava.models;

import org.datban.webjava.models.base.IBaseModel;

public class Table implements IBaseModel<Integer> {
    private Integer id;
    private String name;
    private Integer capacity;
    private String status;
    private String location;

    // Constructor mặc định
    public Table() {
    }

    // Constructor đầy đủ
    public Table(Integer id, String name, Integer capacity, String status, String location) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.status = status;
        this.location = location;
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

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
