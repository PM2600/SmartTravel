package com.app.vo;

import com.app.entity.Views;

import java.util.List;

public class ResultViews {
    private Integer total;
    private Integer state;
    private String message;
    private List<Views> rows;

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

    public List<Views> getRows() {
        return rows;
    }

    public void setRows(List<Views> rows) {
        this.rows = rows;
    }
}