package com.zzu.ehome.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.umeng.analytics.MobclickAgent;
import com.zzu.ehome.R;
import com.zzu.ehome.adapter.AppointmentAdapter;
import com.zzu.ehome.adapter.SelectDateAdapter;
import com.zzu.ehome.application.Constants;
import com.zzu.ehome.bean.DoctorBean;
import com.zzu.ehome.bean.ScheduleBean;
import com.zzu.ehome.db.EHomeDao;
import com.zzu.ehome.db.EHomeDaoImpl;
import com.zzu.ehome.fragment.DoctorFragment;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.utils.ToastUtils;
import com.zzu.ehome.view.HeadView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by zzu on 2016/4/12.
 * 预约医生
 */
public class AppointmentActivity extends BaseActivity {
    private final String mPageName = "AppointmentActivity";
    private ListView listView;
    public  static String department_id;
    public static String hosptial,userid;
    public static String office;
    private List<DoctorBean> mList;
    //请求单例
    private RequestMaker requestMaker;
    public static Activity appointmentAct;
    private String PatientId;
    private EHomeDao dao;
    private Set<String> times;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_appointment);
        department_id=getIntent().getStringExtra("department_id");
        hosptial=getIntent().getStringExtra("hosptial");
        office=getIntent().getStringExtra("office");
        requestMaker = RequestMaker.getInstance();
        userid = SharePreferenceUtil.getInstance(AppointmentActivity.this).getUserId();
        dao= new EHomeDaoImpl(AppointmentActivity.this);
        PatientId=dao.findUserInfoById(userid).getPatientId();
        initViews();
        initData();
        initEvent();
        appointmentAct=this;
        times=new HashSet<String>();

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

    public void initViews(){
        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "预约医生", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();
            }
        });
        listView=(ListView) findViewById(R.id.listDoctor);

    }

    public void initData(){
        requestMaker.DoctorInquiry(department_id,new JsonAsyncTask_Info(AppointmentActivity.this, true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                JSONObject mySO = (JSONObject) result;
                try {
                    JSONArray array = mySO.getJSONArray("DoctorInquiry");
                    mList=new ArrayList<DoctorBean>();
//                    {
//                        "DoctorInquiry": [
//                        {
//                            "Doctor_Id": "24",
//                                "User_FullName": "范琳琳",
//                                "User_Icon": "patient.jpg",
//                                "Doctor_Title": "副主任医师",
//                                "Doctor_Specialty": "河南省神经免疫学会委员，河南省老年病学会委员，河南省中西医结合神经内科专业委员会委员，河南省司法鉴定专家。长期从事神经内科临床工作，擅长急重症脑血管病、多发性硬化及脱髓鞘疾病、头晕头痛、帕金森病、癫痫、痴呆及神经内科疑难病诊治。先后在国内外发表本专业学术论文10余篇，参编神经病学专著1部。近年来荣获郑州市医德标兵、郑州大学优秀医师等称号。",
//                                "IsFavored": "0"
//                        },
//                        {
//                            "Doctor_Id": "44",
//                                "User_FullName": "于泳",
//                                "User_Icon": "159_39.jpg",
//                                "Doctor_Title": "主任医师",
//                                "Doctor_Specialty": "",
//                                "IsFavored": "0"
//                        }
//                        ]
//                    }
                    for(int i=0;i<array.length();i++) {
                        DoctorBean bean=new DoctorBean();
                        JSONObject jsonObject = (JSONObject) array.get(i);
                        bean.setUser_Id(jsonObject.getString("Doctor_Id"));
                        bean.setUser_FullName(jsonObject.getString("User_FullName"));
                        String url=jsonObject.getString("User_Icon").replace("~", "").replace("\\", "/");
                        bean.setUser_Icon(Constants.ICON+url);
                        bean.setDoctor_Title(jsonObject.getString("Doctor_Title"));
                        bean.setDoctor_Specialty(jsonObject.getString("Doctor_Specialty"));
                        bean.setHosptial_Name(hosptial);
                        bean.setHosptial_office(office);
                        mList.add(bean);
                    }
                    listView.setAdapter(new AppointmentAdapter(AppointmentActivity.this,mList));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }));
    }

    public void initEvent(){

    }
    public  void setIntent(final String str,final String id){
//        Intent intent=new Intent();
//        intent.putExtra("doctor",str);
//        intent.putExtra("doctor_id",id);
//        setResult(DoctorFragment.ADD_DOCTOR, intent);
//        finish();

        requestMaker.ScheduleInquiry(id,PatientId, new JsonAsyncTask_Info(AppointmentActivity.this, true, new JsonAsyncTaskOnComplete() {
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
                        Intent intent=new Intent();
                        intent.putExtra("doctor",str);
                        intent.putExtra("doctor_id",id);
                        setResult(DoctorFragment.ADD_DOCTOR, intent);
                        finish();


                    }else{
                        ToastUtils.showMessage(AppointmentActivity.this,"该医生暂无坐诊时间!");

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ToastUtils.showMessage(AppointmentActivity.this,"该医生暂无坐诊时间!");

                }
            }
        }));


    }



}
