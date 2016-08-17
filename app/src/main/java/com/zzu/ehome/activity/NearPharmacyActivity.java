package com.zzu.ehome.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zzu.ehome.R;
import com.zzu.ehome.fragment.CooperationPharmacyFragment;
import com.zzu.ehome.fragment.NearPharmacyFragment;
import com.zzu.ehome.view.HeadView;

/**
 * Created by Mersens on 2016/8/17.
 * 附近药店
 */
public class NearPharmacyActivity extends BaseActivity {
    private RelativeLayout layout_near, layout_cooperation;
    private TextView tv_near, tv_cooperation;
    private int selectColor, unSelectColor;


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.layout_near_pharmacy);
        initViews();
        initEvent();
        initDatas();
    }


    public void initViews() {
        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "附近药店", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();
            }
        });
        layout_near = (RelativeLayout) findViewById(R.id.layout_near);
        layout_cooperation = (RelativeLayout) findViewById(R.id.layout_cooperation);
        tv_near = (TextView) findViewById(R.id.tv_near);
        tv_cooperation = (TextView) findViewById(R.id.tv_cooperation);

    }


    public void initEvent() {
        layout_near.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setColor(Type.NEAR);
                addFragment(Type.NEAR);
            }
        });
        layout_cooperation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setColor(Type.COOPERATION);
                addFragment(Type.COOPERATION);

            }
        });

    }

    public void initDatas() {
        unSelectColor = getResources().getColor(R.color.text_color2);
        selectColor = getResources().getColor(R.color.actionbar_color);
        tv_near.setTextColor(selectColor);
        tv_cooperation.setTextColor(unSelectColor);
        addFragment(Type.NEAR);
    }


    public void setColor(Type type){
        resetColor();
        switch (type){
            case NEAR:
                tv_near.setTextColor(selectColor);
                break;
            case COOPERATION:
                tv_cooperation.setTextColor(selectColor);
                break;
        }

    }


    public void resetColor(){
        tv_near.setTextColor(unSelectColor);
        tv_cooperation.setTextColor(unSelectColor);
    }

    public Fragment creatFragment(Type type) {
        Fragment fragment = null;
        switch (type) {
            case NEAR:
                fragment= NearPharmacyFragment.getInstance();
                break;

            case COOPERATION:
                fragment= CooperationPharmacyFragment.getInstance();
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
        NEAR, COOPERATION;
    }


}
