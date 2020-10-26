package com.moshna.banners.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String req_name;
    private boolean deleted;


    public Category() {
    }

    public Category(Long id, String name, String req_name, boolean deleted) {
        this.id = id;
        this.name = name;
        this.req_name = req_name;
        this.deleted = deleted;
    }

    public Category(String name, String req_name) {
        this.name = name;
        this.req_name = req_name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getReq_name() {
        return req_name;
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

    public void setReq_name(String req_name) {
        this.req_name = req_name;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
