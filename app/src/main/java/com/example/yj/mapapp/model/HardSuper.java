package com.example.yj.mapapp.model;

public class HardSuper {
    /**
     * "ID": 9,
     * "NAME": "五金市场9",
     * "PARENT_NODE": 1,
     * "CATEGORY": "建材码头",
     * "AREA": "梅陇镇",
     * "IMAGES": "",
     * "ADDRESS": "五金市场地址",
     * "CONTACTS": "张三",
     * "PHONE": "18500000000"
     */

    private int id;
    private String name;
    private String area;
    private String category;
    private String contacts;
    private String phone;
    private String address;

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

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
