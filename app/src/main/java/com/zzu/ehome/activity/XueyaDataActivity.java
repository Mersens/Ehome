package com.zzu.ehome.activity;

import android.support.v4.app.Fragment;

import com.zzu.ehome.fragment.BloodPressureFragment;
import com.zzu.ehome.fragment.NewPressFragment;

/**
 * Created by Mersens on 2016/6/27.
 */
public class XueyaDataActivity extends SingleFragmentActivity {
    @Override
    public Fragment creatFragment() {
        return NewPressFragment.getInstance();
    }

    @Override
    public String getHTitle() {
        return "血压";
    }

    @Override
    public Style getStyle() {
        return Style.DEFAULT;
    }
}
