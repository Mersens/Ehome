package com.zzu.ehome.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zzu.ehome.R;
import com.zzu.ehome.adapter.DoctorTimeAdapter;
import com.zzu.ehome.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mersens on 2016/8/9.
 */
public class DoctorTimeActivity extends BaseActivity {
    private ListView listView;
    private DoctorTimeAdapter adapter;
    private ImageView icon_back;
    private ImageView icon_share;
    private TextView tv_title;


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.layout_doctor_time);
        initViews();
        initEvent();
        initDatas();
    }

    public void initViews() {
        listView = (ListView) findViewById(R.id.listView);
        icon_back = (ImageView) findViewById(R.id.icon_back);
        icon_share = (ImageView) findViewById(R.id.icon_share);
        tv_title = (TextView) findViewById(R.id.tv_title);
    }

    public void initEvent() {
        icon_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishActivity();
            }
        });
        icon_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showMessage(DoctorTimeActivity.this, "点击分享");
            }
        });

    }

    public void initDatas() {
        List<String> mList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            mList.add(i + "");
        }
        adapter = new DoctorTimeAdapter(this, mList);
        listView.setAdapter(adapter);
    }
}
