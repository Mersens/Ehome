package com.zzu.ehome.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.zzu.ehome.R;
import com.zzu.ehome.adapter.DoctorListAdapter;
import com.zzu.ehome.bean.DepDateTemp;
import com.zzu.ehome.bean.DepTempBean;
import com.zzu.ehome.bean.DoctorBeanDes;
import com.zzu.ehome.bean.DortorlIst;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.JsonTools;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.view.HeadView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mersens on 2016/8/9.
 */
public class DoctorListActivity extends BaseActivity {
    private ListView listView;
    private DoctorListAdapter adapter;
    private RequestMaker requestMaker;
    private String hosid,depid;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        requestMaker=RequestMaker.getInstance();
        setContentView(R.layout.layout_doctor_list);
        initViews();
        hosid=this.getIntent().getStringExtra("HospitalID");
        depid=this.getIntent().getStringExtra("DepartmentID");
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
        requestMaker.DepertDoctorByTopmd(hosid,depid,new JsonAsyncTask_Info(
                DoctorListActivity.this, true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                String str=result.toString();

                try {
                    JSONObject mySO = (JSONObject) result;
                    JSONArray array = mySO
                            .getJSONArray("DepertDoctorByTopmd");
                    if (array.getJSONObject(0).has("MessageCode")) {
                        Toast.makeText(DoctorListActivity.this, array.getJSONObject(0).getString("MessageContent").toString(),
                                Toast.LENGTH_SHORT).show();
                    } else {
                        DortorlIst date = JsonTools.getData(result.toString(), DortorlIst.class);
                        List<DoctorBeanDes> mList=date.getData();
                        adapter=new DoctorListAdapter(DoctorListActivity.this,mList);
                           listView.setAdapter(adapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }));
//        List<String> mList=new ArrayList<>();
//        mList.add("白蓉");
//        mList.add("刘洪波");
//        mList.add("卢宏");
//        mList.add("孙石磊");
//        mList.add("陈晨");
//        adapter=new DoctorListAdapter(this,mList);
//        listView.setAdapter(adapter);

    }

}
