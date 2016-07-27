package com.zzu.ehome.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/4/12.
 */
public class UserInfoDate {
    @SerializedName("UserInquiry")
    List<User> data;
    public List<User> getData() {
        return data;
    }
}
