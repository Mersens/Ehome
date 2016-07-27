package com.zzu.ehome.bean;

/**
 * Created by zzu on 2016/4/19.
 */
public class ScheduleBean {
    private String Status;
    private String num;
    private String Date;
    private String PerTime;
    private String WeekDay;
    private String PatientCount;
    private String TimeSlot;

    public String getTimeBlock() {
        return TimeBlock;
    }

    public void setTimeBlock(String timeBlock) {
        TimeBlock = timeBlock;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getPerTime() {
        return PerTime;
    }

    public void setPerTime(String perTime) {
        PerTime = perTime;
    }

    public String getWeekDay() {
        return WeekDay;
    }

    public void setWeekDay(String weekDay) {
        WeekDay = weekDay;
    }

    public String getPatientCount() {
        return PatientCount;
    }

    public void setPatientCount(String patientCount) {
        PatientCount = patientCount;
    }

    public String getTimeSlot() {
        return TimeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        TimeSlot = timeSlot;
    }

    private String TimeBlock;


}
