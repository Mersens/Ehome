package com.zzu.ehome.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/5/10.
 */
public class WeightRes {
//    {
//        "height": "",
//            "weight": "42",
//            "UpdatedDate": "2016/5/10 17:27:09",
//            "DeleteFlag": "0",
//            "CreatedDate": "2016/5/10 17:27:09",
//            "CreatedBy": "",
//            "ID": "291",
//            "UserID": "51064",
//            "MonitorTime": "2016/5/10 14:27:00",
//            "UpdatedBy": ""
//    },
@SerializedName("height")
    private String height;
    @SerializedName("weight")
    private String weight;
    @SerializedName("UpdatedDate")
    private String UpdatedDate;
    @SerializedName("DeleteFlag")
    private String DeleteFlag;
    @SerializedName("CreatedDate")
    private String CreatedDate;
    @SerializedName("CreatedBy")
    private String CreatedBy;
    @SerializedName("ID")

    private String ID;
    @SerializedName("UserID")
    private String UserID;
    @SerializedName("MonitorTime")
    private String MonitorTime;
    @SerializedName("UpdatedBy")
    private String UpdatedBy;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getUpdatedDate() {
        return UpdatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        UpdatedDate = updatedDate;
    }

    public String getDeleteFlag() {
        return DeleteFlag;
    }

    public void setDeleteFlag(String deleteFlag) {
        DeleteFlag = deleteFlag;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String createdBy) {
        CreatedBy = createdBy;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getMonitorTime() {
        return MonitorTime;
    }

    public void setMonitorTime(String monitorTime) {
        MonitorTime = monitorTime;
    }

    public String getUpdatedBy() {
        return UpdatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        UpdatedBy = updatedBy;
    }





}
