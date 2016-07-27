package com.zzu.ehome.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.zzu.ehome.application.CustomApplcation;
import com.zzu.ehome.R;
import com.zzu.ehome.bean.NetEvent;
import com.zzu.ehome.bean.User;
import com.zzu.ehome.utils.NetUtils;
import com.zzu.ehome.utils.SystemStatusManager;
import com.zzu.ehome.utils.ToastUtils;
import com.zzu.ehome.view.CustomProgressDialog;
import com.zzu.ehome.view.DialogTips;
import com.zzu.ehome.view.HeadView;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.greenrobot.event.EventBus;

/**
 * Created by Mersens on 2016/3/31.
 */
public class BaseActivity  extends FragmentActivity{
    private int mScreenWidth;
    private int mScreenHeight;
    private Toast mToast;
    private Activity activity;
    private HeadView mHeadView;
    public EventBus eventbus;
    private static final String TAG = "BaseActivity";
    private CustomProgressDialog progressDialog = null;
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                 stopProgressDialog();
                    break;
            }
            super.handleMessage(msg);
        }
    };
    private BroadcastReceiver mBroadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            if(action.equals("action.loginout")){
                confirmExit();
            }
        }
    };
    public void confirmExit() {
        DialogTips dialog = new DialogTips(this, "", "是否退出软件？",
                "确定", true, true);
        dialog.SetOnSuccessListener(new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int userId) {
                CustomApplcation.getInstance().exit();
                finish();
            }
        });

        dialog.show();
        dialog = null;
    }
    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        //设置禁止横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setTranslucentStatus();
        CustomApplcation.getInstance().addActivity(this);
        activity=this;
        //获取手机屏幕的高度和宽度
        DisplayMetrics metrics=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mScreenHeight=metrics.heightPixels;
        mScreenWidth=metrics.widthPixels;
        eventbus=new EventBus();
        eventbus.register(this);
        if(!NetUtils.isNetworkConnected(this)){
            eventbus.post(new NetEvent(false));
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("action.loginout");
      registerReceiver(mBroadcastReceiver, intentFilter);
    }
    public void onEventMainThread(NetEvent event) {
        if(!event.isNet){
            ToastUtils.showMessage(BaseActivity.this,"没有网络，请检查网络连接！");
        }else{
            reload();
        }
    }

    public void reload(){}


    private void setTranslucentStatus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window win = getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            winParams.flags |= bits;
            win.setAttributes(winParams);
        }
        SystemStatusManager tintManager = new SystemStatusManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.actionbar_color);// 状态栏无背景
    }

    /**
     * @author Mersens
     * setDefaultViewMethod--默认显示左侧按钮，标题和右侧按钮
     * @param leftsrcid
     * @param title
     * @param rightsrcid
     * @param onleftclicklistener
     * @param onrightclicklistener
     */
    public void setDefaultViewMethod(int leftsrcid, String title, int rightsrcid, HeadView.OnLeftClickListener onleftclicklistener, HeadView.OnRightClickListener onrightclicklistener) {
        mHeadView=(HeadView) findViewById(R.id.common_actionbar);
        mHeadView.init(HeadView.HeaderStyle.DEFAULT);
        mHeadView.setDefaultViewMethod(leftsrcid,title,rightsrcid,onleftclicklistener,onrightclicklistener);
    }
    public void setDefaultTXViewMethod(int leftsrcid, String title, String rightsrcid, HeadView.OnLeftClickListener onleftclicklistener, HeadView.OnRightClickListener onrightclicklistener) {
        mHeadView=(HeadView) findViewById(R.id.common_actionbar);
        mHeadView.init(HeadView.HeaderStyle.DEFAULT_TX);
        mHeadView.setDefaultTXViewMethod(leftsrcid,title,rightsrcid,onleftclicklistener,onrightclicklistener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * @author Mersens
     * setRightAndTitleMethod--显示右侧按钮和标题
     * @param title
     * @param rightsrcid
     * @param onRightClickListener
     */
    public void setRightAndTitleMethod(String title,int rightsrcid, HeadView.OnRightClickListener onRightClickListener){
        mHeadView=(HeadView) findViewById(R.id.common_actionbar);
        mHeadView.init(HeadView.HeaderStyle.RIGHTANDTITLE);
        mHeadView.setRightAndTitleMethod(title, rightsrcid, onRightClickListener);
    }



    /**
     * @author Mersens
     * setLeftWithTitleViewMethod--显示左侧按钮和标题
     * @param leftsrcid
     * @param title
     * @param onleftclicklistener
     */
    public void setLeftWithTitleViewMethod(int leftsrcid,String title, HeadView.OnLeftClickListener onleftclicklistener){
        mHeadView=(HeadView) findViewById(R.id.common_actionbar);
        mHeadView.init(HeadView.HeaderStyle.LEFTANDTITLE);
        mHeadView.setLeftWithTitleViewMethod( leftsrcid, title,  onleftclicklistener);
    }

    /**
     * @author Mersens
     * setOnlyTileViewMethod--只显示标题
     * @param title
     */
    public void setOnlyTileViewMethod(String title) {
        mHeadView=(HeadView) findViewById(R.id.common_actionbar);
        mHeadView.init(HeadView.HeaderStyle.ONLYTITLE);
        mHeadView.setOnlyTileViewMethod(title);
    }

    /**
     * @author Mersens
     * setLeftViewMethod--只显示左侧按钮
     * @param leftsrcid
     * @param onleftclicklistener
     */
    public void setLeftViewMethod(int leftsrcid,HeadView.OnLeftClickListener onleftclicklistener) {
        mHeadView=(HeadView) findViewById(R.id.common_actionbar);
        mHeadView.init(HeadView.HeaderStyle.LEFT);
        mHeadView.setLeftViewMethod( leftsrcid, onleftclicklistener);
    }

    public void setRightText(String title){
        mHeadView.setRightText(title);
    }

    public String getRightText(){

        return mHeadView.getRightText();
    }

    /**
     * @author Mersens
     * Toast显示以字符串作为显示内容
     * @param text
     */
    public void ShowToast(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(activity, text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
        }
        mToast.show();
    }

    /**
     * @author Mersens
     * Toast显示参数为资源id作为显示内容
     * @param srcid
     */
    public void ShowToast(int srcid) {
        if (mToast == null) {
            mToast = Toast.makeText(activity, srcid, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(srcid);
        }
        mToast.show();
    }


    /**
     * @author Mersens
     * ͨ判断是否有网络连接
     * @return
     */
    public boolean isNetworkAvailable() {
        NetworkInfo info = getNetworkInfo(activity);
        if (info != null) {
            return info.isAvailable();
        }
        return false;
    }

    private static NetworkInfo getNetworkInfo(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }


    /**
     * @author Mersens
     * 获取屏幕的宽度
     * @return mScreenWidth
     */
    public int getScreenWidth(){
        return mScreenWidth;
    }

    /**
     * @author Mersens
     * 获取屏幕高度
     * @return mScreenHeight
     */
    public int getScreenHeight(){
        return mScreenHeight;
    }

    public int getHeadViewHeight(){
        return mHeadView.getHeadViewHeight();
    }
    /**
     * @author Mersens
     * @param context
     * @param cls
     */
    public <T> void intentAction(Activity context, Class<T> cls) {
        Intent intent = new Intent(context, cls);
        startActivity(intent);
    }



    public void finishActivity(){
        finish();
    }

    /**
     *
     * @Title: setHeadViewBg
     * @Description: 设置HeadView的背景颜色
     * @author Mersens
     * @param resid
     * @throws
     */
    public  void setHeadViewBg(int resid){
        mHeadView.setHeadViewBackground(resid);
    }

    /**
     *
     * @Title: setHeadViewTitleColor
     * @Description:设置HeadView的标题颜色
     * @author Mersens
     * @param resid
     * @throws
     */
    public  void setHeadViewTitleColor(int resid){
        mHeadView.setHeadViewTitleColor(resid);
    }

    public  int getStatusHeight() {
        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = activity.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }
    public void startProgressDialog(){
        if (progressDialog == null){
            progressDialog = CustomProgressDialog.createDialog(this);
            //progressDialog.setMessage("正在加载中...");
        }

        progressDialog.show();
        mHandler.sendEmptyMessageDelayed(0, 8000);
    }

    public void stopProgressDialog(){
        if (progressDialog != null){
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopProgressDialog();
        eventbus.unregister(this);
        try {
           unregisterReceiver(mBroadcastReceiver);

            mBroadcastReceiver = null;
        } catch (Exception e) {
        }
    }
}
