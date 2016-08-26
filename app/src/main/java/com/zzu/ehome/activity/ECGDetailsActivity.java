package com.zzu.ehome.activity;

import android.Manifest;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.dinuscxj.progressbar.CircleProgressBar;
import com.zzu.ehome.R;
import com.zzu.ehome.application.Constants;
import com.zzu.ehome.bean.User;
import com.zzu.ehome.db.EHomeDao;
import com.zzu.ehome.db.EHomeDaoImpl;
import com.zzu.ehome.network.DownloadProgressListener;
import com.zzu.ehome.network.FileDownloader;
import com.zzu.ehome.utils.CommonUtils;
import com.zzu.ehome.utils.DateUtils;
import com.zzu.ehome.utils.PermissionsChecker;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.utils.ToastUtils;
import com.zzu.ehome.view.HeadView;
import java.io.File;

/**
 * Created by dell on 2016/6/17.
 */
public class ECGDetailsActivity extends BaseActivity  {

    private Intent mIntent;
    private String filename,filemd5;
    private TextView tvresult,tv_statu,tvtime,tvname;
    private String url="",status;
    long filesize;
    int result;
 private CircleProgressBar circleProgressBar;
    private TextView tvcontent;
    private Boolean isExists=false;
    private RelativeLayout rl_check;
    private User user;
    private EHomeDao dao;
    private PermissionsChecker mPermissionsChecker; // 权限检测器
    // 所需的全部权限
    static final String[] PERMISSIONS = new String[]{

            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    public static final String SDCARD_PATH = Environment.getExternalStorageDirectory().toString();
    public  final String DOWM_FOLDER = SDCARD_PATH + File.separator + "ehome" + File.separator + "download" + File.separator;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    float num = (float)msg.getData().getInt("size")/(float) filesize;
                    result = (int)(num*100);
                    circleProgressBar.setProgress(result);
                    circleProgressBar.setVisibility(View.VISIBLE);
                    //显示下载成功信息
                    if(circleProgressBar.getProgress()==circleProgressBar.getMax()){
                        ToastUtils.showMessage(ECGDetailsActivity.this, "下载成功"+"请到"+DOWM_FOLDER+"查看");
//                      openFolder();
                        openFolder(DOWM_FOLDER+filename);
                        circleProgressBar.setVisibility(View.GONE);
                        tvcontent.setText("打开文件");
                        isExists=true;
                    }
                    break;
                case -1:
                    //显示下载错误信息
                    ToastUtils.showMessage(ECGDetailsActivity.this, "下载失败!");
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.layout_dynamic_detail);
        dao = new EHomeDaoImpl(this);
        mIntent=this.getIntent();

        filemd5=mIntent.getStringExtra("FileMD5");
        status=mIntent.getStringExtra("ReportType");
       user=dao.findUserInfoById(SharePreferenceUtil.getInstance(ECGDetailsActivity.this).getUserId());

        url= (Constants.Download+mIntent.getStringExtra("Download")).replace("\\", "/");

        String temp[] = url.replaceAll("////","/").split("/");
        if (temp.length > 1) {
            filename = temp[temp.length - 1];
        }
        initViews();
        tv_statu.setText(status);
        tvtime.setText(mIntent.getStringExtra("time"));
        tvname.setText(user.getUsername());
        if(status.contains("低")){
            tv_statu.setTextColor(Color.parseColor("#00c07d"));
        }else if(status.contains("中")){
            tv_statu.setTextColor(Color.parseColor("#fb9c2e"));
        }else if(status.contains("高")){
            tv_statu.setTextColor(Color.parseColor("#f95935"));
        }
        circleProgressBar.setVisibility(View.GONE);
        File file=new File(DOWM_FOLDER+filename);
        if(file.exists()){
            if(filemd5.equals(CommonUtils.getFileMD5(file))) {
                tvcontent.setText("打开详细报告");
                isExists = true;
            }else{
                tvcontent.setText("下载详细报告内容");
            }
        }else{
            tvcontent.setText("下载详细报告内容");
        }
        initEvent();
        initDatas();
    }
    public void  initViews(){

        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "动态心电报告", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();
            }
        });
        tvresult=(TextView) findViewById(R.id.tvresult);
        tvresult.setText(mIntent.getStringExtra("Result"));
        rl_check=(RelativeLayout) findViewById(R.id.rl_check);
        circleProgressBar=(CircleProgressBar)findViewById(R.id.line_progress);
        tvcontent=(TextView)findViewById(R.id.tvcontent);
        tv_statu=(TextView)findViewById(R.id.tv_statu);
        tvname=(TextView)findViewById(R.id.tvname);
        tvtime=(TextView)findViewById(R.id.tvtime);




        rl_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isExists){
                    openFolder(DOWM_FOLDER+filename);
                }else {
                    if (result > 0) {
                        return;
                    }
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        //开始下载文件
                        download(url, new File(DOWM_FOLDER));
                    } else {
                        //显示SDCard错误信息
                        ToastUtils.showMessage(ECGDetailsActivity.this, "读取失败!");
                    }
                }
            }
        });
    }
    public void initEvent(){
    }

    public void initDatas(){

    }
    /**
     * 主线程(UI线程)
     * 对于显示控件的界面更新只是由UI线程负责，如果是在非UI线程更新控件的属性值，更新后的显示界面不会反映到屏幕上
     * 如果想让更新后的显示界面反映到屏幕上，需要用Handler设置。
     * @param path
     * @param savedir
     */
    private void download(final String path, final File savedir) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                FileDownloader loader = new FileDownloader(ECGDetailsActivity.this,path,savedir,4);
                filesize=loader.getFileSize();
                circleProgressBar.setMax(100);//设置进度条的最大刻度为文件的长度
                    loader.download(new DownloadProgressListener() {
                        @Override
                        public void onDownloadSize(int size) {//实时获知文件已经下载的数据长度
                            Message msg = new Message();
                            msg.what = 1;
                            msg.getData().putInt("size", size);
                            handler.sendMessage(msg);//发送消息
                        }
                    });
                } catch (Exception e) {
                    handler.obtainMessage(-1).sendToTarget();
                }
            }
        }).start();
    }
    public void openFolder(String filename)
    {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new
                File(filename));
        intent.setDataAndType (uri, "application/pdf");
        this.startActivity(intent);
    }
}
