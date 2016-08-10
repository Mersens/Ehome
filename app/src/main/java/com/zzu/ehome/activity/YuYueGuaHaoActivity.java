package com.zzu.ehome.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.zzu.ehome.R;
import com.zzu.ehome.adapter.YuYueAdapter;
import com.zzu.ehome.bean.HospitalBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mersens on 2016/8/9.
 */
public class YuYueGuaHaoActivity extends BaseActivity {
    private ImageView icon_back;
    private LinearLayout layout_yygh, layout_spwz;
    private ListView listView;
    private YuYueAdapter adapter;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_yygh);
        initViews();
        initEvent();
        initDatas();
    }

    public void initViews() {
        icon_back = (ImageView) findViewById(R.id.icon_back);
        layout_yygh = (LinearLayout) findViewById(R.id.layout_yygh);
        layout_spwz = (LinearLayout) findViewById(R.id.layout_spwz);
        listView=(ListView)findViewById(R.id.listView);
    }

    public void initEvent() {
        icon_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishActivity();
            }
        });
        layout_yygh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(YuYueGuaHaoActivity.this,OrdinaryYuYueActivity.class));

            }
        });
        layout_spwz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(YuYueGuaHaoActivity.this,YuYueShiPinActivity.class));

            }
        });
    }


    public void initDatas() {

        List<HospitalBean> mList=new ArrayList<>();
        mList.add(new HospitalBean());
        mList.add(new HospitalBean());
        mList.add(new HospitalBean());
        mList.add(new HospitalBean());
        adapter=new YuYueAdapter(this,mList);
        listView.setAdapter(adapter);

    }

}
