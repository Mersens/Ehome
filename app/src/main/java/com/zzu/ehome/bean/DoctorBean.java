package com.zzu.ehome.bean;

/**
 * Created by zzu on 2016/4/18.
 */
public class DoctorBean {
    private String Doctor_Id;
    private String User_Id;
    private String User_FullName;
    private String User_Icon;
    private String Doctor_Title;
    private String Doctor_Specialty;
    private String Hosptial_Name;
    private String Hosptial_office;
    private String Department_Id;

    public String getHospital_Id() {
        return Hospital_Id;
    }

    public void setHospital_Id(String hospital_Id) {
        Hospital_Id = hospital_Id;
    }

    private String Hospital_Id;


    public String getDepartment_FullName() {
        return Department_FullName;
    }

    public void setDepartment_FullName(String department_FullName) {
        Department_FullName = department_FullName;
    }

    private String Department_FullName;
    public String getDepartment_Id() {
        return Department_Id;
    }

    public void setDepartment_Id(String department_Id) {
        Department_Id = department_Id;
    }

    public String getDoctorinfo() {
        return doctorinfo;
    }

    public void setDoctorinfo(String doctorinfo) {
        this.doctorinfo = doctorinfo;
    }

    private String doctorinfo;

    public DoctorBean(){

    }

    public String getHosptial_office() {
        return Hosptial_office;
    }

    public void setHosptial_office(String hosptial_office) {
        Hosptial_office = hosptial_office;
    }

    public String getUser_Id() {
        return User_Id;
    }

    public void setUser_Id(String user_Id) {
        User_Id = user_Id;
    }

    public String getUser_FullName() {
        return User_FullName;
    }

    public void setUser_FullName(String user_FullName) {
        User_FullName = user_FullName;
    }

    public String getUser_Icon() {
        return User_Icon;
    }

    public void setUser_Icon(String user_Icon) {
        User_Icon = user_Icon;
    }

    public String getDoctor_Title() {
        return Doctor_Title;
    }

    public void setDoctor_Title(String doctor_Title) {
        Doctor_Title = doctor_Title;
    }

    public String getDoctor_Specialty() {
        return Doctor_Specialty;
    }

    public void setDoctor_Specialty(String doctor_Specialty) {
        Doctor_Specialty = doctor_Specialty;
    }

    public String getHosptial_Name() {
        return Hosptial_Name;
    }

    public void setHosptial_Name(String hosptial_Name) {
        Hosptial_Name = hosptial_Name;
    }
    public String getDoctor_Id() {
        return Doctor_Id;
    }

    public void setDoctor_Id(String doctor_Id) {
        Doctor_Id = doctor_Id;
    }

}
