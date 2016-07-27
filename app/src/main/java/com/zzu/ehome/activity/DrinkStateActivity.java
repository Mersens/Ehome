package com.zzu.ehome.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.zzu.ehome.R;
import com.zzu.ehome.adapter.SelectMarriageAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 2016/6/1.
 */
public class DrinkStateActivity extends NetBaseActivity {
    private ListView listView;
    private TextView tv_cancel;
    private TextView tv_ok;
    private List<String> list;
    private TextView marriage;

    private int index = 0;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.layout_select_marriage);
        initViews();
        initEvent();
        initDatas();
    }
    public void initViews(){
        listView=(ListView) findViewById(R.id.listView);
        tv_cancel=(TextView) findViewById(R.id.tv_cancel);
        tv_ok=(TextView) findViewById(R.id.tv_ok);
        marriage=(TextView) findViewById(R.id.marriage);
        marriage.setText("饮酒状况");


    }

    public void initEvent(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                index = position;
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("drink", list.get(index));
                setResult(BaseFilesActivity.DRINK_STATE, intent);
                finish();
            }
        });
    }

    public void initDatas(){
        list=new ArrayList<String>();
        list.add("从不");
        list.add("偶尔");
        list.add("经常");
        list.add("每天");
        list.add("已戒酒");
        listView.setAdapter(new SelectMarriageAdapter(this,list));
    }

}
