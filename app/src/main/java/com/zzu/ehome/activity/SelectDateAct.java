package com.zzu.ehome.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zzu.ehome.R;
import com.zzu.ehome.application.Constants;
import com.zzu.ehome.bean.ScheduleBean;
import com.zzu.ehome.fragment.DoctorFragment;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.JsonTools;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.view.MonthDateView;
import com.zzu.ehome.view.wheel.wheelview.OnWheelScrollListener;
import com.zzu.ehome.view.wheel.wheelview.WheelView;
import com.zzu.ehome.view.wheel.wheelview.adapter.NumericWheelAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/6/23.
 */
public class SelectDateAct extends Activity {
    private ImageView iv_left;
    private ImageView iv_right;
    private TextView tv_date;
    private TextView tv_week;
    private TextView tv_today;
    private MonthDateView monthDateView;
    private TextView tvcancle;
    private TextView tv_ok;
    private int mYear;
    private int mMonth;
    private int mDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_date_notime);
        initViews();
        initEvents();
        setOnlistener();
    }

    private void initEvents() {


    }

    private void initViews() {

        tv_ok = (TextView) findViewById(R.id.tv_ok);
        iv_left = (ImageView) findViewById(R.id.iv_left);
        iv_right = (ImageView) findViewById(R.id.iv_right);
        monthDateView = (MonthDateView) findViewById(R.id.monthDateView);
        tv_date = (TextView) findViewById(R.id.date_text);
        tv_week = (TextView) findViewById(R.id.week_text);
        tvcancle = (TextView) findViewById(R.id.tv_cancel);
        monthDateView.setTextView(tv_date, tv_week);
        monthDateView.setDateClick(new MonthDateView.DateClick() {
            @Override
            public void onClickOnDate() {
            }
        });

        iv_right.setBackground(getResources().getDrawable(R.mipmap.icon_calender_arr_hui));
        monthDateView.setShow(true);
    }





    private void setOnlistener() {
        iv_left.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                monthDateView.onBackLeftClick(iv_right);
            }
        });

        iv_right.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                monthDateView.onBackRightClick(iv_right);

            }
        });

        tvcancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDate = monthDateView.getmSelDay();
                mMonth = monthDateView.getmSelMonth()+1;
                mYear = monthDateView.getmSelYear();
                String mMos,mDos;
                if(mMonth<10){
                    mMos="0"+mMonth;
                }else{
                    mMos=mMonth+"";
                }
                if(mDate<10){
                    mDos="0"+mDate;
                }else{
                    mDos=mDate+"";
                }
                String time = mYear + "-" + mMos + "-" + mDos;
                Intent intent = new Intent();
                intent.putExtra("time", time);
                setResult(Constants.REQUEST_CALENDAR, intent);
                finish();
            }
        });
    }



}