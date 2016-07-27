package com.zzu.ehome.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.zzu.ehome.R;
import com.zzu.ehome.adapter.SuggarAdapter;
import com.zzu.ehome.fragment.BloodSugarFragment;

import java.util.ArrayList;
import java.util.List;

public class HosListActivity extends Activity {



    private ListView listView;
    private TextView tv_cancel;
    private TextView tv_ok;
    private int index=0;
    private List<String> list;
    private SuggarAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hos_list);
        initViews();
        initEvent();
        initData();

    }
    public void initViews(){
        listView=(ListView) findViewById(R.id.listView);
        tv_cancel=(TextView) findViewById(R.id.tv_cancel);
        tv_ok=(TextView) findViewById(R.id.tv_ok);
    }

    public void initData(){

        list=new ArrayList<String>();
        list.add("河南省人民医院");
        list.add("郑州大学第一附属医院");
        list.add("郑州大学第二附属医院");
        list.add("郑州大学第三附属医院");
        list.add("郑州市中心医院");
        list.add("郑州大学第五附属医院");
        list.add("河南省肿瘤医院");
        list.add("河南省妇幼保健院");
        list.add("河南省军区医院");
        list.add("河南省中医院");
        list.add("河南中医学院第一附属医院");
        list.add("郑州市第一人民医院");
        list.add("郑州市妇幼保健院");
        list.add("郑州解放军153医院");
        list.add("中原油田总医院");
        list.add("郑州商都妇产医院");
        list.add("郑州市红十字医院");
        list.add("郑州市儿童医院");
        list.add("河南省中医药研究院");
        mAdapter=new SuggarAdapter(this,list);
        listView.setAdapter(mAdapter);
    }


    public void initEvent(){
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                index=position;
//            }
//        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mAdapter.getCurrent()!=-1) {
                    Intent intent=new Intent();
                    intent.putExtra("times", mAdapter.getItem(mAdapter.getCurrent()));
                    setResult(BloodSugarFragment.ADD_TIMES, intent);
                }
                finish();

            }
        });

    }
}
