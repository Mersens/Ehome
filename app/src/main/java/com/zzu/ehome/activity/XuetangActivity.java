package com.zzu.ehome.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.zzu.ehome.fragment.BloodPressureFragment;
import com.zzu.ehome.fragment.BloodSugarFragment;
import com.zzu.ehome.utils.OnSelectItemListener;

/**
 * Created by Mersens on 2016/6/27.
 */
public class XuetangActivity extends SingleFragmentActivity implements OnSelectItemListener {
    @Override
    public Fragment creatFragment() {
        return new BloodSugarFragment();
    }

    @Override
    public String getHTitle() {
        return "血糖";
    }

    @Override
    public Style getStyle() {
        return Style.LEFTWITHTITLE;
    }
    @Override
    public void selectItem(int pos) {

    }

}
