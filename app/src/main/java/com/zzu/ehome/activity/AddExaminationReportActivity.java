package com.zzu.ehome.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.zzu.ehome.R;
import com.zzu.ehome.utils.ToastUtils;
import com.zzu.ehome.view.HeadView;

/**
 * Created by Mersens on 2016/8/20.
 */
public class AddExaminationReportActivity extends BaseActivity {
    private RelativeLayout layout_time, layout_jg, layout_person;
    private ImageView icon_upload;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_examination_report);
        initViews();
        initEvent();
        initDatas();
    }

    public void initViews() {
        icon_upload = (ImageView) findViewById(R.id.icon_upload);
        layout_time = (RelativeLayout) findViewById(R.id.layout_time);
        layout_jg = (RelativeLayout) findViewById(R.id.layout_jg);
        layout_person = (RelativeLayout) findViewById(R.id.layout_person);
        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "添加报告", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();
            }
        });

    }


    public void initEvent() {
        icon_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showMessage(AddExaminationReportActivity.this,"添加图片");

            }
        });
        layout_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showMessage(AddExaminationReportActivity.this,"体检日期");
            }
        });
        layout_jg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showMessage(AddExaminationReportActivity.this,"体检机构");
            }
        });
        layout_person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showMessage(AddExaminationReportActivity.this,"体检人");
            }
        });


    }


    public void initDatas() {

    }
}
