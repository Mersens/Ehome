package com.zzu.ehome.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/6/23.
 */
public class MedicalDate {
    @SerializedName("MeidicalReportInquiry")
    List<MedicalBean> data;
    public List<MedicalBean> getData() {
        return data;
    }
}
