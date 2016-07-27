package com.zzu.ehome.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.zzu.ehome.R;
import com.zzu.ehome.application.Constants;
import com.zzu.ehome.application.CustomApplcation;
import com.zzu.ehome.bean.DoctorBean;
import com.zzu.ehome.bean.RefreshEvent;
import com.zzu.ehome.bean.ScheduleBean;
import com.zzu.ehome.db.EHomeDao;
import com.zzu.ehome.db.EHomeDaoImpl;
import com.zzu.ehome.fragment.DoctorFragment;
import com.zzu.ehome.utils.ImageOptions;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.utils.ToastUtils;
import com.zzu.ehome.view.CircleImageView;
import com.zzu.ehome.view.HeadView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import de.greenrobot.event.EventBus;

public class AppointmentDetailActivity extends BaseActivity implements View.OnClickListener {
    private String doctor_id="";
    private String department_id="";
    private String User_FullName="";
    private String Department_FullName="";
    private final String mPageName = "AppointmentDetailActivity";
    //请求单例
    private RequestMaker requestMaker;
    private CircleImageView iv_head;
    private TextView tv_name;
    private TextView tv_zc;
    private TextView tv_doctor_jj;
    private TextView tv_sc;
    private TextView tv_focus;
    private TextView iv_yy;
    private DoctorBean bean;
    private String userid;
    private static final int VIDEO_CONTENT_DESC_MAX_LINE = 3;// 默认展示最大行数3行
    private static final int SHOW_CONTENT_NONE_STATE = 0;// 扩充
    private static final int SHRINK_UP_STATE = 1;// 收起状态
    private static final int SPREAD_STATE = 2;// 展开状态
    private static int mState = SHRINK_UP_STATE;//默认收起状态

    private RelativeLayout mShowMore;
    private ImageView mImageSpread;// 展开
    private ImageView mImageShrinkUp;// 收起
    String yuyue="",MyFocus="";
    private String hospitalid;
    private Set<String> times;
    private String PatientId;
    private EHomeDao dao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_detail);
        EventBus.getDefault().register(this);

        if(getIntent()!=null) {
            doctor_id = getIntent().getStringExtra("doctor_id");
            if(!TextUtils.isEmpty(getIntent().getStringExtra("department_id"))) {
                department_id = getIntent().getStringExtra("department_id");
            }
            User_FullName = getIntent().getStringExtra("User_FullName");
            Department_FullName = getIntent().getStringExtra("Department_FullName");
            if(getIntent().getStringExtra("Hosptial_Name")!=null)
                AppointmentActivity.hosptial = getIntent().getStringExtra("Hosptial_Name");
            if (getIntent().getStringExtra("yuyue") != null) {
                yuyue=getIntent().getStringExtra("yuyue");
            }
            if (getIntent().getStringExtra("MyFocus") != null) {
                MyFocus=getIntent().getStringExtra("MyFocus");
            }
        }
        requestMaker = RequestMaker.getInstance();
        userid = SharePreferenceUtil.getInstance(AppointmentDetailActivity.this).getUserId();
        initViews();
        times=new HashSet<String>();
        dao= new EHomeDaoImpl(AppointmentDetailActivity.this);
        PatientId=dao.findUserInfoById(userid).getPatientId();
        if(!TextUtils.isEmpty(yuyue)){
            iv_yy.setText("已预约");
        }
        initEvent();
        initData();

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

    private  void initViews() {
        setDefaultTXViewMethod(R.mipmap.icon_arrow_left, "", "", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finish();

            }
        }, new HeadView.OnRightClickListener() {
            @Override
            public void onClick() {

            }
        });
        iv_head=(CircleImageView)findViewById(R.id.iv_head);
        tv_name =(TextView)findViewById(R.id.tv_name);
        tv_zc=(TextView)findViewById(R.id.tv_zc);
        tv_doctor_jj=(TextView)findViewById(R.id.tv_doctor_jj);
        tv_sc=(TextView)findViewById(R.id.tv_sc);
        tv_focus=(TextView)findViewById(R.id.tv_focus);
        iv_yy=(TextView)findViewById(R.id.iv_yy);

//        mShowMore=(RelativeLayout) findViewById(R.id.more);
//        mImageSpread = (ImageView) mShowMore.findViewById(R.id.spread);
//        mImageShrinkUp = (ImageView) mShowMore.findViewById(R.id.shrink_up);
    }


    public void initEvent(){

        tv_focus.setOnClickListener(this);
        iv_yy.setOnClickListener(this);
//        mShowMore.setOnClickListener(this);
    }
    private  void initData() {


        requestMaker.DoctorInquiry(department_id,doctor_id,userid,new JsonAsyncTask_Info(AppointmentDetailActivity.this, true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                JSONObject mySO = (JSONObject) result;
                try {
                    bean=new DoctorBean();
                    JSONArray array = mySO.getJSONArray("DoctorInquiry");
                    JSONObject jsonObject = (JSONObject)array.get(0);
                    bean.setUser_Id(jsonObject.getString("Doctor_Id"));
                    bean.setUser_FullName(jsonObject.getString("User_FullName"));
                    User_FullName=jsonObject.getString("User_FullName");
                    bean.setHospital_Id(jsonObject.getString("Hospital_Id"));
                    hospitalid=jsonObject.getString("Hospital_Id");
                    String url=jsonObject.getString("User_Icon").replace("~", "").replace("\\", "/");
                    bean.setUser_Icon(Constants.ICON+url);
                    bean.setDoctor_Title(jsonObject.getString("Doctor_Title"));
                    bean.setDoctor_Specialty(jsonObject.getString("Doctor_Specialty"));
                    bean.setHosptial_Name(AppointmentActivity.hosptial);
                    bean.setHosptial_office(AppointmentActivity.office);
                    bean.setDoctorinfo(jsonObject.getString("Doctor_Intro"));
                    bean.setHosptial_Name(AppointmentActivity.hosptial);
                    if(jsonObject.getString("IsFavored").equals("0")){
                        tv_focus.setText("关注");
                    }else{
                        tv_focus.setText("已关注");
                    }
                    setData();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }));

    }

    public void setData(){

        ImageLoader.getInstance().displayImage(bean.getUser_Icon(),iv_head, ImageOptions.getHeaderOptions());
        tv_name.setText(bean.getUser_FullName());
        tv_zc.setText(bean.getDoctor_Title()+"/"+bean.getHosptial_Name());
        tv_doctor_jj.setText("暂无");
        tv_sc.setText(bean.getDoctor_Specialty());

//        tv_sc.post(new Runnable() {
//            @Override
//            public void run() {
//                if(tv_sc.getLineCount()>=3){
//
//                    mShowMore.setVisibility(View.VISIBLE);
//                }else{
//
//                    mShowMore.setVisibility(View.GONE);
//                }
//            }
//        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_focus:
                String focus=tv_focus.getText().toString();
                if("关注".equals(focus)){
                    doOnfocus();
                }else if("已关注".equals(focus)){
                    doCancelFocus();
                }
                EventBus.getDefault().post(new RefreshEvent(getResources().getInteger(R.integer.refresh_focus)));

                break;
            case R.id.iv_yy:
                if(iv_yy.getText().toString().equals("已预约")){
                    return;
                }
                if(!TextUtils.isEmpty(yuyue)){
                    iv_yy.setText("已预约");
                    ToastUtils.showMessage(AppointmentDetailActivity.this,"已预约");
                    return;
                }
                if(!TextUtils.isEmpty(MyFocus)) {
                    searchScheduleInquiry(1,doctor_id,PatientId);

//                    Intent mIntent1 = new Intent(AppointmentDetailActivity.this, MyDoctorActivity.class);
//                    mIntent1.putExtra("doctor", User_FullName);
//                    mIntent1.putExtra("doctor_id", doctor_id);
//                    mIntent1.putExtra("department_id", department_id);
//                    mIntent1.putExtra("hospitalid", hospitalid);
//                    mIntent1.putExtra("Department_FullName", Department_FullName);
//                    mIntent1.putExtra("department_id", department_id);
//
//
//                    startActivity(mIntent1);
                }else {
                    searchScheduleInquiry(2,doctor_id,PatientId);
//                    Intent mIntent = new Intent(DoctorFragment.ACTION_NAME);
//
//                    CustomApplcation.getInstance().finishSingleActivityByClass(AppointmentActivity.class);
//
//                    if (TextUtils.isEmpty(doctor_id) && TextUtils.isEmpty(department_id)) {
//                        mIntent.putExtra("doctor", bean.getUser_FullName());
//                        mIntent.putExtra("doctor_id", bean.getUser_Id());
//                    } else {
//                        mIntent.putExtra("doctor", User_FullName);
//                        mIntent.putExtra("doctor_id", doctor_id);
//                        mIntent.putExtra("department_id", department_id);
//                        mIntent.putExtra("hospitalid", hospitalid);
//
//
//                        mIntent.putExtra("Department_FullName", Department_FullName);
//
//                    }
//
//
//                    sendBroadcast(mIntent);
//                    finishActivity();
                }

                break;
            case R.id.more: {
                if (mState == SPREAD_STATE) {
                    tv_sc.setMaxLines(VIDEO_CONTENT_DESC_MAX_LINE);
                    tv_sc.requestLayout();
                    mImageShrinkUp.setVisibility(View.GONE);
                    mImageSpread.setVisibility(View.VISIBLE);
                    mState = SHRINK_UP_STATE;
                } else if (mState == SHRINK_UP_STATE) {
                    tv_sc.setMaxLines(Integer.MAX_VALUE);
                    tv_sc.requestLayout();
                    mImageShrinkUp.setVisibility(View.VISIBLE);
                    mImageSpread.setVisibility(View.GONE);
                    mState = SPREAD_STATE;
                }
                break;
            }
        }
    }

    public void doOnfocus(){
        requestMaker.FavorDoctorInsert(userid,doctor_id,new JsonAsyncTask_Info(AppointmentDetailActivity.this, true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                JSONObject mySO = (JSONObject) result;
                try {
                    JSONArray array = mySO.getJSONArray("FavorDoctorInsert");
                    JSONObject jsonObject = (JSONObject)array.get(0);
                    String msg=jsonObject.getString("MessageCode");
                    if("0".equals(msg)){
                        tv_focus.setText("已关注");
                    }else{
                        ToastUtils.showMessage(AppointmentDetailActivity.this,jsonObject.getString("MessageContent"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        }));
    }
    public void doCancelFocus(){
        requestMaker.FavorDoctorDelete(userid,doctor_id,new JsonAsyncTask_Info(AppointmentDetailActivity.this, true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                JSONObject mySO = (JSONObject) result;
                try {
                    JSONArray array = mySO.getJSONArray("FavorDoctorDelete");
                    JSONObject jsonObject = (JSONObject)array.get(0);
                    String msg=jsonObject.getString("MessageCode");
                    if("0".equals(msg)){
                        tv_focus.setText("关注");
                    }else{
                        ToastUtils.showMessage(AppointmentDetailActivity.this,jsonObject.getString("MessageContent"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        }));
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    public void onEventMainThread(RefreshEvent event) {

        if(getResources().getInteger(R.integer.back_detail) == event
                .getRefreshWhere()) {

            iv_yy.setText("已预约");
        }
    }
    private void searchScheduleInquiry(final int type,String id,String PatientId){
        requestMaker.ScheduleInquiry(id,PatientId, new JsonAsyncTask_Info(AppointmentDetailActivity.this, true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                JSONObject mySO = (JSONObject) result;
                Log.e("TAG", mySO.toString());
                Date date = new Date();
                try {
                    JSONArray array = mySO.getJSONArray("ScheduleInquiry");

                    for (int i = 0; i < array.length(); i++) {
                        ScheduleBean bean = new ScheduleBean();
                        JSONObject jsonObject = (JSONObject) array.get(i);
                        bean.setStatus(jsonObject.getString("Status"));
                        bean.setNum(jsonObject.getString("num"));
                        bean.setDate(jsonObject.getString("Date"));

                        bean.setPerTime(jsonObject.getString("PerTime"));
                        bean.setWeekDay(jsonObject.getString("WeekDay"));
                        bean.setPatientCount(jsonObject.getString("PatientCount"));
                        bean.setTimeSlot(jsonObject.getString("TimeSlot"));

                        bean.setTimeBlock(jsonObject.getString("TimeBlock"));
                        if(!TextUtils.isEmpty(jsonObject.getString("TimeSlot"))) {

                            times.add(jsonObject.getString("Date"));
                        }
                    }
                    if(times.size()>0) {
                        switch (type){
                            case 1:
                                Intent mIntent1 = new Intent(AppointmentDetailActivity.this, MyDoctorActivity.class);
                                mIntent1.putExtra("doctor", User_FullName);
                                mIntent1.putExtra("doctor_id", doctor_id);
                                mIntent1.putExtra("department_id", department_id);
                                mIntent1.putExtra("hospitalid", hospitalid);
                                mIntent1.putExtra("Department_FullName", Department_FullName);
                                mIntent1.putExtra("department_id", department_id);
                                startActivity(mIntent1);
                                break;
                            case 2:
                                Intent mIntent = new Intent(DoctorFragment.ACTION_NAME);

                                CustomApplcation.getInstance().finishSingleActivityByClass(AppointmentActivity.class);

                                if (TextUtils.isEmpty(doctor_id) && TextUtils.isEmpty(department_id)) {
                                    mIntent.putExtra("doctor", bean.getUser_FullName());
                                    mIntent.putExtra("doctor_id", bean.getUser_Id());
                                } else {
                                    mIntent.putExtra("doctor", User_FullName);
                                    mIntent.putExtra("doctor_id", doctor_id);
                                    mIntent.putExtra("department_id", department_id);
                                    mIntent.putExtra("hospitalid", hospitalid);


                                    mIntent.putExtra("Department_FullName", Department_FullName);

                                }
                                sendBroadcast(mIntent);
                                finishActivity();
                                break;
                        }



                    }else{
                        ToastUtils.showMessage(AppointmentDetailActivity.this,"该医生暂无坐诊时间!");

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ToastUtils.showMessage(AppointmentDetailActivity.this,"该医生暂无坐诊时间!");

                }
            }
        }));

    }

}
