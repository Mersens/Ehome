package com.zzu.ehome.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.umeng.analytics.MobclickAgent;
import com.zzu.ehome.R;
import com.zzu.ehome.adapter.MyFocusAdapter;
import com.zzu.ehome.application.Constants;
import com.zzu.ehome.bean.DoctorBean;
import com.zzu.ehome.bean.RefreshEvent;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.view.HeadView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by zzu on 2016/4/15.
 */
public class MyFocusActivity extends BaseActivity {
    private ListView listView;
    private String userid;
    private RequestMaker requestMaker;
    private List<DoctorBean> mList;
    private MyFocusAdapter adapter;
    private RelativeLayout rlFocus;
    int page=1;
    private final String mPageName = "MyFocusActivity";

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_focus);
        EventBus.getDefault().register(this);
        requestMaker = RequestMaker.getInstance();
        userid = SharePreferenceUtil.getInstance(MyFocusActivity.this).getUserId();
        initViews();
        initEvent();
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(mPageName);
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(mPageName);
        MobclickAgent.onPause(this);
    }


    public void initViews() {
        listView = (ListView) findViewById(R.id.listView);

        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "我的关注", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();

            }
        });
        rlFocus=(RelativeLayout) findViewById(R.id.rl_guan);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DoctorBean bean=  mList.get(position);
                Intent intent=new Intent(MyFocusActivity.this,AppointmentDetailActivity.class);
                intent.putExtra("department_id",bean.getDepartment_Id());
                intent.putExtra("doctor_id",bean.getUser_Id());
                intent.putExtra("User_FullName",bean.getUser_FullName());
                intent.putExtra("Department_FullName",bean.getDepartment_FullName());
                intent.putExtra("Hosptial_Name",bean.getHosptial_Name());
                intent.putExtra("MyFocus","MyFocus");
                startActivity(intent);
            }
        });
    }

    public void initEvent() {

//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                swipeRefreshLayout.setRefreshing(true);
//                getData();
//            }
//        });
        getData();


    }
    public void onEventMainThread(RefreshEvent event) {

        if(getResources().getInteger(R.integer.refresh_focus) == event
                .getRefreshWhere()) {

            getData();

        }
    }

    public void getData(){
        requestMaker.FavorDoctorInquiry(userid,10000+"",page+"",new JsonAsyncTask_Info(MyFocusActivity.this, true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                JSONObject mySO = (JSONObject) result;
                Log.e("MyFocus",mySO.toString());
                try {
                    JSONArray array = mySO.getJSONArray("FavorDoctorInquiry");
                    if (array.getJSONObject(0).has("MessageCode")) {
                        if(mList!=null&&mList.size()>0){
                            mList.clear();
                        }
                        rlFocus.setVisibility(View.VISIBLE);
                        //adapter.setmList(mList);
                        // adapter.notifyDataSetChanged();
                    }else {
                        mList = new ArrayList<DoctorBean>();
                        rlFocus.setVisibility(View.GONE);
                        for (int i = 0; i < array.length(); i++) {
                            DoctorBean bean = new DoctorBean();
                            JSONObject jsonObject = (JSONObject) array.get(i);
                            bean.setUser_Id(jsonObject.getString("Doctor_Id"));
                            bean.setUser_FullName(jsonObject.getString("User_FullName"));
                            String url = jsonObject.getString("User_Icon").replace("~", "").replace("\\", "/");
                            bean.setUser_Icon(Constants.ICON + url);
                            bean.setDoctor_Title(jsonObject.getString("Doctor_Title"));
                            bean.setDoctor_Specialty("简介:" + jsonObject.getString("Doctor_Specialty"));
                            bean.setDepartment_Id(jsonObject.getString("Department_Id"));
                            bean.setDepartment_FullName(jsonObject.getString("Department_FullName"));
                            bean.setHosptial_Name(jsonObject.getString("Hospital_FullName"));

                            mList.add(bean);
                        }
                        adapter = new MyFocusAdapter(MyFocusActivity.this,mList);
                        listView.setAdapter(adapter);
                        //adapter.setmList(mList);
                        adapter.notifyDataSetChanged();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {

                }
            }
        }));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
