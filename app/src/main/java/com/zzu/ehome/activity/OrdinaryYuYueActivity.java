package com.zzu.ehome.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zzu.ehome.R;
import com.zzu.ehome.adapter.YuYueAdapter;
import com.zzu.ehome.bean.HospitalBean;
import com.zzu.ehome.view.HeadView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mersens on 2016/8/9.
 */
public class OrdinaryYuYueActivity extends BaseActivity {
    private YuYueAdapter adapter;
    private ListView listView;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_ordinary_yy);
        initViews();
        initEvent();
        initDatas();
    }

    public void initViews(){
        listView=(ListView)findViewById(R.id.listView);
        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "普通预约挂号", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();
            }
        });

    }
    public void initEvent(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
             startActivity(new Intent(OrdinaryYuYueActivity.this,OfficeListActivity.class));
            }
        });

    }

    public void initDatas(){
        List<HospitalBean> mList=new ArrayList<>();
        mList.add(new HospitalBean());
        mList.add(new HospitalBean());
        mList.add(new HospitalBean());
        mList.add(new HospitalBean());
        adapter=new YuYueAdapter(this,mList);
        listView.setAdapter(adapter);

    }
}
