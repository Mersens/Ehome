package com.zzu.ehome.activity;

import android.os.Bundle;

import com.zzu.ehome.R;
import com.zzu.ehome.view.HeadView;

/**
 * Created by Mersens on 2016/8/24.
 */
public class PersonalInfoActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.layout_personal_info);
        initViews();
        initEvent();
        initDatas();

    }


    public void initViews(){
        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "个人资料", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();

            }
        });


    }

    public void initEvent(){

    }

    public void initDatas(){

    }
}
