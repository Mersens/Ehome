package com.zzu.ehome.bean;

/**
 * Created by Administrator on 2016/5/16.
 */
public class BusEvent {
    public BusEvent(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    private String value;

}
