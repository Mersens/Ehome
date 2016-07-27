package com.zzu.ehome.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/7/14.
 */
public class StepCounterDate {
    @SerializedName("StepCounterInquiry")
    List<StepCounterBean> data;
    public List<StepCounterBean> getData() {
        return data;
    }
}
