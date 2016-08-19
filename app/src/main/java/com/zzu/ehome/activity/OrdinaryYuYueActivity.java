package com.zzu.ehome.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.zzu.ehome.R;
import com.zzu.ehome.adapter.YuYueAdapter;
import com.zzu.ehome.bean.HospitalBean;
import com.zzu.ehome.bean.HospitalInquiryByTopmd;
import com.zzu.ehome.bean.News;
import com.zzu.ehome.bean.NewsDate;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.JsonTools;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.view.HeadView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mersens on 2016/8/9.
 */
public class OrdinaryYuYueActivity extends BaseActivity {
    private YuYueAdapter adapter;
    private ListView listView;
    private RequestMaker requestMaker;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_ordinary_yy);
        requestMaker=RequestMaker.getInstance();
        initViews();
        initEvent();
        initDatas();
    }

    public void initViews(){
        listView=(ListView)findViewById(R.id.listView);
        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "普通预约挂号", new HeadView.OnLeftClickListener() {
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
             startActivity(new Intent(OrdinaryYuYueActivity.this,OfficeListActivity.class));
            }
        });

    }

    public void initDatas(){
        requestMaker.HospitalInquiryByTopmd(
                new JsonAsyncTask_Info(OrdinaryYuYueActivity.this, true, new JsonAsyncTaskOnComplete() {
                    @Override
                    public void processJsonObject(Object result) {

                        try {
                            JSONObject mySO = (JSONObject) result;
                            JSONArray array = mySO
                                    .getJSONArray("HospitalInquiryByTopmd");
                            if (array.getJSONObject(0).has("MessageCode")) {
                                Toast.makeText(OrdinaryYuYueActivity.this, array.getJSONObject(0).getString("MessageContent").toString(),
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                HospitalInquiryByTopmd date = JsonTools.getData(result.toString(), HospitalInquiryByTopmd.class);
                                List<HospitalBean> mList=date.getDate();
                                adapter=new YuYueAdapter(OrdinaryYuYueActivity.this,mList);
                                listView.setAdapter(adapter);
                            }

//                        ToastUtils.showMessage(getActivity(),result.toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                }
        ));




    }
}
