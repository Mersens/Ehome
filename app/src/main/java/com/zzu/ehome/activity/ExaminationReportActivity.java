package com.zzu.ehome.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.zzu.ehome.R;
import com.zzu.ehome.view.HeadView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mersens on 2016/8/20.
 */
public class ExaminationReportActivity extends BaseActivity {
    private ListView listView;
    private LinearLayout layout_none;


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.layout_examination_report);
        initViews();
        initEvent();
        initDatas();
    }

    public void initViews(){
        listView=(ListView)findViewById(R.id.listView);
        layout_none=(LinearLayout) findViewById(R.id.layout_none);
        setDefaultViewMethod(R.mipmap.icon_arrow_left, "体检报告", R.mipmap.icon_add_report, new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();

            }
        }, new HeadView.OnRightClickListener() {
            @Override
            public void onClick() {
                startActivity(new Intent(ExaminationReportActivity.this,AddExaminationReportActivity.class));
            }
        });

    }

    public void initEvent(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(ExaminationReportActivity.this,ExaminationReportDetailActivity.class));

            }
        });

    }

    public void initDatas(){
        final List<String> list=new ArrayList<>();
        for(int i=0;i<5;i++){
            list.add(i+"");
        }
        listView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public Object getItem(int position) {
                return list.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v= LayoutInflater.from(ExaminationReportActivity.this).inflate(R.layout.examination_report_item,null);
                return v;
            }
        });

    }

}
