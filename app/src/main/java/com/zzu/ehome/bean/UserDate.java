package com.zzu.ehome.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/4/9.
 */
public class UserDate {
    @SerializedName("UserLogin")
    List<User> data;
    public List<User> getData() {
        return data;
    }
}
