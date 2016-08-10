package com.zzu.ehome.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.zzu.ehome.R;
import com.zzu.ehome.fragment.DoctorFragment;
import com.zzu.ehome.view.HeadView;

/**
 * Created by Mersens on 2016/8/10.
 */
public class YuYueShiPinActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.layout_yuyue_shipin);
        initViews();
        initEvent();
        initDatas();
    }

    public void initViews(){
        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "预约视频问诊", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();
            }
        });

    }
    public void initEvent(){}
    public void initDatas(){
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction ft= fm.beginTransaction();
        ft.add(R.id.fragment_container, DoctorFragment.getInstance()).commit();
    }
}
