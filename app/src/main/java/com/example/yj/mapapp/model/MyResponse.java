package com.example.yj.mapapp.model;

public class MyResponse {

    /**
     * Id : 36
     * sd_name : 五吨黄沙
     * Contacts : 老王
     * Phone : 13652147852
     * Content : 我需要五吨黄沙用于迪士尼工地！越快发货越好！5.20号之前必须送到！
     * USER : null
     * Ctime : 2016-05-12 04:49:21
     * Isintention : false
     */

    private int Id;
    private String sd_name;
    private String Contacts;
    private String Phone;
    private String Content;
    private Object USER;
    private String Ctime;
    private boolean Isintention;

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getSd_name() {
        return sd_name;
    }

    public void setSd_name(String sd_name) {
        this.sd_name = sd_name;
    }

    public String getContacts() {
        return Contacts;
    }

    public void setContacts(String Contacts) {
        this.Contacts = Contacts;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String Phone) {
        this.Phone = Phone;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String Content) {
        this.Content = Content;
    }

    public Object getUSER() {
        return USER;
    }

    public void setUSER(Object USER) {
        this.USER = USER;
    }

    public String getCtime() {
        return Ctime;
    }

    public void setCtime(String Ctime) {
        this.Ctime = Ctime;
    }

    public boolean isIsintention() {
        return Isintention;
    }

    public void setIsintention(boolean Isintention) {
        this.Isintention = Isintention;
    }
}
