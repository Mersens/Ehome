package com.zzu.ehome.activity;

import android.support.v4.app.Fragment;

import com.zzu.ehome.fragment.BloodSugarFragment;
import com.zzu.ehome.fragment.NewSuggarFrament;

/**
 * Created by Mersens on 2016/6/27.
 */
public class XuetangDataActivity extends SingleFragmentActivity {
    @Override
    public Fragment creatFragment() {
        return NewSuggarFrament.getInstance();
    }

    @Override
    public String getHTitle() {
        return "血糖";
    }

    @Override
    public Style getStyle() {
        return Style.DEFAULT;
    }

}
