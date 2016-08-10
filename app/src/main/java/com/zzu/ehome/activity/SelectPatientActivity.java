package com.zzu.ehome.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zzu.ehome.R;
import com.zzu.ehome.adapter.SelectPatientAdapter;
import com.zzu.ehome.view.DialogTips;
import com.zzu.ehome.view.HeadView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mersens on 2016/8/9.
 */
public class SelectPatientActivity extends BaseActivity {
    private ListView listView;
    private SelectPatientAdapter adapter;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.layout_doctor_list);
        initViews();
        initEvent();
        initDatas();
    }

    public void initViews() {
        listView = (ListView) findViewById(R.id.listView);
        setDefaultTXViewMethod(R.mipmap.icon_arrow_left, "选择就诊人", "添加", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();

            }
        }, new HeadView.OnRightClickListener() {
            @Override
            public void onClick() {
                startActivity(new Intent(SelectPatientActivity.this, AddUserInfoActivity.class));
            }
        });

    }

    public void initEvent() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    confirmExit();
                } else if (position == 1) {
                    startActivity(new Intent(SelectPatientActivity.this, ConfirmMsgActivity.class));
                }
            }
        });
    }

    public void initDatas() {
        List<String> mList = new ArrayList<>();
        mList.add("张三（用户本人）");
        mList.add("张少龙（家人）");
        mList.add("卢宏（亲友）");
        adapter = new SelectPatientAdapter(this, mList);
        listView.setAdapter(adapter);

    }

    public void confirmExit() {
        DialogTips dialog = new DialogTips(SelectPatientActivity.this, "", "就诊人信息缺失，请完善信息！",
                "确定", true, true);
        dialog.SetOnSuccessListener(new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int userId) {
                startActivity(new Intent(SelectPatientActivity.this, ComplateUserInfoActivity.class));
            }
        });

        dialog.show();
        dialog = null;
    }
}
