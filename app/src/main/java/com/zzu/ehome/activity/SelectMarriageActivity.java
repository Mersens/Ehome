package com.zzu.ehome.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.zzu.ehome.R;
import com.zzu.ehome.adapter.SelectMarriageAdapter;
import com.zzu.ehome.fragment.DoctorFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 2016/6/1.
 */
public class SelectMarriageActivity extends NetBaseActivity {
    private ListView listView;
    private TextView tv_cancel;
    private TextView tv_ok;
    private List<String> list;
    private int index = 0;
    private String marriage=null;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.layout_select_marriage);
        marriage=getIntent().getStringExtra("marriage");
        initViews();
        initEvent();
        initDatas();
    }

    public void initViews(){
        listView=(ListView) findViewById(R.id.listView);
        tv_cancel=(TextView) findViewById(R.id.tv_cancel);
        tv_ok=(TextView) findViewById(R.id.tv_ok);

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
                intent.putExtra("marriage", list.get(index));
                setResult(BaseFilesActivity.MARRIAGE_STATE, intent);
                finish();
            }
        });
    }

    public void initDatas(){
        list=new ArrayList<String>();
        list.add("未婚");
        list.add("已婚");
        listView.setAdapter(new SelectMarriageAdapter(this,list));
        if(TextUtils.isEmpty(marriage)){
            index=0;
        }else{
            if("已婚".equals(marriage)){
                index=1;
            }
        }
        listView.setItemChecked(index,true);
    }
}
