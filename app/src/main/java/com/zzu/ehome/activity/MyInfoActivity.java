package com.zzu.ehome.activity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.umeng.analytics.MobclickAgent;
import com.zzu.ehome.R;
import com.zzu.ehome.adapter.EvaluateAdapter;
import com.zzu.ehome.adapter.MyInfoAdapter;
import com.zzu.ehome.view.HeadView;

import java.util.ArrayList;
import java.util.List;

public class MyInfoActivity extends BaseActivity {
    private ListView mListView;
    private final String mPageName = "MyInfoActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);
        initViews();

    }
    private void initViews() {

        mListView=(ListView)findViewById(R.id.mlistview);
        setDefaultTXViewMethod(R.mipmap.icon_arrow_left, " 我的消息", "", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finish();

            }
        }, new HeadView.OnRightClickListener() {
            @Override
            public void onClick() {

            }
        });
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < 10; i++) {
            list.add(i + "");
        }
        mListView.setAdapter(new MyInfoAdapter(MyInfoActivity.this, list));

    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(mPageName);
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(mPageName);
        MobclickAgent.onPause(this);
    }


}
