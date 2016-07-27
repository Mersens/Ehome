package com.zzu.ehome.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/6/23.
 */
public class MedicalBean {
    @SerializedName("CheckTime")
    private String CheckTime;
    @SerializedName("ID")
    private String ID;
    @SerializedName("ReportImage")
    private String ReportImage;
    @SerializedName("CreatedDate")
    private String CreatedDate;
    @SerializedName("UserName")
    private String UserName;
    @SerializedName("InstituteName")
    private String InstituteName;

    public String getCheckTime() {
        return CheckTime;
    }

    public void setCheckTime(String checkTime) {
        CheckTime = checkTime;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getReportImage() {
        return ReportImage;
    }

    public void setReportImage(String reportImage) {
        ReportImage = reportImage;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getInstituteName() {
        return InstituteName;
    }

    public void setInstituteName(String instituteName) {
        InstituteName = instituteName;
    }


//    "ReportImage": "~\\UploadFile\\AndroidJCD\\201606231546230_.jpg,~\\UploadFile\\AndroidJCD\\201606231546281_.jpg,~\\UploadFile\\AndroidJCD\\201606231546322_.jpg,~\\UploadFile\\AndroidJCD\\201606231546373_.jpg",
//            "UpdatedDate": "2016/6/23 15:48:22",
//            "DeleteFlag": "0",
//            "CreatedDate": "2016/6/23 15:48:22",
//            "CreatedBy": "",
//            "CheckTime": "2016/6/23 0:00:00",
//            "ID": "2",
//            "UserID": "51064",
//            "UserName": "古代",
//            "UpdatedBy": "",
//            "InstituteName": "郑州大学第一附属医院"


}
