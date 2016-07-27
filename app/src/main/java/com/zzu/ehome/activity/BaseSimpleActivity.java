package com.zzu.ehome.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.zzu.ehome.application.CustomApplcation;
import com.zzu.ehome.R;
import com.zzu.ehome.utils.SystemStatusManager;
import com.zzu.ehome.view.HeadView;

/**
 * Created by Administrator on 2016/4/9.
 */
public class BaseSimpleActivity extends FragmentActivity {
    private int mScreenWidth;
    private int mScreenHeight;
    private Toast mToast;
    private Activity activity;
    private HeadView mHeadView;
    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        //设置禁止横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        setTranslucentStatus();
        CustomApplcation.getInstance().addActivity(this);
        activity=this;
        //获取手机屏幕的高度和宽度
        DisplayMetrics metrics=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mScreenHeight=metrics.heightPixels;
        mScreenWidth=metrics.widthPixels;
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
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        // TODO Auto-generated method stub
        super.onActivityResult(arg0, arg1, arg2);
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

}
