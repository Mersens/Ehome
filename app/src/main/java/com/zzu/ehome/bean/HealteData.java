package com.zzu.ehome.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/4/25.
 */
public class HealteData {
    @SerializedName("HealthDataInquiryWithPage")
    List<HealthDataRes> data;
    public List<HealthDataRes> getData() {
        return data;
    }

}
