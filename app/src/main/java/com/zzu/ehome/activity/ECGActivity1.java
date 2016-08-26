package com.zzu.ehome.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zzu.ehome.R;
import com.zzu.ehome.fragment.DynamicFragment;
import com.zzu.ehome.fragment.StaticECGFragment;
import com.zzu.ehome.fragment.DynamicECGFragment;
import com.zzu.ehome.fragment.StaticFragment;
import com.zzu.ehome.view.HeadView;

/**
 * Created by Mersens on 2016/8/22.
 */
public class ECGActivity1 extends BaseActivity {
    private RelativeLayout layout_d, layout_j;
    private TextView tv_d, tv_j;
    private int selectColor, unSelectColor;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.layout_ecg1);
        initViews();
        initEvent();
        initDatas();
    }


    public void initViews() {
        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "心电报告", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();
            }
        });
        layout_d = (RelativeLayout) findViewById(R.id.layout_d);
        layout_j = (RelativeLayout) findViewById(R.id.layout_j);
        tv_d = (TextView) findViewById(R.id.tv_d);
        tv_j = (TextView) findViewById(R.id.tv_j);


    }

    public void initEvent() {
        layout_d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setColor(Type.DYNAMIC);
                addFragment(Type.DYNAMIC);

            }
        });
        layout_j.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setColor(Type.STATIC_STATE);
                addFragment(Type.STATIC_STATE);
            }
        });
    }

    public void initDatas() {
        unSelectColor = getResources().getColor(R.color.text_color2);
        selectColor = getResources().getColor(R.color.actionbar_color);
        tv_d.setTextColor(selectColor);
        tv_j.setTextColor(unSelectColor);
        addFragment(Type.DYNAMIC);
    }


    public void setColor(Type type){
        resetColor();
        switch (type){
            case DYNAMIC:
                tv_d.setTextColor(selectColor);
                break;
            case STATIC_STATE:
                tv_j.setTextColor(selectColor);
                break;
        }

    }


    public void resetColor(){
        tv_j.setTextColor(unSelectColor);
        tv_d.setTextColor(unSelectColor);
    }

    public Fragment creatFragment(Type type) {
        Fragment fragment = null;
        switch (type) {
            case DYNAMIC:

                fragment= DynamicFragment.getInstance();
                break;

            case STATIC_STATE:

                fragment= StaticFragment.getInstance();

                break;
        }

        return fragment;
    }


    public void addFragment(Type type) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, creatFragment(type)).commit();
    }

    public enum Type {
        DYNAMIC, STATIC_STATE
        ;
    }

}
