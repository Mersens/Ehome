package com.zzu.ehome.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.zzu.ehome.R;
import com.zzu.ehome.utils.ToastUtils;
import com.zzu.ehome.view.HeadView;

/**
 * Created by Mersens on 2016/6/27.
 */
public abstract class SingleFragmentActivity extends BaseActivity {
    public abstract Fragment creatFragment();

    public abstract String getHTitle();

    public abstract Style getStyle();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_single);
        init();
    }

    public void init() {
        initHeadView();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, creatFragment()).commit();
    }

    public void initHeadView() {
        switch (getStyle()) {
            case LEFTWITHTITLE:
                setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, getHTitle(), new HeadView.OnLeftClickListener() {
                    @Override
                    public void onClick() {
                        finishActivity();
                    }
                });
                break;
            case DEFAULT:
                setDefaultViewMethod(R.mipmap.icon_arrow_left, getHTitle(), R.mipmap.icon_add_zoushi, new HeadView.OnLeftClickListener() {
                    @Override
                    public void onClick() {
                        finishActivity();
                    }
                }, new HeadView.OnRightClickListener() {
                    @Override
                    public void onClick() {
                        doAdd();
                    }
                });
                break;
        }
    }

    public void doAdd() {
        String title = getHTitle();
        if ("体温".equals(title)) {
            intentAction(SingleFragmentActivity.this, TiwenActivity.class);
        } else if ("体重".equals(title)) {
            intentAction(SingleFragmentActivity.this, TizhongActivity.class);
        } else if ("血压".equals(title)) {
            intentAction(SingleFragmentActivity.this, XueyaActivity.class);
        } else if ("血糖".equals(title)) {
            intentAction(SingleFragmentActivity.this, XuetangActivity.class);
        } else if("用药记录".equals(title)){
            intentAction(SingleFragmentActivity.this, YYJLActivity.class);
        }
    }

    public <T> void intentAction(Activity context, Class<T> cls) {
        Intent intent = new Intent(context, cls);
        startActivity(intent);
    }

    public enum Style {
        LEFTWITHTITLE, DEFAULT;
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        super.onActivityResult(arg0, arg1, arg2);
    }
}
