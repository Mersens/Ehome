package com.zzu.ehome.activity;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.igexin.sdk.PushManager;
import com.zzu.ehome.R;
import com.zzu.ehome.adapter.ECGAdapter;
import com.zzu.ehome.adapter.MedicalExaminationAdapter;
import com.zzu.ehome.bean.MedicalBean;
import com.zzu.ehome.bean.MedicalDate;
import com.zzu.ehome.bean.RefreshEvent;
import com.zzu.ehome.bean.TreatmentSearch;
import com.zzu.ehome.bean.User;
import com.zzu.ehome.bean.UserDate;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.JsonTools;
import com.zzu.ehome.utils.PermissionsChecker;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.view.DialogTips;
import com.zzu.ehome.view.HeadView;
import com.zzu.ehome.view.ListViewCompat;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;


/**
 * 体检报告
 * Created by Administrator on 2016/6/17.
 */
public class MedicalExaminationActivity extends BaseActivity implements View.OnClickListener{
    private static final String PACKAGE_URL_SCHEME = "package:";
    private PermissionsChecker mPermissionsChecker; // 权限检测器
    // 所需的全部权限
    static final String[] PERMISSIONS = new String[]{

            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private RequestMaker requestMaker;
    private TextView tvget,tv2;
    private ListView listView;
    private MedicalExaminationAdapter adapter;
    private List<MedicalBean> mList;
    private RelativeLayout ll_ECG_report,rlnodate;
    private String usrid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_examination);
        EventBus.getDefault().register(this);
        requestMaker=RequestMaker.getInstance();
        mPermissionsChecker = new PermissionsChecker(this);
        usrid= SharePreferenceUtil.getInstance(MedicalExaminationActivity.this).getUserId();
        initViews();
        initEnent();
    }

    private void initEnent() {
        tvget.setOnClickListener(this);
        ll_ECG_report.setOnClickListener(this);
        initDatas();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvget:
                break;
            case R.id.ll_ECG_report:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
                        showMissingPermissionDialog();
                        return;
                    }
                }

                startActivity( new Intent(MedicalExaminationActivity.this, CreateReportActivity.class));
                break;
        }

    }
    private void initViews() {
        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left,"体检报告", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();

            }
        });
        tvget=(TextView) findViewById(R.id.tvget);


        String spantxtyun = "1、请前往当地指定体检机构进行检测后查询:";
        SpannableString styleyun = new SpannableString (
                spantxtyun);
        int bstart = spantxtyun.indexOf("体检机构");
        int bend = bstart + ("体检机构").length();
        tvget.setTextColor(Color.parseColor("#757C86"));
        styleyun.setSpan(new TextAppearanceSpan(this, R.style.styleColor), bstart, bend, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvget.setText(styleyun);
        rlnodate=(RelativeLayout)findViewById(R.id.rlnodate);
        listView=(ListView)findViewById(R.id.listview);
        ll_ECG_report=(RelativeLayout)findViewById(R.id.ll_ECG_report);

    }
    public void initDatas(){
        requestMaker.MeidicalReportInquiry(usrid,new JsonAsyncTask_Info(MedicalExaminationActivity.this, true,new JsonAsyncTaskOnComplete(){
            @Override
            public void processJsonObject(Object result) {
                try {


                    String value=result.toString();
                    JSONObject mySO = (JSONObject) result;
                    org.json.JSONArray array = mySO
                            .getJSONArray("MeidicalReportInquiry");
                    if (array.getJSONObject(0).has("MessageCode")) {
                        rlnodate.setVisibility(View.VISIBLE);
                    } else {
                        rlnodate.setVisibility(View.GONE);
                        if(mList!=null&&mList.size()>0)
                            mList.clear();
                        MedicalDate date = JsonTools.getData(result.toString(), MedicalDate.class);
                        mList = date.getData();
                        if(adapter==null) {

                            adapter = new MedicalExaminationAdapter(MedicalExaminationActivity.this, mList);
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




    }
    public void onEventMainThread(RefreshEvent event) {
        if(getResources().getInteger(R.integer.refresh_report) == event
                .getRefreshWhere()) {

            initDatas();

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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
}
