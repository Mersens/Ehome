package com.zzu.ehome.bean;

/**
 * Created by Mersens on 2016/7/7.
 */
public class StepBean {
    private int id;
    private int num;
    private String startTime;
    private String endTime;
    private int uploadState;//0表示未上传，1表示已上传

    public int getUploadState() {
        return uploadState;
    }

    public void setUploadState(int uploadState) {
        this.uploadState = uploadState;
    }



    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    private String userid;
    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }


    @Override
    public String toString() {
        return "StepBean{" +
                "id=" + id +
                ", num=" + num +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", userid='" + userid + '\'' +
                '}';
    }
}
