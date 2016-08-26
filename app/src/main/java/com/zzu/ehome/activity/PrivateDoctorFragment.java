package com.zzu.ehome.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zzu.ehome.R;
import com.zzu.ehome.fragment.BaseFragment;

/**
 * Created by Mersens on 2016/8/26.
 */
public class PrivateDoctorFragment extends BaseFragment {
    private View mView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_private_doctor,null);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView=view;
    }


    public static Fragment getInstance(){
        return new PrivateDoctorFragment();
    }
    @Override
    protected void lazyLoad() {

    }
}
