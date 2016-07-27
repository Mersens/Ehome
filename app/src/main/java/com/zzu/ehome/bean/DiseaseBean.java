package com.zzu.ehome.bean;

/**
 * Created by dell on 2016/6/6.
 */
public class DiseaseBean {

    private String name;
    private boolean isOpen;

    public DiseaseBean(String name, boolean isOpen) {
        this.name = name;
        this.isOpen = isOpen;
    }

    public DiseaseBean(){

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
