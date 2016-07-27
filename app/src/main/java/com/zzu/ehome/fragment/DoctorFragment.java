package com.zzu.ehome.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zzu.ehome.R;
import com.zzu.ehome.activity.AppointmentActivity;
import com.zzu.ehome.activity.SelectDateActivity_1;
import com.zzu.ehome.activity.SelectHospitalActivity;
import com.zzu.ehome.activity.SelectOfficeActivity;
import com.zzu.ehome.bean.RefreshEvent;
import com.zzu.ehome.db.EHomeDao;
import com.zzu.ehome.db.EHomeDaoImpl;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.utils.ToastUtils;
import com.zzu.ehome.view.DialogTips;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;

/**
 * Created by zzu on 2016/4/7.
 */
public class DoctorFragment extends BaseFragment implements View.OnClickListener {
    private View view;
    private LinearLayout layout_add_hosptial;
    private LinearLayout layout_add_office;
    private LinearLayout layout_add_doctor;
    private LinearLayout layout_add_time;
    public static final int ADD_HOSPITAL = 0x00;
    public static final int ADD_OFFICE = 0x01;
    public static final int ADD_DOCTOR = 0x10;
    public static final int ADD_TIME = 0x11;
    private TextView tv_hosptial;
    private TextView tv_office;
    private TextView tv_doctor;
    private TextView tv_time;
    private Button btn_ok;
    private String hospital_id;
    private String department_id;
    private String doctor_id;
    private String DateStr;
    private String TimeSpanStr;
    private String PerTime;
    private String UserNo;
    //请求单例
    private RequestMaker requestMaker;
    public static final String ACTION_NAME = "ACTION_DOCTOR";
    private EHomeDao dao;
    String userid,PatientId;
    private boolean isPrepared;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_doctor, null);
        EventBus.getDefault().register(this);
        requestMaker = RequestMaker.getInstance();
        userid= SharePreferenceUtil.getInstance(getActivity()).getUserId();
        dao= new EHomeDaoImpl(getActivity());
        PatientId=dao.findUserInfoById(userid).getPatientId();

        initViews();
        initEvent();
        isPrepared = true;
        lazyLoad();
        return view;
    }

    public void initViews() {
        setOnlyTileViewMethod(view, "预约挂号");
        layout_add_hosptial = (LinearLayout) view.findViewById(R.id.layout_add_hosptial);
        layout_add_office = (LinearLayout) view.findViewById(R.id.layout_add_office);
        layout_add_doctor = (LinearLayout) view.findViewById(R.id.layout_add_doctor);
        layout_add_time = (LinearLayout) view.findViewById(R.id.layout_add_time);

        tv_hosptial = (TextView) view.findViewById(R.id.tv_hosptial);
        tv_office = (TextView) view.findViewById(R.id.tv_office);
        tv_doctor = (TextView) view.findViewById(R.id.tv_doctor);
        tv_time = (TextView) view.findViewById(R.id.tv_time);
        btn_ok = (Button) view.findViewById(R.id.btn_ok);
    }






    public void initEvent() {
        layout_add_hosptial.setOnClickListener(this);
        layout_add_office.setOnClickListener(this);
        layout_add_doctor.setOnClickListener(this);
        layout_add_time.setOnClickListener(this);
        btn_ok.setOnClickListener(this);
        registerBoradcastReceiver();

    }

    public static Fragment getInstance() {
        return new DoctorFragment();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_add_hosptial:
                Intent intent1 = new Intent(getActivity(), SelectHospitalActivity.class);
                startActivityForResult(intent1, ADD_HOSPITAL);
                break;
            case R.id.layout_add_office:
                if (TextUtils.isEmpty(hospital_id)) {
                    ToastUtils.showMessage(getActivity(), "请选医院");
                    return;
                }
                Intent intent2 = new Intent(getActivity(), SelectOfficeActivity.class);
                intent2.putExtra("hospital_id", hospital_id);
                startActivityForResult(intent2, ADD_OFFICE);
                break;
            case R.id.layout_add_doctor:
                if (TextUtils.isEmpty(department_id)) {
                    ToastUtils.showMessage(getActivity(), "请选择科室");
                    return;
                }
                Intent intent = new Intent(getActivity(), AppointmentActivity.class);
                intent.putExtra("department_id", department_id);
                intent.putExtra("hosptial", tv_hosptial.getText().toString());
                intent.putExtra("office", tv_office.getText().toString());
                startActivityForResult(intent, ADD_DOCTOR);
                break;
            case R.id.layout_add_time:
                if (TextUtils.isEmpty(doctor_id)) {
                    ToastUtils.showMessage(getActivity(), "请选择医生");
                    return;
                }
                Intent intenttime = new Intent(getActivity(), SelectDateActivity_1.class);
                intenttime.putExtra("department_id", department_id);
                intenttime.putExtra("doctor_id", doctor_id);
                startActivityForResult(intenttime, ADD_TIME);
                break;
            case R.id.btn_ok:
                confirmSave();

                break;
        }
    }
    public void confirmSave() {
        String hosptial = tv_hosptial.getText().toString();
        String office = tv_office.getText().toString();
        String doctor = tv_doctor.getText().toString();
        String time = tv_time.getText().toString();
        UserNo=dao.findUserInfoById(userid).getUserno();
        if (TextUtils.isEmpty(hosptial)) {
            ToastUtils.showMessage(getActivity(), "请选择医院");
            return;
        }
        if (TextUtils.isEmpty(office) || tv_office.getVisibility() == View.INVISIBLE) {
            ToastUtils.showMessage(getActivity(), "请选择科室");
            return;

        }
        if (TextUtils.isEmpty(doctor) || tv_doctor.getVisibility() == View.INVISIBLE) {
            ToastUtils.showMessage(getActivity(), "请选择医生");
            return;

        }
        if (TextUtils.isEmpty(time) || tv_time.getVisibility() == View.INVISIBLE) {
            ToastUtils.showMessage(getActivity(), "请选择时间");
            return;
        }
        if(TextUtils.isEmpty(UserNo)){
            ToastUtils.showMessage(getActivity(), "用户身份证不能为空，请先完善个人资料！");
            return;
        }
        DialogTips dialog = new DialogTips(getActivity(), "","您确定预约"+tv_hosptial.getText().toString()+tv_office.getText().toString()+tv_doctor.getText().toString()+"医生"+tv_time.getText().toString()+"的号码吗？",
                "确定", true, true);
        dialog.SetOnSuccessListener(new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int userId) {
                startProgressDialog();
                requestMaker.TreatmentInsert(doctor_id, PatientId, DateStr,TimeSpanStr,PerTime, new JsonAsyncTask_Info(getActivity(), true, new JsonAsyncTaskOnComplete() {
                    @Override
                    public void processJsonObject(Object result) {
                        JSONObject mySO = (JSONObject) result;
                        try {
                            JSONArray array = mySO.getJSONArray("TreatmentInsert");
                            JSONObject jsonObject=(JSONObject)array.get(0);
                            String code=jsonObject.getString("MessageCode");
                            stopProgressDialog();
                            if("0".equals(code)){
                                ToastUtils.showMessage(getActivity(),jsonObject.getString("MessageContent"));
                                tv_hosptial.setText("");
                                tv_office.setText("");
                                tv_doctor.setText("");
                                tv_time.setText("");
                                hospital_id="";
                                department_id="";
                                doctor_id="";
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


    public void save() {





    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_HOSPITAL && resultCode == ADD_HOSPITAL && data != null) {
            String hosptial = data.getStringExtra("hospital");
            hospital_id = data.getStringExtra("hospital_id");
            if (!TextUtils.isEmpty(hosptial)) {
                tv_hosptial.setText(hosptial);
                tv_office.setText("");
                tv_doctor.setText("");
                tv_time.setText("");
            }
        }
        if (requestCode == ADD_OFFICE && resultCode == ADD_OFFICE && data != null) {
            String office = data.getStringExtra("department");
            department_id = data.getStringExtra("department_id");
            if (!TextUtils.isEmpty(office)) {
                tv_office.setVisibility(View.VISIBLE);
                tv_office.setText(office);
                tv_doctor.setText("");
                tv_time.setText("");
            }
        }

        if (requestCode == ADD_DOCTOR && resultCode == ADD_DOCTOR && data != null) {
            String doctor = data.getStringExtra("doctor");
            doctor_id = data.getStringExtra("doctor_id");
            if (!TextUtils.isEmpty(doctor)) {
                tv_doctor.setVisibility(View.VISIBLE);
                tv_doctor.setText(doctor);
                tv_time.setText("");
            }
        }
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

    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(ACTION_NAME);
        //注册广播
        getActivity().registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ACTION_NAME)) {
                String doctor = intent.getStringExtra("doctor");
                doctor_id = intent.getStringExtra("doctor_id");
                department_id=intent.getStringExtra("department_id");
                String office=intent.getStringExtra("Department_FullName");

                if (!TextUtils.isEmpty(doctor)) {
                    if(!TextUtils.isEmpty(department_id)&&!TextUtils.isEmpty(office)){
                        hospital_id=intent.getStringExtra("hospitalid");
                        tv_hosptial.setText(AppointmentActivity.hosptial);
                        tv_office.setText(office);
                    }
                    tv_office.setVisibility(View.VISIBLE);
                    tv_doctor.setVisibility(View.VISIBLE);
                    tv_doctor.setText(doctor);
                    tv_time.setText("");
                    Intent intentGuaHao = new Intent();
                    intentGuaHao.setAction("action.DateOrFile");
                    intentGuaHao.putExtra("msgGua","Gua");
                    getActivity().sendBroadcast(intentGuaHao);
                }

            }
        }
    };
    public void onEventMainThread(RefreshEvent event) {

        if(getResources().getInteger(R.integer.back_detail) == event
                .getRefreshWhere()) {

            tv_hosptial.setText("");
            tv_office.setText("");
            tv_doctor.setText("");
            tv_time.setText("");
            hospital_id="";
            department_id="";
            doctor_id="";
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mBroadcastReceiver);
        EventBus.getDefault().unregister(this);
    }
    @Override
    protected void lazyLoad() {


    }
}
