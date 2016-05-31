package com.example.yj.mapapp.model;

public class RelatedCompanies {
    /**
     * "ID": 3,
     * "NAME": "测试设备企业3",
     * "CONTACTS": "张三",
     * "PHONE": "18888888888",
     * "ADDRESS": "测试设备企业地址",
     * "AREA": "金山区"
     */

    private int id;
    private String name;
    private String area;
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
