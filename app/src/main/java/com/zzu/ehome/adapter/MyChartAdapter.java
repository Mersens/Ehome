package com.zzu.ehome.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Administrator on 2016/5/16.
 */
public class MyChartAdapter extends FragmentPagerAdapter {
    private List<Fragment> mList;
    public MyChartAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.mList=list;
    }

    @Override
    public Fragment getItem(int idx) {
        return mList.get(idx);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
