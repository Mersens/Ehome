package com.zzu.ehome.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.umeng.analytics.MobclickAgent;
import com.zzu.ehome.R;
import com.zzu.ehome.bean.User;
import com.zzu.ehome.db.EHomeDao;
import com.zzu.ehome.db.EHomeDaoImpl;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.utils.ToastUtils;
import com.zzu.ehome.view.HeadView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ChangePasswordActivity extends BaseActivity {
    private EditText edt_old_Pass;
    private EditText edt_new_Pass;
    private EditText edt_new_Pass_again;
    private Button btn_update;
    private String userid;
    private RequestMaker requestMaker;
    private EHomeDao dao;
    private final String mPageName = "ChangePasswordActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        userid = SharePreferenceUtil.getInstance(ChangePasswordActivity.this).getUserId();
        requestMaker = RequestMaker.getInstance();
        dao=new EHomeDaoImpl(ChangePasswordActivity.this);
        initViews();
        initEvent();
    }

    private void initViews() {
        edt_old_Pass=(EditText) findViewById(R.id.edt_old_Pass);
        edt_new_Pass=(EditText) findViewById(R.id.edt_new_Pass);
        edt_new_Pass_again=(EditText) findViewById(R.id.edt_new_Pass_again);
        btn_update=(Button) findViewById(R.id.btn_update);
        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "密码修改", new HeadView.OnLeftClickListener() {
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
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doUpdate();
            }
        });
    }

    public void doUpdate(){
        String oldPsd=edt_old_Pass.getText().toString().trim();
        String newPsd=edt_new_Pass.getText().toString().trim();
        String newPsdAgain=edt_new_Pass_again.getText().toString().trim();
        if(TextUtils.isEmpty(oldPsd)){
            ToastUtils.showMessage(ChangePasswordActivity.this,"旧密码不能为空");
            return;
        }else if(TextUtils.isEmpty(newPsd)){
            ToastUtils.showMessage(ChangePasswordActivity.this,"新密码不能为空");
            return;
        }else if(newPsd.length()<6){
            ToastUtils.showMessage(ChangePasswordActivity.this,"密码长度不能小于6位");
            return;

        }
        else if(!newPsd.equals(newPsdAgain)){
            ToastUtils.showMessage(ChangePasswordActivity.this,"两次密码输入不一致");
            return;
        }
        User user=dao.findUserInfoById(userid);
       final String  nPsd=newPsd;
        requestMaker.UserAuthChange(user.getMobile(),oldPsd,nPsd,new JsonAsyncTask_Info(ChangePasswordActivity.this, true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                JSONObject mySO = (JSONObject) result;
                Log.e("TAG",result.toString());
                try {
                    JSONArray array = mySO.getJSONArray("UserAuthChange");
                    JSONObject jsonObject = (JSONObject) array.get(0);
                    String msg = jsonObject.getString("MessageCode");



                    if ("0".equals(msg)) {
                        ToastUtils.showMessage(ChangePasswordActivity.this, jsonObject.getString("MessageContent"));
                        User dbUser=dao.findUserInfoById(userid);
                        dbUser.setPassword(nPsd);
                        dao.updateUserInfo(dbUser,userid);                        finishActivity();
                    } else {
                        ToastUtils.showMessage(ChangePasswordActivity.this, jsonObject.getString("MessageContent"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }));
    }
}
