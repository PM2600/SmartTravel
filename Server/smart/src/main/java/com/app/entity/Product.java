package com.app.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("商品基本信息")
public class Product implements Serializable {
//    @ApiModelProperty(value = "商品id", required = false, example = "1")
//    private Integer id;      // 商品id
//    @ApiModelProperty(value = "创建人姓名", required = false, example = "小明")
//    private String name;     // 创建人姓名


    private Integer pid;      // 商品id
    private String phone;     // 用户电话
    private String text;      // 商品标题
    private Integer type;     // 商品类型 1：销售  2：交换
    private double price;     // 商品价格
    private Integer num;      // 商品数量（斤）
    private String image;     // 图片路径
    private Date time;        // 上传时间
    private String ipaddr;    // 发布IP所在省份地址
    private String introdu;   // 商品一句话简介
    private String introduce; // 商品介绍
    private Integer status;   // 商品状态  1：上架  2：下架  3：删除
    private Integer click;    // 点击量
    private Integer like;     // 点赞量
    private Integer heat;     // 热度


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

    public String getIpaddr() {
        return ipaddr;
    }

    public void setIpaddr(String ipaddr) {
        this.ipaddr = ipaddr;
    }

    public String getIntrodu() {
        return introdu;
    }

    public void setIntrodu(String introdu) {
        this.introdu = introdu;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public Integer getClick() {
        return click;
    }

    public void setClick(Integer click) {
        this.click = click;
    }

    public Integer getLike() {
        return like;
    }

    public void setLike(Integer like) {
        this.like = like;
    }

    public Integer getHeat() {
        return heat;
    }

    public void setHeat(Integer heat) {
        this.heat = heat;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
