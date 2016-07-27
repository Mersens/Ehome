package com.zzu.ehome.activity;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.igexin.sdk.PushManager;
import com.umeng.analytics.MobclickAgent;
import com.zzu.ehome.R;
import com.zzu.ehome.adapter.UserAdapter;
import com.zzu.ehome.application.Constants;
import com.zzu.ehome.application.CustomApplcation;
import com.zzu.ehome.application.MMloveConstants;
import com.zzu.ehome.bean.DoctorDate;
import com.zzu.ehome.bean.DoctorRes;
import com.zzu.ehome.bean.User;
import com.zzu.ehome.bean.UserDate;
import com.zzu.ehome.db.EHomeDao;
import com.zzu.ehome.db.EHomeDaoImpl;
import com.zzu.ehome.main.ehome.MainActivity;
import com.zzu.ehome.main.ehome.WelcomeActivity;
import com.zzu.ehome.utils.CommonUtils;
import com.zzu.ehome.utils.IOUtils;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.JsonTools;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.utils.ToastUtils;
import com.zzu.ehome.view.DialogTips;
import com.zzu.ehome.view.HeadView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class LoginActivity extends BaseActivity implements Callback, View.OnClickListener {
    private final String mPageName = "LoginActivity";
    private EditText edUser;
    private EditText edPass;
    // 下拉框对象
    private PopupWindow selectPopupWindow = null;
    // 下拉框选项数据源
    private List<User> datas;


    // 展示所有下拉选项的listView
    private ListView listView = null;
    // 用来处理选中或者删除下拉项消息
    private Handler handler;
    // 是否初始化下拉框完成标志
    private boolean flag = false;
    // 下拉框依附附件
    private RelativeLayout parent;
    private UserAdapter userAdapter;
    // 下拉框依附组件宽度，也将作为下拉框的宽度
    private int pwidth;
    private EHomeDao dao;
    private CheckBox checkbox;
    private TextView tv_forget_psd;
    private Button btn_login;
    private String ClientID;
    private RequestMaker requestMaker;
    private ImageView ivselect;
    private Intent mIntent;
    private User user;
    private String userid;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        PushManager.getInstance().initialize(this.getApplicationContext());
        dao = new EHomeDaoImpl(this);
        mIntent = this.getIntent();
        ClientID = PushManager.getInstance().getClientid(LoginActivity.this);
        requestMaker = RequestMaker.getInstance();
        initViews();
        initEvent();


        checkbox.setChecked(true);
        if(!"".equals(SharePreferenceUtil.getInstance(LoginActivity.this).getUserId())){
            userid=SharePreferenceUtil.getInstance(LoginActivity.this).getUserId();
            User user2=dao.findUserInfoById(userid);
            edUser.setText(user2.getMobile());
            if(SharePreferenceUtil.getInstance(LoginActivity.this).getIsRemeber()){
                edPass.setText(user2.getPassword());
            }

        }

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

    private void initViews() {
        handler = new Handler(LoginActivity.this);
        parent = (RelativeLayout) findViewById(R.id.rlaccount);
        edUser = (EditText) findViewById(R.id.editPhone);
        edPass = (EditText) findViewById(R.id.editPass);
        checkbox = (CheckBox) findViewById(R.id.checkbox);
        tv_forget_psd = (TextView) findViewById(R.id.tv_forget_psd);
        btn_login = (Button) findViewById(R.id.btn_login);
        int width = parent.getWidth();
        ivselect = (ImageView) findViewById(R.id.ivselect);
        pwidth = width;

        setDefaultTXViewMethod(R.mipmap.icon_arrow_left, "", "注册", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {

                if (mIntent.getStringExtra("logout") != null && mIntent.getStringExtra("logout").equals("logout")) {
                    confirmExit();
                }else {
                    finishActivity();
                }


            }
        }, new HeadView.OnRightClickListener() {
            @Override
            public void onClick() {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
//                finishActivity();

            }
        });

        initPopupWindow();


    }


    public void initEvent() {
        tv_forget_psd.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        ivselect.setOnClickListener(this);
    }

    @Override
    public boolean handleMessage(Message message) {
        Bundle data = message.getData();
        switch (message.what) {
            case 1:
                int selIndex = data.getInt("selIndex");
                edUser.setText(datas.get(selIndex).getMobile() + "");
                user=dao.findUserInfoById(datas.get(selIndex).getUserid());
//                edPass.setText(user.getPassword());
                dismiss();
                break;

        }
        return false;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        while (!flag) {
            initViews();
            flag = true;
        }
    }

    protected void PopupWindowShowing() {
        selectPopupWindow.showAsDropDown(parent, 0, -3);
    }

    @SuppressWarnings("deprecation")
    private void initPopupWindow() {
        if (dao.findAllUser() != null) {

            datas = initDatas();
            View loginwindow = (View) this.getLayoutInflater().inflate(
                    R.layout.userlist_layout, null);
            listView = (ListView) loginwindow.findViewById(R.id.list);
            userAdapter = new UserAdapter(this, handler);
            userAdapter.addAll(datas);
            listView.setAdapter(userAdapter);
            selectPopupWindow = new PopupWindow(loginwindow, pwidth,
                    LinearLayout.LayoutParams.WRAP_CONTENT, true);
            selectPopupWindow.setOutsideTouchable(true);
            selectPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        }
    }

    /**
     * 下拉框数据
     *
     * @return
     */
    private List<User> initDatas() {


        List<User> listItems = dao.findAllUser();

        return listItems;
    }

    private void dismiss() {
        selectPopupWindow.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_forget_psd:
                doForgetPsd();
                break;
            case R.id.btn_login:
                if (isNetworkAvailable()) {
                    doLogin();
                } else {
                    ToastUtils.showMessage(LoginActivity.this, R.string.msgUninternet);
                }
                break;
            case R.id.ivselect:
                if (flag) {
                    PopupWindowShowing();
                }

                break;

        }

    }

    /**
     * 执行忘记密码操作
     */
    private void doForgetPsd() {
        Intent intent = new Intent(LoginActivity.this, FindPsdActivity.class);
        startActivity(intent);
    }

    /**
     * 执行登录操作
     */
    private void doLogin() {
        boolean isChecked = checkbox.isChecked();
        String name = edUser.getText().toString().trim();
        String psd = edPass.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            ToastUtils.showMessage(this, "请输入您的账号!");
            return;
        }
        if (TextUtils.isEmpty(psd)) {
            ToastUtils.showMessage(this, "请输入您的密码!");
            return;
        }
        if (!IOUtils.isMobileNO(name)) {
            ToastUtils.showMessage(this, "请输入正确的手机号!");
            return;
        }
        login(name, psd);
    }

    public void login(final String mobile, final String psd) {
        startProgressDialog();


        requestMaker.userLogin(mobile, psd, ClientID, new JsonAsyncTask_Info(LoginActivity.this, true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                if (result != null) {

                    try {
                        JSONObject mySO = (JSONObject) result;
                        org.json.JSONArray array = mySO
                                .getJSONArray("UserLogin");
                        stopProgressDialog();
                        if (array.getJSONObject(0).has("MessageCode")) {
                            Toast.makeText(LoginActivity.this, array.getJSONObject(0).getString("MessageContent").toString(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            if(SharePreferenceUtil.getInstance(LoginActivity.this).getUserId().equals("")&&!SharePreferenceUtil.getInstance(LoginActivity.this).getIsFirst()) {
                                GuideActivity guideActivity = new GuideActivity();
                                guideActivity.guidAct.finish();
                            }
                            UserDate date = JsonTools.getData(result.toString(), UserDate.class);
                            List<User> list = date.getData();
                            String imgHead = list.get(0).getImgHead();
                            if (imgHead != null) {
                                if (imgHead.equals("") || imgHead.contains("vine.gif")) {
                                    imgHead = "";
                                } else {
                                    imgHead = Constants.JE_BASE_URL3 + imgHead.replace("~", "").replace("\\", "/");

                                }
                            } else {
                                imgHead = "";
                            }

                            list.get(0).setImgHead(imgHead);
                            list.get(0).setPassword(psd);
                            list.get(0).setMobile(mobile);


                            if (!dao.findUserIsExist(list.get(0).getUserid())) {
                                dao.addUserInfo(list.get(0));

                            } else {
                                User dbUser=dao.findUserInfoById(list.get(0).getUserid());
                                dbUser.setImgHead(imgHead);
                                dbUser.setPassword(psd);
                                dbUser.setMobile(mobile);
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

                            SharePreferenceUtil.getInstance(LoginActivity.this).setUserId(array.getJSONObject(0).getString("UserID"));
                            SharePreferenceUtil.getInstance(LoginActivity.this).setIsFirst(true);
                            if (checkbox.isChecked()) {
                                SharePreferenceUtil.getInstance(LoginActivity.this).setIsRemeber(true);
                            } else {
                                SharePreferenceUtil.getInstance(LoginActivity.this).setIsRemeber(false);
                            }
                            if(imgHead.equals("")||list.get(0).getUsername().equals("")){
                                if(TextUtils.isEmpty(list.get(0).getUsername())){
                                    if(TextUtils.isEmpty(imgHead)) {
                                        startActivity(new Intent(LoginActivity.this, CompletInfoActivity.class));
                                    }else{
                                        Intent i=new Intent(LoginActivity.this,CompletInfoActivity.class);
                                        i.putExtra("imgHead",list.get(0).getImgHead());
                                        startActivity(i);

                                    }
                                }else{
                                    Intent i=new Intent(LoginActivity.this,CompletInfoActivity.class);
                                    i.putExtra("username",list.get(0).getUsername());
                                    startActivity(i);
                                }


                            }else {

                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }


            }
        }));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (mIntent.getStringExtra("logout") != null && mIntent.getStringExtra("logout").equals("logout")) {
                confirmExit();
            }
            else {
                finishActivity();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void confirmExit() {
        DialogTips dialog = new DialogTips(LoginActivity.this, "", "是否退出软件？",
                "确定", true, true);
        dialog.SetOnSuccessListener(new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int userId) {
                SharePreferenceUtil.getInstance(LoginActivity.this).setUserId("");
                SharePreferenceUtil.getInstance(LoginActivity.this).setIsRemeber(false);
                CustomApplcation.getInstance().exit();
                finish();
            }
        });

        dialog.show();
        dialog = null;
    }
}
