package com.zzu.ehome.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.zzu.ehome.R;
import com.zzu.ehome.adapter.MyFragmentPagerAdapter;
import com.zzu.ehome.view.HeadView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mersens on 2016/8/29.
 * 高血压
 */
public class HypertensionActivity extends BaseActivity {
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private List<Fragment> fragmentList;
    private List<String> tabTitles;
    private MyFragmentPagerAdapter mAdapter;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_create_data);
        initViews();
        initEvent();
        initDatas();

    }

    public void initViews(){
        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "高血压", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();
            }
        });

    }

    public void initEvent(){

    }

    public void initDatas(){
        fragmentList = new ArrayList<Fragment>();
        tabTitles = new ArrayList<String>();
        tabTitles.add("概述");
        tabTitles.add("病因");
        tabTitles.add("症状");
        tabTitles.add("诊断");
        tabTitles.add("治疗");
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.addTab(mTabLayout.newTab().setText(tabTitles.get(0)));
        mTabLayout.addTab(mTabLayout.newTab().setText(tabTitles.get(1)));
        mTabLayout.addTab(mTabLayout.newTab().setText(tabTitles.get(2)));
        mTabLayout.addTab(mTabLayout.newTab().setText(tabTitles.get(3)));
        mTabLayout.addTab(mTabLayout.newTab().setText(tabTitles.get(4)));
        mAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList, tabTitles);
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }
}
