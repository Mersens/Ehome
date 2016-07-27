package com.zzu.ehome.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View.OnClickListener;
import com.zzu.ehome.R;
import com.zzu.ehome.application.Constants;
import com.zzu.ehome.utils.CommonUtils;
import com.zzu.ehome.view.MonthDateView;
import com.zzu.ehome.view.wheel.wheelview.OnWheelScrollListener;
import com.zzu.ehome.view.wheel.wheelview.WheelView;
import com.zzu.ehome.view.wheel.wheelview.adapter.NumericWheelAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/4/15.
 */
public class SelectDateAndTime extends Activity{
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

    private WheelView mHour;
    private WheelView mMin;

    private int mHours=0;
    private int mMinus=1;

    private String mH,mM;
    private Long dateCurr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_time);
        initViews();
        initEvents();
        setOnlistener();
    }

    private void initEvents() {

        NumericWheelAdapter numericWheelAdapter3=new NumericWheelAdapter(this,0, 23, "%02d");
        numericWheelAdapter3.setLabel("时");
        mHour.setViewAdapter(numericWheelAdapter3);
        mHour.setCyclic(true);
        mHour.addScrollingListener(scrollListener);


        NumericWheelAdapter numericWheelAdapter4=new NumericWheelAdapter(this,0, 59, "%02d");
        numericWheelAdapter4.setLabel("分");
        mMin.setViewAdapter(numericWheelAdapter4);
        mMin.setCyclic(true);
        mMin.addScrollingListener(scrollListener);
        Date d = new Date();
        mHours = d.getHours();
        mMinus=d.getMinutes();
        mHour.setCurrentItem(mHours);
        mMin.setCurrentItem(mMinus);
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
        mHour = (WheelView) findViewById(R.id.min);
        mMin = (WheelView) findViewById(R.id.sec);
        mHour.setVisibleItems(3);
        mMin.setVisibleItems(3);
//        Date dateSel= CommonUtils.stringToDate(monthDateView.getmSelYear()+"-"+(monthDateView.getmSelMonth()+1)+"-"+monthDateView.getmSelDay(),"yyyy-MM-dd");
//
//        if(dateSel.getTime()<(new Date().getTime())) {

            monthDateView.setDateClick(new MonthDateView.DateClick() {

                @Override
                public void onClickOnDate() {


                }
            });
//        }
        iv_right.setBackground(getResources().getDrawable(R.mipmap.icon_calender_arr_hui));
        monthDateView.setShow(true);
    }
    OnWheelScrollListener scrollListener = new OnWheelScrollListener() {
        @Override
        public void onScrollingStarted(WheelView wheel) {

        }

        @Override
        public void onScrollingFinished(WheelView wheel) {
            mHours=mHour.getCurrentItem();
            mMinus=mMin.getCurrentItem();

        }
    };




    private void setOnlistener() {
        iv_left.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                monthDateView.onBackLeftClick(iv_right);
            }
        });

        iv_right.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                monthDateView.onBackRightClick(iv_right);

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
                mDate = monthDateView.getmSelDay();
                mMonth = monthDateView.getmSelMonth()+1;
                mYear = monthDateView.getmSelYear();

                if(mHours<10){
                    mH="0"+mHours;
                }else{
                    mH=""+mHours;
                }
                if(mMinus<10){
                    mM="0"+mMinus;
                }else{
                    mM=""+mMinus;
                }
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
                String time = mYear + "-" + mMos + "-" + mDos+" "+mH+":"+mM;
                Intent intent = new Intent();
                intent.putExtra("time", time);
                setResult(Constants.REQUEST_CALENDAR, intent);
                finish();
            }
        });
    }


}
