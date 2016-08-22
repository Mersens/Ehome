package com.zzu.ehome.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.zzu.ehome.R;
import com.zzu.ehome.view.HeadView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mersens on 2016/8/22.
 */
public class ExaminationReportDetailActivity extends BaseActivity {
    private GridView gridView;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.layout_examination_report_detail);
        initViews();
        initEvent();
        initDatas();
    }


    public void initViews(){
        gridView=(GridView)findViewById(R.id.gridView) ;
        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "体检报告", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();
            }
        });

    }

    public void initEvent(){
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

    }

    public void initDatas(){

        final List<Integer> mList=new ArrayList<>();
        for(int i=0;i<6;i++){
            mList.add(i);

        }

        gridView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return mList.size();
            }

            @Override
            public Object getItem(int position) {
                return mList.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v= LayoutInflater.from(ExaminationReportDetailActivity.this).inflate(R.layout.examination_report_gridview_item,null);
                return v;
            }
        });

    }
}
