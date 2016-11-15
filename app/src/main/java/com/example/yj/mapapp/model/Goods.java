package com.example.yj.mapapp.model;

import java.io.Serializable;

/**
 * 商品信息
 */
public class Goods implements Serializable {
    private int id;
    private String name;//商品名称
    private String image;//商品图片
    private String type;//商品类别
    private String price;//商品价格
    private String content;//商品详情

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
