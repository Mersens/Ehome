package com.zzu.ehome.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/5/5.
 */
public class TreatmentSearchDate {
    @SerializedName("TreatmentSearch")
    private List<TreatmentSearch> date;
    public List<TreatmentSearch> getDate() {
        return date;
    }

    public void setDate(List<TreatmentSearch> date) {
        this.date = date;
    }
}
