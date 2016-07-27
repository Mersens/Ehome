package com.zzu.ehome.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dinuscxj.progressbar.CircleProgressBar;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zzu.ehome.R;
import com.zzu.ehome.application.Constants;
import com.zzu.ehome.network.DownloadProgressListener;
import com.zzu.ehome.network.FileDownloader;
import com.zzu.ehome.utils.ToastUtils;
import com.zzu.ehome.view.HeadView;

import java.io.File;

/**
 * Created by Administrator on 2016/6/23.
 */
public class StaticECGDetial extends BaseActivity  {

    private Intent mIntent;
    private ImageView ivstatic;
    private ImageLoader mImageLoader;
    private String imurl;





    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_static_ecg);
        mIntent=this.getIntent();
        mImageLoader=ImageLoader.getInstance();
        mIntent=this.getIntent();
        imurl=mIntent.getStringExtra("imurl");
        initViews();

    }


    public void  initViews(){

        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "心电报告", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();
            }
        });
        ivstatic=(ImageView)findViewById(R.id.ivstatic);
        mImageLoader.displayImage(
                imurl, ivstatic);
        ivstatic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(StaticECGDetial.this,ImageECGDetail.class);
                i.putExtra("imurl",imurl);
                startActivity(i);

            }
        });
    }





}

