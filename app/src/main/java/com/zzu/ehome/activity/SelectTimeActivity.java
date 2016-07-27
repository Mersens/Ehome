package com.zzu.ehome.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zzu.ehome.R;
import com.zzu.ehome.fragment.BloodSugarFragment;
import com.zzu.ehome.fragment.DoctorFragment;
import com.zzu.ehome.view.MonthDateView;

public class SelectTimeActivity extends Activity {
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
        setContentView(R.layout.activity_select_time2);
        initViews();
        setOnlistener();
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
    }
    private void setOnlistener() {
        iv_left.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                monthDateView.onLeftClick();
            }
        });

        iv_right.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                monthDateView.onRightClick();
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
                String time = mYear + "-" + mMonth + "-" + mDate;
                Intent intent = new Intent();
                intent.putExtra("time", time);

                finish();
            }
        });
    }
}
