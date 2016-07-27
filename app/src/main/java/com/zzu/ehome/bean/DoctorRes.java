package com.zzu.ehome.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by xtfgq on 2016/4/5.
 */
public class DoctorRes {
    @SerializedName("AskCount")
    private String AskCount;
    @SerializedName("ImageURL")
    private String ImageURL;
    @SerializedName("Description")
    private String Description;
    @SerializedName("IsLine")
    private String IsLine;
    @SerializedName("DoctorName")
    private String DoctorName;
    @SerializedName("Title")
    private String Title;
    @SerializedName("UserCount")
    private String UserCount;
    @SerializedName("HospitalName")
    private String HospitalName;
    @SerializedName("DepartNO")
    private String DepartNO;
    @SerializedName("AskPrice")
    private String AskPrice;
    @SerializedName("IsFavored")
    private String IsFavored;
    @SerializedName("HospitalID")
    private String HospitalID;
    @SerializedName("AskPriceByOne")
    private String AskPriceByOne;
    @SerializedName("DoctorID")
    private String DoctorID;
    @SerializedName("DoctorFavors")
    private String DoctorFavors;

    public String getAskCount() {
        return AskCount;
    }

    public void setAskCount(String askCount) {
        AskCount = askCount;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getIsLine() {
        return IsLine;
    }

    public void setIsLine(String isLine) {
        IsLine = isLine;
    }

    public String getDoctorName() {
        return DoctorName;
    }

    public void setDoctorName(String doctorName) {
        DoctorName = doctorName;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getUserCount() {
        return UserCount;
    }

    public void setUserCount(String userCount) {
        UserCount = userCount;
    }

    public String getHospitalName() {
        return HospitalName;
    }

    public void setHospitalName(String hospitalName) {
        HospitalName = hospitalName;
    }

    public String getDepartNO() {
        return DepartNO;
    }

    public void setDepartNO(String departNO) {
        DepartNO = departNO;
    }

    public String getAskPrice() {
        return AskPrice;
    }

    public void setAskPrice(String askPrice) {
        AskPrice = askPrice;
    }

    public String getIsFavored() {
        return IsFavored;
    }

    public void setIsFavored(String isFavored) {
        IsFavored = isFavored;
    }

    public String getHospitalID() {
        return HospitalID;
    }

    public void setHospitalID(String hospitalID) {
        HospitalID = hospitalID;
    }

    public String getAskPriceByOne() {
        return AskPriceByOne;
    }

    public void setAskPriceByOne(String askPriceByOne) {
        AskPriceByOne = askPriceByOne;
    }

    public String getDoctorID() {
        return DoctorID;
    }

    public void setDoctorID(String doctorID) {
        DoctorID = doctorID;
    }

    public String getDoctorFavors() {
        return DoctorFavors;
    }

    public void setDoctorFavors(String doctorFavors) {
        DoctorFavors = doctorFavors;
    }






}
