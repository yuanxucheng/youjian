package com.example.yj.mapapp.model;

public class BuyingLeads {
    /**
     * ID : 20
     * NAME : 李四
     * TYPE : 业务专员
     * CONTENT : 二手设备
     * ADDRESS : 地球村
     * CONTACTS : 王五
     * PHONE : 13654125631
     * CTIME : 2016-05-05 05:35:14
     * USER_ID : 1
     */

    private int ID;
    private String NAME;
    private String TYPE;
    private String CONTENT;
    private String ADDRESS;
    private String CONTACTS;
    private String PHONE;
    private String CTIME;
    private int USER_ID;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }

    public String getCONTENT() {
        return CONTENT;
    }

    public void setCONTENT(String CONTENT) {
        this.CONTENT = CONTENT;
    }

    public String getADDRESS() {
        return ADDRESS;
    }

    public void setADDRESS(String ADDRESS) {
        this.ADDRESS = ADDRESS;
    }

    public String getCONTACTS() {
        return CONTACTS;
    }

    public void setCONTACTS(String CONTACTS) {
        this.CONTACTS = CONTACTS;
    }

    public String getPHONE() {
        return PHONE;
    }

    public void setPHONE(String PHONE) {
        this.PHONE = PHONE;
    }

    public String getCTIME() {
        return CTIME;
    }

    public void setCTIME(String CTIME) {
        this.CTIME = CTIME;
    }

    public int getUSER_ID() {
        return USER_ID;
    }

    public void setUSER_ID(int USER_ID) {
        this.USER_ID = USER_ID;
    }
}
