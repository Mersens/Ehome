package com.zzu.ehome.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zzu.ehome.R;
import com.zzu.ehome.activity.SelectDateAndTime;
import com.zzu.ehome.application.Constants;
import com.zzu.ehome.bean.HealteData;
import com.zzu.ehome.bean.HealthDataRes;
import com.zzu.ehome.bean.RefreshEvent;
import com.zzu.ehome.utils.CommonUtils;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.JsonTools;
import com.zzu.ehome.utils.OnSelectItemListener;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.utils.ToastUtils;
import com.zzu.ehome.view.TuneWheel;
import com.zzu.ehome.view.VerticalProgressBar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


import de.greenrobot.event.EventBus;


/**
 * Created by zzu on 2016/4/11.
 * 血压
 */
public class BloodPressureFragment extends BaseFragment {
    private View view;
    private TextView tv_ssy, tv_szy, tv_mb, tvcltime;
    private RelativeLayout rlchecktime;
    private TuneWheel wheel1, wheel2, wheel3;
    private RequestMaker requestMaker;
    private int ssy, szy, mb;
    String chtime;
    private Button btnsave;
    String userid;
    private ImageView imagepress;
    private TextView tvlv;
    private OnSelectItemListener mListener;
    public static int p=-1;
    private VerticalProgressBar progressBar1,progressBar2;

    private int pbvalue,pbvalue2;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_bloodpressure, null);
        requestMaker = RequestMaker.getInstance();
        userid = SharePreferenceUtil.getInstance(getActivity()).getUserId();

        initViews();
        initEvent();
//        progressBar1.setProgress(100);
//        HandlerInit();
//        handler.post(runnable);
        return view;
    }
//    private void HandlerInit() {
//        handler = new Handler();
//        runnable = new Runnable() {
//            @Override
//            public void run() {
//                progressBar1.setProgress(value);
//
//                value += 2;
//                if (value >= 100) {
//                    value = 0;
//                }
//                handler.postDelayed(runnable, 10);
//            }
//        };
//    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        try{
            mListener =(OnSelectItemListener)activity;
        }catch(ClassCastException e){
            throw new ClassCastException(activity.toString()+"must implement OnArticleSelectedListener");
        }
    }
    public void initViews() {
        wheel1 = (TuneWheel) view.findViewById(R.id.scale_mark1);
        wheel2 = (TuneWheel) view.findViewById(R.id.scale_mark2);
        wheel3 = (TuneWheel) view.findViewById(R.id.scale_mark3);
        tv_ssy = (TextView) view.findViewById(R.id.tv_ssy_num);
        tv_szy = (TextView) view.findViewById(R.id.tv_szy_num);
        tv_mb = (TextView) view.findViewById(R.id.tv_mb_num);
        rlchecktime = (RelativeLayout) view.findViewById(R.id.rl_blood_time);
        tvcltime = (TextView) view.findViewById(R.id.tv_cl_time);
        btnsave = (Button) view.findViewById(R.id.btn_savepress);
        imagepress=(ImageView) view.findViewById(R.id.imagepress);
        tvlv=(TextView) view.findViewById(R.id.tv_lv);
        tv_ssy.setText((int) wheel1.getValue() + "");
        tv_szy.setText((int) wheel2.getValue() + "");
        tv_mb.setText((int) wheel3.getValue() + "");
        ssy = (int) wheel1.getValue();
        szy = (int) wheel2.getValue();
        mb = (int) wheel3.getValue();
        tvlv.setText("请滑动刻度尺");
        tvlv.setTextColor(Color.parseColor("#53bbb3"));
        progressBar1 = (VerticalProgressBar) view.findViewById(R.id.progressBar1);
        progressBar2= (VerticalProgressBar)view.findViewById(R.id.progressBar2);

        getBloodPress();
    }

    public void initEvent() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        tvcltime.setText(df.format(new Date()));
        chtime = df.format(new Date());

        wheel1.setValueChangeListener(new TuneWheel.OnValueChangeListener() {
            @Override
            public void onValueChange(float value) {
                ssy = (int) value;
                pbvalue=  (int) (((float) ssy / (float) 300) * 100);

                progressBar1.setProgress(pbvalue);

                tv_ssy.setText(ssy + "");
                changeTextColor(tv_ssy,1);
                changeImgeView(imagepress,tvlv,ssy,szy);
            }
        });
        wheel2.setValueChangeListener(new TuneWheel.OnValueChangeListener() {
            @Override
            public void onValueChange(float value) {
                szy = (int) value;
                tv_szy.setText(szy + "");
                changeTextColor(tv_szy,2);
                changeImgeView(imagepress,tvlv,ssy,szy);
                pbvalue2= (int) (((float) szy / (float) 300) * 100);
                progressBar2.setProgress(pbvalue2);
            }
        });
        wheel3.setValueChangeListener(new TuneWheel.OnValueChangeListener() {
            @Override
            public void onValueChange(float value) {
                mb = (int) value;
                tv_mb.setText(mb + "");
                changeImgeView(imagepress,tvlv,ssy,szy);
            }
        });
        rlchecktime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intenttime = new Intent(getActivity(), SelectDateAndTime.class);
                startActivityForResult(intenttime, Constants.ADDTTIME);

            }
        });
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CommonUtils.isFastClick()) return;
                if(tvlv.getText().equals("请滑动刻度尺")){
                    ToastUtils.showMessage(getActivity(),"请滑动刻度尺");
                    return;
                }
                btnsave.setEnabled(false);
                requestMaker.BloodPressureInsert(userid, ssy + "", szy + "", mb + "", chtime, new JsonAsyncTask_Info(
                        getActivity(), true, new JsonAsyncTaskOnComplete() {
                    public void processJsonObject(Object result) {
                        String value = result.toString();
                        btnsave.setEnabled(true);
                        try {
                            JSONObject mySO = (JSONObject) result;
                            JSONArray array = mySO.getJSONArray("BloodPressureInsert");
                            if (array.getJSONObject(0).getString("MessageCode")
                                    .equals("0")) {
                                EventBus.getDefault().post(new RefreshEvent(getResources().getInteger(R.integer.refresh_manager_data)));
                                ToastUtils.showMessage(getActivity(), array.getJSONObject(0).getString("MessageContent"));
                                if(p==-1){
                                    EventBus.getDefault().post(new RefreshEvent(getResources().getInteger(R.integer.refresh_press)));
                                    getActivity().finish();
                                }else {
                                    if (p <= 2) {
                                        mListener.selectItem(p + 1);

                                    } else {
                                        Intent intentD = new Intent();
                                        intentD.setAction("action.DateOrFile");
                                        intentD.putExtra("msgContent", "Date");
                                        getActivity().sendBroadcast(intentD);

                                        EventBus.getDefault().post(new RefreshEvent(getResources().getInteger(R.integer.refresh_manager_data)));
                                        getActivity().finish();
                                    }
                                }
                            } else {
                                ToastUtils.showMessage(getActivity(), array.getJSONObject(0).getString("MessageContent"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }));
            }
        });

    }
    private void getBloodPress(){
        requestMaker.HealthDataInquirywWithPageType(userid,1+"","1","BloodPressure",new JsonAsyncTask_Info(getActivity(), true, new JsonAsyncTaskOnComplete(){
            @Override
            public void processJsonObject(Object result) {

                try {
                    JSONObject mySO = (JSONObject) result;
                    String resultValue=result.toString();
                    JSONArray array = mySO
                            .getJSONArray("HealthDataInquiryWithPage");
                    if (array.getJSONObject(0).has("MessageCode")){
                        ssy=120;
                        szy=80;
                        pbvalue=  (int) (((float) ssy / (float) 300) * 100);
                        progressBar1.setProgress(pbvalue);
                        pbvalue2= (int) (((float) szy / (float) 300) * 100);
                        progressBar2.setProgress(pbvalue2);
                    }else{

                        HealteData date = JsonTools.getData(result.toString(), HealteData.class);
                        List<HealthDataRes> list = date.getData();
                        wheel1.initViewParam(Integer.valueOf(list.get(0).getValue1()),300,10);
                        wheel2.initViewParam(Integer.valueOf(list.get(0).getValue2()),300,10);
                        wheel3.initViewParam(Integer.valueOf(list.get(0).getValue3()),300,10);
                        ssy = Integer.valueOf(list.get(0).getValue1());

                        pbvalue=  (int) (((float) ssy / (float) 300) * 100);

                        progressBar1.setProgress(pbvalue);
                        pbvalue2= (int) (((float) szy / (float) 300) * 100);
                        progressBar2.setProgress(pbvalue2);
                        tv_ssy.setText(ssy + "");
                        changeTextColor(tv_ssy,1);
                        szy = Integer.valueOf(list.get(0).getValue2());
                        tv_szy.setText(szy + "");
                        changeTextColor(tv_szy,2);
                        mb =Integer.valueOf(list.get(0).getValue3());
                        tv_mb.setText(mb + "");
                        changeImgeView(imagepress,tvlv,ssy,szy);
                    }

                } catch (Exception e) {
                    e.printStackTrace();

                }

            }
        }));
    }


    public static Fragment getInstance() {
        return new BloodPressureFragment();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Constants.REQUEST_CALENDAR && data != null) {
            String time = data.getStringExtra("time");
            if (!TextUtils.isEmpty(time)) {
                tvcltime.setText(time);
                chtime = time;
            }

        }
    }
    private void changeTextColor(TextView textView,int type){
        int value=Integer.valueOf(textView.getText().toString());
        if(type==1){
            if(value<140){
                textView.setTextColor(Color.parseColor("#53bbb3"));
            }else if(value>=140&&value<160){
                textView.setTextColor(Color.parseColor("#fb7701"));
            }else if(value>=160&&value<180){

                textView.setTextColor(Color.parseColor("#fa3b00"));
            }else{

                textView.setTextColor(Color.parseColor("#ea0b35"));
            }
        }else if(type==2){
            if(value<90){
                textView.setTextColor(Color.parseColor("#53bbb3"));
            }else if(value>=90&&value<100){
                textView.setTextColor(Color.parseColor("#fb7701"));
            }else if(value>=100&&value<110){
                textView.setTextColor(Color.parseColor("#fa3b00"));
            }else{
                textView.setTextColor(Color.parseColor("#ea0b35"));
            }

        }

    }
    private void changeImgeView(ImageView iv,TextView tv,int ssz,int szy){
        int lvssz= CommonUtils.computeSsz(ssz);
        int lvszy= CommonUtils.computeSzy(szy);
        int lv=CommonUtils.MaxInt(lvssz,lvszy);
        switch (lv){
            case 1:
                iv.setImageResource(R.drawable.pic_circle_g_b);
                tv.setText("血压正常");
                break;
            case 2:
                iv.setImageResource(R.drawable.pic_circle_g_y);
                tv.setText("高血压一期");
                break;
            case 3:
                iv.setImageResource(R.drawable.pic_circle_org_b);
                tv.setText("高血压二期");
                break;
            case 4:
                iv.setImageResource(R.drawable.pic_circle_o_r);
                tv.setText("高血压三期");
                break;

        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void lazyLoad() {

    }
}
