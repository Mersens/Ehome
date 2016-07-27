package com.zzu.ehome.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/5/10.
 */
public class WeightDate {
    @SerializedName("WeightInquiry")
    List<WeightRes> data;
    public List<WeightRes> getData() {
        return data;
    }
}

