package com.zzu.ehome.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zzu.ehome.R;
import com.zzu.ehome.bean.RefreshEvent;
import com.zzu.ehome.bean.User;
import com.zzu.ehome.db.EHomeDao;
import com.zzu.ehome.db.EHomeDaoImpl;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.utils.ToastUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashSet;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2016/7/8.
 */
public class InputHeightActivity extends Activity {
    private EditText edHeight;
    private TextView tv_cancel;
    private TextView tv_ok;
    private String userid,userHeight;
    private RequestMaker requestMaker;
    private EHomeDao dao;
    User dbUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitity_input_height);

        dao = new EHomeDaoImpl(this);
        userid=SharePreferenceUtil.getInstance(InputHeightActivity.this).getUserId();
        requestMaker=RequestMaker.getInstance();
        dbUser=dao.findUserInfoById(userid);
        initViews();
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(edHeight.getText().toString().trim())){
                    ToastUtils.showMessage(InputHeightActivity.this,"身高不允许为空");
                    return;
                }
                if(edHeight.getText().toString().trim().length()>3){
                    ToastUtils.showMessage(InputHeightActivity.this,"身高长度最多只能3位");
                    return;
                }
                saveInfof();
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void initViews(){
        edHeight=(EditText)findViewById(R.id.edt_height);
        tv_cancel=(TextView)findViewById(R.id.tv_cancel);
        tv_ok=(TextView)findViewById(R.id.tv_ok);

    }
    private void saveInfof(){

        requestMaker.userInfo2(userid,edHeight.getText().toString().trim(),new JsonAsyncTask_Info(InputHeightActivity.this, true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                String returnvalue = result.toString();
                try {

                    JSONObject mySO = (JSONObject) result;
                    JSONArray array = mySO.getJSONArray("UserInfoChange");
                    ToastUtils.showMessage(InputHeightActivity.this,array.getJSONObject(0).getString("MessageContent"));

                    if (array.getJSONObject(0).getString("MessageCode")
                            .equals("0")) {
                        dbUser.setUserHeight(edHeight.getText().toString().trim());
                        dao.updateUserInfo(dbUser, userid);
                        Intent intentF = new Intent();
                        intentF.setAction("action.Weight");
                        sendBroadcast(intentF);
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
