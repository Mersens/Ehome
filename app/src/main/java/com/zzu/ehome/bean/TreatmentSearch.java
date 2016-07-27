package com.zzu.ehome.bean;

import com.google.gson.annotations.SerializedName;
import com.zzu.ehome.view.SlideView;

/**
 * Created by Administrator on 2016/5/5.
 */
public class TreatmentSearch {
//    "Hospital": " ",
//            "JobName": " ",
//            "ReservationId": 731,
//            "Day": "周一",
//            "PatientCount": 1,
//            "Name": "Admin",
//            "Status": 0,
//            "DoctorIcon": "",
//            "Time": "2016/5/9 11:30:00",
//            "doctorId": 2,
//            "InRoom": 0,
//            "doctorUserId": 5,
//            "Satis": "5",
//            "DignoseCount": 25,
//            "Department": " "
    @SerializedName("Hospital")
    private String Hospital;
    @SerializedName("JobName")
    private String JobName;
    @SerializedName("ReservationId")
    private String ReservationId;
    @SerializedName("Day")
    private String Day;
    @SerializedName("PatientCount")
    private String PatientCount;
    @SerializedName("Name")
    private String Name;
    @SerializedName("Status")
    private String Status;
    @SerializedName("DoctorIcon")
    private String DoctorIcon;
    @SerializedName("Time")
    private String Time;
    @SerializedName("doctorId")
    private String doctorId;
    @SerializedName("InRoom")
    private String InRoom;
    @SerializedName("doctorUserId")
    private String doctorUserId;
    @SerializedName("Satis")
    private String Satis;
    @SerializedName("DignoseCount")
    private String DignoseCount;
    public boolean isOverdue() {
        return isOverdue;
    }

    public void setOverdue(boolean overdue) {
        isOverdue = overdue;
    }

    private  boolean isOverdue=false;

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    private boolean isOpen=false;


    @Override
    public String toString() {
        return "TreatmentSearch{" +
                "Hospital='" + Hospital + '\'' +
                ", JobName='" + JobName + '\'' +
                ", ReservationId='" + ReservationId + '\'' +
                ", Day='" + Day + '\'' +
                ", PatientCount='" + PatientCount + '\'' +
                ", Name='" + Name + '\'' +
                ", Status='" + Status + '\'' +
                ", DoctorIcon='" + DoctorIcon + '\'' +
                ", Time='" + Time + '\'' +
                ", doctorId='" + doctorId + '\'' +
                ", InRoom='" + InRoom + '\'' +
                ", doctorUserId='" + doctorUserId + '\'' +
                ", Satis='" + Satis + '\'' +
                ", DignoseCount='" + DignoseCount + '\'' +
                ", Department='" + Department + '\'' +
                '}';
    }

    @SerializedName("Department")
    private String Department;

    public String getHospital() {
        return Hospital;
    }

    public void setHospital(String hospital) {
        Hospital = hospital;
    }

    public String getJobName() {
        return JobName;
    }

    public void setJobName(String jobName) {
        JobName = jobName;
    }

    public String getReservationId() {
        return ReservationId;
    }

    public void setReservationId(String reservationId) {
        ReservationId = reservationId;
    }

    public String getDay() {
        return Day;
    }

    public void setDay(String day) {
        Day = day;
    }

    public String getPatientCount() {
        return PatientCount;
    }

    public void setPatientCount(String patientCount) {
        PatientCount = patientCount;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getDoctorIcon() {
        return DoctorIcon;
    }

    public void setDoctorIcon(String doctorIcon) {
        DoctorIcon = doctorIcon;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getInRoom() {
        return InRoom;
    }

    public void setInRoom(String inRoom) {
        InRoom = inRoom;
    }

    public String getDoctorUserId() {
        return doctorUserId;
    }

    public void setDoctorUserId(String doctorUserId) {
        this.doctorUserId = doctorUserId;
    }

    public String getSatis() {
        return Satis;
    }

    public void setSatis(String satis) {
        Satis = satis;
    }

    public String getDignoseCount() {
        return DignoseCount;
    }

    public void setDignoseCount(String dignoseCount) {
        DignoseCount = dignoseCount;
    }

    public String getDepartment() {
        return Department;
    }

    public void setDepartment(String department) {
        Department = department;
    }
}
