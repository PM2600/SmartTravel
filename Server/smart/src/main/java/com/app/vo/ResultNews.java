package com.app.vo;

import com.app.entity.News;

import java.sql.Date;
import java.util.List;

public class ResultNews {
    private Integer total;
    private Integer state;
    private String message;
    private List<News> rows;

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

    public List<News> getRows() {
        return rows;
    }

    public void setRows(List<News> rows) {
        this.rows = rows;
    }
}
