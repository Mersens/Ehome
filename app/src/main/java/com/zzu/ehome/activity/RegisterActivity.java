package com.zzu.ehome.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.igexin.sdk.PushManager;
import com.umeng.analytics.MobclickAgent;
import com.zzu.ehome.R;
import com.zzu.ehome.application.CustomApplcation;
import com.zzu.ehome.bean.User;
import com.zzu.ehome.db.EHomeDao;
import com.zzu.ehome.db.EHomeDaoImpl;
import com.zzu.ehome.service.RegisterCodeTimerService;
import com.zzu.ehome.utils.CommonUtils;
import com.zzu.ehome.utils.IOUtils;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.RegisterCodeTimer;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.utils.ToastUtils;
import com.zzu.ehome.view.HeadView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zzu on 2016/4/6.
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    private EditText editPhone;
    private EditText edCode;
    private TextView tv_getCode;
    private EditText editPass;
    private Button btn_register;
    private CheckBox checkbox;
//    private TextView tv_ehome_tips;
    private Intent mIntent;
    private String tag="";

    private RequestMaker requestMaker;
    String usermobile="",chkcode="",pwd,mCode,ClientID;
    private EHomeDao dao;
    private final String mPageName = "RegisterActivity";

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        requestMaker = RequestMaker.getInstance();
        setContentView(R.layout.activity_register);
        initViews();
        initEvents();
//        checkbox.setChecked(true);
        dao = new EHomeDaoImpl(this);
        RegisterCodeTimerService.setHandler(mCodeHandler);
        if(this.getIntent()!=null){
            if(this.getIntent().getStringExtra("relation")!=null){
                tag=this.getIntent().getStringExtra("relation");
            }
        }
        mIntent = new Intent(RegisterActivity.this,
                RegisterCodeTimerService.class);
        ClientID = PushManager.getInstance().getClientid(RegisterActivity.this);
    }

    Handler mCodeHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == RegisterCodeTimer.IN_RUNNING) {// 正在倒计时
                tv_getCode.setText(msg.obj.toString());
                tv_getCode.setEnabled(false);
            } else if (msg.what == RegisterCodeTimer.END_RUNNING) {// 完成倒计时
                tv_getCode.setEnabled(true);
                tv_getCode.setText(msg.obj.toString());
            }
        };
    };
    public void initViews() {
        editPhone = (EditText) findViewById(R.id.editPhone);
        edCode = (EditText) findViewById(R.id.edCode);
        tv_getCode = (TextView) findViewById(R.id.tv_getCode);
        editPass = (EditText) findViewById(R.id.editPass);
        btn_register = (Button) findViewById(R.id.btn_register);
        checkbox = (CheckBox) findViewById(R.id.checkBox);
//        tv_ehome_tips = (TextView) findViewById(R.id.tv_ehome_tips);

        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "注册", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();
            }
        });
    }


    public void initEvents() {
        tv_getCode.setOnClickListener(this);
//        tv_ehome_tips.setOnClickListener(this);
        btn_register.setOnClickListener(this);
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



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_getCode:
                if(CommonUtils.isFastClick()) {
                    return;
                }
                if(TextUtils.isEmpty(editPhone.getText().toString().trim())){
                    ToastUtils.showMessage(RegisterActivity.this,R.string.reg_xingmingsubject);
                    return;
                }
                if(!IOUtils.isMobileNO(editPhone.getText().toString().trim())){
                    ToastUtils.showMessage(RegisterActivity.this,R.string.phone_error);
                    return;
                }



                if(isNetworkAvailable()) {
                    doGetCode();
                } else{
                    ToastUtils.showMessage(RegisterActivity.this,R.string.msgUninternet);
                }
                break;
//            case R.id.tv_ehome_tips:
//                doEhomeTips();
//                break;
            case R.id.btn_register:
                if(CommonUtils.isFastClick()) {
                    return;
                }
                if(!usermobile.equals(editPhone.getText().toString().trim())){
                    ToastUtils.showMessage(RegisterActivity.this,R.string.str002_findpass);
                    return;
                }
                if(!chkcode.equals(edCode.getText().toString().trim())){
                    ToastUtils.showMessage(RegisterActivity.this,R.string.str002_findpass);
                    return;
                }

                if(isNetworkAvailable()){
                    doRegister();

                }else{
                    ToastUtils.showMessage(RegisterActivity.this,R.string.msgUninternet);
                }
                break;

        }
    }

    /**
     * 获取验证码
     */
    public void doGetCode(){
        CHKCodeSend();
        startService(mIntent);

    }
    private void CHKCodeSend() {
        usermobile=editPhone.getText().toString().trim();


        if(IOUtils.isMobileNO(usermobile)){
            requestMaker.sendCode(usermobile,new JsonAsyncTask_Info(RegisterActivity.this, true, new JsonAsyncTaskOnComplete() {
                @Override
                public void processJsonObject(Object result) {
                    String value=result.toString();
//               {"SendAuthCode":[{"MessageContent":"296283","MessageCode":"0"}]}

                    try {
                        JSONObject mySO = (JSONObject) result;
                        JSONArray array = mySO
                                .getJSONArray("SendAuthCode");
                        for (int i = 0; i < array.length(); i++)
                        {
                            chkcode = array.getJSONObject(i)
                                    .getString("MessageContent");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }));
        }
    }

    /**
     * 健康E家用户协议
     */
//    public void doEhomeTips(){
//        if(checkbox.isChecked()){
//            checkbox.setChecked(false);
//        }else{
//            checkbox.setChecked(true);
//
//        }
//    }

    /**
     * 注册
     */
    public void doRegister(){

       usermobile=editPhone.getText().toString().trim();
        pwd=editPass.getText().toString().trim();
        mCode=edCode.getText().toString().trim();

        if(editPhone.getText().toString().trim().equals(""))
        {
            ToastUtils.showMessage(RegisterActivity.this,R.string.mobile_register);

            return;
        }else if(!usermobile.equals(editPhone.getText().toString().trim())){
            ToastUtils.showMessage(RegisterActivity.this,R.string.checkmobile_register);
            return;
        }
        else if(mCode.equals("")){
            ToastUtils.showMessage(RegisterActivity.this,R.string.erro_code_register);
            return;
        }
        else if(pwd.equals(""))
        {
            ToastUtils.showMessage(RegisterActivity.this,R.string.pass_register);
            return;
        }else if(pwd.length()<6){
            ToastUtils.showMessage(RegisterActivity.this,R.string.pass_length_register);
            return;
        }
        else if(!IOUtils.isMobileNO(editPhone.getText().toString().trim())){
            ToastUtils.showMessage(RegisterActivity.this,R.string.checkmobile_register);
            return;
        }else if(!mCode.equals(chkcode)){
            ToastUtils.showMessage(RegisterActivity.this,R.string.erro_code_register);
            return;
        }
        else{

            requestMaker.userRegister(usermobile,pwd,ClientID,new JsonAsyncTask_Info(RegisterActivity.this, true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                JSONObject mySO = (JSONObject) result;
                try {
                    JSONArray array = mySO.getJSONArray("UserRegister");

                    if (array.getJSONObject(0).has("UserID")){
                        User user=new User();
                        user.setUserid(array.getJSONObject(0).getString("UserID"));
                        user.setMobile(array.getJSONObject(0).getString("Mobile"));
                        user.setPassword(pwd);
                        dao.addUserInfo(user);


                        SharePreferenceUtil.getInstance(RegisterActivity.this).setIsFirst(true);
//                        CustomApplcation.getInstance().finishSingleActivityByClass(GuideActivity.class);
                        if(TextUtils.isEmpty(tag)){
                            SharePreferenceUtil.getInstance(RegisterActivity.this).setUserId(array.getJSONObject(0).getString("UserID"));
                            CustomApplcation.getInstance().finishSingleActivityByClass(LoginActivity1.class);
                           finish();
                        }else{
                            SharePreferenceUtil.getInstance(RegisterActivity.this).setPARENTID(array.getJSONObject(0).getString("UserID"));
                            startActivity(new Intent(RegisterActivity.this,RelationActivity.class));
                        }



                    }else{
                        ToastUtils.showMessage(RegisterActivity.this,array.getJSONObject(0).getString("MessageContent").toString());

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

        }));

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(mIntent);
    }
}
