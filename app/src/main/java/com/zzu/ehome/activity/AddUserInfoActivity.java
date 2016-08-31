package com.zzu.ehome.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zzu.ehome.R;
import com.zzu.ehome.utils.IOUtils;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.ToastUtils;
import com.zzu.ehome.view.HeadView;
import com.zzu.ehome.view.SexPopupWindows;
import com.zzu.ehome.view.SexPopupWindows.OnGetData;
/**
 * Created by Mersens on 2016/8/10.
 */
public class AddUserInfoActivity extends BaseActivity implements View.OnClickListener,OnGetData {
    private RequestMaker requestMaker;
    private EditText edt_name,edt_card_num,edt_age,edt_phone;
    private SexPopupWindows sexPopupWindows;
    private String name,userNo,age,phone,sex;
    private RelativeLayout  rlsex;

    private TextView tv_sex;
    private Button btn_save;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.layout_adduser_info);
        requestMaker=RequestMaker.getInstance();
        initViews();
        initEvent();
        initDatas();
    }

    public void initViews(){
        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "添加就诊人", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();
            }
        });
        edt_name=(EditText)findViewById(R.id.edt_name);
        edt_card_num=(EditText)findViewById(R.id.edt_card_num);
        edt_age=(EditText)findViewById(R.id.edt_age);
        edt_phone=(EditText)findViewById(R.id.edt_phone);
        rlsex=(RelativeLayout)findViewById(R.id.rlsex);
        btn_save=(Button)findViewById(R.id.btn_save);
        tv_sex=(TextView)findViewById(R.id.tv_sex);
    }

    public void initEvent(){
        btn_save.setOnClickListener(this);
        rlsex.setOnClickListener(this);
    }

    public void initDatas(){

    }
    private void doSex() {
        InputMethodManager imm = (InputMethodManager)getSystemService(AddUserInfoActivity.this.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edt_name.getWindowToken(),0);
        imm.hideSoftInputFromWindow(edt_card_num.getWindowToken(),0);
        imm.hideSoftInputFromWindow(edt_age.getWindowToken(),0);
        sexPopupWindows = new SexPopupWindows(AddUserInfoActivity.this, rlsex, AddUserInfoActivity.this);
        sexPopupWindows.setmOnGetData(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_save:
                name=edt_name.getText().toString();
                userNo=edt_card_num.getText().toString();
                age=edt_age.getText().toString();
                phone=edt_phone.getText().toString();
                sex=tv_sex.getText().toString();
                if(TextUtils.isEmpty(name.trim())){
                    ToastUtils.showMessage(AddUserInfoActivity.this,"请填写姓名！");
                    return;
                }
                if (TextUtils.isEmpty(userNo.trim())) {
                    ToastUtils.showMessage(AddUserInfoActivity.this,"请填写身份证号码！");
                    return;
                }
                if (TextUtils.isEmpty(age.trim())) {
                    ToastUtils.showMessage(AddUserInfoActivity.this,"请填写年龄！");
                    return;
                }
                if (TextUtils.isEmpty(phone.trim())) {
                    ToastUtils.showMessage(AddUserInfoActivity.this,"请填写手机号号码！");
                    return;
                }
                if (sex.trim().equals("请选择性别")) {
                    ToastUtils.showMessage(AddUserInfoActivity.this,"请选择性别！");
                    return;
                }
                if(name.length()>4){
                    ToastUtils.showMessage(AddUserInfoActivity.this, "姓名长度超长！");
                    return;
                }
            if (!IOUtils.isUserNO(userNo.trim())) {
                ToastUtils.showMessage(AddUserInfoActivity.this, "身份证号格式不正确！");
                return;
            }
               if( !IOUtils.isMobileNO(phone.trim())){
                   ToastUtils.showMessage(AddUserInfoActivity.this, "手机号码格式不正确！");
                   return;
               }



                break;
            case R.id.rlsex:
                doSex();
                break;
        }
    }


    @Override
    public void onDataCallBack(String sex) {
        ToastUtils.showMessage(AddUserInfoActivity.this,sex);
        if(sex.equals("01")){
            tv_sex.setText("男");
        }else{
            tv_sex.setText("女");
        }
    }
}
