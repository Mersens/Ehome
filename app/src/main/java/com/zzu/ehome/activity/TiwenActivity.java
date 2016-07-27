package com.zzu.ehome.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.zzu.ehome.fragment.TempFragment;
import com.zzu.ehome.utils.OnSelectItemListener;

/**
 * Created by Mersens on 2016/6/27.
 */
public class TiwenActivity extends SingleFragmentActivity implements OnSelectItemListener {
    @Override
    public Fragment creatFragment() {
        return new TempFragment();
    }

    @Override
    public String getHTitle() {
        return "体温";
    }

    @Override
    public Style getStyle() {
        return Style.LEFTWITHTITLE;
    }
    @Override
    public void selectItem(int pos) {

    }

}
