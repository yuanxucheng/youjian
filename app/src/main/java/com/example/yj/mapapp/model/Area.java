package com.example.yj.mapapp.model;

public class Area {

    /**
     * Id : 2
     * Name : 宝山区
     * Pid : 1
     * Longitude : 121.455888
     * Latitude : 31.407587
     */

    private int Id;
    private String Name;
    private int Pid;
    private String Longitude;
    private String Latitude;

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

    public int getPid() {
        return Pid;
    }

    public void setPid(int Pid) {
        this.Pid = Pid;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String Longitude) {
        this.Longitude = Longitude;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String Latitude) {
        this.Latitude = Latitude;
    }
}
