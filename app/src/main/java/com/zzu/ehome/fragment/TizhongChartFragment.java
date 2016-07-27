package com.zzu.ehome.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zzu.ehome.R;

/**
 * Created by Administrator on 2016/5/16.
 */
public class TizhongChartFragment extends  BaseFragment {
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.layout_tizhongchart,null);
        return view;
    }

    @Override
    protected void lazyLoad() {

    }
}
