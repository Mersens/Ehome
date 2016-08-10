package com.zzu.ehome.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zzu.ehome.R;
import com.zzu.ehome.adapter.OfficeListAdapter;
import com.zzu.ehome.view.HeadView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mersens on 2016/8/9.
 */
public class OfficeListActivity extends BaseActivity {
    private ListView listView;
    private OfficeListAdapter adapter;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.layout_office_list);
        initViews();
        initEvent();
        initDatas();
    }

    public void initViews(){
        listView=(ListView) findViewById(R.id.listView);
        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "郑州大学第一附属医院", new HeadView.OnLeftClickListener() {
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
                startActivity(new Intent(OfficeListActivity.this,DoctorListActivity.class));
            }
        });
    }

    public void initDatas(){
        List<String> mList=new ArrayList<>();
        mList.add("神经内科");
        mList.add("肿瘤科");
        mList.add("康复医学科");
        mList.add("精神医学科");
        mList.add("中西医结合科");
        mList.add("放射治疗部");
        adapter=new OfficeListAdapter(this,mList);
        listView.setAdapter(adapter);

    }

}
