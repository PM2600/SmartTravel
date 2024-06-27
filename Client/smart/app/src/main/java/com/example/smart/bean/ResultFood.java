package com.example.smart.bean;

import com.example.smart.entity.Food;
import com.example.smart.entity.Views;

import java.util.List;

public class ResultFood {
    private Integer total;
    private Integer state;
    private String message;
    private List<Food> rows;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Food> getRows() {
        return rows;
    }

    public void setRows(List<Food> rows) {
        this.rows = rows;
    }
}
