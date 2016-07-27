package com.zzu.ehome.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.zzu.ehome.R;
import com.zzu.ehome.activity.HealthFilesActivity;
import com.zzu.ehome.view.HeadView;
import com.zzu.ehome.view.PullToRefreshLayout;
import com.zzu.ehome.view.PullableListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mersens on 2016/7/26.
 */
public class HomeFragment1 extends BaseFragment implements View.OnClickListener{
    private View mView;
    private ViewPager mViewPager;
    private ListView mListView;
    private PullToRefreshLayout pulltorefreshlayout;
    private LinearLayout layout_health_files;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_home1,null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView=view;
        initViews();
        initEvent();
        initDatas();
    }

    public void initViews(){

        layout_health_files=(LinearLayout) mView.findViewById(R.id.layout_health_files);
        mViewPager=(ViewPager) mView.findViewById(R.id.viewPager);
        mListView=(ListView) mView.findViewById(R.id.listView);
        pulltorefreshlayout=(PullToRefreshLayout)mView.findViewById(R.id.refresh_view);

    }


    public void initEvent(){
        layout_health_files.setOnClickListener(this);

        pulltorefreshlayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                // 下拉刷新操作
                new Handler()
                {
                    @Override
                    public void handleMessage(Message msg)
                    {
                        pulltorefreshlayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                    }
                }.sendEmptyMessageDelayed(0, 3000);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                new Handler()
                {
                    @Override
                    public void handleMessage(Message msg)
                    {
                        pulltorefreshlayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    }
                }.sendEmptyMessageDelayed(0, 3000);
            }
        });

    }



    public void initDatas(){
        mViewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return ImageFragment.getInstance();
            }

            @Override
            public int getCount() {
                return 3;
            }
        });

        final List<Integer> mList=new ArrayList<>();
        for(int i=0;i<10;i++){
            mList.add(i);
        }
        mListView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return mList.size();
            }

            @Override
            public Object getItem(int position) {
                return mList.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View mItemView=LayoutInflater.from(getActivity()).inflate(R.layout.home_item,null);
                return mItemView;
            }
        });
    }
    @Override
    protected void lazyLoad() {

    }

    public Fragment getInstance(){
        return new HomeFragment1();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_health_files:
                startIntent(getActivity(),HealthFilesActivity.class);
                break;
        }

    }

    public <T> void startIntent(Activity context, Class<T> cls) {
        Intent intent = new Intent(context, cls);
        startActivity(intent);
    }




}
