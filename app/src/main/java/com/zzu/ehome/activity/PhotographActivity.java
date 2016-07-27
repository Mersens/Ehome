package com.zzu.ehome.activity;

import android.os.Bundle;

import com.zzu.ehome.R;
import com.zzu.ehome.view.HeadView;

/**
 * Created by dell on 2016/6/17.
 */
public class PhotographActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_photograp);
        initViews();
        initEvent();
        initDatas();
    }



    public void initViews(){
        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "拍报告", new HeadView.OnLeftClickListener() {
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
