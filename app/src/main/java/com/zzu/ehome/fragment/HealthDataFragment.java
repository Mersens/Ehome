package com.zzu.ehome.fragment;


import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.zzu.ehome.R;

import com.zzu.ehome.activity.DataChatActivity;
import com.zzu.ehome.activity.JibuDataActivity;
import com.zzu.ehome.activity.TiwenDataActivity;
import com.zzu.ehome.activity.TizhongActivity;
import com.zzu.ehome.activity.TizhongDataActivity;
import com.zzu.ehome.activity.XuetangDataActivity;
import com.zzu.ehome.activity.XueyaDataActivity;
import com.zzu.ehome.activity.YYJLDataActivity;
import com.zzu.ehome.bean.HealthBean;
import com.zzu.ehome.bean.HealthData;
import com.zzu.ehome.bean.HealthDataRes;
import com.zzu.ehome.bean.HealthDataSearchByDate;

import com.zzu.ehome.bean.RefreshEvent;
import com.zzu.ehome.bean.StepBean;
import com.zzu.ehome.bean.StepCounterBean;
import com.zzu.ehome.bean.StepCounterDate;
import com.zzu.ehome.bean.User;
import com.zzu.ehome.db.EHomeDao;
import com.zzu.ehome.db.EHomeDaoImpl;
import com.zzu.ehome.utils.CommonUtils;
import com.zzu.ehome.utils.DateUtils;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.JsonTools;
import com.zzu.ehome.utils.PermissionsChecker;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.utils.ToastUtils;
import com.zzu.ehome.view.DialogTips;


import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import de.greenrobot.event.EventBus;
import android.Manifest;

/**
 * Created by zzu on 2016/4/9.
 */
public class HealthDataFragment extends BaseFragment implements View.OnClickListener {
    private static final String PACKAGE_URL_SCHEME = "package:";
    private PermissionsChecker mPermissionsChecker; // 权限检测器
    // 所需的全部权限
    static final String[] PERMISSIONS = new String[]{

            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private View view;
    private RequestMaker requestMaker;
    private String userid, date,CurrnetDate;
    private TextView tz_num, tvbmi, tw_num, xt_num, xy_num,tvdate,tw_status,tv3,tv5,tv_yp,tv_xy,tv_xt,yp_num,tv_bimstatus,jibu_num,textView9;
    private ProgressBar pbweight, pbtw, pbBloodSuggar, pbPress1, pbPress2,pbDate;
    Map<String, HealthBean> map = new HashMap<String, HealthBean>();
    private LinearLayout layout_tz, layout_tw, layout_xt, layout_xy, layout_yp,layout_jibu;
    private ImageView imageView_lift,imageView_right;
    User dbUser;
    private EHomeDao dao;
    private RequestMaker requestmaker;

    private BroadcastReceiver mRefrushBroadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            if(action.equals("action.Weight")){
                dbUser=dao.findUserInfoById(userid);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                CurrnetDate=sdf.format(CommonUtils.changeDate(-1).getTime() + 60 * 60 * 24 * 1000);
                date = sdf.format(CommonUtils.changeDate(-1).getTime() + 60 * 60 * 24 * 1000);
                tvdate.setText(DateUtils.getDateDetail(date)+DateUtils.StringPattern(date,"yyyy-MM-dd","MM月dd日"));
                EventBus.getDefault().post(new RefreshEvent(getResources().getInteger(R.integer.refresh_info)));
                initData();
            }
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_healthdata, null);
        EventBus.getDefault().register(this);
        requestMaker = RequestMaker.getInstance();
        userid = SharePreferenceUtil.getInstance(getActivity()).getUserId();
        dao=new EHomeDaoImpl(getActivity());
        dbUser=dao.findUserInfoById(userid);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("action.Weight");
        getActivity().registerReceiver(mRefrushBroadcastReceiver, intentFilter);
        mPermissionsChecker = new PermissionsChecker(getActivity());
        initView();
        initEvent();
        return view;
    }

    public void initView() {

        tz_num = (TextView) view.findViewById(R.id.tz_num);
        tvbmi = (TextView) view.findViewById(R.id.tv_bim);
        tw_num = (TextView) view.findViewById(R.id.tw_num);
        pbweight = (ProgressBar) view.findViewById(R.id.pb_progressbar);
        pbtw = (ProgressBar) view.findViewById(R.id.progressBar1);
        xt_num = (TextView) view.findViewById(R.id.xt_num);
        pbBloodSuggar = (ProgressBar) view.findViewById(R.id.progressBar2);
        xy_num = (TextView) view.findViewById(R.id.xy_num);
        pbPress1 = (ProgressBar) view.findViewById(R.id.progressBar3);
        pbPress2 = (ProgressBar) view.findViewById(R.id.progressBar4);
        layout_tz = (LinearLayout) view.findViewById(R.id.layout_tz);
        layout_jibu=(LinearLayout) view.findViewById(R.id.layout_jibu);
        layout_tw=(LinearLayout) view.findViewById(R.id.layout_tw);
        layout_xt=(LinearLayout) view.findViewById(R.id.layout_xt);
        layout_xy=(LinearLayout) view.findViewById(R.id.layout_xy);
        layout_yp=(LinearLayout) view.findViewById(R.id.layout_yp);
        imageView_lift=(ImageView) view.findViewById(R.id.imageView_lift);
        imageView_right=(ImageView) view.findViewById(R.id.imageView_right);
        tvdate=(TextView)view.findViewById(R.id.textView);
        jibu_num=(TextView)view.findViewById(R.id.jibu_num);
        textView9=(TextView)view.findViewById(R.id.textView9);
        tw_status=(TextView) view.findViewById(R.id.textView2);
        tv_yp=(TextView)view.findViewById(R.id.tv_yp);
        tv_xy=(TextView)view.findViewById(R.id.tv_xy);
        tv_xt=(TextView)view.findViewById(R.id.tv_xt);
        yp_num=(TextView)view.findViewById(R.id.yp_num);
        tv_bimstatus=(TextView)view.findViewById(R.id.tv_bimstatus);
        pbDate=(ProgressBar)view.findViewById(R.id.progressBar8);

//        Typeface typeFace = Typeface.createFromAsset(getActivity().getAssets(),"fonts/font.ttf");
//// 应用字体
//        tv_yp.setTypeface(typeFace);
//        Typeface typeFace1 = Typeface.createFromAsset(getActivity().getAssets(),"fonts/normal.ttf");
//        tv_xy.setTypeface(typeFace1);
//        Typeface typeFace2 = Typeface.createFromAsset(getActivity().getAssets(),"fonts/simple.ttf");
//        tv_xt.setTypeface(typeFace2);
// 应用字体
        tv3=(TextView)view.findViewById(R.id.tv3);
        tv5=(TextView)view.findViewById(R.id.tv5);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        date = sdf.format(CommonUtils.changeDate(-1).getTime() + 60 * 60 * 24 * 1000);
        tvdate.setText(DateUtils.getDateDetail(date)+DateUtils.StringPattern(date,"yyyy-MM-dd","MM月dd日"));
    }

    public void initEvent(){
        layout_tz.setOnClickListener(this);
        layout_tw.setOnClickListener(this);
        layout_xy.setOnClickListener(this);
        layout_xt.setOnClickListener(this);
        layout_yp.setOnClickListener(this);
        imageView_lift.setOnClickListener(this);
        imageView_right.setOnClickListener(this);
        layout_jibu.setOnClickListener(this);


    }

    public void initData() {
        pbtw.setProgress(0);
        pbweight.setProgress(0);
        pbBloodSuggar.setProgress(0);
        pbPress1.setProgress(0);
        pbPress2.setProgress(0);
        startProgressDialog();

        requestMaker.HealthDataSearchByDate(userid, date, new JsonAsyncTask_Info(getActivity(), true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                try {
                    JSONObject mySO = (JSONObject) result;
                    org.json.JSONArray array = mySO
                            .getJSONArray("HealthDataSearchByDate");
                    getJIbu();
                    stopProgressDialog();

                    if (array.getJSONObject(0).has("MessageCode")) {
                        tz_num.setText("0.0");
                        tw_num.setText("0.0");
                        xt_num.setText("0.0");
                        xy_num.setText("0/0");
                        pbweight.setProgress(0);
                        pbtw.setProgress(0);
                        pbBloodSuggar.setProgress(0);
                        pbweight.setProgress(0);
                        pbPress1.setProgress(0);
                        pbPress2.setProgress(0);
                        tw_status.setVisibility(View.GONE);

                        tv3.setVisibility(View.GONE);

                        tv5.setVisibility(View.GONE);

                        tvbmi.setText("BMI:0");
                        tv_bimstatus.setVisibility(View.GONE);
                        yp_num.setText(0+"");
                        tv_yp.setText("药品名：");

                    } else {
                        HealthDataSearchByDate date = JsonTools.getData(result.toString(), HealthDataSearchByDate.class);
                        List<HealthBean> list = date.getData();
                        map.clear();
                        for (HealthBean bean : list) {
                            map.put(bean.getType(), bean);
                        }
                        if (map.get("Weight")!=null) {

                            tz_num.setText(map.get("Weight").getValue1());
                            if(!TextUtils.isEmpty(dbUser.getUserHeight())) {
                                tv_bimstatus.setVisibility(View.VISIBLE);
                                float w = Float.valueOf(map.get("Weight").getValue1());
                                float h = Float.valueOf(dbUser.getUserHeight()) / 100;
                                float bmi = w / (h * h);
                                BigDecimal b = new BigDecimal(bmi);
                                tv_bimstatus.setText(CommonUtils.showBMIDetail(b.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue()));
                                DecimalFormat decimalFormat = new DecimalFormat("0.0");
                                tvbmi.setText("BMI:" + decimalFormat.format(bmi));
                                String showBmi = CommonUtils.showBMIDetail(bmi);
                                if (showBmi.equals(CommonUtils.TINY)) {
                                    tv_bimstatus.setTextColor(Color.parseColor("#97dee9"));
                                } else if (showBmi.equals(CommonUtils.NORMAL)) {
                                    tv_bimstatus.setTextColor(Color.parseColor("#53bbb3"));
                                } else if (showBmi.equals(CommonUtils.OVERLOAD)) {
                                    tv_bimstatus.setTextColor(Color.parseColor("#fa3b00"));
                                } else if (showBmi.equals(CommonUtils.SAMLL)) {
                                    tv_bimstatus.setTextColor(Color.parseColor("#97dee9"));
                                } else if (showBmi.equals(CommonUtils.MIDDLE)) {
                                    tv_bimstatus.setTextColor(Color.parseColor("#ea0b35"));
                                } else if (showBmi.equals(CommonUtils.BIG)) {
                                    tv_bimstatus.setTextColor(Color.parseColor("#fa5779"));
                                }
                            }else{
                                tv_bimstatus.setVisibility(View.GONE);
                            }
                            float num = Float.valueOf(map.get("Weight").getValue1()) / 150.0f;
                            pbStartAnima(pbweight,(int)(num*100));

                        } else {
                            tz_num.setText("0.0");
                            pbweight.setProgress(0);
                            pbStartAnima(pbweight,0);
                            tvbmi.setText("BMI:0");
                            tv_bimstatus.setVisibility(View.GONE);
                            tv_bimstatus.setText("");

                        }
                        if (map.get("Temperature")!=null) {

                            float num = (Float.valueOf(map.get("Temperature").getValue1()) - 35.0f) / (45.0f - 35.0f);
                            pbStartAnima(pbtw,(int)(num*100));
                            tw_num.setText(map.get("Temperature").getValue1());

                                float tw = Float.parseFloat(map.get("Temperature").getValue1());
                            tw_status.setVisibility(View.VISIBLE);
                                if (Float.compare(tw, 37.1F) >= 0 && Float.compare(tw, 38F) <= 0) {
                                    tw_status.setText("低热");
                                    tw_status.setTextColor(Color.parseColor("#f9a116"));


                                } else if (Float.compare(tw, 38.1F) >= 0 && Float.compare(tw, 39F) <= 0) {
                                    tw_status.setText("中等度热");
                                    tw_status.setTextColor(Color.parseColor("#fb7701"));

                                } else if (Float.compare(tw, 39.1F) >= 0 && Float.compare(tw, 41) <= 0) {
                                    tw_status.setText("高热");
                                    tw_status.setTextColor(Color.parseColor("#fa3b00"));

                                } else if (Float.compare(tw, 41F) > 0) {

                                    tw_status.setText("超高热");
                                    tw_status.setTextColor(Color.parseColor("#ea0b35"));

                                } else {
                                    tw_status.setText("正常");

                                    tw_status.setTextColor(Color.parseColor("#53bbb3"));
                                }

                        } else {
                            tw_num.setText("0.0");
                            tw_status.setVisibility(View.GONE);
                            pbStartAnima(pbtw,0);
                        }
                        if (map.get("BloodSugar")!=null) {
                            float bnum = Float.valueOf(map.get("BloodSugar").getValue1());
                            if (Integer.valueOf(map.get("BloodSugar").getValue3()) == 2) {
                                bnum = bnum / 18.0f;
                            }
                            float num = bnum / 33.0f;
                            DecimalFormat decimalFormat = new DecimalFormat("0.0");
                            xt_num.setText(decimalFormat.format(bnum));
                            tv3.setVisibility(View.VISIBLE);
                            checkSuager(bnum,map.get("BloodSugar").getValue2());
                            pbStartAnima(pbBloodSuggar,(int)(num*100));

                        } else {
                            xt_num.setText("0.0");
                            pbStartAnima(pbBloodSuggar,0);
                            tv3.setVisibility(View.GONE);

                        }
                        if (map.get("BloodPressure")!=null) {
                            xy_num.setText(map.get("BloodPressure").getValue1() + "/" + map.get("BloodPressure").getValue2());
                            float num1 = (Float.valueOf(map.get("BloodPressure").getValue1())) / 300f;
                            float num2 = (Float.valueOf(map.get("BloodPressure").getValue2())) / 300f;
                            pbStartAnima(pbPress1,(int)(num1*100));
                            pbStartAnima(pbPress2,(int)(num2*100));
                            tv5.setVisibility(View.VISIBLE);
                            changeImgeView(tv5,Integer.valueOf(map.get("BloodPressure").getValue1()),Integer.valueOf(map.get("BloodPressure").getValue2()));
                        } else {
                            tv5.setVisibility(View.GONE);
                            xy_num.setText("0/0");
                            pbStartAnima(pbPress1,0);
                            pbStartAnima(pbPress2,0);
                        }
                        if (map.get("MedicationRecord")!=null) {

                            yp_num.setText(map.get("MedicationRecord").getValue2());
                            tv_yp.setText("药品名："+map.get("MedicationRecord").getValue1());
                        }else{
                            yp_num.setText(0+"");
                            tv_yp.setText("药品名：");
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        }));
    }
    private void getJIbu(){
        /**
         * 查询历史数据
         */
        requestMaker.StepCounterInquiry(userid, date,new JsonAsyncTask_Info(getActivity(), true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                try {
                    JSONObject mySO = (JSONObject) result;
                    org.json.JSONArray array = mySO
                            .getJSONArray("StepCounterInquiry");
                    if (array.getJSONObject(0).has("MessageCode"))
                    {
                        jibu_num.setText("0.0");
                        textView9.setText("距离"+"0"+"公里");
                        pbStartAnima(pbDate,0);

                    }else {

                        StepCounterDate date = JsonTools.getData(result.toString(), StepCounterDate.class);
                        List<StepCounterBean> list = date.getData();
                        jibu_num.setText(list.get(0).getTotalStep());
                        textView9.setText("距离"+list.get(0).getTotalDistance()+"公里");
                        float num = Float.valueOf(list.get(0).getTotalStep()) / 10000f;
                        if(Float.compare(num,1.00f)>0){
                            num=1.00f;
                        }
                        pbStartAnima(pbDate,(int)(num*100));

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }));
    }
    private void changeImgeView(TextView tv, int ssz, int szy) {
        int lvssz = CommonUtils.computeSsz(ssz);
        int lvszy = CommonUtils.computeSzy(szy);
        int lv = CommonUtils.MaxInt(lvssz, lvszy);
        switch (lv) {
            case 1:

                tv.setText("血压正常");
                tv.setTextColor(Color.parseColor("#53bbb3"));

                break;
            case 2:

                tv.setText("高血压一期");
                tv.setTextColor(Color.parseColor("#fb7701"));
                break;
            case 3:

                tv.setText("高血压二期");
                tv.setTextColor(Color.parseColor("#fa3b00"));
                break;
            case 4:

                tv.setText("高血压三期");
                tv.setTextColor(Color.parseColor("#ea0b35"));
                break;

        }

    }
    private void checkSuager(float value,String time) {
        if(time.contains("餐后")||time.contains("睡前")){
            if(Float.compare(value,6.7F)>=0&&Float.compare(value,11.1F)<=0){

                tv3.setText("正常");
                tv3.setTextColor(Color.parseColor("#53bbb3"));
            }else if(Float.compare(value,11.1F)>0){
                tv3.setText("高血糖");
                tv3.setTextColor(Color.parseColor("#ea0b35"));
            }else{
                tv3.setText("低血糖");
                tv3.setTextColor(Color.parseColor("#fb7701"));
            }

        }else{
            if(Float.compare(value,3.9F)<0){
                tv3.setText("低血糖");
                tv3.setTextColor(Color.parseColor("#fb7701"));

            }else if(Float.compare(value,3.9F)>=0&&Float.compare(value,6.1F)<=0){
                tv3.setText("正常");
                tv3.setTextColor(Color.parseColor("#53bbb3"));

            }else{
                tv3.setText("高血糖");
                tv3.setTextColor(Color.parseColor("#ea0b35"));

            }

        }


    }

    public static Fragment getInstance() {
        return new HealthDataFragment();
    }

    @Override
    protected void lazyLoad() {

    }

    public void onEventMainThread(RefreshEvent event) {
        if (getResources().getInteger(R.integer.refresh_manager_data) == event
                .getRefreshWhere()) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            dbUser=dao.findUserInfoById(userid);
            CurrnetDate=sdf.format(CommonUtils.changeDate(-1).getTime() + 60 * 60 * 24 * 1000);
            date = sdf.format(CommonUtils.changeDate(-1).getTime() + 60 * 60 * 24 * 1000);
            tvdate.setText(DateUtils.getDateDetail(date)+DateUtils.StringPattern(date,"yyyy-MM-dd","MM月dd日"));
            EventBus.getDefault().post(new RefreshEvent(getResources().getInteger(R.integer.refresh_info)));
            imageView_lift.setVisibility(View.VISIBLE);
            imageView_right.setVisibility(View.INVISIBLE);
            initData();


        }
        if (getResources().getInteger(R.integer.refresh_manager) == event
                .getRefreshWhere()) {
            dbUser=dao.findUserInfoById(userid);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            CurrnetDate=sdf.format(CommonUtils.changeDate(-1).getTime() + 60 * 60 * 24 * 1000);
            date = sdf.format(CommonUtils.changeDate(-1).getTime() + 60 * 60 * 24 * 1000);
            tvdate.setText(DateUtils.getDateDetail(date)+DateUtils.StringPattern(date,"yyyy-MM-dd","MM月dd日"));
            imageView_lift.setVisibility(View.VISIBLE);
            imageView_right.setVisibility(View.INVISIBLE);
            EventBus.getDefault().post(new RefreshEvent(getResources().getInteger(R.integer.refresh_info)));
            initData();

        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        try {
            getActivity().unregisterReceiver(mRefrushBroadcastReceiver);

            mRefrushBroadcastReceiver = null;
        } catch (Exception e) {
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_tz:
                intentAction(getActivity(),TizhongDataActivity.class,date,1);
                break;
            case R.id.layout_jibu:
//                if(TextUtils.isEmpty(SharePreferenceUtil.getInstance(getActivity()).getWeight())){
//                    intentAction(getActivity(), TizhongActivity.class,date,1);
//                }else {
                    intentAction(getActivity(), JibuDataActivity.class, date, 5);
//                }
                break;
            case R.id.layout_tw:
                intentAction(getActivity(),TiwenDataActivity.class,date,0);
                break;
            case R.id.layout_xt:
                intentAction(getActivity(),XuetangDataActivity.class,date,3);
                break;
            case R.id.layout_xy:
                intentAction(getActivity(),XueyaDataActivity.class,date,2);
                break;
            case R.id.layout_yp:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
                        showMissingPermissionDialog();
                        return;
                    }
                }
                intentAction(getActivity(),YYJLDataActivity.class,date,4);
                break;
            case R.id.imageView_lift:
                try {
                    DateFormat fmt =new SimpleDateFormat("yyyy-MM-dd");
                    Date day = fmt.parse(date);
                    date = fmt.format(day.getTime()-60 * 60 * 24 * 1000);
                    initData();
                    tvdate.setText(DateUtils.getDateDetail(date)+DateUtils.StringPattern(date,"yyyy-MM-dd","MM月dd日"));
                    imageView_right.setVisibility(View.VISIBLE);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                break;

            case R.id.imageView_right:
                try {
                    DateFormat fmt =new SimpleDateFormat("yyyy-MM-dd");
                    Date day = fmt.parse(date);
                    date = fmt.format(day.getTime()+60 * 60 * 24 * 1000);
                    initData();
                    tvdate.setText(DateUtils.getDateDetail(date)+DateUtils.StringPattern(date,"yyyy-MM-dd","MM月dd日"));
                    if(date.equals(CurrnetDate)){
                        imageView_right.setVisibility(View.INVISIBLE);
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                break;
        }
    }

    public <T> void intentAction(Activity context, Class<T> cls,String time,int postion) {
        Intent intent = new Intent(context, cls);
        intent.putExtra("position",postion);
        intent.putExtra("time",time);
        startActivity(intent);
    }
    public void pbStartAnima(final ProgressBar pb,final int progress){
        new Thread() {
            public void run() {
                int v1=1;
                while(v1 <= progress) {
                    try {
                        if(progress>50){
                            v1+=10;
                        }else
                        v1+=5;
                        Thread.sleep(50);
                        pb.setProgress(v1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                pb.setProgress(progress);
            }
        }.start();

    }

    // 显示缺失权限提示
    private void showMissingPermissionDialog() {
        DialogTips dialog = new DialogTips(getActivity(), "请点击设置，打开所需存储权限",
                "确定");
        dialog.SetOnSuccessListener(new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int userId) {
                startAppSettings();

            }
        });

        dialog.show();
        dialog = null;

    }
    // 启动应用的设置
    private void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse(PACKAGE_URL_SCHEME + getActivity().getPackageName()));
        startActivity(intent);
    }

}
