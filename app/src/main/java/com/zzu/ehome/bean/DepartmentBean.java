package com.zzu.ehome.bean;

/**
 * Created by zzu on 2016/4/18.
 */
public class DepartmentBean  {
    private String Department_Id;


    private String Department_FullName;
    public DepartmentBean(){

    }

    public String getDepartment_FullName() {
        return Department_FullName;
    }

    public void setDepartment_FullName(String department_FullName) {
        Department_FullName = department_FullName;
    }

    public String getDepartment_Id() {
        return Department_Id;
    }

    public void setDepartment_Id(String department_Id) {
        Department_Id = department_Id;
    }

    @Override
    public String toString() {
        return "DepartmentBean{" +
                "Department_Id='" + Department_Id + '\'' +
                ", Department_FullName='" + Department_FullName + '\'' +
                '}';
    }

}
