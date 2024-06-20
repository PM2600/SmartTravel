package com.example.smart.bean;

public class ResultBean {

    private String state;
    private String message;
    private String data;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public ResultBean(String state, String message, String data) {
        this.state = state;
        this.message = message;
        this.data = data;
    }

}