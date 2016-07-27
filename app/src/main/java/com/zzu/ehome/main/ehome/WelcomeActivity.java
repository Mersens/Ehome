package com.zzu.ehome.main.ehome;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Window;
import android.widget.Toast;

import com.igexin.sdk.PushManager;
import com.umeng.analytics.MobclickAgent;
import com.zzu.ehome.R;
import com.zzu.ehome.activity.BaseSimpleActivity;
import com.zzu.ehome.activity.CompletInfoActivity;
import com.zzu.ehome.activity.GuideActivity;
import com.zzu.ehome.activity.LoginActivity;
import com.zzu.ehome.application.Constants;
import com.zzu.ehome.bean.HealteData;
import com.zzu.ehome.bean.HealthDataRes;
import com.zzu.ehome.bean.User;
import com.zzu.ehome.bean.UserDate;
import com.zzu.ehome.db.EHomeDao;
import com.zzu.ehome.db.EHomeDaoImpl;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.JsonTools;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.utils.ToastUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class WelcomeActivity extends BaseSimpleActivity {
    private static final long SPLASH_DELAY_MILLIS = 1000;

    private static final int GO_HOME = 0X00;
    private static final int GO_GUIDE = 0X01;
    String userid,ClientID;
    private RequestMaker requestMaker;
    private EHomeDao dao;
    private final String mPageName = "WelcomeActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_welcome);
        PushManager.getInstance().initialize(this.getApplicationContext());
        ClientID = PushManager.getInstance().getClientid(WelcomeActivity.this);
        dao=new EHomeDaoImpl(this);
        requestMaker = RequestMaker.getInstance();
        userid=SharePreferenceUtil.getInstance(WelcomeActivity.this).getUserId();
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(mPageName);
        MobclickAgent.onResume(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(mPageName);
        MobclickAgent.onPause(this);
    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GO_HOME:

                    if(userid.equals("")){
                        goLogin();
                    }else {
                        User user = dao.findUserInfoById(userid);
                        if(user!=null) {
                            login(user.getMobile(), user.getPassword());
                        }else{
                            goLogin();
                        }
                    }


                    break;
                case GO_GUIDE:
                    goGuide();

                    break;

            }
            super.handleMessage(msg);
        }
    };

    private void init() {
        if(!isNetworkAvailable()){
            ToastUtils.showMessage(WelcomeActivity.this,R.string.msgUninternet);
            return;
        }


        if (!SharePreferenceUtil.getInstance(WelcomeActivity.this).getIsFirst()) {
            mHandler.sendEmptyMessageDelayed(GO_GUIDE, SPLASH_DELAY_MILLIS);
        } else {

            mHandler.sendEmptyMessageDelayed(GO_HOME, SPLASH_DELAY_MILLIS);
        }
    }

    private void goHome() {

        startActivity(new Intent(WelcomeActivity.this, MainActivity.class));

        finish();

    }

    private void goGuide() {
        startActivity(new Intent(WelcomeActivity.this, GuideActivity.class));
        finish();
    }
    public void login(final String name, final String psd) {

        requestMaker.userLogin(name, psd, ClientID, new JsonAsyncTask_Info(WelcomeActivity.this, true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                if (result != null)
                {

                    try {
                        JSONObject mySO = (JSONObject) result;
                        org.json.JSONArray array = mySO
                                .getJSONArray("UserLogin");
                        if (array.getJSONObject(0).has("MessageCode"))
                        {
                            Toast.makeText(WelcomeActivity.this,array.getJSONObject(0).getString("MessageContent").toString(),
                                    Toast.LENGTH_SHORT).show();
                            goLogin();
                        }else {


                            UserDate date = JsonTools.getData(result.toString(), UserDate.class);
                            List<User> list = date.getData();
                            String imgHead = list.get(0).getImgHead();
                            if (imgHead.contains("vine.gif")) {
                                imgHead="";
                            } else {
                                imgHead = Constants.JE_BASE_URL3 + imgHead.replace("~", "").replace("\\", "/");

                            }
                            list.get(0).setImgHead(imgHead);
                            list.get(0).setPassword(psd);

                            if (!dao.findUserIsExist(list.get(0).getUserid())) {
                                dao.addUserInfo(list.get(0));

                            } else {
                                User dbUser=dao.findUserInfoById(list.get(0).getUserid());
                                dbUser.setImgHead(imgHead);
                                dbUser.setPassword(psd);
                                dbUser.setMobile(name);
                                dbUser.setPatientId(list.get(0).getPatientId());
                                if(list.get(0).getSex()!=null){
                                    dbUser.setSex(list.get(0).getSex());
                                }
                                if(list.get(0).getUserno()!=null){
                                    dbUser.setUserno(list.get(0).getUserno());
                                }
                                if(list.get(0).getAge()!=null){
                                    dbUser.setAge(list.get(0).getAge());
                                }
                                if(list.get(0).getUsername()!=null){
                                    dbUser.setUsername(list.get(0).getUsername());
                                }
                                if(list.get(0).getUserHeight()!=null){
                                    dbUser.setUserHeight(list.get(0).getUserHeight());
                                }
                                dao.updateUserInfo(dbUser, list.get(0).getUserid());
                            }
                            SharePreferenceUtil.getInstance(WelcomeActivity.this).setUserId(array.getJSONObject(0).getString("UserID"));
                            if(imgHead.equals("")||list.get(0).getUsername().equals("")) {
                                if (TextUtils.isEmpty(list.get(0).getUsername())) {
                                    if(TextUtils.isEmpty(imgHead)) {
                                        startActivity(new Intent(WelcomeActivity.this, CompletInfoActivity.class));
                                        finishActivity();
                                    }else{
                                        Intent i = new Intent(WelcomeActivity.this, CompletInfoActivity.class);
                                        i.putExtra("imgHead", imgHead);
                                        startActivity(i);
                                        finishActivity();

                                    }
                                } else {
                                    Intent i = new Intent(WelcomeActivity.this, CompletInfoActivity.class);
                                    i.putExtra("username", list.get(0).getUsername());
                                    startActivity(i);
                                    finishActivity();
                                }
                            }else
                            goHome();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }


            }
        }));
    }

    private void goLogin() {
        startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
        finish();
    }

}
