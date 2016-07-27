package com.zzu.ehome.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/6/30.
 */
public class MedicationRecord {
    @SerializedName("ID")
    private String ID;
    @SerializedName("DrugImage")
    private String DrugImage;
    @SerializedName("Number")
    private String Number;
  @SerializedName("MedicationTime")
    private String MedicationTime;
    @SerializedName("DrugName")
    private String DrugName;
    @SerializedName("Remarks")
    private String Remarks;

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getDrugImage() {
        return DrugImage;
    }

    public void setDrugImage(String drugImage) {
        DrugImage = drugImage;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public String getMedicationTime() {
        return MedicationTime;
    }

    public void setMedicationTime(String medicationTime) {
        MedicationTime = medicationTime;
    }

    public String getDrugName() {
        return DrugName;
    }

    public void setDrugName(String drugName) {
        DrugName = drugName;
    }
}
