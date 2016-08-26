package com.zzu.ehome.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zzu.ehome.R;
import com.zzu.ehome.application.Constants;
import com.zzu.ehome.utils.DateUtils;
import com.zzu.ehome.view.HeadView;

/**
 * Created by Mersens on 2016/8/22.
 */
public class StaticECGDetailActivity extends BaseActivity {
    private Intent mIntent;
    private TextView tvtime,tvname,tvres;
    private String imurl,Diagnosis,PatientName,CollectTime;
    private ImageView icon_pdf;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.layout_static_detail);
        initViews();

//        holder.name.setText(DateUtils.StringPattern(item.getCollectTime(),"yyyy/MM/dd HH:mm:ss","yyyy年M月dd日")+"心电报告");

        mIntent=this.getIntent();
        imurl= mIntent.getStringExtra("imurl");
        Diagnosis=mIntent.getStringExtra("Diagnosis");
        PatientName=mIntent.getStringExtra("PatientName");
        CollectTime=mIntent.getStringExtra("CollectTime");
        initEvent();

    }

    public void initViews(){
        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "静态心电报告", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();
            }
        });
        tvtime=(TextView) findViewById(R.id.tvtime);
        tvname=(TextView)findViewById(R.id.tvname);
        tvres=(TextView)findViewById(R.id.tvres);
        icon_pdf=(ImageView)findViewById(R.id.icon_pdf);
    }

    public void initEvent(){
        tvtime.setText(DateUtils.StringPattern(CollectTime,"yyyy/MM/dd HH:mm:ss","yyyy/MM/dd"));
        tvname.setText(PatientName);
        tvres.setText(Diagnosis);
        Glide.with(StaticECGDetailActivity.this).load(imurl).into(icon_pdf);
        icon_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(StaticECGDetailActivity.this,ImageECGDetail.class);
                i.putExtra("imurl",imurl);
                startActivity(i);
            }
        });
    }


}
