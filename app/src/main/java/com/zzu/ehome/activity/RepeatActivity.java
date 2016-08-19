package com.zzu.ehome.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.zzu.ehome.R;
import com.zzu.ehome.view.HeadView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mersens on 2016/8/18.
 */
public class RepeatActivity extends BaseActivity{
    private ListView listView;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.layout_repeat);
        initViews();
        initEvent();
        initDatas();
    }

    public void initViews(){
        listView=(ListView)findViewById(R.id.listView);
        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "重复", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();
            }
        });

    }


    public void initEvent(){

    }

    public void initDatas(){
        final List<String> mList=new ArrayList<>();
        mList.add("周一");
        mList.add("周二");
        mList.add("周三");
        mList.add("周四");
        mList.add("周五");
        mList.add("周六");
        mList.add("周日");
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
                View v= LayoutInflater.from(RepeatActivity.this).inflate(R.layout.repeat_item,null);
                TextView tv_time=(TextView)v.findViewById(R.id.tv_time);
                tv_time.setText(mList.get(position));
                return v;
            }
        });

    }

}
