package com.zzu.ehome.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.zzu.ehome.fragment.BloodPressureFragment;
import com.zzu.ehome.fragment.WeightFragment;
import com.zzu.ehome.utils.OnSelectItemListener;

/**
 * Created by Mersens on 2016/6/27.
 */
public class XueyaActivity extends SingleFragmentActivity implements OnSelectItemListener {
    @Override
    public Fragment creatFragment() {
        return new BloodPressureFragment();
    }

    @Override
    public String getHTitle() {
        return "血压";
    }

    @Override
    public Style getStyle() {
        return Style.LEFTWITHTITLE;
    }
    @Override
    public void selectItem(int pos) {

    }
}
