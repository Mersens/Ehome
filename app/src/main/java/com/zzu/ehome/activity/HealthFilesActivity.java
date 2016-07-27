package com.zzu.ehome.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.zzu.ehome.R;
import com.zzu.ehome.fragment.HealthFilesFragment1;
import com.zzu.ehome.view.HeadView;

/**
 * Created by Mersens on 2016/7/27.
 */
public class HealthFilesActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.layout_health_activity);
        initViews();
        initEvent();
        initDatas();
    }
    public void initViews(){
        setDefaultViewMethod(R.mipmap.icon_arrow_left, "健康档案", R.mipmap.icon_editor, new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();
            }
        }, new HeadView.OnRightClickListener() {
            @Override
            public void onClick() {
                finishActivity();
            }
        });


    }
    public void initEvent(){}
    public void initDatas(){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, HealthFilesFragment1.getInstance()).commit();
    }

}
