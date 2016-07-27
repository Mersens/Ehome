package com.zzu.ehome.activity;

import android.support.v4.app.Fragment;

import com.zzu.ehome.fragment.NewWeight;
import com.zzu.ehome.fragment.WeightFragment;

/**
 * Created by Mersens on 2016/6/27.
 */
public class TizhongDataActivity extends SingleFragmentActivity {
    @Override
    public Fragment creatFragment() {
        return NewWeight.getInstance();
    }

    @Override
    public String getHTitle() {
        return "体重";
    }

    @Override
    public Style getStyle() {
        return Style.DEFAULT;
    }


}
