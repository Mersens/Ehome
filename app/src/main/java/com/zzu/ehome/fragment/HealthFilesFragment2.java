package com.zzu.ehome.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zzu.ehome.R;

/**
 * Created by Mersens on 2016/7/27.
 */
public class HealthFilesFragment2 extends BaseFragment {
    private View mView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_healthfiles1,null);
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


    }
    public void initEvent(){}
    public void initDatas(){

    }


    public static Fragment getInstance(){
        return new HealthFilesFragment2();
    }


    @Override
    protected void lazyLoad() {

    }
}
