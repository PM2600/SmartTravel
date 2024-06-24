package com.app.vo;

import com.app.entity.Dynamic;
import com.app.entity.News;

import java.util.List;

public class ResultDynamic {
    private Integer total;
    private Integer state;
    private String message;
    private List<DynamicView> rows;

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

    public List<DynamicView> getRows() {
        return rows;
    }

    public void setRows(List<DynamicView> rows) {
        this.rows = rows;
    }
}
