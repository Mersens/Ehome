package com.zzu.ehome.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/5/11.
 */
public class BloodPressRes {
//    {
//        "UpdatedDate": "2016/5/11 10:59:45",
//            "DeleteFlag": "0",
//            "CreatedDate": "2016/5/11 10:59:45",
//            "CreatedBy": "",
//            "ID": "128",
//            "UserID": "51064",
//            "MonitorTime": "2016/5/11 8:59:00",
//            "UpdatedBy": "",
//            "pulse": "75",
//            "high": "152",
//            "low": "86"
//    },
@SerializedName("UpdatedDate")
    private String UpdatedDate;
    @SerializedName("DeleteFlag")
    private String DeleteFlag;
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
    @SerializedName("pulse")
    private String pulse;
    @SerializedName("high")
    private String high;
    @SerializedName("low")
    private String low;

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

    public String getPulse() {
        return pulse;
    }

    public void setPulse(String pulse) {
        this.pulse = pulse;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }
}
