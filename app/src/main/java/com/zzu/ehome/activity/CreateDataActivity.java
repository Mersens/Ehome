package com.zzu.ehome.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;

import com.umeng.analytics.MobclickAgent;
import com.zzu.ehome.R;
import com.zzu.ehome.adapter.MyFragmentPagerAdapter;
import com.zzu.ehome.application.CustomApplcation;
import com.zzu.ehome.fragment.BloodPressureFragment;
import com.zzu.ehome.fragment.BloodSugarFragment;
import com.zzu.ehome.fragment.BooldFatFragment;
import com.zzu.ehome.fragment.ElectrocarDiogramFragment;
import com.zzu.ehome.fragment.TempFragment;
import com.zzu.ehome.fragment.WeightFragment;
import com.zzu.ehome.utils.OnSelectItemListener;
import com.zzu.ehome.view.HeadView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by zzu on 2016/4/11.
 */
public class CreateDataActivity extends BaseActivity implements OnSelectItemListener {
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private List<Fragment> fragmentList;
    private List<String> tabTitles;
    private MyFragmentPagerAdapter mAdapter;
    private final String mPageName = "CreateDataActivity";

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_create_data);
        initViews();
        initEvent();
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

    public void initViews() {
        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "新建数据", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();
                Intent intentD = new Intent();
                intentD.setAction("action.DateOrFile");
                intentD.putExtra("msgContent", "Date");
                sendBroadcast(intentD);
            }
        });
    }

    public void initEvent() {
        fragmentList = new ArrayList<Fragment>();
        tabTitles = new ArrayList<String>();
        if (CustomApplcation.getInstance().myMap.size() > 0) {
            addMap(CustomApplcation.getInstance().myMap, tabTitles);
        } else {
            fragmentList.add(TempFragment.getInstance());
            fragmentList.add(WeightFragment.getInstance());
            fragmentList.add(BloodPressureFragment.getInstance());
            fragmentList.add(BloodSugarFragment.getInstance());
            tabTitles.add("体温");
            tabTitles.add("体重");
            tabTitles.add("血压");
            tabTitles.add("血糖");
        }
        for (int i = 0; i < tabTitles.size(); i++) {
            setPSort(tabTitles.get(i), i);
        }

//        fragmentList.add(BooldFatFragment.getInstance());
//        fragmentList.add(ElectrocarDiogramFragment.getInstance());

//        tabTitles.add("血脂");
//        tabTitles.add("心电");
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.addTab(mTabLayout.newTab().setText(tabTitles.get(0)));
        mTabLayout.addTab(mTabLayout.newTab().setText(tabTitles.get(1)));
        mTabLayout.addTab(mTabLayout.newTab().setText(tabTitles.get(2)));
        mTabLayout.addTab(mTabLayout.newTab().setText(tabTitles.get(3)));
//        mTabLayout.addTab(mTabLayout.newTab().setText(tabTitles.get(4)));
//        mTabLayout.addTab(mTabLayout.newTab().setText(tabTitles.get(5)));
        mAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList, tabTitles);
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void setPSort(String s, int i) {
        if (s.equals("血压")) {
            BloodPressureFragment.p = i;
        } else if (s.equals("体重")) {
            WeightFragment.p = i;
        } else if (s.equals("体温")) {
            TempFragment.p = i;
        } else {
            BloodSugarFragment.p = i;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            finishActivity();
            Intent intentD = new Intent();
            intentD.setAction("action.DateOrFile");
            intentD.putExtra("msgContent", "Date");
            sendBroadcast(intentD);

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void selectItem(int pos) {
        mViewPager.setCurrentItem(pos);
    }

    private void addMap(Map map, List<String> titles) {

        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            if (entry.getKey().equals("precount")) {
                fragmentList.add(BloodPressureFragment.getInstance());
                titles.add("血压");
            } else if (entry.getKey().equals("weightcount")) {
                fragmentList.add(WeightFragment.getInstance());
                titles.add("体重");
            } else if (entry.getKey().equals("tempcount")) {
                fragmentList.add(TempFragment.getInstance());
                titles.add("体温");
            } else {
                fragmentList.add(BloodSugarFragment.getInstance());
                titles.add("血糖");
            }
//            entry.getKey() + ":" + entry.getValue());
        }

    }
}
