package com.example.yj.mapapp.model;

public class Firm {

    /**
     * Id : 2
     * Name : 上海归宇新型建材有限公司
     * Address : 上海市浦东新区老港镇塘下公路51弄3号
     * Contacts : 张盛林
     * Authentication : 已认证
     */

    private int Id;
    private String Name;
    private String Address;
    private String Contacts;
    private String Authentication;
    private int iconId;

    public Firm(int iconId, int id, String name, String address, String contacts, String authentication) {
        Id = id;
        Name = name;
        Address = address;
        Contacts = contacts;
        Authentication = authentication;
        iconId = iconId;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public String getContacts() {
        return Contacts;
    }

    public void setContacts(String Contacts) {
        this.Contacts = Contacts;
    }

    public String getAuthentication() {
        return Authentication;
    }

    public void setAuthentication(String Authentication) {
        this.Authentication = Authentication;
    }
}
