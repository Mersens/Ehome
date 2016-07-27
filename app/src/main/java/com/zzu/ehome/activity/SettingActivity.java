package com.zzu.ehome.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.umeng.analytics.MobclickAgent;
import com.zzu.ehome.R;
import com.zzu.ehome.bean.RefreshEvent;
import com.zzu.ehome.bean.StepBean;
import com.zzu.ehome.db.EHomeDao;
import com.zzu.ehome.db.EHomeDaoImpl;
import com.zzu.ehome.service.StepDetector;
import com.zzu.ehome.utils.CommonUtils;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.utils.ToastUtils;
import com.zzu.ehome.view.DialogTips;
import com.zzu.ehome.view.HeadView;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import de.greenrobot.event.EventBus;

/**
 * Created by zzu on 2016/4/15.
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {
//    private RelativeLayout layout_about;
    private RelativeLayout layout_xgmm;
    private RelativeLayout layout_qchc;
    private RelativeLayout layout_exit;
    private RequestMaker request;
    private String userid;
    private final String mPageName = "SettingActivity";
    private EHomeDao dao;
    private float weight;
    private double calories = 0;
    private int step_length = 55;
    private int minute_distance = 80;
    private String timeCount;


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_setting);
        request=RequestMaker.getInstance();
        userid=SharePreferenceUtil.getInstance(SettingActivity.this).getUserId();
        dao=new EHomeDaoImpl(this);
        initViews();
        if(TextUtils.isEmpty(SharePreferenceUtil.getInstance(SettingActivity.this).getWeight())){
            weight=50.0f;
        }else {
            weight = Float.parseFloat(SharePreferenceUtil.getInstance(SettingActivity.this).getWeight());
        }
        initEvent();
    }

    public void initViews(){
//        layout_about=(RelativeLayout)findViewById(R.id.layout_about);
        layout_xgmm=(RelativeLayout)findViewById(R.id.layout_xgmm);
        layout_qchc=(RelativeLayout)findViewById(R.id.layout_qchc);
        layout_exit=(RelativeLayout)findViewById(R.id.layout_exit);
        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "设置", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();
            }
        });
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

    public void initEvent(){
//        layout_about.setOnClickListener(this);
        layout_xgmm.setOnClickListener(this);
        layout_qchc.setOnClickListener(this);
        layout_exit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
//            case R.id.layout_about:
//                intentAction(SettingActivity.this,AboutEhomeActivity.class);
//                break;
            case R.id.layout_xgmm:
                intentAction(SettingActivity.this,ChangePasswordActivity.class);
                break;
            case R.id.layout_qchc:
                DialogTips dialog = new DialogTips(SettingActivity.this, "", "确定清除缓存?",
                        "确定", true, true);
                dialog.SetOnSuccessListener(new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int userId) {
                        showTips();
                    }
                });

                dialog.show();
                dialog = null;
                break;
            case R.id.layout_exit:
               if(CommonUtils.isFastClick())
                   return;
                upload();

               break;
        }
    }
    private void upload(){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");

        calories = (weight * StepDetector.CURRENT_SETP * 50 * 0.01 * 0.01)/1000;
        double d = step_length * StepDetector.CURRENT_SETP;
        timeCount = String.format("%.2f", d / 100000);
        int m = StepDetector.CURRENT_SETP / minute_distance;
        String h1 = String.valueOf(m / 60);
        String h2 = String.valueOf(m % 60);
        request.StepCounterInsert(userid, StepDetector.CURRENT_SETP + "", timeCount, h1 + "." + h2, String.format("%.2f",calories),sdf.format(new Date()), new JsonAsyncTask_Info(this, true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                try {
                    JSONObject mySO = (JSONObject) result;
                    org.json.JSONArray array = mySO
                            .getJSONArray("StepCounterInsert");
                    UserClientBind();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }));
    }

    private void UserClientBind() {
        startProgressDialog();
        request.loginOut(userid,new JsonAsyncTask_Info(SettingActivity.this, true, new JsonAsyncTaskOnComplete(){
            @Override
            public void processJsonObject(Object result) {
                String UserClientBind = result.toString();
                if (UserClientBind == null)
                {

                }
                else
                {
                    stopProgressDialog();

                    try {
                        JSONObject mySO = (JSONObject) result;
                        org.json.JSONArray array = mySO
                                .getJSONArray("UserClientIDChange");
                        StepDetector.CURRENT_SETP=0;
                        ToastUtils.showMessage(SettingActivity.this,array.getJSONObject(0).getString("MessageContent").toString());
                        Intent i=new Intent(SettingActivity.this,LoginActivity.class);
                        i.putExtra("logout","logout");
                        startActivity(i);

                        StepBean step=new StepBean();
                        step.setEndTime("");
                        step.setStartTime("");
                        step.setNum(0);
                        step.setUserid("");
                        step.setUploadState(0);
                        dao.updateStep(step);
                       finishActivity();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        }));
    }

    public <T> void intentAction(Activity context, Class<T> cls) {
        Intent intent = new Intent(context, cls);
        startActivity(intent);
    }

    public void showTips(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                DialogTips dialog = new DialogTips(SettingActivity.this,"清除成功!","确定");
                dialog.show();
                dialog = null;
            }
        },1000);
    }

}
