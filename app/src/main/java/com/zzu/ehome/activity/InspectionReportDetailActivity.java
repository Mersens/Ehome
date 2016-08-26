package com.zzu.ehome.activity;

import android.os.Bundle;

import com.zzu.ehome.R;
import com.zzu.ehome.view.HeadView;

/**
 * Created by Mersens on 2016/8/23.
 */
public class InspectionReportDetailActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.layout_inspection_report_detail);
        initViews();
        initEvent();
        initDatas();

    }

    public void initViews(){
        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "TITLE", new HeadView.OnLeftClickListener() {
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
