package com.zzu.ehome.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.zzu.ehome.R;
import com.zzu.ehome.view.HeadView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mersens on 2016/8/18.
 */
public class MyRemindActivity1 extends BaseActivity {
    private ListView listView;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_remind1);
        initViews();
        initEvent();
        initDatas();
    }

    public void initViews(){
        listView=(ListView)findViewById(R.id.listView);
        setDefaultTXViewMethod(R.mipmap.icon_arrow_left, "我的提醒", "添加", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();
            }
        }, new HeadView.OnRightClickListener() {
            @Override
            public void onClick() {
                startActivity(new Intent(MyRemindActivity1.this,AddRemindActivity.class));


            }
        });

    }

    public void initEvent(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }


    public void initDatas(){
        final List<Integer> mList=new ArrayList<>();
        for(int i=0;i<3;i++){
            mList.add(i);
        }
        listView.setAdapter(new BaseAdapter() {
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
                View v= LayoutInflater.from(MyRemindActivity1.this).inflate(R.layout.item_my_remind,null);
                return v;
            }
        });

    }

}
