package com.zzu.ehome.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zzu.ehome.R;
import com.zzu.ehome.view.DialogTips;
import com.zzu.ehome.view.HeadView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mersens on 2016/8/23.
 * 检查报告
 */
public class InspectionReportActivity extends BaseActivity {
    private ListView listView;
    private LinearLayout layout_none;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.layout_inspection_report);
        initViews();
        initEvent();
        initDatas();
    }


    public void initViews(){
        setDefaultViewMethod(R.mipmap.icon_arrow_left, "检查报告", R.drawable.icon_ocr, new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();

            }
        }, new HeadView.OnRightClickListener() {
            @Override
            public void onClick() {

            }
        });

        listView=(ListView)findViewById(R.id.listView);
        layout_none=(LinearLayout)findViewById(R.id.layout_none);
        layout_none.setVisibility(View.GONE);

    }

    public void initEvent(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               startActivity(new Intent(InspectionReportActivity.this,InspectionReportDetailActivity.class));
            }
        });

    }

    public void initDatas(){
        final List<String> mList=new ArrayList<>();
        mList.add("血糖");
        mList.add("血常规");
        mList.add("尿常规");
        mList.add("肝功能");
        mList.add("肾功能");
        mList.add("生化");
        mList.add("血常规");
        listView.setAdapter(new BaseAdapter() {
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
                View v= LayoutInflater.from(InspectionReportActivity.this).inflate(R.layout.dynamic_item,null);
                TextView tv_title=(TextView)findViewById(R.id.tv_title);
                tv_title.setText(mList.get(position));
                return v;
            }
        });

    }


    /**
     * 如果用户信息不完善，显示提示框
     */
    public void completeInfoTips(){
        DialogTips dialog = new DialogTips(this, "", "就诊信息缺失，请先完善信息",
                "去完善", true, true);
        dialog.SetOnSuccessListener(new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int userId) {
                startActivity(new Intent(InspectionReportActivity.this,ComplateUserInfoActivity.class));

            }
        });

        dialog.show();
        dialog = null;
    }
}
