package com.zzu.ehome.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.zzu.ehome.R;
import com.zzu.ehome.adapter.MyFragmentPagerAdapter;
import com.zzu.ehome.bean.BusEvent;
import com.zzu.ehome.bean.RefreshEvent;


import com.zzu.ehome.fragment.NewPressFragment;
import com.zzu.ehome.fragment.NewSuggarFrament;
import com.zzu.ehome.fragment.NewTemp;
import com.zzu.ehome.fragment.NewWeight;


import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.view.HeadView;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2016/4/26.
 */
public class DataChatActivity extends BaseActivity {
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private List<Fragment> fragmentList;
    private List<String> tabTitles;
    private Intent mIntent;
    private String date;
    private int tag = -1;
    private View view;
    private final String mPageName = "DataChatActivity";

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        mIntent = this.getIntent();
        setContentView(R.layout.activity_create_data);
        initViews();
        initEvent();
        if (mIntent != null) {
            tag = mIntent.getIntExtra("position", 0);
            mViewPager.setCurrentItem(tag);
//            date=mIntent.getStringExtra("time");
//            if(!TextUtils.isEmpty(date))
//            EventBus.getDefault().post(new BusEvent(date));

        }
    }

    public void initViews() {
        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        setDefaultViewMethod(R.mipmap.icon_arrow_left, "健康走势", R.mipmap.icon_add_zoushi
                , new HeadView.OnLeftClickListener() {
                    @Override
                    public void onClick() {
                        finishActivity();
                    }
                }, new HeadView.OnRightClickListener() {
                    @Override
                    public void onClick() {
//                        doCheck();
//                startActivity(new Intent(DataChatActivity.this,CreateDataActivity.class));
//                finishActivity();
                    }
                });
//        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "健康走势", new HeadView.OnLeftClickListener() {
//            @Override
//            public void onClick() {
//                finishActivity();
//
//            }
//        });
    }

    public void doCheck() {
        switch (tag) {
            case 0:
                intentAction(DataChatActivity.this, TiwenActivity.class);
                break;
            case 1:
                intentAction(DataChatActivity.this, TizhongActivity.class);
                break;
            case 2:
                intentAction(DataChatActivity.this, XueyaActivity.class);
                break;
            case 3:
                intentAction(DataChatActivity.this, XuetangActivity.class);
                break;
        }

    }

    public <T> void intentAction(Activity context, Class<T> cls) {
        Intent intent = new Intent(context, cls);
        startActivity(intent);
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


    public void initEvent() {
        fragmentList = new ArrayList<Fragment>();

//       fragmentList.add(TempChatFragment.getInstance());
        fragmentList.add(NewTemp.getInstance());

//        fragmentList.add(WeightChatFragment.getInstance());
        fragmentList.add(NewWeight.getInstance());

//        fragmentList.add(BloodPressFragment.getInstance());
        fragmentList.add(NewPressFragment.getInstance());
//        fragmentList.add(BloodSuggarFrament.getInstance());
        fragmentList.add(NewSuggarFrament.getInstance());

        tabTitles = new ArrayList<String>();
        tabTitles.add("体温");
        tabTitles.add("体重");
        tabTitles.add("血压");
        tabTitles.add("血糖");

        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.addTab(mTabLayout.newTab().setText(tabTitles.get(0)));
        mTabLayout.addTab(mTabLayout.newTab().setText(tabTitles.get(1)));
        mTabLayout.addTab(mTabLayout.newTab().setText(tabTitles.get(2)));
        mTabLayout.addTab(mTabLayout.newTab().setText(tabTitles.get(3)));

        mViewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList, tabTitles));
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tag = tab.getPosition();
                mViewPager.setCurrentItem(tag);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
