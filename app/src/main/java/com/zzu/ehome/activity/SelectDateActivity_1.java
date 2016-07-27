package com.zzu.ehome.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zzu.ehome.R;
import com.zzu.ehome.adapter.SelectDateAdapter;
import com.zzu.ehome.bean.ScheduleBean;
import com.zzu.ehome.db.EHomeDao;
import com.zzu.ehome.db.EHomeDaoImpl;
import com.zzu.ehome.fragment.DoctorFragment;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.utils.ToastUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class SelectDateActivity_1 extends Activity {
    private String department_id;
    private String doctor_id;
    //请求单例
    private RequestMaker requestMaker;
    private List<ScheduleBean> mList;
    private ListView listView;
    private LinearLayout rl_time;
    private TextView[] mTabViews;
    private TextView tv_cancel;
    private TextView tv_ok;
    private int index = -1;
    private int btn_indext = -1;
    private String userid,PatientId;
    private Set<String> times;
    private List<String> listtimes;
    private Map<String, List<ScheduleBean>> map;
    private EHomeDao dao;
    private SelectDateAdapter mAapter;
   private TextView tvnotime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_date_1);
        requestMaker = RequestMaker.getInstance();
        department_id = getIntent().getStringExtra("department_id");
        doctor_id = getIntent().getStringExtra("doctor_id");
        userid = SharePreferenceUtil.getInstance(this).getUserId();
        dao= new EHomeDaoImpl(this);
        PatientId=dao.findUserInfoById(userid).getPatientId();
        initViews();
        initDatas();
        initEvent();
        times=new HashSet<String>();
    }

    private void initViews() {
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        tv_ok = (TextView) findViewById(R.id.tv_ok);
        listView = (ListView) findViewById(R.id.listView);
        rl_time = (LinearLayout) findViewById(R.id.rl_time);
        tvnotime=(TextView) findViewById(R.id.tv_notime);
        mTabViews = new TextView[2];
        mTabViews[0] = (TextView) rl_time.findViewById(R.id.tv_am);
        mTabViews[1] = (TextView) rl_time.findViewById(R.id.tv_pm);

    }


    public void initEvent() {
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btn_indext == -1 || index == -1) {
                    ToastUtils.showMessage(SelectDateActivity_1.this, "请选择预约时间");
                    return;

                }
                if(index==-2){
                    finish();
                    return;
                }
                String time = listtimes.get(btn_indext);
                List<ScheduleBean> list = map.get(time);
                Intent intent = new Intent();
                intent.putExtra("DateStr", list.get(index).getDate());
                intent.putExtra("TimeSpanStr",list.get(index).getTimeSlot());
                intent.putExtra("PerTime",list.get(index).getPerTime());
                setResult(DoctorFragment.ADD_TIME, intent);
                finish();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String time = listtimes.get(position);
                List<ScheduleBean> list = map.get(time);
                view.setBackgroundResource(R.color.item_color);
                mAapter.setCurr(position);
                mAapter.notifyDataSetChanged();
                if (list != null && list.size() > 0) {
                    btn_indext = position;

                    if (list.size() == 2) {

                            rl_time.setVisibility(View.VISIBLE);
                        mTabViews[0].setVisibility(View.VISIBLE);
                        mTabViews[1].setVisibility(View.VISIBLE);
                            for (int m=0;m<list.size();m++) {

                              setTextView(m,list.get(m));

                            }
                        mTabViews[0].setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                setButton(0);
                            }
                        });

                        mTabViews[1].setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                setButton(1);
                            }
                        });
                        int i1=Integer.valueOf(list.get(0).getStatus());
                        int i2=Integer.valueOf(list.get(1).getStatus());
                        if(i1==1){
                            setButton(0);
                            return;
                        }
                        if(i2==1){
                            setButton(1);
                            return;
                        }


                    } else {

                            rl_time.setVisibility(View.VISIBLE);
                            for (ScheduleBean b : list) {
                                int i=Integer.valueOf(list.get(0).getStatus());
                                if (b.getTimeBlock().equals("上午")) {
                                    mTabViews[0].setVisibility(View.GONE);
                                    mTabViews[1].setVisibility(View.GONE);
                                    if (!TextUtils.isEmpty(b.getTimeSlot())) {


                                        setTextView(0,list.get(0));
                                        mTabViews[0].setVisibility(View.VISIBLE);
                                        if(i==1)
                                        setSingleButton(0);
                                    }


                                } else if (b.getTimeBlock().equals("下午")) {
                                    mTabViews[0].setVisibility(View.GONE);
                                    mTabViews[1].setVisibility(View.GONE);
                                    if (!TextUtils.isEmpty(b.getTimeSlot())) {

                                        mTabViews[1].setVisibility(View.VISIBLE);
                                        setTextView(1,list.get(0));
                                        if(i==1)
                                        setSingleButton(1);
                                    }


                                }
                            }
                        mTabViews[0].setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                setSingleButton(0);
                            }
                        });

                        mTabViews[1].setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                setSingleButton(1);
                            }
                        });
                        }

                }

            }


        });
    }

    private void setTextView(int m, ScheduleBean scheduleBean) {
        int i=Integer.valueOf(scheduleBean.getStatus());
        switch (i){
            case -1:
                mTabViews[m].setText("请假");
                mTabViews[m].setEnabled(false);
                index=-2;
                mTabViews[m].setTextColor(getResources().getColor(R.color.base_color_text_white));
                mTabViews[m].setBackgroundResource(R.drawable.bg_select_gray);

                break;
            case 0:
                index=-2;
                mTabViews[m].setText("没有排班");
                mTabViews[m].setEnabled(false);
                mTabViews[m].setTextColor(getResources().getColor(R.color.base_color_text_white));
                mTabViews[m].setBackgroundResource(R.drawable.bg_select_gray);
                break;
            case 1:
                mTabViews[m].setText(scheduleBean.getTimeBlock());
                mTabViews[m].setEnabled(true);
                break;
            case 2:
                index=-2;
                mTabViews[m].setText("已满");
                mTabViews[m].setEnabled(false);
                mTabViews[m].setTextColor(getResources().getColor(R.color.base_color_text_white));
                mTabViews[m].setBackgroundResource(R.drawable.bg_select_gray);
                break;
            case 3:
                index=-2;
                mTabViews[m].setText("停诊");
                mTabViews[m].setEnabled(false);
                mTabViews[m].setTextColor(getResources().getColor(R.color.base_color_text_white));
                mTabViews[m].setBackgroundResource(R.drawable.bg_select_gray);
                break;
            case 4:
                index=-2;
                mTabViews[m].setText("已预约");
                mTabViews[m].setEnabled(false);
                mTabViews[m].setTextColor(getResources().getColor(R.color.base_color_text_white));
                mTabViews[m].setBackgroundResource(R.drawable.bg_select_gray);
                break;

        }
    }


    private void setButton(int postion) {
        for (int m = 0; m < mTabViews.length; m++) {
            if(mTabViews[m].getText().equals("上午")||mTabViews[m].getText().equals("下午")) {
                mTabViews[m].setTextColor(getResources().getColor(R.color.base_color_text_black));
                mTabViews[m].setBackgroundResource(R.color.back_color);
            }
        }
        mTabViews[postion].setTextColor(getResources().getColor(R.color.base_color_text_white));
        mTabViews[postion].setBackgroundResource(R.drawable.bg_select);
        index = postion;
    }
    /*
    单个上下午
     */
    private void setSingleButton(int postion) {
        for (int m = 0; m < mTabViews.length; m++) {
            if(mTabViews[m].getText().equals("上午")||mTabViews[m].getText().equals("下午")) {
                mTabViews[m].setTextColor(getResources().getColor(R.color.base_color_text_white));
                mTabViews[m].setBackgroundResource(R.color.back_color);
            }
        }
        mTabViews[postion].setTextColor(getResources().getColor(R.color.base_color_text_white));
        mTabViews[postion].setBackgroundResource(R.drawable.bg_select);
        index = 0;
    }

    public void initDatas() {

        requestMaker.ScheduleInquiry(doctor_id,PatientId, new JsonAsyncTask_Info(SelectDateActivity_1.this, true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                JSONObject mySO = (JSONObject) result;
                Log.e("TAG", mySO.toString());
                Date date = new Date();
                try {
                    JSONArray array = mySO.getJSONArray("ScheduleInquiry");
                    mList = new ArrayList<ScheduleBean>();
                    times = new TreeSet<String>();
                    for (int i = 0; i < array.length(); i++) {
                        ScheduleBean bean = new ScheduleBean();
                        JSONObject jsonObject = (JSONObject) array.get(i);
                        bean.setStatus(jsonObject.getString("Status"));
                        bean.setNum(jsonObject.getString("num"));
                        bean.setDate(jsonObject.getString("Date"));

                        bean.setPerTime(jsonObject.getString("PerTime"));
                        bean.setWeekDay(jsonObject.getString("WeekDay"));
                        bean.setPatientCount(jsonObject.getString("PatientCount"));
                        bean.setTimeSlot(jsonObject.getString("TimeSlot"));

                        bean.setTimeBlock(jsonObject.getString("TimeBlock"));
                        if(!TextUtils.isEmpty(jsonObject.getString("TimeSlot"))) {
                            mList.add(bean);
                            times.add(jsonObject.getString("Date"));
                        }
                    }
                    if(times.size()>0) {
                        listtimes = new ArrayList<String>(times);
                        mAapter = new SelectDateAdapter(SelectDateActivity_1.this, listtimes);
                        listView.setAdapter(mAapter);
                        setData();
                    }else{
                        tvnotime.setVisibility(View.VISIBLE);
                        index=-2;
                        btn_indext=-2;

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    tvnotime.setVisibility(View.VISIBLE);
                    index=-2;
                    btn_indext=-2;
                }
            }
        }));
    }


    public void setData() {
        map = new HashMap<>();
        for (int i = 0; i < listtimes.size(); i++) {
            List<ScheduleBean> list = new ArrayList<ScheduleBean>();
            for (int j = 0; j < mList.size(); j++) {
                if (listtimes.get(i).equals(mList.get(j).getDate())) {
                    list.add(mList.get(j));
                    map.put(listtimes.get(i), list);
                }
            }
        }
    }
}
