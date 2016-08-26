package com.zzu.ehome.main.ehome;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.igexin.sdk.PushManager;
import com.umeng.analytics.MobclickAgent;
import com.zzu.ehome.R;
import com.zzu.ehome.activity.BaseSimpleActivity;
import com.zzu.ehome.activity.PrivateDoctorFragment;
import com.zzu.ehome.application.CustomApplcation;
import com.zzu.ehome.bean.RefreshEvent;
import com.zzu.ehome.bean.StepBean;
import com.zzu.ehome.db.EHomeDao;
import com.zzu.ehome.db.EHomeDaoImpl;
import com.zzu.ehome.fragment.DoctorFragment;
import com.zzu.ehome.fragment.HealthDataFragment;
import com.zzu.ehome.fragment.HealthFragment;
import com.zzu.ehome.fragment.HomeFragment1;
import com.zzu.ehome.fragment.UserCenterFragment;
import com.zzu.ehome.service.StepDetector;
import com.zzu.ehome.service.StepService;
import com.zzu.ehome.utils.DateUtils;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.view.DialogTips;

import java.util.List;

import de.greenrobot.event.EventBus;

public class MainActivity extends BaseSimpleActivity implements View.OnClickListener{
    private final String mPageName = "MainActivity";

    private Fragment[] fragments;
    private int index;
    private int currentTabIndex;
    private Fragment mHomeFragment, mHealthFragment, mDoctorFragment,mInfoFragment,mPrivateDoctorFragment;

    private ImageView img_home;
    private ImageView img_health;
    private ImageView img_doctor;
    private ImageView img_info;
    private ImageView img_private_doctor;

    private TextView tv_home;
    private TextView tv_health;
    private TextView tv_doctor;
    private TextView tv_info;
    private TextView tv_private_doctor;

    private RelativeLayout layout_home;
    private RelativeLayout layout_health;
    private RelativeLayout layout_doctor;
    private RelativeLayout layout_info;
    private RelativeLayout layout_private_doctor;
    private RelativeLayout[] mTabs;
    private EHomeDao dao;
    private String userid;
    private Intent intent;
    private int selectColor;
    private int unSelectColor;
    private String file="";
    private BroadcastReceiver mDateOrFileBroadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            if(action.equals("action.DateOrFile")){
                String msg=intent.getStringExtra("msgGua");
                if(msg!=null&&msg.equals("Gua")){
                    index = 2;
                    setTab(2);
                    selectCommitItem(index);

                }else if(intent.getStringExtra("msgContent")!=null&&intent.getStringExtra("msgContent").equals("Date")){
                    index=1;
                    setTab(1);
                    selectCommitDateItem(index);
//                    EventBus.getDefault().post(new RefreshEvent(getResources().getInteger(R.integer.refresh_manager_data)));
                }else if(intent.getStringExtra("msgContent")!=null&&intent.getStringExtra("msgContent").equals("File")){
                    file=intent.getStringExtra("msgContent");
                    index=1;
                    setTab(1);
                    selectCommitItem(index);
//                    EventBus.getDefault().post(new RefreshEvent(getResources().getInteger(R.integer.refresh_manager_file)));
                }
                else{
                    index = 1;
                    setTab(1);
                    selectCommitItem(index);

                }

            }

        }
    };

    private void selectCommitDateItem(int index) {
        if (currentTabIndex != index) {
            FragmentTransaction trx = getSupportFragmentManager()
                    .beginTransaction();
            trx.hide(fragments[currentTabIndex]);
            if (!fragments[index].isAdded()) {
                trx.add(R.id.fragment_container, fragments[index]);
            }
            trx.show(fragments[index]).commitAllowingStateLoss();
        }
        mTabs[currentTabIndex].setSelected(false);
        mTabs[index].setSelected(true);
        currentTabIndex = index;

        if(index==1){

//                    EventBus.getDefault().post(new RefreshEvent(getResources().getInteger(R.integer.change_date)));

            ((HealthFragment)mHealthFragment).addFragment(HealthFragment.Style.DATA);
            Handler handler=new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    EventBus.getDefault().post(new RefreshEvent(getResources().getInteger(R.integer.refresh_manager_data)));
                }
            },1000);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(mPageName);
        MobclickAgent.onResume(this);
    }


    public void setStepData(){
        String time = DateUtils.getTodayTime();
        StepBean step = dao.loadSteps(userid, time.substring(0, 10));
        if (step != null) {
            StepDetector.CURRENT_SETP = step.getNum();
        }
    }



    public void saveStepData(){
        String time = DateUtils.getTodayTime();
        StepBean step = dao.loadSteps(userid, time.substring(0, 10));
        if (step != null) {
            // StepDetector.CURRENT_SETP = step.getNum();
            step.setNum(StepDetector.CURRENT_SETP);
            step.setStartTime(time);
            dao.updateStep(step);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(mPageName);
        MobclickAgent.onPause(this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intent = new Intent(this, StepService.class);
        startService(intent);
        dao = new EHomeDaoImpl(this);
        userid = SharePreferenceUtil.getInstance(this).getUserId();
        PushManager.getInstance().initialize(this.getApplicationContext());
        initViews();
        initEvent();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("action.DateOrFile");
        registerReceiver(mDateOrFileBroadcastReceiver, intentFilter);
        if(null!=savedInstanceState){
        resetFragmet(savedInstanceState);
        }
        setTab(0);
        setStepData();

    }
    private void resetFragmet(Bundle savedInstanceState){
        index=savedInstanceState.getInt("index",0);
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        for(Fragment ft:fragmentList){
            if(ft instanceof HomeFragment1){
                mHomeFragment=ft;
            }else if(ft instanceof HealthDataFragment){
                mHealthFragment=ft;
            }else if(ft instanceof DoctorFragment){
                mDoctorFragment=ft;
            }else if(ft instanceof UserCenterFragment ){
                mInfoFragment=ft;
            }else if(ft instanceof PrivateDoctorFragment){
                mPrivateDoctorFragment=ft;
            }
        }
        getSupportFragmentManager().beginTransaction()
                .hide(mHomeFragment)
                .hide(mHealthFragment)
                .hide(mDoctorFragment)
                .hide(mInfoFragment)
                .hide(mPrivateDoctorFragment)
                .show(fragments[index])
                .commit();
    }

    private void initViews() {
        selectColor=getResources().getColor(R.color.bottom_text_color_pressed);
        unSelectColor=getResources().getColor(R.color.bottom_text_color_normal);

        img_home = (ImageView) findViewById(R.id.image_home);
        img_health = (ImageView) findViewById(R.id.img_health);
        img_doctor = (ImageView) findViewById(R.id.img_doctor);
        img_info = (ImageView) findViewById(R.id.img_info);
        img_private_doctor=(ImageView)findViewById(R.id.img_private_doctor);

        tv_home=(TextView) findViewById(R.id.tv_home);
        tv_health=(TextView) findViewById(R.id.tv_health);
        tv_doctor=(TextView) findViewById(R.id.tv_doctor);
        tv_info=(TextView) findViewById(R.id.tv_info);
        tv_private_doctor=(TextView)findViewById(R.id.tv_private_doctor);

        layout_private_doctor=(RelativeLayout)findViewById(R.id.layout_private_doctor);
        layout_home = (RelativeLayout) findViewById(R.id.layout_home);
        layout_health = (RelativeLayout) findViewById(R.id.layout_health);
        layout_doctor = (RelativeLayout) findViewById(R.id.layout_doctor);
        layout_info = (RelativeLayout) findViewById(R.id.layout_info);

        mTabs = new RelativeLayout[5];
        mTabs[0] = layout_home;
        mTabs[1] = layout_health;
        mTabs[2] = layout_private_doctor;
        mTabs[3] = layout_doctor;
        mTabs[4] = layout_info;

//       mHomeFragment = HomeFragment.getInstance();
        mHomeFragment = new HomeFragment1();
        mHealthFragment = HealthDataFragment.getInstance();
        mDoctorFragment= DoctorFragment.getInstance();
        mInfoFragment = UserCenterFragment.getInstance();
        mPrivateDoctorFragment=PrivateDoctorFragment.getInstance();
        fragments = new Fragment[]{mHomeFragment, mHealthFragment, mPrivateDoctorFragment,mDoctorFragment,mInfoFragment};
    }

    public void initEvent() {
        layout_home.setOnClickListener(this);
        layout_health.setOnClickListener(this);
        layout_doctor.setOnClickListener(this);
        layout_info.setOnClickListener(this);
        layout_private_doctor.setOnClickListener(this);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, fragments[0])
                .add(R.id.fragment_container, fragments[1])
                .add(R.id.fragment_container, fragments[2])
                .add(R.id.fragment_container, fragments[3])
                .add(R.id.fragment_container, fragments[4])
                .hide(fragments[1]) .hide(fragments[2]).hide(fragments[4])
                .hide(fragments[3])
                .show(fragments[0])
                .commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_home:
                index = 0;
                setTab(0);
                EventBus.getDefault().post(new RefreshEvent(getResources().getInteger(R.integer.refresh_info)));
                break;
            case R.id.layout_health:
                index = 1;
                setTab(1);
                break;

            case R.id.layout_private_doctor:
                index = 2;
                setTab(2);
                break;

            case R.id.layout_doctor:
                index = 3;
                setTab(3);

                break;
            case R.id.layout_info:
                index = 4;
                setTab(4);
                break;

        }
        selectItem(index);
    }
    private void selectItem(int index){

        if (currentTabIndex != index) {
            FragmentTransaction trx = getSupportFragmentManager()
                    .beginTransaction();
            trx.hide(fragments[currentTabIndex]);
            if (!fragments[index].isAdded()) {
                trx.add(R.id.fragment_container, fragments[index]);
            }
            trx.show(fragments[index]).commit();
        }
        mTabs[currentTabIndex].setSelected(false);
        mTabs[index].setSelected(true);
        currentTabIndex = index;
    }
    private void selectCommitItem(int index){
        if (currentTabIndex != index) {
            FragmentTransaction trx = getSupportFragmentManager()
                    .beginTransaction();
            trx.hide(fragments[currentTabIndex]);
            if (!fragments[index].isAdded()) {
                trx.add(R.id.fragment_container, fragments[index]);
            }
            trx.show(fragments[index]).commitAllowingStateLoss();
        }
        mTabs[currentTabIndex].setSelected(false);
        mTabs[index].setSelected(true);
        currentTabIndex = index;
        if(index==1){

           ((HealthFragment)mHealthFragment).addBrodFragment(HealthFragment.Style.FILES,1);
            Handler handler=new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    EventBus.getDefault().post(new RefreshEvent(getResources().getInteger(R.integer.refresh_manager_file)));
                }
            },1000);


        }
    }

    private void setTab(int i) {
        resetImgs();
        switch (i) {
            case 0:
                img_home.setImageResource(R.mipmap.icon_home_pressed);
                tv_home.setTextColor(selectColor);
                EventBus.getDefault().post(new RefreshEvent(getResources().getInteger(R.integer.refresh_info)));
                break;
            case 1:
                img_health.setImageResource(R.mipmap.icon_jiankang_pressed);
                tv_health.setTextColor(selectColor);
                EventBus.getDefault().post(new RefreshEvent(getResources().getInteger(R.integer.refresh_manager)));
                break;
            case 2:
                img_private_doctor.setImageResource(R.mipmap.icon_private_doctor_pressed);
                tv_private_doctor.setTextColor(selectColor);
                break;
            case 3:
                img_doctor.setImageResource(R.mipmap.icon_msg_pressed);
                tv_doctor.setTextColor(selectColor);
                break;
            case 4:
                img_info.setImageResource(R.mipmap.icon_myinfo_pressed);
                tv_info.setTextColor(selectColor);
                break;
        }
    }

    private void resetImgs() {
        tv_home.setTextColor(unSelectColor);
        tv_health.setTextColor(unSelectColor);
        tv_doctor.setTextColor(unSelectColor);
        tv_info.setTextColor(unSelectColor);
        tv_private_doctor.setTextColor(unSelectColor);
        img_home.setImageResource(R.mipmap.icon_home_normal);
        img_health.setImageResource(R.mipmap.icon_jiankang_normal);
        img_doctor.setImageResource(R.mipmap.icon_msg_normal);
        img_info.setImageResource(R.mipmap.icon_myinfo_normal);
        img_private_doctor.setImageResource(R.mipmap.icon_private_doctor_normal);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            this.confirmExit();
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    public void confirmExit() {
        DialogTips dialog = new DialogTips(MainActivity.this, "", "是否退出软件？",
                "确定", true, true);
        dialog.SetOnSuccessListener(new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int userId) {
                CustomApplcation.getInstance().exit();
                finish();
            }
        });

        dialog.show();
        dialog = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveStepData();
        try {
            unregisterReceiver(mDateOrFileBroadcastReceiver);
            mDateOrFileBroadcastReceiver = null;
        } catch (Exception e) {
        }

    }



    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        resetFragmet(savedInstanceState);
        setTab(index);
        super.onRestoreInstanceState(savedInstanceState);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("index",index);
        String inde=index+"";
        super.onSaveInstanceState(outState);
    }
}
