package com.example.yj.mapapp.model;

import com.google.gson.annotations.SerializedName;

public class CompanyDetail {

    /**
     * "m": "",
     * "s": 1,
     * "d": {
     * "imagesURL": "../../Content/image/shopCommodity_1.jpg|../../Content/image/shopCommodity_2.jpg|../../Content/image/shopCommodity_3.jpg",
     * "SI_Id": 1250,
     * "SI_CompanyName": "上海颖异机械厂",
     * "SI_Address": "上海奉贤区南桥镇牡丹园路135号",
     * "SI_Contacts": "周德贵",
     * "SI_Phone": "18621573566  、   13905738922",
     * "SI_YJAuthentication": 0
     *
     */

    private String m;
    private int s;

    /**
     * "imagesURL": "../../Content/image/shopCommodity_1.jpg|../../Content/image/shopCommodity_2.jpg|../../Content/image/shopCommodity_3.jpg",
     * "SI_Id": 1250,
     * "SI_CompanyName": "上海颖异机械厂",
     * "SI_Address": "上海奉贤区南桥镇牡丹园路135号",
     * "SI_Contacts": "周德贵",
     * "SI_Phone": "18621573566  、   13905738922",
     * "SI_YJAuthentication": 0
     */

    private DEntity d;

    public String getM() {
        return m;
    }

    public void setM(String m) {
        this.m = m;
    }

    public int getS() {
        return s;
    }

    public void setS(int s) {
        this.s = s;
    }

    public DEntity getD() {
        return d;
    }

    public void setD(DEntity d) {
        this.d = d;
    }

    public static class DEntity {
        @SerializedName("SI_Id")
        private int SI_Id;
        @SerializedName("imagesURL")
        private String imagesURL;
        @SerializedName("SI_CompanyName")
        private String SI_CompanyName;
        @SerializedName("SI_Address")
        private String SI_Address;
        @SerializedName("SI_Contacts")
        private String SI_Contacts;
        @SerializedName("SI_Phone")
        private String SI_Phone;
        @SerializedName("SI_YJAuthentication")
        private int SI_YJAuthentication;

        public int getSI_Id() {
            return SI_Id;
        }

        public void setSI_Id(int SI_Id) {
            this.SI_Id = SI_Id;
        }

        public String getImagesURL() {
            return imagesURL;
        }

        public void setImagesURL(String imagesURL) {
            this.imagesURL = imagesURL;
        }

        public String getSI_CompanyName() {
            return SI_CompanyName;
        }

        public void setSI_CompanyName(String SI_CompanyName) {
            this.SI_CompanyName = SI_CompanyName;
        }

        public String getSI_Address() {
            return SI_Address;
        }

        public void setSI_Address(String SI_Address) {
            this.SI_Address = SI_Address;
        }

        public String getSI_Contacts() {
            return SI_Contacts;
        }

        public void setSI_Contacts(String SI_Contacts) {
            this.SI_Contacts = SI_Contacts;
        }

        public String getSI_Phone() {
            return SI_Phone;
        }

        public void setSI_Phone(String SI_Phone) {
            this.SI_Phone = SI_Phone;
        }

        public int getSI_YJAuthentication() {
            return SI_YJAuthentication;
        }

        public void setSI_YJAuthentication(int SI_YJAuthentication) {
            this.SI_YJAuthentication = SI_YJAuthentication;
        }
    }
}
