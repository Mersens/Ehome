package com.zzu.ehome.activity;

import android.support.v4.app.Fragment;

import com.zzu.ehome.fragment.NewTemp;
import com.zzu.ehome.fragment.TempFragment;

/**
 * Created by Mersens on 2016/6/27.
 */
public class TiwenDataActivity extends SingleFragmentActivity {
    @Override
    public Fragment creatFragment() {
        return NewTemp.getInstance();
    }

    @Override
    public String getHTitle() {
        return "体温";
    }

    @Override
    public Style getStyle() {
        return Style.DEFAULT;
    }

}
