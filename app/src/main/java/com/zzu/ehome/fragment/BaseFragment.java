package com.zzu.ehome.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.zzu.ehome.R;
import com.zzu.ehome.application.CustomApplcation;
import com.zzu.ehome.bean.NetEvent;
import com.zzu.ehome.main.ehome.MainActivity;
import com.zzu.ehome.utils.NetUtils;
import com.zzu.ehome.utils.SystemStatusManager;
import com.zzu.ehome.utils.ToastUtils;
import com.zzu.ehome.view.CustomProgressDialog;
import com.zzu.ehome.view.DialogTips;
import com.zzu.ehome.view.HeadView;

import de.greenrobot.event.EventBus;

/**
 * Created by zzu on 2016/3/31.
 */
public abstract class BaseFragment extends Fragment {
    private HeadView mHeadView;
    private CustomProgressDialog progressDialog = null;
    protected boolean isVisible;
    private EventBus eventbus;
    private BroadcastReceiver mBroadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            if(action.equals("action.loginout")){
                confirmExit();
            }
        }
    };

/*    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if(getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }*/
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventbus=new EventBus();
        eventbus.register(this);
        if(!NetUtils.isNetworkConnected(getActivity())){
            eventbus.post(new NetEvent(false));
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("action.loginout");
        getActivity().registerReceiver(mBroadcastReceiver, intentFilter);
    }

    public void onEventMainThread(NetEvent event) {
        if(!event.isNet){
            ToastUtils.showMessage(getActivity(),"没有网络，请检查网络连接！");
        }else{
            reload();
        }
    }

    public void reload(){

    }
    public void confirmExit() {
        DialogTips dialog = new DialogTips(getActivity(), "", "是否退出软件？",
                "确定", true, true);
        dialog.SetOnSuccessListener(new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int userId) {
                CustomApplcation.getInstance().exit();
                getActivity().finish();
            }
        });

        dialog.show();
        dialog = null;
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
    public void setDefaultViewMethod(View v,int leftsrcid, String title, int rightsrcid, HeadView.OnLeftClickListener onleftclicklistener, HeadView.OnRightClickListener onrightclicklistener) {
        mHeadView=(HeadView) v.findViewById(R.id.common_actionbar);
        mHeadView.init(HeadView.HeaderStyle.DEFAULT);
        mHeadView.setDefaultViewMethod(leftsrcid,title,rightsrcid,onleftclicklistener,onrightclicklistener);
    }
    public void setDefaultTXViewMethod(View v,int leftsrcid, String title, String rightsrcid, HeadView.OnLeftClickListener onleftclicklistener, HeadView.OnRightClickListener onrightclicklistener) {
        mHeadView=(HeadView) v.findViewById(R.id.common_actionbar);
        mHeadView.init(HeadView.HeaderStyle.DEFAULT_TX);
        mHeadView.setDefaultTXViewMethod(leftsrcid,title,rightsrcid,onleftclicklistener,onrightclicklistener);
    }
    /**
     * @author Mersens
     * setRightAndTitleMethod--显示右侧按钮和标题
     * @param title
     * @param rightsrcid
     * @param onRightClickListener
     */
    public void setRightAndTitleMethod(View v,String title,int rightsrcid, HeadView.OnRightClickListener onRightClickListener){
        mHeadView=(HeadView) v.findViewById(R.id.common_actionbar);
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
    public void setLeftWithTitleViewMethod(View v,int leftsrcid,String title, HeadView.OnLeftClickListener onleftclicklistener){
        mHeadView=(HeadView) v.findViewById(R.id.common_actionbar);
        mHeadView.init(HeadView.HeaderStyle.LEFTANDTITLE);
        mHeadView.setLeftWithTitleViewMethod( leftsrcid, title,  onleftclicklistener);
    }

    /**
     * @author Mersens
     * setOnlyTileViewMethod--只显示标题
     * @param title
     */
    public void setOnlyTileViewMethod(View v,String title) {
        mHeadView=(HeadView) v.findViewById(R.id.common_actionbar);
        mHeadView.init(HeadView.HeaderStyle.ONLYTITLE);
        mHeadView.setOnlyTileViewMethod(title);
    }

    /**
     * @author Mersens
     * setLeftViewMethod--只显示左侧按钮
     * @param leftsrcid
     * @param onleftclicklistener
     */
    public void setLeftViewMethod(View v,int leftsrcid,HeadView.OnLeftClickListener onleftclicklistener) {
        mHeadView=(HeadView) v.findViewById(R.id.common_actionbar);
        mHeadView.init(HeadView.HeaderStyle.LEFT);
        mHeadView.setLeftViewMethod( leftsrcid, onleftclicklistener);
    }

    public  void setHeadViewBackground(int resid){
        if(resid!=0 && mHeadView!=null){
            mHeadView.setHeadViewBackground(resid);
        }
    }

    /**
     *
     * @Title: setHeadViewTitleColor
     * @Description:设置HeadView的标题颜色
     * @author Mersens
     * @param resid
     * @throws
     */
    public void setTitleColor(int resid){
        if(resid!=0 && mHeadView!=null){
            mHeadView.setHeadViewTitleColor(resid);
        }

    }
    public void startProgressDialog(){
        if (progressDialog == null){
            progressDialog = CustomProgressDialog.createDialog(getActivity());
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
    public void onDestroy() {
        super.onDestroy();
        stopProgressDialog();
        eventbus.unregister(this);
        try {
            getActivity().unregisterReceiver(mBroadcastReceiver);

            mBroadcastReceiver = null;
        } catch (Exception e) {
        }
    }
    /**
     * 可见
     */
    protected void onVisible() {
        lazyLoad();
    }


    /**
     * 不可见
     */
    protected void onInvisible() {


    }
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

    /**
     * 延迟加载
     * 子类必须重写此方法
     */
    protected abstract void lazyLoad();
}
