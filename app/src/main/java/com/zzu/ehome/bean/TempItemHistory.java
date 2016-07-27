package com.zzu.ehome.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/4/29.
 */
public class TempItemHistory {
    @SerializedName("ID")
    private String ID;
    @SerializedName("UserID")
    private String UserID;
    @SerializedName("Value")
    private String Value;
    @SerializedName("MonitorTime")
    private String MonitorTime;
    @SerializedName("CreatedBy")
    private String CreatedBy;
    @SerializedName("CreatedDate")
    private String CreatedDate;
    @SerializedName("UpdatedBy")

    private String UpdatedBy;
    @SerializedName("UpdatedDate")
    private String UpdatedDate;
    @SerializedName("DeleteFlag")
    private String DeleteFlag;

    public String getUpdatedBy() {
        return UpdatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        UpdatedBy = updatedBy;
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

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

    public String getMonitorTime() {
        return MonitorTime;
    }

    public void setMonitorTime(String monitorTime) {
        MonitorTime = monitorTime;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String createdBy) {
        CreatedBy = createdBy;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
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
}
