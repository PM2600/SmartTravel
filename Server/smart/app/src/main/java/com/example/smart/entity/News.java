package com.example.smart.entity;

import java.sql.Date;

public class News {
    private int nid;
    private String title;
    private String content;
    private String imgUrl;
    private int likeNumber;
    private int viewsNumber;
    private String author;
    private String createTime;

    public int getNid() {
        return nid;
    }

    public void setNid(int nid) {
        this.nid = nid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getLikeNumber() {
        return likeNumber;
    }

    public void setLikeNumber(int likeNumber) {
        this.likeNumber = likeNumber;
    }

    public int getViewsNumber() {
        return viewsNumber;
    }

    public void setViewsNumber(int viewsNumber) {
        this.viewsNumber = viewsNumber;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public News(int nid, String title, String content, String imgUrl, int likeNumber, int viewsNumber, String author, String createTime) {
        this.nid = nid;
        this.title = title;
        this.content = content;
        this.imgUrl = imgUrl;
        this.likeNumber = likeNumber;
        this.viewsNumber = viewsNumber;
        this.author = author;
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "News{" +
                "nid=" + nid +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", likeNumber=" + likeNumber +
                ", viewsNumber=" + viewsNumber +
                ", author='" + author + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}
