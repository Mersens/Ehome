package com.zzu.ehome.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zzu.ehome.R;
import com.zzu.ehome.adapter.NetHospitalAreaAdapter;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.view.HeadView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2016/6/17.
 */
public class NetHospitalAreaActivity extends BaseActivity {
    private ListView listview;
    private NetHospitalAreaAdapter adapter;
    List<Map<String, Object>> mList= new ArrayList<Map<String, Object>>();
    private RequestMaker requestMaker;
    String id,name;


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_net_hospital);
        requestMaker=RequestMaker.getInstance();
        id=getIntent().getStringExtra("Id");
        name=getIntent().getStringExtra("name");
        initViews();
        initEvent();
        initDatas();
    }

    public void initViews(){

        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, name, new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();
            }
        });
        listview=(ListView) findViewById(R.id.listview);

    }

    public void initEvent(){
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String subname=mList.get(position).get("Name").toString();

                Intent intent=new Intent(NetHospitalAreaActivity.this,InternetMap.class);
                intent.putExtra("Base",name);
                intent.putExtra("Name",subname);
                startActivity(intent);

            }
       });
    }

    public void initDatas(){
//        startProgressDialog();
        requestMaker.HospitalInquiryByRegion(id,new JsonAsyncTask_Info(NetHospitalAreaActivity.this, true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                String returnvalue = result.toString();
                try {

                    JSONObject mySO = (JSONObject) result;
                    JSONArray array = mySO.getJSONArray("HospitalInquiryByRegion");
                    stopProgressDialog();
                    if(array.getJSONObject(0).has("MessageCode")){

                    }else {
                        Map<String, Object> map;
                        for(int i=0;i<array.length();i++) {
                            map = new HashMap<String, Object>();
                            map.put("Name",
                                    array.getJSONObject(i).getString("Name"));
                            map.put("ID",
                                    array.getJSONObject(i).getString("ID"));
//                            map.put("Value",
//                                    array.getJSONObject(i).getString("Value"));
                            mList.add(map);
                        }
                        adapter=new NetHospitalAreaAdapter(NetHospitalAreaActivity.this,mList);
                        listview.setAdapter(adapter);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }));



    }
}
