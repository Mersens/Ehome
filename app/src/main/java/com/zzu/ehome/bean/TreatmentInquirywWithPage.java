package com.zzu.ehome.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/4/20.
 */
public class TreatmentInquirywWithPage {

    @SerializedName("Treatment_AppointmentTime")
    private String time;
    @SerializedName("Treatment_Unit")
    private String hosname;
    @SerializedName("Treatment_Diagnosis")
    private String zhenduan;
    @SerializedName("Treatment_Opinion")
    private String Opinion;
    @SerializedName("Treatment_Image")
    private String Images;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getHosname() {
        return hosname;
    }

    public void setHosname(String hosname) {
        this.hosname = hosname;
    }

    public String getZhenduan() {
        return zhenduan;
    }

    public void setZhenduan(String zhenduan) {
        this.zhenduan = zhenduan;
    }

    public String getOpinion() {
        return Opinion;
    }

    public void setOpinion(String opinion) {
        Opinion = opinion;
    }

    public String getImages() {
        return Images;
    }

    public void setImages(String images) {
        Images = images;
    }
}
