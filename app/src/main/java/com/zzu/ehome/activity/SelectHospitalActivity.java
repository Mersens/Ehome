package com.zzu.ehome.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.zzu.ehome.R;
import com.zzu.ehome.adapter.SelectHospitalAdapter;
import com.zzu.ehome.bean.HospitalBean;
import com.zzu.ehome.fragment.DoctorFragment;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.JsonTools;
import com.zzu.ehome.utils.RequestMaker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzu on 2016/4/13.
 */
public class SelectHospitalActivity extends NetBaseActivity {
    private ListView listView;
    private TextView tv_cancel;
    private TextView tv_ok;
    private int index = 0;
    private List<HospitalBean> mHList;
    //请求单例
    private RequestMaker requestMaker;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_selecthospital);
        requestMaker = RequestMaker.getInstance();
        initViews();
        initData();
        initEvent();
    }

    public void initViews() {
        listView = (ListView) findViewById(R.id.listView);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        tv_ok = (TextView) findViewById(R.id.tv_ok);
    }

    public void initData() {
        startProgressDialog();
        requestMaker.HospitalInquiry(new JsonAsyncTask_Info(SelectHospitalActivity.this, true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                JSONObject mySO = (JSONObject) result;
                try {
                    JSONArray array = mySO.getJSONArray("HospitalInquiry");
                    mHList=new ArrayList<HospitalBean>();

                    for(int i=0;i<array.length();i++){
                        JSONObject jsonObject=(JSONObject)array.get(i);
                        mHList.add(JsonTools.getData(jsonObject.toString(),HospitalBean.class));
                    }
                    listView.setAdapter(new SelectHospitalAdapter(SelectHospitalActivity.this, mHList));
                    stopProgressDialog();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }));

    }


    public void initEvent() {
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
                intent.putExtra("hospital", mHList.get(index).getHospital_FullName());
                intent.putExtra("hospital_id", mHList.get(index).getHospital_Id());
                setResult(DoctorFragment.ADD_HOSPITAL, intent);
                finish();

            }
        });

    }
}
