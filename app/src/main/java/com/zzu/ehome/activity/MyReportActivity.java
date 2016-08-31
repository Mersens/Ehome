package com.zzu.ehome.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.zzu.ehome.R;
import com.zzu.ehome.fragment.MyReportFragment;
import com.zzu.ehome.view.HeadView;

/**
 * Created by Mersens on 2016/8/29.
 */
public class MyReportActivity extends BaseActivity {
    private int selectColor, unSelectColor;
    private TextView tv_jiancha, tv_tijian, tv_xindian;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.layout_my_report);
        initViews();
        initEvent();
        initDatas();
    }

    public void initViews() {
        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "我的报告", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();
            }
        });
        tv_jiancha = (TextView) findViewById(R.id.tv_jiancha);
        tv_tijian = (TextView) findViewById(R.id.tv_tijian);
        tv_xindian = (TextView) findViewById(R.id.tv_xindian);

    }


    public void initEvent() {
        tv_jiancha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setColor(Type.INSPECT);
                addFragment(Type.INSPECT);
            }
        });
        tv_tijian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setColor(Type.EXAMINATION);
                addFragment(Type.EXAMINATION);
            }
        });
        tv_xindian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setColor(Type.ECG);
                addFragment(Type.ECG);
            }
        });
    }

    public void initDatas() {
        unSelectColor = getResources().getColor(R.color.text_color2);
        selectColor = getResources().getColor(R.color.actionbar_color);
        tv_jiancha.setTextColor(selectColor);
        tv_tijian.setTextColor(unSelectColor);
        tv_xindian.setTextColor(unSelectColor);
        addFragment(Type.INSPECT);
    }

    public void setColor(Type type){
        resetColor();
        switch (type){
            case INSPECT:
                tv_jiancha.setTextColor(selectColor);
                break;
            case EXAMINATION:
                tv_tijian.setTextColor(selectColor);
                break;
            case ECG:
                tv_xindian.setTextColor(selectColor);
                break;
        }
    }

    public void resetColor(){
        tv_jiancha.setTextColor(unSelectColor);
        tv_tijian.setTextColor(unSelectColor);
        tv_xindian.setTextColor(unSelectColor);
    }
    public Fragment creatFragment(Type type) {
        Fragment fragment = null;
        switch (type) {
            case INSPECT:
                //请求数据地址
                fragment= MyReportFragment.getInstance("");
                break;
            case EXAMINATION:
                //请求数据地址
                fragment= MyReportFragment.getInstance("");
                break;
            case ECG:
                //请求数据地址
                fragment= MyReportFragment.getInstance("");
                break;
        }

        return fragment;
    }


    public void addFragment(Type type) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, creatFragment(type)).commit();
    }
    public enum Type{
        INSPECT,//检查
        EXAMINATION,//体检
        ECG;//心电
    }

}

