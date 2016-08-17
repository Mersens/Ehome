package com.zzu.ehome.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zzu.ehome.R;
import com.zzu.ehome.view.DialogTips;
import com.zzu.ehome.view.HeadView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mersens on 2016/8/17.
 */
public class CooperationPharmacyActivity extends BaseActivity {
    private GridView gridView;
    private RelativeLayout layout_tel;
    private TextView tv_tel;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_cooperation_pharmacy);
        initViews();
        initEvent();
        initDatas();
    }


    public void initViews() {
        gridView = (GridView) findViewById(R.id.gridView);
        layout_tel = (RelativeLayout) findViewById(R.id.layout_tel);
        tv_tel = (TextView) findViewById(R.id.tv_tel);
        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "合作药店", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();
            }
        });
    }

    public void initEvent() {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        layout_tel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTel();

            }
        });
    }

    public void getTel(){
        String tel = tv_tel.getText().toString();
        if(!TextUtils.isEmpty(tel)){
            doTel(tel);
        }
    }


    public void doTel(String tel) {
        DialogTips dialog = new DialogTips(this, "", tel,
                "拨打", true, true);
        dialog.SetOnSuccessListener(new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int userId) {

            }
        });

        dialog.show();
        dialog = null;
    }


    public void initDatas() {
        final List<Integer> mList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            mList.add(i);

        }
        gridView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return mList.size();
            }

            @Override
            public Object getItem(int position) {
                return mList.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = LayoutInflater.from(CooperationPharmacyActivity.this).inflate(R.layout.gridview_item, null);
                return v;
            }
        });


    }


}
