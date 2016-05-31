package com.example.yj.mapapp.model;

import java.util.List;

public class IndustryClassification {

    /**
     * P_Id : 1
     * P_Name : 墙体材料
     * P_ParentId : 0
     * P_Desc : 0
     */

    private int P_Id;
    private String P_Name;
    private int P_ParentId;
    private int P_Desc;
    public List<IndustryClassification> mList;

    public List<IndustryClassification> getmList() {
        return mList;
    }

    public void setmList(List<IndustryClassification> mList) {
        this.mList = mList;
    }

    public int getP_Id() {
        return P_Id;
    }

    public void setP_Id(int P_Id) {
        this.P_Id = P_Id;
    }

    public String getP_Name() {
        return P_Name;
    }

    public void setP_Name(String P_Name) {
        this.P_Name = P_Name;
    }

    public int getP_ParentId() {
        return P_ParentId;
    }

    public void setP_ParentId(int P_ParentId) {
        this.P_ParentId = P_ParentId;
    }

    public int getP_Desc() {
        return P_Desc;
    }

    public void setP_Desc(int P_Desc) {
        this.P_Desc = P_Desc;
    }
}
