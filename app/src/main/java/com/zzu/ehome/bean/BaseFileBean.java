package com.zzu.ehome.bean;

/**
 * Created by dell on 2016/6/3.
 */
public class BaseFileBean {
    private String name;
    private boolean isOpen;

    public BaseFileBean(){

    }

    public BaseFileBean(String name, boolean isOpen) {
        this.name = name;
        this.isOpen = isOpen;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



}
