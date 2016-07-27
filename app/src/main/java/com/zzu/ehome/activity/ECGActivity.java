package com.zzu.ehome.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.zzu.ehome.R;
import com.zzu.ehome.adapter.ECGAdapter;
import com.zzu.ehome.fragment.DynamicFragment;
import com.zzu.ehome.fragment.HealthDataFragment;
import com.zzu.ehome.fragment.HealthFilesFragment;
import com.zzu.ehome.fragment.StaticFragment;
import com.zzu.ehome.view.HeadView;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 2016/6/16.
 */
public class ECGActivity extends BaseActivity {
    private RelativeLayout layout_dt,layout_jt;
    private TextView tv_dt,tv_jt;
    private int selectcolor;
    private int unselectcolor;
    private ImageView imageview_jt,imageview_dt;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_ecg_empty);
        initViews();
        initEvent();
        addFragment(Style.DONGTAI);

    }
    public void initViews(){
        unselectcolor=getResources().getColor(R.color.text_color);
        selectcolor=getResources().getColor(R.color.actionbar_color);
        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "心电报告", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();
            }
        });
        layout_dt=(RelativeLayout) findViewById(R.id.layout_dt);
        layout_jt=(RelativeLayout) findViewById(R.id.layout_jt);
        tv_dt=(TextView) findViewById(R.id.tv_dt);
        tv_jt=(TextView) findViewById(R.id.tv_jt);
        imageview_jt=(ImageView) findViewById(R.id.imageview_jt);
        imageview_dt=(ImageView) findViewById(R.id.imageview_dt);

    }

    public void initEvent(){
        layout_dt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFragment(Style.DONGTAI);
            }
        });
        layout_jt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFragment(Style.JINGTAI);
            }
        });
    }

    public void addFragment(Style style) {
        setColor(style);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.container, getFragment(style)).commit();
    }
    public Fragment getFragment(Style style) {
        Fragment fragment = null;
        switch (style) {
            case DONGTAI:
                fragment= DynamicFragment.getInstance();
                break;
            case JINGTAI:
                fragment= StaticFragment.getInstance();
                break;
        }
        return fragment;
    }

    public void setColor(Style style){
        reSetColor();
        switch (style){
            case DONGTAI:
                tv_dt.setTextColor(selectcolor);
                imageview_dt.setImageResource(R.mipmap.icon_dongtai_pressed);
                break;
            case JINGTAI:
                tv_jt.setTextColor(selectcolor);
                imageview_jt.setImageResource(R.mipmap.icon_jingtai_pressed);
                break;
        }
    }

    public void reSetColor(){
        tv_dt.setTextColor(unselectcolor);
        tv_jt.setTextColor(unselectcolor);
        imageview_jt.setImageResource(R.mipmap.icon_jingtai_normal);
        imageview_dt.setImageResource(R.mipmap.icon_dongtai_normal);
    }
    public enum Style {
        DONGTAI, JINGTAI;
    }
}
