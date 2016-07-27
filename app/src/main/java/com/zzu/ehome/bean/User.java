package com.zzu.ehome.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/4/6.
 */
public class User {

    @SerializedName("UserID")
    private String userid;//userid
    @SerializedName("RealName")
    private String username;//用户名
    @SerializedName("NickName")
    private String nick;//昵称
    @SerializedName("PictureURL")
    private String imgHead;//头像地址
    @SerializedName("Mobile")
    private String mobile;
    private String password;//密码
    @SerializedName("UserSex")
    private String sex;
    @SerializedName("UserAge")
    private String age;

    public String getUserHeight() {
        return UserHeight;
    }

    public void setUserHeight(String userHeight) {
        UserHeight = userHeight;
    }

    @SerializedName("UserHeight")
    private String UserHeight;

    public String getUserno() {
        return userno;
    }

    public void setUserno(String userno) {
        this.userno = userno;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
    @SerializedName("UserNO")
    private String userno;
    @SerializedName("Patient_Id")
    private String PatientId;
    public String getPatientId() {
        return PatientId;
    }

    public void setPatientId(String patientId) {
        PatientId = patientId;
    }
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }



    public User(){

    }

    public String getImgHead() {
        return imgHead;
    }

    public void setImgHead(String imgHead) {
        this.imgHead = imgHead;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }



}
