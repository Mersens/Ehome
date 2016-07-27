package com.zzu.ehome.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.zzu.ehome.fragment.TempFragment;
import com.zzu.ehome.fragment.WeightFragment;
import com.zzu.ehome.utils.OnSelectItemListener;

/**
 * Created by Mersens on 2016/6/27.
 */
public class TizhongActivity extends SingleFragmentActivity implements OnSelectItemListener {
    @Override
    public Fragment creatFragment() {
        return new WeightFragment();
    }

    @Override
    public String getHTitle() {
        return "体重";
    }

    @Override
    public Style getStyle() {
        return Style.LEFTWITHTITLE;
    }

    public void selectItem(int pos) {

    }
}
