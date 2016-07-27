package com.zzu.ehome.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/4/20.
 */
public class TreatmentInquirywWithPageDate {
    @SerializedName("TreatmentInquirywWithPage")
    List<TreatmentInquirywWithPage> data;
    public List<TreatmentInquirywWithPage> getData() {
        return data;
    }

}
