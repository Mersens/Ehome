package com.zzu.ehome.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/6/25.
 */
public class HealthBean {

    @SerializedName("DeleteFlag")
    String DeleteFlag;
    @SerializedName("UserID")
    String UserID;
    @SerializedName("MonitorTime")
    String MonitorTime;
    @SerializedName("Value3")
    String Value3;
    @SerializedName("Type")
    String Type;
    @SerializedName("Value1")
    String  Value1;
    @SerializedName("Value2")
    String Value2;

    public String getDeleteFlag() {
        return DeleteFlag;
    }

    public void setDeleteFlag(String deleteFlag) {
        DeleteFlag = deleteFlag;
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

    public String getValue3() {
        return Value3;
    }

    public void setValue3(String value3) {
        Value3 = value3;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getValue1() {
        return Value1;
    }

    public void setValue1(String value1) {
        Value1 = value1;
    }

    public String getValue2() {
        return Value2;
    }

    public void setValue2(String value2) {
        Value2 = value2;
    }
}
