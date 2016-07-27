package com.zzu.ehome.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/6/25.
 */
public class HealthDataSearchByDate {
    @SerializedName("HealthDataSearchByDate")
    List<HealthBean> data;
    public List<HealthBean> getData() {
        return data;
    }
}
