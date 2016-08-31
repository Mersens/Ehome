package com.zzu.ehome.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.zzu.ehome.R;
import com.zzu.ehome.fragment.AppointmentFragment;
import com.zzu.ehome.view.HeadView;

/**
 * Created by Mersens on 2016/8/27.
 */
public class AppointmentActivity1 extends BaseActivity {
    private TextView tv_normal, tv_net;
    private int selectColor, unSelectColor;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.layout_appointment);
        initViews();
        initEvent();
        initDatas();
    }

    public void initViews() {
        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "我的预约", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();
            }
        });
        tv_normal = (TextView) findViewById(R.id.tv_normal);
        tv_net = (TextView) findViewById(R.id.tv_net);
    }

    public void initEvent() {
        tv_normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setColor(Type.NORMAL);
                addFragment(Type.NORMAL);
            }
        });
        tv_net.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setColor(Type.NET);
                addFragment(Type.NET);
            }
        });
    }

    public void setColor(Type type){
        resetColor();
        switch (type){
            case NORMAL:
                tv_normal.setTextColor(selectColor);
                break;
            case NET:
                tv_net.setTextColor(selectColor);
                break;
        }
    }

    public void resetColor(){
        tv_normal.setTextColor(unSelectColor);
        tv_net.setTextColor(unSelectColor);
    }

    public Fragment creatFragment(Type type) {
        Fragment fragment = null;
        switch (type) {
            case NORMAL:
                //请求数据地址
                fragment= AppointmentFragment.getInstance("");
                break;
            case NET:
                //请求数据地址
                fragment= AppointmentFragment.getInstance("");
                break;
        }

        return fragment;
    }


    public void addFragment(Type type) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, creatFragment(type)).commit();
    }

    public void initDatas() {
        unSelectColor = getResources().getColor(R.color.text_color2);
        selectColor = getResources().getColor(R.color.actionbar_color);
        tv_normal.setTextColor(selectColor);
        tv_net.setTextColor(unSelectColor);
        addFragment(Type.NORMAL);
    }

    public enum Type {
        NORMAL, NET
        ;
    }
}
