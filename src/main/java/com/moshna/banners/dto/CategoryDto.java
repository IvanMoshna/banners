package com.moshna.banners.dto;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class CategoryDto {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String req_name;
    private boolean deleted;

    public CategoryDto() {
    }

    public CategoryDto(String name, String req_name) {
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
