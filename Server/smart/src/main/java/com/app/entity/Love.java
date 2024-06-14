package com.app.entity;

import java.io.Serializable;

public class Love implements Serializable {
    private Integer lid;
    private Integer pid;
    private String phone;
    private Integer islove;

    public Integer getLid() {
        return lid;
    }

    public void setLid(Integer lid) {
        this.lid = lid;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getIslove() {
        return islove;
    }

    public void setIslove(Integer islove) {
        this.islove = islove;
    }
}
