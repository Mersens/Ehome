package com.zzu.ehome.activity;

import android.os.Bundle;

import com.zzu.ehome.R;
import com.zzu.ehome.view.HeadView;

/**
 * Created by zzu on 2016/4/15.
 */
public class MyRemindActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_remind);
        initView();
        initEvent();

    }

    public void initView(){
        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "我的提醒", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();
            }
        });

    }
    public void initEvent(){

    }
}
