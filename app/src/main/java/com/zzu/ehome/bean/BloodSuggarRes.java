package com.zzu.ehome.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/5/11.
 */
public class BloodSuggarRes {
    @SerializedName("UpdatedDate")
    private String UpdatedDate;
    @SerializedName("Type")
    private String Type;
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
    @SerializedName("BloodSugarValue")
    private String BloodSugarValue;
    @SerializedName("MonitorPoint")
    private String MonitorPoint;
    public String getUpdatedDate() {
        return UpdatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        UpdatedDate = updatedDate;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
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

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
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

    public String getBloodSugarValue() {
        return BloodSugarValue;
    }

    public void setBloodSugarValue(String bloodSugarValue) {
        BloodSugarValue = bloodSugarValue;
    }

    public String getMonitorPoint() {
        return MonitorPoint;
    }

    public void setMonitorPoint(String monitorPoint) {
        MonitorPoint = monitorPoint;
    }


}
