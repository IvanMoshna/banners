package com.moshna.banners.dto;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class BannerDto {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private double price;
    private Long categoryID;
    private String text;
    private boolean deleted;

    public BannerDto() {
    }

    public BannerDto(Long id, String name, double price, Long categoryID, String text, boolean deleted) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.categoryID = categoryID;
        this.text = text;
        this.deleted = deleted;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public Long getCategoryID() {
        return categoryID;
    }

    public String getText() {
        return text;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setCategoryID(Long categoryID) {
        this.categoryID = categoryID;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
