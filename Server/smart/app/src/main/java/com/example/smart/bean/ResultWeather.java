package com.example.smart.bean;

import com.example.smart.entity.Weather;

import java.util.List;

public class ResultWeather {
    private Integer status;
    private Integer count;
    private String info;
    private String infocode;
    private List<Weather> lives;

    public Integer getStatus() {
        return status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getInfocode() {
        return infocode;
    }

    public void setInfocode(String infocode) {
        this.infocode = infocode;
    }

    public List<Weather> getLives() {
        return lives;
    }

    public void setLives(List<Weather> lives) {
        this.lives = lives;
    }
}
