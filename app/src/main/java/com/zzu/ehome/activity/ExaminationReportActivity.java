package com.zzu.ehome.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.zzu.ehome.R;
import com.zzu.ehome.adapter.MedicalExaminationAdapter;
import com.zzu.ehome.bean.MedicalBean;
import com.zzu.ehome.bean.MedicalDate;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.JsonTools;
import com.zzu.ehome.utils.PermissionsChecker;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.view.DialogTips;
import com.zzu.ehome.view.HeadView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mersens on 2016/8/20.
 */
public class ExaminationReportActivity extends BaseActivity {
    private String userid;
    private ListView listView;
    private LinearLayout layout_none;
    private static final String PACKAGE_URL_SCHEME = "package:";
    private PermissionsChecker mPermissionsChecker; // 权限检测器
    // 所需的全部权限
    static final String[] PERMISSIONS = new String[]{

            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private RequestMaker requestMaker;
    private List<MedicalBean> mList;
    private MedicalExaminationAdapter adapter;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.layout_examination_report);
        requestMaker=RequestMaker.getInstance();
        mPermissionsChecker = new PermissionsChecker(this);
        initViews();
        initEvent();
        initDatas();
    }

    public void initViews(){
        listView=(ListView)findViewById(R.id.listView);
        layout_none=(LinearLayout) findViewById(R.id.layout_none);
        setDefaultViewMethod(R.mipmap.icon_arrow_left, "体检报告", R.mipmap.icon_add_report, new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();

            }
        }, new HeadView.OnRightClickListener() {
            @Override
            public void onClick() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
                        showMissingPermissionDialog();
                        return;
                    }
                }

                startActivity( new Intent(ExaminationReportActivity.this, CreateReportActivity.class));
//                startActivity(new Intent(ExaminationReportActivity.this,AddExaminationReportActivity.class));
            }
        });

    }

    public void initEvent(){


    }
    // 显示缺失权限提示
    private void showMissingPermissionDialog() {
        DialogTips dialog = new DialogTips(this, "请点击设置，打开所需存储权限",
                "确定");
        dialog.SetOnSuccessListener(new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int userId) {
                startAppSettings();

            }
        });

        dialog.show();
        dialog = null;

    }
    // 启动应用的设置
    private void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse(PACKAGE_URL_SCHEME + getPackageName()));
        startActivity(intent);
    }

    public void initDatas(){
        userid= SharePreferenceUtil.getInstance(ExaminationReportActivity.this).getUserId();
        requestMaker.MeidicalReportInquiry(userid,new JsonAsyncTask_Info(ExaminationReportActivity.this, true,new JsonAsyncTaskOnComplete(){
            @Override
            public void processJsonObject(Object result) {
                try {


                    String value=result.toString();
                    JSONObject mySO = (JSONObject) result;
                    org.json.JSONArray array = mySO
                            .getJSONArray("MeidicalReportInquiry");
                    if (array.getJSONObject(0).has("MessageCode")) {

                    } else {

                        if(mList!=null&&mList.size()>0)
                            mList.clear();
                        MedicalDate date = JsonTools.getData(result.toString(), MedicalDate.class);
                        mList = date.getData();
                        if(adapter==null) {

                            adapter = new MedicalExaminationAdapter(ExaminationReportActivity.this, mList);
                            listView.setAdapter(adapter);
                        }else{
                            adapter.setmList(mList);
                            listView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }));

        final List<String> list=new ArrayList<>();
        for(int i=0;i<5;i++){
            list.add(i+"");
        }
        listView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public Object getItem(int position) {
                return list.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v= LayoutInflater.from(ExaminationReportActivity.this).inflate(R.layout.examination_report_item,null);
                return v;
            }
        });

    }

}
