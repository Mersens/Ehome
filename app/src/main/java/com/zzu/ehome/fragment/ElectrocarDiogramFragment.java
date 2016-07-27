package com.zzu.ehome.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zzu.ehome.R;

/**
 * Created by zzu on 2016/4/12.
 * 心电
 */
public class ElectrocarDiogramFragment extends BaseFragment {
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.layout_electrocardiogram,null);
        initViews();
        initEvents();
        return view;
    }

    public void initViews() {

    }

    public void initEvents() {

    }
    @Override
    protected void lazyLoad() {

    }
    public static Fragment getInstance(){
        return new ElectrocarDiogramFragment();
    }
}
