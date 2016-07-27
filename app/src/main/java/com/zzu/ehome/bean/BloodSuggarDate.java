package com.zzu.ehome.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/5/11.
 */
public class BloodSuggarDate {
    @SerializedName("BloodSugarInquiry")
    List<BloodSuggarRes> data;
    public List<BloodSuggarRes> getData() {
        return data;
    }
}
