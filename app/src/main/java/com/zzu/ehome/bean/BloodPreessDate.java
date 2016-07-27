package com.zzu.ehome.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/5/11.
 */
public class BloodPreessDate {
    @SerializedName("BloodPressureInquiry")
    List<BloodPressRes> data;
    public List<BloodPressRes> getData() {
        return data;
    }
}
