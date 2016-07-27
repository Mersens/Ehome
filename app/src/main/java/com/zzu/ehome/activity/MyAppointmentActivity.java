package com.zzu.ehome.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.umeng.analytics.MobclickAgent;
import com.zzu.ehome.R;
import com.zzu.ehome.adapter.MyAppointmentAdapter;
import com.zzu.ehome.bean.DoctorBean;
import com.zzu.ehome.bean.TreatmentSearch;
import com.zzu.ehome.bean.TreatmentSearchDate;
import com.zzu.ehome.db.EHomeDao;
import com.zzu.ehome.db.EHomeDaoImpl;
import com.zzu.ehome.utils.DateUtils;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.JsonTools;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.ScreenUtils;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.view.HeadView;

import android.widget.BaseAdapter;
import org.json.JSONObject;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuLayout;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnSwipeListener;
import java.util.List;

/**
 * Created by zzu on 2016/4/15.
 */
public class MyAppointmentActivity extends BaseActivity {
    private SwipeMenuListView listViewCompat;
    private MyAppointmentAdapter adapter;
    private int index=0;
    private String userid,parentid;
    List<TreatmentSearch> list;
    //请求单例
    private RequestMaker requestMaker;
    private EHomeDao dao;
    int page=1;
    private final String mPageName = "MyAppointmentActivity";
    public RelativeLayout rl_yuyue;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.layout_my_appointment);
        requestMaker = RequestMaker.getInstance();
        userid= SharePreferenceUtil.getInstance(this).getUserId();
        dao= new EHomeDaoImpl(this);
        parentid=dao.findUserInfoById(userid).getPatientId();
        initViews();
        initEvent();
        initDatas();
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
    public void initDatas(){
        startProgressDialog();
        requestMaker.TreatmentSearch(parentid,1+"",10000+"",new JsonAsyncTask_Info(MyAppointmentActivity.this, true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                try {
                    JSONObject mySO = (JSONObject) result;
                    Log.e("tag",mySO.toString());
                    org.json.JSONArray array = mySO
                            .getJSONArray("TreatmentSearch");
                    stopProgressDialog();
                    if (array.getJSONObject(0).has("MessageCode")) {
//                        Toast.makeText(MyAppointmentActivity.this, array.getJSONObject(0).getString("MessageContent").toString(),
//                                Toast.LENGTH_SHORT).show();
                        rl_yuyue.setVisibility(View.VISIBLE);
                    }else{
                        rl_yuyue.setVisibility(View.GONE);
                        TreatmentSearchDate date = JsonTools.getData(result.toString(), TreatmentSearchDate.class);
                        list = date.getDate();
                        for(int i=0;i<list.size();i++){
                            TreatmentSearch ts=list.get(i);
                            list.get(i).setOverdue(DateUtils.Compare_date(ts.getTime(),DateUtils.getFormatTime()));
                            if(DateUtils.Compare_date(ts.getTime(),DateUtils.getFormatTime())){
                                list.get(i).setOpen(false);
                            }
                        }
                        adapter.setList(list);
                        adapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }));
    }

    public void initViews(){
        listViewCompat=(SwipeMenuListView)findViewById(R.id.listViewCompat);
        rl_yuyue=(RelativeLayout) findViewById(R.id.rl_yuyue);
        setDefaultTXViewMethod(R.mipmap.icon_arrow_left, "我的预约", "", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();
            }
        }, new HeadView.OnRightClickListener() {
            @Override
            public void onClick() {
//                if(list!=null) {
//                    if (index % 2 == 0) {
//                        setRightText("取消");
//                        if (adapter != null) {
//                            adapter.openAll();
//                        }
//                    } else {
//                        setRightText("管理");
//                        if (adapter != null) {
//                            adapter.shrinkAll();
//                        }
//                    }
//                    index++;
//                }
            }
        });
    }

    public void initEvent(){
        adapter=new MyAppointmentAdapter(MyAppointmentActivity.this);
        listViewCompat.setAdapter(adapter);

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // Create different menus depending on the view type
                switch (menu.getViewType()) {
                    case 0:
                        createMenu1(menu);
                        break;
                    case 1:
                        createMenu2(menu);
                        break;

                }
            }

            private void createMenu1(SwipeMenu menu) {
                SwipeMenuItem delItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background

                delItem.setBackground(new ColorDrawable(Color.rgb(0xF9,0x3F, 0x25)));
                // set item width
                delItem.setWidth(ScreenUtils.dip2px(MyAppointmentActivity.this,90));
                // set item title
                delItem.setTitle("取消预约");
                // set item title fontsize
                delItem.setTitleSize(18);
                // set item title font color
                delItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(delItem);
            }

            private void createMenu2(SwipeMenu menu) {
                SwipeMenuItem item1 = new SwipeMenuItem(
                        getApplicationContext());

                menu.addMenuItem(item1);


            }


        };

            listViewCompat.setMenuCreator(creator);
        listViewCompat.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(int position, SwipeMenu menu, int index) {
                TreatmentSearch item = list.get(position);
                switch (index) {
                    case 0:
                        // open
                        deleteRecent(item,position);
                        break;

                }
            }
        });




        listViewCompat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TreatmentSearch bean=  list.get(position);
                if(!bean.isOverdue()) {
                    Intent i = new Intent(MyAppointmentActivity.this, AppointmentDetailActivity.class);
                    i.putExtra("doctor_id", bean.getDoctorId());
                    i.putExtra("Hosptial_Name", bean.getHospital());
                    i.putExtra("yuyue", "yuyue");
                    startActivity(i);
                }
            }
        });

    }


    public void deleteRecent(TreatmentSearch bean,final int position) {

        String id = bean.getReservationId();
        requestMaker.TreatmentCancel(id, new JsonAsyncTask_Info(MyAppointmentActivity.this, true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                try {
                    Log.e("Log", result.toString());
                    JSONObject mySO = (JSONObject) result;
                    org.json.JSONArray array = mySO
                            .getJSONArray("TreatmentCancel");
                    if (array.getJSONObject(0).has("MessageCode")) {
                        Toast.makeText(MyAppointmentActivity.this, array.getJSONObject(0).getString("MessageContent").toString(),
                                Toast.LENGTH_SHORT).show();
                        list.remove(position);
                        adapter.notifyDataSetChanged();
                        if(list.size()==0){

                            rl_yuyue.setVisibility(View.VISIBLE);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }));

    }
}
