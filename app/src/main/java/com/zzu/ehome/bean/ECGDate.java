package com.zzu.ehome.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/6/21.
 */
public class ECGDate {
    @SerializedName("HolterPDFInquiry")
    List<ECGDynamicBean> data;
    public List<ECGDynamicBean> getData() {
        return data;
    }
}
