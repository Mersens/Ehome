package com.zzu.ehome.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.zzu.ehome.R;
import com.zzu.ehome.utils.ToastUtils;
import com.zzu.ehome.view.HeadView;

/**
 * Created by Mersens on 2016/8/18.
 */
public class AddRemindActivity extends BaseActivity {
    private RelativeLayout layout_time, layout_repeat, layout_type;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_add_remind);
        initViews();
        initEvent();
        initDatas();

    }

    public void initViews() {
        layout_time = (RelativeLayout) findViewById(R.id.layout_time);
        layout_repeat = (RelativeLayout) findViewById(R.id.layout_repeat);
        layout_type = (RelativeLayout) findViewById(R.id.layout_type);
        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "添加提醒", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();
            }
        });
    }


    public void initEvent() {
        layout_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showMessage(AddRemindActivity.this,"时间");

            }
        });
        layout_repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddRemindActivity.this,RepeatActivity.class));

            }
        });
        layout_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddRemindActivity.this,RemindTypeActivity.class));
            }
        });

    }


    public void initDatas() {
    }


}
