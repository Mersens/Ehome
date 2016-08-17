package com.zzu.ehome.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zzu.ehome.R;
import com.zzu.ehome.utils.ToastUtils;

/**
 * Created by Mersens on 2016/8/16.
 */
public class DoctorDetialActivity extends BaseActivity implements View.OnClickListener {
    private ImageView icon_back, icon_share;
    private LinearLayout layout_ljyy, layout_mfzx;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_doctor_detial);
        initViews();
        initEvent();
        initDatas();
    }

    public void initViews() {
        icon_back = (ImageView) findViewById(R.id.icon_back);
        icon_share = (ImageView) findViewById(R.id.icon_share);
        layout_ljyy = (LinearLayout) findViewById(R.id.layout_ljyy);
        layout_mfzx = (LinearLayout) findViewById(R.id.layout_mfzx);
    }

    public void initEvent() {
        icon_back.setOnClickListener(this);
        icon_share.setOnClickListener(this);
        layout_ljyy.setOnClickListener(this);
        layout_mfzx.setOnClickListener(this);
    }

    public void initDatas() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.icon_share:
                ToastUtils.showMessage(this, "点击分享");
                break;
            case R.id.icon_back:
                finishActivity();
                break;
            case R.id.layout_ljyy:
                ToastUtils.showMessage(this, "立即签约");
                break;
            case R.id.layout_mfzx:
                ToastUtils.showMessage(this, "免费咨询");
                break;
        }

    }
}
