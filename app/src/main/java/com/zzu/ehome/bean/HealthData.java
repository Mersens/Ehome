package com.zzu.ehome.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/4/16.
 */
public class HealthData {
    @SerializedName("HealthDataSearch")
    private List<HealthDes> date;
    public List<HealthDes> getDate() {
        return date;
    }

    public void setDate(List<HealthDes> date) {
        this.date = date;
    }


}
