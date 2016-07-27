package com.zzu.ehome.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/6/30.
 */
public class MedicationDate {
    @SerializedName("MedicationRecordInquiry")
    List<MedicationRecord> data;
    public List<MedicationRecord> getData() {
        return data;
    }
}
