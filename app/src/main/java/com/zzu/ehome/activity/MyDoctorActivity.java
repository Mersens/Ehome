package com.zzu.ehome.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zzu.ehome.R;
import com.zzu.ehome.bean.RefreshEvent;
import com.zzu.ehome.db.EHomeDao;
import com.zzu.ehome.db.EHomeDaoImpl;
import com.zzu.ehome.fragment.BaseFragment;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.utils.ToastUtils;
import com.zzu.ehome.view.DialogTips;
import com.zzu.ehome.view.HeadView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2016/5/30.
 */
public class MyDoctorActivity extends BaseActivity implements View.OnClickListener{
    private RequestMaker requestMaker;
    private String userid,parentid,doctor_id,department_id,hospitalid;
    private EHomeDao dao;
    private Intent mIntent;
    private LinearLayout layout_add_hosptial;
    private LinearLayout layout_add_office;
    private LinearLayout layout_add_doctor;
    private LinearLayout layout_add_time;
    private TextView tv_hosptial;
    private TextView tv_office;
    private TextView tv_doctor;
    private TextView tv_time;
    private Button btn_ok;
    public static final int ADD_TIME = 0x11;
    private String DateStr;
    private String TimeSpanStr;
    private String PerTime;
    private String UserNo;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.layout_doctor);
        requestMaker = RequestMaker.getInstance();
        userid= SharePreferenceUtil.getInstance(this).getUserId();
        dao= new EHomeDaoImpl(this);
        parentid=dao.findUserInfoById(userid).getPatientId();
        mIntent=this.getIntent();
        initViews();
        if(mIntent!=null){
            initEvent();
        }

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_add_time:

                Intent intenttime = new Intent(MyDoctorActivity.this, SelectDateActivity_1.class);
                intenttime.putExtra("department_id", department_id);
                intenttime.putExtra("doctor_id", doctor_id);
                startActivityForResult(intenttime, ADD_TIME);
                break;
            case R.id.btn_ok:
                confirmSave();

                break;
        }

    }
    public void initViews() {
        setDefaultTXViewMethod(R.mipmap.icon_arrow_left, "预约挂号", "", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finish();

            }
        }, new HeadView.OnRightClickListener() {
            @Override
            public void onClick() {

            }
        });
        layout_add_hosptial = (LinearLayout) findViewById(R.id.layout_add_hosptial);
        layout_add_office = (LinearLayout) findViewById(R.id.layout_add_office);
        layout_add_doctor = (LinearLayout) findViewById(R.id.layout_add_doctor);
        layout_add_time = (LinearLayout) findViewById(R.id.layout_add_time);

        tv_hosptial = (TextView) findViewById(R.id.tv_hosptial);
        tv_office = (TextView) findViewById(R.id.tv_office);
        tv_doctor = (TextView) findViewById(R.id.tv_doctor);
        tv_time = (TextView) findViewById(R.id.tv_time);
        btn_ok = (Button) findViewById(R.id.btn_ok);
    }
    public void initEvent() {
        tv_office.setVisibility(View.VISIBLE);
        tv_doctor.setVisibility(View.VISIBLE);

        tv_hosptial.setText(AppointmentActivity.hosptial);
        layout_add_time.setOnClickListener(this);

        btn_ok.setOnClickListener(this);
        tv_office.setText( mIntent.getStringExtra("Department_FullName"));
        tv_doctor.setText(mIntent.getStringExtra("doctor"));
        department_id=mIntent.getStringExtra("department_id");
        doctor_id=mIntent.getStringExtra("doctor_id");
        hospitalid=mIntent.getStringExtra("hospitalid");
    }
    public void confirmSave() {
        String time = tv_time.getText().toString();
        if (TextUtils.isEmpty(time) ) {
            ToastUtils.showMessage(this, "请选择时间");
            return;
        }
        UserNo=dao.findUserInfoById(userid).getUserno();
        if(TextUtils.isEmpty(UserNo)){
            ToastUtils.showMessage(MyDoctorActivity.this, "用户身份证不能为空，请先完善个人资料！");
            return;
        }
        DialogTips dialog = new DialogTips(MyDoctorActivity.this, "","您确定预约"+tv_hosptial.getText().toString()+tv_office.getText().toString()+tv_doctor.getText().toString()+"医生"+tv_time.getText().toString()+"的号码吗？",
                "确定", true, true);
        dialog.SetOnSuccessListener(new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int userId) {
                startProgressDialog();
                requestMaker.TreatmentInsert(doctor_id, parentid, DateStr,TimeSpanStr,PerTime, new JsonAsyncTask_Info(MyDoctorActivity.this, true, new JsonAsyncTaskOnComplete() {
                    @Override
                    public void processJsonObject(Object result) {
                        JSONObject mySO = (JSONObject) result;
                        try {
                            EventBus.getDefault().post(new RefreshEvent(getResources().getInteger(R.integer.back_detail)));
                            JSONArray array = mySO.getJSONArray("TreatmentInsert");
                            JSONObject jsonObject=(JSONObject)array.get(0);
                            String code=jsonObject.getString("MessageCode");
                            stopProgressDialog();
                            if("0".equals(code)){

                              finishActivity();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }finally {
                            stopProgressDialog();
                        }

                    }
                }));
            }
        });

        dialog.show();
        dialog = null;


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_TIME && resultCode == ADD_TIME && data != null) {
            DateStr = data.getStringExtra("DateStr");
            TimeSpanStr = data.getStringExtra("TimeSpanStr");
            PerTime = data.getStringExtra("PerTime");
            if (!TextUtils.isEmpty(DateStr)) {
                tv_time.setVisibility(View.VISIBLE);
                tv_time.setText(DateStr+" "+TimeSpanStr);
            }
        }
    }





}
