package com.zzu.ehome.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.zzu.ehome.fragment.HypertensionFragment;
import com.zzu.ehome.fragment.HypertensionWithWebFragment;

import java.util.List;

/**
 * Created by zzu on 2016/4/11.
 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragmentList;
    private List<String> tabTitles;

    public MyFragmentPagerAdapter(FragmentManager fm,List<Fragment> fragmentList,List<String> tabTitles){
        super(fm);
        this.fragmentList=fragmentList;
        this.tabTitles=tabTitles;

    }
    @Override
    public Fragment getItem(int position) {
        if(position==0){
            return HypertensionWithWebFragment.getInstance("https://www.baidu.com/");
        }else{
            return HypertensionFragment.getInstance("http://cn.bing.com/");
        }

    }

    @Override
    public int getCount() {
        return tabTitles.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return tabTitles.get(position % tabTitles.size());
    }
}
