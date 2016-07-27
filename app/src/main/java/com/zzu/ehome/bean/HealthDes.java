package com.zzu.ehome.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/4/16.
 */
public class HealthDes {

//                    "HealthDataSearch": [
//                    {
//                        "opinion": "用药建议",
//                            "Diagnosis": "诊断",
//                            "weight": "75",
//                            "prevalue": "120,90",
//                            "sugvalue": "49",
//                            "tempvalue": "64"
//                    }
//                    ]
//                }
@SerializedName("opinion")
    String opinion;
    @SerializedName("diagnosis")
    String Diagnosis;
    @SerializedName("weight")
    String weight;
    @SerializedName("prevalue")
    String prevalue;
    @SerializedName("sugvalue")
    String sugvalue;
    @SerializedName("tempvalue")
    String tempvalue;



    @SerializedName("precount")
    String precount;
    @SerializedName("weightcount")
    String weightcount;
    @SerializedName("tempcount")
    String tempcount;
    @SerializedName("sugcount")
    String sugcount;

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    public String getDiagnosis() {
        return Diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        Diagnosis = diagnosis;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getPrevalue() {
        return prevalue;
    }

    public void setPrevalue(String prevalue) {
        this.prevalue = prevalue;
    }

    public String getSugvalue() {
        return sugvalue;
    }

    public void setSugvalue(String sugvalue) {
        this.sugvalue = sugvalue;
    }

    public String getTempvalue() {
        return tempvalue;
    }

    public void setTempvalue(String tempvalue) {
        this.tempvalue = tempvalue;
    }
    public String getPrecount() {
        return precount;
    }

    public void setPrecount(String precount) {
        this.precount = precount;
    }

    public String getWeightcount() {
        return weightcount;
    }

    public void setWeightcount(String weightcount) {
        this.weightcount = weightcount;
    }

    public String getTempcount() {
        return tempcount;
    }

    public void setTempcount(String tempcount) {
        this.tempcount = tempcount;
    }

    public String getSugcount() {
        return sugcount;
    }

    public void setSugcount(String sugcount) {
        this.sugcount = sugcount;
    }
}
