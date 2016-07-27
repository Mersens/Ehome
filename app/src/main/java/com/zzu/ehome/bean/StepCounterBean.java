package com.zzu.ehome.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/7/14.
 */
public class StepCounterBean {
    @SerializedName("MonitorTime")
    private String MonitorTime;
    @SerializedName("TotalHeat")
    private String TotalHeat;
    @SerializedName("TotalStep")
    private String TotalStep;
    @SerializedName("TotalTime")
    private String TotalTime;
    @SerializedName("TotalDistance")
    private String TotalDistance;

    public String getMonitorTime() {
        return MonitorTime;
    }

    public void setMonitorTime(String monitorTime) {
        MonitorTime = monitorTime;
    }

    public String getTotalHeat() {
        return TotalHeat;
    }

    public void setTotalHeat(String totalHeat) {
        TotalHeat = totalHeat;
    }

    public String getTotalStep() {
        return TotalStep;
    }

    public void setTotalStep(String totalStep) {
        TotalStep = totalStep;
    }

    public String getTotalTime() {
        return TotalTime;
    }

    public void setTotalTime(String totalTime) {
        TotalTime = totalTime;
    }

    public String getTotalDistance() {
        return TotalDistance;
    }

    public void setTotalDistance(String totalDistance) {
        TotalDistance = totalDistance;
    }
}
