package com.moshna.banners.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long banner_Id;
    private String user_agent;
    private String ip_address;
    private Date dateTime;

    public Request() {
    }

    public Request(Long banner_Id, String user_agent, String ip_address, Date dateTime) {
        this.banner_Id = banner_Id;
        this.user_agent = user_agent;
        this.ip_address = ip_address;
        this.dateTime = dateTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBanner_Id() {
        return banner_Id;
    }

    public void setBanner_Id(Long banner_Id) {
        this.banner_Id = banner_Id;
    }

    public String getUser_agent() {
        return user_agent;
    }

    public void setUser_agent(String user_agent) {
        this.user_agent = user_agent;
    }

    public String getIp_address() {
        return ip_address;
    }

    public void setIp_address(String ip_address) {
        this.ip_address = ip_address;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

}
