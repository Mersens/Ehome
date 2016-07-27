package com.zzu.ehome.db;

import com.zzu.ehome.bean.DiseaseBean;
import com.zzu.ehome.bean.StepBean;
import com.zzu.ehome.bean.User;

import java.util.List;

/**
 * Created by zzu on 2016/4/6.
 */
public interface EHomeDao {

    //查询所有用户信息
    public List<User> findAllUser();

    //根据id查找用户信息
    public User findUserInfoById(String userid);

    //删除用户信息
    public void delUserInfoById(String userid);

    //判断用户是否存在
    public boolean findUserIsExist(String userid);

    //添加用户信息
    public void addUserInfo(User user);

    //修改用户信息
    public void updateUserInfo(User user,String userid);

    //保存计步数据
    public void saveStep(StepBean step);

    //修改计步数据
    public void updateStep(StepBean step);

    //查找计步数据
    public StepBean loadSteps(String userid,String time);











}
