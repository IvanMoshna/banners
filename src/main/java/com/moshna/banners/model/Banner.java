package com.moshna.banners.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;


@Entity
public class Banner {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;



    @NotBlank(message = "Please fill the unique name")
    @Column(unique = true, nullable = false)
    private String name;
    @Column(nullable = false)
    private double price;
    @Column(nullable = false)
    private Long categoryID;
    @Column(nullable = false)
    private String text;
    private boolean deleted;

    public Banner() {
    }

    public Banner(Long id, String name, double price, Long categoryID, String text, boolean deleted) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.categoryID = categoryID;
        this.text = text;
        this.deleted = deleted;
    }

    public Banner(String name, double price, Long categoryID, String text) {
        this.name = name;
        this.price = price;
        this.categoryID = categoryID;
        this.text = text;
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
