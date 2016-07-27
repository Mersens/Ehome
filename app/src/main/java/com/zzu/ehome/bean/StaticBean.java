package com.zzu.ehome.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/6/22.
 */
public class StaticBean {

    @SerializedName("UserID")
    private String UserID;
    @SerializedName("ImgPath")
    private String ImgPath;
    @SerializedName("Diagnosis")
    private String Diagnosis;
    @SerializedName("PatientName")
    private String PatientName;
    @SerializedName("ApplicationId")
    private String ApplicationId;
    @SerializedName("CollectTime")
    private String CollectTime;
    public String getCollectTime() {
        return CollectTime;
    }

    public void setCollectTime(String collectTime) {
        CollectTime = collectTime;
    }



    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getImgPath() {
        return ImgPath;
    }

    public void setImgPath(String imgPath) {
        ImgPath = imgPath;
    }

    public String getDiagnosis() {
        return Diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        Diagnosis = diagnosis;
    }

    public String getPatientName() {
        return PatientName;
    }

    public void setPatientName(String patientName) {
        PatientName = patientName;
    }

    public String getApplicationId() {
        return ApplicationId;
    }

    public void setApplicationId(String applicationId) {
        ApplicationId = applicationId;
    }
}
