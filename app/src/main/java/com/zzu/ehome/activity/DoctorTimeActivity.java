package com.zzu.ehome.activity;

import android.os.Bundle;
import android.widget.ListView;

import com.zzu.ehome.R;
import com.zzu.ehome.adapter.DoctorTimeAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mersens on 2016/8/9.
 */
public class DoctorTimeActivity extends BaseActivity {
    private ListView listView;
    private DoctorTimeAdapter adapter;


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.layout_doctor_time);
        initViews();
        initEvent();
        initDatas();
    }

    public void initViews(){
        listView=(ListView)findViewById(R.id.listView);


    }
    public void initEvent(){

    }

    public void initDatas(){
        List<String> mList=new ArrayList<>();
        for(int i=0;i<5;i++){
            mList.add(i+"");
        }
        adapter=new DoctorTimeAdapter(this,mList);
        listView.setAdapter(adapter);
    }
}
