package com.zzu.ehome.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.zzu.ehome.R;
import com.zzu.ehome.service.DownloadServiceForAPK;
import com.zzu.ehome.utils.DialogUtils;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.ToastUtils;
import com.zzu.ehome.view.DialogEnsureCancelView;
import com.zzu.ehome.view.HeadView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by zzu on 2016/7/15.
 */
public class AboutEhomeActivity extends BaseActivity {
    private final String mPageName = "AboutEhomeActivity";
    private String versionlog;
/*    private RelativeLayout layout_version_update,layout_xietong;*/
    private RequestMaker requestMaker;
    private int oldCode;
    private int newCode;
    String appVersion;
    private TextView tvapp;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        requestMaker=RequestMaker.getInstance();
        setContentView(R.layout.activity_about);
        try {
        PackageManager manager = AboutEhomeActivity.this
                .getPackageManager();
        PackageInfo info = manager.getPackageInfo(
                AboutEhomeActivity.this.getPackageName(), 0);
        appVersion = info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        initViews();
        initEvents();
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
    private void versioninquiry() {
        requestMaker.updaateApk(new JsonAsyncTask_Info(
                AboutEhomeActivity.this, true, new JsonAsyncTaskOnComplete(){
            public void processJsonObject(Object result){
                Map<String, Object> map;

                try {
                    JSONObject mySO = (JSONObject) result;
                    JSONArray array = mySO
                            .getJSONArray("VersionInquiry");
                    if (array.getJSONObject(0).has("MessageCode")) {
                        ToastUtils.showMessage(AboutEhomeActivity.this,"已经是最新版本！");

                    } else {
                        for (int i = 0; i < array.length(); i++) {
                            newCode = Integer.valueOf(array
                                    .getJSONObject(i).getString(
                                            "VersionID"));
                            int VersionFlag = Integer.valueOf(array
                                    .getJSONObject(i).getString(
                                            "VersionFlag"));

                            PackageManager manager = AboutEhomeActivity.this
                                    .getPackageManager();
                            PackageInfo info = manager.getPackageInfo(
                                    AboutEhomeActivity.this.getPackageName(), 0);
                            String appVersion = info.versionName; // 版本名
                            oldCode = info.versionCode; // 版本号
                            if (oldCode < newCode) {
                                versionlog = array.getJSONObject(i)
                                        .getString("VersionLog")
                                        .replace("@", "\n");
                                    showUpdateDialog();
                            }
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }));

    }
    private void showUpdateDialog() {
        DialogEnsureCancelView dialogEnsureCancelView = new DialogEnsureCancelView(
                AboutEhomeActivity.this).setDialogMsg("检测到新版本", versionlog, "下载")
                .setOnClickListenerEnsure(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        final Intent it = new Intent(AboutEhomeActivity.this,
                                DownloadServiceForAPK.class);
                        startService(it);

                    }
                });
        DialogUtils.showSelfDialog(AboutEhomeActivity.this, dialogEnsureCancelView);


    }
    public void initViews() {

        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "关于健康E家", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();
            }
        });
        tvapp= (TextView)findViewById(R.id.tvver);
        tvapp.setText("v"+appVersion);
    }

    public void initEvents() {

    }
}
