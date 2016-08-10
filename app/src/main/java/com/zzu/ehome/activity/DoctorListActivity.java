package com.zzu.ehome.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zzu.ehome.R;
import com.zzu.ehome.adapter.DoctorListAdapter;
import com.zzu.ehome.view.HeadView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mersens on 2016/8/9.
 */
public class DoctorListActivity extends BaseActivity {
    private ListView listView;
    private DoctorListAdapter adapter;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.layout_doctor_list);
        initViews();
        initEvent();
        initDatas();
    }
    public void initViews(){
        listView=(ListView) findViewById(R.id.listView);
        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "神经内科", new HeadView.OnLeftClickListener() {
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
                startActivity(new Intent(DoctorListActivity.this,DoctorTimeActivity.class));
            }
        });
    }
    public void initDatas(){
        List<String> mList=new ArrayList<>();
        mList.add("白蓉");
        mList.add("刘洪波");
        mList.add("卢宏");
        mList.add("孙石磊");
        mList.add("陈晨");
        adapter=new DoctorListAdapter(this,mList);
        listView.setAdapter(adapter);

    }

}
