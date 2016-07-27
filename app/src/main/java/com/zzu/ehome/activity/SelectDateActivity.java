package com.zzu.ehome.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zzu.ehome.R;
import com.zzu.ehome.bean.ScheduleBean;
import com.zzu.ehome.fragment.DoctorFragment;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.JsonTools;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.view.MonthDateView;
import com.zzu.ehome.view.MonthDateView.DateClick;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SelectDateActivity extends Activity {
    private ImageView iv_left;
    private ImageView iv_right;
    private TextView tv_date;
    private TextView tv_week;
    private TextView tv_today;

    private MonthDateView monthDateView;
    private LinearLayout rl_time;

    private TextView[] mTabViews;
    private TextView tvcancle;
    private TextView tv_ok;

    private int mYear;
    private int mMonth;
    private int mDate;
    private int index;

    private String department_id;
    private String doctor_id;
    //请求单例
    private RequestMaker requestMaker;
    private List<ScheduleBean> mList;
    private List<Integer> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_date);
        requestMaker = RequestMaker.getInstance();
        department_id = getIntent().getStringExtra("department_id");
        doctor_id = getIntent().getStringExtra("doctor_id");
        initViews();
        initDatas();
        setOnlistener();


    }

    private void initViews() {

        tv_ok = (TextView) findViewById(R.id.tv_ok);
        iv_left = (ImageView) findViewById(R.id.iv_left);
        iv_right = (ImageView) findViewById(R.id.iv_right);
        monthDateView = (MonthDateView) findViewById(R.id.monthDateView);
        tv_date = (TextView) findViewById(R.id.date_text);
        tv_week = (TextView) findViewById(R.id.week_text);
        rl_time = (LinearLayout) findViewById(R.id.rl_time);
        mTabViews = new TextView[2];
        mTabViews[0] = (TextView) rl_time.findViewById(R.id.tv_am);
        mTabViews[1] = (TextView) rl_time.findViewById(R.id.tv_pm);
        tvcancle = (TextView) findViewById(R.id.tv_cancel);

        monthDateView.setTextView(tv_date, tv_week);

        monthDateView.setDateClick(new DateClick() {
            @Override
            public void onClickOnDate() {
/*                int mDate=monthDateView.getmSelDay();
                if(!list.contains(mDate)){
                    ToastUtils.showMessage(SelectDateActivity.this,"该时间段禁止预约");
                    return;

                }else{
                    rl_time.setVisibility(View.VISIBLE);
                    setButton(0);
                }*/
            }
        });
    }

    private void setButton(int postion) {

        for (int m = 0; m < mTabViews.length; m++) {
            mTabViews[m].setTextColor(getResources().getColor(R.color.base_color_text_black));
            mTabViews[m].setBackgroundColor(getResources().getColor(R.color.back_color));
        }
        mTabViews[postion].setTextColor(getResources().getColor(R.color.base_color_text_white));
        mTabViews[postion].setBackgroundResource(R.drawable.bg_select);
    }

    private void setOnlistener() {
        iv_left.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                rl_time.setVisibility(View.GONE);
                monthDateView.onLeftClick();
            }
        });

        iv_right.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                rl_time.setVisibility(View.GONE);
                monthDateView.onRightClick();

            }
        });
        mTabViews[0].setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setButton(0);
                index = 0;
            }
        });

        mTabViews[1].setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setButton(1);
                index = 1;
            }
        });
        tvcancle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_ok.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                String str = null;
                if (index == 0) {
                    str = "上午";
                } else if (index == 1) {
                    str = "下午";
                }
                mDate = monthDateView.getmSelDay();
                mMonth = monthDateView.getmSelMonth() + 1;
                mYear = monthDateView.getmSelYear();

                String time = mYear + "-" + mMonth + "-" + mDate + "  " + str;
                Intent intent = new Intent();
                intent.putExtra("time", time);
                setResult(DoctorFragment.ADD_TIME, intent);
                finish();
            }
        });
    }

    public void initDatas() {
        Log.e("TAG",department_id);
        Log.e("TAG",doctor_id);

        requestMaker.ScheduleInquiry(department_id, doctor_id, new JsonAsyncTask_Info(SelectDateActivity.this, true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                JSONObject mySO = (JSONObject) result;
                Log.e("TAG",result.toString());
                try {
                    JSONArray array = mySO.getJSONArray("ScheduleInquiry");
                    mList = new ArrayList<ScheduleBean>();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = (JSONObject) array.get(i);
                        mList.add(JsonTools.getData(jsonObject.toString(),ScheduleBean.class));
                    }
/*                    list = new ArrayList<Integer>();
                    String []starttime=getDateNum(mList.get(0).getSchedule_StartTime());
                    String []endtime=getDateNum(mList.get(0).getSchedule_EndTime());
                    int mstarttimeyear=Integer.parseInt(starttime[0]);
                    int mstarttimeMonth=Integer.parseInt(starttime[1]);
                    int mstarttimedate=Integer.parseInt(starttime[2].trim());
                    int mendtimeyear=Integer.parseInt(endtime[0]);
                    int mendtimeMonth=Integer.parseInt(endtime[1]);
                    int mendtimedate=Integer.parseInt(endtime[2].trim());
                    int startmMonthDays = DateUtils.getMonthDays(mstarttimeyear,mstarttimeMonth);
                    for(int i=mstarttimedate;i<=mendtimedate;i++){
                        list.add(i);
                    }*/

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }));
    }
/*
   private String[] getDateNum(String date){
        String str[]=new String[3];
        String formatDate=date.substring(0,date.length()-7);
        str[0]=formatDate.split("/")[0];
        str[1]=formatDate.split("/")[1];
        str[2]=formatDate.split("/")[2];
        return str;
    }*/

}
