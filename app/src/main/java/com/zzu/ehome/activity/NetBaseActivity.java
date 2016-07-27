package com.zzu.ehome.activity;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;

import com.zzu.ehome.application.CustomApplcation;
import com.zzu.ehome.view.CustomProgressDialog;

/**
 * Created by Administrator on 2016/4/20.
 */
public class NetBaseActivity extends Activity{
    private CustomProgressDialog progressDialog = null;
    public void startProgressDialog(){
        if (progressDialog == null){
            progressDialog = CustomProgressDialog.createDialog(this);
            //progressDialog.setMessage("正在加载中...");
        }

        progressDialog.show();
    }

    public void stopProgressDialog(){
        if (progressDialog != null){
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        //设置禁止横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        CustomApplcation.getInstance().addActivity(this);

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopProgressDialog();
    }
}
