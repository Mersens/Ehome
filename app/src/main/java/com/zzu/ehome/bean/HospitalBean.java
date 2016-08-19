package com.zzu.ehome.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zzu on 2016/4/18.
 */
public class HospitalBean {
    @SerializedName("HospitalID")
    private String Hospital_Id;
    private String Hospital_Code;
    @SerializedName("HospitalName")

    private String Hospital_FullName;
    private String Hospital_ShortName;
    private String Hopital_Level;
    private String Hospital_Introduction;
    private String Hospital_Address;
    private String Hospital_Website;
    private String Hospital_Contact;
    private String Hospital_Tel;
    private String Hospital_Signature;
    private String Hospital_RegisterTime;
    private String Hospital_RegisterId;
    private String Hospital_Status;


    public HospitalBean(){

    }
    public String getHospital_RegisterId() {
        return Hospital_RegisterId;
    }

    public void setHospital_RegisterId(String hospital_RegisterId) {
        Hospital_RegisterId = hospital_RegisterId;
    }

    public String getHospital_Id() {
        return Hospital_Id;
    }

    public void setHospital_Id(String hospital_Id) {
        Hospital_Id = hospital_Id;
    }

    public String getHospital_Code() {
        return Hospital_Code;
    }

    public void setHospital_Code(String hospital_Code) {
        Hospital_Code = hospital_Code;
    }

    public String getHospital_FullName() {
        return Hospital_FullName;
    }

    public void setHospital_FullName(String hospital_FullName) {
        Hospital_FullName = hospital_FullName;
    }

    public String getHospital_ShortName() {
        return Hospital_ShortName;
    }

    public void setHospital_ShortName(String hospital_ShortName) {
        Hospital_ShortName = hospital_ShortName;
    }

    public String getHopital_Level() {
        return Hopital_Level;
    }

    public void setHopital_Level(String hopital_Level) {
        Hopital_Level = hopital_Level;
    }

    public String getHospital_Introduction() {
        return Hospital_Introduction;
    }

    public void setHospital_Introduction(String hospital_Introduction) {
        Hospital_Introduction = hospital_Introduction;
    }

    public String getHospital_Address() {
        return Hospital_Address;
    }

    public void setHospital_Address(String hospital_Address) {
        Hospital_Address = hospital_Address;
    }

    public String getHospital_Website() {
        return Hospital_Website;
    }

    public void setHospital_Website(String hospital_Website) {
        Hospital_Website = hospital_Website;
    }

    public String getHospital_Contact() {
        return Hospital_Contact;
    }

    public void setHospital_Contact(String hospital_Contact) {
        Hospital_Contact = hospital_Contact;
    }

    public String getHospital_Tel() {
        return Hospital_Tel;
    }

    public void setHospital_Tel(String hospital_Tel) {
        Hospital_Tel = hospital_Tel;
    }

    public String getHospital_Signature() {
        return Hospital_Signature;
    }

    public void setHospital_Signature(String hospital_Signature) {
        Hospital_Signature = hospital_Signature;
    }

    public String getHospital_RegisterTime() {
        return Hospital_RegisterTime;
    }

    public void setHospital_RegisterTime(String hospital_RegisterTime) {
        Hospital_RegisterTime = hospital_RegisterTime;
    }

    public String getHospital_Status() {
        return Hospital_Status;
    }

    public void setHospital_Status(String hospital_Status) {
        Hospital_Status = hospital_Status;
    }




    @Override
    public String toString() {
        return "HospitalBean{" +
                "Hospital_Id='" + Hospital_Id + '\'' +
                ", Hospital_Code='" + Hospital_Code + '\'' +
                ", Hospital_FullName='" + Hospital_FullName + '\'' +
                ", Hospital_ShortName='" + Hospital_ShortName + '\'' +
                ", Hopital_Level='" + Hopital_Level + '\'' +
                ", Hospital_Introduction='" + Hospital_Introduction + '\'' +
                ", Hospital_Address='" + Hospital_Address + '\'' +
                ", Hospital_Website='" + Hospital_Website + '\'' +
                ", Hospital_Contact='" + Hospital_Contact + '\'' +
                ", Hospital_Tel='" + Hospital_Tel + '\'' +
                ", Hospital_Signature='" + Hospital_Signature + '\'' +
                ", Hospital_RegisterTime='" + Hospital_RegisterTime + '\'' +
                ", Hospital_Status='" + Hospital_Status + '\'' +
                ", Hospital_RegisterId='" + Hospital_RegisterId + '\'' +
                '}';
    }


}
